USE `manager`;
DROP procedure IF EXISTS `pr_combobox`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_combobox`(IN _operation INT,
							   IN _inactive BIT)
BEGIN	
	IF(1=_operation) THEN /** revenue_source */
		BEGIN
			SELECT id, description FROM revenue_source WHERE inactive=_inactive ORDER BY description;
        END;
	ELSEIF (2=_operation) THEN /** revenue_type */
		BEGIN
			SELECT id, description FROM revenue_type WHERE inactive=_inactive ORDER BY description;
        END;
    END IF;
END$$

/**
CALL pr_combobox(1, 0);
*/
