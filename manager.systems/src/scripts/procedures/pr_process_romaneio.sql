USE `manager`;
DROP procedure IF EXISTS `pr_process_romaneio`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_romaneio`(IN _operation INT, 
									   IN _company_id BIGINT,
									   IN _document_parent_id BIGINT,
									   IN _user_id BIGINT,                                          
									   IN _document_note VARCHAR(1000),
                                       IN _count_week_process TINYINT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
    DECLARE _week_day TINYINT DEFAULT 0;
    DECLARE _week_year INT;
    /**
    DECLARE v_execution_period	TINYINT(4);
    DECLARE v_execution_initial_period DATETIME;
    DECLARE v_id BIGINT(20) DEFAULT 0;
    
	DECLARE v_actual_date DATE;
    DECLARE v_payment_data DATETIME;
    DECLARE v_execution_date DATETIME;
    DECLARE v_expiry_data DATETIME;
    DECLARE v_exist INT DEFAULT 0;
    DECLARE v_bank_account_origin_id INT(11);
    DECLARE v_person_id BIGINT(20) DEFAULT 0;
    DECLARE v_week_year INT DEFAULT 0;
    DECLARE v_exist_negative INT DEFAULT 0;
    */
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		 @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'ROMANEIO', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;
		
	END;
    
	/**Temporary table to insert tmp_execution_days*/
    DROP TEMPORARY TABLE IF EXISTS tmp_execution_days;
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_execution_days
	(
		execution_day INT NOT NULL
	);
	/**End Temporary table to insert tmp_execution_days*/    

	SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	SELECT WEEKDAY(DATE(_processing_date_time)) INTO _week_day;
	SELECT 
		(CASE
			WHEN
				_week_day = 6
			THEN
				YEARWEEK(DATE_ADD(DATE(_processing_date_time),
							INTERVAL - 1 DAY))
			ELSE YEARWEEK(DATE(_processing_date_time))
		END) + 1 INTO _week_year;
	
	IF(1=_operation) THEN /* Process negative */
		BEGIN
			START TRANSACTION;
            UPDATE company_movement_execution SET motive = 1, document_note = _document_note, inactive = 1, execution_date = _processing_date_time WHERE company_id = _company_id AND week_year = _week_year;            
			COMMIT;
        END; /* End Process negative */
	ELSEIF(2=_operation) THEN  /* Process block */
			BEGIN   
			   START TRANSACTION;
               UPDATE company_movement_execution SET motive = 4, document_note = _document_note, execution_date = _processing_date_time WHERE company_id = _company_id AND week_year = _week_year;            
               COMMIT;        
			END;  /* End Process generate block */
	ELSEIF(3=_operation) THEN  /* Process Movement */
			BEGIN   
			   START TRANSACTION;
               UPDATE company_movement_execution SET document_parent_id = _document_parent_id, execution_date = _processing_date_time WHERE company_id = _company_id AND week_year = _week_year;            
			   COMMIT;       
			END;  /* End Process Movement */            
	ELSEIF(4=_operation) THEN  /* Process gerenate new week movements */
			BEGIN   
				IF(1 = 1) THEN
					BEGIN
						SET SQL_SAFE_UPDATES = 0;
                    END;
				END IF;
				
                IF(1 = 1) THEN
					BEGIN
					DECLARE finished INTEGER DEFAULT 0;
					DECLARE _client_id TINYINT;
					DECLARE _person_id BIGINT;                
					DECLARE _company_description VARCHAR(100);
					DECLARE _execution_period INTEGER;  
					DECLARE _execution_initial_period DATE;  
					DECLARE _week_day_company TINYINT;  
					DECLARE _expiry_data DATE;  
					DECLARE _execution_days VARCHAR(50);
					DECLARE _initial_date_week DATE;  
					DECLARE _final_date_week DATE;
					DECLARE _next_week_year INT;
					DECLARE _exist_movement_company BIT;
					DECLARE _blocked BIT;
					DECLARE _last_execution DATE;
					DECLARE _next_expiry_data DATE;
					DECLARE _fixed_day BIT;
					DECLARE _fixed_day_month BIT;
					
					DEClARE cursorMovements 
					CURSOR FOR 
					SELECT 
						cof.client_id,
						cof.person_id,
						cof.company_id,
						cof.company_description,
						cof.blocked,
						gsf.execution_period,
						gsf.execution_initial_period,
						gsf.fixed_day,
						gsf.fixed_day_month,
						gsf.week_day,
						gsf.execution_days
					FROM
						(SELECT 
							gs.bank_account_origin_id,
								gs.bank_account_destiny_id,
								gs.execution_period,
								gs.execution_initial_period,
								gs.fixed_day,
								gs.fixed_day_month,
								gs.week_day,
								gs.execution_days
						FROM
							financial_transfer_group_setting gs
						WHERE
							gs.inactive = 0
								AND gs.bank_account_destiny_id = 0
								AND gs.execution_period IS NOT NULL
								AND gs.week_day IS NOT NULL
						GROUP BY gs.bank_account_origin_id , gs.bank_account_destiny_id , gs.fixed_day, gs.fixed_day_month, gs.execution_period , gs.execution_initial_period , gs.week_day, gs.execution_days) gsf
							INNER JOIN
							(SELECT 
								co.person_id,
								co.id company_id,
								co.social_name company_description,
								co.client_id,
								pe.bank_account_id bank_account_origin_id,
								0 bank_account_destiny_id,
								pe.blocked
							FROM
								company co
							INNER JOIN person pe ON co.person_id = pe.id
							WHERE
								co.id =  (CASE WHEN IFNULL(_company_id, 0) = 0 THEN co.id ELSE _company_id END)
								AND pe.inactive = 0
							) cof ON gsf.bank_account_origin_id = cof.bank_account_origin_id AND gsf.bank_account_destiny_id = cof.bank_account_destiny_id
								
							AND (CASE WHEN WEEKDAY(DATE(gsf.execution_initial_period)) = 6
									  THEN YEARWEEK(DATE_ADD(DATE(gsf.execution_initial_period), INTERVAL - 1 DAY))
									  ELSE YEARWEEK(DATE(gsf.execution_initial_period)) END)  
									  <= 
								(CASE WHEN WEEKDAY(DATE(NOW())) = 6 THEN YEARWEEK(DATE_ADD(DATE(NOW()), INTERVAL - 1 DAY))
									  ELSE YEARWEEK(DATE(NOW())) END)
								-- AND gsf.execution_initial_period <= DATE(now())
								-- AND gsf.fixed_day_month = 1 -- AND gsf.week_day = 6
								-- AND cof.company_id = 1655
						ORDER BY 
							gsf.execution_period,
							gsf.week_day;

					-- declare NOT FOUND handler
					DECLARE CONTINUE HANDLER 
					FOR NOT FOUND SET finished = 1;
						
					SELECT 
						(CASE
							WHEN
								_week_day = 6
							THEN
								YEARWEEK(DATE_ADD(DATE(DATE_ADD(_processing_date_time, INTERVAL (7 * _count_week_process) DAY)), INTERVAL - 1 DAY))
							ELSE YEARWEEK(DATE(DATE_ADD(_processing_date_time, INTERVAL (7 * _count_week_process) DAY)))
						END) + 1 INTO _next_week_year;
                        						
						SET _initial_date_week = STR_TO_DATE(CONCAT(_next_week_year,' Monday'), '%x%v %W');
						SET _final_date_week = STR_TO_DATE(CONCAT(_next_week_year,' Sunday'), '%x%v %W');

						OPEN cursorMovements;

						getMovements: LOOP
						FETCH cursorMovements INTO _client_id, _person_id, _company_id, _company_description, _blocked, 
						_execution_period, _execution_initial_period, _fixed_day, _fixed_day_month, _week_day_company, _execution_days;  
			
							IF finished = 1 THEN 
								LEAVE getMovements;
							END IF;			
							
							DELETE FROM tmp_execution_days;
							IF(LENGTH(IFNULL(_execution_days, '')) > 0) THEN 
								BEGIN
									SET @sql = concat("INSERT INTO tmp_execution_days (execution_day) ", CONCAT('SELECT ', REPLACE(_execution_days, ",", " UNION ALL SELECT ")));
									PREPARE stmt1 FROM @sql;
									EXECUTE stmt1;
								END; 
							END IF;	
																			
							SET _exist_movement_company = 0;
							IF(SELECT 1 = 1 FROM company_movement_execution WHERE company_id = _company_id AND  week_year = _next_week_year LIMIT 1) THEN
								BEGIN
									SET _exist_movement_company = 1;                            
								END;
							END IF;
                                                        														
							IF(_exist_movement_company = 0 AND IFNULL(_fixed_day,0) = 1) THEN
								BEGIN                                             
									SET _week_day_company = CASE WHEN IFNULL(_week_day_company,-1) = -1 THEN 0 ELSE _week_day_company END;
									INSERT INTO company_movement_execution(company_id, company_description, expiry_data, initial_date_week, final_date_week, week_year, week_day, execution_date, document_parent_id, motive, document_note, inactive)
									SELECT _company_id, _company_description, DATE_ADD(_initial_date_week, INTERVAL _week_day_company DAY) expiry_data, 
									_initial_date_week, _final_date_week, _next_week_year week_year, _week_day_company week_day, 
									(CASE WHEN _blocked = 1 THEN DATE_ADD(_initial_date_week, INTERVAL _week_day_company DAY) ELSE NULL END) _execution_date, 
									NULL document_parent_id, 
									(CASE WHEN _blocked = 1 THEN 4 ELSE NULL END) _motive, 
									(CASE WHEN _blocked = 1 THEN 'Bloqueado' ELSE NULL END) _document_note, 
									0 _inactive;
							   END;
							ELSEIF(_exist_movement_company = 0 AND IFNULL(_fixed_day_month,0) = 1) THEN 
								BEGIN
									DECLARE has_mor_execution_days INT DEFAULT 0;
									DECLARE _initial_execution_day DATE;  
									DECLARE _final_execution_day DATE;
                                                                      
									-- Definição do cursor
									DECLARE cursorExecutionDays CURSOR 
                                    FOR 
									SELECT STR_TO_DATE(CONCAT((CASE WHEN year(_initial_date_week) < year(_final_date_week) THEN year(_final_date_week) ELSE year(_initial_date_week) END), '-', (CASE WHEN month(_initial_date_week) < month(_final_date_week) THEN month(_final_date_week) ELSE month(_initial_date_week) END), '-', execution_day), '%Y-%m-%d') initial_execution, STR_TO_DATE(CONCAT((CASE WHEN year(_initial_date_week) < year(_final_date_week) THEN year(_final_date_week) ELSE year(_initial_date_week) END), '-', (CASE WHEN month(_initial_date_week) < month(_final_date_week) THEN month(_final_date_week) ELSE month(_initial_date_week) END), '-', execution_day), '%Y-%m-%d') final_execution  from tmp_execution_days;

									-- Definição da variável de controle de looping do cursor
									DECLARE CONTINUE HANDLER FOR NOT FOUND SET has_mor_execution_days = 1;

									-- Abertura do cursor
									OPEN cursorExecutionDays;

									-- Looping de execução do cursor
									loopExecutionDays: LOOP
									FETCH cursorExecutionDays INTO _initial_execution_day, _final_execution_day;                                          

									-- Controle de existir mais registros na tabela
									IF has_mor_execution_days = 1 THEN
										LEAVE loopExecutionDays;
									END IF;
																	
									IF(DAYOFWEEK(_initial_execution_day) = 1) THEN
										BEGIN
											SET _initial_execution_day = DATE_ADD(_initial_execution_day, INTERVAL 1 DAY);
										END;
									END IF;
                                    
                                        -- select _initial_execution_day, _final_execution_day, _initial_date_week, _final_date_week;
                                        																			
										IF(_initial_execution_day >= _initial_date_week AND _initial_execution_day <= _final_date_week) THEN
											BEGIN
												-- SELECT _initial_execution_day, _initial_date_week, _final_date_week;
												INSERT INTO company_movement_execution(company_id, company_description, expiry_data, initial_date_week, final_date_week, week_year, week_day, execution_date, document_parent_id, motive, document_note, inactive)
												SELECT _company_id, _company_description, _initial_execution_day expiry_data, 
												_initial_date_week, _final_date_week, _next_week_year week_year, _week_day_company week_day, 
												(CASE WHEN _blocked = 1 THEN DATE_ADD(_initial_date_week, INTERVAL _week_day_company DAY) ELSE NULL END) _execution_date, 
												NULL document_parent_id, 
												(CASE WHEN _blocked = 1 THEN 4 ELSE NULL END) _motive, 
												(CASE WHEN _blocked = 1 THEN 'Bloqueado' ELSE NULL END) _document_note, 
												0 _inactive;
												
											END;
										ELSEIF(_final_execution_day >= _initial_date_week AND _final_execution_day <= _final_date_week) THEN
											BEGIN
												-- SELECT _initial_execution_day, _initial_date_week, _final_date_week;
												
												INSERT INTO company_movement_execution(company_id, company_description, expiry_data, initial_date_week, final_date_week, week_year, week_day, execution_date, document_parent_id, motive, document_note, inactive)                                            
												SELECT _company_id, _company_description, _final_execution_day expiry_data, 
												_initial_date_week, _final_date_week, _next_week_year week_year, _week_day_company week_day, 
												(CASE WHEN _blocked = 1 THEN DATE_ADD(_initial_date_week, INTERVAL _week_day_company DAY) ELSE NULL END) _execution_date, 
												NULL document_parent_id, 
												(CASE WHEN _blocked = 1 THEN 4 ELSE NULL END) _motive, 
												(CASE WHEN _blocked = 1 THEN 'Bloqueado' ELSE NULL END) _document_note, 
												0 _inactive;
												
											END;                                        
										END IF;
											
									-- Retorna para a primeira linha do loop
									END LOOP loopExecutionDays;			
								END;
							END IF;
							
							-- SIGNAL SQLSTATE '45000'
							-- SET MESSAGE_TEXT = 'Processamento de movimentos inativo.';
						
					END LOOP getMovements;
					CLOSE cursorMovements;    

                    END;
				END IF;
				
			END;  /* End Process gerenate new week movements */            
	END IF;/*End operations*/
END$$
-- SET SQL_SAFE_UPDATES = 0;
CALL pr_process_romaneio(4, null, 0, 1, '', 0); 
