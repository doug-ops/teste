USE `manager`;
DROP procedure IF EXISTS `pr_cashier_closing_generate_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_generate_movement`(IN _operation TINYINT, 
														IN _user_id BIGINT,
                                                        IN _users_children_parent VARCHAR(5000),
														IN _week_year INT,
                                                        IN _user_change BIGINT)
BEGIN	

	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _date_from DATE;
    DECLARE _date_to DATE;
    DECLARE _cashier_closing_id BIGINT DEFAULT 0;
    DECLARE _cashier_closing_status BIT DEFAULT 0;
    DECLARE _client_id TINYINT DEFAULT 0;
    DECLARE _user_id_cash BIGINT DEFAULT 0;
    DECLARE _week_day TINYINT DEFAULT 0;
    DECLARE _week_year_now INT;
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING GENERATE MOVEMENT', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        SELECT @full_error;
	END;
      
  	SET _date_from = func_yearweek_to_date(_week_year, 'Monday');
	SET _date_to = func_yearweek_to_date(_week_year, 'Sunday');
    
  	/**Drop temporary tables*/
    
    DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_user_filter; 
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;    
    
	/**End Drop temporary tables*/
    
	/**Temporary table to insert tmp_user_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_user_values*/
    
	/**Temporary table to insert tmp_user_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_filter
	(
		user_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_user_filter*/    
    
	/**Temporary table to insert tmp_person_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_person_values*/
    
	/**Temporary table to insert tmp_person_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL, 
        week_year INT NOT NULL, 
        date_from DATE, 
        date_to DATE, 
        inactive BIT
	);
	/**End Temporary table to insert tmp_person_filter*/
    
    SELECT IFNULL((SELECT u.client_id FROM user u WHERE u.id = _user_id LIMIT 1),0) INTO _client_id;
    
    SET _user_id_cash = (CASE WHEN _client_id = 1 THEN 8 WHEN _client_id = 2 THEN 7 ELSE 0 END);
    
	INSERT INTO tmp_company_filter (company_id, week_year, date_from, date_to, inactive)
	SELECT c.id, _week_year, _date_from, _date_to, p.inactive FROM company c
	INNER JOIN (
				SELECT company_id FROM user_company WHERE user_id IN 
                (
					SELECT x.id FROM
					(
						SELECT u.id FROM user_parent up 
						INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = CASE WHEN _user_id_cash = 7 THEN 27 ELSE _user_id_cash END
						and u.client_id = _client_id 
						WHERE up.inactive = 0 GROUP BY u.id
                        UNION ALL 
                        SELECT CASE WHEN _user_id_cash = 7 THEN 27 ELSE _user_id_cash END id
					) x
					GROUP BY id                       
				) GROUP BY company_id
			) uc ON c.id = uc.company_id
             INNER JOIN person p ON c.person_id = p.id
             WHERE c.client_id = _client_id AND p.person_type_id = 1
             GROUP BY c.id;
    
	IF(1=_operation) THEN /** Operation 1*/
		BEGIN
            
			SELECT IFNULL(timezone_database, 0) INTO _timezone_database from config_systems LIMIT 1;
			SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
                
			SELECT WEEKDAY(DATE(_processing_date_time)) INTO _week_day;
			SELECT 
				(CASE
					WHEN
						_week_day = 6
					THEN
						YEARWEEK(DATE_ADD(DATE(_processing_date_time),
									INTERVAL - 1 DAY))
					ELSE YEARWEEK(DATE(_processing_date_time))
				END) + 1 INTO _week_year_now;
            
            SELECT IFNULL((SELECT cashier_closing_id FROM cashier_closing WHERE week_year = _week_year AND user_id = _user_id_cash LIMIT 1), 0) INTO _cashier_closing_id;
            
            IF(_cashier_closing_id = 0) THEN
				BEGIN
					INSERT INTO cashier_closing(user_id, week_year, date_from, date_to, cashier_closing_status, user_creation, creation_date, user_change, change_date, inactive)
					SELECT _user_id_cash, _week_year, _date_from, _date_to, 0 cashier_closing_status, _user_change, _processing_date_time, _user_change, _processing_date_time, 0;
								
                    SELECT IFNULL((SELECT cashier_closing_id FROM cashier_closing WHERE week_year = _week_year AND user_id = _user_id_cash LIMIT 1), 0) INTO _cashier_closing_id;
                END;
			END IF;
            
            IF(_cashier_closing_id = 0) THEN
				BEGIN
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = 'ERRO AO GERAR MOVIMENTO FECHAMENTO DE CAIXA.';
                END;
			END IF;
                    
			SELECT IFNULL((SELECT IFNULL(cashier_closing_status, 0) FROM cashier_closing WHERE cashier_closing_id = _cashier_closing_id LIMIT 1), 0) INTO _cashier_closing_status;
            
            IF(_cashier_closing_status < 2) THEN
				BEGIN
                
					/** Insert movement to all companys */
					INSERT INTO cashier_closing_company(cashier_closing_id, user_id, week_year, date_from, date_to, company_id, movement_value, pending_movement_value, discount_total, payment_total, pending_movement_after_value, cashier_closing_status, user_creation, creation_date, user_change, change_date, inactive)
                    SELECT _cashier_closing_id, _user_id_cash, cf.week_year, cf.date_from, cf.date_to, cf.company_id,             
					0 movement_value, 
					0 pending_movement_value, 					
                    0 discount_total, 
					0 payment_total, 
                    0 pending_movement_after_value, 
					0 cashier_closing_status, 
					_user_change, _processing_date_time, _user_change, _processing_date_time, 0
					FROM 
                    tmp_company_filter cf 
                    LEFT JOIN cashier_closing_company cc ON cf.week_year = cc.week_year AND cc.user_id = _user_id_cash AND cf.company_id = cc.company_id
                    WHERE 
                    -- cf.company_id = 1835 AND 
                    cf.inactive = 0 
                    AND cc.company_id IS NULL;
                
					/* Recebimento Previsto = Valores das duplicatas geradas em execução das lojas. Essa que na planilha onde tem os valores de pendências está descrimada como “duplicatas”  */                   
				   INSERT INTO cashier_closing_company_document(cashier_closing_id, user_id, week_year, date_from, date_to, company_id, document_movement_id, document_parent_id, product_id, product_movement, residue, document_value, credit, document_note, user_creation, creation_date, user_change, change_date, inactive)
				   SELECT _cashier_closing_id, _user_id_cash, x.week_year, x.date_from, x.date_to, x.company_id, ms.id document_movement_id, x.document_parent_id, IFNULL(ms.product_id,0), ms.is_product_movement, 0 residue, 
				   (CASE WHEN ms.credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) document_value, ms.credit, ms.document_note,
				   _user_change, _processing_date_time, _user_change, _processing_date_time, 0 inactive 
                   FROM 
					(              
					  SELECT 
						cf.company_id, m.document_parent_id, cf.week_year, cf.date_from, cf.date_to
					  FROM 
						tmp_company_filter cf 
						INNER JOIN document_movement m ON m.company_id = cf.company_id
					  WHERE
						m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(cf.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
						AND CAST(CONCAT(DATE_FORMAT(cf.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
						AND m.inactive = 0
						AND IFNULL(m.is_product_movement, 0) = 1
						AND m.document_status = 2
                        AND cf.inactive = 0 
					  GROUP BY 
						cf.company_id, m.document_parent_id, cf.week_year, cf.date_from, cf.date_to
					) x 
					INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					LEFT JOIN cashier_closing_company_document cd ON 
                    -- x.week_year = cd.week_year AND cd.user_id = _user_id_cash AND 
                    x.company_id = cd.company_id AND ms.id = cd.document_movement_id  
				   WHERE
                        cd.document_movement_id IS NULL;
				   /* End Recebimento Previsto = Valores das duplicatas geradas em execução das lojas. Essa que na planilha onde tem os valores de pendências está descrimada como “duplicatas”  */                
                    
				   /* Residual em aberto */	
				   INSERT INTO cashier_closing_company_document(cashier_closing_id, user_id, week_year, date_from, date_to, company_id, document_movement_id, document_parent_id, product_id, product_movement, residue, document_value, credit, document_note, user_creation, creation_date, user_change, change_date, inactive)
				   SELECT 
                   _cashier_closing_id, _user_id_cash, cf.week_year, cf.date_from, cf.date_to,
				   cf.company_id, m.id document_movement_id, m.document_parent_id, 0 product_id, 0 is_product_movement, 1 residue, 
				   (CASE WHEN m.credit = 0 THEN m.document_value ELSE (m.document_value * -1) END) document_value, m.credit, m.document_note,
				   _user_change, _processing_date_time, _user_change, _processing_date_time, 0  
				   FROM 
						tmp_company_filter cf 
						INNER JOIN document_movement m ON m.company_id = cf.company_id
                        -- INNER JOIN cashier_closing_company ccc ON ccc.week_year = _week_year AND ccc.user_id = _user_id_cash AND ccc.company_id = cf.company_id AND IFNULL(ccc.cashier_closing_status,0) < 2
                        LEFT JOIN cashier_closing_company_document cd ON 
                        cf.company_id = cd.company_id AND m.id = cd.document_movement_id  
				   WHERE
						m.payment_residue = 1 AND m.document_status = 1 AND m.is_cashing_close = 1 AND m.inactive = 0	
                        -- AND cd.company_id = 14
                        AND cd.week_year IS NULL;
                   /* End Residual em aberto */
                                                                        
					INSERT INTO cashier_closing_company(cashier_closing_id, user_id, week_year, date_from, date_to, company_id, movement_value, pending_movement_value, discount_total, payment_total, pending_movement_after_value, cashier_closing_status, user_creation, creation_date, user_change, change_date, inactive)
                    SELECT _cashier_closing_id, _user_id_cash, x.week_year, x.date_from, x.date_to, x.company_id,             
					SUM(CASE WHEN ms.residue = 1 THEN 0 ELSE ms.document_value END) movement_value, 
					SUM(CASE WHEN ms.residue = 0 THEN 0 ELSE document_value END) pending_movement_value, 					
                    0 discount_total, 
					((SUM(CASE WHEN ms.residue = 1 THEN 0 ELSE ms.document_value END)) +
                    (SUM(CASE WHEN ms.residue = 0 THEN 0 ELSE ms.document_value END))) payment_total, 
                    0 pending_movement_after_value, 
					0 cashier_closing_status, 
					_user_change, _processing_date_time, _user_change, _processing_date_time, 0
					FROM 
					(              
					  SELECT 
						cf.company_id, m.cashier_closing_id, cf.week_year, cf.date_from, cf.date_to
					  FROM 
						tmp_company_filter cf 
						INNER JOIN cashier_closing_company_document m ON m.company_id = cf.company_id AND cf.week_year = m.week_year
					  GROUP BY 
						cf.company_id, m.cashier_closing_id, cf.week_year, cf.date_from, cf.date_to
					) x 
				   INNER JOIN cashier_closing_company_document ms ON x.cashier_closing_id = ms.cashier_closing_id AND x.company_id = ms.company_id
                   LEFT JOIN cashier_closing_company cc ON x.week_year = cc.week_year AND cc.user_id = _user_id_cash AND x.company_id = cc.company_id
                   WHERE cc.company_id IS NULL
				   GROUP BY x.company_id, x.week_year, x.date_from, x.date_to;
                                      
					UPDATE cashier_closing_company ccc
                    INNER JOIN 
                    (
                    SELECT _cashier_closing_id cashier_closing_id, _user_id_cash, x.week_year, x.date_from, x.date_to, x.company_id,             
					SUM(CASE WHEN ms.residue = 1 THEN 0 ELSE ms.document_value END) movement_value, 
					SUM(CASE WHEN ms.residue = 0 THEN 0 ELSE document_value END) pending_movement_value, 					
                    0 discount_total, 
					((SUM(CASE WHEN ms.residue = 1 THEN 0 ELSE ms.document_value END)) +
                    (SUM(CASE WHEN ms.residue = 0 THEN 0 ELSE ms.document_value END))) payment_total, 
                    0 pending_movement_after_value, 
					0 cashier_closing_status, 
					_user_change create_user, _processing_date_time creation_date, _user_change user_change, _processing_date_time change_date, 0
					FROM 
					(              
					  SELECT 
						cf.company_id, m.cashier_closing_id, cf.week_year, cf.date_from, cf.date_to
					  FROM 
						tmp_company_filter cf 
						INNER JOIN cashier_closing_company_document m ON m.company_id = cf.company_id AND cf.week_year = m.week_year
					  GROUP BY 
						cf.company_id, m.cashier_closing_id, cf.week_year, cf.date_from, cf.date_to
					) x
				   INNER JOIN cashier_closing_company_document ms ON x.cashier_closing_id = ms.cashier_closing_id AND x.company_id = ms.company_id
                   LEFT JOIN cashier_closing_company cc ON x.week_year = cc.week_year AND cc.user_id = _user_id_cash AND x.company_id = cc.company_id                   
				   GROUP BY x.company_id, x.week_year, x.date_from, x.date_to
                   ) y ON y.cashier_closing_id = ccc.cashier_closing_id AND y.week_year = ccc.week_year AND ccc.user_id = _user_id_cash AND y.company_id = ccc.company_id
                   AND IFNULL(ccc.cashier_closing_status,0) < 2
                   SET 
					   ccc.movement_value = y.movement_value, 
					   ccc.pending_movement_value = y.pending_movement_value,
                       ccc.payment_total = y.payment_total;        
                END;
			END IF; /* End  insert cashier_closing_company */            
            
			/** Generate residual balance */
		   IF(1 = 0) THEN 
			BEGIN  				
				/**VARIABLES CURSOR*/
				DECLARE _has_movements_pending_process BIT DEFAULT 0;
				DECLARE _has_movements BIT DEFAULT 0;
				DECLARE _week_filter INT;
				DECLARE _company_id BIGINT;
				DECLARE _balance_moviment DECIMAL(19,2) DEFAULT 0;
				DECLARE _bank_account_id BIGINT;
				DECLARE _bank_balance_available DECIMAL(19,2) DEFAULT 0;
				DECLARE _document_note VARCHAR(1000);
				DECLARE _document_id BIGINT;
				DECLARE _document_parent_id BIGINT;
	
				/**Cursor to search documents*/
				DECLARE documents_cursor CURSOR FOR
				SELECT 
					x.company_id, x.balance_moviment, b.id bank_account_id, b.bank_balance_available
				FROM 
				(
				SELECT 
					ccc.company_id, sum((ccc.movement_value + ccc.pending_movement_value)) balance_moviment
				FROM 
					cashier_closing_company ccc
				WHERE 
					ccc.company_id = 14 AND 
					ccc.cashier_closing_status <> 2    
				GROUP BY ccc.company_id
				) x
				INNER JOIN company c ON x.company_id = c.id
				INNER JOIN person p ON c.person_id = p.id
				INNER JOIN bank_account b ON p.bank_account_id = b.id
				WHERE (b.bank_balance_available - x.balance_moviment) <> 0;
				
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
				OPEN documents_cursor;
				meuLoop: LOOP
				FETCH documents_cursor INTO _company_id, _balance_moviment, _bank_account_id, _bank_balance_available;
				
				IF _has_movements = 1 THEN
					LEAVE meuLoop;
				END IF;
				
				/**Has movements pending to process*/
				SET _has_movements_pending_process = 1;
				
				INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();  
				
				INSERT INTO document_parent_generate_id(inactive) select 0;
				SET _document_parent_id = LAST_INSERT_ID();
				
				SET _document_note = CASE WHEN _client_id = 1 THEN 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA: RGB COFRE CAIXA (182)' ELSE 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA: COLETIVA JB (1)' END;
				
				INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
				document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
				payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
				inactive, user_creation, creation_date, user_change, change_date, cashier_closing_date)
				SELECT 
				1 is_cashing_close, _document_id, _document_parent_id, 0 _document_parent_id_principal, 0 document_transfer_id,  3 _document_type,                        
				(CASE WHEN (_bank_balance_available - _balance_moviment) >= 0 THEN 0 ELSE 1 END) _credit, _company_id, 53 provider_id, _bank_account_id, 
				(CASE WHEN _client_id = 1 THEN 199 ELSE 1565 END) _bank_account_origin_id,
				CONCAT(DATE_FORMAT(now(), '%Y%m%d%H%i%S'), '_', _company_id) _document_number, 
				(CASE WHEN (_bank_balance_available - _balance_moviment) >= 0 THEN (_bank_balance_available - _balance_moviment) ELSE ((_bank_balance_available - _balance_moviment) * -1) END) _document_value, 
				_document_note, 1 _document_status, 
				(CASE WHEN (_bank_balance_available - _balance_moviment) >= 0 THEN (_bank_balance_available - _balance_moviment) ELSE ((_bank_balance_available - _balance_moviment) * -1) END) payment_value, 
				0 payment_discount, 0 payment_extra, 1 payment_residue, 
				_processing_date_time _payment_date, _processing_date_time _payment_expiry_data, 
				_user_change _payment_user, 0 nfe_number, 1 payment_status, 0 generate_billet, '20.000' _financial_group_id, 
				(CASE WHEN (_bank_balance_available - _balance_moviment) < 0 THEN '20.003' ELSE '20.002' END) _financial_sub_group_id,
				0 _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _processing_date_time;
				
			  END LOOP meuLoop;
			  CLOSE documents_cursor;
	  
				END; /** End operation 1 */
			END IF;
		
        END; /* End Operation 1 */
    END IF;
END$$

SET SQL_SAFE_UPDATES = 0;
CALL pr_cashier_closing_generate_movement(1, 7, null, 202436, 7);
