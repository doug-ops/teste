USE `manager`;
DROP PROCEDURE IF EXISTS `pr_financial_incomes_expenses_report`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_financial_incomes_expenses_report` (IN _operation TINYINT, 
											   IN _companys_id VARCHAR(5000),
											   IN _date_from DATETIME, 
											   IN _date_to DATETIME,
                                               IN _users_children_parent VARCHAR(5000))
BEGIN    

	DECLARE  _date_from_week DATE; 
	DECLARE  _date_to_week DATE; 
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR COMPANY MOVEMENT REPORT', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;
        
	/**Drop temporary tables*/
    
    DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_user_filter;  
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;    
    
    DROP TEMPORARY TABLE IF EXISTS tmp_movement_sintetic;
    DROP TEMPORARY TABLE IF EXISTS tmp_movement_analitic;
    
    DROP TEMPORARY TABLE IF EXISTS tmp_bank_account;
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
    
	/**Temporary table to insert tmp_company_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_company_values*/
    
	/**Temporary table to insert tmp_company_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_company_filter*/
    
	/**Temporary table to insert tmp_movement_sintetic*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_sintetic
	(
		company_id BIGINT,
		week_year INT,
        date_from DATE,
        date_to DATE,
        movement_type_id TINYINT,
		movement_description VARCHAR(100),    
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
		financial_group_id VARCHAR(7),
		financial_sub_group_id VARCHAR(7),
        bank_account_id BIGINT,
        bank_account_origin_id  BIGINT,
        document_id BIGINT,
        document_parent_id BIGINT,
        document_type TINYINT,
        document_note VARCHAR(500),
        movement_type_id TINYINT,
		total DECIMAL(19,2)
	);
	/**End Temporary table to insert tmp_movement_analitic*/
    
	/**Temporary table to insert tmp_bank_account*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account
	(
		company_id BIGINT NOT NULL,
        bank_account_id INT NOT NULL
	);
	/**End Temporary table to insert tmp_bank_account*/
    
    IF(LENGTH(IFNULL(_companys_id, '')) > 0) THEN 
		BEGIN
			INSERT INTO tmp_company_values VALUES(_companys_id);
			SET @sql = concat("INSERT INTO tmp_company_filter (company_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_company_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
		END;
	ELSEIF(LENGTH(IFNULL(_users_children_parent, '')) > 0) THEN 
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

	SET _date_from_week = CASE WHEN WEEKDAY(date(_date_from)) = 6 THEN DATE_ADD(date(_date_from), INTERVAL -1 day) ELSE date(_date_from) END;
    SET _date_to_week = CASE WHEN WEEKDAY(date(_date_to)) = 6 THEN DATE_ADD(date(_date_to), INTERVAL -1 day) ELSE date(_date_to) END;
    
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
					SELECT yearweek(days) days FROM 
					(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) days FROM
					(select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
					(select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
					(select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,
					(select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,
					(select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v
					where days between date(_date_from_week) and DATE_ADD(date(_date_to_week), INTERVAL (CASE WHEN date(_date_from_week) = date(_date_to_week) THEN -0 ELSE -1 END) DAY)
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
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 1, 'Receitas', 0, 1 FROM tmp_company_filter;
                
				INSERT INTO tmp_movement_sintetic(company_id, week_year, date_from, date_to, movement_type_id, movement_description, total, order_item)
                SELECT company_id, _week_filter, func_yearweek_to_date(_week_filter, 'Monday'), func_yearweek_to_date(_week_filter, 'Sunday'), 2, 'Despesas',  0, 2 FROM tmp_company_filter;

			  END LOOP meuLoop;
			  CLOSE documents_cursor;   
								                      
			  /* Insert Duplicatas */
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, total, 
			  payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				x.company_id, x.week_year, x.date_from, x.date_to, x.movement_type_id,
                SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
                MAX(ms.payment_data), MAX(ms.provider_id), MAX(ms.financial_group_id), MAX(ms.financial_sub_group_id), MAX(ms.bank_account_id), MAX(ms.bank_account_origin_id), 
                MAX(ms.document_id), ms.document_parent_id, MAX(ms.document_type),  'FATURAMENTO LOJA'
              FROM 
              (
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, m.document_parent_id
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 1
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
                    AND m.is_product_movement = 1
                    AND m.document_status = 2
			  GROUP BY 
				    f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id, m.document_parent_id
			  ) x 
              INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY 
				    ms.document_parent_id, x.company_id, x.week_year, x.date_from, x.date_to, x.movement_type_id;
			 /* End Insert Duplicatas */
                  
			 /* Insert Insert movimentos de credito */
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, total, 
			  payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, (CASE WHEN  m.document_note LIKE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA:%' AND  m.company_id NOT IN (1, 182) THEN 2 
																		 WHEN  m.document_note LIKE 'RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA:%' AND  m.company_id NOT IN (1, 182) THEN 2
                ELSE f.movement_type_id END) movement_type_id,
				CASE WHEN credit = 1 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.financial_group_id, m.financial_sub_group_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 1
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
                    AND m.document_type in (2, 3) 
                    AND m.credit = 1 
                    AND m.document_status = 2
                    AND m.inactive = 0;
			 /* End Insert movimentos de credito */
             
			/* Insert Custo Software HAB */
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, total, 
			  payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, f.movement_type_id,
				CASE WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.financial_group_id, m.financial_sub_group_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND document_type in (1, 4)
                    AND m.document_status = 2 
                    AND provider_id = 2379
                    AND m.inactive = 0;
			 /* End Insert Custo Software HAB */ 
                  
			 /* Insert Insert movimentos de debito */
			  INSERT INTO tmp_movement_analitic(company_id, week_year, date_from, date_to, movement_type_id, total, 
			  payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_id, document_parent_id, document_type, document_note)
              SELECT 
				f.company_id, f.week_year, f.date_from, f.date_to, (CASE WHEN  m.document_note LIKE 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%' AND  m.company_id IN (1, 182) THEN 1 
                ELSE f.movement_type_id END) movement_type_id,
				CASE WHEN m.document_note LIKE 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%' AND  m.company_id IN (1, 182) AND credit = 0 THEN (m.document_value * -1)  WHEN credit = 0 THEN m.document_value ELSE (m.document_value * -1) END total,
                m.payment_data, m.provider_id, m.financial_group_id, m.financial_sub_group_id, m.bank_account_id, m.bank_account_origin_id, 
                m.document_id, m.document_parent_id, m.document_type,  m.document_note
              FROM 
              (
				  SELECT 
					company_id, week_year, date_from, date_to, movement_type_id FROM tmp_movement_sintetic s 
				  WHERE 
					s.movement_type_id = 2
				  GROUP BY company_id, week_year, date_from, date_to, movement_type_id
              ) f
              INNER JOIN document_movement m ON m.company_id = f.company_id
              WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(f.date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
								   AND CAST(CONCAT(DATE_FORMAT(f.date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
                    AND m.document_type in (2, 3) 
                    AND m.credit = 0
                    AND m.document_status = 2
                    AND m.inactive = 0;
			 /* End Insert movimentos de debito */
             
			INSERT INTO tmp_bank_account(company_id, bank_account_id)
            SELECT 
				c.company_id, x.bank_account_id 
            FROM
			(SELECT s.company_id FROM tmp_movement_sintetic s GROUP BY s.company_id) c 
			INNER JOIN 
			(
				SELECT cb.id company_id, pb.bank_account_id FROM company cb 
				INNER JOIN person pb ON cb.person_id = pb.id -- WHERE cb.id = 182
				UNION ALL
				SELECT cb.id company_id, pba.bank_account_id FROM company cb 
				INNER JOIN person pb ON cb.person_id = pb.id
				INNER JOIN person_bank_account pba ON pb.id = pba.person_id -- WHERE cb.id = 182
				GROUP BY company_id, bank_account_id
			) x ON c.company_id = x.company_id;
            
			SELECT 
				c.social_name company_description, s.company_id, s.week_year, s.date_from, s.date_to, s.movement_type_id, 
                s.movement_description, s.total, s.order_item
            FROM 
				tmp_movement_sintetic s
			INNER JOIN 
				company c ON s.company_id = c.id
            ORDER BY s.company_id, s.week_year, s.movement_type_id;

			SELECT
				a.company_id, c_destiny.social_name company_description, a.week_year, a.date_from, a.date_to, 
                date_format(a.payment_data,'%d/%m/%y') payment_data, date_format(a.payment_data,'%y%m%d') payment_data_long,
                a.provider_id, IFNULL(p_origin.social_name,'NAO INFORMADO') AS provider_description, 
                IFNULL(a.financial_group_id,'0') AS financial_group_id, IFNULL((SELECT fg.description FROM financial_group fg WHERE a.financial_group_id = fg.id LIMIT 1),'NAO INFORMADO') AS financial_group_description,
				IFNULL(a.financial_sub_group_id,'0') AS financial_sub_group_id, IFNULL((SELECT fsg.description FROM financial_sub_group fsg WHERE a.financial_sub_group_id = fsg.id LIMIT 1),'NAO INFORMADO') AS financial_sub_group_description,
                a.bank_account_id, ba.description bank_account_description,
                a.bank_account_origin_id, ba_origin.description AS bank_account_origin_description, 
                CASE WHEN a.company_id IN (1, 182) AND a.document_type = 2 AND a.bank_account_id not in (446, 445) AND a.bank_account_origin_id not in (446, 445) THEN IFNULL((SELECT 1 FROM tmp_bank_account bfo WHERE bfo.company_id = a.company_id AND bfo.bank_account_id = a.bank_account_origin_id LIMIT 1),0) 
					ELSE 0 END transaction_same_company,                    
				CASE WHEN a.company_id IN (1, 182) AND a.document_type = 2 AND (a.bank_account_id in (446, 445) OR a.bank_account_origin_id in (446, 445)) THEN 1  ELSE 0 END profit_distribution,
                a.document_id, a.document_parent_id, a.document_type, a.document_note, a.movement_type_id, a.total, 
                CASE 
					WHEN IFNULL(a.provider_id,0) > 0 AND a.document_type IN (2, 3) THEN (CASE WHEN p_origin.social_name IS NULL THEN a.document_note ELSE  p_origin.social_name END)
                    WHEN a.document_type in (2) AND document_note LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%' THEN REPLACE(a.document_note, 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:', '')
                    WHEN a.document_type in (2) AND document_note LIKE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA:%' THEN a.document_note
                    WHEN IFNULL(a.provider_id,0) = 0 AND a.document_type IN (2, 3) THEN CONCAT(ba_origin.description,' \\ ',a.document_note)
                    WHEN a.document_type = 4 THEN a.document_note
                    ELSE NULL 
				END document_description
            FROM 
				tmp_movement_analitic a
                LEFT JOIN bank_account ba_origin ON a.bank_account_origin_id = ba_origin.id
                LEFT JOIN bank_account ba ON a.bank_account_id = ba.id
                LEFT JOIN provider p_origin ON a.provider_id = p_origin.id
                LEFT JOIN company c_destiny ON a.company_id = c_destiny.id
              --  LEFT JOIN person p_origin ON a.bank_account_origin_id = p_origin.bank_account_id AND p_origin.inactive = 0
              --  LEFT JOIN company c_origin ON p_origin.id = c_origin.person_id
			  -- where a.provider_id in (3722	, 284, 3433, 287, 3543, 3404, 3837, 3838, 363)
              WHERE CAST(date_format(a.payment_data,'%y%m%d') AS SIGNED) BETWEEN CAST(date_format(_date_from,'%y%m%d') AS SIGNED) AND CAST(date_format(_date_to,'%y%m%d') AS SIGNED)
			ORDER BY 
				-- a.company_id, a.week_year, a.movement_type_id;
                a.payment_data, a.document_parent_id, c_destiny.social_name, document_description;
						
		END; /** End operation 1 */
	END IF; /** End operation 1 */
END$$

CALL pr_financial_incomes_expenses_report(1, '182', '2023-08-01 00:00:00', '2023-10-31 23:59:59', '8'  );