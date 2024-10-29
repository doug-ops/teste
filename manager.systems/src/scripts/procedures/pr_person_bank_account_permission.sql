USE `manager`;
DROP PROCEDURE IF EXISTS `pr_person_bank_account_permission`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_person_bank_account_permission` (IN _operation TINYINT, 
													  IN _user_id BIGINT)
BEGIN

	IF(4 = _operation) THEN /** GETALL */
		BEGIN
			   SELECT 
					b.id, b.description, co.id company_id
				FROM 
					person_bank_account_permission pe
				INNER JOIN 
					bank_account b ON b.id = pe.bank_account_id 
				INNER JOIN 
                 (
					SELECT 
						x.person_id, x.bank_account_id
					FROM
					(
						SELECT p.id person_id, p.bank_account_id FROM person p WHERE p.object_type = 'COM' and p.inactive = 0 group by p.id, p.bank_account_id
						UNION ALL 
						SELECT pbi.person_id, pbi.bank_account_id FROM person_bank_account pbi WHERE pbi.inactive = 0 group by pbi.person_id, pbi.bank_account_id
					) x 
					GROUP BY x.person_id, x.bank_account_id 
                 ) pb ON b.id = pb.bank_account_id
				INNER JOIN user us ON us.id = pe.user_id
				INNER JOIN 
				company co ON pb.person_id = co.person_id AND us.client_id = co.client_id
				WHERE 
					pe.user_id = _user_id and pe.inactive = 0
				GROUP BY 
					b.id, b.description, co.id
				ORDER BY 
					b.description;
            
		END; /** End operation 4 */
	END IF;
END$$

CALL pr_person_bank_account_permission(4, 8);


