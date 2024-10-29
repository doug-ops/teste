USE `manager`;
DROP procedure IF EXISTS `pr_cashier_closing_process_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_process_movement`(IN _operation TINYINT, 
													   IN _user_operator BIGINT,
													   IN _companys_id VARCHAR(5000),
													   IN _cashier_closing_id BIGINT,
                                                       IN _payment_date DATETIME,
													   IN _user_change BIGINT)
BEGIN	

	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _client_id TINYINT DEFAULT 0;
    DECLARE _user_id_cash BIGINT DEFAULT 0;    
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
        
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING PROCESS MOVEMENT', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        SELECT @full_error;
	END;
		
  	/**Drop temporary tables*/
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;    
    
	/**End Drop temporary tables*/
	/**Temporary table to insert tmp_person_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_person_values*/
    
	/**Temporary table to insert tmp_person_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_person_filter*/
    
    SELECT IFNULL((SELECT u.client_id FROM user u WHERE u.id = _user_operator LIMIT 1),0) INTO _client_id;
    
    SET _user_id_cash = (CASE WHEN _client_id = 1 THEN 8 WHEN _client_id = 2 THEN 7 ELSE 0 END);    
    
	IF(LENGTH(IFNULL(_companys_id, '')) > 0) THEN 
		BEGIN
			INSERT INTO tmp_company_values VALUES(_companys_id);
			SET @sql = concat("INSERT INTO tmp_company_filter (company_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_company_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
		END;
    END IF;
    
	SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);    
    
	IF(1=_operation) THEN /** Operation 1*/
		BEGIN
			DECLARE _document_parent_id_principal BIGINT;
        
			IF(IFNULL((SELECT cashier_closing_status FROM  cashier_closing WHERE cashier_closing_id = _cashier_closing_id LIMIT 1),0) > 1) THEN
				BEGIN
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = 'Erro ao processar. Movimento Caixa ja fechado.';
                END;
			END IF; /** End verify cashing closing status */
            
			INSERT INTO document_parent_generate_id(inactive) select 0;
			SET _document_parent_id_principal = LAST_INSERT_ID(); 

            IF(1 = 1) THEN /** Cursor process company movements*/
				BEGIN
					/**VARIABLES CURSOR*/
					DECLARE _has_movements_pending_process BIT DEFAULT 0;
					DECLARE _has_movements BIT DEFAULT 0;
                    DECLARE _company_id BIGINT;
                    DECLARE _discount_total DECIMAL(19,2) DEFAULT 0;
                    DECLARE _payment_total DECIMAL(19,2) DEFAULT 0;
                    DECLARE _pending_movement_after_value DECIMAL(19,2) DEFAULT 0;
                    DECLARE _document_id BIGINT;
                    DECLARE _document_parent_id BIGINT;
                    DECLARE _bank_account_id BIGINT;
                    DECLARE _document_note VARCHAR(1000);
                    DECLARE _company_description VARCHAR(100);
                    DECLARE _expense_total DECIMAL(19,2) DEFAULT 0;
                    DECLARE _credit_total DECIMAL(19,2) DEFAULT 0;
					
					/**Cursor to process companys*/
					DECLARE companys_cursor CURSOR FOR
                    SELECT 
						ccc.company_id, c.social_name, pe.bank_account_id, ccc.discount_total, ccc.payment_total, ccc.pending_movement_after_value 
                    FROM 
						cashier_closing_company ccc 
						INNER JOIN tmp_company_filter f ON ccc.company_id = f.company_id
						INNER JOIN company c ON f.company_id = c.id
						INNER JOIN person pe ON c.person_id = pe.id
                    WHERE 
						ccc.cashier_closing_id = _cashier_closing_id AND ccc.cashier_closing_status < 2;
					
					DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
					OPEN companys_cursor;
					meuLoop: LOOP
					FETCH companys_cursor INTO _company_id, _company_description, _bank_account_id, _discount_total, _payment_total, _pending_movement_after_value;					
                    
					IF _has_movements = 1 THEN
						LEAVE meuLoop;
					END IF;
                    				
					INSERT INTO document_generate_id(inactive) select 0;
					SET _document_id = LAST_INSERT_ID();    
					
					INSERT INTO document_parent_generate_id(inactive) select 0;
					SET _document_parent_id = LAST_INSERT_ID(); 					
                    
                    SET _document_note = CASE WHEN _client_id = 1 THEN 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA: RGB COFRE CAIXA (182)' ELSE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA: COLETIVA JB (1)' END;
									
                    SELECT IFNULL((SELECT sum(document_value) FROM cashier_closing_company_launch WHERE company_id = _company_id AND expense = 1 AND cashier_closing_id = _cashier_closing_id AND user_operator = _user_operator AND cashier_closing_status < 2 AND inactive = 0),0) INTO _expense_total;
                    SELECT IFNULL((SELECT sum(document_value) FROM cashier_closing_company_launch WHERE company_id = _company_id AND expense = 0 AND cashier_closing_id = _cashier_closing_id AND user_operator = _user_operator AND cashier_closing_status < 2 AND inactive = 0),0) INTO _credit_total;

					SET _payment_total = _payment_total - _expense_total;
                    SET _payment_total = _payment_total + _credit_total;
                    
                    -- SELECT _payment_total, _expense_total, _credit_total;
												
					INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
					document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
					payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
					inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
                    SELECT 1 is_cashing_close, _document_id, _document_parent_id, _document_parent_id_principal _document_transfer_id,  2 _document_type, 
                    (CASE WHEN _payment_total < 0 THEN 1 ELSE 0  END) _credit, _company_id, 53 provider_id, _bank_account_id, 
                    (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id, 
					CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
                    (CASE WHEN _payment_total < 0 THEN (_payment_total * -1) ELSE _payment_total END) _payment_value, 
					_document_note, 2 _document_status, (CASE WHEN _payment_total < 0 THEN (_payment_total * -1) ELSE _payment_total END) _payment_value, 
                    0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date, _payment_date, 
                    _user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, '20.000' _financial_group_id, (CASE WHEN _payment_total < 0 THEN '20.003' ELSE '20.002' END) _financial_sub_group_id,
					0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;

					-- SELECT * FROM bank_account WHERE ID = _bank_account_id;
                    
                    IF(_discount_total > 0) THEN
						BEGIN
                        
                        	INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id = LAST_INSERT_ID();  
                    
							SET _document_note = CASE WHEN _client_id = 1 THEN 'DESCONTO FEC. CAIXA >> DESTINO EMPRESA: RGB COFRE CAIXA (182)' ELSE 'DESCONTO FEC. CAIXA >> DESTINO EMPRESA: COLETIVA JB (1)' END;
												
							INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
							document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
							payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
							inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
							SELECT 1 is_cashing_close, _document_id, _document_parent_id, _document_parent_id_principal document_transfer_id,  3 _document_type, 
							0 _credit, _company_id, 53 provider_id, _bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id, 
							CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, _discount_total, _document_note, 2 _document_status, _discount_total, 
                            0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date, _payment_date _payment_expiry_data, 
							_user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, '05.000' _financial_group_id, '05.010' _financial_sub_group_id,
							0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;
                            
							UPDATE bank_account ba 
							INNER JOIN 
							(SELECT (_discount_total * -1) AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
							SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);
																			
							INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, provider_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
							SELECT 
							(SELECT person_id FROM company WHERE id=_company_id) person_id,
							_bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id, 3 _document_type, _document_parent_id, _document_id, 53, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1), _discount_total * -1 AS movement_balance,
							_document_note, 0, _user_change, _processing_date_time, _user_change, _payment_date;                            
                            
                        END;
					END IF;                    				
                    
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT (_payment_total * -1) AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
					SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);
											
					SET _document_note = CASE WHEN _client_id = 1 THEN 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA: RGB COFRE CAIXA (182)' ELSE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA: COLETIVA JB (1)' END;
                                            
				    INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, provider_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
				    SELECT 
				    (SELECT person_id FROM company WHERE id=_company_id) person_id,
				    _bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id, 2 _document_type, _document_parent_id, _document_id, 53, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1), _payment_total * -1 AS movement_balance,
				    _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_date;
                    
                    -- SELECT * FROM bank_account WHERE id = _bank_account_id;
                    -- SELECT * FROM bank_account_statement WHERE  bank_account_id = _bank_account_id;
					-- SELECT * FROM document_movement where document_parent_id = _document_parent_id;                    
                    
                    IF(_pending_movement_after_value <> 0) THEN
						BEGIN
										
                        	INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id = LAST_INSERT_ID();  
                            
                            INSERT INTO document_parent_generate_id(inactive) select 0;
							SET _document_parent_id = LAST_INSERT_ID();
                    
							SET _document_note = CASE WHEN _client_id = 1 THEN 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA: RGB COFRE CAIXA (182)' ELSE 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA: COLETIVA JB (1)' END;
												
							INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
							document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
							payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
							inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
							SELECT 1 is_cashing_close, _document_id, _document_parent_id, _document_parent_id_principal, _document_parent_id_principal document_transfer_id,  3 _document_type, 
							(CASE WHEN _pending_movement_after_value >= 0 THEN 0 ELSE 1 END) _credit, _company_id, 53 provider_id, _bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id,
							CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
							(CASE WHEN _pending_movement_after_value >= 0 THEN _pending_movement_after_value ELSE (_pending_movement_after_value * -1) END) _document_value, _document_note, 1 _document_status, (CASE WHEN _pending_movement_after_value >= 0 THEN _pending_movement_after_value ELSE (_pending_movement_after_value * -1) END) payment_value, 0 payment_discount, 0 payment_extra, 1 payment_residue, 
                            _payment_date, _payment_date _payment_expiry_data, 
							_user_change _payment_user, 0 nfe_number, 1 payment_status, 0 generate_billet, '20.000' _financial_group_id, (CASE WHEN _payment_total < 0 THEN '20.003' ELSE '20.002' END) _financial_sub_group_id,
							0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;   							
                        END;
					END IF;								

					-- SELECT * FROM document_movement where document_parent_id = _document_parent_id
                    
					INSERT INTO document_generate_id(inactive) select 0;
					SET _document_id = LAST_INSERT_ID();  
					
					SET _document_note = CONCAT('TRANSF. FEC. CAIXA >> ORIGEM EMPRESA: ', _company_id, ' - ',_company_description);
                    
					INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, 
                    company_id, provider_id, bank_account_id, bank_account_origin_id,
					document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
					payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
					inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
                    SELECT 1 is_cashing_close, _document_id, _document_parent_id_principal document_parent_id, _document_parent_id_principal document_transfer_id,  2 _document_type, 
                    (CASE WHEN _payment_total < 0 THEN 0 ELSE 1  END) _credit, (CASE WHEN _client_id = 1 THEN 182 ELSE 1 END) company_id, 
                    (CASE WHEN _payment_total < 49 THEN 0 ELSE 48  END) provider_id, 
                    (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_id, _bank_account_id bank_account_origin_id, 
					CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
                    (CASE WHEN _payment_total < 0 THEN (_payment_total * -1) ELSE _payment_total END) _payment_value, 
					_document_note, 2 _document_status, (CASE WHEN _payment_total < 0 THEN (_payment_total * -1) ELSE _payment_total END) _payment_value, 
                    0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date, _payment_date _payment_expiry_data, 
                    _user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, 
                    (CASE WHEN _payment_total < '07.000' THEN 0 ELSE '01.000' END) _financial_group_id, (CASE WHEN _payment_total < '20.000' THEN 0 ELSE (CASE WHEN _payment_total < 0 THEN '20.003' ELSE '20.002' END) END) _financial_sub_group_id,
					0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;
                    
					-- SELECT * FROM document_movement where document_parent_id = _document_parent_id;
                    
                    -- SELECT * FROM bank_account WHERE ID = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END);
                    
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT _payment_total AS movement_balance, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) AS bank_account_id) x ON ba.id=x.bank_account_id
					SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);
                    
					-- SELECT * FROM bank_account WHERE ID = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END);
														 
				    INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
				    SELECT 
				    (SELECT person_id FROM company WHERE id = (CASE WHEN _client_id = 1 THEN 182 ELSE 1 END)) person_id,
				    (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_id, _bank_account_id bank_account_origin_id, 2 _document_type, 
                    _document_parent_id_principal document_parent_id, _document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END)  LIMIT 1), _payment_total AS movement_balance,
				    _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_date;  
                    
					UPDATE cashier_closing_company ccc SET cashier_closing_status = 2, close_user = _user_change, close_date = _processing_date_time
                    WHERE  ccc.cashier_closing_id = _cashier_closing_id AND ccc.company_id = _company_id;	

                    UPDATE document_movement d 
					INNER JOIN cashier_closing_company_document ccd ON d.id = ccd.document_movement_id AND d.document_parent_id = ccd.document_parent_id AND d.company_id = ccd.company_id
					SET d.document_status = 2, d.payment_status = 2, d.document_transfer_id = 0, d.is_document_transfer = 1, d.cashier_closing_date = _processing_date_time
					WHERE 
					ccd.cashier_closing_id = _cashier_closing_id AND ccd.company_id = _company_id;                     

				  END LOOP meuLoop;
				  CLOSE companys_cursor;
                  
                END;
			END IF; /** End Cursor process company movements*/
            
			IF(1 = 1) THEN /** Cursor process company launchs*/
				BEGIN
					/**VARIABLES CURSOR*/
					DECLARE _has_movements_pending_process BIT DEFAULT 0;
					DECLARE _has_movements BIT DEFAULT 0;
                    DECLARE _launch_id BIGINT;
                    DECLARE _company_id BIGINT;
                    DECLARE _document_id BIGINT;
                    DECLARE _document_parent_id BIGINT;
                    DECLARE _bank_account_id INT;
                    DECLARE _provider_id BIGINT; 
                    DECLARE _financial_group_id VARCHAR(7);
                    DECLARE _financial_sub_group_id VARCHAR(7);
					DECLARE _credit BIT;
                    DECLARE _expense BIT;
                    DECLARE _document_status TINYINT; 
                    DECLARE _payment_date_launch DATETIME; 
                    DECLARE _document_value DECIMAL(19,2);
                    DECLARE _document_note VARCHAR(1000);
                    
					/**Cursor to process companys launchs*/
					DECLARE companys_launchs_cursor CURSOR FOR
                    SELECT
						cl.id, cl.company_id, cl.bank_account_id, cl.provider_id, cl.financial_group_id, cl.financial_sub_group_id,
						cl.credit, cl.expense, cl.document_status, cl.payment_data, cl.document_value, cl.document_note
                    FROM 
						cashier_closing_company_launch cl
                    WHERE 
						cl.cashier_closing_id = _cashier_closing_id AND cl.user_operator = _user_operator AND cl.cashier_closing_status < 2 AND cl.inactive = 0;
					
					DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
					OPEN companys_launchs_cursor;
					meuLoop: LOOP
					FETCH companys_launchs_cursor INTO _launch_id, _company_id, _bank_account_id, _provider_id, _financial_group_id, _financial_sub_group_id,
													   _credit, _expense, _document_status, _payment_date_launch, _document_value, _document_note;
					
					IF _has_movements = 1 THEN
						LEAVE meuLoop;
					END IF;
                    
							IF(_expense = 0 AND _bank_account_id NOT IN (199, 1565)) THEN
								BEGIN
                                                                
									SET _document_note = CONCAT('TRANSF. FEC. CAIXA >> FORNECEDOR: ',  (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END), ' OBS: ', _document_note);

									INSERT INTO document_generate_id(inactive) select 0;
									SET _document_id = LAST_INSERT_ID();  
								
									INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, 
									company_id, provider_id, bank_account_id, bank_account_origin_id,
									document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
									payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
									inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
									SELECT 1 is_cashing_close, _document_id, _document_parent_id_principal _document_parent_id, _document_parent_id_principal document_transfer_id,  
									2 _document_type, (CASE WHEN _credit = 1 THEN 0 ELSE _credit END) credit, (CASE WHEN _client_id = 1 THEN 182 ELSE 1 END) company_id, 
									(CASE WHEN _document_value < 0 THEN 48 ELSE 49  END) provider_id, 
									(CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_id, _bank_account_id bank_account_origin_id, 
									CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', (CASE WHEN _client_id = 1 THEN 182 ELSE 1 END)) _document_number, 
									(CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									_document_note, _document_status, (CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date, _payment_date _payment_expiry_data, 
									_user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, 
									(CASE WHEN _expense = 1 THEN '20.000' ELSE '20.001' END) _financial_group_id, 
									(CASE WHEN _expense = 1 THEN '20.000' ELSE '20.001' END) _financial_sub_group_id,
									0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;

									-- SELECT * FROM document_movement where document_id = _document_id;

									-- SELECT * FROM bank_account WHERE id = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END);

									UPDATE bank_account ba 
									INNER JOIN 
									(SELECT (CASE WHEN _credit = 0 THEN _document_value ELSE _document_value * -1 END) AS movement_balance, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) AS bank_account_id) x ON ba.id=x.bank_account_id
									SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);

									-- SELECT * FROM bank_account WHERE ID = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END);
											 
									INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
									SELECT 
									(SELECT person_id FROM company WHERE id = (CASE WHEN _client_id = 1 THEN 182 ELSE 1 END)) person_id,
									(CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_id, _bank_account_id bank_account_origin_id, 2 _document_type, 
									_document_parent_id_principal, _document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id = (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END)  LIMIT 1), 
                                    (CASE WHEN _credit = 0 THEN _document_value ELSE _document_value * -1 END) AS movement_balance,
									_document_note, 0 _inactive, _user_change, _processing_date_time, _user_change, _payment_date_launch;                                    
									
                                    -- SELECT * FROM bank_account_statement WHERE document_id = _document_id;
                                    
                                    
									INSERT INTO document_generate_id(inactive) select 0;
									SET _document_id = LAST_INSERT_ID();  
								
									-- SELECT _expense;
									SET _document_note = 
									CASE WHEN _expense = 1 THEN
									CONCAT('DESPESA TRANSF. FEC. CAIXA >> FORNECEDOR: ',  (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END), ' OBS: ', _document_note)
									ELSE 
									CONCAT('TRANSF. FEC. CAIXA >> FORNECEDOR: ',  (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END), ' OBS: ', _document_note)
									END;

									INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, 
									company_id, provider_id, bank_account_id, bank_account_origin_id,
									document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
									payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
									inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
									SELECT 1 is_cashing_close, _document_id, _document_parent_id_principal _document_parent_id, _document_parent_id_principal, _document_parent_id_principal document_transfer_id,  
									(CASE WHEN _expense = 1 THEN 3 ELSE 2 END) _document_type, _credit, _company_id, (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END) provider_id, 
									_bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_origin_id, 
									CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
									(CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									_document_note, _document_status, (CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date_launch, _payment_date_launch _payment_expiry_data, 
									_user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, 
									(CASE WHEN _expense = 1 THEN _financial_group_id ELSE (CASE WHEN _expense = 1 THEN '20.000' ELSE '20.001' END) END) _financial_group_id, 
									(CASE WHEN _expense = 1 THEN _financial_group_id ELSE (CASE WHEN _expense = 1 THEN '20.000' ELSE '20.001' END) END) _financial_sub_group_id,
									0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;
									
								   -- SELECT * FROM document_movement where document_id = _document_id 
								   -- union all 
								   -- SELECT * FROM document_movement where document_id = 1349506;
								
									-- SELECT * FROM bank_account WHERE ID = _bank_account_id;

									UPDATE bank_account ba 
									INNER JOIN 
									(SELECT 
									(CASE WHEN  _expense = 1 THEN (CASE WHEN _document_value < 0 THEN _document_value ELSE _document_value * -1 END) ELSE  
									(CASE WHEN _document_value < 0 THEN _document_value * -1 ELSE _document_value END) END) AS movement_balance, 
									_bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
									SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);

									-- SELECT * FROM bank_account WHERE ID = _bank_account_id;
											 
									INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, provider_id, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
									SELECT 
									(SELECT person_id FROM company WHERE id = _company_id) person_id,
									_bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_origin_id, 
									(CASE WHEN _expense = 1 THEN 3 ELSE 2 END) _document_type,  
									(CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END),
									_document_parent_id_principal, 
									_document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id = _bank_account_id  LIMIT 1), 
									(CASE WHEN  _expense = 1 THEN (CASE WHEN _document_value < 0 THEN _document_value ELSE _document_value * -1 END) ELSE  
									(CASE WHEN _document_value < 0 THEN _document_value * -1 ELSE _document_value END) END) AS movement_balance,
									_document_note, 0, _user_change, _processing_date_time, _user_change, _payment_date_launch;       
									
								   -- SELECT * FROM bank_account_statement WHERE document_id = _document_id
								   -- union all 
								   -- SELECT * FROM bank_account_statement WHERE document_id = 1349506;		                                    
								END;
							ELSEIF(_expense = 1) THEN
								BEGIN
                                
									INSERT INTO document_generate_id(inactive) select 0;
									SET _document_id = LAST_INSERT_ID();  
								
									-- SELECT _expense;
									SET _document_note = 
									CASE WHEN _expense = 1 THEN
									CONCAT('DESPESA TRANSF. FEC. CAIXA >> FORNECEDOR: ',  (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END), ' OBS: ', _document_note)
									ELSE 
									CONCAT('TRANSF. FEC. CAIXA >> FORNECEDOR: ',  (CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END), ' OBS: ', _document_note)
									END;

									INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, 
									company_id, provider_id, bank_account_id, bank_account_origin_id,
									document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
									payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
									inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
									SELECT 1 is_cashing_close, _document_id, _document_parent_id_principal _document_parent_id, _document_parent_id_principal, _document_parent_id_principal document_transfer_id,  
									(CASE WHEN _expense = 1 THEN 3 ELSE 2 END) _document_type, _credit, _company_id, _provider_id, 
									_bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_origin_id, 
									CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
									(CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									_document_note, _document_status, (CASE WHEN _document_value < 0 THEN (_document_value * -1) ELSE _document_value END) _payment_value, 
									0 payment_discount, 0 payment_extra, 0 payment_residue, _payment_date_launch, _payment_date_launch _payment_expiry_data, 
									_user_change _payment_user, 0 nfe_number, 2 payment_status, 0 generate_billet, 
									_financial_group_id, 
									_financial_sub_group_id,
									0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;
									
								   -- SELECT * FROM document_movement where document_id = _document_id 
								   -- union all 
								   -- SELECT * FROM document_movement where document_id = 1349506;
								
									-- SELECT * FROM bank_account WHERE ID = _bank_account_id;

									UPDATE bank_account ba 
									INNER JOIN 
									(SELECT 
									(CASE WHEN  _expense = 1 THEN (CASE WHEN _document_value < 0 THEN _document_value ELSE _document_value * -1 END) ELSE  
									(CASE WHEN _document_value < 0 THEN _document_value * -1 ELSE _document_value END) END) AS movement_balance, 
									_bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
									SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);

									-- SELECT * FROM bank_account WHERE ID = _bank_account_id;
											 
									INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, provider_id, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
									SELECT 
									(SELECT person_id FROM company WHERE id = _company_id) person_id,
									_bank_account_id, (CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) bank_account_origin_id, 
									(CASE WHEN _expense = 1 THEN 3 ELSE 2 END) _document_type,  
									(CASE WHEN _expense = 1 THEN _provider_id ELSE (CASE WHEN _document_value < 49 THEN 0 ELSE 48  END) END),
									_document_parent_id_principal, 
									_document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id = _bank_account_id  LIMIT 1), 
									(CASE WHEN  _expense = 1 THEN (CASE WHEN _document_value < 0 THEN _document_value ELSE _document_value * -1 END) ELSE  
									(CASE WHEN _document_value < 0 THEN _document_value * -1 ELSE _document_value END) END) AS movement_balance,
									_document_note, 0, _user_change, _processing_date_time, _user_change, _payment_date_launch;       
									
								   -- SELECT * FROM bank_account_statement WHERE document_id = _document_id
								   -- union all 
								   -- SELECT * FROM bank_account_statement WHERE document_id = 1349506;					
									
                                END;
							END IF;
                                            
				  END LOOP meuLoop;
				  CLOSE companys_launchs_cursor;
                  
                  UPDATE cashier_closing_company_launch SET close_user = _user_change, close_date = _processing_date_time, cashier_closing_status = 2  WHERE cashier_closing_id = _cashier_closing_id AND user_operator = _user_operator AND inactive = 0;
                  
                END;
			END IF; /** End Cursor process company launchs*/                    
            
           -- SELECT * FROM cashier_closing c WHERE c.cashier_closing_id = _cashier_closing_id;
        END; /* End Operation 1 */
	END IF;
END$$