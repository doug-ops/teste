USE `manager`;
DROP procedure IF EXISTS `pr_person_report`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_person_report` 
(
	IN _operation INT,
	IN _person_id_from BIGINT,
    IN _person_id_to BIGINT,
	IN _inactive INT,
    IN _description VARCHAR(100),
    IN _object_type VARCHAR(3),
    IN _person_type VARCHAR(2), 
    IN _person_type_id INT,
    IN _establishment_type_id INT,
    IN _users VARCHAR(1000),
    IN _client_id INT
)
BEGIN
    
    DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
	
    SELECT IFNULL(timezone_database, 0)	INTO _timezone_database FROM config_systems	LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
         
	IF(3=_operation) THEN
        BEGIN
			SET @t1 = CONCAT('SELECT p.id as person_id,  p.person_type, p.object_type, p.access_profile_id, p.cpf_cnpj, p.ie, ',
			'p.address_zip_code, p.address_zip_code_side, p.address_street_number, p.address_street_complement,  p.email, p.site, ',
			'IFNULL(p.register_type,0) AS register_type, p.inactive, p.user_creation, p.creation_date, IFNULL(p.bank_account_id,0) as bank_account_id, p.main_company_id, (SELECT description FROM bank_account WHERE id=p.bank_account_id LIMIT 1) AS bank_account_description,  ',
            'IFNULL(p.credit_provider_id,0) as credit_provider_id, (SELECT social_name FROM provider WHERE id=IFNULL(p.credit_provider_id,0) LIMIT 1) as credit_provider_description, ',
            'IFNULL(p.debit_provider_id,0) as debit_provider_id, (SELECT social_name FROM provider WHERE id=IFNULL(p.debit_provider_id,0) LIMIT 1) as debit_provider_description, ',
			'p.user_change, IFNULL((select alias_name from user where person_id=p.user_change LIMIT 1),''SISTEMA'') as user_change_name, p.change_date, a.address_street, a.address_district, a.address_city_ibge, a.address_state_ibge, a.address_country_ibge,',
			'a.address_gia, cy.description as description_city, st.description as description_state, ct.description as description_country, ', 
			IF(('COM'=_object_type), 'c.id, c.social_name as name, c.fantasy_name as alias_name, c.negative_close, c.process_movement_automatic, NULL as rg, NULL as access_data_user, NULL as access_data_password,  NULL as expiration_date, NULL as last_access_date, 0 as financial_group_id, '''' as description_financial_group, 0 as financial_sub_group_id, '''' as description_financial_sub_group, p.person_type_id, c.establishment_type_id, p.blocked ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name, u.alias_name, 0 as negative_close, u.rg, u.access_data_user, u.access_data_password,  u.expiration_date, u.last_access_date, 0 as financial_group_id, '''' as description_financial_group, 0 as financial_sub_group_id, 0 as process_movement_automatic, '''' as description_financial_sub_group, p.person_type_id, 0 as establishment_type_id, 0 as blocked ',' '),
            IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.fantasy_name as alias_name, 0 as negative_close, 0 as process_movement_automatic, NULL as rg, NULL as access_data_user, NULL as access_data_password, NULL as expiration_date, NULL as last_access_date, IFNULL(f.financial_group_id,0) as financial_group_id, IFNULL((SELECT description FROM financial_group WHERE id=IFNULL(f.financial_group_id,0) LIMIT 1),'''') as description_financial_group, 0 as person_type_id, 0 as establishment_type_id, 0 as blocked, IFNULL(financial_sub_group_id,0) as financial_sub_group_id,  IFNULL((SELECT description FROM financial_sub_group WHERE id=IFNULL(f.financial_sub_group_id,0) LIMIT 1),'''') as description_financial_sub_group ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), 'INNER JOIN company c ON p.id=c.person_id',' '), 
            -- IF(('COM'=_object_type AND _user_change>0), concat(' INNER JOIN user_company uc ON uc.company_id=c.id and uc.user_id=',_user_change),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
            IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id',' '), 
			' LEFT JOIN address a ON p.address_zip_code=a.address_zip_code and p.address_zip_code_side=a.address_zip_code_side ',
			' LEFT JOIN address_city cy ON a.address_city_ibge=cy.address_city_ibge ',
			' LEFT JOIN address_state st ON a.address_state_ibge=st.address_state_ibge ',
			' LEFT JOIN address_country ct ON a.address_country_ibge=ct.address_country_ibge ',
			' WHERE 1=1 ',
   			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),' '),
   			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),' '),        
   			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),' '));                          
            -- select @t1;
            PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /** End operation 3 */
	ELSEIF(4=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT p.id as person_id, p.object_type,  p.cpf_cnpj, p.email, p.bank_account_id, p.inactive, p.user_change, p.change_date, p.person_type, ',
            IF(('COM'=_object_type), 'c.id, c.social_name as name, c.fantasy_name as alias_name, '' as access_data_user, '' as access_data_password,  NULL as expiration_date, NULL as last_access_date, NULL AS exit_access_date, IFNULL(p.person_type_id,0) AS person_type_id, IFNULL(pt.description,0) AS person_type_description, IFNULL(c.establishment_type_id,0) AS establishment_type_id, IFNULL(et.description,0) AS establishment_type_description, IFNULL((select count(*) qtde from product p where p.company_id = c.id and p.inactive = 0),0) AS count_products, c.client_id, 0 count_company, (select REPLACE((SELECT group_concat(DISTINCT name) as data from user where id in (select user_id from user_company where company_id = c.id and inactive = 0 group by user_id)), '','', '', '')) attached_users, IFNULL(c.financial_cost_center_id,0) financial_cost_center_id  ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name, u.alias_name, u.access_data_user, u.access_data_password,  u.expiration_date, u.last_access_date, u.exit_access_date, 0 AS person_type_id, NULL AS person_type_description, 0 AS establishment_type_id, NULL AS establishment_type_description, 0 AS count_products, u.client_id, 0 count_company, '''' attached_users, 0 financial_cost_center_id  ',' '),
			IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.fantasy_name as alias_name, '' as access_data_user, '' as access_data_password,  NULL as expiration_date, NULL as last_access_date, NULL as exit_access_date, 0 AS person_type_id, NULL AS person_type_description, 0 AS establishment_type_id, NULL AS establishment_type_description, 0 AS count_products, 0 client_id, 0 count_company, '''' attached_users, 0 financial_cost_center_id ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), CONCAT('INNER JOIN company c ON p.id=c.person_id AND c.client_id=', _client_id),' '),   
            IF(('COM'=_object_type AND (length(rtrim(ltrim(IFNULL(_users,''))))>0)>0), concat(' INNER JOIN (select company_id from user_company where user_id in (',_users,') group by company_id) uc ON uc.company_id=c.id '),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
			IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id',' '),             
			' LEFT JOIN address a ON p.address_zip_code=a.address_zip_code and p.address_zip_code_side=a.address_zip_code_side ',
			' LEFT JOIN address_city cy ON a.address_city_ibge=cy.address_city_ibge ',
			' LEFT JOIN address_state st ON a.address_state_ibge=st.address_state_ibge ',
			' LEFT JOIN address_country ct ON a.address_country_ibge=ct.address_country_ibge ',
            IF(('COM'=_object_type), 'LEFT JOIN person_type pt ON p.person_type_id = pt.id AND pt.inactive = 0 AND pt.object_type =  ''COM'' ',' '), 
            IF(('COM'=_object_type), 'LEFT JOIN establishment_type et ON c.establishment_type_id = et.id AND pt.inactive = 0 ',' '), 
			' WHERE 1=1 ',
			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_description,''))))>0)), concat(' AND u.name like ''%', rtrim(ltrim(_description)), '%'' '), ''),
            -- IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_exit_access_date,''))))>0)), concat(' AND u.exit_access_date ', rtrim(ltrim(_exit_access_date)),' '), ''),
            IF((('USU'=_object_type) AND _client_id>0), concat(' AND u.client_id=', _client_id),''),
			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('COM'=_object_type) AND (length(rtrim(ltrim(IFNULL(_description,''))))>0)), concat(' AND c.social_name like ''%', rtrim(ltrim(_description)), '%'' '), ''),
			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('FOR'=_object_type) AND (length(rtrim(ltrim(IFNULL(_description,''))))>0)), concat(' AND f.social_name like ''%', rtrim(ltrim(_description)), '%'' '), ''),
            IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive= ', _inactive), ''),
            IF((('FOR'=_object_type) AND (_person_type IS NOT NULL and _person_type <>'T')), concat(' AND p.person_type= ''', _person_type, ''' '), ''),
            IF((('COM'=_object_type) AND (IFNULL(_person_type_id,0)>0 and IFNULL(_establishment_type_id,0)=0)), concat(' AND p.person_type_id= ''', _person_type_id, ''' '), ''),
            IF((('COM'=_object_type) AND (IFNULL(_person_type_id,0)>0 and IFNULL(_establishment_type_id,0)>0)), concat(' AND p.person_type_id=', _person_type_id,' AND c.establishment_type_id= ''', _establishment_type_id, ''' '), ''),';');
			
			-- select @t1;
			
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END;
	END IF; /**End if operation */ 
END$$
/**
    3 - get operation
    4 - get all operation
    
    	IN _operation INT,
	IN _person_id_from BIGINT,
    IN _person_id_to BIGINT,
	IN _inactive INT,
    IN _description VARCHAR(100),
    IN _object_type VARCHAR(3),
    IN _person_type VARCHAR(2), 
    IN _person_type_id INT,
    IN _establishment_type_id INT,
    IN _users VARCHAR(1000),
    IN _client_id INT
*/

-- CALL pr_person_report(4, 0, 0, 0, '', 'COM', 'F', 1, 0, '8,14,37,16,44,10,35,19,36,73,40,11,26,22,38,47,21,23,13,20,34,6,1,15,45,12,25,39,9,2,24,50,18,17,48,3', 1  )
CALL pr_person_report(4, 0, 0, 0, '', 'COM', 'F', 1, 0, '8,14,37,16,44,10,35,19,36,73,40,11,26,76,22,38,47,13,20,34,6,1,15,45,12,25,39,9,2,24,50,18,17,48,3', 1  )
