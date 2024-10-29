USE `manager`;
DROP procedure IF EXISTS `pr_financial_cost_center`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_financial_cost_center` (IN _operation INT,
											 IN _description VARCHAR(50),
										     IN _user_change BIGINT,
                                             IN _inactive INT)
BEGIN
	IF(1=_operation) THEN
		BEGIN  
			DECLARE v_id INT DEFAULT 0;
			SELECT id INTO v_id FROM financial_cost_center WHERE description = _description;
					
			IF(v_id>0) THEN 
				UPDATE financial_cost_center SET inativo=_inactive, change_date=now(), change_user=_user_change WHERE id = v_id;
				SELECT v_id financial_cost_center_id;
			ELSE 
				INSERT INTO financial_cost_center(description, creation_user, creation_date, change_user, change_date, inactive)
				SELECT _description, _user_change, now(), _user_change, now(), _inactive;
				SELECT LAST_INSERT_ID() financial_cost_center_id;
				SET v_id = LAST_INSERT_ID();        
			END IF;
		END;
    ELSEIF(5=_operation) THEN
		BEGIN  
            SELECT id, description FROM financial_cost_center WHERE inactive=_inactive ORDER BY description;
		END;
	END IF;  /** End operation 1 */
END$$