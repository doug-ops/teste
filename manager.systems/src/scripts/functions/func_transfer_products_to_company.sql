USE `manager`;
DROP FUNCTION IF EXISTS `func_transf_products_to_company`;
USE `manager`$$
DELIMITER $$
CREATE FUNCTION `func_transfer_products_to_company` ( _company_origin INT, _company_destiny INT)
RETURNS BIT
BEGIN
	UPDATE product set company_id = _company_destiny where company_id = _company_origin; 
RETURN 1;
END$$
DELIMITER ;
-- SELECT func_transfer_products_to_company(696, 1346)

-- SET GLOBAL log_bin_trust_function_creators = 1;