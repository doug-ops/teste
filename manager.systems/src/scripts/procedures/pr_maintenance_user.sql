USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_user`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_user` (IN _operation INT,
										IN _id BIGINT,
										IN _person_id BIGINT,
										IN _name VARCHAR(100),
										IN _alias_name VARCHAR(100), 
                                        IN _rg VARCHAR(20), 
										IN _access_data_user VARCHAR(100), 
										IN _access_data_password VARCHAR(100),
                                        IN _expiration_date DATETIME, 
										IN _last_access_date DATETIME,
                                        IN _exit_access_date DATETIME,
                                        IN _inactive INT)
										
BEGIN
	IF(3=_operation) THEN
		BEGIN
			SELECT 
				u.id, u.name as description 
            FROM user u
            INNER JOIN person p ON u.person_id = p.id AND p.inactive = 0
            WHERE 
            u.id != _id and client_id = (select client_id from  user where id = _id LIMIT 1);
         END;
	ELSEIF(5=_operation) THEN
		BEGIN
			SELECT 
				u.id, u.name as description 
			FROM user u
            INNER JOIN person p ON u.person_id = p.id AND p.inactive = 0
            WHERE u.client_id = (select client_id from  user where id = _id LIMIT 1);
         END;
	ELSEIF(4=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT u.id, u.name description ',
			'FROM user_parent up ',
			'INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = ',_id, ' ',
            ' and u.client_id = (select client_id from user where id = ',_id, ' limit 1)  ',
			'WHERE ',
			 'up.inactive = ',_inactive,' order by u.name;');
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; 
     ELSEIF(18=_operation) THEN
		 UPDATE 
			user 
		 SET 
			last_access_date = _last_access_date 
		WHERE 
			id=_id; 
	ELSEIF(19=_operation) THEN
		 UPDATE 
			user 
		 SET 
			exit_access_date = _exit_access_date 
		WHERE 
			id=_id;
	END IF; 
END$$