USE `manager`;
DROP FUNCTION IF EXISTS `func_save_config_user_data`;
USE `manager`$$
DELIMITER $$
CREATE FUNCTION `func_save_config_user_data` ( _user_id BIGINT, _cash_closing_max_discount decimal(19,2))
RETURNS BIT 
BEGIN
	UPDATE user set cash_closing_max_discount = _cash_closing_max_discount where id = _user_id; 
RETURN 1;
END$$
DELIMITER ;
-- SELECT func_save_config_user_data(1, 10)

-- SET GLOBAL log_bin_trust_function_creators = 1;

-- drop function func_save_config_user_data

