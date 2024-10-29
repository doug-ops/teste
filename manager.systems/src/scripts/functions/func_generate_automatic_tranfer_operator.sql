USE `manager`;
DROP FUNCTION IF EXISTS `func_generate_automatic_tranfer_operator`;
USE `manager`$$
DELIMITER $$
CREATE FUNCTION `func_generate_automatic_tranfer_operator`(_document_parent_principal_id BIGINT, _bank_account_automatic_transfer_id INT)
RETURNS BIT
BEGIN

	DECLARE _user BIGINT DEFAULT 1;
    DECLARE _product_id BIGINT DEFAULT NULL;
    DECLARE _product_description VARCHAR(100) DEFAULT NULL;
	DECLARE _document_value_transfer DECIMAL(19,4);
    DECLARE _bank_account_origin_id INT;
    DECLARE _company_id BIGINT;
    DECLARE _company_description VARCHAR(100);
    DECLARE _person_id_origin BIGINT;
    DECLARE _document_parent_id_transfer BIGINT;
    DECLARE _document_id_transfer BIGINT;
    DECLARE _debit_provider_id_transfer BIGINT;
    DECLARE _company_id_transfer BIGINT;
    DECLARE _company_description_transfer VARCHAR(100);
    DECLARE _person_id_transfer BIGINT;
    DECLARE _credit_provider_id_transfer BIGINT;
    DECLARE _financial_group_id_transfer VARCHAR(7);
    DECLARE _financial_subgroup_id_transfer VARCHAR(7);
    DECLARE _processing_date_time DATE;
    DECLARE _validation_message  VARCHAR(100);
    
			/** Automatic Transfer Configured */
               IF(IFNULL(_bank_account_automatic_transfer_id,0)>0) THEN
			   BEGIN                  
					SELECT IFNULL(SUM(CASE WHEN credit THEN document_value ELSE (document_value*-1) END),0) INTO _document_value_transfer FROM document_movement WHERE document_parent_id = _document_parent_principal_id;
                    SELECT MAX(payment_data), MAX(bank_account_id), MAX(company_id) INTO _processing_date_time, _bank_account_origin_id, _company_id FROM document_movement WHERE document_parent_id = _document_parent_principal_id limit 1;
					SELECT social_name, person_id INTO _company_description, _person_id_origin FROM company WHERE id = _company_id LIMIT 1;                    
    
                    IF(IFNULL(_document_value_transfer,0)<>0) THEN
						BEGIN
							INSERT INTO document_parent_generate_id(inactive) select 0;
							SET _document_parent_id_transfer = LAST_INSERT_ID();                            
							
							INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id_transfer = LAST_INSERT_ID();
                            
   							SELECT 
								IFNULL(p.debit_provider_id, 0)
							INTO _debit_provider_id_transfer FROM
								company c
									INNER JOIN
								person p ON c.person_id = p.id
							WHERE
								p.bank_account_id = _bank_account_origin_id
							LIMIT 1;
                            
   							SELECT 
								c.id, c.social_name, p.id, IFNULL(p.credit_provider_id, 0)
							INTO _company_id_transfer , _company_description_transfer , _person_id_transfer , _credit_provider_id_transfer 
                            FROM
								company c
									INNER JOIN
								person p ON c.person_id = p.id
							WHERE
								p.bank_account_id = _bank_account_automatic_transfer_id
							LIMIT 1;
                            
							SELECT 
								IFNULL(financial_group_id, ''),
								IFNULL(financial_sub_group_id, '')
							INTO _financial_group_id_transfer , _financial_subgroup_id_transfer FROM
								provider
							WHERE
								id = _debit_provider_id_transfer;
                            
   							INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_number, document_value,
							document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, 
							nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, inactive, user_creation, creation_date, user_change, change_date, product_id, product_description, is_automatic_transfer, document_id_origin)
							SELECT _document_id_transfer, _document_parent_id_transfer, 2 AS document_type, (case when _document_value_transfer < 0 then 1 else 0 end) AS credit, _company_id, _debit_provider_id_transfer, _bank_account_origin_id, _bank_account_automatic_transfer_id, 
							_document_id_transfer AS document_number, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS document_value, CONCAT('TRANSF. MOVIMENTO:',_document_parent_principal_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							2 AS document_status, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, _processing_date_time, 
							_processing_date_time AS payment_expiry_data, _user AS payment_user, 0 AS nfe_number, 0 AS nfe_serie_number, 0 AS generate_billet, 
							'22.000' _financial_group_id_transfer, '22.001' _financial_subgroup_id_transfer, 0 _is_product_movement, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date, _product_id, _product_description, 1 _is_automatic_transfer, _document_parent_principal_id;
    
							/**Insert bank account statement*/
							INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)  
							SELECT _person_id_origin, _bank_account_origin_id, _bank_account_automatic_transfer_id, _debit_provider_id_transfer, 2 _launch_type, _document_parent_id_transfer, _document_id_transfer, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end), CONCAT('TRANSF. MOVIMENTO:',_document_parent_principal_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							((SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_origin_id LIMIT 1)+(case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end)),  0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date;

							/**Update bank_balance_available from movement balance */
							UPDATE bank_account 
							SET 
								bank_balance_available = (bank_balance_available + (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end))
							WHERE
								id = _bank_account_origin_id;
                            
   							INSERT INTO document_parent_generate_id(inactive) select 0;
							SET _document_parent_id_transfer = LAST_INSERT_ID();                            
							
							INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id_transfer = LAST_INSERT_ID();
                            
							SELECT 
								IFNULL(financial_group_id, ''),
								IFNULL(financial_sub_group_id, '')
							INTO _financial_group_id_transfer , _financial_subgroup_id_transfer FROM
								provider
							WHERE
								id = _credit_provider_id_transfer;
                                                                            
  							INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_number, document_value,
							document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, 
							nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, inactive, user_creation, creation_date, user_change, change_date, product_id, product_description, is_automatic_transfer, document_id_origin)
							SELECT _document_id_transfer, _document_parent_id_transfer, 2 AS document_type, (case when _document_value_transfer < 0 then 0 else 1 end) AS credit, _company_id_transfer, _credit_provider_id_transfer, _bank_account_automatic_transfer_id, _bank_account_origin_id,
							_document_id_transfer AS document_number, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS document_value, CONCAT('TRANSF. MOVIMENTO:',_document_parent_principal_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							2 AS document_status, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, _processing_date_time, 
							_processing_date_time AS payment_expiry_data, _user AS payment_user, 0 AS nfe_number, 0 AS nfe_serie_number, 0 AS generate_billet, 
							_financial_group_id_transfer, _financial_subgroup_id_transfer, 0 _is_product_movement, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date, _product_id, _product_description, 0 _is_automatic_transfer, _document_parent_principal_id;
							
                            
							/**Insert bank account statement*/
							INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)  
							SELECT _person_id_transfer, _bank_account_automatic_transfer_id, _bank_account_origin_id, _debit_provider_id_transfer, 2 _launch_type, _document_parent_id_transfer, (_document_id_transfer), _document_value_transfer, CONCAT('TRANSF. MOVIMENTO:',_document_parent_principal_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							((SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_origin_id LIMIT 1)+_document_value_transfer),  0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date;

							/**Update bank_balance_available from movement balance */
							UPDATE bank_account 
							SET 
								bank_balance_available = (bank_balance_available + (_document_value_transfer))
							WHERE
								id = _bank_account_automatic_transfer_id;   
                            
                            /** SET principal document parent with automatic transfer */
                            UPDATE document_movement SET is_automatic_transfer = 1 WHERE document_parent_id = _document_parent_principal_id and company_id = _company_id;
                            
                            /**
							SET _validation_message = concat(_financial_group_id_transfer, ', ', _financial_subgroup_id_transfer);
							SIGNAL SQLSTATE '45000'
							SET MESSAGE_TEXT = _validation_message; 
                            */
                            
					END; END IF; /** End if _document_value_transfer > 0 */
               END; END IF; /** End if _bank_account_automatic_transfer_id > 0 */

	RETURN 1;
END$$
DELIMITER ;

-- START TRANSACTION;
-- SET SQL_SAFE_UPDATES = 0;
SELECT func_generate_automatic_tranfer_operator(429302, 382);
-- ROLLBACK;
-- SET GLOBAL log_bin_trust_function_creators = 1;