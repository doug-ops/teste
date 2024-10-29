USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_financial_transfer`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_financial_transfer`(IN _operation INT,
													IN _bank_account_origin_id	INT,
													IN _bank_account_destiny_id	INT,												
                                                    IN _group_id INT,
                                                    IN _execution_order_group TINYINT,
                                                    IN _group_description VARCHAR(100), 
                                                    IN _inactive INT,	
                                                    IN _group_item_id INT,
													IN _execution_order_item TINYINT,
													IN _description_transfer VARCHAR(100),
													IN _provider_id	BIGINT, 
                                                    IN _transfer_type TINYINT,
   													IN _value_transfer	DECIMAL(19,2),
													IN _bank_account_id	INT,
													IN _credit_debit BIT,
													IN _transfer_state	TINYINT,
													IN _is_over_total	BIT,
                                                    IN _expense TINYINT,
													IN _is_use_remaining_balance BIT,
													IN _inactive_transfer BIT,
                                                    IN _products VARCHAR(1000), 
                                                    IN _user_change	BIGINT, 
                                                    IN _bank_account_automatic_transfer_id	INT, 
                                                    IN _execution_period INT,
                                                    IN _execution_initial_period Date, 
                                                    IN _fixed_day BIT, 
                                                    IN _week_day INT,
                                                    IN _automatic_processing BIT, 
                                                    IN _move_launch_type INT,
                                                    IN _financial_group_id VARCHAR(7),
                                                    IN _financial_sub_group_id VARCHAR(7),
                                                    IN _execution_days VARCHAR(50),
                                                    IN _execution_time VARCHAR(5),
                                                    IN _fixed_day_month BIT,
                                                    IN _group_product_id INT,
                                                    IN _sub_group_product_id INT
                                                    )
