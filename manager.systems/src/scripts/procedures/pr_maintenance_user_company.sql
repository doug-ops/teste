USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_user_company`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_user_company` (IN _operation INT,
												IN _user_id VARCHAR(500),
												IN _company_id BIGINT,
												IN _inactive INT, 
                                                IN _user_change BIGINT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
    DECLARE _week_day TINYINT DEFAULT 0;
    DECLARE _week_year INT;
    
	/**Drop temporary tables*/
	DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_user_filter;
    DROP TEMPORARY TABLE IF EXISTS tmp_user_filter2;
    DROP TEMPORARY TABLE IF EXISTS tmp_user_filter3;
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
    
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_filter2
	(
		user_id BIGINT NOT NULL
	);
    
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_filter3
	(
		user_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_user_filter*/
    
	IF(LENGTH(IFNULL(_user_id, '')) > 0) THEN 
		BEGIN
			INSERT INTO tmp_user_values VALUES(_user_id);
			SET @sql = concat("INSERT INTO tmp_user_filter (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
            
			SET @sql = concat("INSERT INTO tmp_user_filter2 (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
            
			SET @sql = concat("INSERT INTO tmp_user_filter3 (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
		END;
	END IF;  

	SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	SELECT WEEKDAY(DATE(_processing_date_time)) INTO _week_day;
	SELECT 
		(CASE
			WHEN
				_week_day < 6
			THEN
				YEARWEEK(DATE(_processing_date_time)) + 1
			ELSE YEARWEEK(DATE(_processing_date_time))
		END) INTO _week_year;
        
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM user_company WHERE user_id=_user_id AND company_id=_company_id limit 1) THEN
			BEGIN
				UPDATE user_company SET inactive=_inactive, user_change=_user_change, change_date=NOW()
				WHERE user_id=_user_id AND company_id=_company_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO user_company
				(
					user_id, company_id, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _user_id, _company_id, _inactive, _user_change, NOW(), _user_change, NOW();
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE BY USER */
		BEGIN
			UPDATE user_company SET inactive=_inactive, user_change=_user_change, change_date=NOW()
			WHERE user_id=_user_id;
		END; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		BEGIN
			SELECT 
				uc.user_id,
				c.id company_id, 
                c.process_movement_automatic,
				p.person_type_id,
                (case when p.person_type_id = 1 then 'Empresa' 
                     when p.person_type_id = 2 then 'Operador' 
                     when p.person_type_id = 3 then 'Interno'
                     else 'Outro' end) person_type_description,
				uc.inactive,
				c.social_name company_description, 
                (CASE WHEN x.expiry_data IS NULL THEN ""  ELSE DATE_FORMAT(x.expiry_data,'%d/%m/%Y') END) AS expiry_data_string
				FROM company c
                INNER JOIN person p ON c.person_id = p.id
				INNER JOIN user_company uc on c.id=uc.company_id                  
				LEFT JOIN 
				(
					SELECT 
					cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id, 
					MIN(cm.expiry_data) expiry_data
					FROM company_movement_execution cm
					INNER JOIN user_company uc ON cm.company_id=uc.company_id AND user_id=_user_id
					WHERE cm.execution_date IS NULL
                    AND cm.inactive=0
					GROUP BY cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id
				) x ON c.id = x.company_id
				WHERE 
				uc.user_id = _user_id
				AND uc.inactive = 0
                AND p.inactive = 0
				ORDER BY x.expiry_data;
		END; /** End operation 3 */
	ELSEIF (4=_operation) THEN /** GET ALL */
		BEGIN
			SELECT 
				MAX(y.user_id) user_id, y.company_id, y.process_movement_automatic, y.person_type_id, y.person_type_description, 
				y.bank_account_origin_id, y.bank_account_destiny_id, y.inactive, y.company_description, y.week_year, 
				y.expiry_data, y.expiry_data_string, y.execution_date, y.background_color, y.document_parent_id, y.payment_data, y.register_order
            FROM (
				SELECT 
					uc.user_id,
					c.id company_id, 
					c.process_movement_automatic,
					p.person_type_id,
					(case when p.person_type_id = 1 then 'Empresa' 
						 when p.person_type_id = 2 then 'Operador' 
						 when p.person_type_id = 3 then 'Interno'
						 else 'Outro' end) person_type_description,
					IFNULL(x.bank_account_origin_id, 0) bank_account_origin_id, 
					IFNULL(x.bank_account_destiny_id, 0) bank_account_destiny_id,
					uc.inactive,
					c.social_name company_description, 
					IFNULL(x.week_year, YEARWEEK(now())) week_year,
                    x.expiry_data,
					(CASE WHEN x.expiry_data IS NULL THEN ''  ELSE DATE_FORMAT(x.expiry_data,'%d/%m/%Y') END) AS expiry_data_string,
					(CASE WHEN x.execution_date IS NULL THEN '-'  ELSE DATE_FORMAT(x.execution_date,'%d/%m/%Y %H:%i') END) AS execution_date,
					(CASE WHEN x.execution_date IS NOT NULL THEN '#c8ffc7' WHEN date(x.expiry_data) < date(now()) THEN '#ffdbdb' WHEN date(x.expiry_data) > date(now()) THEN '#ffffff' ELSE '#fffdc7' END) AS background_color,
					z.document_parent_id, IFNULL(z.payment_data,'-') AS payment_data,
					CASE WHEN x.expiry_data IS NULL THEN 0 
						 WHEN x.execution_date IS NULL THEN 1
						 ELSE 2
					END register_order
					FROM company c
					INNER JOIN person p ON c.person_id = p.id
					INNER JOIN user_company uc on c.id = uc.company_id AND uc.inactive = 0   
					INNER JOIN tmp_user_filter uf ON uc.user_id = uf.user_id 
					LEFT JOIN 
					(
						SELECT 
						cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id, 
						MAX(cm.expiry_data) expiry_data,
						MAX(cm.execution_date) execution_date,
						cm.week_year week_year
						FROM company_movement_execution cm
						INNER JOIN user_company uc ON cm.company_id=uc.company_id 
                        INNER JOIN tmp_user_filter2 uf ON uc.user_id = uf.user_id 
						WHERE 
						cm.week_year = _week_year
						GROUP BY cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id, cm.week_year
					) x ON c.id = x.company_id
                    LEFT JOIN 
                    (
						SELECT 
							uc.company_id,  max(doc.document_parent_id) document_parent_id, DATE_FORMAT(max(doc.payment_data),'%d/%m/%Y') payment_data
						FROM 
							user_company uc
                            INNER JOIN tmp_user_filter3 uf ON uc.user_id = uf.user_id AND uc.inactive = 0
							INNER JOIN document_movement doc on uc.company_id = doc.company_id AND is_product_movement = 1 AND doc.document_status = 2 AND doc.inactive = 0
						GROUP BY uc.company_id
					) z ON c.id = z.company_id
					WHERE 
					p.inactive = 0
					AND p.person_type_id = 1
				) y
                GROUP BY
					y.company_id, y.process_movement_automatic, y.person_type_id, y.person_type_description, 
					y.bank_account_origin_id, y.bank_account_destiny_id, y.inactive, y.company_description, y.week_year, 
					y.expiry_data, y.expiry_data_string, y.execution_date, y.background_color, y.document_parent_id, y.payment_data, y.register_order
                ORDER BY 
					y.register_order, y.expiry_data, y.company_description;
		END; /** End operation 4 */
	ELSEIF (5=_operation) THEN /** GET ALL USERS COMPANY */
		BEGIN
			SELECT 
				uc.user_id, uc.company_id
			FROM 
				user_company uc
			WHERE 
				uc.company_id = _company_id
				AND uc.inactive = 0
			GROUP BY 
				uc.user_id, uc.company_id;
		END; /** End operation 5 */
	ELSEIF (6 = _operation) THEN /** INACTIVE BY COMPANY */
		BEGIN
			UPDATE user_company SET inactive = _inactive, user_change = _user_change, change_date = now()
			WHERE company_id = _company_id and user_id <> _user_change;
		END; /** End operation 6 */
	END IF; 
END$$

CALL pr_maintenance_user_company(4, '8', null, null, null  );