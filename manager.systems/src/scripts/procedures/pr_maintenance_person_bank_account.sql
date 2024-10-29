USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_person_bank_account`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_person_bank_account` (IN _operation INT, 
													   IN _object_id BIGINT, 
													   IN _object_type VARCHAR(10), 
                                                       IN _bank_account_id INT, 
                                                       IN _inactive INT, 
                                                       IN _user_change BIGINT)
	BEGIN
		DECLARE _person_id BIGINT DEFAULT 0;
        DECLARE _message VARCHAR(100); 
  		DECLARE _timezone_database TINYINT DEFAULT 0;
		DECLARE _processing_date_time DATETIME DEFAULT NOW();
        
        IF(_operation=1 OR _operation=6) THEN
			BEGIN
				SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
				SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);            
            END; END IF;

		SELECT person_id INTO _person_id FROM company WHERE id = _object_id;
		IF(IFNULL(_person_id,0)=0) THEN
			BEGIN
					SET _message = CONCAT('Codigo de Pessoa : ',IFNULL(_person_id,0), ' inválido.');
					SIGNAL SQLSTATE '45000' 
					SET MESSAGE_TEXT = _message;
			END; END IF; 
		IF(_operation=1) THEN
			BEGIN
				IF ('COM'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM person_bank_account WHERE person_id=_person_id 
										      AND bank_account_id=_bank_account_id) THEN
							BEGIN
								UPDATE 
									person_bank_account
								SET 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_processing_date_time
								WHERE
									person_id=_person_id
                                    AND bank_account_id=_bank_account_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO person_bank_account(person_id, bank_account_id, inactive, user_creation, creation_date, user_change, change_date)
								select _person_id, _bank_account_id, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
                            END;
						END IF; /**End if exists company bank account */ 
					END; /** End operation type 1 Company */
				END IF;
			END; /** End operation type 1 */
		ELSEIF(_operation=4) THEN
			BEGIN
				SET @t1 = CONCAT('SELECT ',
                'pb.bank_account_id, pb.person_id, ba.description ',
				'FROM ', 
                'person_bank_account pb ',
                'INNER JOIN bank_account ba ON pb.bank_account_id=ba.id ',
				'WHERE ',
                'pb.person_id=', _person_id,
                ' AND pb.inactive= ', _inactive
                );
				-- select @t1;
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
            END; /**End operation 5*/
		ELSEIF(_operation=6) THEN
			BEGIN
				IF ('COM'=_object_type) THEN
					BEGIN
						UPDATE 
							person_bank_account
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_processing_date_time
						WHERE 
							person_id=_person_id;
					END;
				END IF; /**End if delete object type */
            END; /**End operation 6*/
		ELSEIF(_operation=7) THEN /*Get all bank account of company */
			BEGIN
				SET @t1 = CONCAT(
                'SELECT x.bank_account_id, x.description, x.bank_balance_available, x.main_bank_account ',
                'FROM ',
				'(SELECT ',
				'bank_account_id, UPPER(ba.description) AS description, ba.bank_balance_available, 1 main_bank_account ', 
				'FROM ', 
				'person p ',
				'INNER JOIN bank_account ba ON p.bank_account_id=ba.id ',
				'WHERE p.id=',_person_id, 
				' AND ba.inactive = 0 ',
				' UNION ALL ',
                'SELECT ',
                'pb.bank_account_id, UPPER(ba.description) AS description, ba.bank_balance_available, 0 main_bank_account ',
				'FROM ', 
                'person_bank_account pb ',
                'INNER JOIN bank_account ba ON pb.bank_account_id=ba.id ',
				'WHERE ',
                'pb.person_id=', _person_id,
                ' AND pb.inactive = 0 ',
                ' AND ba.inactive = 0 ',
                ') x ',
				'INNER JOIN ( ',
				'	SELECT p.bank_account_id ',
				'	FROM person p INNER JOIN company c ON p.id = c.person_id ',
				'	INNER JOIN user_company uc ON uc.user_id = ', _user_change, ' AND uc.company_id = c.id ',
				'	WHERE c.id NOT IN (1, 182) ',
				'	UNION ALL ',
				'	SELECT pbp.bank_account_id FROM person_bank_account_permission pbp WHERE pbp.user_id = ', _user_change, ' AND pbp.inactive = 0 ',
				') f ON x.bank_account_id = f.bank_account_id ',
                'group by x.bank_account_id, x.description, x.bank_balance_available, x.main_bank_account ',
                'order by x.main_bank_account DESC, x.description'
                );
				-- select @t1;
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
            END; /**End operation 7*/
		ELSEIF(_operation=8) THEN /*Get all bank account of company */
			BEGIN
				SET @t1 = CONCAT(
                'SELECT x.bank_account_id, x.description, x.bank_balance_available, x.main_bank_account ',
                'FROM ',
				'(SELECT ',
				'bank_account_id, UPPER(ba.description) AS description, ba.bank_balance_available, 1 main_bank_account ', 
				'FROM ', 
				'person p ',
				'INNER JOIN bank_account ba ON p.bank_account_id=ba.id ',
				'WHERE p.id=',_person_id, 
				' UNION ALL ',
                'SELECT ',
                'pb.bank_account_id, UPPER(ba.description) AS description, ba.bank_balance_available, 0 main_bank_account ',
				'FROM ', 
                'person_bank_account pb ',
                'INNER JOIN bank_account ba ON pb.bank_account_id=ba.id ',
				'WHERE ',
                'pb.person_id=', _person_id,
                ' AND pb.inactive = 0 ',
                ' AND ba.inactive = 0 ',
                ') x ',
				'INNER JOIN ( ',
				'	SELECT p.bank_account_id ',
				'	FROM person p INNER JOIN company c ON p.id = c.person_id ',
				'	INNER JOIN user_company uc ON uc.user_id = ', _user_change, ' AND uc.company_id = c.id ',
				'	WHERE c.id NOT IN (1, 182) ',
				'	UNION ALL ',
				'	SELECT pbp.bank_account_id FROM person_bank_account_permission pbp WHERE pbp.user_id = ', _user_change, ' AND pbp.inactive = 0 ',
				') f ON x.bank_account_id = f.bank_account_id ',
                'group by x.bank_account_id, x.description, x.bank_balance_available, x.main_bank_account ',
                'order by x.main_bank_account DESC, x.description'
                );
				-- select @t1;
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
            END; /**End operation 8*/            
		END IF; /** End operation */
	END$$