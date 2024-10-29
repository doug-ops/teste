USE `manager`;
DROP procedure IF EXISTS `pr_reprocess_romaneio`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_reprocess_romaneio`()
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
    DECLARE _week_day TINYINT DEFAULT 0;
    DECLARE _week_year INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		 @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'REPROCESS ROMANEIO', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;
		
	END;
    
  	/**Drop temporary tables*/
    
    DROP TEMPORARY TABLE IF EXISTS tmp_filter;  
    
	/**End Drop temporary tables*/
    
    /**Temporary table to insert tmp_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_filter
	(
		company_id BIGINT NOT NULL, 
        week_year INT NOT NULL, 
        date_from DATE, 
        date_to DATE,
        document_parent_id BIGINT, 
        payment_data DATETIME
	);
	/**End Temporary table to insert tmp_filter*/

	SELECT 
		IFNULL(timezone_database, 0) INTO _timezone_database 
	FROM
		config_systems LIMIT 1;

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
		END) + 1 INTO _week_year;
	
    INSERT INTO tmp_filter(company_id, week_year, date_from, date_to, document_parent_id, payment_data)
	SELECT 
		y.company_id, y.week_year, y.initial_date_week, y.final_date_week, doc.document_parent_id, MAX(doc.payment_data) payment_data
	FROM
	(
		SELECT 
			x.company_id, x.week_year, x.initial_date_week, x.final_date_week, 
			x.count_executed, x.count_not_executed, x.count_negative_executed, 
			x.count_low_movement, x.count_others, x.count_disable_executed, 
			x.count_negative, x.count_company_not_expiry, x.count_not_informed 
		FROM 
		(
			SELECT 
					c.company_id, 
					c.week_year, c.initial_date_week AS initial_date_week, c.final_date_week AS final_date_week,
					CASE WHEN c.document_parent_id IS NOT NULL THEN 1 ELSE 0 END AS count_executed, 
					CASE WHEN c.document_parent_id IS NULL THEN 1 ELSE 0 END as count_not_executed,
					CASE WHEN c.document_parent_id IS NOT NULL AND c.motive = 1 then 1 ELSE 0 END AS count_negative_executed, 
					CASE WHEN c.motive = 2 then 1 ELSE 0 END AS count_low_movement, 
					CASE WHEN c.motive = 3 then 1 ELSE 0 END AS count_others, 
					CASE WHEN c.document_parent_id IS NOT NULL AND c.motive = 4 then 1 ELSE 0 END AS count_disable_executed,
					CASE WHEN c.document_parent_id IS NULL AND c.motive = 1 then 1 ELSE 0 END AS count_negative, 
					CASE WHEN c.document_parent_id IS NULL AND IFNULL(c.motive, 0) = 0 AND c.expiry_data >= DATE(now()) THEN 1 ELSE 0 END AS count_company_not_expiry,
					CASE WHEN c.document_parent_id IS NULL AND IFNULL(c.motive, 0) = 0  AND c.expiry_data < DATE(now()) THEN 1 ELSE 0 END AS count_not_informed
				FROM 
					company_movement_execution  c 
				WHERE 
					c.week_year	= _week_year
	) x       
	WHERE x.count_executed = 0 AND x.count_negative_executed = 0
	) y 
	INNER JOIN document_movement doc ON y.company_id = doc.company_id 
	AND doc.payment_data BETWEEN y.initial_date_week AND y.final_date_week
	AND doc.document_type = 4 AND doc.is_product_movement = 1
	GROUP BY y.company_id, y.week_year, y.initial_date_week, y.final_date_week, doc.document_parent_id;

   -- SET SQL_SAFE_UPDATES = 0;
	UPDATE company_movement_execution a 
    INNER JOIN tmp_filter b ON a.company_id = b.company_id AND a.week_year = b.week_year 
    SET a.document_parent_id = b.document_parent_id, a.document_note = 'Movimento executado com sucesso', execution_date = payment_data, motive = NULL;
 
END$$


CALL pr_reprocess_romaneio();

select * from company_movement_execution  where week_year = 202431 and execution_date is null