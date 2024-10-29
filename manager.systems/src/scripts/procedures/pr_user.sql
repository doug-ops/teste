USE `manager`;
DROP procedure IF EXISTS `pr_user`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_user` (IN _operation INT)
										
BEGIN
	IF(3=_operation) THEN
		BEGIN
			SELECT id, name as description FROM user;
         END;
	END IF;/*end operation 1*/
END$$
-- call pr_maintenance_user (1,1,1,'MANAGER',null,null,'adm@manager.com','123456',null,'2021-11-30 10:08:11');