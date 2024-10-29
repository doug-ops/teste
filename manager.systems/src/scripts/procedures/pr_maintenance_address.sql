USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_address`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_address` (IN operation int)
BEGIN	
	IF 1=operation THEN
		select 
			state_acronym,
            address_state_ibge,
			description
		from 
			address_state
		order by 
			description;
   ELSEIF 2=operation THEN
		select 
			country_acronym,
            address_country_ibge,
			description
		from 
			address_country
		order by 
			description;
   ELSE
       SIGNAL SQLSTATE '45000'
	   SET MESSAGE_TEXT = 'Operacao invalida.';
   END IF;
END$$

/**
	Operation
	1 - State List
    2 - Country List
*/
-- call pr_maintenance_address(2);