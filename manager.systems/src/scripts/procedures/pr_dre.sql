USE `manager`;
DROP procedure IF EXISTS `pr_dre`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_dre`(IN _operation INT,
						  IN _company_id BIGINT,
                          IN _financial_group_id VARCHAR(7),
						  IN _financial_sub_group_id VARCHAR(7),
						  IN _bank_account_id INT,											   
                          IN _type_document_value INT, 
                          IN _date_from DATETIME,
                          IN _date_to DATETIME,
						  IN _filter_data_type INT,
                          IN _user_id BIGINT)
BEGIN	
	IF(4=_operation) THEN
		BEGIN
			DROP TEMPORARY TABLE IF EXISTS tmp_report_dre;
            CREATE TEMPORARY TABLE IF NOT EXISTS tmp_report_dre
			(
				group_type_id INT NOT NULL,
                -- group_type_description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
                financial_group_id VARCHAR(7) CHARACTER SET utf8 NOT NULL,
                -- financial_group VARCHAR(100) CHARACTER SET utf8 NOT NULL,
                financial_sub_group_id VARCHAR(7) CHARACTER SET utf8 NOT NULL,
                -- financial_sub_group VARCHAR(100) CHARACTER SET utf8 NOT NULL,
               document_id BIGINT NOT NULL,
               document_parent_id BIGINT NOT NULL,
               document_note VARCHAR(2042) CHARACTER SET utf8 NOT NULL,
               credit bit NOT NULL,
               document_value decimal(14,2) NOT NULL,
               payment_value decimal(14,2) NOT NULL,
               document_date date NOT NULL, 
               payment_date date NOT NULL, 
               order_type TINYINT NOT NULL 
               -- order_position int NOT NULL
			);
            
            insert into tmp_report_dre(group_type_id, financial_group_id, financial_sub_group_id, document_id, 
									   document_parent_id, document_note, credit, document_value, payment_value, 
                                       document_date, payment_date, order_type)
            SELECT 
				10001 group_type_id, -- 10001 - Receita Bruta                
                mov.financial_group_id,
                mov.financial_sub_group_id,
                mov.document_id, 
                mov.document_parent_id,
				mov.document_note,  
                mov.credit, 
                mov.document_value, 
                mov.payment_value,
				mov.creation_date,
                mov.payment_data, 
                1 order_type
            FROM 
				document_movement mov
            INNER JOIN 
				user_company uc ON mov.company_id = uc.company_id 
            WHERE 
				mov.payment_data BETWEEN _date_from AND _date_to
				AND uc.user_id=_user_id
                AND mov.company_id = (case when iFNULL(_company_id,0) > 0 then _company_id else mov.company_id end)
                AND mov.financial_group_id='24.000';
                
            SELECT 
				dre.group_type_id, 
				(CASE WHEN dre.group_type_id = 10001 THEN 'Receita Bruta' ELSE 'Outros' END) group_type_description,
				dre.financial_group_id,
				(CASE WHEN fg.description IS NULL THEN 'Outros' ELSE fg.description END) AS financial_group,
				dre.financial_sub_group_id,
				(CASE WHEN fsg.description IS NULL THEN 'Outros' ELSE fsg.description END) AS financial_sub_group,
				dre.document_id, 
				dre.document_parent_id,
				dre.document_note,  
				dre.credit, 
				dre.document_value, 
				dre.payment_value,
				dre.document_date,
				dre.payment_date, 
				dre.order_type
            FROM 
				tmp_report_dre dre
            LEFT JOIN financial_group fg on dre.financial_group_id = fg.id 
            LEFT JOIN financial_sub_group fsg on dre.financial_sub_group_id = fsg.id
            
            ;
			/**
			SET @t1 = CONCAT('SELECT (CASE WHEN fgt.id IS NULL THEN 6 ELSE fgt.id END) AS group_type_id, (CASE WHEN fgt.description IS NULL THEN ''Outros'' ELSE fgt.description END) AS description_financial_group_type, ',
				'(CASE WHEN fgt.description IS NULL THEN ''Outros'' ELSE fgt.description END) AS description_financial_group, ',
				'(CASE WHEN fsg.description IS NULL THEN ''Outros'' ELSE fsg.description END) AS description_financial_sub_group,  m.*, ', 
				'(CASE WHEN fgt.id = 1 THEN 1 WHEN fgt.id = 2 THEN 2 WHEN fgt.id = 3 THEN 3 WHEN fgt.id = 4 THEN 4 WHEN fgt.id = 5 THEN 5 WHEN fgt.id IS NULL THEN 6 END) AS order_type, (CASE WHEN ', IFNULL(_type_document_value,0),' = 1 THEN m.document_value ELSE m.payment_value END) as total  ',
                'FROM document_movement m ',
				'INNER JOIN user_company uc ON m.company_id = uc.company_id ',
                'LEFT JOIN financial_group fg on m.financial_group_id=fg.id ', 
				'LEFT JOIN financial_sub_group fsg on m.financial_sub_group_id=fsg.id ', 
				'LEFT JOIN financial_group_type fgt on fsg.financial_group_type_id=fgt.id ',
				' WHERE ',
				'm.payment_data ', 
				'BETWEEN ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' ', 
				'AND uc.user_id=',_user_id, ' ',
				'AND m.is_product_movement=1 ', 
				'AND m.document_status=2 ',
                IF((_company_id>0), concat(' AND m.company_id=', _company_id), ''), 
                IF((_bank_account_id>0), concat(' AND m.bank_account_id=', _bank_account_id), ''), 
				-- IF((length(IFNULL(_document_type,''))>0), concat(' AND m.document_type in (', _document_type, ') '), ''), 
                IF((_filter_data_type=1), concat(' AND m.creation_date between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                IF((_filter_data_type=2), concat(' AND m.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                IF((_filter_data_type=3), concat(' AND m.payment_expiry_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
				IF((IFNULL(_financial_group_id,0)>0), concat(' AND m.financial_group_id=', _financial_group_id), ''), 
                IF((IFNULL(_financial_sub_group_id,0)>0), concat(' AND m.financial_sub_group_id=', _financial_sub_group_id), ''),
                ' ORDER BY fgt.id;');
                -- Colocar outros filtros a partir daqui 
                
                -- no final tem um group by por grupo financeiro e order by por ordem de grupo e sub grupo

			-- select @t1;
                
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3; 
		DEALLOCATE PREPARE stmt3;	
        */
        END;
    END IF;/*end operation 4*/
END$$
 -- CALL pr_dre(4, null, null, null, null, null, '2022-08-05 00:00:00', '2022-09-18 23:59:59', null, 8 );
--  CALL pr_dre(4, null, null, null, null, null, '2022-10-10 00:00:00', '2022-10-10 23:59:59', 2, 8);
 -- CALL pr_log_system(4, null, null, null, 1 )
-- CALL pr_dre(4, 14, null, null, null, null, '2022-10-10 00:00:00', '2022-10-10 23:59:59', 2, 1 );
-- CALL pr_dre(4, 14, null, null, null, null, '2022-10-10 00:00:00', '2022-10-16 23:59:59', 2, 1 );