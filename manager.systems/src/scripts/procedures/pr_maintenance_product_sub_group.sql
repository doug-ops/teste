USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_product_sub_group`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_product_sub_group` (IN _operation INT,
													IN _product_sub_group_id_from INT, 
													IN _product_sub_group_id_to INT,
                                                    IN _product_group_id varchar(100),
													IN _description varchar(100), 
                                                    IN _sale_price NUMERIC(19,2),
													IN _conversion_factor NUMERIC(19,2),
													IN _inactive int,  
													IN _user_change BIGINT)
BEGIN	
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM product_sub_group where id=_product_sub_group_id_from) THEN
			BEGIN
				UPDATE product_sub_group SET
					product_group_id=_product_group_id,
					description=_description, sale_price=_sale_price,
					conversion_factor=_conversion_factor, inactive=_inactive,  
                    user_change=_user_change, change_date=now()
				WHERE
					id=_product_sub_group_id_from;
            END;
		ELSE 
			BEGIN
				INSERT INTO product_sub_group(id, description, product_group_id, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date)
                select _product_sub_group_id_from, _description, _product_group_id, _sale_price, _conversion_factor, _inactive, _user_change, now(), _user_change, now();
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM product_sub_group where id=_product_sub_group_id_from) THEN
			BEGIN
				UPDATE 
					 product_sub_group SET inactive=_inactive, user_change=_user_change, change_date=now()
				WHERE id=_product_sub_group_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
        SELECT 
			s.id, s.description, s.product_group_id, g.description as description_product_group, 
            s.inactive, s.sale_price, s.conversion_factor, s.user_creation, s.creation_date, 
            s.user_change, s.change_date
		FROM
			product_sub_group s
            inner join product_group g on s.product_group_id=g.id
		WHERE 
			s.id=_product_sub_group_id_from;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT s.id, s.description, s.product_group_id, g.description as description_product_group, s.sale_price, s.conversion_factor, s.inactive, s.user_change, s.change_date FROM product_sub_group s inner join product_group g on s.product_group_id=g.id ', 
						 ' WHERE 1=1 ', 
                         IF((_product_sub_group_id_from>0 and _product_sub_group_id_to>0), concat(' AND s.id between ', _product_sub_group_id_from, ' AND ', _product_sub_group_id_to), ''), 
						 IF((length(IFNULL(_product_group_id,''))>0), concat(' AND g.id in(', _product_group_id, ')'), ''),
                         IF((length(IFNULL(_description,''))>0), concat(' AND s.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND s.inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;
	ELSEIF(5=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT s.id, s.description, s.sale_price, s.conversion_factor, s.product_group_id, g.description as description_product_group FROM product_sub_group s inner join product_group g on s.product_group_id=g.id ', 
					 ' WHERE 1=1 ', 
                     IF((_product_sub_group_id_from>0 and _product_sub_group_id_to>0), concat(' AND s.id between ', _product_sub_group_id_from, ' AND ', _product_sub_group_id_to), ''), 
					 IF((length(IFNULL(_product_group_id,''))>0), concat(' AND g.id in(', _product_group_id, ')'), ''),
                     IF((length(IFNULL(_description,''))>0), concat(' AND s.description like ''%', _description, '%'''), ''),
                     IF((_inactive IS NOT NULL and _inactive<2), concat(' AND s.inactive=', _inactive), ''),
                     ';');
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /**End operation 5*/
    END IF;
END$$

/**
call pr_maintenance_product_sub_group(
5, -- 1 - IN _operation INT
1, -- 2 - IN _product_sub_group_id_from int, 
1, -- 3 - IN _product_sub_group_id_to int,
1, -- 4 - IN _product_group_id_to varchar(100),
'Teste', -- 5 - IN _description varchar(100), 
1.00, -- 6 - IN _sale_price NUMERIC(19,2),
1.00, -- 7 - IN _conversion_factor NUMERIC(19,2),
0, -- 8 - IN _inactive int, 
1 -- 9 - IN _user_change BIGINT, 
);
*/
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/