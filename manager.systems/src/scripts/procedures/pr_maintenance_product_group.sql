USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_product_group`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_product_group` (IN _operation INT,
												 IN _product_group_id_from int, 
												 IN _product_group_id_to int,
                                                 IN _description varchar(100), 
                                                 IN _inactive int, 
                                                 IN _user_change BIGINT,
                                                 IN _transfer_hab DECIMAL(19,2))
BEGIN	
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM product_group where id=_product_group_id_from) THEN
			BEGIN
				UPDATE product_group SET 
					description=_description, 
                    inactive=_inactive,  user_change=_user_change, change_date=now(),
                    transfer_hab=_transfer_hab
				WHERE
					id=_product_group_id_from;
            END;
		ELSE 
			BEGIN
				INSERT INTO product_group(id, description, inactive, transfer_hab, user_creation, creation_date, user_change, change_date)
                select _product_group_id_from, _description, _inactive, _transfer_hab, _user_change, now(), _user_change, now();
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM product_group where id=_product_group_id_from) THEN
			BEGIN
				UPDATE product_group SET inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE id=_product_group_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
		SELECT 
			id, description, inactive, transfer_hab, 
            user_creation, creation_date, user_change, change_date
		FROM
			product_group
		WHERE 
			id=_product_group_id_from;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT id, description, inactive, transfer_hab, user_change, change_date FROM product_group', 
						 ' WHERE 1=1 ', 
                         IF((_product_group_id_from>0 and _product_group_id_to>0), concat(' AND id between ', _product_group_id_from, ' AND ', _product_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
	ELSEIF(5=_operation) THEN
		SET @t1 = CONCAT('SELECT id, description FROM product_group', 
						 ' WHERE 1=1 ', 
                         IF((_product_group_id_from>0 and _product_group_id_to>0), concat(' AND id between ', _product_group_id_from, ' AND ', _product_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
        
    END IF;
END$$

/**
1 - IN _operation INT
2 - IN _product_group_id_from int, 
3 - IN _product_group_id_to int,
4 - IN _description varchar(100), 
5 - IN _inactive int, 
6 - IN _user_change BIGINT, 
*/
-- call pr_maintenance_product_group(5, 1, 1, 'teste2', 0, 1);
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/