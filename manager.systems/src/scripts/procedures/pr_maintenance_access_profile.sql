USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_access_profile`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_access_profile` (IN _operation INT,
                                                IN _id_from INT, 
												IN _id_to INT,
												IN _description VARCHAR(100), 
                                                IN _role VARCHAR(50),  
												IN _inactive INT, 
                                                IN _user_change BIGINT)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM access_profile WHERE id=_id_from) THEN
			BEGIN
				UPDATE access_profile
					SET
						description = _description, role = IFNULL(_role,''), inactive = _inactive, user_change = _user_change, change_date = NOW()
					WHERE
						id=_id_from;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO access_profile
				(
					id, description, role, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _id_from, _description, IFNULL(_role,''), _inactive, _user_change, NOW(), _user_change, NOW();
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		IF (SELECT 1 = 1 FROM access_profile WHERE id=_id_from) THEN
			BEGIN
				UPDATE  access_profile SET inactive=_inactive, user_change=_user_change, change_date=NOW()
				WHERE id=_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		IF (SELECT 1 = 1 FROM access_profile WHERE id=_id_from) THEN
			BEGIN
				SELECT
					ap.id, ap.description, ap.role, ap.inactive, apc.cash_closing_max_discount, ap.user_creation, ap.creation_date, ap.user_change, IFNULL(us.name,'Nao encontrado') as user_change_name, ap.change_date
				FROM
					access_profile ap
                    LEFT JOIN user us on ap.user_change = us.id
                    LEFT JOIN access_profile_config apc on ap.id = apc.access_profile_id
				WHERE
					ap.id = _id_from;
			END;
		END IF; /** End operation 3 */
	ELSEIF(4=_operation) THEN /** GETALL */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			' ap.id, ap.description, ap.role, ap.inactive, ap.user_creation, ap.creation_date, ap.user_change, IFNULL(us.name,''Nao encontrado'') as user_change_name, ap.change_date ',
			'FROM ', 
			'access_profile ap ',
            ' LEFT JOIN user us on ap.user_change=us.id ',
			'WHERE 1=1 ',
				IF((_id_from>0 AND _id_to>0), CONCAT(' AND ap.id between ', _id_from, ' and ', _id_to),''),
				IF((LENGTH(IFNULL(_description,''))>0), CONCAT(' AND ap.description like ''%', _description, '%'''), ''),
				IF((_inactive IS NOT NULL AND _inactive<2), CONCAT(' AND ap.inactive= ', _inactive), ''),
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
					access_profile
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 5*/
	END IF;
END$$

/**
call pr_maintenance_access_profile 
(4, -- _id_from BIGINT
 4, -- _id_to BIGINT
 'teste descricao', -- _description VARCHAR(100)
 'teste role', -- _role VARCHAR(50)
 0, -- _inactive INT
 1, -- _user_change BIGINT
 1); -- _operation INT
 */

/**
call pr_maintenance_access_profile 
(4, -- _id_from BIGINT
 4, -- _id_to BIGINT
 'teste descricao', -- _description VARCHAR(100)
 'teste role', -- _role VARCHAR(50)
 1, -- _inactive INT
 1, -- _user_change BIGINT
 2); -- _operation INT
 */
 
/**
call pr_maintenance_access_profile 
(4, -- _id_from BIGINT
 4, -- _id_to BIGINT
 NULL, -- _description VARCHAR(100)
 NULL, -- _role VARCHAR(50)
 0, -- _inactive INT
 1, -- _user_change BIGINT
 3); -- _operation INT
 */

/**
call pr_maintenance_access_profile 
(0, -- _id_from BIGINT
 0, -- _id_to BIGINT
 NULL, -- _description VARCHAR(100)
 NULL, -- _role VARCHAR(50)
 0, -- _inactive INT
 1, -- _user_change BIGINT
 4); -- _operation INT
 */
 
 /**
call pr_maintenance_access_profile 
(0, -- _id_from BIGINT
 0, -- _id_to BIGINT
 NULL, -- _description VARCHAR(100)
 NULL, -- _role VARCHAR(50)
 0, -- _inactive INT
 1, -- _user_change BIGINT
 5); -- _operation INT
 */
 