USE `manager`;
DROP procedure IF EXISTS `pr_document_movement_rollback`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_document_movement_rollback`(IN _document_parent_id BIGINT, IN _document_id BIGINT, IN _user_change BIGINT)
	BEGIN    
		/**Create global variables*/
		DECLARE _document_transaction_id  BIGINT;
       	DECLARE _company_id BIGINT;
		DECLARE _document_status INT;
		DECLARE _document_type INT;
		DECLARE _document_value DECIMAL(19,4);
		DECLARE _bank_account_id INT;
		DECLARE _bank_account_origin_id INT;
		DECLARE _provider_id BIGINT;
		DECLARE _credit BIT;
		DECLARE _document_note VARCHAR(1000);
		DECLARE _inactive BIT;
        DECLARE _payment_data DATETIME;
    
		DECLARE _timezone_database TINYINT DEFAULT 0;
		DECLARE _processing_date_time DATETIME DEFAULT NOW();
		DECLARE _message VARCHAR(2000);
		DECLARE _step_process INT DEFAULT 1;        
		/**End Create global variables*/
        
		DECLARE EXIT HANDLER FOR SQLEXCEPTION
		BEGIN
			GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
			@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
			SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
			
			INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
			SELECT 'DOCUMENT ROLLBACK', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
		END;
        
		/**Drop temporary tables*/
		DROP TEMPORARY TABLE IF EXISTS tmp_filter;
		/**End Drop temporary tables*/

		SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
		SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
		/**End Set global variables */    
        
        
		/**Temporary table to insert bank_account_filter*/
		CREATE TEMPORARY TABLE IF NOT EXISTS tmp_filter
		(
			document_parent_id BIGINT,
			document_id BIGINT,
			company_id BIGINT,
			document_status INT,
			document_type INT,
			document_value DECIMAL(19,4),
			bank_account_id INT,
			bank_account_origin_id INT,
			provider_id BIGINT,
			credit BIT,
			document_note VARCHAR(1000),
			inactive BIT,
            payment_data DATETIME
		);
        
		SELECT document_transaction_id INTO _document_transaction_id FROM document_movement WHERE document_parent_id = _document_parent_id AND document_id = _document_id LIMIT 1;
		
		INSERT INTO tmp_filter(document_parent_id, document_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data)
		SELECT document_parent_id, document_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data
		FROM document_movement WHERE document_parent_id = _document_parent_id AND document_id = _document_id LIMIT 1;
                
		IF(IFNULL(_document_transaction_id,0) > 0) THEN
			BEGIN
				INSERT INTO tmp_filter(document_parent_id, document_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data)
				SELECT document_parent_id, document_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data
				FROM document_movement WHERE document_transaction_id = _document_transaction_id AND document_parent_id <> _document_parent_id AND document_id <> _document_id;
			END;
		END IF;
        
							
		/** Step 1 - Get documents movement to rollback */
		IF(1=_step_process) THEN
			BEGIN
				/**VARIABLES CURSOR*/
                DECLARE _has_movements_pending_process BIT DEFAULT 0;
				DECLARE _has_movements BIT DEFAULT 0;
                
                /**Cursor to search documents*/
				DECLARE documents_cursor CURSOR FOR
				SELECT
					document_parent_id, document_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data
				FROM tmp_filter;

				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
				OPEN documents_cursor;
				meuLoop: LOOP
				FETCH documents_cursor INTO 
                _document_parent_id, _document_id, _company_id, _document_status, _document_type, _document_value, _bank_account_id, _bank_account_origin_id, _provider_id, _credit, _document_note, _inactive, _payment_data;
                
				IF _has_movements = 1 THEN
					LEAVE meuLoop;
				END IF;
                
                /**Has movements pending to process*/
                SET _has_movements_pending_process = 1;
                
				/**Update document movement inactive */
				UPDATE 
					document_movement 
				SET inactive = (CASE WHEN _inactive = 1 THEN 0 ELSE 1 END), user_change = _user_change, change_date = _processing_date_time
				WHERE document_parent_id = _document_parent_id AND document_id = _document_id; 
				
				/**Verify document status*/
				IF(2 = _document_status) THEN /**If document close, return movement and create extrato operation*/
					BEGIN  
					
					   /**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						SET ba.bank_balance_available = (ba.bank_balance_available + (CASE WHEN (_inactive = 1 AND _credit = 0) THEN (_document_value * -1)  WHEN (_inactive = 1 AND _credit = 1) THEN _document_value WHEN (_inactive = 0 AND _credit = 0) THEN _document_value  ELSE (_document_value * -1) END)),
						user_change = _user_change, change_date = _processing_date_time
						WHERE ba.id = _bank_account_id AND ba.accumulate_balance = 1;
						
						INSERT INTO bank_account_statement(bank_account_id, bank_account_origin_id, launch_type, provider_id, person_id, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)
						SELECT _bank_account_id, _bank_account_origin_id, _document_type, _provider_id, 
						(SELECT person_id FROM company WHERE id = _company_id 	LIMIT 1) person_id, 
						_document_parent_id, _document_id, 
						(CASE WHEN (_inactive = 1 AND _credit = 0) THEN (_document_value * -1)  WHEN (_inactive = 1 AND _credit = 1) THEN _document_value WHEN (_inactive = 0 AND _credit = 0) THEN _document_value  ELSE (_document_value * -1) END), 
						CONCAT('EXTORNO >> ', _document_note), 
						 0 bank_balance_available, 0 document_inactive, _user_change, _processing_date_time, _user_change, _payment_data;
						 
					END;
				END IF;
                
			  END LOOP meuLoop;
			  CLOSE documents_cursor;              			  
            END; /** Step 1 - Get documents movement to rollback */
		END IF;        
    END$$
DELIMITER ;

/*
START TRANSACTION;
SET SQL_SAFE_UPDATES = 0;
	
CALL pr_document_movement_rollback(291716, 1052633, 7);
	
commit;
ROLLBACK;
*/

