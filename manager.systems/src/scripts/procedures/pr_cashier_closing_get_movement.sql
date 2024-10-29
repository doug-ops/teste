USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cashier_closing_get_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cashier_closing_get_movement`(IN _operation TINYINT, 
												   IN _user_id BIGINT, 
                                                   IN _status_cash TINYINT,
												   IN _users_children_parent VARCHAR(5000),
                                                   IN _companys_id VARCHAR(5000),
												   IN _week_year INT)
BEGIN	
    DECLARE _client_id TINYINT DEFAULT 0;
    DECLARE _user_id_cash BIGINT DEFAULT 0;
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
        
		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, 
		@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SET @full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
		
		INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
		SELECT 'PR CASHIER CLOSING GET MOVEMENT', @full_error, 'emarianodeveloper@gmail.com', NOW(), 0, NOW(), 0;			
        
		SELECT @full_error;
	END;
    
	SELECT IFNULL((SELECT u.client_id FROM user u WHERE u.id = _user_id LIMIT 1),0) INTO _client_id;
    
	SET _user_id_cash = (CASE WHEN _client_id = 1 THEN 8 WHEN _client_id = 2 THEN 7 ELSE 0 END);
    
  	/**Drop temporary tables*/
    
	DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;    
    
	/**End Drop temporary tables*/   
    
	/**Temporary table to insert tmp_person_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_person_filter*/    
            
	IF(1 = _operation) THEN /** Operation 1 */
		BEGIN            
			
		INSERT INTO tmp_company_filter (company_id)
		SELECT id FROM company c
		INNER JOIN (
				SELECT company_id FROM user_company WHERE user_id IN 
                (
					SELECT x.id FROM
					(
						SELECT u.id FROM user_parent up 
						INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = _user_id_cash
						and u.client_id = _user_id 
						WHERE up.inactive = 0 GROUP BY u.id
                        UNION ALL
                        SELECT _user_id id
					) x
					GROUP BY id                       
				) GROUP BY company_id
			) uc ON c.id = uc.company_id;
        
			SELECT 
				cc.cashier_closing_id, cc.user_id, cc.week_year, cc.cashier_closing_status, date_format(cc.date_from,'%d/%m/%y') date_from, date_format(cc.date_to,'%d/%m/%y') date_to, 
                cc.user_creation, cc.creation_date, cc.user_change, cc.change_date, cc.inactive, ccc.cashier_closing_status cashier_closing_status_company, 
                ccc.company_id
			FROM 
				cashier_closing cc 
                INNER JOIN cashier_closing_company ccc ON cc.cashier_closing_id = ccc.cashier_closing_id AND cc.user_id = ccc.user_id
                INNER JOIN tmp_company_filter cf ON ccc.company_id = cf.company_id
			WHERE 
				cc.user_id = _user_id_cash 
                AND cc.cashier_closing_status < _status_cash
                AND (ccc.movement_value <> 0 OR ccc.pending_movement_value <> 0 OR ccc.discount_total <> 0 OR ccc.payment_total <> 0 OR ccc.pending_movement_after_value <> 0)
			GROUP BY 
				cc.cashier_closing_id, cc.user_id, cc.week_year, cc.date_from, cc.date_to, cc.cashier_closing_status,
                cc.user_creation, cc.creation_date, cc.user_change, cc.change_date, cc.inactive, 
                ccc.cashier_closing_status,
                ccc.company_id
			ORDER BY 
				cc.cashier_closing_id;
                                
        END; /** End Operation 1 */
	ELSEIF(2 = _operation) THEN /** Operation 2 */
		BEGIN
		
			/**VARIABLES CURSOR*/
                DECLARE _has_movements_pending_process BIT DEFAULT 0;
				DECLARE _has_movements BIT DEFAULT 0;
                DECLARE _cashier_closing_id BIGINT;
                DECLARE _week_filter INT;
			
                /**Cursor to search documents*/
				DECLARE documents_cursor CURSOR FOR
                
                SELECT 
					cashier_closing_id, week_year
				FROM 
					cashier_closing 
				WHERE 
					user_id = _user_id 
					AND cashier_closing_status < _status_cash
				ORDER BY 
					cashier_closing_id;
                
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
				OPEN documents_cursor;
				meuLoop: LOOP
				FETCH documents_cursor INTO _cashier_closing_id, _week_filter;
                
				IF _has_movements = 1 THEN
					LEAVE meuLoop;
				END IF;
                
                /**Has movements pending to process*/
                SET _has_movements_pending_process = 1;
                
                select _week_filter;
                CALL pr_cashier_closing_generate_movement(1, _user_id, _user_id, _week_filter, 1);
                
			  END LOOP meuLoop;
			  CLOSE documents_cursor;
		
		END; /** End Operation 2 */
	ELSEIF(3 = _operation) THEN /** Operation 3 */
		BEGIN
        
			/**Drop temporary tables*/
            
			DROP TEMPORARY TABLE IF EXISTS tmp_user_values;
			DROP TEMPORARY TABLE IF EXISTS tmp_user_filter; 
    
			DROP TEMPORARY TABLE IF EXISTS tmp_company_values;
			DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;   
            /**End Drop temporary tables*/
            
			/**Temporary table to insert tmp_person_values*/
			CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_values
			(
				txt text
			);
			/**End Temporary table to insert tmp_person_values*/
            
			/**Temporary table to insert tmp_user_values*/
			CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_values
			(
				txt text
			);
			/**End Temporary table to insert tmp_user_values*/
			
			/**Temporary table to insert tmp_user_filter*/
			CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_filter
			(
				user_id BIGINT NOT NULL
			);
			/**End Temporary table to insert tmp_user_filter*/              
			
			/**Temporary table to insert tmp_person_filter*/
			CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
			(
				company_id BIGINT NOT NULL
			);
			/**End Temporary table to insert tmp_person_filter*/
            
			IF(LENGTH(IFNULL(_companys_id, '')) > 0) THEN 
				BEGIN
					INSERT INTO tmp_company_values VALUES(_companys_id);
					SET @sql = concat("INSERT INTO tmp_company_filter (company_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_company_values), ",", "'),('"),"');");
					PREPARE stmt1 FROM @sql;
					EXECUTE stmt1;
				END;
			ELSEIF(LENGTH(IFNULL(_users_children_parent, '')) > 0) THEN 
					BEGIN
					
						INSERT INTO tmp_user_values VALUES(_users_children_parent);
						SET @sql = concat("INSERT INTO tmp_user_filter (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_user_values), ",", "'),('"),"');");
						PREPARE stmt1 FROM @sql;
						EXECUTE stmt1;
						
						INSERT INTO tmp_company_filter (company_id)
						SELECT id FROM company c
						INNER JOIN (SELECT company_id FROM user_company WHERE user_id IN (SELECT user_id FROM tmp_user_filter) GROUP BY company_id) uc ON c.id = uc.company_id;
												
					END;
				END IF;
            
			SELECT
				cc.cashier_closing_id, cc.user_id, cc.week_year, date_format(cc.date_from,'%d/%m/%y') date_from, date_format(cc.date_to,'%d/%m/%y') date_to, 
                CASE WHEN cc.cashier_closing_status = 0 THEN 'A' WHEN cc.cashier_closing_status = 1 THEN 'L' ELSE 'F' END cashier_closing_status, cc.user_creation, cc.creation_date, cc.user_change, cc.change_date, cc.inactive           
            FROM 
				cashier_closing cc 
            WHERE 
				cc.week_year = _week_year
                AND cc.user_id = _user_id_cash
                AND cc.inactive = 0;
                              
			SELECT
                ccc.cashier_closing_id, ccc.user_id, ccc.week_year, date_format(ccc.date_from,'%d/%m/%y') date_from, date_format(ccc.date_to,'%d/%m/%y') date_to, 
                ccc.company_id, co.social_name company_description, pe.bank_account_id, ba.description bank_account_description, ccc.movement_value, ccc.pending_movement_value, 
                (ccc.movement_value + ccc.pending_movement_value) total_company, ccc.discount_total, ccc.payment_total, ccc.pending_movement_after_value, 
                CASE WHEN ccc.cashier_closing_status = 0 THEN 'A' WHEN ccc.cashier_closing_status = 1 THEN 'L' ELSE 'F' END cashier_closing_status, ccc.user_creation, ccc.creation_date, ccc.user_change, ccc.change_date,
                DATE_FORMAT(ccc.launch_date, '%d/%m/%y %H:%i') launch_date, ccc.launch_user, DATE_FORMAT(ccc.close_date, '%d/%m/%y %H:%i') close_date, ccc.close_user, ccc.inactive
            FROM 
				cashier_closing_company ccc
			INNER JOIN 
				company co ON ccc.company_id = co.id			
			INNER JOIN 
				tmp_company_filter f ON ccc.company_id = f.company_id
			INNER JOIN person pe ON co.person_id = pe.id                
            INNER JOIN bank_account ba ON pe.bank_account_id = ba.id
            WHERE 
				ccc.week_year = _week_year
                AND ccc.user_id = _user_id_cash
                AND ccc.inactive = 0
                AND (ccc.movement_value <> 0 OR ccc.pending_movement_value <> 0 OR ccc.discount_total <> 0 OR ccc.payment_total <> 0 OR ccc.pending_movement_after_value <> 0)
				AND ccc.cashier_closing_status  = CASE WHEN _status_cash = 9 THEN ccc.cashier_closing_status 
									  ELSE _status_cash END;
                
			SELECT
                ccd.cashier_closing_id, ccd.user_id, ccd.week_year, date_format(ccd.date_from,'%d/%m/%y') date_from, date_format(ccd.date_to,'%d/%m/%y') date_to, ccd.company_id, 
                ccd.document_movement_id, ccd.document_parent_id, ccd.residue, ccd.credit, ccd.document_value, ccd.product_id, ccd.product_movement is_product_movement, ccd.document_note, ccd.user_creation, ccd.creation_date, 
                ccd.user_change, ccd.change_date, ccd.inactive
            FROM 
				cashier_closing_company_document ccd
            INNER JOIN 
				cashier_closing_company ccc ON ccd.company_id = ccc.company_id AND ccd.week_year = ccc.week_year
			INNER JOIN 
				tmp_company_filter f ON ccd.company_id = f.company_id
            WHERE 
				ccd.week_year = _week_year
                AND ccd.user_id = _user_id_cash
                AND ccd.inactive = 0
                AND ccc.cashier_closing_status  = CASE WHEN _status_cash = 9 THEN ccc.cashier_closing_status 
									  ELSE _status_cash END
				ORDER BY ccd.document_parent_id, ccd.company_id;
                                                  
		END; /** End Operation 3 */         
    END IF;
END$$

CALL pr_cashier_closing_get_movement(3, 7, 9, '7', '', 202429  )
-- CALL pr_cashier_closing_get_movement(3, 8, 9, '8', '', 202429  );