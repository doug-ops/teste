USE `manager`;
DROP procedure IF EXISTS `pr_manager_next_free_code`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_manager_next_free_code` (IN table_search varchar(50), IN initial bigint)
BEGIN
	
    IF(initial = 0 AND table_search = 'company') THEN
		BEGIN
			SET @t1 =CONCAT("SELECT MAX(t.id) + 1 AS next_id FROM ", table_search," t ");
     
			 PREPARE stmt3 FROM @t1;
			 EXECUTE stmt3;
			 DEALLOCATE PREPARE stmt3; 
		END;
    ELSE
		BEGIN
			SET @t1 =CONCAT("SELECT t.id + 1 AS next_id FROM ", table_search," t LEFT JOIN ", table_search, " t1 ON t1.id = t.id + 1 WHERE t.id>=",initial," and t1.id IS NULL ORDER BY t.id LIMIT 0, 1;");
     
			 PREPARE stmt3 FROM @t1;
			 EXECUTE stmt3;
			 DEALLOCATE PREPARE stmt3; 
        END;
	END IF;
END$$

call pr_manager_next_free_code('company', 0);

select * from 