USE `manager`;
DROP procedure IF EXISTS `pr_user_parent`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_user_parent` (	IN _operation INT,
									IN _user_id BIGINT,
									IN _user_parent_id BIGINT,
									IN _inactive INT,
									IN _user_change BIGINT)
										
BEGIN
	IF(1=_operation) THEN
		BEGIN
			IF(SELECT 1=1 FROM user_parent WHERE user_parent_id = _user_parent_id and user_id =_user_id )THEN
				UPDATE user_parent SET inactive=_inactive, change_user=_user_change, change_date=NOW() WHERE user_id=_user_id;
            ELSE
				INSERT INTO user_parent(user_parent_id, user_id, creation_user, creation_date, change_user, change_date, inactive)
				SELECT _user_parent_id, _user_id, _user_change, NOW(), _user_change, NOW(), _inactive;
			END IF;
         END;
	ELSEIF (2=_operation) THEN /** INACTIVE */
		BEGIN
			UPDATE user_parent SET inactive=_inactive, change_user=_user_change, change_date=NOW()
			WHERE user_parent_id=_user_parent_id;
		END; /** End operation 2 */
	ELSEIF(3=_operation) THEN
     BEGIN
		SELECT 
			up.user_parent_id, up.user_id, up.inactive 
		FROM 
			user_parent up 
			INNER JOIN user us ON up.user_id = us.id
			INNER JOIN person pe ON us.person_id = pe.id AND pe.inactive = 0
		WHERE 
			up.user_parent_id = _user_parent_id 
			AND up.inactive = 0;
	END;
	END IF;/*end operation 1*/
END$$