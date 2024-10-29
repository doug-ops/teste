USE `manager`;
DROP FUNCTION IF EXISTS `func_save_execution_company`;
USE `manager`$$
DELIMITER $$
CREATE FUNCTION `func_save_execution_company` (_company_id BIGINT,
											   _document_parent_id BIGINT,
                                               _processing_date_time DATETIME,
											   _user_id BIGINT,                                          
											   _document_note VARCHAR(1000))
RETURNS bit 
BEGIN
	
	DECLARE _week_day TINYINT DEFAULT 0;
    DECLARE _week_year INT;
    DECLARE _exists BIT;
	
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

		SELECT IFNULL((SELECT 1 FROM company_movement_execution WHERE company_id = _company_id AND week_year = _week_year LIMIT 1),0) INTO _exists;
        
        IF(_exists) THEN 
			BEGIN
				UPDATE company_movement_execution SET document_parent_id = _document_parent_id, document_note = _document_note, execution_date = _processing_date_time, motive = NULL WHERE company_id = _company_id AND week_year = _week_year;                        
            END;
		END IF;
RETURN _exists;
END$$
DELIMITER ;


-- SET GLOBAL log_bin_trust_function_creators = 1;

-- drop function func_save_config_user_data

select * from sinc_jobs
          