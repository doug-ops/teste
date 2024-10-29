USE `manager`;
DROP procedure IF EXISTS `pr_process_product_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_product_movement`(IN _operation INT, 
											   IN _movement_legacy_id BIGINT,
                                               IN _legacy_system_id INT,
                                               IN _product_id BIGINT,
											   IN _company_description VARCHAR(100),
                                               IN _credit_in_final BIGINT, 
                                               IN _credit_out_final BIGINT, 
                                               IN _reading_date DATETIME, 
                                               IN _movement_type VARCHAR(1),
                                               IN _inactive BIT,
                                               IN _change_user BIGINT, 
                                               IN _credit_in_initial BIGINT, 
                                               IN _credit_out_initial BIGINT,
                                               IN _credit_clock_initial BIGINT, 
                                               IN _credit_clock_final BIGINT,
                                               IN _company_id BIGINT,
                                               IN _is_offline BIT, 
                                               IN _initial_date DATETIME, 
											   IN _product_description VARCHAR(100),
                                               IN _user_id BIGINT)
                                               
BEGIN	

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR PROCESS PRODUCT MOVEMENT', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
        select @full_error;
	END;

	IF(1 = _operation AND _initial_date IS NULL) THEN /* Populate initial data*/
		BEGIN
			SELECT reading_date INTO _initial_date from product_movement where product_id = _product_id and company_id = _company_id and processing = 1 order by id desc limit 1;
		
            IF (_initial_date IS NULL) THEN
