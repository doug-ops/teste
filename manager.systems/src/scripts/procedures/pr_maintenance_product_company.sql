USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_product_company`;
 
DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_product_company` (IN _operation INT,
												   IN _product_id BIGINT, 										   
												   IN _company_id BIGINT,
												   IN _bank_account_id INT,
												   IN _inactive int, 
												   IN _user_change BIGINT)
BEGIN	
	IF(IFNULL(_bank_account_id,0)>0) THEN
		BEGIN
			SET _company_id = (select co.id from bank_account ba inner join company co on ba.id=co.id_bank_account
							   where ba.id=_bank_account_id);
        END;
	END IF; /**End if bank account is not null*/
    
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM product_company where product_id=_product_id AND company_id=_company_id) THEN
			BEGIN
				UPDATE product_company SET					
                    inactive=_inactive, user_change=_user_change, change_date=now()
				WHERE
					product_id=_product_id AND company_id=_company_id;
            END;
		ELSE 
			BEGIN   
				INSERT INTO product_company(product_id, company_id, inactive, user_creation, creation_date, user_change, change_date)
                select _product_id, _company_id, _inactive, _user_change, now(), _user_change, now();
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM product_company where product_id=_product_id AND company_id=_company_id) THEN
			BEGIN
				UPDATE product_company SET inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE product_id=_product_id AND company_id=_company_id;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
        SELECT 
			pc.company_id, c.social_name, pc.product_id, p.description, pc.inactive, 
            pc.user_creation, pc.creation_date, pc.user_change, pc.change_date
		FROM
			product_company pc
            inner join product p on pc.product_id=p.id
            inner join company c on pc.company_id=c.id
		WHERE 
			pc.product_id=_product_id
            and pc.company_id=_company_id;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT pc.company_id, c.social_name, pc.product_id, p.description, pc.inactive, pc.user_change, pc.change_date FROM product_company pc inner join product p on pc.product_id=p.id inner join company c on pc.company_id=c.id', 
						 ' WHERE 1=1 ', 
                         IF((_product_id>0), concat(' AND pc.product_id=', _product_id), ''), 
                         IF((_company_id>0), concat(' AND pc.company_id=', _company_id), ''), 
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND pc.inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;	
	ELSEIF(6=_operation) THEN
		UPDATE product_company SET inactive=_inactive,  user_change=_user_change, change_date=now()
		WHERE product_id=_product_id;
    END IF;
END$$
/**
call pr_maintenance_product_company
(
4, -- 01 - IN _operation INT,
1, -- 02 - IN _product_id BIGINT, 										   
114, -- 03 - IN _company_id BIGINT,
0, -- 04 - IN _bank_account_id INT,
0, -- 05 - IN _inactive int, 
1 -- 06 - IN _user_change BIGINT
);
*/
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/