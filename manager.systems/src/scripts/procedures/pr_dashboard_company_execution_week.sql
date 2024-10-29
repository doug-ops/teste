USE `manager`;
DROP procedure IF EXISTS `pr_dashboard_company_execution_week`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_dashboard_company_execution_week`(IN _operation INT,
													   IN _date_from DATETIME,
										               IN _date_to DATETIME,
                                                       IN _users_id VARCHAR(5000))
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
	
    SELECT IFNULL(timezone_database, 0)	INTO _timezone_database FROM config_systems	LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	/**Drop temporary tables*/
	DROP TEMPORARY TABLE IF EXISTS tmp_users_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_users;
    DROP TEMPORARY TABLE IF EXISTS tmp_company;
	/**End Drop temporary tables*/
        
	/**Temporary table to insert tmp_users_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_users_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_users_values*/
        
	/**Temporary table to insert tmp_users*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_users
	(
		user_id BIGINT NOT NULL
	);
    /**End Temporary table to insert tmp_users*/
    
   	/**Temporary table to insert tmp_company*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company
	(
		company_id BIGINT NOT NULL
	);
    /**End Temporary table to insert tmp_company*/
    
	INSERT INTO tmp_users_values VALUES(_users_id);
	SET @sql = concat("INSERT INTO tmp_users (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_users_values), ",", "'),('"),"');");
	PREPARE stmt1 FROM @sql;
	EXECUTE stmt1;
    
    insert into tmp_company 
    SELECT company_id FROM user_company uc inner join tmp_users tu on uc.user_id = tu.user_id GROUP BY company_id;
        
	IF(4=_operation) THEN
		BEGIN
			SELECT 
				c.week_year, MAX(c.initial_date_week) AS initial_date_week, MAX(c.final_date_week) AS final_date_week, COUNT(*) AS count_movement,
				SUM(CASE WHEN c.document_parent_id IS NOT NULL THEN 1 ELSE 0 END) AS count_executed, 
				SUM(CASE WHEN c.document_parent_id IS NULL THEN 1 ELSE 0 END) as count_not_executed,
				SUM(CASE WHEN c.document_parent_id IS NOT NULL AND c.motive = 1 then 1 ELSE 0 END) AS count_negative_executed, 
				SUM(CASE WHEN c.motive = 2 then 1 ELSE 0 END) AS count_low_movement, 
				SUM(CASE WHEN c.motive = 3 then 1 ELSE 0 END) AS count_others, 
                (SELECT SUM(p.blocked) FROM person p INNER JOIN company c ON p.id=c.person_id WHERE p.blocked=1 AND p.id in (SELECT company_id FROM user_company uc inner join tmp_users tu on uc.user_id = tu.user_id GROUP BY company_id)) AS count_disable,
                SUM(CASE WHEN c.document_parent_id IS NOT NULL AND c.motive = 4 then 1 ELSE 0 END) AS count_disable_executed,
                SUM(CASE WHEN c.document_parent_id IS NULL AND c.motive = 1 then 1 ELSE 0 END) AS count_negative, 
                SUM(CASE WHEN c.document_parent_id IS NULL AND IFNULL(c.motive, 0) = 0 AND c.expiry_data >= DATE(_processing_date_time) then 1 ELSE 0 END) AS count_company_not_expiry,
				SUM(CASE WHEN c.document_parent_id IS NULL AND IFNULL(c.motive, 0) = 0  AND c.expiry_data < DATE(_processing_date_time) then 1 ELSE 0 END) AS count_not_informed
			FROM 
				company_movement_execution  c 
                inner join tmp_company tc on c.company_id = tc.company_id
			WHERE 
				c.week_year	BETWEEN YEARWEEK(DATE(_date_from)) AND YEARWEEK(DATE(_date_to)) 
			GROUP BY 
				c.week_year;
		END;
	ELSEIF(5=_operation) THEN
		BEGIN
			select 
				x.product_group_id, 
				gr.description group_description,
				x.product_sub_group_id, 
				sgr.description sub_group_description,
				-- x.product_id, 
				x.total
			from 
			(
				SELECT 
					po.product_group_id, 
                    po.product_sub_group_id, 
                    IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
				FROM 
					document_movement mo
					INNER JOIN product po on mo.product_id = po.id
                    INNER JOIN tmp_company tc on mo.company_id = tc.company_id
				WHERE 
					mo.payment_data BETWEEN _date_from AND _date_to
					AND mo.is_product_movement = 1
				GROUP BY 
					po.product_group_id, po.product_sub_group_id
			) x
			left join product_group gr on x.product_group_id = gr.id
            left join product_sub_group sgr on x.product_group_id = sgr.product_group_id and x.product_sub_group_id = sgr.id;
		END;
    END IF;
END$$
/**
CALL pr_dashboard_company_execution_week
(
	5, -- 01 - IN _operation INT,
    '2022-10-24 00:00:00', -- 02 - IN _date_from DATETIME,
    '2022-10-30 00:00:00', -- 03 - IN _date_to DATETIME,
    '7'             -- 04 - In _user BIGINT
);
*/
