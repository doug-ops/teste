USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_closing_save_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_save_movement`(IN _operation TINYINT, 
												   IN _cashier_closing_id BIGINT, 
                                                   IN _week_year INT,
                                                   IN _movement_value DECIMAL(19,2),
                                                   IN _pending_movement_value DECIMAL(19,2),
                                                   IN _total_company DECIMAL(19,2),
												   IN _discount_total DECIMAL(19,2),
                                                   IN _payment_total DECIMAL(19,2),
                                                   IN _pending_movement_after_value DECIMAL(19,2),
                                                   IN _status INT,
                                                   IN _user_change BIGINT,
                                                   IN _note_cash VARCHAR(2000),
                                                   IN _note_company VARCHAR(2000),
                                                   IN _company_id BIGINT)
                                                   
BEGIN	
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _cashing_close_status INT DEFAULT 0;
    DECLARE _validation_message VARCHAR(5000) DEFAULT '';
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING SAVE MOVEMENT', @full_error, 'emarianodeveloper@gmail.com', NOW(), 0, NOW(), 0;			
        
		SELECT @full_error;
	END;
            
	IF(1 = _operation) THEN /** Operation 1 */
		BEGIN            
			SELECT IFNULL(timezone_database, 0) INTO _timezone_database from config_systems LIMIT 1;
			SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
            
            SELECT IFNULL((SELECT cashier_closing_status FROM cashier_closing WHERE cashier_closing_id  = _cashier_closing_id AND week_year = _week_year LIMIT 1),0) INTO _cashing_close_status;
			
            IF(_cashing_close_status > 1) THEN /* validation status cashing close */
				BEGIN
					SET _validation_message = concat("Caixa da semana: ", SUBSTRING(CAST(202401 AS CHAR), 5, 2), '/', SUBSTRING(CAST(_week_year AS CHAR), 1, 4), " não pode ser alterado pois o mesmo encontra-se com status de fechado.");
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message,
					MYSQL_ERRNO = 403;  
                END;
			END IF; /* end validation status cashing close */        
                        
			UPDATE 
				cashier_closing SET user_change = _user_change, change_date = _processing_date_time, 
				launch_user = CASE WHEN _status <= 1 THEN _user_change ELSE launch_user END, 
				launch_date = CASE WHEN _status <= 1 THEN _processing_date_time ELSE launch_date END,
				close_user = CASE WHEN _status > 1 THEN _user_change ELSE close_user END, 
				close_date = CASE WHEN _status > 1 THEN _processing_date_time ELSE close_date END,
                cashier_closing_status = _status,
				note = _note_cash
			WHERE
				cashier_closing_id  = _cashier_closing_id AND week_year = _week_year;

        END; /** End Operation 1 */
	ELSEIF(2 = _operation) THEN /** Operation 2 */
		BEGIN            
			SELECT IFNULL(timezone_database, 0) INTO _timezone_database from config_systems LIMIT 1;
			SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
            
            SELECT IFNULL((SELECT cashier_closing_status FROM cashier_closing_company WHERE cashier_closing_id  = _cashier_closing_id AND week_year = _week_year AND company_id = _company_id LIMIT 1),0) INTO _cashing_close_status;
			
            IF(_cashing_close_status > 1) THEN /* validation status cashing close */
				BEGIN
					SET _validation_message = concat("Caixa da semana: ", SUBSTRING(CAST(202401 AS CHAR), 5, 2), '/', SUBSTRING(CAST(_week_year AS CHAR), 1, 4), " empresa: ", CAST(_company_id AS CHAR), " não pode ser alterado pois o mesmo encontra-se com status de fechado.");
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message,
					MYSQL_ERRNO = 403;  
                END;
			END IF; /* end validation status cashing close */        
                        
			UPDATE 
				cashier_closing_company SET user_change = _user_change, change_date = _processing_date_time, 
				launch_user = CASE WHEN _status <= 1 THEN _user_change ELSE launch_user END, 
				launch_date = CASE WHEN _status <= 1 THEN _processing_date_time ELSE launch_date END,
				close_user = CASE WHEN _status > 1 THEN _user_change ELSE close_user END, 
				close_date = CASE WHEN _status > 1 THEN _processing_date_time ELSE close_date END,
                cashier_closing_status = _status,
                movement_value = _movement_value,
                pending_movement_value = _pending_movement_value,
                discount_total = _discount_total,
                payment_total = _payment_total,
                pending_movement_after_value = _pending_movement_after_value,
				note = _note_company
			WHERE
				cashier_closing_id  = _cashier_closing_id AND week_year = _week_year AND company_id = _company_id AND cashier_closing_status < 2;

        END; /** End Operation 2 */
    END IF;
END$$