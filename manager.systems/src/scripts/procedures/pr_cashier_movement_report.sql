USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_movement_report`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_movement_report` (IN _operation TINYINT, 
											   IN _company_id BIGINT,
											   IN _date_from DATETIME, 
											   IN _date_to DATETIME,
                                               IN _users_children_parent VARCHAR(5000))
BEGIN    

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER MOVEMENT REPORT', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;
        
	/**Drop temporary tables*/
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;    
    
    DROP TEMPORARY TABLE IF EXISTS tmp_movement_sintetic;
    DROP TEMPORARY TABLE IF EXISTS tmp_movement_analitic;
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
    
    IF(LENGTH(IFNULL(_users_children_parent, '')) > 0) THEN 
		BEGIN
        
			INSERT INTO tmp_user_values VALUES(_users_children_parent);
			SET @sql = concat("INSERT INTO tmp_user_filter (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
            
			INSERT INTO tmp_company_filter (company_id)
			SELECT id FROM company c
			INNER JOIN (SELECT company_id FROM user_company WHERE user_id IN (SELECT user_id FROM tmp_user_filter) GROUP BY company_id) uc ON c.id = uc.company_id;

		END;
	END IF; 
    
	/**Temporary table to insert tmp_movement_sintetic*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_sintetic
	(
		company_id BIGINT,
		week_year INT,
        date_from DATE,
        date_to DATE,
        movement_type_id TINYINT,
		movement_description VARCHAR(100),
		group_type_id TINYINT, -- 0 Debit, 1 Credit       
		total DECIMAL(19,2),
        order_item TINYINT
	);
	/**End Temporary table to insert tmp_movement_sintetic*/
    
	/**Temporary table to insert tmp_movement_analitic*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_analitic
	(
		company_id BIGINT,
		week_year INT,
        date_from DATE,
        date_to DATE,
        payment_data DATE,
        provider_id BIGINT,
        bank_account_id BIGINT,
        bank_account_origin_id  BIGINT,
        document_id BIGINT,
        document_parent_id BIGINT,
        document_type TINYINT,
        document_note VARCHAR(200),
        movement_type_id TINYINT,
		group_type_id TINYINT, -- 0 Debit, 1 Credit       
		total DECIMAL(19,2)
	);
	/**End Temporary table to insert tmp_movement_analitic*/
    
    /**
	IF(LENGTH(IFNULL(_company_ids, '')) > 0) THEN 
		BEGIN
			INSERT INTO tmp_company_values VALUES(_company_ids);
			SET @sql = concat("INSERT INTO tmp_company_filter (company_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_company_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
		END;
	END IF;   
    */
    
    INSERT INTO tmp_company_filter (company_id)
    SELECT id FROM company c
    INNER JOIN (SELECT client_id FROM company where id = _company_id LIMIT 1) f ON c.client_id = f.client_id;
	
	IF(1 = _operation) THEN /** REPORT SINTETIC */          
		BEGIN
            
				/**VARIABLES CURSOR*/
                DECLARE _has_movements_pending_process BIT DEFAULT 0;
				DECLARE _has_movements BIT DEFAULT 0;
                DECLARE _week_filter INT;
			
                
                /**Cursor to search documents*/
				DECLARE documents_cursor CURSOR FOR
                SELECT days FROM 
                (
					SELECT YEARWEEK(DATE_ADD(days, INTERVAL (CASE WHEN WEEKDAY(days) = 6 THEN 6 ELSE 7 END) DAY)) days FROM 
					(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) days FROM
					(select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
					(select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
					(select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,
					(select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,
					(select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v
					where days between date(_date_from) and DATE_ADD(date(_date_to), INTERVAL -1 DAY)
				) x GROUP BY days;
                
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
				OPEN documents_cursor;
				meuLoop: LOOP
				FETCH documents_cursor INTO _week_filter;
                
				IF _has_movements = 1 THEN
					LEAVE meuLoop;
				END IF;
                
                /**Has movements pending to process*/
                SET _has_movements_pending_process = 1;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 1, 'Saldo Anterior Pendencias (Fiados)', 1, 0, 1 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 2, 'Duplicatas', 1, 0, 2 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 3, 'Despesas Lojas', 1, 0, 3 FROM tmp_company_filter;

				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 4, 'Total Creditos', 1, 0, 4 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 5, 'Despesas Custo Software HAB', 2, 0, 5 FROM tmp_company_filter;
          
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 6, 'Despesas', 2, 0, 6 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 7, 'Caixa', 2, 0, 7 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 8, 'Valor Meninos', 2, 0, 8 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 9, 'Leituras Pendentes', 2, 0, 9 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 10, 'Total Debitos', 2, 0, 10 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 11, 'Fechamento Caixa', 3, 0, 11 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 12, 'Saldo Semana', 3, 0, 12 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, group_type_id, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 13, 'Saldo Acumulado', 3, 0, 13 FROM tmp_company_filter;

			  END LOOP meuLoop;
			  CLOSE documents_cursor;   
              
              /* Insert Saldo Anterior Fiados */
              INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
                CASE WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 1 AND group_type_id = 1
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN DATE_ADD(CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME), INTERVAL -7 DAY) 
								   AND DATE_ADD(CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME), INTERVAL -7 DAY) 
					AND m.payment_residue = 1 AND m.document_transfer_id > 0 AND m.inactive = 0;
			 /* End Insert Saldo Anterior Fiados */
             
			/* Insert Duplicatas */
			  -- INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  -- payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				x.company_id, x.week_year, x.date_from, x.date_to, x.movement_type_id, x.group_type_id, 
                SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
                MAX(ms.payment_data), MAX(ms.provider_id), MAX(ms.bank_account_id), MAX(ms.bank_account_origin_id), 
                MAX(ms.document_id), ms.document_parent_id, MAX(ms.document_type),  'MOV PROD'
              FROM 
              (
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id, m.document_parent_id
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 2 AND group_type_id = 1
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
                    AND m.is_product_movement = 1
                    AND m.document_status = 2
			  GROUP BY 
				    f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id, m.document_parent_id
			  ) x 
              INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY 
				    ms.document_parent_id, x.company_id, x.week_year, x.date_from, x.date_to, x.movement_type_id, x.group_type_id;
			 /* End Insert Duplicatas */
             
             /* Insert despesas loja */
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
				CASE WHEN credit = 1 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 3 AND group_type_id = 1
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.company_id <> _company_id
                    AND document_type = 3 AND m.document_status = 2 
                    AND IFNULL(document_transfer_id,0) = 0 
                    AND m.document_note LIKE '%DESPESA TRANSF. FEC. CAIXA >>%'
                    AND m.inactive = 0;
			 /* End Insert despesas loja */
             
             /* Insert Custo Software HAB */
             /**
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
				CASE WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 5 AND group_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND document_type in (1, 4)
                    AND m.document_status = 2 
                    AND provider_id = 2379
                    AND m.inactive = 0;
                    */
			 /* End Insert Custo Software HAB */    
             
			 /* Insert despesas dif caixas */
             /**
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
				CASE WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 6 AND group_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.company_id = _company_id
                    AND document_type = 3 AND m.document_status = 2 
                    AND IFNULL(document_transfer_id,0) = 0 
                    AND m.document_note LIKE '%DESPESA TRANSF. FEC. CAIXA >>%'
                    AND m.inactive = 0;
                    */
			 /* End Insert despesas dif caixas */
             
			  /* Insert fechamento caixas */
              /**
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
				CASE WHEN credit = 1 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 7 AND group_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.company_id = _company_id
                    AND document_type = 2 AND m.document_status = 2 
                    AND m.document_note LIKE '%TRANSF. FEC. CAIXA >>%'
                    AND m.inactive = 0;
                    */
			 /* End Insert fechamento caixas */
             
              /* Insert Leituras Pendentes */
              /**
              INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, group_type_id, total, 
			  payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, f.group_type_id,
                CASE WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id, group_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 9 AND group_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id, group_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.payment_residue = 1 AND m.document_transfer_id > 0 AND m.inactive = 0;
                    */
			 /* End Leituras Pendentes */             
                    
                    /**
			SELECT 
				c.social_name company_description, s.company_id, s.week_year, s.date_from, s.date_to, s.movement_type_id, 
                s.movement_description, s.group_type_id, s.total, s.order_item
            FROM 
				tmp_movement_sintetic s
			INNER JOIN 
				company c ON s.company_id = c.id
            ORDER BY s.company_id, s.week_year, s.group_type_id, s.movement_type_id;

			SELECT 
				company_id, week_year, date_from, date_to, payment_data, provider_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note, movement_type_id, group_type_id, total
            FROM 
				tmp_movement_analitic a 
			ORDER BY 
				a.company_id, a.week_year, a.group_type_id, a.movement_type_id;
                */
                
		END; /** End operation 1 */
	END IF; /** End operation 1 */
END$$


CALL pr_cashier_movement_report(1, '182', '2024-01-01 00:00:00', '2023-01-07 23:59:59', '8');
