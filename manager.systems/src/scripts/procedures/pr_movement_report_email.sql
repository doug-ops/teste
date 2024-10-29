USE `manager`;
DROP procedure IF EXISTS `pr_movement_report_email`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_movement_report_email` (IN _operation INT, IN _document_parent_id BIGINT, IN _company_id BIGINT)
BEGIN
		IF(1=_operation) THEN
			BEGIN
				/**Get bank_balance_available */
				SELECT ba.id AS bank_account_id, ba.description AS bank_account_description, ba.bank_balance_available FROM company co
				INNER JOIN person pe ON co.person_id=pe.id
				INNER JOIN bank_account ba ON pe.bank_account_id=ba.id
				WHERE co.id=_company_id;
    
				/**Get Company data */                
				SELECT id, social_name AS company_description FROM  company where id=_company_id LIMIT 1;
				
				select
                d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin, d.document_id_origin, d.document_transaction_id, 
                d.is_cashing_close, d.document_type, credit, d.company_id, d.provider_id, d.bank_account_id, d.bank_account_origin_id, 
                d.document_transfer_id, d.is_document_transfer, d.is_automatic_transfer, d.document_number, d.document_value, d.document_note, 
                d.document_status, d.payment_value, d.payment_discount, d.payment_extra, d.payment_residue, d.payment_data, d.payment_expiry_data, 
                d.payment_user, d.payment_status, d.nfe_number, d.nfe_serie_number, d.generate_billet, d.financial_group_id, d.financial_sub_group_id, 
                d.is_product_movement, d.product_id, d.product_description, d.group_transfer_id, d.group_execution_order, d.group_item_execution_order, 
                d.cashier_closing_date, d.inactive, d.user_creation, d.creation_date, d.user_change, d.change_date, d.financial_cost_center_id, 
                IFNULL(u.name,'-') as user_change_name from document_movement d 
                left join user u on d.user_change=u.id
                where d.document_parent_id=_document_parent_id and d.company_id=_company_id order by d.group_execution_order, d.group_item_execution_order, d.id;
            END;
		ELSEIF(2=_operation) THEN
			BEGIN
				select s.id, s.email, s.document_parent_id, s.company_id from send_email_document s 
				inner join document_movement doc on s.document_parent_id = doc.document_parent_id and s.company_id = doc.company_id
				where s.is_send=0 and s.inactive=0 limit 5;
            END;
		ELSEIF(3=_operation) THEN
			BEGIN
				update send_email_document set is_send=1, change_date=sysdate() where document_parent_id=_document_parent_id and company_id=_company_id;
            END;            
		END IF;
END$$

/**
CALL pr_movement_report_email
(
	1,    -- 01 - IN _operation INT
	216667, -- 02 - IN _document_parent_id BIGINT
	37   -- 03 - IN company_id BIGINT
);
*/
CALL pr_movement_report_email(1,428319,671 );