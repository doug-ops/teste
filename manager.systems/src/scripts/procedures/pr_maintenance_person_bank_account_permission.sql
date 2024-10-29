USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_person_bank_account_permission`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_person_bank_account_permission` (IN _operation TINYINT, 
																  IN _bank_account_id INT,
																  IN _user_id BIGINT, 
                                                                  IN _inactive BIT,
																  IN _user_change BIGINT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();

	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);            
    
	IF (1 = _operation) THEN /** Save */
		IF (SELECT 1 = 1 FROM person_bank_account_permission WHERE bank_account_id = _bank_account_id AND user_id = _user_id) THEN
			BEGIN
				UPDATE person_bank_account_permission
					SET
						inactive = _inactive, user_change = _user_change, change_date = _processing_date_time
					WHERE 
						bank_account_id = _bank_account_id AND user_id = _user_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO person_bank_account_permission
				(
					user_id, bank_account_id, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT 
					_user_id, _bank_account_id, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2 = _operation) THEN /** INACTIVE */
		BEGIN
			UPDATE person_bank_account_permission SET inactive = _inactive, user_change = _user_change, change_date = _processing_date_time
			WHERE  user_id = _user_id;
		END; /** End operation 2 */
	ELSEIF (3 = _operation) THEN /** GET */
		BEGIN
			SELECT user_id, bank_account_id, inactive, user_creation, creation_date, user_change, change_date FROM person_bank_account_permission
			WHERE  user_id = _user_id AND inactive = 0;
		END; /** End operation 3 */        
	END IF;
END$$

CALL pr_maintenance_person_bank_account_permission(3, 0, 8, 0, 0);