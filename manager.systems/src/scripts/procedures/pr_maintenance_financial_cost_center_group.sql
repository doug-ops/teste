USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_financial_cost_center_group`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_financial_cost_center_group` (IN _operation INT,
												 IN _financial_cost_center_group_id_from int, 
												 IN _financial_cost_center_group_id_to int,
                                                 IN _description varchar(100), 
                                                 IN _inactive int, 
                                                 IN _change_user BIGINT)
BEGIN	
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM financial_cost_center where id=_financial_cost_center_group_id_from) THEN
			BEGIN
				UPDATE financial_cost_center SET 
					description=_description, 
                    inactive=_inactive,  
                    change_user=_change_user, 
                    change_date=now()
				WHERE
					id=_financial_cost_center_group_id_from;
            END;
		ELSE 
			BEGIN
				INSERT INTO financial_cost_center(id, description, inactive, creation_user, creation_date, change_user, change_date)
                select _financial_cost_center_group_id_from, _description, _inactive, _change_user, now(), _change_user, now();
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM financial_cost_center where id=_financial_cost_center_group_id_from) THEN
			BEGIN
				UPDATE financial_cost_center SET inactive=_inactive,  change_user=_change_user, change_date=now()
				WHERE id=_financial_cost_center_group_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
		SELECT 
			id, description, inactive, 
            creation_user, creation_date, change_user, change_date
		FROM
			financial_cost_center
		WHERE 
			id=_financial_cost_center_group_id_from;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT id, description, inactive, change_user, change_date FROM financial_cost_center', 
						 ' WHERE 1=1 ', 
                         IF((_financial_cost_center_group_id_from>0 and _financial_cost_center_group_id_to>0), concat(' AND id between ', _financial_cost_center_group_id_from, ' AND ', _financial_cost_center_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
	ELSEIF(5=_operation) THEN
		SET @t1 = CONCAT('SELECT* FROM financial_cost_center', 
						 ' WHERE 1=1 ', 
                         IF((_financial_cost_center_group_id_from>0 and _financial_cost_center_group_id_to>0), concat(' AND id between ', _financial_cost_center_group_id_from, ' AND ', _financial_cost_center_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
        
    END IF;
END$$
-- CALL pr_maintenance_financial_cost_center_group(2, 1, 1, null, 1, 1  )
-- CALL pr_maintenance_financial_cost_center_group(3, 1, 1, null, null, null  )
-- CALL pr_maintenance_financial_cost_center_group(3, 1, 1, null, null, null  )
-- CALL pr_maintenance_financial_cost_center_group(1, 8, 8, 'MARINAO', 0, 1  )
-- CALL pr_maintenance_financiancial_cast_center_group(4, 0, 0, '', 0, null  )