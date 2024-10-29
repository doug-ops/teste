USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_access_profile_permission`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_access_profile_permission` (IN _operation INT,
															 IN _id INT,
															 IN _parent_id INT,
                                                             IN _access_profile_id INT,
															 IN _permission BIT,
															 IN _inactive INT, 
															 IN _user_change BIGINT)
BEGIN
    DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR_MAINTENANCE_ACCESS_PROFILE', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;
    
    SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM access_profile_permission WHERE id=_id AND parent_id=_parent_id AND access_profile_id=_access_profile_id) THEN
			BEGIN
				UPDATE access_profile_permission
					SET
						permission=_permission, inactive = _inactive, user_change = _user_change, change_date = _processing_date_time
					WHERE id = _id AND parent_id = _parent_id AND access_profile_id = _access_profile_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO access_profile_permission
				(
					id, parent_id, access_profile_id, permission, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _id, _parent_id, _access_profile_id, _permission, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		IF (SELECT 1 = 1 FROM access_profile_permission WHERE id=_id AND parent_id=_parent_id) THEN
			BEGIN
				UPDATE access_profile_permission SET inactive= _inactive, user_change= _user_change, change_date = _processing_date_time
				WHERE id= _id AND parent_id= _parent_id AND access_profile_id= _access_profile_id;
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
				ap.id AS access_profile_id, ap.description AS access_profile_description, ap.inactive,  pe.id AS permission_id, pe.parent_id AS permission_parent_id, 
				pe.description AS permission_description, pe.role AS permission_role, IFNULL(app.permission,0) AS permission
			FROM
				access_profile ap
				INNER JOIN permission pe ON 1=1
				LEFT JOIN access_profile_permission app ON pe.id = app.id AND pe.parent_id = app.parent_id AND ap.id = app.access_profile_id
			WHERE
				ap.id=_access_profile_id AND ap.inactive=_inactive
			ORDER BY 
				ap.description, pe.parent_id, pe.id, pe.description;
		END; /** End operation 3 */
	ELSEIF(4=_operation) THEN /** GETALL */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			'app.access_profile_id, ap.description, app.id, app.parent_id, pe.description, pe.role, app.permission, app.inactive, app.user_creation, app.creation_date, app.user_change, IFNULL(us.name,''Nao encontrado'') as user_change_name, app.change_date ',
			'FROM ', 
            'access_profile_permission app ',
			'INNER JOIN permission pe ON app.id=pe.id AND app.parent_id=pe.parent_id ',
 			'INNER JOIN access_profile ap ON app.access_profile_id=ap.id ',
			'LEFT JOIN user us on app.user_change=us.id ',
			'WHERE 1=1 ',
				IF((_id>0), CONCAT(' AND app.id=', _id),''),
   				IF((_parent_id>0), CONCAT(' AND app.parent_id=', _parent_id),''),
   				IF((_access_profile_id>0), CONCAT(' AND app.access_profile_id=', _access_profile_id),''),
				IF((_inactive IS NOT NULL AND _inactive<2), CONCAT(' AND app.inactive= ', _inactive), ''),
                ';');
                
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /** End operation 4 */
	ELSEIF(5=_operation) THEN
			BEGIN
				SELECT
					app.access_profile_id, ap.description, app.id, app.parent_id, pe.description, pe.role, app.permission
				FROM
					access_profile_permission app
					INNER JOIN permission pe ON app.id=pe.id AND app.parent_id=pe.parent_id
                    INNER JOIN access_profile ap ON app.access_profile_id=ap.id
                    LEFT JOIN user us on app.user_change=us.id
				WHERE
					app.inactive=0
				ORDER BY 
					ap.description;
            END; /**End operation 5*/
    ELSEIF (13=_operation) THEN /** INACTIVE_ALL */		
			BEGIN		
               UPDATE access_profile_permission SET permission = 0, user_change = _user_change, change_date = _processing_date_time
			   WHERE access_profile_id = _access_profile_id;
            END;		
		END IF; /** End operation 13 */
END$$

/**
call pr_maintenance_access_profile_permission(
1, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
1, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
*/
/**
call pr_maintenance_access_profile_permission(
2, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
1, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
*/

/**
call pr_maintenance_access_profile_permission(
3, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
6, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
*/
/**
call pr_maintenance_access_profile_permission(
4, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
1, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
*/
/**
call pr_maintenance_access_profile_permission(
5, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
1, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
call pr_maintenance_access_profile_permission(
13, -- IN _operation INT,
2, -- IN _id INT,
1, -- IN _parent_id INT,
1, -- IN _access_profile_id INT,
1, -- IN _permission BIT,
0, -- IN _inactive INT, 
1 -- IN _user_change BIGINT
);
*/