USE `manager_test_2`;
DROP procedure IF EXISTS `pr_maintenance_person`;

DELIMITER $$
USE `manager_test_2`$$
CREATE PROCEDURE `pr_maintenance_person` 
(
	IN _operation INT,
	IN _person_id_from BIGINT,
    IN _person_id_to BIGINT,
	IN _inactive INT,
    IN _name VARCHAR(100),
    IN _alias_name VARCHAR(100), 
    IN _object_type VARCHAR(3),
    IN _person_type VARCHAR(2), 
    IN _cpf_cnpj BIGINT, 
    IN _rg VARCHAR(20), 
    IN _ie VARCHAR(20),
    IN _email VARCHAR(100), 
    IN _address_zip_code BIGINT, 
	IN _address_zip_code_side VARCHAR(1),
    IN _address_street VARCHAR(100),
    IN _address_street_number VARCHAR(20),
	IN _address_street_complement VARCHAR(50),
	IN _address_district VARCHAR(100),
    IN _address_city_ibge INT,
	IN _address_state_ibge INT,
	IN _address_country_ibge INT,
    IN _address_gia VARCHAR(10),
	IN _access_profile_id INT, 
    IN _user_change BIGINT, 
    IN _change_date DATETIME, 
    IN _access_data_user VARCHAR(100), 
    IN _access_data_password VARCHAR(100), 
    IN _expiration_date DATETIME, 
    IN _last_access_date DATETIME,
    IN _negative_close BIT, 
    IN _bank_account_id BIGINT, 
    IN _credit_provider_id BIGINT, 
    IN _debit_provider_id BIGINT,
	IN _financial_group_id VARCHAR(7), 
    IN _financial_sub_group_id VARCHAR(7), 
    IN _register_type TINYINT, 
    IN _movement_automatic BIT,
    IN _exit_access_date DATETIME,
    IN _person_type_id INT,
	IN _establishment_type_id INT,
    IN _blocked TINYINT,
    IN _main_company_id BIGINT,
    IN _client_id INT,
	IN _financial_cost_center_id INT
)
BEGIN
    
	DECLARE _is_valid_address BIT;
	DECLARE _person_id BIGINT;
    DECLARE _message VARCHAR(100); 
    DECLARE _generated_id BIGINT;
    DECLARE v_bank_account_id INT DEFAULT 0;
    DECLARE v_c_provider_id BIGINT;
    DECLARE v_d_provider_id BIGINT;
    DECLARE v_c_person_id BIGINT;
	DECLARE v_d_person_id BIGINT;
    DECLARE v_object_type VARCHAR(3) DEFAULT 'FOR';
    
    DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
	
    SELECT IFNULL(timezone_database, 0)	INTO _timezone_database FROM config_systems	LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	IF ('USU'=_object_type) THEN
		BEGIN
            SET _person_id = IFNULL((select person_id from user where id=_person_id_from LIMIT 1),0);
		END; /**End if object type User */
	ELSEIF ('COM'=_object_type) THEN 
		BEGIN
            SET _person_id = IFNULL((select person_id from company where id=_person_id_from LIMIT 1),0);        
        END; /**End if object type Company */
	ELSEIF ('FOR'=_object_type) THEN 
		BEGIN
            SET _person_id = IFNULL((select person_id from provider where id=_person_id_from LIMIT 1),0);
        END; /**End if object type Provider */
	ELSE 
		BEGIN
				SET _message = CONCAT('Tipo de objeto: ',_object_type, ' inválido.');
				SIGNAL SQLSTATE '45000' 
				SET MESSAGE_TEXT = _message;
		END;
	END IF; /**End if object type */
    
    IF(IFNULL(_person_id,0)=0) THEN
		BEGIN
			SET _person_id = (SELECT (IFNULL(max(id),0)+1) from person);
        END; 
	END IF; /**End if get next person id */
    
     IF(IFNULL(_person_id,0)=0) THEN
		BEGIN
				SET _message = CONCAT('Codigo de Pessoa : ',IFNULL(_person_id,0), ' inválido.');
				SIGNAL SQLSTATE '45000' 
				SET MESSAGE_TEXT = _message;
		END; 
	END IF; /**End if _person_id is not defined */
         
    IF(1=_operation) THEN		
		BEGIN 
			set _is_valid_address = 0;
			IF(LENGTH(IFNULL(_address_zip_code,0)) BETWEEN 7 AND 8) THEN
				BEGIN
					IF NOT EXISTS (SELECT 1 FROM address WHERE address_zip_code=_address_zip_code AND address_zip_code_side=_address_zip_code_side LIMIT 1) THEN
						BEGIN
							IF(LENGTH(IFNULL(_address_street,''))>0 AND 
							   LENGTH(IFNULL(_address_district,''))>0 AND
							   IFNULL(_address_city_ibge,0)>0 AND
							   IFNULL(_address_state_ibge,0)>0 AND
							   IFNULL(_address_country_ibge,0)>0) THEN
								BEGIN
									INSERT INTO address(address_zip_code, address_zip_code_side, address_street, address_district, address_city_ibge, address_state_ibge, address_country_ibge, address_gia, inactive, user_creation, creation_date, user_change, change_date)
									select _address_zip_code, _address_zip_code_side, _address_street, _address_district, _address_city_ibge, _address_state_ibge, _address_country_ibge, _address_gia, 0 as _inactive, _user_change, _change_date, _user_change, _change_date;
								END;    
							END IF;
						END;
					END IF; /** End not exists Address */
				END;
			END IF; /** End if insert Address */
			IF (SELECT 1 = 1 FROM person where id=_person_id) THEN
				BEGIN
					UPDATE person SET 
						inactive=_inactive,
						object_type=_object_type,
						person_type=_person_type, 
						cpf_cnpj=_cpf_cnpj,
						ie=_ie,
						email=_email, 
                        bank_account_id=_bank_account_id,
                        credit_provider_id=_credit_provider_id,
                        debit_provider_id=_debit_provider_id,
						address_zip_code=_address_zip_code, 
						address_zip_code_side=_address_zip_code_side,
						address_street_number=_address_street_number,
						address_street_complement=_address_street_complement,
						access_profile_id=_access_profile_id, 
                        register_type=_register_type,
                        person_type_id=_person_type_id,
						user_change=_user_change, 
						change_date=_change_date,
                        blocked=_blocked,
                        main_company_id=_main_company_id                        
					WHERE
						id=_person_id;
				END; /** End if update Person */
			ELSE 
				BEGIN
					INSERT INTO person(id, inactive, object_type, person_type, cpf_cnpj, ie, email, 
									   bank_account_id, credit_provider_id, debit_provider_id, address_zip_code, 
									   address_zip_code_side, address_street_number, address_street_complement, 
                                       access_profile_id, register_type, person_type_id, user_creation, creation_date, user_change, change_date, blocked, main_company_id)
					select _person_id, _inactive, _object_type, _person_type, _cpf_cnpj, _ie, _email,  
						   _bank_account_id, _credit_provider_id, _debit_provider_id, _address_zip_code, 
						   _address_zip_code_side, _address_street_number, _address_street_complement, _access_profile_id, 
                           _register_type, _person_type_id, _user_change, _change_date, _user_change, _change_date, _blocked, _main_company_id;
				END;			
			END IF; /** End if operation 1 person */
            
			IF ('USU'=_object_type) THEN
				BEGIN
					IF (SELECT 1 = 1 FROM user where id=_person_id_from) THEN
						BEGIN
							UPDATE user SET 
                                person_id=_person_id,
								name=UPPER(_name), 
								alias_name=UPPER(_alias_name), 
                                rg=_rg,
								access_data_user=_access_data_user, 
								access_data_password=_access_data_password, 
								expiration_date=_expiration_date, 
								last_access_date=_last_access_date,
                                client_id=_client_id
							WHERE
								id=_person_id_from;	
						END;
					ELSE 
						BEGIN
							INSERT INTO user(id, person_id, name, alias_name, rg, access_data_user, access_data_password, expiration_date, last_access_date, main_company_id, client_id)
							select _person_id_from, _person_id, UPPER(_name), UPPER(_alias_name), _rg, _access_data_user, _access_data_password, _expiration_date, _last_access_date,_main_company_id, _client_id;
                            
                            IF(SELECT 1 = 1 FROM user_parent WHERE user_parent_id = _user_change AND user_id = _person_id_from) THEN
								BEGIN
									UPDATE user_parent SET inactive = 0 WHERE user_parent_id = _user_change AND user_id = _person_id_from;
								END;
                            ELSE 
								BEGIN
									INSERT INTO user_parent(user_parent_id, user_id, creation_user, creation_date, change_user, change_date, inactive)
									SELECT _user_change, _person_id_from, _user_change, _change_date, _user_change, _change_date, 0;
								END;
                            END IF;
						END;        
					END IF;
				END; /** End operation type 1 User */
			ELSEIF ('COM'=_object_type) THEN
				BEGIN
					IF (SELECT 1 = 1 FROM company where id=_person_id_from) THEN
						BEGIN
							UPDATE company SET 
								person_id=_person_id,
								social_name=UPPER(_name), 
								fantasy_name=UPPER(_alias_name),
								negative_close=_negative_close, 
                                process_movement_automatic=_movement_automatic,
                                establishment_type_id=_establishment_type_id, 
                                user_change=_user_change, 
								change_date=_change_date,
                                client_id=(CASE WHEN IFNULL(_client_id,0) = 0 THEN client_id ELSE _client_id END),
                                financial_cost_center_id = _financial_cost_center_id
							WHERE
								id=_person_id_from;
                                
							/** inative bank Account config Trasnfer */
							SELECT 
								p.bank_account_id, p.credit_provider_id, p.debit_provider_id
                            INTO 
								v_bank_account_id, v_c_provider_id, v_d_provider_id
							FROM person p
								INNER JOIN company c ON p.id = c.person_id 
							WHERE 
								p.id=_person_id;
                                
							IF(IFNULL(v_bank_account_id,0))THEN
								BEGIN
									UPDATE financial_transfer_group_setting SET 
									inactive = _inactive,  user_change = _user_change, change_date = _change_date
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
                                    /** inative all items*/
                                    UPDATE 
										financial_transfer_group_item_setting 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _change_date
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
									 /** inative all Products*/
                                    UPDATE 
										financial_transfer_product_setting 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _change_date
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
                                        
                                    /** update bank account*/    
                                    UPDATE 
										bank_account 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _change_date
									WHERE 
										id = v_bank_account_id;
                                        
									/** update user company
                                    TODO: Nao pode inativar o user company pq senao nao aparece no consulta e baixa e no fechamento de caixa
                                    UPDATE 
										user_company 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _change_date
									WHERE 
										user_id = _user_change AND company_id = _person_id_from;                                            
								  */    
								END;
							END IF;
                            /** inative Fornecedor Crédito e Bébito*/
                            SELECT person_id FROM provider WHERE id=v_c_provider_id INTO v_c_person_id;
                            SELECT person_id FROM provider WHERE id=v_d_provider_id INTO v_d_person_id;
                           
                            IF(IFNULL(v_c_person_id,0) and IFNULL(v_d_person_id,0))THEN 
								BEGIN
									 UPDATE 
										person 
                                     SET 
										inactive=_inactive,  user_change=_user_change, change_date=now()
									WHERE 
										id IN(v_c_person_id, v_d_person_id) AND object_type=v_object_type;
								END;
                            END IF;
						END;
					ELSE 
						BEGIN
							INSERT INTO company(id, person_id, social_name, fantasy_name, negative_close, process_movement_automatic, establishment_type_id, user_change, change_date, client_id, financial_cost_center_id)
							select _person_id_from, _person_id, UPPER(_name), UPPER(_alias_name), _negative_close, _movement_automatic, _establishment_type_id, _user_change, _change_date, _client_id, _financial_cost_center_id;
                            
                            insert into user_company(user_id, company_id, inactive, user_creation, creation_date, user_change, change_date) 
							select _user_change, _person_id_from, 0, _user_change, now(), _user_change, now();
						END;
					END IF;
					IF(IFNULL(_bank_account_id,0)=0) THEN
						BEGIN
							SET _bank_account_id = (IFNULL((SELECT MAX(id) FROM bank_account LIMIT 1),0) + 1);
							INSERT INTO bank_account(id, description, bank_balance_available, bank_limit_available, bank_code, bank_angency,
													 inactive, user_creation, creation_date, user_change, change_date, accumulate_balance)
							SELECT _bank_account_id, IFNULL(UPPER(_name),UPPER(_alias_name)), 0, 0, 0, 0, 0, _user_change, NOW(), _user_change, NOW(), 1;
							UPDATE person SET bank_account_id=_bank_account_id WHERE id=_person_id;
						END; END IF;
                        
					IF(IFNULL(_credit_provider_id,0)=0 AND IFNULL(_register_type,0)=0) THEN
						BEGIN
							INSERT INTO person(person_type, object_type, access_profile_id, inactive, user_creation, creation_date, user_change, change_date, blocked)
							SELECT 'C' AS person_type, 'FOR' AS object_type, 0 AS access_profile_id, 0 AS inactive, 1 AS user_creation, NOW() AS creation_date, 1 AS user_change, NOW() AS change_date, 0 AS blocked;
							SET _generated_id = LAST_INSERT_ID();
                             
                            SET _credit_provider_id = (IFNULL((SELECT MAX(id) FROM provider LIMIT 1),0) + 1);
                            INSERT INTO provider(id, person_id, social_name, fantasy_name, financial_group_id, financial_sub_group_id)
							SELECT _credit_provider_id, _generated_id AS person_id, CONCAT(UPPER(_name), '(IN)') AS social_name, CONCAT(UPPER(_alias_name), '(IN)') AS fantasy_name, '01.000' AS financial_group_id, '01.001' AS financial_sub_group_id;
                            
							UPDATE person SET credit_provider_id=_credit_provider_id WHERE id=_person_id;
						END; END IF;
                        
					IF(IFNULL(_debit_provider_id,0)=0 AND IFNULL(_register_type,0)=0) THEN
						BEGIN
							INSERT INTO person(person_type, object_type, access_profile_id, inactive, user_creation, creation_date, user_change, change_date, blocked)
							SELECT 'C' AS person_type, 'FOR' AS object_type, 0 AS access_profile_id, 0 AS inactive, 1 AS user_creation, NOW() AS creation_date, 1 AS user_change, NOW() AS change_date, 0 AS blocked;
							SET _generated_id = LAST_INSERT_ID();
                            
                            SET _debit_provider_id = (IFNULL((SELECT MAX(id) FROM provider LIMIT 1),0) + 1);
                            INSERT INTO provider(id, person_id, social_name, fantasy_name, financial_group_id, financial_sub_group_id)
							SELECT _debit_provider_id, _generated_id AS person_id, CONCAT(UPPER(_name), '(OUT)') AS social_name, CONCAT(UPPER(_alias_name), '(OUT)') AS fantasy_name, '07.000' AS financial_group_id, '07.001' AS financial_sub_group_id;
							UPDATE person SET debit_provider_id=_debit_provider_id WHERE id=_person_id;
						END; END IF;                        
				END; /** End operation type 1 Company */
			ELSEIF ('FOR'=_object_type) THEN
				BEGIN
					IF (SELECT 1 = 1 FROM provider where id=_person_id_from) THEN
						BEGIN
							UPDATE provider SET 
								person_id=_person_id,
								social_name=UPPER(_name), 
								fantasy_name=UPPER(_alias_name), 
                                financial_group_id=_financial_group_id, 
                                financial_sub_group_id=_financial_sub_group_id
							WHERE
								id=_person_id_from;
						END;
					ELSE 
						BEGIN
							INSERT INTO provider(id, person_id, social_name, fantasy_name, financial_group_id, financial_sub_group_id)
							select _person_id_from, _person_id, UPPER(_name), UPPER(_alias_name), _financial_group_id, _financial_sub_group_id;
						END;
					END IF;
				END; /** End operation type 1 Provider */
			END IF;
		END; /** End operation 1 */
	ELSEIF(2=_operation) THEN
		BEGIN   
			 UPDATE person SET inactive=_inactive,  user_change=_user_change, change_date = _processing_date_time
			 WHERE id=_person_id;
             
            IF ('COM'=_object_type) THEN
				BEGIN
					/** inative bank Account config Trasnfer */
					SELECT 
						p.bank_account_id, p.credit_provider_id, p.debit_provider_id
                    INTO 
						v_bank_account_id, v_c_provider_id, v_d_provider_id
					FROM 
						person p
						INNER JOIN company c ON p.id = c.person_id
					WHERE
						p.id=_person_id;
                        
							IF(IFNULL(v_bank_account_id,0))THEN
								BEGIN
									UPDATE financial_transfer_group_setting SET 
									inactive = _inactive,  user_change = _user_change, change_date = _processing_date_time
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
                                    /** inative all items*/
                                    UPDATE 
										financial_transfer_group_item_setting 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _processing_date_time
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
									 /** inative all Products*/
                                    UPDATE 
										financial_transfer_product_setting 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _processing_date_time
									WHERE 
										bank_account_origin_id=v_bank_account_id AND bank_account_destiny_id=0;
                                        
                                    /** update bank account*/    
                                    UPDATE 
										bank_account 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _processing_date_time
									WHERE 
										id = v_bank_account_id;
                                        
									/** update user company   
                                    TODO: Nao pode inativar o user company pq senao nao aparece no consulta e baixa e no fechamento de caixa
                                    UPDATE 
										user_company 
									SET 
										inactive = _inactive,  user_change = _user_change, change_date = _processing_date_time
									WHERE 
										user_id = _user_change AND company_id = _person_id_from;
									*/
								END;
							END IF;
                    /** inative Fornecedor Crédito e Bébito*/
					SELECT person_id FROM provider WHERE id=v_c_provider_id INTO v_c_person_id;        
                    SELECT person_id FROM provider WHERE id=v_d_provider_id INTO v_d_person_id;
                           
                    IF(IFNULL(v_c_person_id,0) and IFNULL(v_d_person_id,0))THEN 
						BEGIN
							UPDATE 
								person 
                            SET 
								inactive=_inactive,  user_change=_user_change, change_date = _processing_date_time
							WHERE 
								id IN(v_c_person_id, v_d_person_id) AND object_type=v_object_type;
						END;
                    END IF;
				END;
             END IF;
		END; /** End operation type equals 2 */
	ELSEIF(3=_operation) THEN
        BEGIN
			SET @t1 = CONCAT('SELECT p.id as person_id,  p.person_type, p.object_type, p.access_profile_id, p.cpf_cnpj, p.ie, ',
			'p.address_zip_code, p.address_zip_code_side, p.address_street_number, p.address_street_complement,  p.email, p.site, ',
			'IFNULL(p.register_type,0) AS register_type, p.inactive, p.user_creation, p.creation_date, IFNULL(p.bank_account_id,0) as bank_account_id, p.main_company_id, (SELECT description FROM bank_account WHERE id=p.bank_account_id LIMIT 1) AS bank_account_description,  ',
            'IFNULL(p.credit_provider_id,0) as credit_provider_id, (SELECT social_name FROM provider WHERE id=IFNULL(p.credit_provider_id,0) LIMIT 1) as credit_provider_description, ',
            'IFNULL(p.debit_provider_id,0) as debit_provider_id, (SELECT social_name FROM provider WHERE id=IFNULL(p.debit_provider_id,0) LIMIT 1) as debit_provider_description, ',
			'p.user_change, IFNULL((select name from user where id = p.user_change LIMIT 1),''SISTEMA'') as user_change_name, p.change_date, a.address_street, a.address_district, a.address_city_ibge, a.address_state_ibge, a.address_country_ibge,',
			'a.address_gia, cy.description as description_city, st.description as description_state, ct.description as description_country, ', 
			IF(('COM'=_object_type), 'c.id, c.social_name as name, c.fantasy_name as alias_name, c.negative_close, c.process_movement_automatic, NULL as rg, NULL as access_data_user, NULL as access_data_password,  NULL as expiration_date, NULL as last_access_date, 0 as financial_group_id, '''' as description_financial_group, 0 as financial_sub_group_id, '''' as description_financial_sub_group, p.person_type_id, c.establishment_type_id, p.blocked, c.client_id, 0 cash_closing_max_discount, IFNULL(c.financial_cost_center_id,0) financial_cost_center_id ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name, u.alias_name, 0 as negative_close, u.rg, u.access_data_user, u.access_data_password,  u.expiration_date, u.last_access_date, 0 as financial_group_id, '''' as description_financial_group, 0 as financial_sub_group_id, 0 as process_movement_automatic, '''' as description_financial_sub_group, p.person_type_id, 0 as establishment_type_id, 0 as blocked, u.client_id, u.cash_closing_max_discount, 0 financial_cost_center_id ',' '),
            IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.fantasy_name as alias_name, 0 as negative_close, 0 as process_movement_automatic, NULL as rg, NULL as access_data_user, NULL as access_data_password, NULL as expiration_date, NULL as last_access_date, IFNULL(f.financial_group_id,0) as financial_group_id, IFNULL((SELECT description FROM financial_group WHERE id=IFNULL(f.financial_group_id,0) LIMIT 1),'''') as description_financial_group, 0 as person_type_id, 0 as establishment_type_id, 0 as blocked,  0 client_id, IFNULL(financial_sub_group_id,0) as financial_sub_group_id,  IFNULL((SELECT description FROM financial_sub_group WHERE id=IFNULL(f.financial_sub_group_id,0) LIMIT 1),'''') as description_financial_sub_group, 0 cash_closing_max_discount, 0 financial_cost_center_id ',' '),
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
            IF((('USU'=_object_type) AND _client_id>0), concat(' AND u.client_id=', _client_id),''),
   			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),' '),        
            -- IF((('COM'=_object_type) AND _client_id>0), concat(' AND c.client_id=', _client_id),''),
   			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),' '));                          
            -- select @t1;
            PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /** End operation 3 */
	ELSEIF(4=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT p.id as person_id, p.object_type,  p.cpf_cnpj, p.email, p.bank_account_id, p.inactive, p.user_change, p.change_date, p.person_type, ',
			IF(('COM'=_object_type), 'c.id, c.social_name as name, c.fantasy_name as alias_name, '' as access_data_user, '' as access_data_password,  NULL as expiration_date, NULL as last_access_date, NULL AS exit_access_date, IFNULL(p.person_type_id,0) AS person_type_id, IFNULL(pt.description,0) AS person_type_description, IFNULL(c.establishment_type_id,0) AS establishment_type_id, IFNULL(et.description,0) AS establishment_type_description, IFNULL((select count(*) qtde from product p where p.company_id = c.id and p.inactive = 0),0) AS count_products, c.client_id, 0 count_company, '''' users, IFNULL(c.financial_cost_center_id,0) financial_cost_center_id  ',' '), 
			IF(('USU'=_object_type), concat('u.id, u.name, u.alias_name, u.access_data_user, u.access_data_password,  u.expiration_date, u.last_access_date, u.exit_access_date, IFNULL(p.person_type_id,0) AS person_type_id, IFNULL(pt.description,'''') AS person_type_description, 0 AS establishment_type_id, NULL AS establishment_type_description, 0 AS count_products, u.client_id, (SELECT IFNULL((SELECT count(*) FROM user_company WHERE user_id =', 'u.id and inactive = 0', '),0)) count_company, '''' users, 0 financial_cost_center_id  '),' '),
			IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.fantasy_name as alias_name, '' as access_data_user, '' as access_data_password,  NULL as expiration_date, NULL as last_access_date, NULL as exit_access_date, 0 AS person_type_id, NULL AS person_type_description, 0 AS establishment_type_id, NULL AS establishment_type_description, 0 AS count_products, 0 client_id, 0 count_company, '''' users, 0 financial_cost_center_id ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), 'INNER JOIN company c ON p.id=c.person_id',' '), 
            IF(('COM'=_object_type AND _user_change>0), concat(' INNER JOIN user_company uc ON uc.company_id=c.id and uc.user_id=',_user_change),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
			IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id',' '),             
			' LEFT JOIN address a ON p.address_zip_code=a.address_zip_code and p.address_zip_code_side=a.address_zip_code_side ',
			' LEFT JOIN address_city cy ON a.address_city_ibge=cy.address_city_ibge ',
			' LEFT JOIN address_state st ON a.address_state_ibge=st.address_state_ibge ',
			' LEFT JOIN address_country ct ON a.address_country_ibge=ct.address_country_ibge ',
            IF(('COM'=_object_type), 'LEFT JOIN person_type pt ON p.person_type_id = pt.id AND pt.inactive = 0 AND pt.object_type =  ''COM'' ',' '), 
            IF(('USU'=_object_type), 'LEFT JOIN person_type pt ON p.person_type_id = pt.id AND pt.inactive = 0 AND pt.object_type =  ''USU'' ',' '), 
            IF(('COM'=_object_type), 'LEFT JOIN establishment_type et ON c.establishment_type_id = et.id AND pt.inactive = 0 ',' '), 
			' WHERE 1=1 ',
			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND u.name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
            IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_exit_access_date,''))))>0)), concat(' AND u.exit_access_date ', rtrim(ltrim(_exit_access_date)),' '), ''),
            IF((('USU'=_object_type) AND _client_id>0), concat(' AND u.client_id=', _client_id),''),
			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('COM'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND c.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
            IF((('COM'=_object_type) AND _client_id>0), concat(' AND c.client_id=', _client_id),''),
			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('FOR'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND f.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
            IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive= ', _inactive), ''),
            IF((('FOR'=_object_type) AND (_person_type IS NOT NULL and _person_type <>'T')), concat(' AND p.person_type= ''', _person_type, ''' '), ''),
            IF((('COM'=_object_type) AND (IFNULL(_person_type_id,0)>0 and IFNULL(_establishment_type_id,0)=0)), concat(' AND p.person_type_id= ''', _person_type_id, ''' '), ''),
            IF((('COM'=_object_type) AND (IFNULL(_person_type_id,0)>0 and IFNULL(_establishment_type_id,0)>0)), concat(' AND p.person_type_id=', _person_type_id,' AND c.establishment_type_id= ''', _establishment_type_id, ''' '), ''),';');
			
			--  select @t1;
			
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END;
	ELSEIF(5=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			IF(('COM'=_object_type), 'c.id, c.social_name as name ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name ',' '),
			IF(('FOR'=_object_type), 'f.id, f.social_name as name ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), 'INNER JOIN company c ON p.id=c.person_id',' '), 
            IF(('COM'=_object_type AND _user_change>0), concat(' INNER JOIN user_company uc ON uc.company_id=c.id and uc.user_id=',_user_change),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
			IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id',' '), 
			' WHERE 1=1 ',
			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND u.name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('COM'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND c.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('FOR'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND f.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive= ', _inactive), ''),
            
   			IF(('COM'=_object_type), ' order by c.social_name',' '), 
			IF(('USU'=_object_type), ' order by u.name ',' '),
			IF(('FOR'=_object_type), ' order by f.social_name ',' '),';');            
			
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END;
	ELSEIF(7=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			IF(('COM'=_object_type), 'c.id, c.social_name as name, NULL as financial_group_id, NULL as financial_sub_group_id, NULL as description_financial_group, NULL as description_financial_sub_group ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name, NULL as financial_group_id, NULL as financial_sub_group_id, NULL as description_financial_group, NULL as description_financial_sub_group ',' '),
			IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.financial_group_id, f.financial_sub_group_id, fg.description as description_financial_group, fsg.description as description_financial_sub_group ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), 'INNER JOIN company c ON p.id=c.person_id',' '), 
            IF(('COM'=_object_type AND _user_change>0), concat(' INNER JOIN user_company uc ON uc.company_id=c.id and uc.user_id=',_user_change),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
			IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id '
            ' LEFT JOIN financial_group fg ON f.financial_group_id = fg.id '
            ' LEFT JOIN financial_sub_group fsg ON f.financial_sub_group_id = fsg.id and f.financial_group_id = fsg.financial_group_id  '
            ' LEFT JOIN address a ON p.address_zip_code=a.address_zip_code and p.address_zip_code_side=a.address_zip_code_side '
            ' LEFT JOIN address_city cy ON a.address_city_ibge=cy.address_city_ibge '
            ' LEFT JOIN address_state st ON a.address_state_ibge=st.address_state_ibge '
			' LEFT JOIN address_country ct ON a.address_country_ibge=ct.address_country_ibge ',' '), 
			' WHERE 1=1 ',
			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND u.name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('COM'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND c.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('FOR'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND f.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive= ', _inactive), ' '),
            IF((('FOR'=_object_type) AND (_person_type IS NOT NULL and _person_type <>'T')), concat(' AND p.person_type= ''', _person_type, ''' '), ''),
            
			IF(('COM'=_object_type), ' order by c.social_name',' '), 
			IF(('USU'=_object_type), ' order by u.name ',' '),
			IF(('FOR'=_object_type), ' order by f.social_name ',' '),';');
            
			--  select @t1;
			
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /**End operation 7*/	
	ELSEIF(8=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			IF(('COM'=_object_type), 'c.id, c.social_name as name, NULL as financial_group_id, NULL as financial_sub_group_id, NULL as description_financial_group, NULL as description_financial_sub_group ',' '), 
			IF(('USU'=_object_type), 'u.id, u.name, NULL as financial_group_id, NULL as financial_sub_group_id, NULL as description_financial_group, NULL as description_financial_sub_group ',' '),
			IF(('FOR'=_object_type), 'f.id, f.social_name as name, f.financial_group_id, f.financial_sub_group_id, fg.description as description_financial_group, fsg.description as description_financial_sub_group ',' '),
			' FROM ', 
			'person p ',
			IF(('COM'=_object_type), 'INNER JOIN company c ON p.id=c.person_id',' '), 
            IF(('COM'=_object_type AND _user_change>0), concat(' INNER JOIN user_company uc ON uc.company_id=c.id and uc.user_id=',_user_change),' '), 
			IF(('USU'=_object_type), 'INNER JOIN user u ON p.id=u.person_id',' '),
			IF(('FOR'=_object_type), 'INNER JOIN provider f ON p.id=f.person_id '
            ' LEFT JOIN financial_group fg ON f.financial_group_id = fg.id '
            ' LEFT JOIN financial_sub_group fsg ON f.financial_sub_group_id = fsg.id and f.financial_group_id = fsg.financial_group_id  '
            ' LEFT JOIN address a ON p.address_zip_code=a.address_zip_code and p.address_zip_code_side=a.address_zip_code_side '
            ' LEFT JOIN address_city cy ON a.address_city_ibge=cy.address_city_ibge '
            ' LEFT JOIN address_state st ON a.address_state_ibge=st.address_state_ibge '
			' LEFT JOIN address_country ct ON a.address_country_ibge=ct.address_country_ibge ',' '), 
			' WHERE 1=1 ',
			IF((('USU'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND u.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('USU'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND u.name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('COM'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND c.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('COM'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND c.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			IF((('FOR'=_object_type) AND (_person_id_from>0 and _person_id_to>0)), concat(' AND f.id between ', _person_id_from, ' and ', _person_id_to),''),
			IF((('FOR'=_object_type) AND (length(rtrim(ltrim(IFNULL(_name,''))))>0)), concat(' AND f.social_name like ''%', rtrim(ltrim(_name)), '%'' '), ''),
			-- IF((_inactive IS NOT NULL and _inactive<2), concat(' AND p.inactive= ', _inactive), ' '),
            IF((('FOR'=_object_type) AND (_person_type IS NOT NULL and _person_type <>'T')), concat(' AND p.person_type= ''', _person_type, ''' '), ''),
            
			IF(('COM'=_object_type), ' order by c.social_name',' '), 
			IF(('USU'=_object_type), ' order by u.name ',' '),
			IF(('FOR'=_object_type), ' order by f.social_name ',' '),';');
				
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
		END; /**End operation 8*/	        
	END IF; /**End if operation 4 */ 
END$$
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/