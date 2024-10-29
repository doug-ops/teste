USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_closing_save_launch`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_save_launch`(IN _operation TINYINT, 
												  IN _id BIGINT,
												  IN _cashier_closing_id BIGINT,
                                                  IN _user_operator BIGINT,
												  IN _company_id BIGINT, 
												  IN _bank_account_id INT, 
												  IN _provider_id BIGINT,
												  IN _financial_group_id VARCHAR(7),
												  IN _financial_sub_group_id VARCHAR(7),    
												  IN _credit BIT,
												  IN _expense BIT,
                                                  IN _payment_data DATETIME,
												  IN _document_status TINYINT,
												  IN _document_value DECIMAL(19,2),
												  IN _document_note VARCHAR(1000),
												  IN _user_change BIGINT,
												  IN _inactive BIT)                                                   
BEGIN	
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _cashing_close_status INT DEFAULT 0;
    DECLARE _validation_message VARCHAR(5000) DEFAULT '';
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING SAVE LAUNCH', @full_error, 'emarianodeveloper@gmail.com', NOW(), 0, NOW(), 0;			
        
		SELECT @full_error;
	END;

	SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	IF(1 = _operation) THEN /** Operation 1 */
		BEGIN            
        
			IF(IFNULL(_company_id,0) = 0) THEN
				BEGIN
					SELECT IFNULL(
						(
							SELECT 
								x.company_id
							FROM
							(
								SELECT 
								c.id company_id 
								FROM person p
								INNER JOIN company c ON p.id = c.person_id
								WHERE 
								p.bank_account_id = _bank_account_id
								UNION ALL 
								SELECT 
								c.id company_id 
								FROM 
								person_bank_account pb
								INNER JOIN company c ON pb.person_id = c.person_id
								WHERE 
								pb.bank_account_id = _bank_account_id
							) x LIMIT 1
                        ), 
					0) INTO _company_id;
                END;
            END IF;
            
            IF(IFNULL(_id,0) > 0) THEN
				BEGIN
                
					UPDATE cashier_closing_company_launch SET 
						   cashier_closing_id = _cashier_closing_id,                           
                           cashier_closing_status = 1,
                           user_operator = _user_operator,
						   company_id = _company_id, 
						   bank_account_id = _bank_account_id,
						   provider_id = _provider_id,
						   financial_group_id = _financial_group_id,
						   financial_sub_group_id = _financial_sub_group_id,
						   credit = _credit,
						   expense = _expense,
						   document_status = _document_status,
						   payment_data = _payment_data,
						   document_value = _document_value,
						   document_note = _document_note,
						   user_change = _user_change,
						   change_date = _processing_date_time,
						   launch_user = _user_change,
						   launch_date = _processing_date_time,
                           inactive = _inactive
					WHERE
						id = _id;           
                        
                END;
			ELSE 
				BEGIN
					INSERT INTO cashier_closing_company_launch
                    (cashier_closing_id, cashier_closing_status, user_operator, company_id, bank_account_id, provider_id, financial_group_id, financial_sub_group_id,
					 credit, expense, document_status, payment_data, document_value, document_note, user_creation, creation_date,
					 user_change, change_date, launch_user, launch_date, inactive)
					SELECT 
                    _cashier_closing_id, 1 cashier_closing_status, _user_operator, _company_id, _bank_account_id, _provider_id, _financial_group_id, _financial_sub_group_id,
					_credit, _expense, _document_status, _payment_data, _document_value, _document_note, _user_change, _processing_date_time,
					_user_change, _processing_date_time, _user_change, _processing_date_time, _inactive;
                END;
			END IF;
            
        END; /** End Operation 1 */
	ELSEIF(2 = _operation) THEN /** Operation 2 */
		BEGIN            
			
            UPDATE 
				cashier_closing_company_launch 
			SET 
				close_user = _user_change,
				close_date = _processing_date_time, 
                cashier_closing_status = 2
			WHERE
				id = _id;
            
        END; /** End Operation 2 */
    END IF;
END$$

/**
  IN _operation TINYINT, 
  IN _id BIGINT,
  IN _cashier_closing_id BIGINT,
  IN _user_operator BIGINT,
  IN _company_id BIGINT, 
  IN _bank_account_id INT, 
  IN _provider_id BIGINT,
  IN _financial_group_id VARCHAR(7),
  IN _financial_sub_group_id VARCHAR(7),    
  IN _credit BIT,
  IN _expense BIT,
  IN _payment_data DATETIME,
  IN _document_status TINYINT,
  IN _document_value DECIMAL(19,2),
  IN _document_note VARCHAR(1000),
  IN _user_change BIGINT,
  IN _inactive BIT
*/
