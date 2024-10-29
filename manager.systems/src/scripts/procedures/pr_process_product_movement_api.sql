USE `manager`;
DROP procedure IF EXISTS `pr_process_product_movement_api`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_product_movement_api`(IN _operation INT, 
												   IN _product_id BIGINT,
												   IN _product_description VARCHAR(100),
												   IN _company_id BIGINT,
												   IN _company_description VARCHAR(100),
												   IN _credit_in_final BIGINT, 
												   IN _credit_out_final BIGINT,                                                
												   IN _reading_date DATETIME, 
                                                   IN _initial_date DATETIME,
												   IN _movement_type VARCHAR(1),                                               
												   IN _is_offline BIT, 
												   IN _inactive BIT,
												   IN _change_user BIGINT)
                                               
BEGIN	
    DECLARE _movement_id BIGINT;
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR PROCESS PRODUCT MOVEMENT API', @full_error, 'emarianodeveloper@gmail.com', now(), 0, now(), 0;			
        
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
			SELECT IFNULL((SELECT id  FROM product_movement  WHERE product_id = _product_id AND company_description = _company_description AND processing = 0),0) INTO _movement_id;
			IF (_movement_id > 0) THEN
				BEGIN			                
					UPDATE 
						product_movement 
					SET					
						product_id  =_product_id,
                        company_id = _company_id,
                        company_description = _company_description,
                        credit_in_final = _credit_in_final,
                        credit_out_final = _credit_out_final,
						movement_type = _movement_type,                        
                        initial_date = _initial_date,
                        reading_date = _reading_date,
                        processing = 0,
						inactive = _inactive, 
                        user_change = _change_user, 
                        change_date = now()
					WHERE
						product_id = _product_id AND company_description = _company_description AND processing = 0;
                        
                    SELECT _movement_id;    
				END;
			ELSE 
				BEGIN   
					INSERT INTO product_movement(movement_legacy_id, legacy_system_id, product_id, company_id, company_description, credit_in_initial, credit_in_final, credit_out_initial, credit_out_final, credit_clock_initial, credit_clock_final, movement_type, reading_date, initial_date, processing, processing_date, document_in_id, document_out_id, is_offline, inactive, user_creation, creation_date, user_change, change_date)
                    select 0 movement_legacy_id, 0 legacy_system_id, _product_id, _company_id, _company_description, NULL as _credit_in_initial, _credit_in_final, NULL as _credit_out_initial, _credit_out_final,  0 _credit_clock_initial, 0 _credit_clock_final, _movement_type, _reading_date, _initial_date,  (CASE WHEN (_credit_in_final = 0 AND _credit_out_final = 0) THEN 1 ELSE 0 END) as _processing, NULL as processing_date, NULL as document_in_id, NULL as document_out_id, 1 is_offline, _inactive, _change_user, now(), _change_user, now();
                    SELECT LAST_INSERT_ID() INTO _movement_id; 
                    SELECT _movement_id;
				END;
			END IF;            
		END;
    END IF;
END$$

/**
USE `manager`;
START TRANSACTION;
SET SQL_SAFE_UPDATES = 0;
CALL pr_process_product_movement_api(1, 99999999, 'TESTE PRODUTO', 99999999, 'TESTE LOJA', 12,123, now(), now(), 'A' ,0 ,0 , 1);
ROLLBACK;

SELECT * FROM product_movement where id >= 145330

delete from product_movement where id in (145337, 145339);
*/