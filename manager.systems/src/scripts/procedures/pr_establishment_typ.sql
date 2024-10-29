USE `manager`;
DROP procedure IF EXISTS `pr_establishment_type`;
DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_establishment_type` (IN _operation INT,
                                    IN _description VARCHAR(50),
									IN _inactive INT,
									IN _user_change BIGINT)
BEGIN
	IF(1=_operation) THEN
		BEGIN  
			DECLARE v_id INT DEFAULT 0;
			SELECT id INTO v_id FROM establishment_type WHERE description = _description;
			IF(v_id>0) THEN 
				UPDATE establishment_type SET inativo=_inactive, change_date=now(), change_user=_user_change WHERE id = v_id;
				SELECT v_id person_type_id;
			ELSE 
				INSERT INTO establishment_type(description, inactive, user_creation, creation_date, user_change, change_date)
				SELECT _description, _inactive, _user_change, now(), _user_change, now();
				SELECT LAST_INSERT_ID() person_type_id;
				SET v_id = LAST_INSERT_ID();        
			END IF;           
		END;
    ELSEIF(5=_operation) THEN
		BEGIN
			SELECT id, description FROM establishment_type WHERE inactive=0 ORDER BY description;
		END;
	END IF;  /** End operation 1 */
END$$
-- CALL pr_establishment_type(1, 'TESTE NOVOS', 0, 1, 2 )
 -- CALL pr_establishment_type(5, null, null, null, null);
 -- CALL pr_establishment_type(1, 'DOUGLASM', 0, 1, 8  );