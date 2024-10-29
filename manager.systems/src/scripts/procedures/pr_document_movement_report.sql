USE `manager`;
DROP procedure IF EXISTS `pr_document_movement_report`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_document_movement_report`(IN _operation INT,
											   IN _company_id BIGINT,
											   IN _provider_id BIGINT,
                                               IN _financial_group_id VARCHAR(7),
 											   IN _financial_sub_group_id VARCHAR(5000),
                                               IN _bank_account_id INT,
											   IN _document_type VARCHAR(50),
                                               IN _document_number VARCHAR(20), 											   
                                               IN _moviment_type_credit BIT, 
                                               IN _moviment_type_debit BIT, 
                                               IN _moviment_type_financial BIT, 
                                               IN _moviment_type_open BIT, 
                                               IN _moviment_type_close BIT, 
											   IN _moviment_type_removed BIT,
                                               IN _moviment_transf BIT,
                                               IN _moviment_transf_hab BIT,
                                               IN _date_from DATETIME,
                                               IN _date_to DATETIME,
											   IN _filter_data_type INT,
                                               IN _id BIGINT,
                                               IN _document_id BIGINT,
											   IN _document_parent_id BIGINT, 
                                               IN _user_operation  varchar(200),
                                               IN _product_group_id INT,
                                               IN _product_sub_group_id INT,
                                               IN _user_childrens_parent VARCHAR(5000),
                                               IN _moviment_type_closing BIT,
                                               IN _financial_cost_center_id INT,
                                               IN _moviment_type_launch BIT,
                                               IN _moviment_type_input_movement BIT,
                                               IN _moviment_type_output_movement BIT,
                                               IN _moviment_type_duplicate_movement BIT)										
BEGIN	
	IF(3=_operation) THEN
		SET @t1 = CONCAT('SELECT m.id, ', 
						  'm.document_id, ', 
                          'm.document_parent_id, ', 
                          'm.document_type, ', 
                          'm.credit, ', 
                          'm.company_id, ', 
                          'm.provider_id, ', 
                          'm.bank_account_id, ',						
						  'IFNULL(IFNULL(m.financial_group_id,pr.financial_group_id),'''') AS financial_group_id, ', 
                          'IFNULL(IFNULL(m.financial_sub_group_id,pr.financial_sub_group_id),'''') AS financial_sub_group_id, ', 
                          'IFNULL(m.document_number,''-'') as document_number, ', 
                          'm.document_value, ', 
                          'm.document_note, ', 
                          'm.document_status, ', 
                          'm.payment_value, ', 
						  'm.payment_discount, m.payment_extra, m.payment_residue, DATE_FORMAT(m.payment_data,''%d/%m/%Y'') as payment_data, DATE_FORMAT(m.payment_expiry_data,''%d/%m/%Y'') as payment_expiry_data, ', 
						  'm.payment_user, IFNULL(m.payment_status,2) AS payment_status, m.inactive, m.user_creation, DATE_FORMAT(m.creation_date,''%d/%m/%Y %H:%i'') as creation_date, m.user_change, DATE_FORMAT(m.change_date,''%d/%m/%Y %H:%i'') as change_date, ', 
						  'IFNULL((SELECT ba.description FROM bank_account ba WHERE m.bank_account_id=ba.id LIMIT 1),''-'') as bank_account_description, ',
                          'IFNULL((SELECT co.social_name FROM company co WHERE m.company_id=co.id LIMIT 1),''-'') as company_description, ',
                          'IFNULL(pr.social_name,''-'') as provider_description, ',
                          -- 'IFNULL((SELECT fg.description FROM financial_group fg WHERE IFNULL(IFNULL(m.financial_group_id,pr.financial_group_id),'''')=fg.id LIMIT 1),''-'') as description_financial_group, ',
                          -- 'IFNULL((SELECT fsg.description FROM financial_sub_group fsg WHERE IFNULL(IFNULL(m.financial_sub_group_id,pr.financial_sub_group_id),'''')=fsg.id LIMIT 1),''-'') as description_financial_sub_group, ',
                          'CASE WHEN m.document_note LIKE ''MAQ:%'' and m.credit = 1 THEN ''E'' ',
							   'WHEN m.document_note LIKE ''MAQ:%'' and m.credit = 0 THEN ''S'' ',
							   'WHEN m.document_note LIKE ''GRUPO:%'' THEN ''T'' ',
							   'ELSE '''' END as initials, ',
						  'IFNULL(pm.product_id,0) AS product_id, ',
                          'IFNULL((SELECT po.description FROM product po WHERE pm.product_id=po.id LIMIT 1),''-'') AS product_description, ',
                          '(CASE WHEN pm.reading_date IS NULL THEN ''-'' ELSE DATE_FORMAT(pm.reading_date,''%d/%m/%Y'') END) AS reading_date, ',
                          '(CASE WHEN pm.initial_date IS NULL THEN ''-'' ELSE DATE_FORMAT(pm.initial_date,''%d/%m/%Y'') END) AS initial_date, ',
                          'pm.credit_in_initial, pm.credit_in_final, pm.credit_out_initial, pm.credit_out_final ',
						  'FROM document_movement m ',
                          'LEFT JOIN product_movement pm ON m.document_parent_id=pm.document_parent_id AND m.company_id=pm.company_id AND m.document_id=(CASE WHEN m.credit THEN pm.document_in_id ELSE pm.document_out_id END) ',
                          'LEFT JOIN provider pr on m.provider_id=pr.id ',
                          ' WHERE 1=1 ', 
                         IF((_company_id>0), concat(' AND m.company_id=', _company_id), ''), 
                         IF((_document_id>0), concat(' AND m.document_id=', _document_id), ''),
                         IF((_document_parent_id>0), concat(' AND m.document_parent_id=', _document_parent_id), ''),
                         ' order by document_parent_id, credit desc;');
						-- select @t1;
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;			 
    END IF;
	IF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT m.id, m.document_id, m.document_parent_id, m.document_type, m.credit, m.company_id, m.provider_id, pr.social_name provider_description, m.bank_account_id, m.bank_account_origin_id, bao.description bank_account_origin_description, ', 
						 'm.financial_group_id, m.financial_sub_group_id, IFNULL(m.document_transfer_id,0) as document_transfer_id, IFNULL(m.document_number,''-//-'') as document_number, (case when (m.credit = 1 and m.document_value < 0) then (m.document_value * -1) else m.document_value end) document_value, x.document_total, ',
                         'CASE WHEN document_note LIKE ''DESPESA TRANSF. FEC. CAIXA >> FORNECEDOR:%'' THEN CONCAT(replace(m.document_note, ''FORNECEDOR: '', CONCAT(''FORNECEDOR: '', pr.social_name, '' '')),''/'', ''CONTA: '', bao.description) ', 
                         'WHEN document_note LIKE ''TRANSF. FEC. CAIXA >> FORNECEDOR:%'' AND m.credit = 0 THEN CONCAT(''TRANSF. SALDO >> CONTA ORIGEM: '', ba.description,'' / '', ''CONTA DESTINO: '', bao.description, REPLACE(document_note, (CASE WHEN POSITION(''OBS:'' IN document_note) > 0 THEN substring(document_note, 1, POSITION(''OBS:'' IN document_note)+3) ELSE '''' END), ''''))   ', 
                         'WHEN document_note LIKE ''TRANSF. FEC. CAIXA >> FORNECEDOR:%'' AND m.credit = 1 THEN CONCAT(''TRANSF. SALDO TRANSF. FEC. CAIXA >> '', ''CONTA DESTINO: '', bao.description, REPLACE(document_note, (CASE WHEN POSITION(''OBS:'' IN document_note) > 0 THEN substring(document_note, 1, POSITION(''OBS:'' IN document_note)+3) ELSE '''' END), ''''))   ', 
                         'ELSE m.document_note END document_note, ',
                         'm.document_status, m.payment_value, m.payment_discount, m.payment_extra, m.payment_residue, ',
                         'DATE_FORMAT(m.payment_data,''%d/%m/%Y'') as payment_data, DATE_FORMAT(m.payment_expiry_data,''%d/%m/%Y'') as payment_expiry_data, ', 
						 'm.payment_user, m.inactive, m.user_creation, DATE_FORMAT(m.creation_date,''%d/%m/%Y %H:%i'') as creation_date, m.user_change, ',
                         'DATE_FORMAT(m.change_date,''%d/%m/%Y %H:%i'') as change_date, ', 
						 'IFNULL(ba.description,''-//-'') as bank_account_description, ',
                         'IFNULL(co.social_name,''-//-'') as company_description, ',
                         'IFNULL(pr.social_name,''-//-'') as provider_description, ',
                         'IFNULL(fg.description,''-//-'') as description_financial_group, ',
                         'IFNULL(fsg.description,''-//-'') as description_financial_sub_group, ',
                         'IFNULL(m.product_id, 0) as product_id, ',
                         'IFNULL(m.product_description,''-//-'') as product_description, ',
                         'IFNULL(m.is_product_movement,0) as is_product_movement, ',
                         'IFNULL(m.is_document_transfer,0) as is_document_transfer, ',
                         'IFNULL(m.is_automatic_transfer,0) as is_automatic_transfer ',
						 'FROM document_movement m ',
                         'INNER JOIN (select company_id from user_company where user_id in (',_user_childrens_parent,') and inactive = 0 group by company_id) uc ON uc.company_id=m.company_id ',
                         'INNER JOIN (SELECT document_parent_id, company_id, sum(case when (credit=1 and document_value < 0) then (document_value * -1) when (credit=1 and document_value >= 0) then document_value else (document_value*-1) end) document_total ',        
                         'FROM document_movement mi ', 
                         'left join bank_account_out_document_parent bank_out on mi.bank_account_id=bank_out.bank_account_id and bank_out.inactive=0 ',
						 ' WHERE 1=1 ', 
                         IF((_company_id>0), concat(' AND mi.company_id=', _company_id), ''), 
                         IF((_provider_id>0), concat(' AND mi.provider_id=', _provider_id), ''), 
                         IF((_bank_account_id>0), concat(' AND mi.bank_account_id=', _bank_account_id), ''), 
                         IF((length(IFNULL(_document_type,''))>0), concat(' AND mi.document_type in (', _document_type, ') '), ''), 
						 IF((length(IFNULL(_document_number,''))>0), concat(' AND mi.document_number like ''%', _document_number, '%'''), ''),
						
                         IF((IFNULL(_provider_id,0)=0 AND _moviment_transf_hab=0), concat(' AND IFNULL(bank_out.bank_account_id,0)=', 0), ''), 
                         
                         IF((_filter_data_type=1), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=2), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=3), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         
                         IF((_moviment_type_removed=1), concat(' AND IFNULL(mi.inactive,0)=', 1), ''),
                         IF((_moviment_type_removed=0), concat(' AND IFNULL(mi.inactive,0)=', 0), ''),
                         
                         ' GROUP BY mi.document_parent_id, mi.company_id) x ON x.document_parent_id=m.document_parent_id AND x.company_id=m.company_id '
                         'LEFT JOIN bank_account ba on m.bank_account_id=ba.id ',
                         'LEFT JOIN bank_account bao on m.bank_account_origin_id=bao.id ',
                         'LEFT JOIN company co on m.company_id=co.id ',
                         'LEFT JOIN provider pr on m.provider_id=pr.id ',
                         'LEFT JOIN financial_group fg on m.financial_group_id=fg.id ',
                         'LEFT JOIN financial_sub_group fsg on m.financial_sub_group_id=fsg.id ',
                         'WHERE 1=1 ',
                         
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=0), concat(' AND IFNULL(m.credit,0)=', 1), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=1), concat(' AND IFNULL(m.credit,0)=', 0), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=0), '', ''),
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=1), '', ''),
                         
						 IF((_moviment_type_close=1 AND _moviment_type_open=0), concat(' AND IFNULL(m.document_status,1)=', 2), ''),
                         IF((_moviment_type_close=0 AND _moviment_type_open=1), concat(' AND IFNULL(m.document_status,1)=', 1), ''),
                         IF((_moviment_type_close=0 AND _moviment_type_open=0), '', ''),
                         IF((_moviment_type_close=1 AND _moviment_type_open=1), '', ''),
                         
                         IF((IFNULL(_moviment_transf,0) = 1 AND IFNULL(_moviment_type_launch,0) = 1), ' AND m.document_type in (1, 4, 2, 3) ', 
                         IF((IFNULL(_moviment_transf,0) = 1 AND IFNULL(_moviment_type_launch,0) = 0), ' AND m.document_type in (1, 4, 3) ', 
                         IF((IFNULL(_moviment_transf,0) = 0 AND IFNULL(_moviment_type_launch,0) = 1), ' AND m.document_type in (1, 4, 2) ', 
                         IF((IFNULL(_moviment_transf,0) = 0 AND IFNULL(_moviment_type_launch,0) AND IFNULL(_company_id,0) > 0), '', ' AND m.document_type in (1, 4) ')))), 
                         
						 IF((LENGTH(IFNULL(_financial_group_id,''))>0), concat(' AND m.financial_group_id in (', _financial_group_id, ')'), ''), 
                         IF((LENGTH(IFNULL(_financial_sub_group_id,''))>0), concat(' AND m.financial_sub_group_id in (', _financial_sub_group_id, ')'), ''), 
						 IF((LENGTH(IFNULL(_financial_cost_center_id,''))>0), concat(' AND m.financial_cost_center_id=', _financial_cost_center_id), ''), 
						 IF((IFNULL(_product_group_id,0)>0 and IFNULL(_product_sub_group_id,0)=0), concat(' AND m.product_id in (select id from product WHERE product_group_id=',_product_group_id,' group by id) '), ''), 
						 IF((IFNULL(_product_group_id,0)>0 and IFNULL(_product_sub_group_id,0)>0), concat(' AND m.product_id in (select id from product WHERE product_group_id=',_product_group_id,' AND product_sub_group_id=',_product_sub_group_id,')'), ''), 
                         IF((IFNULL(_product_group_id,0)=0 and IFNULL(_product_sub_group_id,0)>0), concat(' AND m.product_id in (select id from product WHERE product_sub_group_id=',_product_sub_group_id,')'), ''), 
                         
						 IF((_moviment_type_removed=1), concat(' AND IFNULL(m.inactive,0)=', 1), ''),
                         IF((_moviment_type_removed=0), concat(' AND IFNULL(m.inactive,0)=', 0), ''),
                         
                         IF((_bank_account_id>0), concat(' AND m.bank_account_id=', _bank_account_id), ''), 
                         
                         ' ORDER BY m.document_parent_id, m.credit desc, m.document_id;');
              
             -- select @t1;
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3; 
		DEALLOCATE PREPARE stmt3;			 
    END IF;
    	IF(5=_operation) THEN
		SET @t1 = CONCAT('SELECT m.id, m.document_parent_id, m.document_id, m.company_id, IFNULL(co.social_name,''SEM DESCRICAO'') as company_description, m.document_type,', 
						 'm.credit, (case when m.credit = 1 then m.document_value else (m.document_value * -1) end) document_value, ',
                         'IFNULL(m.product_id, 0) as product_id, ',
                         'IFNULL(m.is_product_movement,0) as is_product_movement, ',
                         'm.document_note, DATE_FORMAT(m.creation_date,''%d/%m/%y'') as creation_date, DATE_FORMAT(m.payment_data,''%d/%m/%y'') as payment_data, ',
                         'DATE_FORMAT(m.creation_date,''%Y%m%d%h%m%s'') as initial_date_long, DATE_FORMAT(m.payment_data,''%Y%m%d%h%m%s'') as final_date_long ',
						 'FROM document_movement m ',
                         'INNER JOIN (select company_id from user_company where user_id in (',_user_childrens_parent,') and inactive = 0 group by company_id) uc ON uc.company_id=m.company_id ',
                         'INNER JOIN (SELECT document_parent_id, company_id, sum(case when (credit=1 and document_value < 0) then (document_value * -1) when (credit=1 and document_value >= 0) then document_value else (document_value*-1) end) document_total ',        
                         'FROM document_movement mi ', 
                         'left join bank_account_out_document_parent bank_out on mi.bank_account_id=bank_out.bank_account_id and bank_out.inactive=0 ',
						 ' WHERE 1=1 ', 
                         IF((_company_id>0), concat(' AND mi.company_id=', _company_id), ''), 
                         IF((_provider_id>0), concat(' AND mi.provider_id=', _provider_id), ''), 
                         IF((_bank_account_id>0), concat(' AND mi.bank_account_id=', _bank_account_id), ''), 
                         IF((length(IFNULL(_document_type,''))>0), concat(' AND mi.document_type in (', _document_type, ') '), ''), 
						 IF((length(IFNULL(_document_number,''))>0), concat(' AND mi.document_number like ''%', _document_number, '%'''), ''),
						
                         IF((_moviment_transf_hab=0), concat(' AND IFNULL(bank_out.bank_account_id,0)=', 0), ''), 
                         
                         IF((_filter_data_type=1), concat(' AND mi.creation_date between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=2), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=3), concat(' AND mi.payment_expiry_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         
                         IF((_moviment_type_removed=1), concat(' AND IFNULL(mi.inactive,0)=', 1), ''),
                         IF((_moviment_type_removed=0), concat(' AND IFNULL(mi.inactive,0)=', 0), ''),
                         
                         ' GROUP BY mi.document_parent_id, mi.company_id) x ON x.document_parent_id=m.document_parent_id AND x.company_id=m.company_id '
                         'LEFT JOIN bank_account ba on m.bank_account_id=ba.id ',
                         'LEFT JOIN bank_account bao on m.bank_account_origin_id=bao.id ',
                         'LEFT JOIN company co on m.company_id=co.id ',
                         'LEFT JOIN provider pr on m.provider_id=pr.id ',
                         'LEFT JOIN financial_group fg on m.financial_group_id=fg.id ',
                         'LEFT JOIN financial_sub_group fsg on m.financial_sub_group_id=fsg.id ',
                         'WHERE 1=1 ',
                         
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=0), concat(' AND IFNULL(m.credit,0)=', 1), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=1), concat(' AND IFNULL(m.credit,0)=', 0), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=0), '', ''),
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=1), '', ''),
                         
						 IF((_moviment_type_close=1 AND _moviment_type_open=0), concat(' AND IFNULL(m.document_status,1)=', 2), ''),
                         IF((_moviment_type_close=0 AND _moviment_type_open=1), concat(' AND IFNULL(m.document_status,1)=', 1), ''),
                         IF((_moviment_type_close=0 AND _moviment_type_open=0), '', ''),
                         IF((_moviment_type_close=1 AND _moviment_type_open=1), '', ''),
                         
                         IF((IFNULL(_moviment_transf,0) = 1 AND IFNULL(_moviment_type_launch,0) = 1), ' AND m.document_type in (1, 4, 2, 3) ', 
                         IF((IFNULL(_moviment_transf,0) = 1 AND IFNULL(_moviment_type_launch,0) = 0), ' AND m.document_type in (1, 4, 3) ', 
                         IF((IFNULL(_moviment_transf,0) = 0 AND IFNULL(_moviment_type_launch,0) = 1), ' AND m.document_type in (1, 4, 2) ', 
                         IF((IFNULL(_moviment_transf,0) = 0 AND IFNULL(_moviment_type_launch,0) AND IFNULL(_company_id,0) > 0), '', ' AND m.document_type in (1, 4) ')))), 
                         
						 IF((LENGTH(IFNULL(_financial_group_id,''))>0), concat(' AND m.financial_group_id in (', _financial_group_id, ')'), ''), 
                         IF((LENGTH(IFNULL(_financial_sub_group_id,''))>0), concat(' AND m.financial_sub_group_id in (', _financial_sub_group_id, ')'), ''), 
						 IF((LENGTH(IFNULL(_financial_cost_center_id,''))>0), concat(' AND m.financial_cost_center_id=', _financial_cost_center_id), ''), 
						 IF((IFNULL(_product_group_id,0)>0 and IFNULL(_product_sub_group_id,0)=0), concat(' AND m.product_id in (select id from product WHERE product_group_id=',_product_group_id,' group by id) '), ''), 
						 IF((IFNULL(_product_group_id,0)>0 and IFNULL(_product_sub_group_id,0)>0), concat(' AND m.product_id in (select id from product WHERE product_group_id=',_product_group_id,' AND product_sub_group_id=',_product_sub_group_id,')'), ''), 
                         
						 IF((_moviment_type_removed=1), concat(' AND IFNULL(m.inactive,0)=', 1), ''),
                         IF((_moviment_type_removed=0), concat(' AND IFNULL(m.inactive,0)=', 0), ''),
                         
                         ' ORDER BY m.document_parent_id, m.credit desc, m.document_id;');
              
             -- select @t1;
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3; 
		DEALLOCATE PREPARE stmt3;			 
    END IF;
END$$

/**
	3 - get operation
    4 - get all operation
*/

-- CALL pr_document_movement_report(5, 0, 0, null, null, 0, '', '', 1, 1, 0, 0, 1, 0, 0, 0, '2023-05-01 00:00:00', '2023-05-07 23:59:59', 3, null, null, null, 8, 0, 0, '8', null, null, 0, 0, 0, 0  )

/**
CALL pr_document_movement_report(
5, -- 01 IN _operation INT,
0, -- 02 IN _company_id BIGINT,
0, -- 03 IN _provider_id BIGINT,
null, -- 04 IN _financial_group_id VARCHAR(7),
null, -- 05 IN _financial_sub_group_id VARCHAR(5000),
0, -- 06 IN _bank_account_id INT,
'', -- 07 IN _document_type VARCHAR(50),
'', -- 08 IN _document_number VARCHAR(20), 											   
0, -- 09 IN _moviment_type_credit BIT, 
0, -- 10 IN _moviment_type_debit BIT, 
0, -- 11 IN _moviment_type_financial BIT, 
0, -- 12 IN _moviment_type_open BIT, 
0, -- 13 IN _moviment_type_close BIT, 
0, -- 14 IN _moviment_type_removed BIT,
1, -- 15 IN _moviment_transf BIT,
0, -- 16 IN _moviment_transf_hab BIT,
'2023-01-01 00:00:00', -- 17 IN _date_from DATETIME,
'2023-03-27 08:38:59', -- 18 IN _date_to DATETIME,
3, -- 19 IN _filter_data_type INT,
null, -- 20 IN _id BIGINT,
null, -- 21 IN _document_id BIGINT,
null, -- 22 IN _document_parent_id BIGINT, 
8, -- 23 IN _user_operation  varchar(200),
0, -- 24 IN _product_group_id INT,
0, -- 25 IN _product_sub_group_id INT,
'8', -- 26 IN _user_childrens_parent VARCHAR(5000),
null, -- 27 IN _moviment_type_closing BIT,
null, -- 28 IN _financial_cost_center_id INT,
1, -- 29 IN _moviment_type_launch BIT,
0, -- 30 IN _moviment_type_input_movement BIT,
0, -- 31 IN _moviment_type_output_movement BIT,
0  -- 32 IN _moviment_type_duplicate_movement BIT
);
*/

CALL pr_document_movement_report(4, 0, 0, null, null, 0, '', '', 1, 1, 0, 0, 1, 0, 0, 0, '2024-08-26 00:00:00', '2024-09-01 23:59:59', null, 1, null, null, 7, 2, 0, '7', null, null, 0, 0, 0, 0  )

-- select * from document_movement where document_id = 1500732