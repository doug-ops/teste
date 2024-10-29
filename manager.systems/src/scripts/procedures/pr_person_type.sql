USE `manager`;
DROP procedure IF EXISTS `pr_person_type`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_person_type` (IN _operation INT,
                                    IN _description VARCHAR(50),
									IN _inactive INT,
									IN _user_change BIGINT,
                                    IN _object_type VARCHAR(3))
BEGIN
	IF(1=_operation) THEN
		BEGIN  
			DECLARE v_id INT DEFAULT 0;
			SELECT id INTO v_id FROM person_type WHERE description = _description AND object_type = _object_type;
					
			IF(v_id>0) THEN 
				UPDATE person_type SET inactive = _inactive, change_date=now(), change_user=_user_change WHERE id = v_id;
				SELECT v_id person_type_id;
			ELSE 
				INSERT INTO person_type(description, object_type, inactive, user_creation, creation_date, user_change, change_date)
				SELECT _description, _object_type, _inactive, _user_change, now(), _user_change, now();
				SELECT LAST_INSERT_ID() person_type_id;
				SET v_id = LAST_INSERT_ID();        
			END IF;
		END;
    ELSEIF(5=_operation) THEN
		BEGIN  
            SELECT id, description, object_type  FROM person_type WHERE object_type = CASE WHEN _object_type IS NULL THEN object_type ELSE _object_type END AND inactive=0 ORDER BY description;
		END;
	END IF;  /** End operation 1 */
END$$
-- CALL pr_person_type(1, 'TESTE NOVOS', 0, 1, 2 )
 -- CALL pr_person_type(5, null, null, null, null);
-- CALL pr_person_type(1, 'DOUGLASM', 0, 1, 8  );