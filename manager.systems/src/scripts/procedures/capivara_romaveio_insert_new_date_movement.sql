USE `manager`;
DROP PROCEDURE IF EXISTS `pr_capivara_romaveio`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_capivara_romaveio`(IN _operation INT)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		BEGIN
			DECLARE v_can_execute INT DEFAULT 1;
            DECLARE v_has_financial_transfer_process BIT DEFAULT 0;
			DECLARE finished INTEGER DEFAULT 0;
			DECLARE v_bank_account_origin_id INT(11);
			DECLARE v_bank_account_destiny_id INT(11);
			DECLARE v_execution_period	TINYINT(4);
			DECLARE v_execution_initial_period DATE;
            DECLARE v_company_id BIGINT; 
            DECLARE v_company_description VARCHAR(100);
            DECLARE v_payment_expiry_data DATETIME;
            DECLARE v_week_year INT(11);
			DECLARE v_week_day INT(11);
            DECLARE v_initial_date_week DATE;
            DECLARE v_final_date_week DATE; 
            DECLARE v_actual_date DATE;
            DECLARE v_actual_initial_date_week DATE;
            DECLARE v_actual_final_date_week DATE;
			DECLARE v_actual_week_year INT(11);
            DECLARE v_actual_week_day INT(11);
			DECLARE v_document_parent_id BIGINT default 0;
            DECLARE v_execution_date DATETIME;
            DECLARE v_has_movement BIT DEFAULT 1;
            DECLARE v_count INT DEFAULT 0;
	
			DEClARE cursor_ft CURSOR FOR 
				SELECT 
					bank_account_origin_id, bank_account_destiny_id, execution_period, execution_initial_period  
				FROM 
					financial_transfer_group_setting WHERE execution_initial_period > '2021-12-26 00:00:00' 
                    AND inactive = 0
                    AND execution_period > 0
					GROUP BY bank_account_origin_id, bank_account_destiny_id;
                
			-- declare NOT FOUND handler com esta variavel para finalizar o cursor, dizendo que feito a primeira passada, a variavel de inicio é finished = 0
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 0;
			-- abriu cursor
			OPEN cursor_ft;
             meuLoop: LOOP
				FETCH cursor_ft INTO 
				 v_bank_account_origin_id,
				 v_bank_account_destiny_id,
				 v_execution_period,
				 v_execution_initial_period;
				IF finished = 1 THEN 
					LEAVE meuLoop;
				END IF;
                
                SELECT 
						doc.company_id, co.social_name, fg.execution_period, doc.change_date, YEARWEEK(doc.payment_expiry_data) week_year,
						WEEKDAY(DATE_ADD(doc.payment_expiry_data, INTERVAL fg.execution_period DAY)) week_day,
                        DATE_ADD(doc.payment_expiry_data, INTERVAL - weekday(doc.payment_expiry_data) DAY) initial_date_week,
						DATE_ADD(doc.payment_expiry_data, INTERVAL (6 - weekday(doc.payment_expiry_data)) DAY) final_date_week 
                     INTO 
					 v_company_id, v_company_description, v_execution_period, v_payment_expiry_data, v_week_year, v_week_day, v_initial_date_week,v_final_date_week
				FROM 
					document_movement doc
					inner join company co on doc.company_id = co.id
                    inner join financial_transfer_group_setting fg on doc.bank_account_id = fg.bank_account_origin_id
				WHERE
					doc.is_product_movement = 1 
					and doc.group_transfer_id is not null
                    and doc.payment_data > '2021-12-26 00:00:00' 
					and doc.bank_account_id = v_bank_account_origin_id
                    ORDER BY doc.document_parent_id LIMIT 1 ;
                    
				-- select v_company_id, v_company_description, v_execution_period, v_payment_expiry_data, v_week_year, v_week_day, v_initial_date_week,v_final_date_week;
				set v_count = v_count + 1;
				 -- select v_bank_account_origin_id, v_bank_account_destiny_id, v_company_id, v_company_description, v_execution_period, 
                 -- v_payment_expiry_data, v_week_year, v_week_day, v_initial_date_week, v_final_date_week;

                 /*variavel de inicializar uma data atual*/
                  SET v_actual_date = v_execution_initial_period;
                  /*variavel de inicializar document parent id*/
                  SET v_document_parent_id = 0;
                 /*variavel de inicialização do while do loop*/
                 SET v_can_execute = 1;
				  
                 /*while para criar proxima data de execução por 2 meses para frente, conforme o execution_period*/
				
				  WHILE v_can_execute = 1 DO
                    
					  SET v_actual_initial_date_week = DATE_ADD(v_actual_date, INTERVAL - weekday(v_actual_date) DAY);
					  SET v_actual_final_date_week = DATE_ADD(v_actual_date, INTERVAL (6 -  weekday(v_actual_date)) DAY);
					  SET v_actual_week_year = YEARWEEK(v_actual_date);
					  SET v_actual_week_day = WEEKDAY(v_actual_date); 
                      -- select v_actual_date, v_actual_initial_date_week, v_actual_final_date_week, v_actual_week_year, v_actual_week_day;
					  /*if para sair fora do loop para proxima registro*/
						IF(v_actual_date > date_add(now(), INTERVAL 2 MONTH)) THEN
						   SET v_can_execute = 0;
							-- select 'saiu';
						END IF; 
                     
                        /*insert das proxima execuçoes*/
                        INSERT INTO company_movement_execution (expiry_data, initial_date_week, final_date_week, week_year, week_day, execution_date, company_id, company_description, 
                        bank_account_origin_id, bank_account_destiny_id, document_parent_id, inactive, document_note, motive)
                        SELECT v_actual_date, v_actual_initial_date_week, v_actual_final_date_week, v_actual_week_year, v_actual_week_day, null, v_company_id, v_company_description,
                        v_bank_account_origin_id, v_bank_account_destiny_id, null, 0, null, null;

						SET v_has_movement = 0;
						/** Pega id do movimento e data de pagamento */
						SELECT document_parent_id, payment_data, 1 as has_movement
						INTO v_document_parent_id, v_execution_date, v_has_movement
						FROM 
							document_movement 
                        WHERE 
							company_id = v_company_id
							AND is_product_movement = 1 
							AND group_transfer_id IS NOT NULL 
							AND  payment_data > '2021-12-26 00:00:00'
							AND document_parent_id > v_document_parent_id 
                            ORDER BY document_parent_id LIMIT 1;
                            
                            IF(v_has_movement) then	
                                CALL pr_company_movement_execution(23, null, null, null, null, null, null, v_company_id, v_document_parent_id, v_execution_date, null, null, null, null, null);                             
                            END IF;
                           
                    if(v_execution_period = 30)then
						set v_actual_date = date_add(v_actual_date, INTERVAL 1 month);
					else
						set v_actual_date = date_add(v_actual_date, INTERVAL v_execution_period DAY); 
                    end if;
                    
                   CALL pr_company_movement_execution(27, null, null, null, null, null, null, v_company_id, v_document_parent_id, v_execution_date, v_actual_date, v_bank_account_origin_id, v_bank_account_destiny_id, null, v_company_description);                             
				   END WHILE;
            END LOOP meuLoop; 
				CLOSE cursor_ft;
        END; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		BEGIN
        END; /** End operation 2 */
	END IF;
END$$
CALL pr_capivara_romaveio(1);

-- select * from company_movement_execution where company_id=1207;
-- truncate table company_movement_execution;

-- select * from document_movement where company_id=464 and document_parent_id> 123293

-- describe company_movement_execution

