USE `manager`;
DROP procedure IF EXISTS `pr_filter_bank_account_by_company`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_filter_bank_account_by_company` 
(
	IN _operation INT,
    IN _companys VARCHAR(1000)
)
BEGIN
	IF(1=_operation) THEN
        BEGIN
			SET @t1 = CONCAT('SELECT company_id, bank_account_id, ba.description bank_account_description, ba.bank_balance_available ', 
			'FROM ', 
			'( ',
			'SELECT ', 
			'c.id company_id, p.bank_account_id  ',
			'FROM  ',
			'company c ', 
			'INNER JOIN person p ON c.person_id = p.id  ',
			'WHERE c.id IN (', _companys, ') ',
			'UNION ALL ', 
			'SELECT  ',
			'c.id company_id, pb.bank_account_id  ',
			'FROM  ',
			'company c ', 
			'INNER JOIN person p ON c.person_id = p.id  ',
			'INNER JOIN person_bank_account pb ON p.id = pb.person_id  ',
			'WHERE c.id IN (', _companys, ') AND pb.inactive = 0 ',
			') x ',
			'INNER JOIN bank_account ba ON x.bank_account_id = ba.id ',
			'GROUP BY x.company_id, x.bank_account_id ');                          
            -- select @t1;
            PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /** End operation 1 */
	END IF; /**End if operation */ 
END$$

CALL pr_filter_bank_account_by_company(1, '1009, 1616, 182');

