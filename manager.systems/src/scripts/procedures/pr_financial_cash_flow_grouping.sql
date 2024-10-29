USE `manager`;
DROP PROCEDURE IF EXISTS `pr_financial_cash_flow_grouping`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_financial_cash_flow_grouping` (IN _operation TINYINT,
												    IN _date_from DATETIME, 
												    IN _date_to DATETIME,
                                                    IN _users_children_parent VARCHAR(5000),
                                                    IN _company_id BIGINT,
												    IN _bak_account_ids VARCHAR(5000))
BEGIN    

	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR FINANCIAL CASH FLOW GROUPING', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;
        
	/**Drop temporary tables*/
    
    DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_user_filter;  
    
    DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_filter;
    
    DROP TEMPORARY TABLE IF EXISTS tmp_company_filter; 
        
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

	/**Temporary table to insert tmp_bank_account_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_bank_account_values*/
    
	/**Temporary table to insert tmp_bank_account_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_filter
	(
		bank_account_id_id INT NOT NULL
	);
	/**End Temporary table to insert tmp_bank_account_filter*/
    
	/**Temporary table to insert tmp_company_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_company_filter*/
    
    /**Temporary table to insert tmp_bank_account*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account
	(
		company_id BIGINT NOT NULL,
        bank_account_id INT NOT NULL
	);
	/**End Temporary table to insert tmp_bank_account*/
    
	/**Temporary table to insert tmp_movement_analitic*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_analitic
	(
        movement_type_id TINYINT,
		company_id BIGINT,
        total DECIMAL(19,2),
        payment_data DATE,
        provider_id BIGINT,
		financial_group_id VARCHAR(7),
		financial_sub_group_id VARCHAR(7),
        bank_account_id BIGINT,
        bank_account_origin_id  BIGINT,
		document_parent_id BIGINT,
        document_type_id TINYINT,
        document_note VARCHAR(500),
        transaction_same_company BIT
	);
	/**End Temporary table to insert tmp_movement_analitic*/
    
	IF(LENGTH(IFNULL(_bak_account_ids, '')) > 0) THEN 
		BEGIN        
			INSERT INTO tmp_bank_account_values VALUES(_bak_account_ids);
            
			SET @sql = concat("INSERT INTO tmp_bank_account_filter (bank_account_id_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_bank_account_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
		END;
	END IF; 
    
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
    
	INSERT INTO tmp_bank_account(company_id, bank_account_id)
	SELECT cb.id company_id, pb.bank_account_id FROM company cb 
	INNER JOIN person pb ON cb.person_id = pb.id -- WHERE cb.id = _company_id
	UNION ALL
	SELECT cb.id company_id, pba.bank_account_id FROM company cb 
	INNER JOIN person pb ON cb.person_id = pb.id
	INNER JOIN person_bank_account pba ON pb.id = pba.person_id -- WHERE cb.id = _company_id
	GROUP BY company_id, bank_account_id;
        
	IF(1 = _operation) THEN /** REPORT SINTETIC */          
		BEGIN                     
			  /* Recebimento Previsto = Valores das duplicatas geradas em execução das lojas. Essa que na planilha onde tem os valores de pendências está descrimada como “duplicatas”  */
			    INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
                SELECT 
				1 movement_type_id,
				x.company_id, 
                -- (select social_name from company where id = x.company_id) company_description,                
                SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
                MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MIN(ms.financial_group_id) financial_group_id, MIN(ms.financial_sub_group_id) financial_sub_group_id, 
                MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
                ms.document_parent_id, MAX(ms.document_type) document_type,  'FATURAMENTO LOJA' document_note, 0 is_same_account
                FROM 
                (                              
				  SELECT 
					cf.company_id, m.document_parent_id
				  FROM 
					tmp_company_filter cf
					INNER JOIN document_movement m ON m.company_id = cf.company_id
				  WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND IFNULL(m.is_product_movement, 0) = 1
					AND m.document_status = 2
                    AND m.company_id not in (1, 182)
				  GROUP BY 
					cf.company_id, m.document_parent_id
			  ) x 
              INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY  ms.document_parent_id, x.company_id;
			 /* End Insert Recebimento Previsto */
             
			/* Recebimento Previsto residual = Valores das duplicatas geradas em execução das lojas. Essa que na planilha onde tem os valores de pendências está descrimada como “duplicatas”  */
		        INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
			    SELECT 
				1 movement_type_id, x.company_id, 
				SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
				MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
				MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_id) bank_account_origin_id, 
				ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
				FROM 
				(              
				  SELECT 
					cf.company_id, m.document_parent_id
				  FROM 
					tmp_company_filter cf
					INNER JOIN document_movement m ON m.company_id = cf.company_id
				  WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.document_status = 1 
					AND m.credit = 0
					AND m.document_type = 3
					AND m.is_cashing_close = 1
					AND IFNULL(m.is_product_movement, 0) = 0
					AND m.bank_account_id not in (446, 445, 605)
				  GROUP BY 
					cf.company_id, m.document_parent_id
				) x 
				INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
			    GROUP BY  ms.document_parent_id, x.company_id;
			 /* End Recebimento Previsto residual */
                  
			  /* Recebimento Realizado = Valores de créditos que entram nas contas da empresa 182 ou da empresa 1 que tenha origem de lojas ou operadores (não podemos considerar as transferências entre contas da própria empresa seja a 182 ou a 1. Esse crédito pode ter vindo via fechamento de caixa ou via transferência de outras empresas (lojas/operadores) clientes. */
			   INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
			   SELECT 
				2 movement_type_id, x.company_id, 
				SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
				MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
				MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
				ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
				FROM 
				(              
				  SELECT 
					m.company_id, m.document_parent_id, m.document_id
				  FROM 
					document_movement m
				  WHERE
					m.company_id = _company_id
					AND m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.document_status = 2 
					AND m.credit = 1
					AND IFNULL(m.is_product_movement, 0) = 0
					AND m.bank_account_id not in (446, 445, 605)
				  GROUP BY 
					m.company_id, m.document_parent_id, m.document_id
				) x 
				INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.document_id = ms.document_id AND x.company_id = ms.company_id
			  GROUP BY 
				ms.document_parent_id, ms.document_id, x.company_id;             
			 /* End Recebimento Realizado */
             
			/* Pagamento Realizado = Todas despesas lançadas nas empresas 1 ou 182 já baixadas, seja lançada pela área “novo documento” ou lançada no fechamento de caixa ou transferência.*/
			    INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
			    SELECT 
				3 movement_type_id, x.company_id, 
				SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
				MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
				MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
				ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
				FROM 
				(              
				  SELECT 
					m.company_id, m.document_parent_id, m.document_id
				  FROM 
					document_movement m
				  WHERE
					m.company_id = _company_id
					AND m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.document_status = 2 
					AND m.credit = 0
					AND IFNULL(m.is_product_movement, 0) = 0
					AND m.bank_account_id not in (446, 445, 605)
                    AND m.bank_account_origin_id not in (446, 445, 605)
					AND document_note NOT LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%'
				  GROUP BY 
					m.company_id, m.document_parent_id, m.document_id
				) x 
				INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
			  GROUP BY 
				ms.document_parent_id, ms.document_id, x.company_id;         
			 /* End Pagamento Realizado */
             
			/* Pagamento Previsto = Despesas lançadas na 182 ou na 1 que não esteja baixado. (Ainda nao temos isso ativo mas futuramente vamos necessitar de lançar despesas futuras como um “contas a pagar” mas não baixar. Exemplo deixar lá lançado as despesas fixas futuras mas não baixar.*/
			   INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
			   SELECT 
				4 movement_type_id, x.company_id, 
				SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
				MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
				MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
				ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
				FROM 
				(              
				  SELECT 
					m.company_id, m.document_parent_id, m.document_id
				  FROM 
					document_movement m
				  WHERE
					m.company_id = _company_id
					AND m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.credit = 0
					AND IFNULL(m.is_product_movement, 0) = 0
					AND m.bank_account_id not in (446, 445, 605)
                    AND m.bank_account_origin_id not in (446, 445, 605)
					AND document_note NOT LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%'
				  GROUP BY 
					m.company_id, m.document_parent_id, m.document_id
				) x 
				INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
			  GROUP BY 
				ms.document_parent_id, ms.document_id, x.company_id;
			 /* End Pagamento Previsto */    
             
			/* Transferências entre contas  = Apenas transferências entre as contas da empresa 1 ou 182. Seja ela uma Transferências de entrada (créditos) ou Transferência de saída (débitos) */
               INSERT INTO tmp_movement_analitic(movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type_id, document_note, transaction_same_company)
               SELECT movement_type_id, company_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company
               FROM (
               SELECT 
				5 movement_type_id, x.company_id, 
                SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
                MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
                MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
                ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 
				CASE WHEN x.company_id IN (1, 182) AND MAX(ms.bank_account_id) not in (446, 445) AND MAX(ms.bank_account_origin_id) not in (446, 445) THEN IFNULL((SELECT 1 FROM tmp_bank_account bfo WHERE bfo.company_id = x.company_id AND bfo.bank_account_id = MAX(ms.bank_account_origin_id) LIMIT 1),0) ELSE 0 END transaction_same_company
                FROM 
                (              
				  SELECT 
					m.company_id, m.document_parent_id, m.document_id
				  FROM 
					document_movement m
				  WHERE
					m.company_id = _company_id
					AND m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.document_status = 2 
					AND IFNULL(m.is_product_movement, 0) = 0
                    AND m.document_type = 2
				  GROUP BY 
					m.company_id, m.document_parent_id, m.document_id
			    ) x 
                INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY 
				ms.document_parent_id, ms.document_id, x.company_id ) a
			  WHERE a.transaction_same_company = 1;
			 /* End Transferências entre contas */   
             
            SELECT 
				c.id company_id, c.social_name company_description
			FROM  
				company c
				INNER JOIN tmp_company_filter f ON c.id = f.company_id
			WHERE
				c.id = _company_id
			ORDER BY c.social_name;     
            
			SELECT 
				u.id user_id, u.name user_name
			FROM  
				user u
				INNER JOIN tmp_user_filter uf ON u.id = uf.user_id
			GROUP BY u.id, u.name
			ORDER BY u.name; 

			SELECT
				a.company_id, c_destiny.social_name company_description, 
                date_format(a.payment_data,'%d/%m/%y') payment_data, date_format(a.payment_data,'%Y%m%d') payment_data_long,
                case when DAYOFWEEK(a.payment_data) = 1 then yearweek(a.payment_data -1, 1) else yearweek(a.payment_data, 1) end week_year, date_format(a.payment_data,'%Y%m') month_year,
                a.provider_id, IFNULL(p_origin.social_name,'NAO INFORMADO') AS provider_description, 
                IFNULL(a.financial_group_id, a.provider_id) AS financial_group_id, IFNULL((SELECT fg.description FROM financial_group fg WHERE a.financial_group_id = fg.id LIMIT 1),'NAO INFORMADO') AS financial_group_description,
				IFNULL(a.financial_sub_group_id,'0') AS financial_sub_group_id, IFNULL((SELECT fsg.description FROM financial_sub_group fsg WHERE a.financial_sub_group_id = fsg.id LIMIT 1),'NAO INFORMADO') AS financial_sub_group_description,
                a.bank_account_id, ba.description bank_account_description,
                a.bank_account_origin_id, ba_origin.description AS bank_account_origin_description, 
                a.transaction_same_company,                    
				CASE WHEN a.company_id IN (1, 182) AND a.document_type_id = 2 AND (a.bank_account_id in (446, 445) OR a.bank_account_origin_id in (446, 445)) THEN 1  ELSE 0 END profit_distribution,
                a.document_parent_id, a.document_type_id, a.document_note, a.movement_type_id, a.total, 
                CASE 
					WHEN IFNULL(a.provider_id,0) > 0 AND a.document_type_id IN (2, 3) THEN (CASE WHEN p_origin.social_name IS NULL THEN a.document_note ELSE  p_origin.social_name END)
                    WHEN a.document_type_id in (2) AND document_note LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%' THEN REPLACE(a.document_note, 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:', '')
                    WHEN a.document_type_id in (2) AND document_note LIKE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA:%' THEN a.document_note
                    WHEN IFNULL(a.provider_id,0) = 0 AND a.document_type_id IN (2, 3) THEN CONCAT(ba_origin.description,' \\ ',a.document_note)
                    WHEN a.document_type_id = 4 THEN a.document_note
                    ELSE NULL 
				END document_description
            FROM 
				tmp_movement_analitic a
                LEFT JOIN bank_account ba_origin ON a.bank_account_origin_id = ba_origin.id
                LEFT JOIN bank_account ba ON a.bank_account_id = ba.id
                LEFT JOIN provider p_origin ON a.provider_id = p_origin.id
                LEFT JOIN company c_destiny ON a.company_id = c_destiny.id
              WHERE CAST(date_format(a.payment_data,'%y%m%d') AS SIGNED) BETWEEN CAST(date_format(_date_from,'%y%m%d') AS SIGNED) AND CAST(date_format(_date_to,'%y%m%d') AS SIGNED)
			ORDER BY 
                a.movement_type_id, a.payment_data, c_destiny.social_name, ba_origin.description, a.document_parent_id, document_description;
                
             /**
                SELECT
                a.payment_data,
				case when DAYOFWEEK(a.payment_data) = 7 then yearweek(a.payment_data -1) else yearweek(a.payment_data) end week_year,       
                IFNULL(a.financial_group_id, a.provider_id) AS financial_group_id,
                a.movement_type_id, sum(a.total) total
            FROM 
				tmp_movement_analitic a
              WHERE CAST(date_format(a.payment_data,'%y%m%d') AS SIGNED) BETWEEN CAST(date_format(_date_from,'%y%m%d') AS SIGNED) AND CAST(date_format(_date_to,'%y%m%d') AS SIGNED)
			group by  a.payment_data,             
                IFNULL(a.financial_group_id, a.provider_id) ,
                a.movement_type_id;
             */
	
		END; /** End operation 1 */
	END IF; /** End operation 1 */
END$$

CALL pr_financial_cash_flow_grouping(1, '2023-11-20 00:00:00', '2023-12-03 23:59:59', '8', 182, '199');

-- select  YEARWEEK(date('2024-01-08 00:00:00'), 1)

-- select * from bank_account where id = 156
-- grupo 06.000
-- sub 06.003
-- 2379

SELECT * from document_movement where bank_account_id = 156 and ifnull(provider_id,0) = 0
SET SQL_SAFE_UPDATES = 0;
update document_movement set provider_id = 2379, financial_group_id = '06.000', financial_sub_group_id = '06.003' where bank_account_origin_id = 156 and ifnull(provider_id,0) = 0