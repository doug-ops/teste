USE `manager`;
DROP PROCEDURE IF EXISTS `pr_financial_cash_flow`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_financial_cash_flow` (IN _operation TINYINT, 
										   IN _company_id BIGINT,
										   IN _date_from DATETIME, 
										   IN _date_to DATETIME,
										   IN _users_children_parent VARCHAR(5000),
                                           IN _group_by INT)
BEGIN    

	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR FINANCIAL CASH FLOW', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;
        
	/**Drop temporary tables*/
    
    DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_user_filter;  
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter; 
    DROP TEMPORARY TABLE IF EXISTS tmp_company_principal_filter; 
    
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
    
	/**Temporary table to insert tmp_company_principal_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_principal_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_company_principal_filter*/
    
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
		company_id BIGINT,
        payment_data DATE,
        provider_id BIGINT,
		financial_group_id VARCHAR(7),
		financial_sub_group_id VARCHAR(7),
        bank_account_id BIGINT,
        bank_account_origin_id  BIGINT,
        document_parent_id BIGINT,
        document_type TINYINT,
        document_note VARCHAR(500),
        movement_type_id TINYINT,
        transaction_same_company BIT,
		total DECIMAL(19,2)
	);
	/**End Temporary table to insert tmp_movement_analitic*/
    
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
    
	IF(IFNULL(_group_by, 0) = 1) THEN  
		BEGIN
        
			INSERT INTO tmp_user_values VALUES(_users_children_parent);
			SET @sql = concat("INSERT INTO tmp_user_filter (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
            
			INSERT INTO tmp_company_principal_filter (company_id)
			SELECT id FROM company c
			INNER JOIN (SELECT company_id FROM user_company WHERE user_id IN (SELECT user_id FROM tmp_user_filter) GROUP BY company_id) uc ON c.id = uc.company_id;

		END;
	ELSE 
		BEGIN
			INSERT INTO tmp_company_principal_filter (company_id)
            SELECT _company_id;
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
			   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
               SELECT 
				x.company_id, 
                -- (select social_name from company where id = x.company_id) company_description, 
                1 movement_type_id,
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
				  GROUP BY 
					cf.company_id, m.document_parent_id
			    ) x 
                INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY 
				ms.document_parent_id, x.company_id;
			 /* End Insert Recebimento Previsto */
             
			/* Recebimento Previsto residual = Valores das duplicatas geradas em execução das lojas. Essa que na planilha onde tem os valores de pendências está descrimada como “duplicatas”  */
			   IF(IFNULL(_group_by, 0) = 0) THEN  
					BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 1 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id, m.document_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
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
                            AND m.bank_account_origin_id not in (446, 445, 605)
						  GROUP BY 
							cf.company_id, m.document_parent_id, m.document_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.document_id = ms.document_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, ms.document_id, x.company_id;
					END;
				ELSE
					BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 1 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_id) bank_account_origin_id, 
						MAX(ms.document_parent_id), MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
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
                            AND m.bank_account_origin_id not in (446, 445, 605)
						  GROUP BY 
							cf.company_id, m.document_parent_id 
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						x.company_id;
                    END;
				END IF;
			 /* End Recebimento Previsto residual */
                  
			  /* Recebimento Realizado = Valores de créditos que entram nas contas da empresa 182 ou da empresa 1 que tenha origem de lojas ou operadores (não podemos considerar as transferências entre contas da própria empresa seja a 182 ou a 1. Esse crédito pode ter vindo via fechamento de caixa ou via transferência de outras empresas (lojas/operadores) clientes. */
			   IF(IFNULL(_group_by, 0) = 0) THEN  
					BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 2 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id, m.document_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.document_status = 2 
							AND m.credit = 1
							AND IFNULL(m.is_product_movement, 0) = 0
							AND m.bank_account_id not in (446, 445, 605)
						  GROUP BY 
							cf.company_id, m.document_parent_id, m.document_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.document_id = ms.document_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, ms.document_id, x.company_id;
					END;
				ELSE
					BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 2 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_id) bank_account_origin_id, 
						MAX(ms.document_parent_id), MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.document_status = 2 
							AND m.credit = 1
							AND m.bank_account_id not in (446, 445, 605)
						  GROUP BY 
							cf.company_id, m.document_parent_id 
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						x.company_id;
                    END;
				END IF;
			 /* End Recebimento Realizado */
             
			/* Pagamento Realizado = Todas despesas lançadas nas empresas 1 ou 182 já baixadas, seja lançada pela área “novo documento” ou lançada no fechamento de caixa ou transferência.*/
			   IF(IFNULL(_group_by, 0) = 0) THEN  
				  BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 3 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.document_status = 2 
							AND m.credit = 0
							AND IFNULL(m.is_product_movement, 0) = 0
							AND m.bank_account_id not in (446, 445, 605)
							AND document_note NOT LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%'
						  GROUP BY 
							cf.company_id, m.document_parent_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, x.company_id;                  
                  END;
			   ELSE
				  BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 3 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.document_status = 2 
							AND m.credit = 0
							AND IFNULL(m.is_product_movement, 0) = 0
							AND m.bank_account_id not in (446, 445, 605)
							AND document_note LIKE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA:%'
						  GROUP BY 
							cf.company_id, m.document_parent_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, x.company_id;                  
                  END;
			   END IF;               
			 /* End Pagamento Realizado */
             
			/* Pagamento Previsto = Despesas lançadas na 182 ou na 1 que não esteja baixado. (Ainda nao temos isso ativo mas futuramente vamos necessitar de lançar despesas futuras como um “contas a pagar” mas não baixar. Exemplo deixar lá lançado as despesas fixas futuras mas não baixar.*/
			   IF(IFNULL(_group_by, 0) = 0) THEN  
				  BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 4 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.credit = 0
							AND IFNULL(m.is_product_movement, 0) = 0
							AND m.bank_account_id not in (446, 445, 605)
							AND document_note NOT LIKE 'TRANSF. FEC. CAIXA >> ORIGEM%'
						  GROUP BY 
							cf.company_id, m.document_parent_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, x.company_id;                  
                  END;
			   ELSE
                  BEGIN
					   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
					   SELECT 
						x.company_id, 4 movement_type_id,
						SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
						MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
						MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
						ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 0 transaction_same_company
						FROM 
						(              
						  SELECT 
							cf.company_id, m.document_parent_id
						  FROM 
							(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
							INNER JOIN document_movement m ON m.company_id = cf.company_id
						  WHERE
							m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
							AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
							AND m.inactive = 0
							AND m.credit = 0
							AND IFNULL(m.is_product_movement, 0) = 0
							AND m.bank_account_id not in (446, 445, 605)
							AND document_note LIKE 'TRANSF. FEC. CAIXA >> DESTINO EMPRESA:%'
						  GROUP BY 
							cf.company_id, m.document_parent_id
						) x 
						INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
					  GROUP BY 
						ms.document_parent_id, x.company_id;                                    
                  END;
			   END IF;
			 /* End Pagamento Previsto */    
             
			/* Transferências entre contas  = Apenas transferências entre as contas da empresa 1 ou 182. Seja ela uma Transferências de entrada (créditos) ou Transferência de saída (débitos) */
			   INSERT INTO tmp_movement_analitic(company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company)
               SELECT company_id, movement_type_id, total, payment_data, provider_id, financial_group_id, financial_sub_group_id, bank_account_id, bank_account_origin_id, document_parent_id, document_type, document_note, transaction_same_company
               FROM (
               SELECT 
				x.company_id, 5 movement_type_id,
                SUM(CASE WHEN credit = 1 THEN ms.document_value ELSE (ms.document_value * -1) END) total,
                MAX(ms.payment_data) payment_data, MAX(ms.provider_id) provider_id, MAX(ms.financial_group_id) financial_group_id, MAX(ms.financial_sub_group_id) financial_sub_group_id, 
                MAX(ms.bank_account_id) bank_account_id, MAX(ms.bank_account_origin_id) bank_account_origin_id, 
                ms.document_parent_id, MAX(ms.document_type) document_type,  MAX(ms.document_note) document_note, 
				CASE WHEN x.company_id IN (1, 182) AND MAX(ms.bank_account_id) not in (446, 445) AND MAX(ms.bank_account_origin_id) not in (446, 445) THEN IFNULL((SELECT 1 FROM tmp_bank_account bfo WHERE bfo.company_id = x.company_id AND bfo.bank_account_id = MAX(ms.bank_account_origin_id) LIMIT 1),0) 
					 ELSE 0 END transaction_same_company
                FROM 
                (              
				  SELECT 
					cf.company_id, m.document_parent_id 
				  FROM 
					(SELECT company_id FROM tmp_company_principal_filter GROUP BY company_id) cf
					INNER JOIN document_movement m ON m.company_id = cf.company_id
				  WHERE
					m.payment_data BETWEEN CAST(CONCAT(DATE_FORMAT(_date_from,'%Y-%m-%d'), ' 00:00:00') AS DATETIME)
					AND CAST(CONCAT(DATE_FORMAT(_date_to,'%Y-%m-%d'), ' 23:59:59') AS DATETIME)
					AND m.inactive = 0
					AND m.document_status = 2 
					AND IFNULL(m.is_product_movement, 0) = 0
                    AND m.document_type = 2
				  GROUP BY 
					cf.company_id, m.document_parent_id
			    ) x 
                INNER JOIN document_movement ms ON x.document_parent_id = ms.document_parent_id AND x.company_id = ms.company_id
              GROUP BY 
				ms.document_parent_id, x.company_id ) a
			  WHERE a.transaction_same_company = 1;
			 /* End Transferências entre contas */   
             
			IF(IFNULL(_group_by, 0) = 0) THEN 
				BEGIN
					SELECT 
						c.id company_id, c.social_name company_description
					FROM  
						company c
                        INNER JOIN tmp_company_principal_filter f ON c.id = f.company_id
					WHERE 
						c.id = _company_id
					ORDER BY c.social_name; 
				END;
			ELSE 
				BEGIN
					SELECT 
						c.id company_id, c.social_name company_description
					FROM  
						company c
                        INNER JOIN tmp_company_filter f ON c.id = f.company_id
					ORDER BY c.social_name;                    
                END;
			END IF; 
            
			SELECT 
				u.id user_id, u.name user_name
			FROM  
				user u
				INNER JOIN tmp_user_filter uf ON u.id = uf.user_id
			GROUP BY u.id, u.name
			ORDER BY u.name; 

			SELECT
				a.company_id, c_destiny.social_name company_description, 
                date_format(a.payment_data,'%d/%m/%y') payment_data, date_format(a.payment_data,'%y%m%d') payment_data_long,
                a.provider_id, IFNULL(p_origin.social_name,'NAO INFORMADO') AS provider_description, 
                IFNULL(a.financial_group_id, a.provider_id) AS financial_group_id, IFNULL((SELECT fg.description FROM financial_group fg WHERE a.financial_group_id = fg.id LIMIT 1),'NAO INFORMADO') AS financial_group_description,
				IFNULL(a.financial_sub_group_id,'0') AS financial_sub_group_id, IFNULL((SELECT fsg.description FROM financial_sub_group fsg WHERE a.financial_sub_group_id = fsg.id LIMIT 1),'NAO INFORMADO') AS financial_sub_group_description,
                a.bank_account_id, ba.description bank_account_description,
                a.bank_account_origin_id, ba_origin.description AS bank_account_origin_description, 
                a.transaction_same_company,                    
				CASE WHEN a.company_id IN (1, 182) AND a.document_type = 2 AND (a.bank_account_id in (446, 445) OR a.bank_account_origin_id in (446, 445)) THEN 1  ELSE 0 END profit_distribution,
                a.document_parent_id, a.document_type, a.document_note, a.movement_type_id, a.total, 
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
                a.movement_type_id, a.payment_data, c_destiny.social_name, ba_origin.description, a.document_parent_id, document_description;

		END; /** End operation 1 */
	END IF; /** End operation 1 */
END$$

CALL pr_financial_cash_flow(1, 182, '2023-08-01 00:00:00', '2023-10-22 23:59:59', '8', 1);

