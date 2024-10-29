USE `manager`;
DROP PROCEDURE IF EXISTS `pr_bank_account_statement_report`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_bank_account_statement_report` (IN _operation INT, 
													 IN _bank_account_ids VARCHAR(500),
                                                     IN _person_ids VARCHAR(500),
                                                     IN _date_from DATETIME, 
                                                     IN _date_to DATETIME,
                                                     IN _financial_cost_center_id INT)
BEGIN    
	/**Drop temporary tables*/
	DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_filter;
    
	DROP TEMPORARY TABLE IF EXISTS tmp_person_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_person_filter;
	/**End Drop temporary tables*/

	/**Temporary table to insert tmp_bank_account_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_bank_account_values*/
    
	/**Temporary table to insert tmp_person_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_person_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_person_values*/
	
	/**Temporary table to insert tmp_bank_account_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_filter
	(
		bank_account_id INT NOT NULL
	);
	/**End Temporary table to insert tmp_bank_account_filter*/
    
	/**Temporary table to insert tmp_person_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_person_filter
	(
		person_id INT NOT NULL
	);
	/**End Temporary table to insert tmp_person_filter*/
	
	IF(1=_operation) THEN /** ANALITIC */      
		BEGIN
        	
	DECLARE _bank_balance_available DECIMAL(19,2);
			DECLARE _bank_balance_available_initial DECIMAL(19,2);
            DECLARE _total_movement DECIMAL(19,2);
            
			INSERT INTO tmp_bank_account_values VALUES(_bank_account_ids);
			SET @sql = concat("INSERT INTO tmp_bank_account_filter (bank_account_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_bank_account_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
	
            IF(LENGTH(IFNULL(_person_ids, '')) > 0) THEN 
				BEGIN
					INSERT INTO tmp_person_values VALUES(_person_ids);
					SET @sql = concat("INSERT INTO tmp_person_filter (person_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_person_values), ",", "'),('"),"');");
					PREPARE stmt1 FROM @sql;
					EXECUTE stmt1;
				END;
			ELSE
				BEGIN
					INSERT INTO tmp_person_filter
					SELECT id FROM person p WHERE p.bank_account_id IN (
						SELECT bank_account_id FROM tmp_bank_account_values
					);
				END;
			END IF;
           
			SELECT 
				IFNULL((SELECT SUM(IFNULL(ba.bank_balance_available,0)) 
			FROM 
				tmp_bank_account_filter f
				INNER JOIN bank_account ba ON f.bank_account_id=ba.id),0) INTO _bank_balance_available;     
            
			SELECT            
				(_bank_balance_available + IFNULL(SUM(s.document_value * -1),0)), IFNULL(SUM(s.document_value),0) INTO _bank_balance_available_initial, _total_movement
			FROM
             bank_account_statement s
             -- inner join document_movement d on s.document_parent_id = d.document_parent_id
             -- INNER JOIN tmp_person_filter pf ON s.person_id = pf.person_id
             INNER JOIN tmp_bank_account_filter f ON s.bank_account_id = f.bank_account_id
			WHERE
				s.change_date BETWEEN _date_from AND CAST(concat(DATE_FORMAT(now(),'%Y-%m-%d') , ' 23:59:59') AS DATETIME)
				AND IFNULL(s.inactive,0)=0
                -- AND d.document_parent_id in (232281, 232280, 232274)
                ;      
                
			-- SELECT _bank_balance_available;
			SELECT _bank_balance_available_initial AS bank_balance_available_initial;
            -- SELECT _total_movement;

			SELECT
				s.document_parent_id,
                s.document_id,
                d.document_type,
				case when d.document_type = 2 and s.document_value < 0 then 'Transf. D'
					 when d.document_type = 2 and s.document_value >= 0 then 'Transf. C'
                     when d.document_type = 3 then 'Despesa'
                     when d.document_type = 1 then 'Despesa'
                     when d.document_type = 4 then 'Mov. Loja'
				end document_type_description,
                CONCAT((CASE WHEN s.document_note LIKE '%EXTORNO >> %' THEN 'EXTORNO >> ' ELSE '' END),
                case when d.document_type = 2 and s.document_value < 0 then (SELECT description FROM bank_account WHERE id= d.bank_account_origin_id)
					 when d.document_type = 2 and s.document_value >= 0 then (SELECT description FROM bank_account WHERE id= d.bank_account_origin_id)
                     when d.document_type = 3 then (CASE WHEN IFNULL(s.provider_id,0) = 0 THEN s.document_note ELSE (SELECT social_name FROM provider WHERE id = s.provider_id) END)
                     when d.document_type = 1 then (CASE WHEN IFNULL(s.provider_id,0) = 0 THEN s.document_note ELSE (SELECT social_name FROM provider WHERE id = s.provider_id) END)
                     when d.document_type = 4 then (CASE WHEN IFNULL(s.provider_id,0) = 0 THEN s.document_note ELSE (SELECT social_name FROM provider WHERE id = s.provider_id) END)
				end) movement_description,
                s.provider_id,
                0 AS is_group_movement,
                (CASE WHEN IFNULL(s.provider_id,0) = 0 THEN s.document_note ELSE (SELECT social_name FROM provider WHERE id = s.provider_id) END) AS provider_description,
				DATE_FORMAT(s.change_date, '%d/%m/%Y') AS document_date,
   				DATE_FORMAT(s.change_date, '%Y%m%d%H%i%s') AS document_date_long,
				s.bank_account_id,
                s.bank_account_origin_id,
                (SELECT description FROM bank_account WHERE id = s.bank_account_origin_id) AS bank_account_origin_description,
				b.description AS bank_account_description,
				s.document_note,
				s.document_value, 
                c.id AS company_id, 
                c.fantasy_name AS company_description
			FROM
				tmp_bank_account_filter f
				INNER JOIN bank_account b ON f.bank_account_id=b.id
				INNER JOIN bank_account_statement s ON f.bank_account_id=s.bank_account_id 
                INNER JOIN document_movement d on s.document_parent_id = d.document_parent_id AND s.document_id = CASE WHEN s.document_id = 0 THEN 0 ELSE d.document_id END
                INNER JOIN company c ON s.person_id=c.person_id
			WHERE
				s.change_date BETWEEN _date_from AND _date_to
				AND IFNULL(s.inactive,0)=0
                AND d.document_status = 2
                -- AND d.financial_cost_center_id = (CASE WHEN IFNULL(_financial_cost_center_id,0) = 0 THEN d.financial_cost_center_id ELSE _financial_cost_center_id END)
			ORDER BY 
					document_date_long, s.document_parent_id, document_id;
		END; /** End operation 1 */
    ELSEIF(2=_operation) THEN /** REPORT SINTETIC */    
			BEGIN
				DECLARE _bank_balance_available DECIMAL(19,2);
				DECLARE _bank_balance_available_initial DECIMAL(19,2);
				DECLARE _total_movement DECIMAL(19,2);
				DECLARE _accumulate_balance BIT;
				
				INSERT INTO tmp_bank_account_values VALUES(_bank_account_ids);
				SET @sql = concat("INSERT INTO tmp_bank_account_filter (bank_account_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_bank_account_values), ",", "'),('"),"');");
				PREPARE stmt1 FROM @sql;
				EXECUTE stmt1;
		
				IF(LENGTH(IFNULL(_person_ids, '')) > 0) THEN 
					BEGIN
						INSERT INTO tmp_person_values VALUES(_person_ids);
						SET @sql = concat("INSERT INTO tmp_person_filter (person_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_person_values), ",", "'),('"),"');");
						PREPARE stmt1 FROM @sql;
						EXECUTE stmt1;
					END;
				ELSE
					BEGIN
						INSERT INTO tmp_person_filter
						SELECT id FROM person p WHERE p.bank_account_id IN (
							SELECT bank_account_id FROM tmp_bank_account_values
						);
					END;
				END IF;
			   
				SELECT 
					IFNULL((SELECT SUM(IFNULL(ba.bank_balance_available,0))
				FROM 
					tmp_bank_account_filter f
					INNER JOIN bank_account ba ON f.bank_account_id = ba.id),0) INTO _bank_balance_available;     
					
				SELECT 
					IFNULL((SELECT IFNULL(MAX(ba.accumulate_balance),0)
				FROM 
					tmp_bank_account_filter f
					INNER JOIN bank_account ba ON f.bank_account_id=ba.id),0) INTO _accumulate_balance;                   
				
				SELECT            
					(_bank_balance_available + IFNULL(SUM(s.document_value * -1),0)), IFNULL(SUM(s.document_value),0) INTO _bank_balance_available_initial, _total_movement
				FROM
				 bank_account_statement s
				 -- INNER JOIN document_movement d on s.document_parent_id = d.document_parent_id
				 -- INNER JOIN tmp_person_filter pf ON s.person_id = pf.person_id
				 INNER JOIN tmp_bank_account_filter f ON s.bank_account_id = f.bank_account_id
				 WHERE
					s.change_date BETWEEN _date_from AND concat(DATE_FORMAT(now(),'%Y-%m-%d') , ' 23:59:59')
					-- AND IFNULL(s.inactive,0)=0
					-- AND d.document_parent_id in (232281, 232280, 232274)
					;      
					
				IF(_accumulate_balance = 0) THEN
					SET _bank_balance_available_initial = 0;
				END IF;
					
				-- SELECT _bank_balance_available;
				SELECT _bank_balance_available_initial AS bank_balance_available_initial;
				-- SELECT _total_movement;
					
				SELECT 
					x.document_parent_id,
					is_group_movement,
					(case when is_group_movement = 1 then x.document_parent_id else x.document_id end) AS document_id,
					x.document_type,
					case when x.document_type = 2 and x.document_value < 0 then 'Transf. D'
						 when x.document_type = 2 and x.document_value >= 0 then 'Transf. C'
						 when x.document_type = 3 then 'Despesa'
						 when x.document_type = 1 then 'Despesa'
						 when x.document_type = 4 then 'Mov. Loja'
					end document_type_description,
					CASE WHEN x.document_type = 2 AND x.is_cashing_close THEN (SELECT description FROM bank_account WHERE id= x.bank_account_id)
						 WHEN x.document_type = 2 AND x.document_value < 0 THEN (SELECT description FROM bank_account WHERE id= x.bank_account_origin_id)
						 WHEN x.document_type = 2 AND x.document_value >= 0 THEN (SELECT description FROM bank_account WHERE id= x.bank_account_origin_id)
						 WHEN x.document_type = 3 THEN (CASE WHEN IFNULL(x.provider_id,0) = 0 THEN x.document_note ELSE (SELECT social_name FROM provider WHERE id= x.provider_id) END)
						 WHEN x.document_type = 1 THEN (CASE WHEN IFNULL(x.provider_id,0) = 0 THEN x.document_note ELSE (SELECT social_name FROM provider WHERE id= x.provider_id) END)
						 WHEN x.document_type = 4 THEN c.fantasy_name
					end movement_description,
					x.provider_id,
					(CASE WHEN IFNULL(x.provider_id,0) = 0 THEN x.document_note ELSE (SELECT social_name FROM provider WHERE id= x.provider_id) END) AS provider_description,
					x.document_date,
					x.document_date_long,
					x.bank_account_id,
					x.bank_account_origin_id,
					(SELECT description FROM bank_account WHERE id= x.bank_account_origin_id) AS bank_account_origin_description,
					x.document_value,
					x.document_note,
					x.person_id,
					b.description AS bank_account_description,
					c.id AS company_id, 
					c.fantasy_name AS company_description
				FROM
				(
					SELECT            
						(case when COUNT(s.document_parent_id) > 1 then 1 else 0 end) AS is_group_movement,
						s.document_parent_id,
						MAX(s.document_id) AS document_id,
						MIN(d.document_type) AS document_type,
						MAX(s.provider_id) AS provider_id,
						DATE_FORMAT(MAX(d.payment_data), '%d/%m/%Y') AS document_date,
						DATE_FORMAT(MAX(d.payment_data), '%Y%m%d%H%i%s') AS document_date_long,
						MAX(s.bank_account_id) AS bank_account_id,
						MAX(s.bank_account_origin_id) AS bank_account_origin_id,
						SUM(s.document_value) AS document_value,
						MAX(d.document_note) AS document_note,
						MAX(s.person_id) AS person_id, 
						MAX(d.is_cashing_close) AS is_cashing_close 
					FROM
						 bank_account_statement s
						 INNER JOIN document_movement d ON s.document_parent_id = d.document_parent_id AND s.document_id = CASE WHEN s.document_id = 0 THEN 0 ELSE d.document_id END
						 INNER JOIN tmp_person_filter pf ON s.person_id = pf.person_id
						 INNER JOIN tmp_bank_account_filter f ON s.bank_account_id = f.bank_account_id
					WHERE
						s.change_date BETWEEN _date_from AND _date_to
						AND IFNULL(s.inactive,0)=0
						AND d.document_status = 2
                        -- AND d.financial_cost_center_id = (CASE WHEN IFNULL(_financial_cost_center_id,0) = 0 THEN d.financial_cost_center_id ELSE _financial_cost_center_id END)
					GROUP BY
						s.document_parent_id
					ORDER BY 
						s.document_parent_id                
				) x
				INNER JOIN bank_account b ON x.bank_account_id = b.id
				INNER JOIN company c ON x.person_id = c.person_id
				where x.document_value <> 0
				order by document_date_long asc;
				
		END; /** End operation 2 */
	ELSEIF(4=_operation) THEN /** REPORT DATE  TODAY*/      
		BEGIN        
         SELECT 
			s.bank_account_id,
			s.person_id,
			c.id AS company_id,
			uc.user_id,
			p.email
				 FROM 
                    person p 
                    INNER JOIN user us ON p.id = us.person_id
					INNER JOIN user_company uc ON us.id = uc.user_id
                    INNER JOIN company c ON uc.company_id=c.id
					INNER JOIN person pc ON c.person_id = pc.id
					INNER JOIN bank_account_statement s ON pc.id = s.person_id
                    LEFT JOIN bank_account_out_document_parent bao ON bao.bank_account_id = s.bank_account_id
                 WHERE  
					p.access_profile_id IN (2,4)
                    AND uc.inactive = 0
                    AND s.change_date BETWEEN  _date_from AND _date_to
                    AND bao.bank_account_id is null
			 GROUP BY
			 s.bank_account_id,
			 s.person_id,
			 c.id,
			 uc.user_id,
			 p.email;
		END; /** End operation 4 */
	END IF; /** End operation 1 */
END$$

CALL pr_bank_account_statement_report(2, '1565', null, '2024-06-18 00:00:00', '2024-06-30 23:59:59', 0  );