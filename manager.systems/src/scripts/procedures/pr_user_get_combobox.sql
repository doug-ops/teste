USE `manager`;
DROP procedure IF EXISTS `pr_user_get_combobox`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_user_get_combobox` (IN _operation INT, IN _user_id BIGINT)
										
BEGIN
	IF(1=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT u.id, u.name description ',
			' FROM user_parent up ',
            ' INNER JOIN (select client_id from user where id = ',_user_id, ' limit 1) c ON 1 = 1  ',
			' INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = ',_user_id, ' ',
            ' AND u.client_id = c.client_id ',
            ' INNER JOIN person pe ON pe.id = u.person_id ',
			' WHERE ',
			' up.inactive = 0 AND pe.person_type_id = 6 order by u.name;');
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
         END;
	ELSEIF(2=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT u.id, u.name description ',
			' FROM user_parent up ',
            ' INNER JOIN (select client_id from user where id = ',_user_id, ' limit 1) c ON 1 = 1  ',
			' INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = ',_user_id, ' ',
            ' AND u.client_id = c.client_id ',
            ' INNER JOIN person pe ON pe.id = u.person_id ',
			' WHERE ',
			' up.inactive = 0 AND pe.person_type_id = 10 order by u.name;');
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
         END;
	END IF; 
END$$

CALL pr_user_get_combobox(2, 8);