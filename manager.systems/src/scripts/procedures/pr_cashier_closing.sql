USE `manager`;
DROP procedure IF EXISTS `pr_cashier_closing`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing`(IN _operation INT,
                                      IN _document_type VARCHAR(50),
									  IN _credit BIT,
									  IN _company_id BIGINT,
                                      IN _bank_account_id INT,
									  IN _document_number VARCHAR(20), 
									  IN _document_value DECIMAL(19,4), 
                                      IN _document_note VARCHAR(1000), 
                                      IN _document_status INT, 
                                      IN _payment_value DECIMAL(19,4), 
									  IN _payment_data DATETIME, 
									  IN _payment_expiry_data DATETIME, 
                                      IN _payment_user BIGINT,
                                      IN _payment_status INT, 
                                      IN _financial_group_id VARCHAR(7),
								      IN _financial_sub_group_id VARCHAR(7),
								      IN _inactive BIT,
                                      IN _user_change BIGINT,
                                      IN _document_parent_id BIGINT,
                                      IN _document_transfer_id BIGINT,
                                      IN _bank_account_origin_id INT,
									  IN _provider_id BIGINT,
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
		                              IN _user_operation BIGINT,
		                              IN _user_childrens_parent VARCHAR(5000),
		                              IN _moviment_type_closing BIT,
                                      IN _financial_cost_center_id INT,
                                      IN _document_id BIGINT,
                                      IN _residue INT,
                                      IN _companysId VARCHAR(5000))
BEGIN	
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _transfer_document_number BIGINT;
    DECLARE _message VARCHAR(2000);
    DECLARE _document_number_result BIGINT;
    DECLARE _bank_account_origin_description VARCHAR(2000);
    DECLARE _bank_account_description VARCHAR(2000);
	DECLARE _person_id BIGINT;
    
	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
  
	IF(1=_operation) THEN /** Tranfer total closing destiny*/
			BEGIN
                
                IF(IFNULL(_document_id,0) = 0) THEN
					INSERT INTO document_generate_id(inactive) select 0;
					SET _document_id = LAST_INSERT_ID();    
                END IF;
                
                IF(IFNULL(_document_parent_id,0) = 0) THEN
					INSERT INTO document_parent_generate_id(inactive) select 0;
					SET _document_parent_id = LAST_INSERT_ID(); 
                END IF;
                
                IF(IFNULL(_bank_account_id,0) = 0) THEN 
					SELECT pe.bank_account_id INTO _bank_account_id FROM company co INNER JOIN person pe ON co.person_id = pe.id  WHERE co.id = _company_id LIMIT 1;
                END IF;
	
				INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
                document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
                payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
                inactive, user_creation, creation_date, user_change, change_date)
                select 1, _document_id, _document_parent_id, _document_transfer_id,  _document_type, _credit, _company_id, 0 provider_id, _bank_account_id, _bank_account_origin_id, 
                _document_number, _payment_value, _document_note, _document_status, _payment_value, 0 payment_discount, 
                0 payment_extra, 0 payment_residue, _payment_data, _payment_expiry_data, _payment_user, 0 nfe_number, 
                2 payment_status, 0 generate_billet, _financial_group_id, _financial_sub_group_id,
                _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;

                IF(2=_document_status) THEN
					BEGIN
						/**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) as movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);
                                       
					   /**Insert bank account statement*/
					   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
					   SELECT 
                       (SELECT person_id FROM company WHERE id=_company_id) person_id,
                       _bank_account_id, _bank_account_origin_id, _document_type, _document_parent_id, _document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1), IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) AS movement_balance,
					   _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_data;
                   END; 
				END IF;  
				SELECT _document_parent_id AS document_parent_id;
            END;
		ELSEIF(3=_operation) THEN
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
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT(
        
        'SELECT m.id, m.document_id, m.document_parent_id, m.document_type, m.credit, m.company_id, m.provider_id, pr.social_name provider_description, m.bank_account_id, m.bank_account_origin_id, bao.description bank_account_origin_description, ', 
						 'm.financial_group_id, m.financial_sub_group_id, IFNULL(m.document_transfer_id,0) as document_transfer_id, IFNULL(m.document_number,''-//-'') as document_number, (case when (m.credit = 1 and m.document_value < 0) then (m.document_value * -1) else m.document_value end) document_value, x.document_total, ',
                         'm.document_note, m.document_status, (case when (m.credit = 1 and m.payment_value < 0) then (m.payment_value * -1) else m.payment_value end) payment_value, m.payment_discount, m.payment_extra, ',
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
                         '0 payment_residue '
						 'FROM document_movement m ',
                         'INNER JOIN (select company_id from user_company where user_id in (',_user_childrens_parent,') group by company_id) uc ON uc.company_id=m.company_id ',
                         'INNER JOIN (SELECT document_parent_id, company_id, sum(case when (credit=1 and document_value < 0) then (document_value * -1) when (credit=1 and document_value >= 0) then document_value else (document_value*-1) end) document_total ',        
                         'FROM document_movement mi ', 
                         'left join bank_account_out_document_parent bank_out on mi.bank_account_id=bank_out.bank_account_id and bank_out.inactive=0 ',
						 ' WHERE 1=1 ', 
                         IF((_companysId>0), concat(' AND mi.company_id in (',_companysId,')'), ''), 
                         IF((length(IFNULL(_document_type,''))>0), concat(' AND mi.document_type in (', _document_type, ') '), ''), 
                         IF((length(IFNULL(_document_number,''))>0), concat(' AND mi.document_number like ''%', _document_number, '%'''), ''),
						 IF((_moviment_transf=0), concat(' AND IFNULL(mi.group_transfer_id,0)>', 0), ''),    
                         IF((_moviment_transf_hab=0), concat(' AND IFNULL(bank_out.bank_account_id,0)=', 0), ''),                                                      
						 IF((_moviment_type_closing=1 and _moviment_type_open=1), concat(' AND IFNULL(mi.document_transfer_id,0)>=', 0), ''),  
                         IF((_moviment_type_closing=1 and _moviment_type_open=0), concat(' AND IFNULL(mi.document_transfer_id,0)>=', 1), ''), 
                         IF((_moviment_type_closing=0 and _moviment_type_open=1), concat(' AND IFNULL(mi.document_transfer_id,0)=', 0), ''), 
                         IF((_filter_data_type=1), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=2), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         IF((_filter_data_type=3), concat(' AND mi.payment_data between ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '), ''),
                         ' AND mi.document_status = 2 ',
                         'GROUP BY mi.document_parent_id, mi.company_id) x ON x.document_parent_id=m.document_parent_id AND x.company_id=m.company_id '
                         'LEFT JOIN bank_account ba on m.bank_account_id=ba.id ',
                         'LEFT JOIN bank_account bao on m.bank_account_origin_id=bao.id ',
                         'LEFT JOIN company co on m.company_id=co.id ',
                         'LEFT JOIN provider pr on m.provider_id=pr.id ',
                         'LEFT JOIN financial_group fg on m.financial_group_id=fg.id ',
                         'LEFT JOIN financial_sub_group fsg on m.financial_sub_group_id=fsg.id ',
                         'WHERE 1=1  ',
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=0), concat(' AND IFNULL(x.document_total,0)>=', 0), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=1), concat(' AND IFNULL(x.document_total,0)<', 0), ''),  
                         IF((_moviment_type_removed=1), concat(' AND IFNULL(m.inactive,0)=', 1), ''),
                         IF((_moviment_type_removed=0), concat(' AND IFNULL(m.inactive,0)=', 0), ''),
                         ' AND m.document_status = 2 ',
                         ' UNION ALL ',
						 'SELECT ',
						 'residue.id, residue.document_id, residue.document_parent_id, residue.document_type, residue.credit, residue.company_id, ',
						 'residue.provider_id, pr_residue.social_name provider_description, residue.bank_account_id, residue.bank_account_origin_id, ',
						 'bao_residue.description bank_account_origin_description, ',
						 'residue.financial_group_id, residue.financial_sub_group_id, residue.document_transfer_id, ',
						 'IFNULL(residue.document_number,''-//-'') as document_number, (case when residue.credit = 1 then (residue.document_value * -1) else residue.document_value end) document_value, 0 document_total, ',
						 'residue.document_note, residue.document_status, (case when residue.credit = 1 then (residue.payment_value * -1) else residue.payment_value end) payment_value, residue.payment_discount, residue.payment_extra, ',
						 'DATE_FORMAT(residue.payment_data,''%d/%m/%Y'') as payment_data, DATE_FORMAT(residue.payment_expiry_data,''%d/%m/%Y'') as payment_expiry_data, ',
						 'residue.payment_user, residue.inactive, residue.user_creation, DATE_FORMAT(residue.creation_date,''%d/%m/%Y %H:%i'') as creation_date, residue.user_change, ',
						 'DATE_FORMAT(residue.change_date,''%d/%m/%Y %H:%i'') as change_date, ',
						 'IFNULL(ba_residue.description,''-//-'') as bank_account_description, ',
						 'IFNULL(co_residue.social_name,''-//-'') as company_description, ',
						 'IFNULL(pr_residue.social_name,''-//-'') as provider_description, ',
						 'IFNULL(fg_residue.description,''-//-'') as description_financial_group, ',
						 'IFNULL(fsg_residue.description,''-//-'') as description_financial_sub_group, ',
						 'IFNULL(residue.product_id, 0) as product_id, ',
						 'IFNULL(residue.product_description,''-//-'') as product_description, ',
						 'IFNULL(residue.is_product_movement,0) as is_product_movement, ',
                         '1 payment_residue '
						 'FROM document_movement residue ', 
						 'LEFT JOIN bank_account ba_residue on residue.bank_account_id=ba_residue.id ',
						 'LEFT JOIN bank_account bao_residue on residue.bank_account_origin_id=bao_residue.id ',
						 'LEFT JOIN company co_residue on residue.company_id=co_residue.id ',
						 'LEFT JOIN provider pr_residue on residue.provider_id=pr_residue.id ',
						 'LEFT JOIN financial_group fg_residue on residue.financial_group_id=fg_residue.id ',
						 'LEFT JOIN financial_sub_group fsg_residue on residue.financial_sub_group_id=fsg_residue.id ',
						 'WHERE 1=1 ',
                         IF((IFNULL(_companysId,0)>0), concat('AND residue.company_id in (',_companysId,')'), ''),
						 ' AND residue.payment_residue = 1 AND residue.document_status = 1 AND residue.document_transfer_id > 0 AND residue.inactive = 0 ',
						 'ORDER BY document_parent_id, credit DESC, document_id;');
                         
                    -- select @t1;
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3; 
		DEALLOCATE PREPARE stmt3;	
	ELSEIF(27=_operation) THEN /**Closing transfer origin */
    
			IF(IFNULL(_bank_account_id,0) = 0) THEN 
				SELECT pe.bank_account_id INTO _bank_account_id FROM company co INNER JOIN person pe ON co.person_id = pe.id  WHERE co.id = _company_id LIMIT 1;
			END IF;
                
			INSERT INTO document_generate_id(inactive) select 0;
			SET _document_id = LAST_INSERT_ID();  
			INSERT INTO document_parent_generate_id(inactive) select 0;
			SET _document_parent_id = LAST_INSERT_ID();

			-- SET _message = CONCAT('RESIDUAL TRANSF. FEC. CAIXA:', CAST(_document_parent_id AS CHAR),' >> EMPRESA: ',CAST(_company_id AS CHAR));

			INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id,
			document_transfer_id, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
			payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
			inactive, user_creation, creation_date, user_change, change_date)
			select 1, _document_id, _document_parent_id, _document_transfer_id,  3 document_type, (case when (_document_value - _payment_value > 0) then 0 else 1 end)  credit, _company_id, 0 provider_id, _bank_account_id, _bank_account_id,
			_document_transfer_id, _document_number, (_document_value-_payment_value), _document_note, _document_status, (_document_value-_payment_value), 0 payment_discount, 
			0 payment_extra, 1 payment_residue, _payment_data, _payment_expiry_data, _payment_user, 0 nfe_number, 
			1 payment_status, 0 generate_billet, _financial_group_id, _financial_sub_group_id,
			_inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
	ELSEIF(29=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Número de documento informado já existe.';
            END;
		ELSE 
			BEGIN
                INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();         
				
                /**Company_id is null */
				IF(IFNULL(_company_id,0)=0) THEN
					SELECT c.id INTO _company_id FROM person p
					INNER JOIN company c ON p.id = c.person_id
					WHERE p.bank_account_id = _bank_account_id LIMIT 1;
				END IF;
                
				INSERT INTO document_movement(is_cashing_close, document_id, document_parent_id, document_parent_id_origin, document_transfer_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, 
                document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
                payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
                inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id)
                select 1, _document_id, _document_parent_id, _document_transfer_id, _document_transfer_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, _bank_account_origin_id,
                _document_number, _document_value, _document_note, _document_status, _document_value, 0.0 payment_discount, 
                 0.0 payment_extra, 0 payment_residue, _payment_data, _payment_expiry_data, _payment_user, 0 nfe_number, 
                _payment_status, 0 generate_billet, _financial_group_id, _financial_sub_group_id,
                _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _financial_cost_center_id;
                
                IF(2=_document_status) THEN
					BEGIN
						/**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) as movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance);
                                       
					   /**Insert bank account statement*/
					   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, launch_type, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
					   SELECT 
                       (SELECT person_id FROM company WHERE id=_company_id) person_id,
                       _bank_account_id, _bank_account_id, _document_type, _document_parent_id, _document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1), IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) AS movement_balance,
					   _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_data;
                   END; 
				END IF; 
            END;
		END IF;
	ELSEIF(30=_operation) THEN /**Closing transfer transfer success */
			IF(IFNULL(_bank_account_id,0) = 0) THEN 
				SELECT pe.id, pe.bank_account_id INTO _person_id, _bank_account_id FROM company co INNER JOIN person pe ON co.person_id = pe.id  WHERE co.id = _company_id LIMIT 1;
			END IF;
            
			SELECT 
				IFNULL((SELECT SUM(CASE WHEN credit = 1 THEN document_value ELSE (document_value * -1) END) FROM document_movement WHERE document_parent_id = _document_parent_id AND company_id = _company_id AND document_status = 1),0) 
            INTO _payment_value; 
			
			IF(_payment_value <> 0) THEN
				BEGIN
					/**Update bank_balance_available from document value */
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT _payment_value AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id = x.bank_account_id
					SET ba.bank_balance_available=(ba.bank_balance_available + x.movement_balance);
										   
				   /**Insert bank account statement*/
				   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, document_parent_id, document_id, bank_balance_available, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
				   SELECT 
				   _person_id, _bank_account_id, _bank_account_id, _document_parent_id, document_id, (SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id = _bank_account_id LIMIT 1), IFNULL(CASE WHEN credit = 1 THEN document_value ELSE (document_value * -1) END,0) AS movement_balance,
				   document_note, 0, _user_operation, _processing_date_time, _user_change, payment_data
                   FROM document_movement WHERE document_parent_id = _document_parent_id AND company_id = _company_id AND document_status = 1;
				END; 
			END IF;
                   
			UPDATE document_movement SET document_status = 2, payment_status = 2, document_transfer_id = _document_transfer_id, is_document_transfer = 1, cashier_closing_date = _processing_date_time, user_change = _user_change, change_date = _processing_date_time,
			payment_data = (case when (document_status = 1 and payment_residue = 1) then _processing_date_time else payment_data end)
			WHERE 
			document_parent_id = _document_parent_id 
			AND company_id = _company_id; 
    END IF;
END$$

 /*   2 - inactive operation
    3 - get operation
    4 - get all operation
    8 - ungroup operation
    9 - changeStatus operation
*/