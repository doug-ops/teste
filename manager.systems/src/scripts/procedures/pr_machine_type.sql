USE `manager`;
DROP procedure IF EXISTS `pr_machine_type`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_machine_type` (IN _operation INT,
								    IN _description VARCHAR(50),
								    IN _inactive INT,
									IN _user_change BIGINT)
BEGIN
	IF(1=_operation) THEN
		BEGIN  
			DECLARE v_id INT DEFAULT 0;
			SELECT id INTO v_id FROM machine_type WHERE description = _description;
            
			IF(v_id>0) THEN 
				UPDATE machine_type SET inactive = _inactive, change_date=now(), user_change=_user_change WHERE id = v_id;
				SELECT v_id machine_type_id;
			ELSE 
				INSERT INTO machine_type(description, inactive, user_creation, creation_date, user_change, change_date)
				SELECT _description, _inactive, _user_change, now(), _user_change, now();
				SELECT LAST_INSERT_ID() machine_type_id;
				SET v_id = LAST_INSERT_ID();        
			END IF;
		END;
    ELSEIF(5=_operation) THEN
		BEGIN  
            SELECT id, description FROM machine_type WHERE inactive=0 ORDER BY description;
		END;
	END IF;  /** End operation 1 */
END$$
-- CALL pr_machine_type(1, 'ELETRONICO', 0, 8 );
-- CALL pr_machine_type(5, null, null, null );