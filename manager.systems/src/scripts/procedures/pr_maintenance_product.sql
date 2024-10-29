USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_product`;
 
DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_product` (IN _operation INT,
										   IN _product_id_from BIGINT, 
										   IN _product_id_to BIGINT,
   										   IN _description varchar(100), 
                                           IN _sale_price NUMERIC(19,2),
										   IN _cost_price NUMERIC(19,2),
										   IN _conversion_factor NUMERIC(19,2),
                                           IN _product_group_id varchar(100),
                                           IN _product_sub_group_id varchar(100),
                                           IN _company_id BIGINT,
										   IN _input_movement BIGINT,
										   IN _output_movement BIGINT,
										   IN _inactive int, 
										   IN _user_change BIGINT, 
                                           IN _clock_movement BIGINT, 
                                           IN _enable_clock_movement BIT, 
                                           IN _user_id BIGINT,
                                           IN _alias_product varchar(100),
                                           IN _game_type_id INT,
                                           IN _machine_type_id INT)
BEGIN	
    
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM product where id=_product_id_from) THEN
			BEGIN
				/**Create global variables*/
				DECLARE v_company_id BIGINT;
				/**Inactive Product Config Transfer*/	
                SELECT company_id INTO v_company_id FROM product WHERE id = _product_id_from limit 1;

                IF(IFNULL(v_company_id,0) <> _company_id)THEN
					UPDATE financial_transfer_product_setting SET inactive=1 WHERE product_id = _product_id_from;
				END IF;
                
				UPDATE product SET					
					description=_description,
                    sale_price=_sale_price,
				    cost_price=_cost_price,
				    conversion_factor=_conversion_factor,
				    product_group_id=_product_group_id,
				    product_sub_group_id=_product_sub_group_id,
				    company_id=_company_id,
				    input_movement=_input_movement,
				    output_movement=_output_movement,
                    clock_movement=_clock_movement,
                    enable_clock_movement=_enable_clock_movement,
                    inactive=_inactive,  user_change=_user_change, change_date=now(),
                    alias_product=_alias_product, game_type=_game_type_id, machine_type=_machine_type_id
				WHERE
					id=_product_id_from;
            END;
		ELSE 
			BEGIN   
				INSERT INTO product(id, description, sale_price, cost_price, conversion_factor, product_group_id, product_sub_group_id, company_id, input_movement, output_movement, clock_movement, enable_clock_movement, inactive, user_creation, creation_date, user_change, change_date,
                alias_product, game_type, machine_type)
                select _product_id_from, _description, _sale_price, _cost_price, _conversion_factor, _product_group_id, _product_sub_group_id, _company_id, _input_movement, _output_movement, _clock_movement, _enable_clock_movement, _inactive, _user_change, now(), _user_change, now(),
                _alias_product, _game_type_id, _machine_type_id;
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM product where id=_product_id_from) THEN
			BEGIN
				UPDATE product SET inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE id=_product_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
        SELECT
			p.id, p.description, p.sale_price, p.cost_price, p.conversion_factor, p.input_movement, 
            p.output_movement, IFNULL(p.clock_movement,0) as clock_movement, IFNULL(p.enable_clock_movement,0) as enable_clock_movement, p.company_id, p.product_group_id, 
            g.description as description_product_group, p.product_sub_group_id, s.description as description_product_sub_group, p.inactive, 
            p.user_creation, p.creation_date, p.user_change, p.change_date, uc.user_id, p.alias_product, p.game_type, p.machine_type
		FROM
			product p
            INNER JOIN product_group g on p.product_group_id=g.id
            INNER JOIN product_sub_group s on p.product_sub_group_id=s.id
            LEFT JOIN user_company uc on p.company_id=uc.company_id AND uc.user_id= _user_id
		WHERE 
			p.id=_product_id_from;
            
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT p.id, p.description, p.sale_price, p.cost_price, p.conversion_factor, p.input_movement, p.output_movement, p.clock_movement, p.enable_clock_movement, p.company_id, c.social_name AS company_description, p.product_group_id, g.description as description_product_group, p.product_sub_group_id, s.description as description_product_sub_group, p.inactive, p.user_change, p.change_date, p.alias_product, p.game_type, p.machine_type ',
						 'FROM product p ', 
                         'INNER JOIN company c ON p.company_id=c.id ',
                         'INNER JOIN product_group g ON p.product_group_id=g.id ', 
                         'INNER JOIN product_sub_group s ON p.product_sub_group_id=s.id ', 
                         'INNER JOIN user_company uc on c.id=uc.company_id ',
						 ' WHERE 1=1 ', 
                         IF((_product_id_from>0 and _product_id_to>0), concat(' AND p.id between ', _product_id_from, ' AND ', _product_id_to), ''), 
						 IF((length(IFNULL(_product_group_id,''))>0), concat(' AND g.id in(', _product_group_id, ')'), ''),
                         IF((length(IFNULL(_product_sub_group_id,''))>0), concat(' AND s.id in(', _product_sub_group_id, ')'), ''),
                         IF((length(IFNULL(_description,''))>0), concat(' AND p.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive=', _inactive), ''),
                         concat(' AND uc.user_id=', _user_id, ''),';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;
	ELSEIF(5=_operation) THEN
			BEGIN
				SELECT 
					id, description
				FROM
					product
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 6*/	
	ELSEIF(7=_operation) THEN
			BEGIN
				SET @t1 = CONCAT('SELECT p.id, p.description FROM product p', 
				 ' WHERE 1=1 ', 
				 IF((_product_id_from>0 and _product_id_to>0), concat(' AND p.id between ', _product_id_from, ' AND ', _product_id_to), ''), 
				 IF((length(IFNULL(_description,''))>0), concat(' AND p.description like ''%', _description, '%'''), ''),
				 IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive=', _inactive), ''),
				 IF((_company_id>0), concat(' AND p.company_id=', _company_id), ''),
				 ' ORDER BY p.description;');
                         
				-- select @t1;
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
            END; /**End operation 7*/
		ELSEIF(10=_operation) THEN
			BEGIN
                    SET @t1 = CONCAT('select x.id, x.description, x.processing from ('
									'SELECT p.id, p.description, ',
									'IFNULL((SELECT 1 FROM product_movement WHERE product_id=p.id AND processing=0 AND is_offline=1 LIMIT 1),0) AS processing ',
									'FROM product p', 
									 ' WHERE 1=1 ', 
									 IF((_product_id_from>0 and _product_id_to>0), concat(' AND p.id between ', _product_id_from, ' AND ', _product_id_to), ''), 
									 IF((length(IFNULL(_description,''))>0), concat(' AND p.description like ''%', _description, '%'''), ''),
									 IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive=', _inactive), ''),
									 IF((_company_id>0), concat(' AND p.company_id=', _company_id), ''),
									 ' union all ',
									 'select pm.product_id id, po.description, 1 processing ',
                                     'from product_movement pm ', 
									 'inner join product po on pm.product_id = po.id ',
									 'where 1=1 ',
                                     IF((_company_id>0), concat(' AND pm.company_id=', _company_id), ''),
                                     ' and pm.processing = 0 and pm.inactive = 0 ',
                                     ') x ', 
									 'group by x.id, x.description, x.processing ', 
									 'ORDER BY x.description; ');
							-- select @t1;
					PREPARE stmt3 FROM @t1;
					EXECUTE stmt3;
					DEALLOCATE PREPARE stmt3;
		END; /**End operation 10*/
	ELSEIF(17=_operation) THEN
		IF (IFNULL((SELECT 1 FROM product where id=_product_id_from limit 1),0)=0) THEN
			BEGIN
				INSERT INTO product(id, description, sale_price, cost_price, conversion_factor, product_group_id, product_sub_group_id, company_id, input_movement, output_movement, clock_movement, enable_clock_movement, inactive, user_creation, creation_date, user_change, change_date, alias_product, game_type, machine_type)
                select _product_id_from, _description, _sale_price, _cost_price, _conversion_factor, _product_group_id, _product_sub_group_id, _company_id, _input_movement, _output_movement, _clock_movement, _enable_clock_movement, _inactive, _user_change, now(), _user_change, now(), _alias_product, _game_type_id, _machine_type_id;				
            END;
		END IF;
	ELSEIF(18=_operation) THEN
		SELECT p.id, p.input_movement, p.output_movement FROM product p where p.company_id=_company_id;
	ELSEIF(24=_operation) THEN
    
		 SET @t1 = CONCAT('SELECT DISTINCT p.id product_id, p.description product_description FROM product p ', 
						  'INNER JOIN (select pe.bank_account_id as bank_account_origin_id, 0 as bank_account_destiny_id,', 
                          _company_id, ' as company_id from company co inner join person pe on co.person_id=pe.id where co.id=', _company_id, ' limit 1) x on 1 = 1 ',
				 ' LEFT JOIN financial_transfer_product_setting ftps ON p.id = ftps.product_id ',
                 'and x.bank_account_origin_id = ftps.bank_account_origin_id and x.bank_account_destiny_id = ftps.bank_account_destiny_id AND IFNULL(ftps.inactive,0)=0  ',
				 ' WHERE 1=1 ', 
				 IF((_product_id_from>0 and _product_id_to>0), concat(' AND p.id between ', _product_id_from, ' AND ', _product_id_to), ''), 
				 IF((length(IFNULL(_description,''))>0), concat(' AND p.description like ''%', _description, '%'''), ''),
				 IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive=', _inactive), ''),
				 IF((_company_id>0), concat(' AND p.company_id=', _company_id), ''),
				 ' AND ftps.product_id IS NULL',
				 ' ORDER BY p.id;');
             --   select @t1;
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
    END IF;
END$$
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
    7 - Autocomplete
    18 - Get all products by compnay id
*/

CALL pr_maintenance_product(10, 0, 0, null, null, null, null, null, null, 911, null, null, 0, -5, null, null, 46, null, null, null  )