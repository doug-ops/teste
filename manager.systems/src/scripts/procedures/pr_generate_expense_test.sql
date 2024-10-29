USE `manager`;
DROP procedure IF EXISTS `pr_generate_expense_test`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_generate_expense_test`()
BEGIN	

	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();

	SELECT IFNULL(timezone_database, 0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
            
	IF(1=1) THEN /** Operation 1*/
		BEGIN
			DECLARE finished INTEGER DEFAULT 0;
            DECLARE _company_id BIGINT;
            DECLARE _document_value decimal(19,2);
			DECLARE _document_id BIGINT;
			DECLARE _document_parent_id BIGINT;
			DECLARE _document_note VARCHAR(1000);
			DECLARE _bank_account_id BIGINT;
            
			DEClARE cursorMovements 
			CURSOR FOR 
				select d.company_id, d.document_value, p.bank_account_id from despesas_bk d
				inner join company c on d.company_id = c.id
				inner join person p on c.person_id = p.id;

				-- declare NOT FOUND handler
				DECLARE CONTINUE HANDLER 
				FOR NOT FOUND SET finished = 1;

				OPEN cursorMovements;

				getMovements: LOOP
				FETCH cursorMovements INTO _company_id, _document_value, _bank_account_id;  
		
				IF finished = 1 THEN 
					LEAVE getMovements;
				END IF;			
                        
                INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();  
                            
				INSERT INTO document_parent_generate_id(inactive) select 0;
				SET _document_parent_id = LAST_INSERT_ID();
                    
				SET _document_note = 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA: COLETIVA JB (1)';
												
				INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
				document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
				payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
				inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
				SELECT 1 is_cashing_close, _document_id, _document_parent_id, 0 _document_parent_id_principal, 0 document_transfer_id,  3 _document_type, 
				0 _credit, _company_id, 53 provider_id, _bank_account_id, 47 _bank_account_origin_id,
				CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
				_document_value, _document_note, 1 _document_status, _document_value, 0 payment_discount, 0 payment_extra, 1 payment_residue, 
				_processing_date_time, _processing_date_time _processing_date_time, 
				1 _payment_user, 0 nfe_number, 1 payment_status, 0 generate_billet, 
                '20.000' _financial_group_id, '20.003' _financial_sub_group_id,
				0 _inactive, 1 _user_change, _processing_date_time, 1 _user_change, _processing_date_time, _processing_date_time;   							
					
				END LOOP getMovements;
				CLOSE cursorMovements;   
                
		
        END; /* End Operation 1 */
    END IF;
END$$

-- SET SQL_SAFE_UPDATES = 0;
   CALL pr_generate_expense_test();

/**
create table despesas_bk(company_id bigint, document_value decimal(19,2))
select * from despesas_bk

insert into despesas_bk(company_id, document_value)
select 395,  1103.50 union all -- Andradas Roma $
select 1798, 33.30 union all -- Cohab Poa $
select 543,  3521.70 union all -- Rui2 Poa Bravo
select 1813,  261.90 union all -- Júlio_BetSul
select 659,  161.70 union all -- Bet Sul IND
select 497,  1059.60; -- Bet Sul Bravo
*/

select * from document_movement where company_id = 543 and document_value = 3521.70

