USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_financial_transfer_offline`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_financial_transfer_offline`(IN _operation INT,
															 IN _bank_account_origin_id	INT,
															 IN _bank_account_destiny_id INT,
															 IN _group_id INT,
                                                             IN _group_item_id INT,
															 IN _company_id	BIGINT,
															 IN _value_transfer	DECIMAL(19,2),
                                                             IN _expense INT,
															 IN _inactive INT,
															 IN _user_change BIGINT)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		BEGIN
			IF(_group_item_id > 0) THEN /** Save financitial group item*/
				UPDATE 
					financial_transfer_group_item_setting 
				SET 
					value_transfer = _value_transfer,
					inactive = _inactive,
					user_change = _user_change,
					change_date = now()
				WHERE
					group_id = _group_id
					and id = _group_item_id;
			END IF; /** End Save financitial group item*/
        END; /** End operation 2 */
	ELSEIF(4=_operation) THEN /** GET ALL*/
		BEGIN
			SELECT 
				gu.id as group_id,
				gu.bank_account_origin_id, 
				(SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_origin_id) as bank_account_origin_description,
				gu.bank_account_destiny_id,
				(CASE WHEN gu.bank_account_destiny_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_destiny_id) END) as bank_account_destiny_description,
				gu.execution_period,
				gu.execution_initial_period,
				gu.bank_account_automatic_transfer_id,
				(CASE WHEN gu.bank_account_automatic_transfer_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gu.bank_account_automatic_transfer_id) END) as bank_account_automatic_transfer_description,                        
				gu.description as group_description,
				gu.execution_order as group_execution_order, 
				gu.inactive as inactive_group,
				gu.user_change,
				(SELECT IFNULL(name,'-') FROM user WHERE id=gu.user_change) as user_change_name,
				gu.change_date,						
				gi.id as group_item_id,
				gi.execution_order as group_item_execution_order,
				gi.provider_id,
				(SELECT IFNULL(social_name,'-') FROM provider WHERE id=gi.provider_id) as provider_name,
                '' as financial_group_id,
				'' as description_financial_group,
				'' as financial_sub_group_id,
				'' as description_financial_sub_group,

				gi.description_transfer,
				gi.transfer_type,
				gi.value_transfer,
				gi.bank_account_id,
				(CASE WHEN gi.bank_account_id=0 THEN '-' ELSE (SELECT IFNULL(description,'-') FROM bank_account WHERE id=gi.bank_account_id) END) as bank_account_description,
				gi.credit_debit,
				gi.transfer_state,
				gi.is_over_total,
				gi.expense,
				gi.is_use_remaining_balance,
				gi.inactive AS inactive_transfer,
                gi.group_product_id, 
                IFNULL(gp.description, 'Nao Informado')  AS group_product_description,
				gi.sub_group_product_id,
                IFNULL(sgp.description, 'Nao Informado')  AS sub_group_product_description
			FROM               
				(select pe.bank_account_id as bank_account_origin_id, 0 as bank_account_destiny_id, _company_id company_id from company co 
                INNER JOIN person pe ON co.person_id=pe.id WHERE co.id = _company_id LIMIT 1) x
				INNER JOIN financial_transfer_group_setting gu ON x.bank_account_origin_id = gu.bank_account_origin_id and x.bank_account_destiny_id = gu.bank_account_destiny_id
				INNER JOIN financial_transfer_group_item_setting gi ON gu.id=gi.group_id and x.bank_account_origin_id = gi.bank_account_origin_id and x.bank_account_destiny_id = gi.bank_account_destiny_id
                LEFT JOIN product_group gp ON gi.group_product_id = gp.id
                LEFT JOIN product_sub_group sgp ON gi.sub_group_product_id = sgp.id
			WHERE
				gu.id=(CASE WHEN IFNULL(_group_id,0)=0 THEN gu.id ELSE _group_id END)
				AND gu.bank_account_origin_id=(CASE WHEN IFNULL(_bank_account_origin_id,0)=0 THEN gu.bank_account_origin_id ELSE _bank_account_origin_id END) 
				AND gu.bank_account_destiny_id=(CASE WHEN IFNULL(_bank_account_destiny_id,0)=0 THEN gu.bank_account_destiny_id ELSE _bank_account_destiny_id END) 
				AND gi.id=(CASE WHEN IFNULL(_group_item_id,0)=0 THEN gi.id ELSE _group_item_id END)
                AND gi.expense = _expense
                AND gu.inactive = _inactive
                AND gi.inactive = _inactive;
        END; /** End operation 4 */
	END IF;
END$$

/* Operations */
/* 1 - Save/Update */
/* 2 - Inactive */
/* 3 - Get */
/* 4 - GetAll */