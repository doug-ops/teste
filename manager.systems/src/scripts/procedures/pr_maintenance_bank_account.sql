USE `manager`;
DROP PROCEDURE IF EXISTS `pr_maintenance_bank_account`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_bank_account` (IN _id_account_from INT, 
												IN _id_account_to INT,
												IN _description VARCHAR(100), 
                                                IN _bank_balance_available DECIMAL(19,2), 
                                                IN _bank_limit_available DECIMAL(19,2),
												IN _bank_code VARCHAR(10), 
                                                IN _bank_angency VARCHAR(10),
												IN _inactive TINYINT, 
                                                IN _user_change BIGINT, 
												IN _operation TINYINT,
												IN _bank_account_unbound TINYINT, 
                                                IN _person_id BIGINT,
                                                IN _object_type VARCHAR(3), 
                                                IN _accumulate_balance BIT)
BEGIN
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    
    IF(_operation=1 OR _operation=6) THEN
	BEGIN
		SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
		SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);            
	END; END IF;

	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM bank_account WHERE id=_id_account_from) THEN
			BEGIN
				UPDATE bank_account
					SET
						description = _description, bank_balance_available = _bank_balance_available, bank_limit_available = _bank_limit_available, 
						bank_code = _bank_code, bank_angency = _bank_angency, inactive = _inactive, accumulate_balance = _accumulate_balance, 
                        user_change = _user_change, change_date = _processing_date_time
					WHERE
						id = _id_account_from;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO bank_account
				(
					id, description, bank_balance_available, bank_limit_available, bank_code, bank_angency,
					inactive, accumulate_balance, user_creation, creation_date, user_change, change_date
				)
				SELECT 
					_id_account_from, _description, _bank_balance_available, _bank_limit_available, _bank_code, _bank_angency,
					_inactive, _accumulate_balance, _user_change, _processing_date_time, _user_change, _processing_date_time;
                    
			    CALL pr_maintenance_person_bank_account_permission(1, _id_account_from, _user_change, 0, _user_change);
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE */
		IF (SELECT 1 = 1 FROM bank_account WHERE id=_id_account_from) THEN
			BEGIN
				UPDATE  bank_account SET inactive = _inactive, user_change = _user_change, change_date = _processing_date_time
				WHERE id = _id_account_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		IF (SELECT 1 = 1 FROM bank_account WHERE id=_id_account_from) THEN
			BEGIN
				SELECT
					ba.id, ba.description, ba.bank_balance_available, ba.bank_limit_available, ba.bank_code, ba.bank_angency,
					ba.inactive, ba.accumulate_balance, ba.user_creation, ba.creation_date, ba.user_change, IFNULL(us.name,'Nao encontrado') as user_change_name, ba.change_date
				FROM
					bank_account ba
                    LEFT JOIN user us on ba.user_change=us.id
				WHERE
					ba.id=_id_account_from;
			END;
		END IF; /** End operation 3 */
	ELSEIF(4=_operation) THEN /** GETALL */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			' ba.id, ba.description, ba.bank_balance_available, ba.bank_limit_available, ba.bank_code, ba.bank_angency, ',
			' ba.inactive, ba.accumulate_balance, ba.user_creation, ba.creation_date, ba.user_change, ba.change_date ',
			'FROM ', 
			'bank_account ba ',
			'INNER JOIN (SELECT p.bank_account_id ',
			'FROM person p INNER JOIN company c ON p.id=c.person_id ',
			'INNER JOIN user_company uc ON uc.user_id=', _user_change, ' and uc.company_id=c.id ',
            ' UNION ALL ',
            'SELECT pb.bank_account_id ',
			'FROM person p INNER JOIN company c ON p.id=c.person_id ',
			'INNER JOIN user_company uc ON uc.user_id=', _user_change, ' AND uc.company_id=c.id ',
            'INNER JOIN person_bank_account pb ON p.id = pb.person_id AND pb.inactive = 0 ',
            'GROUP BY bank_account_id'
            ') x ON ba.id = x.bank_account_id ',
			'INNER JOIN ( ',
			'	SELECT p.bank_account_id ',
			'	FROM person p INNER JOIN company c ON p.id = c.person_id ',
			'	INNER JOIN user_company uc ON uc.user_id = ', _user_change, ' AND uc.company_id = c.id ',
			'	WHERE c.id NOT IN (1, 182) ',
			'	UNION ALL ',
			'	SELECT pbp.bank_account_id FROM person_bank_account_permission pbp WHERE pbp.user_id = ', _user_change, ' AND pbp.inactive = 0 ',
			') f ON ba.id = f.bank_account_id ',
			'WHERE 1=1 ',
				IF((_id_account_from>0 AND _id_account_to>0), CONCAT(' AND ba.id between ', _id_account_from, ' and ', _id_account_to),''),
				IF((LENGTH(IFNULL(_description,''))>0), CONCAT(' AND ba.description like ''%', _description, '%'''), ''),
				IF((LENGTH(IFNULL(_bank_code,''))>0), CONCAT(' AND ba.bank_code like ''%', _bank_code, '%'''), ''),
				IF((LENGTH(IFNULL(_bank_angency,''))>0), CONCAT(' AND ba.bank_angency like ''%', _bank_angency, '%'''), ''),
				IF((_inactive IS NOT NULL AND _inactive<2), CONCAT(' AND ba.inactive= ', _inactive), ''),
                ' GROUP BY ba.id, ba.description, ba.bank_balance_available, ba.bank_limit_available, ba.bank_code, ba.bank_angency,  ',
				' ba.inactive, ba.accumulate_balance, ba.user_creation, ba.creation_date, ba.user_change, ba.change_date ',
                ' order by 2;');
                
                -- select @t1;
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
            
		END; /** End operation 4 */
	ELSEIF(5=_operation) THEN
			BEGIN
				SELECT 
					x.id, x.description
                FROM
				(
					SELECT 
						b.id, b.description
					FROM
						bank_account b
						INNER JOIN (SELECT p.bank_account_id, c.id AS company_id, c.person_id
						FROM person p INNER JOIN company c ON p.id=c.person_id
						INNER JOIN user_company uc ON uc.user_id=_user_change
						and uc.company_id=c.id) x ON b.id = x.bank_account_id
					WHERE
						b.inactive=0
					UNION ALL 
					SELECT 
						b.id, b.description
					FROM
						bank_account b
						INNER JOIN (SELECT pb.bank_account_id, c.id AS company_id, c.person_id
						FROM company c
						INNER JOIN user_company uc ON uc.user_id=_user_change
						and uc.company_id=c.id
						INNER JOIN person_bank_account pb ON c.person_id = pb.person_id and pb.inactive = 0) x ON b.id = x.bank_account_id
					WHERE
						b.inactive=0 
				) x
				INNER JOIN ( 
					SELECT p.bank_account_id 
					FROM person p INNER JOIN company c ON p.id = c.person_id 
					INNER JOIN user_company uc ON uc.user_id = _user_change AND uc.company_id = c.id 
					WHERE c.id NOT IN (1, 182) 
					UNION ALL 
					SELECT pbp.bank_account_id FROM person_bank_account_permission pbp WHERE pbp.user_id = _user_change AND pbp.inactive = 0 
				) f ON x.id = f.bank_account_id 
                GROUP BY x.id, x.description
				ORDER BY 
					x.description;
            END; /**End operation 5*/
	ELSEIF(7=_operation) THEN /** Autocomplete operation 7 */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			' ba.id, ba.description ',
			'FROM ', 
			'bank_account ba ',
   			'INNER JOIN ( ',
            'SELECT distinct y.bank_account_id FROM ( ',
            'SELECT p.bank_account_id ',
			'FROM person p INNER JOIN company c ON p.id=c.person_id ',
			'INNER JOIN user_company uc ON uc.user_id=', _user_change, 
            ' and uc.company_id=c.id ', 
            'UNION ALL ',
            'SELECT b.id AS bank_account_id FROM bank_account b LEFT JOIN person p ON b.id = p.bank_account_id WHERE ',
            CONCAT(' b.description like ''%', _description, '%'''),
            'AND p.bank_account_id IS NULL ',
            ') y ',
            ') x ON ba.id = x.bank_account_id ',
   			IF((_bank_account_unbound>0), CONCAT(' LEFT JOIN person pe ON ba.id=pe.bank_account_id '),''),
            IF((_bank_account_unbound>0 AND 'COM'=IFNULL(_object_type,'')), CONCAT(' LEFT JOIN company co ON pe.id=co.person_id '),''),
   			IF((_bank_account_unbound>0), CONCAT(' LEFT JOIN person_bank_account pb ON ba.id=pb.bank_account_id AND pb.inactive=0 '),''),
            'WHERE ',
			CONCAT(' ba.description like ''%', _description, '%'''),
			CONCAT(' AND ba.inactive= ', _inactive),
            IF((_bank_account_unbound=1 AND _person_id=0), CONCAT(' AND pe.id IS NULL AND pb.person_id IS NULL '),''),
            IF((_bank_account_unbound=1 AND _person_id>0 AND 'COM'=IFNULL(_object_type,'')), CONCAT(' AND (pe.id IS NULL OR co.id=',_person_id,') AND (pb.person_id IS NULL OR pb.person_id=','(SELECT person_id FROM company WHERE id=',_person_id,'))'),''),
            ' ORDER BY ba.description;');
            
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;    		
		END; /**End Autocomplete operation 7*/
	ELSEIF(8=_operation) THEN /** Autocomplete operation 8 */
		BEGIN
			SET @t1 = CONCAT('SELECT ',
			' ba.id, ba.description ',
			'FROM ', 
			'bank_account ba ',
   			'INNER JOIN ( ',
            'SELECT distinct y.bank_account_id FROM ( ',
            'SELECT p.bank_account_id ',
			'FROM person p INNER JOIN company c ON p.id=c.person_id ',
			'INNER JOIN user_company uc ON uc.user_id=', _user_change, 
            ' and uc.company_id=c.id ', 
            'UNION ALL ',
            'SELECT b.id AS bank_account_id FROM bank_account b LEFT JOIN person p ON b.id = p.bank_account_id WHERE ',
            CONCAT(' b.description like ''%', _description, '%'''),
            'AND p.bank_account_id IS NULL ',
            ') y ',
            ') x ON ba.id = x.bank_account_id ',
            'WHERE ',
			CONCAT(' ba.description like ''%', _description, '%'''),
			CONCAT(' AND ba.inactive= ', _inactive),
            ' ORDER BY ba.description;');
            
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;    		
		END; /**End Autocomplete operation 8*/        
	ELSEIF(99=_operation) THEN
			BEGIN
				SELECT 
					distinct bank_account_id
				FROM
					bank_account_out_document_parent
				WHERE
					inactive=0;
            END; /**End operation 5*/
	END IF;
END$$



CALL pr_maintenance_bank_account('0', '0', '', null, null, null, null, 0, 46, 4, 0, 0, '', null  )