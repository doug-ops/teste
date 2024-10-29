USE `manager`;
DROP PROCEDURE IF EXISTS `pr_provider_statement_report`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_provider_statement_report`(IN _providers_ids VARCHAR(500),
                                                IN _date_from DATETIME, 
                                                IN _date_to DATETIME,
                                                IN _document_type_ids VARCHAR(500),
                                                IN _group_by VARCHAR(500),
                                                IN _user_childrens_parent VARCHAR(5000))
BEGIN    
	/**Drop temporary tables*/
	DROP TEMPORARY TABLE IF EXISTS tmp_provider_values;
	DROP TEMPORARY TABLE IF EXISTS tmp_provider_filter;
    DROP TEMPORARY TABLE IF EXISTS tmp_document_type_filter;
	/**End Drop temporary tables*/

	/**Temporary table to insert tmp_provider_values*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_provider_values
	(
		txt text
	);
	/**End Temporary table to insert tmp_provider_values*/

	/**Temporary table to insert tmp_provider_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_provider_filter
	(
		provider_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_provider_filter*/
    
	/**Temporary table to insert tmp_provider_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_document_type_filter
	(
		document_type TINYINT NOT NULL
	);
	/**End Temporary table to insert tmp_document_type_filter*/
    
    INSERT INTO tmp_provider_values VALUES(_providers_ids);
			SET @sql = concat("INSERT INTO tmp_provider_filter (provider_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_provider_values), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
    IF(_document_type_ids is not null) THEN
		BEGIN
			SET @sql = concat("INSERT INTO tmp_document_type_filter (document_type) VALUES ('", REPLACE((SELECT group_concat(DISTINCT x.txt) as data FROM (select _document_type_ids txt) x), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
        END;
	END IF;
			SET @sql = concat("SELECT x.document_parent_id, x.document_id, x.document_type, ",
            "case when x.document_type = 2 and x.document_value < 0 then 'Transf. D' ",
					 "when x.document_type = 2 and x.document_value >= 0 then 'Transf. C' ",
                     "when x.document_type = 3 then 'Despesa' ",
                     "when x.document_type = 1 then 'Mov. Loja' ",
                     "when x.document_type = 4 then 'Mov. Loja' ",
				"end document_type_description, ",
				"case when x.document_type = 2 and x.document_value < 0 then c.fantasy_name ",
					 "when x.document_type = 2 and x.document_value >= 0 then c.fantasy_name ",
                     "when x.document_type = 3 then p.fantasy_name ",
                     "when x.document_type = 1 then c.fantasy_name ",
                     "when x.document_type = 4 then c.fantasy_name ",
				"end movement_description, ",
            "x.provider_id, p.fantasy_name AS provider_description, x.is_group_movement, ",
			"DATE_FORMAT(x.payment_data, '%d/%m/%Y') AS document_date, ",
			"DATE_FORMAT(x.payment_data, '%Y%m%d%H%i%s') AS document_date_long, ",
            "x.document_value, x.company_id, c.fantasy_name AS company_description, ",
            "x.financial_group_id, x.financial_sub_group_id, ",
            "(select description from financial_group where id = x.financial_group_id limit 1) AS financial_group_description, "
            "(select description from financial_sub_group where id = x.financial_sub_group_id and financial_group_id = x.financial_group_id limit 1) AS financial_sub_group_description ",
            "FROM (", "SELECT ",
				IF((length(_group_by) = 0), "d.document_parent_id, ", IF((POSITION("d.document_parent_id" IN _group_by)>0), "d.document_parent_id, ", " MAX(d.document_parent_id) AS document_parent_id, ")),
                IF((length(_group_by) = 0), "d.document_id, ", "max(d.document_id) AS document_id, "),
                IF((length(_group_by) = 0), "d.document_type, ", IF((POSITION("d.document_type" IN _group_by)>0), "d.document_type, ", " MAX(d.document_type) AS document_type, ")),
                IF((length(_group_by) = 0), "d.provider_id, ", IF((POSITION("d.provider_id" IN _group_by)>0), "d.provider_id, ", " MAX(d.provider_id) AS provider_id, ")),
                IF((length(_group_by) = 0), "0 AS is_group_movement, ", "1 AS is_group_movement, "), " ",
				IF((length(_group_by) = 0), "DATE(d.payment_data) payment_data, ", IF((POSITION("d.payment_data" IN _group_by)>0), "DATE(d.payment_data) payment_data, ", " MAX(DATE(d.payment_data)) AS payment_data, ")),
				IF((length(_group_by) = 0), "CASE WHEN d.credit = 1 THEN d.document_value else (d.document_value * -1) END document_value, ", "SUM(CASE WHEN d.credit = 1 THEN d.document_value else (d.document_value * -1) END) AS document_value, "), " ",
				IF((length(_group_by) = 0), "d.company_id, ", IF((POSITION("d.company_id" IN _group_by)>0), "d.company_id, ", " MAX(d.company_id) AS company_id, ")),
                IF((length(_group_by) = 0), "d.financial_group_id, ", IF((POSITION("d.financial_group_id" IN _group_by)>0), "d.financial_group_id, ", " MAX(d.financial_group_id) AS financial_group_id, ")),
				IF((length(_group_by) = 0), "d.financial_sub_group_id ", IF((POSITION("d.financial_sub_group_id" IN _group_by)>0), "d.financial_sub_group_id ", " MAX(d.financial_sub_group_id) AS financial_sub_group_id ")),
			"FROM ",
				"tmp_provider_filter f ",
				"INNER JOIN document_movement d ON f.provider_id=d.provider_id ",
                'INNER JOIN (select company_id from user_company where user_id in (',_user_childrens_parent,') group by company_id) uc ON uc.company_id = d.company_id ',
                "INNER JOIN tmp_document_type_filter df on d.document_type = df.document_type ",
			"WHERE ",
				"d.payment_data BETWEEN '", _date_from, "' AND '", _date_to, "' ",
				" AND IFNULL(d.inactive,0)=0 ",
                IF((length(_group_by)>0), concat(' GROUP BY ', _group_by), " "), 
			") x ",
            "INNER JOIN provider p ON x.provider_id = p.id ",
            "INNER JOIN company c ON x.company_id = c.id ",
            " ORDER BY  ",
					-- "document_date_long,  ",
                    "document_id; ");      
                    
                 -- select @sql;
                    PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
END$$

-- CALL pr_provider_statement_report('2379', '2023-06-01 00:00:00', '2023-06-28 23:59:59','1,2,3,4','d.provider_id, d.financial_group_id, DATE(d.payment_data), d.company_id');
-- 0 nao agrupar
-- 1 por fornecedor
-- 2 por data
-- 3 loja
-- 4 grupo fin
-- 5 sub grupo fin
-- 6 duplicata
-- 7 document_type