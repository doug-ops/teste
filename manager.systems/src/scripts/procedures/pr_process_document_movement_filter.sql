USE `manager`;
DROP PROCEDURE IF EXISTS `pr_process_document_movement_filter`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_document_movement_filter` (IN _operation INT, IN _company_id BIGINT, IN _is_offline BIT)
BEGIN
	DECLARE _limit INT;
    
	IF(3=_operation) THEN /** GET */
    
		IF(IFNULL(_is_offline,0)=0) THEN
			BEGIN
				SET _limit = 1;
			END; 
		ELSE 
			BEGIN
				SET _limit = 1000;
			END; END IF;
            
		SELECT
			pm.id as movement_id, co.id as company_id
		FROM
			product_movement pm
			INNER JOIN product po ON pm.product_id=po.id
			INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
			INNER JOIN company co ON (CASE WHEN 1=_is_offline THEN pm.company_id=co.id ELSE pm.company_description=co.social_name END)
			INNER JOIN person pe ON co.person_id=pe.id
		WHERE
			IFNULL(pm.processing,0)=0
			AND IFNULL(pm.movement_type,'L')='A'
            AND IFNULL(pm.has_error,0)=0
            AND IFNULL(co.process_movement_automatic,0)=1
		ORDER BY
			pm.reading_date
		LIMIT _limit;
	END IF; /** End operation 3 */
END$$

/**
CALL pr_process_document_movement_filter(
	3, -- IN _operation INT,
    0, -- IN _company_id BIGINT, 
	0 -- IN _is_offline BIT
);
-- update company set process_movement_automatic=1 where id=79;
*/