BEGIN
	IF (1=_operation) THEN /** SAVE */
		BEGIN
			DECLARE v_company_id BIGINT;
			SELECT social_name INTO _description_transfer FROM provider WHERE id=_provider_id LIMIT 1; 
            
			IF (SELECT 1 = 1 FROM financial_transfer_group_setting WHERE id=_group_id) THEN
				BEGIN
					UPDATE 
						financial_transfer_group_setting
					SET
						bank_account_origin_id=_bank_account_origin_id,
						bank_account_destiny_id=_bank_account_destiny_id,
                        bank_account_automatic_transfer_id=_bank_account_automatic_transfer_id,
						description=_group_description,
                        execution_order=_execution_order_group, 
                        execution_period=_execution_period,
                        execution_initial_period = _execution_initial_period,
                        fixed_day = _fixed_day, 
                        fixed_day_month = _fixed_day_month,
                        week_day = _week_day,
                        automatic_processing = _automatic_processing, 
                        execution_days = _execution_days,
                        execution_time = _execution_time,
                        inactive=_inactive, 
                        user_change=_user_change, 
                        change_date=now()
					WHERE
						id=_group_id;
                        
					IF(_inactive = 1) THEN
						BEGIN
							DELETE FROM financial_transfer_product_setting WHERE group_id = _group_id AND bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id;
                            DELETE FROM financial_transfer_group_item_setting WHERE group_id = _group_id AND bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id;
                            DELETE FROM financial_transfer_group_setting WHERE id = _group_id AND bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id;
						END;
                    END IF;
				END; /** End Update financial_transfer_group_setting */
			ELSE
				BEGIN
					INSERT INTO financial_transfer_group_setting (id, bank_account_origin_id, bank_account_destiny_id, bank_account_automatic_transfer_id, description, execution_order, execution_period, execution_initial_period, inactive, user_creation, creation_date, user_change, change_date, fixed_day, fixed_day_month, week_day, automatic_processing, execution_days, execution_time)
					SELECT _group_id, _bank_account_origin_id, _bank_account_destiny_id, _bank_account_automatic_transfer_id, _group_description, _execution_order_group, _execution_period, _execution_initial_period, _inactive, _user_change, now(), _user_change, now(), _fixed_day, _fixed_day_month, _week_day, _automatic_processing, _execution_days, _execution_time;
				END; /** End insert financial_transfer_group_setting */
			END IF; /** End operation 1 financial_transfer_group_setting */     
            IF(_group_item_id>0) THEN
				BEGIN
					IF (SELECT 1 = 1 FROM financial_transfer_group_item_setting WHERE id=_group_item_id) THEN
						BEGIN
							UPDATE 
								financial_transfer_group_item_setting
							SET
								group_id=_group_id,
								bank_account_origin_id=_bank_account_origin_id,
								bank_account_destiny_id=_bank_account_destiny_id,
								execution_order=_execution_order_item,
								description_transfer=_description_transfer,
								provider_id=_provider_id,
								transfer_type=_transfer_type,
								value_transfer=_value_transfer,
								bank_account_id=_bank_account_id,
                                group_product_id = _group_product_id,
                                sub_group_product_id = _sub_group_product_id,
								credit_debit=_credit_debit,
								transfer_state=_transfer_state,
                                move_launch_type = _move_launch_type,
								is_over_total=_is_over_total,
                                expense = _expense,
								is_use_remaining_balance=_is_use_remaining_balance,
								financial_group_id = _financial_group_id,
                                financial_sub_group_id = _financial_sub_group_id,                                                    
								inactive=_inactive_transfer,
								user_change=_user_change,
								change_date=now()
							WHERE
								id=_group_item_id;
                                
								IF(_inactive_transfer = 1) THEN
									BEGIN
										DELETE FROM financial_transfer_group_item_setting WHERE id=_group_item_id AND group_id = _group_id AND bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id;
									END;
								END IF;
							END; /** End Update financial_transfer_group_item_setting*/
						ELSE
							BEGIN
								INSERT INTO financial_transfer_group_item_setting (id, group_id, bank_account_origin_id, bank_account_destiny_id, provider_id, execution_order, description_transfer, transfer_type, value_transfer, bank_account_id, credit_debit, transfer_state, is_over_total, expense, is_use_remaining_balance, inactive, user_creation, creation_date, user_change, change_date, move_launch_type, financial_group_id, financial_sub_group_id, group_product_id, sub_group_product_id)
								SELECT	_group_item_id, _group_id, _bank_account_origin_id, _bank_account_destiny_id, _provider_id, _execution_order_item, _description_transfer, _transfer_type, _value_transfer, _bank_account_id, _credit_debit, _transfer_state, _is_over_total, _expense, _is_use_remaining_balance, _inactive_transfer, _user_change, now(), _user_change, now(), _move_launch_type, _financial_group_id, _financial_sub_group_id, _group_product_id, _sub_group_product_id;
							END; /** End insert financial_transfer_group_item_setting*/
					END IF; /** End operation 1 financial_transfer_group_item_setting */
				END;
			END IF;
			/**financial_transfer_product_setting operation 1*/
  			UPDATE financial_transfer_product_setting SET inactive=1 WHERE group_id=_group_id;
			IF(LENGTH(IFNULL(_products,''))>0 	AND _inactive = 0) THEN
				BEGIN                
					/**Create table key products */
					DROP TEMPORARY TABLE IF EXISTS temp_values;
					CREATE TEMPORARY TABLE temp_values(txt text);                    
                    DROP TEMPORARY TABLE IF EXISTS temp_products;
					CREATE TEMPORARY TABLE temp_products(product_id BIGINT);
					INSERT INTO temp_values VALUES(_products);
					SET @sql = concat("INSERT INTO temp_products (product_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM temp_values), ",", "'),('"),"');");
					PREPARE stmt1 FROM @sql;
					EXECUTE stmt1;
                    
                    INSERT INTO financial_transfer_product_setting
					SELECT
						_group_id, _bank_account_origin_id, _bank_account_destiny_id, x.product_id, 0, _user_change, now(), _user_change, now()
					FROM
						temp_products x
					WHERE 
						x.product_id NOT IN (SELECT 
												product_id 
											FROM 
												financial_transfer_product_setting 
											WHERE
												group_id=_group_id);
                                                
					UPDATE 
						financial_transfer_product_setting 
					set 
						bank_account_origin_id=_bank_account_origin_id,
                        bank_account_destiny_id=_bank_account_destiny_id,
						inactive=0, user_change=_user_change, change_date=change_date
					WHERE
						group_id=_group_id
                        AND product_id IN (SELECT product_id FROM  temp_products);
                END;
			END IF; /** End operation 1 financial_transfer_product_setting */
        END; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		BEGIN
			select 2;
        END; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		BEGIN
			SELECT 
				gu.id as group_id,
				gu.bank_account_origin_id, 
				(SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_origin_id) as bank_account_origin_description,
				gu.bank_account_destiny_id,
				(CASE WHEN gu.bank_account_destiny_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_destiny_id) END) as bank_account_destiny_description,
				gu.execution_period,
				gu.execution_initial_period,
                gu.fixed_day, 
                gu.fixed_day_month, 
                gu.week_day,
                gu.automatic_processing,
                IFNULL(gu.execution_days,'') execution_days, 
                IFNULL(gu.execution_time,'') execution_time,
				gu.bank_account_automatic_transfer_id,
				(CASE WHEN gu.bank_account_automatic_transfer_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_automatic_transfer_id) END) as bank_account_automatic_transfer_description,                        
				gu.description as group_description,
				gu.execution_order as group_execution_order, 
				gu.inactive as inactive_group,
				gu.user_change,
				(SELECT IFNULL(name,'-') FROM user WHERE id=gu.user_change) as user_change_name,
				gu.change_date,						
				gi.id as group_item_id,
				gi.execution_order as group_item_execution_order,
				IFNULL(gi.provider_id,0) as provider_id,
				IFNULL(pv.social_name,'-') as provider_name,
                IFNULL(gi.financial_group_id, pv.financial_group_id) as financial_group_id,
				(SELECT IFNULL(description,'-') FROM financial_group WHERE id = IFNULL(gi.financial_group_id, pv.financial_group_id)) as description_financial_group,
				IFNULL(gi.financial_sub_group_id, pv.financial_sub_group_id) as financial_sub_group_id,
				(SELECT IFNULL(description,'-') FROM financial_sub_group WHERE id = IFNULL(gi.financial_sub_group_id, pv.financial_sub_group_id)) as description_financial_sub_group,
				gi.description_transfer,
				gi.transfer_type,
				gi.value_transfer,
				gi.bank_account_id,
				(CASE WHEN gi.bank_account_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gi.bank_account_id) END) as bank_account_description,
				gi.credit_debit,
				gi.transfer_state,
                gi.move_launch_type,
				gi.is_over_total,
				gi.expense,
				gi.is_use_remaining_balance,
				gi.inactive as inactive_transfer, 
                gi.group_product_id,
                IFNULL(gp.description, 'Nao Informado')  AS group_product_description,
                gi.sub_group_product_id,
                IFNULL(sgp.description, 'Nao Informado')  AS sub_group_product_description
			FROM                    
				financial_transfer_group_setting gu
				LEFT JOIN financial_transfer_group_item_setting gi ON gu.bank_account_origin_id = gi.bank_account_origin_id AND gu.bank_account_destiny_id = gi.bank_account_destiny_id AND gu.id = gi.group_id 
                AND gi.id=(CASE WHEN IFNULL(_group_item_id,0)=0 THEN gi.id ELSE _group_item_id END)
                LEFT JOIN provider pv ON gi.provider_id = pv.id
                LEFT JOIN product_group gp ON gi.group_product_id = gp.id
                LEFT JOIN product_sub_group sgp ON gi.sub_group_product_id = sgp.id
			WHERE
				gu.id=(CASE WHEN IFNULL(_group_id,0)=0 THEN gu.id ELSE _group_id END)
				AND gu.bank_account_origin_id = _bank_account_origin_id 
				AND gu.bank_account_destiny_id = _bank_account_destiny_id;
        END; /** End operation 3 */
	ELSEIF(4=_operation) THEN /** GETALL */
		BEGIN
			SELECT 
				gu.id as group_id,
				gu.bank_account_origin_id, 
				(SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_origin_id) as bank_account_origin_description,
				gu.bank_account_destiny_id,
				(CASE WHEN gu.bank_account_destiny_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_destiny_id) END) as bank_account_destiny_description,
				gu.bank_account_automatic_transfer_id,
                gu.execution_period,
                gu.execution_initial_period,
                gu.fixed_day, 
                gu.fixed_day_month,
                gu.week_day,
                gu.automatic_processing,
				IFNULL(gu.execution_days,'') execution_days, 
                IFNULL(gu.execution_time,'') execution_time,
                (CASE WHEN gu.bank_account_automatic_transfer_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_automatic_transfer_id) END) as bank_account_automatic_transfer_description,
                gu.description as group_description,
				gu.execution_order as group_execution_order, 
				gu.inactive as inactive_group,
				gu.user_change,
				(SELECT IFNULL(name,'-') FROM user WHERE id=gu.user_change) as user_change_name,
				gu.change_date,						
				gi.id as group_item_id,
				gi.execution_order as group_item_execution_order,
				IFNULL(gi.provider_id,0) as provider_id,
				IFNULL(pv.social_name,'-') as provider_name,
				IFNULL(gi.financial_group_id, pv.financial_group_id) as financial_group_id,
				(SELECT IFNULL(description,'-') FROM financial_group WHERE id = IFNULL(gi.financial_group_id, pv.financial_group_id)) as description_financial_group,
				IFNULL(gi.financial_sub_group_id, pv.financial_sub_group_id) as financial_sub_group_id,
				(SELECT IFNULL(description,'-') FROM financial_sub_group WHERE id = IFNULL(gi.financial_sub_group_id, pv.financial_sub_group_id)) as description_financial_sub_group,
				gi.description_transfer,
				gi.transfer_type,
                gi.move_launch_type,
				gi.value_transfer,
				gi.bank_account_id,
				(CASE WHEN gi.bank_account_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gi.bank_account_id) END) as bank_account_description,
				gi.credit_debit,
				gi.transfer_state,
				gi.is_over_total,
				gi.expense,
				gi.is_use_remaining_balance,
				gi.inactive as inactive_transfer,
                gi.group_product_id,
				IFNULL(gp.description, 'Nao Informado')  AS group_product_description,
                gi.sub_group_product_id,
                IFNULL(sgp.description, 'Nao Informado')  AS sub_group_product_description
			FROM                    
				financial_transfer_group_setting gu
                INNER JOIN (
					SELECT 
					z.bank_account_id
					FROM
					(
					SELECT p.bank_account_id
					FROM person p INNER JOIN company c ON p.id = c.person_id   
					INNER JOIN user_company uc ON uc.user_id = _user_change and uc.company_id = c.id
					UNION ALL 
					SELECT pb.bank_account_id
					FROM person p INNER JOIN company c ON p.id = c.person_id   
					INNER JOIN user_company uc ON uc.user_id = _user_change and uc.company_id = c.id
					INNER JOIN person_bank_account pb ON pb.person_id = p.id
					) z group by z.bank_account_id
                ) x ON gu.bank_account_origin_id = x.bank_account_id
				LEFT JOIN financial_transfer_group_item_setting gi ON gu.bank_account_origin_id = gi.bank_account_origin_id AND gu.bank_account_destiny_id = gi.bank_account_destiny_id AND gu.id=gi.group_id
                LEFT JOIN provider pv ON gi.provider_id = pv.id
                LEFT JOIN product_group gp ON gi.group_product_id = gp.id
                LEFT JOIN product_sub_group sgp ON gi.sub_group_product_id = sgp.id
			WHERE
				gu.bank_account_origin_id=(CASE WHEN IFNULL(_bank_account_origin_id,0)=0 THEN gu.bank_account_origin_id ELSE _bank_account_origin_id END)  
				AND gu.bank_account_destiny_id=(CASE WHEN IFNULL(_bank_account_destiny_id,0)=0 THEN gu.bank_account_destiny_id ELSE _bank_account_destiny_id END)
				AND IFNULL(gu.inactive,0) = (CASE WHEN _inactive=2 THEN ISNULL(gu.inactive) ELSE _inactive END)
                ORDER BY gu.execution_order, gi.execution_order;
		END; /** End operation 4 */
	ELSEIF(5=_operation) THEN /** GET products group */
		BEGIN
			SELECT 
				gu.id as group_id,
				pt.product_id, 
				po.description as product_description
			FROM                    
				financial_transfer_group_setting gu
				INNER JOIN financial_transfer_product_setting pt ON gu.id=pt.group_id
				LEFT JOIN product po ON pt.product_id=po.id AND po.inactive=0
			WHERE
				gu.id=_group_id
				AND gu.bank_account_origin_id=_bank_account_origin_id 
				AND gu.bank_account_destiny_id=_bank_account_destiny_id
				AND pt.inactive=0;
        END; /** End operation 5 */
   	ELSEIF(14=_operation) THEN /** Clona configuracao */
		BEGIN            
            DECLARE _has_itens BIT DEFAULT 0; 
            DECLARE _has_config_to_copy BIT DEFAULT 0; 
            
            DECLARE save_config_itens_cursor CURSOR FOR
            SELECT execution_order, description_transfer, provider_id, transfer_type, value_transfer, bank_account_id, credit_debit, transfer_state, is_over_total, expense, is_use_remaining_balance, inactive, move_launch_type FROM financial_transfer_group_item_setting WHERE bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = 0;
			
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_itens=1;
            
            SET _has_config_to_copy = (SELECT 1 FROM financial_transfer_group_setting WHERE bank_account_origin_id=_bank_account_origin_id AND bank_account_destiny_id=0 LIMIT 1);
            /**Verify jas config transfer to clone*/
			IF(0=_has_config_to_copy) THEN
				BEGIN
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = 'Não existe configuração para copiar da conta de origem.';
				END;
			END IF; /**End if Verify jas config transfer to clone */
            
			SET _group_id = (SELECT (MAX(id)+1) FROM financial_transfer_group_setting);
            SET _group_description = (SELECT description FROM bank_account WHERE id=_bank_account_destiny_id);
            
			INSERT INTO financial_transfer_group_setting(id, bank_account_origin_id, bank_account_destiny_id, execution_order, description, inactive, user_creation, creation_date, user_change, change_date, fixed_day, fixed_day_month, week_day, automatic_processing, execution_days, execution_time)
			SELECT _group_id, _bank_account_destiny_id, '0', '1', _group_description, 0, _user_change, now(), _user_change, now(), fixed_day, fixed_day_month, week_day, automatic_processing, execution_days, execution_time FROM financial_transfer_group_setting WHERE bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = 0;
            
			OPEN save_config_itens_cursor;
			meuLoop: LOOP
			FETCH save_config_itens_cursor INTO _execution_order_item, _description_transfer, _provider_id, _transfer_type, _value_transfer, _bank_account_id, _credit_debit, _transfer_state, _is_over_total, _expense, _is_use_remaining_balance, _inactive_transfer, _move_launch_type;

			IF _has_itens = 1 THEN
				LEAVE meuLoop;
			END IF; 
   
            SET _group_item_id = (SELECT (MAX(id)+1) FROM financial_transfer_group_item_setting);
            INSERT INTO financial_transfer_group_item_setting(id, group_id, bank_account_origin_id, bank_account_destiny_id, execution_order, description_transfer, provider_id, transfer_type, value_transfer, bank_account_id, credit_debit, transfer_state, is_over_total, expense, is_use_remaining_balance, inactive, user_creation, creation_date, user_change, change_date, move_launch_type)
			SELECT _group_item_id, _group_id, _bank_account_destiny_id, 0, _execution_order_item, _description_transfer, _provider_id, _transfer_type, _value_transfer, (case when _bank_account_id = _bank_account_origin_id then _bank_account_destiny_id else _bank_account_id end), _credit_debit, _transfer_state, _is_over_total, _expense, _is_use_remaining_balance, _inactive_transfer, _user_change, now(), _user_change, now(), _move_launch_type;
            
			END LOOP meuLoop;
            CLOSE save_config_itens_cursor;
        END; /** End operation 14 */
	END IF;
END$$

/* Operations */
/* 1 - Save/Update */
/* 2 - Inactive */
/* 3 - Get */
/* 4 - GetAll */