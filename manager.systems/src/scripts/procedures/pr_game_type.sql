USE `manager`;
DROP procedure IF EXISTS `pr_game_type`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_game_type` (IN _operation INT,
								 IN _description VARCHAR(50),
								 IN _inactive INT,
								 IN _user_change BIGINT)
BEGIN
	IF(1=_operation) THEN
		BEGIN  
			DECLARE v_id INT DEFAULT 0;
			SELECT id INTO v_id FROM game_type WHERE description = _description;
            
			IF(v_id>0) THEN 
				UPDATE game_type SET inactive = _inactive, change_date=now(), user_change=_user_change WHERE id = v_id;
				SELECT v_id game_type_id;
			ELSE 
				INSERT INTO game_type(description, inactive, user_creation, creation_date, user_change, change_date)
				SELECT _description, _inactive, _user_change, now(), _user_change, now();
				SELECT LAST_INSERT_ID() game_type_id;
				SET v_id = LAST_INSERT_ID();        
			END IF;
		END;
    ELSEIF(5=_operation) THEN
		BEGIN  
            SELECT id, description FROM game_type WHERE inactive=0 ORDER BY description;
		END;
	END IF;  /** End operation 1 */
END$$
-- CALL pr_game_type(1, 'BILHAR', 0, 8 );
-- CALL pr_game_type(5, null, null, null );