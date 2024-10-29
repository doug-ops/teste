USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_access_profile_config`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_access_profile_config` (IN _operation INT,
													     IN _access_profile_id INT, 
														 IN _cash_closing_max_discount decimal(19,2),
														 IN _inactive INT, 
														 IN _user_change BIGINT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR ACCESS PROFILE CONFIG', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        SELECT @full_error;
	END;
    
	SELECT IFNULL(timezone_database, 0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM access_profile_config WHERE access_profile_id = _access_profile_id) THEN
			BEGIN				
				UPDATE access_profile_config
					SET
						cash_closing_max_discount = _cash_closing_max_discount, inactive = _inactive, user_change = _user_change, change_date = _processing_date_time
					WHERE
						access_profile_id = _access_profile_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO access_profile_config
				(
					access_profile_id, cash_closing_max_discount, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _access_profile_id, _cash_closing_max_discount, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
			END;/** End insert */
		END IF; /** End operation 1 */
	END IF;
END$$

/**
call pr_maintenance_access_profile_config
(1, -- IN _operation INT,
1, -- IN _access_profile_id INT, 
100, -- IN _cash_closing_max_discount decimal(19,2),
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
 */