USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_permission`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_permission` (IN _operation INT,
											  IN _id INT,
											  IN _parent_id INT, 
											  IN _description VARCHAR(100), 
											  IN _role VARCHAR(100),  
											  IN _inactive INT, 
											  IN _user_change BIGINT)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM permission WHERE id=_id AND parent_id=_parent_id) THEN
			BEGIN
				UPDATE permission
					SET
						description=_description, role=_role, inactive=_inactive, user_change=_user_change, change_date=NOW()
					WHERE
						id=_id AND parent_id=_parent_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO permission
				(
					id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _id, _parent_id, _description, _role, _inactive, _user_change, NOW(), _user_change, NOW();
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		IF (SELECT 1 = 1 FROM permission WHERE id=_id AND parent_id=_parent_id) THEN
			BEGIN
				UPDATE permission SET inactive=_inactive, user_change=_user_change, change_date=NOW()
				WHERE id=_id AND parent_id=_parent_id;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		BEGIN
			SELECT
				pe.id, pe.parent_id, pe.description, pe.role, pe.inactive, pe.user_creation, pe.creation_date, pe.user_change, IFNULL(us.name,'Nao encontrado') as user_change_name, pe.change_date
			FROM
				permission pe
				LEFT JOIN user us on pe.user_change=us.id
			WHERE
				pe.id=_id AND pe.parent_id=_parent_id;
		END; /** End operation 3 */
	ELSEIF(4=_operation) THEN /** GETALL */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			' pe.id, pe.parent_id, pe.description, pe.role, pe.inactive, pe.user_creation, pe.creation_date, pe.user_change, IFNULL(us.name,''Nao encontrado'') as user_change_name, pe.change_date ',
			'FROM ', 
			'permission pe ',
            ' LEFT JOIN user us on pe.user_change=us.id ',
			'WHERE 1=1 ',
				IF((_id>0), CONCAT(' AND pe.id=', _id),''),
   				IF((_parent_id>0), CONCAT(' AND pe.parent_id=', _parent_id),''),
				IF((LENGTH(IFNULL(_description,''))>0), CONCAT(' AND pe.description like ''%', _description, '%'''), ''),
				IF((_inactive IS NOT NULL AND _inactive<2), CONCAT(' AND pe.inactive= ', _inactive), ''),
                ';');
                
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /** End operation 4 */
	ELSEIF(5=_operation) THEN
			BEGIN
				SELECT 
					id, description
				FROM
					permission
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 5*/
	END IF;
END$$

/**
call pr_maintenance_permission( 
1, -- IN _operation INT
15, -- IN _id INT,
0, -- IN _parent_id INT, 
'Teste Descricao', -- IN _description VARCHAR(100), 
'TESTE', -- IN _role VARCHAR(100),  
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT, 
); -- _operation INT
*/

/**
call pr_maintenance_permission( 
2, -- IN _operation INT
15, -- IN _id INT,
0, -- IN _parent_id INT, 
'Teste Descricao', -- IN _description VARCHAR(100), 
'TESTE', -- IN _role VARCHAR(100),  
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT, 
); -- _operation INT
*/

/**
call pr_maintenance_permission( 
3, -- IN _operation INT
15, -- IN _id INT,
0, -- IN _parent_id INT, 
'Teste Descricao', -- IN _description VARCHAR(100), 
'TESTE', -- IN _role VARCHAR(100),  
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT, 
); -- _operation INT
*/
/**
call pr_maintenance_permission( 
4, -- IN _operation INT
0, -- IN _id INT,
0, -- IN _parent_id INT, 
'', -- IN _description VARCHAR(100), 
'', -- IN _role VARCHAR(100),  
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT, 
); -- _operation INT
*/