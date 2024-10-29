USE `manager`;
DROP FUNCTION IF EXISTS `func_rollbak_document_movement`;
USE `manager`$$
DELIMITER $$
CREATE FUNCTION `func_rollbak_document_movement`(_document_parent_id BIGINT, _document_id BIGINT, _user_change BIGINT)
RETURNS BIT
BEGIN

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
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
        
	SELECT
		document_transaction_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive 
	INTO _document_transaction_id, _company_id, _document_status, _document_type, _document_value, _bank_account_id, _bank_account_origin_id, _provider_id, _credit, _document_note, _inactive
	FROM document_movement WHERE document_parent_id = _document_parent_id AND document_id = _document_id LIMIT 1;
		
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
			 0 bank_balance_available, 0 document_inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
			 
		END;
	END IF;
    
	RETURN 1;
END$$
DELIMITER ;
-- SELECT func_rollbak_document_movement(284020, 1029448, 1);

-- SET GLOBAL log_bin_trust_function_creators = 1;