BEGIN	
					SELECT change_date INTO _initial_date from product where id = _product_id and company_id = _company_id;
                    
					IF (_initial_date IS NULL) THEN
					BEGIN
						SELECT _reading_date INTO _initial_date;
					END; END IF; /*End initial data is null*/
				END; END IF; /*End initial data is null*/			
			END; END IF; /*End Populate initial data*/
                
	IF(1=_operation AND 0=IFNULL(_is_offline,0)) THEN
		BEGIN
			DECLARE _exists_compnay BIT;
            DECLARE _exists_compnay_id BIT;
            DECLARE _next_company_id BIGINT;
		    DECLARE _exists_product BIT;
            SELECT 1 INTO _exists_compnay FROM company WHERE social_name=_company_description LIMIT 1;
			IF (IFNULL(_exists_compnay,0)=0) THEN
				BEGIN /** Not exists company */
					SELECT 1 INTO _exists_compnay_id FROM company WHERE id=_company_id  LIMIT 1; 
                   
                    IF (IFNULL(_exists_compnay_id,0)=1) THEN /** if id company integration exists*/
						BEGIN                                        
							DROP TEMPORARY TABLE IF EXISTS temp_next_code;
							CREATE TEMPORARY TABLE temp_next_code(next_code INT);
							INSERT INTO temp_next_code VALUES(next_code);
							SET @t1 =CONCAT("INSERT INTO temp_next_code SELECT t.id + 1 AS next_id FROM company t LEFT JOIN company t1 ON t1.id = t.id + 1 WHERE t.id>=0 and t1.id IS NULL ORDER BY t.id LIMIT 0, 1;");
							PREPARE stmt3 FROM @t1;
							EXECUTE stmt3;
							DEALLOCATE PREPARE stmt3; 
							SELECT next_code INTO _next_company_id FROM temp_next_code WHERE next_code IS NOT NULL LIMIT 1;
                        END;  /** end if id company integration exists*/
                    ELSE 
						BEGIN /** else set then id company integration exists*/
							SET _next_company_id  = _company_id;
                        END;
					END IF; 
					CALL pr_maintenance_person(1, _next_company_id, _next_company_id, 0, _company_description, _company_description, 'COM', 'J', null, null, null, '', null, 'A', '', '', '', '', null, null, 1058, null, 0, _change_user, now(), null, null, now(), now(), 0, 0, 0, 0, 0, '', null, 0, now(), 0, null, 0, 0, 0);

                    CALL pr_maintenance_user_company(1, 1, _next_company_id, 0, 1);
                    CALL pr_maintenance_integration_systems(1, _next_company_id, 'COM', 2, _company_id, 0, _change_user, now());
                END; END IF;   /** End not exists company */   
                
			SELECT 1 INTO _exists_product FROM product WHERE id=_product_id;
			IF (IFNULL(_exists_product,0)=0) THEN
				BEGIN 
					CALL pr_maintenance_product(1, _product_id, _product_id, _product_description, '1.00', '1.00', '1.00', '2', '3', _next_company_id, 0, 0, 0, _change_user, 0, 0, 0, null, 0, 0);
                    CALL pr_maintenance_integration_systems(1, _product_id, 'PRO', 2, _product_id, 0, _change_user, now());
                END; END IF;  
                
			IF(_credit_in_final = 0 AND _credit_out_final = 0) THEN
				BEGIN
					UPDATE product SET input_movement = _credit_in_final, output_movement = _credit_out_final WHERE id = _product_id;
				END;
			END IF;
                    
			IF (SELECT 1 = 1 FROM product_movement WHERE movement_legacy_id=_movement_legacy_id AND legacy_system_id=_legacy_system_id) THEN
				BEGIN			
                
					UPDATE 
						product_movement 
					SET					
						product_id=_product_id,
                        company_description=_company_description,
                        credit_in_final=_credit_in_final,
                        credit_out_final=_credit_out_final,
						movement_type=_movement_type,
                        reading_date=_reading_date,
                        initial_date=_initial_date,
                        processing = (CASE WHEN (_credit_in_final = 0 AND _credit_out_final = 0) THEN 1 ELSE 0 END),
						inactive=_inactive, 
                        user_change=_change_user, 
                        change_date=now()
					WHERE
						movement_legacy_id=_movement_legacy_id 
                        AND legacy_system_id=_legacy_system_id;
				END;
			ELSE 
				BEGIN   
					INSERT INTO product_movement(movement_legacy_id, legacy_system_id, product_id, company_description, credit_in_initial, credit_in_final, credit_out_initial, credit_out_final, credit_clock_initial, credit_clock_final, movement_type, reading_date, initial_date, processing, processing_date, document_in_id, document_out_id, is_offline, inactive, user_creation, creation_date, user_change, change_date)
                    select _movement_legacy_id, _legacy_system_id, _product_id, _company_description, NULL as _credit_in_initial, _credit_in_final, NULL as _credit_out_initial, _credit_out_final, ifnull(_credit_clock_initial,0), ifnull(_credit_clock_final,0), _movement_type, _reading_date, _initial_date,  (CASE WHEN (_credit_in_final = 0 AND _credit_out_final = 0) THEN 1 ELSE 0 END) as _processing, NULL as processing_date, NULL as document_in_id, NULL as document_out_id, ifnull(_is_offline, 1), _inactive, _change_user, now(), _change_user, now();
				END;
			END IF;            
		END;
	ELSEIF(1=_operation AND 1=IFNULL(_is_offline,0)) THEN
		BEGIN
			IF (SELECT 1 = 1 FROM product_movement WHERE product_id=_product_id AND company_id=_company_id AND is_offline=_is_offline AND processing=0) THEN
				BEGIN
					UPDATE 
						product_movement 
					SET					
						credit_in_initial=_credit_in_initial,
                        credit_in_final=_credit_in_final,
						credit_out_initial=_credit_out_initial,
                        credit_out_final=_credit_out_final,
                        credit_clock_initial=_credit_clock_initial,
                        credit_clock_final=_credit_clock_final,
						movement_type=_movement_type,
                        reading_date=_reading_date,
                        initial_date=_initial_date,
                        processing=0,
                        is_offline=_is_offline,
						inactive=_inactive, 
                        user_change=_change_user, 
                        change_date=now()
					WHERE
						product_id=_product_id 
                        AND company_id=_company_id 
                        AND is_offline=_is_offline 
                        AND processing=0;
				END;
			ELSE 
				BEGIN   
					INSERT INTO product_movement(movement_legacy_id, legacy_system_id, product_id, company_id, company_description, credit_in_initial, credit_in_final, credit_out_initial, credit_out_final, credit_clock_initial, credit_clock_final, movement_type, reading_date, initial_date,processing, processing_date, document_in_id, document_out_id, is_offline, inactive, user_creation, creation_date, user_change, change_date)
                    select _movement_legacy_id, _legacy_system_id, _product_id, _company_id, _company_description, _credit_in_initial, _credit_in_final, _credit_out_initial, _credit_out_final, _credit_clock_initial, _credit_clock_final, _movement_type, _reading_date, _initial_date, 0 as _processing, NULL as processing_date, NULL as document_in_id, NULL as document_out_id, _is_offline, _inactive, _change_user, now(), _change_user, now();
				END;
			END IF;
        END;
	ELSEIF(3=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT IFNULL(pm.id,0) AS movement_id, p.id AS product_id, p.description AS product_description, p.company_id, p.sale_price, p.conversion_factor, ', 
						 'IFNULL(p.input_movement,0) AS credit_in_initial, IFNULL(pm.credit_in_final,0) AS credit_in_final, ', 
                         'IFNULL(p.output_movement,0) AS credit_out_initial, IFNULL(pm.credit_out_final,0) AS credit_out_final,  ',
                         'IFNULL(p.clock_movement,0) AS credit_clock_initial, IFNULL(pm.credit_clock_final,0) AS credit_clock_final,  ',
                         'IFNULL(pm.id,0) AS movement_id, is_offline, ',
                         'IFNULL((DATE_FORMAT(pm.initial_date, ''%d/%m/%Y %H:%i'')),''-'') as last_processing, ',
                         'p.enable_clock_movement ',
                         'FROM ',
                         'product p ',
                         'LEFT JOIN product_movement pm ON p.id=pm.product_id AND pm.processing=0 ',
						 ' WHERE 1=1 ', 
                         IF((_product_id>0), concat(' AND p.id=', _product_id), ''),
                         -- IF((_company_id>0), concat(' AND p.company_id=', _company_id), ''),                         
                         ';');                         
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
        END;
	ELSEIF(4=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT IFNULL(pm.id,0) AS movement_id, p.id AS product_id, p.description AS product_description, p.company_id, p.sale_price, p.conversion_factor, ', 
						 'IFNULL(p.input_movement,0) AS credit_in_initial, IFNULL(pm.credit_in_final,0) AS credit_in_final, ', 
                         'IFNULL(p.output_movement,0) AS credit_out_initial, IFNULL(pm.credit_out_final,0) AS credit_out_final,  ',
                         'p.clock_movement, credit_clock_final, '
                         'IFNULL(p.clock_movement,0) AS credit_clock_initial, (CASE WHEN IFNULL(p.enable_clock_movement,0) = 0 THEN p.clock_movement ELSE IFNULL(pm.credit_clock_final,0) END) AS credit_clock_final,  ',
                         '(IFNULL(pm.credit_in_final,0)-IFNULL(p.input_movement,0)) AS in_total, ',
                         '(IFNULL(pm.credit_out_final,0)-IFNULL(p.output_movement,0)) out_total, ',
                         '((CASE WHEN IFNULL(p.enable_clock_movement,0) = 0 THEN p.clock_movement ELSE IFNULL(pm.credit_clock_final,0) END)-IFNULL(p.clock_movement,0)) clock_total, ',
                         '(((IFNULL(pm.credit_in_final,0)-IFNULL(p.input_movement,0))-(IFNULL(pm.credit_out_final,0)-IFNULL(p.output_movement,0)))-((CASE WHEN IFNULL(p.enable_clock_movement,0) = 0 THEN p.clock_movement ELSE IFNULL(pm.credit_clock_final,0) END)-IFNULL(p.clock_movement,0)))*p.conversion_factor AS total, ',
                         'DATE_FORMAT(pm.reading_date, ''%d/%m/%Y %H:%i'') as reading_date, IFNULL(p.enable_clock_movement,0) AS enable_clock_movement, DATE_FORMAT(IFNULL(pm.initial_date, pm.reading_date), ''%d/%m/%Y %H:%i'') as initial_date ',
                         'FROM ',
                         'product p ',
                         'INNER JOIN product_movement pm ON p.id=pm.product_id ',
						 ' WHERE 1=1 ', 
                         IF((_product_id>0), concat(' AND p.id=', _product_id), ''),
                         IF((_company_id>0), concat(' AND pm.company_id=', _company_id), ''),
                         IF((_is_offline=1), concat(' AND pm.is_offline=', 1), ''),
                         IF((_is_offline=0), concat(' AND pm.is_offline=', 0), ''),
                         ' AND pm.processing=0 ',
                         ';');  
                         
			-- select @t1;
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
        END;/*End operation 4*/
	ELSEIF(21=_operation AND 1=IFNULL(_is_offline,0)) THEN
		BEGIN
        DECLARE v_initial_date DATETIME;
        
        SELECT  
			max(reading_date) reading_date 
		FROM 
			product_movement 
		WHERE 
			company_id=_company_id
			AND processing = 1
            AND product_id=_product_id LIMIT 1 INTO v_initial_date;
        
			IF(IFNULL(v_initial_date,0)) THEN
				BEGIN
					SET _initial_date = v_initial_date;
				END;
			ELSE
				BEGIN
				   SET _initial_date = DATE(now());
				END;
			END IF;
            
			IF (SELECT 1 = 1 FROM product_movement WHERE product_id=_product_id AND company_id=_company_id AND is_offline=_is_offline AND processing=0) THEN
				BEGIN
                    UPDATE 
						product_movement 
					SET					
						credit_in_initial=_credit_in_initial,
                        credit_in_final=_credit_in_final,
						credit_out_initial=_credit_out_initial,
                        credit_out_final=_credit_out_final,
                        credit_clock_initial=_credit_clock_initial,
                        credit_clock_final=_credit_clock_final,
						movement_type=_movement_type,
                        reading_date=_reading_date,
						-- initial_date=_initial_date,
                        processing=0,
                        is_offline=_is_offline,
						inactive=_inactive, 
                        user_change=_change_user, 
                        change_date=now()
					WHERE
						product_id=_product_id 
                        AND company_id=_company_id 
                        AND is_offline=_is_offline 
                        AND processing=0;
				END;
			ELSE 
				BEGIN  
					 INSERT INTO product_movement(movement_legacy_id, legacy_system_id, product_id, company_id, company_description, credit_in_initial, credit_in_final, credit_out_initial, credit_out_final, credit_clock_initial, credit_clock_final, movement_type, reading_date, initial_date,processing, processing_date, document_in_id, document_out_id, is_offline, inactive, user_creation, creation_date, user_change, change_date)
                     select _movement_legacy_id, _legacy_system_id, _product_id, _company_id, _company_description, _credit_in_initial, _credit_in_final, _credit_out_initial, _credit_out_final, _credit_clock_initial, _credit_clock_final, _movement_type, _reading_date, _initial_date, 0 as _processing, NULL as processing_date, NULL as document_in_id, NULL as document_out_id, _is_offline, _inactive, _change_user, now(), _change_user, now();
				END;
			END IF;
        END;/*End operation 21*/
        ELSEIF(22=_operation) THEN
		BEGIN
				/**VARIABLES CURSOR*/
				DECLARE _has_motive BIGINT DEFAULT 1;
                DECLARE _validation_message VARCHAR(5000) DEFAULT '';
                 IF (ifnull(_company_id, 0)) THEN
					BEGIN
						SELECT 
							-- c.company_description ,c.execution_date, c.motive, c.document_note,
							-- CASE WHEN c.execution_date IS NULL THEN 0 ELSE 1 END AS has_executed,
							-- primeiro verifica se tem lojas pendentes
						SUM(CASE WHEN c.execution_date IS NOT NULL THEN 0 WHEN ifnull(c.motive, 0)=0 THEN 1 WHEN (ifnull(c.motive, 0)=3 AND c.document_note IS NULL) THEN 1 ELSE 0 END) AS has_motive INTO _has_motive
						FROM company_movement_execution c WHERE 
						c.company_id IN (SELECT us.company_id FROM user_company us WHERE us.user_id =_user_id GROUP BY us.company_id) AND c.week_year <= YEARWEEK(date_add(now(), INTERVAL -1 WEEK))
                        AND c.company_id<>_company_id AND c.inactive=0;
				END;
				END IF;
            IF (1 >= _has_motive) THEN
				BEGIN
				SELECT p.id AS has_no_movement FROM 
					product p
					LEFT JOIN product_movement pm ON p.id=pm.product_id AND pm.processing=0
					WHERE p.company_id = _company_id
					AND pm.id IS NULL;
				END;
			ELSE
				BEGIN
					SET _validation_message = CONCAT('Existe loja pendente de processamento. Favor processar as mesmas ou informar um motivo do não processamento antes de continuar.');
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message;
				END;
			END IF;
		END; /**End operation 22*/
        ELSEIF(23=_operation) THEN
		BEGIN
			select max(doc.document_parent_id) document_parent_id, doc.company_id, DATE_FORMAT(max(doc.payment_data),'%d/%m/%Y') payment_data from document_movement doc where doc.company_id = _company_id and is_product_movement = 1 group by doc.company_id;
        END; /**End operation 23*/
    END IF;
END$$

/**
START TRANSACTION;
SET SQL_SAFE_UPDATES = 0;
CALL pr_process_product_movement(21,0,0,10832,'',86299600,59351500,'2023-01-27 15:23:16.995778','A',0,0,null,null,0,0,1521,1,null,null,null );
ROLLBACK;
*/
--  pr_process_product_movement(3,null,null,21760,null,null,null,null,null,null,null,null,null,null,null,1357,null,null,null,null );

-- CALL pr_process_product_movement(4,null,null,null,null,null,null,null,null,null,null,null,null,null,null,1357,1,null,null,null )