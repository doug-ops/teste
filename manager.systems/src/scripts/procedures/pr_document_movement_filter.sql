USE `manager`;
DROP procedure IF EXISTS `pr_document_movement_filter`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_document_movement_filter`(IN _operation TINYINT,
                                               IN _date_from DATETIME,
                                               IN _date_to DATETIME,
											   IN _company_id BIGINT,
											   IN _provider_id BIGINT,
                                               IN _financial_group_id VARCHAR(7),
 											   -- IN _financial_sub_group_id VARCHAR(5000)
                                               IN _bank_account_id INT
											   -- IN _document_type VARCHAR(50),
                                               -- IN _document_number VARCHAR(20), 											   
                                               -- IN _moviment_type_credit BIT, 
                                               -- IN _moviment_type_debit BIT, 
                                               -- IN _moviment_type_financial BIT, 
                                               -- IN _moviment_type_open BIT, 
                                               -- IN _moviment_type_close BIT, 
											   -- IN _moviment_type_removed BIT,
                                               -- IN _moviment_transf BIT,
                                               -- IN _moviment_transf_hab BIT,
											   -- IN _filter_data_type INT,
                                               -- IN _id BIGINT,
                                               -- IN _document_id BIGINT,
											   -- IN _document_parent_id BIGINT, 
                                               -- IN _user_operation  varchar(200),
                                               -- IN _product_group_id INT,
                                               -- IN _product_sub_group_id INT,
                                               -- IN _user_childrens_parent VARCHAR(5000),
                                               -- IN _moviment_type_closing BIT,
                                               -- IN _financial_cost_center_id INT
                                               )
BEGIN	
	IF(3=_operation) THEN /* Filter documents */
		select 3;
    END IF; /* End Filter documents */
	IF(4=_operation) THEN /* Get all document */
		
        /**
        drop temporary table if exists temp_fin_sub_group;
		create temporary table temp_fin_sub_group( val varchar(07) );
		set @sql = concat("insert into temp_fin_sub_group (val) values ('", replace(( select group_concat(_financial_sub_group_id) as data), ",", "'),('"),"');");
		prepare stmt1 from @sql;
		execute stmt1;
        */
        
        SELECT 
			mov.* 
		FROM 
        (
			SELECT 
				mov_i.* 
			FROM 
				document_movement mov_i
			WHERE 
				mov_i.creation_date BETWEEN DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s') AND DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s')
                AND mov_i.company_id = (CASE WHEN _company_id > 0 THEN _company_id ELSE mov_i.company_id END)
		) mov_filter
        INNER JOIN document_movement mov ON mov_filter.document_parent_id = mov.document_parent_id
        -- LEFT JOIN temp_fin_sub_group tfsb ON mov.financial_sub_group_id = tfsb.val
        WHERE  1=1
			AND mov.provider_id = (CASE WHEN _provider_id > 0 THEN _provider_id ELSE mov.provider_id END)
            AND mov.bank_account_id = (CASE WHEN _bank_account_id > 0 THEN _bank_account_id ELSE mov.bank_account_id END)
            AND mov.financial_group_id = (CASE WHEN LENGTH(TRIM(_financial_group_id)) > 0 THEN _financial_group_id ELSE mov.financial_group_id END)
            -- AND tfsb.val IS NOT NULL
            ;	
    END IF; /* End Get all document */
END$$
CALL pr_document_movement_filter(
4, -- _operation
'2022-11-14 00:00:00', -- _date_from
'2022-11-20 23:59:59', -- _date_to
0, -- _company_id
0, -- _provider_id,
'24.000', -- _financial_group_id
-- '24.001,24.002', -- _financial_sub_group_id,
1383 -- _bank_account_id
);
/**
	3 - get operation
    4 - get all operation
*/