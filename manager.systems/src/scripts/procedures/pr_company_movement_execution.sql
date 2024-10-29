USE `manager`;
DROP procedure IF EXISTS `pr_company_movement_execution`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_company_movement_execution`(
										  IN _operation INT,
                                          IN _date_from DATETIME,
										  IN _date_to DATETIME,
                                          IN _company_ids VARCHAR(500),
                                          IN _field_id BIGINT(20),
                                          IN _field_name VARCHAR(20),
                                          IN _field_value VARCHAR(500),
                                          IN _company_id BIGINT(20),
										  IN _document_parent_id BIGINT(20),
                                          IN _execution_date DATETIME,
                                          IN _expirationDate DATETIME,
										  IN _bank_account_origin_id BIGINT,
                                          IN _bank_account_destiny_id BIGINT,
                                          IN _week_year INT(11),
                                          IN _company_description VARCHAR(500),
                                          IN _user_id BIGINT,
                                          IN _is_execution BIT,
                                          IN _document_note varchar(1000),
                                          IN _motive INT,
                                          IN _inactive BIT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE v_execution_period	TINYINT(4);
    DECLARE v_execution_initial_period DATETIME;
    DECLARE v_id BIGINT(20) DEFAULT 0;
    DECLARE v_week_day TINYINT DEFAULT 0;
	DECLARE v_actual_date DATE;
    DECLARE v_payment_data DATETIME;
    DECLARE v_execution_date DATETIME;
    DECLARE v_expiry_data DATETIME;
    DECLARE v_exist INT DEFAULT 0;
    DECLARE v_bank_account_origin_id INT(11);
    DECLARE v_person_id BIGINT(20) DEFAULT 0;
    DECLARE v_week_year INT DEFAULT 0;
    DECLARE v_exist_negative INT DEFAULT 0;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		 @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'ROMANEIO', @full_error, 'douglinhas.mariano@gmail.com', now(), 0, now(), 0;
		
	END;

    SELECT IFNULL(timezone_database, 0)	INTO _timezone_database FROM config_systems	LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
    IF(IFNULL(_bank_account_origin_id,0))THEN
		SELECT person_id INTO v_person_id from company where id = _company_id;
		SELECT bank_account_id INTO v_bank_account_origin_id from person WHERE id = v_person_id limit 1;
	ELSE
		SET v_bank_account_origin_id = _bank_account_origin_id;
	END IF;
	
	IF(1=_operation) THEN
			IF ('executionDate' = _field_name) THEN
				BEGIN
					UPDATE company_movement_execution SET execution_date = STR_TO_DATE(_field_value,'%d/%m/%Y') WHERE id = _field_id;
				END;
			ELSEIF ('expiryData' = _field_name) THEN
				BEGIN
					UPDATE company_movement_execution SET expiry_data = STR_TO_DATE(_field_value,'%d/%m/%Y') WHERE id = _field_id;
					IF('expiryData' = _field_name) THEN
						BEGIN
							UPDATE company_movement_execution SET initial_date_week = date_add(STR_TO_DATE(_field_value,'%d/%m/%Y'), interval - weekday(STR_TO_DATE(_field_value,'%d/%m/%Y')) day) WHERE id = _field_id;
						END;
					END IF;
					IF('expiryData' = _field_name) THEN
						BEGIN
							UPDATE company_movement_execution SET final_date_week = date_add(STR_TO_DATE(_field_value,'%d/%m/%Y'), interval (6 - weekday(STR_TO_DATE(_field_value,'%d/%m/%Y'))) day) WHERE id = _field_id;
                        END;
					END IF;
					IF('expiryData' = _field_name) THEN
						BEGIN
							UPDATE company_movement_execution SET week_year = YEARWEEK(STR_TO_DATE(_field_value,'%d/%m/%Y')) WHERE id = _field_id;
                        END;
					END IF;
					IF ('expiryData' = _field_name) THEN
						BEGIN
							UPDATE company_movement_execution SET week_day = weekday(STR_TO_DATE(_field_value,'%d/%m/%Y')) WHERE id = _field_id;
                        END;
					END IF;
				END;
			ELSEIF ('inactive' = _field_name) THEN
				BEGIN
					UPDATE company_movement_execution SET inactive = CASE WHEN _field_value = '1' THEN 1 ELSE 0 END WHERE id = _field_id;
				END;
			ELSEIF ('motive' = _field_name) THEN
				BEGIN
					UPDATE company_movement_execution SET motive = _field_value, execution_date=_processing_date_time, inactive=1 WHERE id = _field_id;
                END;
			ELSEIF ('documentNote' = _field_name) THEN
				BEGIN
					UPDATE company_movement_execution SET document_note = _field_value WHERE id = _field_id;
				END;
			END IF;
		ELSEIF(4=_operation) THEN
			BEGIN   
                SET @t1 = CONCAT('SELECT DATE_FORMAT(c.execution_date,''%d/%m/%Y'') AS execution_date, DATE_FORMAT(c.expiry_data, ''%d/%m/%Y'') AS expiry_data,', 
					'c.id, c.company_id, c.company_description, c.expiry_data, c.execution_date, c.document_parent_id, IFNULL(c.motive,0) as motive, c.document_note, c.inactive FROM ',
                    'company_movement_execution c ', 
                    'INNER JOIN (select company_id from user_company where user_id in (',_user_id,') group by company_id) uc ON c.company_id = uc.company_id ',
                    ' WHERE ',
                    concat('c.week_year BETWEEN CASE WHEN WEEKDAY(''', _date_from ,''') < 6 THEN YEARWEEK(DATE(''', _date_from ,''')) + 1 ELSE YEARWEEK(DATE(''', _date_from ,''')) END '
                    'AND CASE WHEN WEEKDAY(''', _date_to ,''') < 6 THEN YEARWEEK(DATE(''', _date_to ,''')) + 1 ELSE YEARWEEK(DATE(''', _date_to ,''')) END '),
                    IF((LENGTH(_company_ids) > 0), concat(' AND c.company_id in (', _company_ids, ')'),''),
                    -- concat(' AND c.inactive=0'),
					-- concat(' AND YEARWEEK(c.expiry_data) = CASE WHEN WEEKDAY(''', _date_from ,''') = 0 THEN YEARWEEK(DATE(''', _date_from ,''')) + 1 ELSE YEARWEEK(DATE(''', _date_from ,''')) END '),
                    concat(' ORDER BY c.company_description, c.execution_date, c.motive DESC '), '');
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
                
                -- select @t1;
			END;
		ELSEIF(23=_operation) THEN
			BEGIN
					-- TODO pegar a configuracao da loja na conf transferencia para achar qual a week_year correto 
                    	SELECT f.execution_initial_period, f.execution_period, f.week_day 
                        INTO v_execution_initial_period, v_execution_period, v_week_day
                        FROM company c
						INNER JOIN person p on c.person_id=p.id
                        INNER JOIN financial_transfer_group_setting f ON f.bank_account_origin_id = p.bank_account_id AND bank_account_destiny_id = 0
                        WHERE c.id = _company_id ORDER BY f.change_date DESC LIMIT 1;

                        SELECT max(payment_data) INTO v_payment_data FROM document_movement WHERE company_id = _company_id AND is_product_movement = 1 ORDER BY payment_data;
							IF(v_execution_period = 7) THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL v_execution_period DAY); 
								END; 
							 ELSEIF(v_execution_period = 15) THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL 14 DAY);
								END; 
							 ELSEIF(v_execution_period = 30)THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL 1 month);
								END;
							 END IF;
							SET v_execution_date = date_add(DATE(v_actual_date), INTERVAL ( v_week_day - weekday(v_actual_date) ) DAY); 
								
                    /** Pega a data de execução da semana pra descobrir a semana do ano*/           
					select yearWeek(DATE(v_execution_date)) INTO v_week_year;
					IF(_is_execution=1) THEN
						BEGIN
							UPDATE company_movement_execution SET document_parent_id = _document_parent_id, execution_date = now(), 
							motive = _motive, inactive = _inactive, document_note = _document_note where week_year = v_week_year and company_id = _company_id;
							
							if(SELECT ROW_COUNT() = 0) then 
								SELECT MIN(id) AS id FROM company_movement_execution WHERE company_id = _company_id AND ifnull(inactive,0)=0 AND execution_date IS NULL INTO v_id;
								UPDATE company_movement_execution SET  document_parent_id = _document_parent_id, execution_date = now(), 
								motive = _motive, inactive = _inactive, document_note = _document_note where id = v_id and company_id= _company_id;                
							end if;
						END;
				   ELSE
						BEGIN
							START TRANSACTION;
							/** 1 = motiv negative */    
							UPDATE company_movement_execution SET execution_date = now(), motive = _motive, document_note = _document_note, inactive=_inactive WHERE week_year = v_week_year and company_id= _company_id;
                            
                            if(SELECT ROW_COUNT() = 0) then 
								SELECT MIN(id) AS id FROM company_movement_execution WHERE company_id = _company_id AND ifnull(inactive,0)=0 AND execution_date IS NULL INTO v_id;
								UPDATE company_movement_execution SET execution_date = now(), motive = _motive, inactive = _inactive, document_note = _document_note where id = v_id and company_id= _company_id;                
							end if;
                            COMMIT;
						END; 
					END IF; 
					SELECT MIN(id) AS id FROM company_movement_execution WHERE company_id = _company_id AND ifnull(inactive,0)=0 AND execution_date IS NULL INTO v_id;
					IF(_motive=4 && v_id > 0) THEN
							UPDATE company_movement_execution SET execution_date = now(), motive = _motive, document_note = _document_note, inactive=_inactive WHERE id = v_id; 	
							DELETE FROM company_movement_execution WHERE id > v_id AND company_id = _company_id;
					END IF;
				END;
		 ELSEIF(25=_operation) THEN
			BEGIN   
				DECLARE v_bank_account_id INT(11) default 0;
				
				SELECT person_id INTO v_person_id FROM company WHERE id=_company_id;
				SELECT bank_account_id INTO v_bank_account_id FROM bank_account_statement WHERE person_id=v_person_id LIMIT 1;
				IF(v_bank_account_id <> _bank_account_origin_id)THEN
					BEGIN
						SET _bank_account_origin_id=v_bank_account_id;
					END; 
				END IF;  
				 SELECT c.id, c.company_description, c.company_id, c.motive, c.document_note FROM 
				 company_movement_execution c
				 INNER JOIN user_company uc on c.company_id=uc.company_id
				 WHERE 
				 c.company_id=_company_id
				 and uc.user_id=_user_id
				 and c.expiry_data=DATE(_expirationDate)
				 and c.bank_account_origin_id=_bank_account_origin_id
				 and c.bank_account_destiny_id=_bank_account_destiny_id
				 and c.week_year=_week_year;
			END;
		ELSEIF(27=_operation) THEN
			BEGIN 
				DECLARE v_verif_week_year INT(11);
                
				SELECT MIN(id) AS id INTO v_id FROM company_movement_execution WHERE company_id = _company_id AND execution_date IS NULL AND inactive=0;
				IF(ifnull(v_id,0)=0) then
					SET v_id = 0;
				END IF;
				IF(v_id=0) THEN
					SELECT yearweek(max(payment_data)) INTO v_verif_week_year FROM document_movement 
					WHERE company_id=_company_id AND is_product_movement = 1  AND payment_data > '2021-12-26 00:00:00' ORDER BY payment_data;
					IF(IFNULL(v_verif_week_year,0) <= YEARWEEK(DATE(_processing_date_time)))THEN
						SET v_exist=0;
						SELECT 1 INTO v_exist FROM company_movement_execution where week_year=YEARWEEK(DATE(_processing_date_time)) and company_id=_company_id limit 1;
					
						-- Buscar Data de inicio, dias e semana.
						SELECT f.execution_initial_period, f.execution_period, f.week_day 
						INTO v_execution_initial_period, v_execution_period, v_week_day
						FROM company c
						INNER JOIN person p on c.person_id=p.id
                        INNER JOIN financial_transfer_group_setting f ON f.bank_account_origin_id = p.bank_account_id AND bank_account_destiny_id = 0
                        WHERE c.id = _company_id ORDER BY f.change_date DESC LIMIT 1;
                    
                        SELECT max(id) id INTO v_id FROM company_movement_execution where company_id = _company_id limit 1;
                        
                        IF(IFNULL(v_execution_initial_period,0) = 0)THEN
							BEGIN
								SET v_execution_initial_period = date(NOW());
                            END;
						END IF;
					   IF(v_exist = 0 &&  v_id = 0)THEN
                            
							INSERT INTO company_movement_execution(company_id, company_description, expiry_data, initial_date_week, final_date_week, 
							week_year, week_day, execution_date, document_parent_id, motive, document_note, bank_account_origin_id, bank_account_destiny_id,
							inactive)
							
							SELECT 
							_company_id, _company_description, v_execution_initial_period, DATE_ADD(v_execution_initial_period, INTERVAL - weekday(v_execution_initial_period) DAY) initial_date_week,
							DATE_ADD(v_execution_initial_period, INTERVAL (6 - weekday(v_execution_initial_period)) DAY) new_final_date_week,
							(case when v_week_day = 6 then yearweek(date_add(date(v_execution_initial_period), interval -1 day)) else yearweek(date(v_execution_initial_period)) end ) week_year, 
                            weekday(v_execution_initial_period), null, null, null, null, v_bank_account_origin_id, 
							_bank_account_destiny_id, 0; 
						ELSE
                        
							SELECT max(payment_data) INTO v_payment_data FROM document_movement WHERE company_id = _company_id AND is_product_movement = 1 ORDER BY payment_data;
							
                            SELECT 1 INTO v_exist_negative FROM company_movement_execution where company_id = _company_id and motive=1 and week_year=yearweek(DATE(_expirationDate)) limit 1;
                           
                           IF(IFNULL(v_payment_data,0)=0) THEN
								BEGIN
									set v_payment_data = _expirationDate;
								END;
							 END IF;
                             
                              IF(v_exist_negative=1) THEN
								BEGIN
									SET v_payment_data = _expirationDate;
								END;
							 END IF;
                            IF(v_execution_period = 7) THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL v_execution_period DAY); 
								END; 
							 ELSEIF(v_execution_period = 15) THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL 14 DAY);
								END; 
							 ELSEIF(v_execution_period = 30)THEN
								BEGIN
									SET v_actual_date = date_add(DATE(v_payment_data), INTERVAL 1 month);
								END;
							 END IF;
                             
                             SET v_execution_date = date_add(DATE(v_actual_date), INTERVAL ( v_week_day - weekday(v_actual_date) ) DAY); 
                             
							START TRANSACTION;
							INSERT INTO company_movement_execution(company_id, company_description, expiry_data, initial_date_week, final_date_week, 
							week_year, week_day, execution_date, document_parent_id, motive, document_note, bank_account_origin_id, bank_account_destiny_id,
							inactive)
				
							SELECT 
							_company_id, _company_description, v_execution_date, 
							DATE_ADD(v_execution_date, INTERVAL - weekday(v_execution_date) DAY) initial_date_week,
							DATE_ADD(v_execution_date, INTERVAL (6 - weekday(v_execution_date)) DAY) new_final_date_week,
							(case when v_week_day = 6 then yearweek(date_add(date(v_execution_date), interval -1 day)) else yearweek(date(v_execution_date)) end ) v_week_year, 
                            weekday(v_execution_date), null, null, null, null, v_bank_account_origin_id, 
							_bank_account_destiny_id, 0;
							COMMIT;
						END IF;
					END IF;
				END IF;
			END;
	END IF;/*End operation 1*/
END$$

CALL pr_company_movement_execution(4, '2024-01-08 00:00:00', '2024-01-14 23:59:59', null, null, null, null, null, null, null, null, null, null, null, null, 8, null, null, null, null  );