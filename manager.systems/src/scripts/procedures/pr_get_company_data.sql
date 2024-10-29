USE `manager`;
DROP PROCEDURE IF EXISTS `pr_get_company_data`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_get_company_data` (IN _operation INT,
									    IN _company_id BIGINT)
BEGIN
	IF (1=_operation) THEN /** GET DATA INTEGRATION SYSTEMS BRAVO*/
		SELECT 
			c.id company_id, c.social_name company_description, c.client_id, ci.integration_system_id, ci.legacy_id
		FROM 
			company c 
            LEFT JOIN company_integration_system ci ON c.id = ci.company_id AND ci.integration_system_id = 3 AND ci.inactive = 0
        WHERE 
			c.id = _company_id;
	END IF; 
END$$

CALL pr_get_company_data(1, 106);