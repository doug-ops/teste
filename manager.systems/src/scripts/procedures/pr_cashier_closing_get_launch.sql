USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_closing_get_launch`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_get_launch`(IN _operation TINYINT, 
												 IN _cashier_closing_id BIGINT,
												 IN _user_operator BIGINT)                                                   
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING GET LAUNCH', @full_error, 'emarianodeveloper@gmail.com', NOW(), 0, NOW(), 0;			
        
		SELECT @full_error;
	END;

	IF(1 = _operation) THEN /** Operation 1 */
		BEGIN            
            
           SELECT 
				la.id, 
				la.cashier_closing_id, 
				la.cashier_closing_status, 
				la.user_operator, 
				la.company_id, 
				la.bank_account_id, 
                ba.description bank_account_description,
				la.provider_id, 
                po.social_name provider_description,
				la.financial_group_id, 
                fg.description description_financial_group,
				la.financial_sub_group_id, 
                fs.description description_financial_sub_group,
				la.credit, 
				la.expense, 
				la.document_status, 
				date_format(la.payment_data,'%d/%m/%y') payment_data, 
				la.document_value, 
				la.document_note, 
				la.user_creation, 
				la.creation_date, 
				la.user_change, 
				la.change_date, 
				la.launch_user, 
				la.launch_date, 
				la.close_user, 
				la.close_date, 
				la.inactive
			FROM 
				cashier_closing_company_launch la
                LEFT JOIN bank_account ba ON la.bank_account_id = ba.id
                LEFT JOIN provider po ON la.provider_id = po.id
                LEFT JOIN financial_group fg ON la.financial_group_id = fg.id
                LEFT JOIN financial_sub_group fs ON la.financial_sub_group_id = fs.id AND la.financial_group_id = fs.financial_group_id
			WHERE
				la.cashier_closing_id = _cashier_closing_id
				AND la.user_operator = _user_operator
                AND IFNULL(la.cashier_closing_status,0) < 2;

        END; /** End Operation 1 */     
    END IF;
END$$