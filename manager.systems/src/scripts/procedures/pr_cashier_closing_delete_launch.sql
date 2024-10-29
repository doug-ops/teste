USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_closing_delete_launch`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_delete_launch`(IN _operation TINYINT, 
												    IN _cashier_closing_id BIGINT,
												    IN _user_operator BIGINT)                                                   
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING DELETE LAUNCH', @full_error, 'emarianodeveloper@gmail.com', NOW(), 0, NOW(), 0;			
        
		SELECT @full_error;
	END;
	
    IF(1 = _operation) THEN /** Operation 1 */
		BEGIN            
            
           DELETE 
		   FROM 
				cashier_closing_company_launch la
			WHERE
				la.cashier_closing_id = _cashier_closing_id
				AND la.user_operator = _user_operator
                AND IFNULL(la.cashier_closing_status,0) < 2;

        END; /** End Operation 1 */        
    END IF;
END$$

-- CALL pr_cashier_closing_delete_launch(1, 1, 8  );