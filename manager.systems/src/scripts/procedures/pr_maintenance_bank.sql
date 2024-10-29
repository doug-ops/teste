USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_bank`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_bank` (IN _operation INT, 
										IN _id VARCHAR(10), 
										IN _description VARCHAR(10), 
                                        IN _inactive INT, 
                                        IN _user_change BIGINT, 
										IN _change_date DATETIME)
	BEGIN
		IF(_operation=5) THEN
			BEGIN
				SELECT 
					id, description
				FROM
					bank
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 5*/
		END IF; /** End operation */
	END$$

call pr_maintenance_bank
(
	5, -- 01 - IN _operation INT, 
	'', -- 02 - IN _id VARCHAR(10), 
	'', -- 03 - IN _description VARCHAR(10), 
	0, -- 04 - IN _inactive INT, 
	1, -- 05 - IN _user_change BIGINT, 
	now() -- 06 - IN _change_date DATETIME
);