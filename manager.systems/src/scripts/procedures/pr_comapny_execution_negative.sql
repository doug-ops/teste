USE `manager`;
DROP PROCEDURE IF EXISTS `pr_comapny_execution_negative`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_comapny_execution_negative`( IN _operation INT,
												  IN _company_id BIGINT(20),
                                                  IN _user_id BIGINT)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		BEGIN
			DECLARE v_company_description VARCHAR(100);
			DECLARE v_company_id BIGINT;
			DECLARE v_bank_account_origin_id INT(11);
			DECLARE v_bank_account_destiny_id INT(11);
            DECLARE v_has_movement BIT DEFAULT 1;
						
            SET v_bank_account_destiny_id = 0;
                        
            SELECT 
				c.id, c.social_name, p.bank_account_id, 1 as has_movement
            INTO 
				v_company_id, v_company_description, v_bank_account_origin_id, v_has_movement
            FROM 
				company c
				INNER JOIN user_company uc on c.id=uc.company_id 
				INNER JOIN person p on c.person_id=p.id
			WHERE 
				uc.user_id=_user_id
				AND uc.inactive=0
				AND c.id=_company_id;
				-- select  v_company_id, v_company_description, v_bank_account_origin_id, v_has_movement;
				IF(v_has_movement) THEN
					CALL pr_company_movement_execution(28, null, null, null, null, null, null, v_company_id, null, null, null, v_bank_account_origin_id, v_bank_account_destiny_id, null, v_company_description, null);                             
			   END IF;
        END;
	END IF;
END$$
 --  CALL pr_comapny_execution_negative(1,1219,1 );


