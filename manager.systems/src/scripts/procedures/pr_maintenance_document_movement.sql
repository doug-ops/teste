USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_document_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_document_movement`(IN _operation INT,
													IN _document_id BIGINT,
													IN _document_parent_id BIGINT,
                                                    IN _document_type INT,
													IN _credit BIT,
													IN _company_id BIGINT,
													IN _provider_id BIGINT,
                                                    IN _bank_account_id INT,
                                                    IN _document_number VARCHAR(20), 
                                                    IN _document_value DECIMAL(19,4), 
                                                    IN _document_note VARCHAR(1000), 
                                                    IN _document_status INT, 
                                                    IN _payment_value DECIMAL(19,4), 
                                                    IN _payment_discount DECIMAL(19,4),
													IN _payment_extra  DECIMAL(19,4), 
                                                    IN _payment_residue BIT, 
                                                    IN _payment_data DATETIME, 
                                                    IN _payment_expiry_data DATETIME, 
                                                    IN _payment_user BIGINT, 
                                                    IN _nfe_number BIGINT, 
                                                    IN _payment_status INT, 
                                                    IN _generate_billet BIT, 
                                                    IN _financial_group_id VARCHAR(7),
													IN _financial_sub_group_id VARCHAR(7),
													IN _inactive BIT,
                                                    IN _user_change BIGINT,
                                                    IN _creation_date DATETIME,
                                                    IN _filter_data_type INT, 
                                                    IN _moviment_type_credit BIT, 
                                                    IN _moviment_type_debit BIT, 
                                                    IN _moviment_type_nfe BIT,
                                                    IN _document_status_filter VARCHAR(50), 
                                                    IN _bank_account_origin_id INT,
                                                    IN _financial_cost_center_id INT,
                                                    IN _totalDebitOrigin DECIMAL(19,4),
                                                    IN _document_parent_id_origin BIGINT,
													IN _document_id_origin BIGINT,
                                                    IN _document_transaction_id BIGINT)
BEGIN	
	DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
	DECLARE _message VARCHAR(2000);
    
	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
	IF(1=_operation) THEN
		BEGIN
			INSERT INTO document_generate_id(inactive) select 0;
			SET _document_id = LAST_INSERT_ID();                
			INSERT INTO document_parent_generate_id(inactive) select 0;
			SET _document_parent_id = LAST_INSERT_ID(); 
            
            IF(_document_note IS NULL) THEN
				IF(_credit = 1) THEN 
					set _document_note = CONCAT('TRANSF. CONTA: ', (select description from bank_account where id = _bank_account_origin_id limit 1), '(', CAST(_bank_account_origin_id AS CHAR), ')', ' >> CONTA: ', (select description from bank_account where id = _bank_account_id limit 1), '(', CAST(_bank_account_id AS CHAR), ')', ' >> MOV: ', CAST(_document_parent_id_origin AS CHAR), (CASE WHEN LENGTH(IFNULL(_document_note,'')) > 0 THEN CONCAT(' >> OBS: ', _document_note) ELSE '' END));
				ELSE
					set _document_note = CONCAT('TRANSF. CONTA: ', (select description from bank_account where id = _bank_account_id limit 1), '(', CAST(_bank_account_id AS CHAR), ')', ' >> CONTA: ', (select description from bank_account where id = _bank_account_origin_id limit 1), '(', CAST(_bank_account_origin_id AS CHAR), ')', ' >> MOV: ', CAST(_document_parent_id_origin AS CHAR), (CASE WHEN LENGTH(IFNULL(_document_note,'')) > 0 THEN CONCAT(' >> OBS: ', _document_note) ELSE '' END));
				END IF;
            END IF;
            
			INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, 
			document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
			payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
			inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id, document_parent_id_origin, document_id_origin, document_transaction_id) 
			select _document_id, _document_parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, _bank_account_origin_id,
			_document_number, _payment_value, _document_note, _document_status, _payment_value, _payment_discount, 
			_payment_extra, _payment_residue, _payment_data, _payment_expiry_data, _payment_user, _nfe_number, 
			_payment_status, _generate_billet, _financial_group_id, _financial_sub_group_id,
			_inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _financial_cost_center_id, 
            _document_parent_id_origin, _document_id_origin, _document_transaction_id;
            
            /**
            -- if value Document > value payment, create Documente residual debit to destiny
			 IF(_document_value - (_payment_value + _totalDebitOrigin) <> 0) THEN  

				INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();                
				INSERT INTO document_parent_generate_id(inactive) select 0;
				SET _document_parent_id = LAST_INSERT_ID(); 
				
				SET _message = CONCAT('RESIDUAL TRASNF.', CAST(_document_id AS CHAR),' >> EMPRESA: ',CAST(_company_id AS CHAR));

				INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_origin_id, bank_account_id,
				document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
				payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
				inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id, document_transaction_id)
				select _document_id, _document_parent_id, _document_type, (case when (_document_value - _payment_value > 0) then 0 else 1 end) credit, _company_id, _provider_id, _bank_account_origin_id, _bank_account_id,
				_document_number, (_document_value-_payment_value) payment_value, _message, 1 document_status, (_document_value-_payment_value) payment_value, _payment_discount, 
				_payment_extra, 1 payment_residue, _payment_data, _payment_expiry_data, _payment_user, _nfe_number, 
				0 payment_status, _generate_billet, _financial_group_id, _financial_sub_group_id,
				_inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _financial_cost_center_id, _document_transaction_id;                 
			END IF;
             */
			IF(2 = _document_status) THEN
				BEGIN
					/**Update bank_balance_available from document value */
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) as movement_balance, _bank_account_id AS bank_account_id) x ON ba.id = x.bank_account_id
					SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),
                    user_change = _user_change,
                    change_date = _processing_date_time
                    where ba.accumulate_balance = 1;
				   
					/**Insert bank account statement*/
				   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, bank_balance_available, document_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
				   SELECT 
				   (SELECT person_id FROM company WHERE id = _company_id) person_id,
				   _bank_account_id, _bank_account_origin_id, _provider_id, _document_type, _document_parent_id, _document_id, 0 bank_balance_available, IFNULL(CASE WHEN _credit=1 THEN _payment_value ELSE (_payment_value*-1) END,0) AS movement_balance,
				   _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_data;    
				END; 
			END IF;  
            
            update document_movement set is_document_transfer = 1 where document_parent_id = _document_parent_id_origin and document_id = (case when _document_id_origin > 0 then _document_id_origin else document_id end);
		END;
	ELSEIF(2=_operation) THEN
		/** Verify exists document and document group */
		IF (SELECT 1 = 1 FROM document_movement WHERE document_parent_id = _document_parent_id AND document_id = _document_id LIMIT 1) THEN
			BEGIN       
				SELECT
             		document_transaction_id, company_id, document_status, document_type, document_value, bank_account_id, bank_account_origin_id, provider_id, credit, document_note, inactive, payment_data 
				INTO _document_transaction_id, _company_id, _document_status, _document_type, _document_value, _bank_account_id, _bank_account_origin_id, _provider_id, _credit, 
                     _document_note,  _inactive, _payment_data
				FROM document_movement WHERE document_parent_id = _document_parent_id AND document_id = _document_id LIMIT 1;
                
				/**Update document movement inactive */
                UPDATE 
					document_movement 
				SET inactive = (CASE WHEN _inactive = 1 THEN 0 ELSE 1 END), user_change = _user_change, change_date = _processing_date_time
				WHERE document_parent_id = _document_parent_id AND document_id = _document_id;   
                
                /**Verify document status*/
				IF(2 = _document_status) THEN /**If document close, return movement and create extrato operation*/
					BEGIN  
                    
					   /**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						SET ba.bank_balance_available = (ba.bank_balance_available + (CASE WHEN (_inactive = 1 AND _credit = 0) THEN (_document_value * -1)  WHEN (_inactive = 1 AND _credit = 1) THEN _document_value WHEN (_inactive = 0 AND _credit = 0) THEN _document_value  ELSE (_document_value * -1) END)),
                        user_change = _user_change, change_date = _processing_date_time
						WHERE ba.id = _bank_account_id AND ba.accumulate_balance = 1;
                        
                        INSERT INTO bank_account_statement(bank_account_id, bank_account_origin_id, launch_type, provider_id, person_id, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)
                        SELECT _bank_account_id, _bank_account_origin_id, _document_type, _provider_id, 
                        (SELECT person_id FROM company WHERE id = _company_id 	LIMIT 1) person_id, 
                        _document_parent_id, _document_id, 
                        (CASE WHEN (_inactive = 1 AND _credit = 0) THEN (_document_value * -1)  WHEN (_inactive = 1 AND _credit = 1) THEN _document_value WHEN (_inactive = 0 AND _credit = 0) THEN _document_value  ELSE (_document_value * -1) END), 
                        CONCAT('EXTORNO >> ', _document_note), 
                         0 bank_balance_available, 0 document_inactive, _user_change, _processing_date_time, _user_change, _payment_data;
                         
						END;
					END IF;
				END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
		SELECT 
			document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
            document_number, document_value, document_note, document_status, payment_value, 
            payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, 
            payment_user, nfe_number, nfe_serie_number, generate_billet, financial_group_id, 
            financial_sub_group_id, inactive, user_creation, creation_date, user_change, change_date
		FROM
			document_movement
		WHERE 
			document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, ', 
						 'document_number, document_value, document_note, document_status, payment_value, ', 
						 'payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, ', 
						 'payment_user, nfe_number, nfe_serie_number, generate_billet, financial_group_id, ', 
						 'financial_sub_group_id, inactive, user_creation, creation_date, user_change, change_date', 
						 'FROM document_movement ',
                         ' WHERE 1=1 ', 
                         IF((_company_id>0), concat(' AND company_id=', _company_id), ''), 
                         IF((_provider_id>0), concat(' AND provider_id=', _provider_id), ''), 
                         IF((_financial_group_id>0), concat(' AND financial_group_id=', _financial_group_id), ''), 
                         IF((_financial_sub_group_id>0), concat(' AND financial_sub_group_id=', _financial_sub_group_id), ''), 
                         IF((_bank_account_id>0), concat(' AND bank_account_id=', _bank_account_id), ''), 
                         IF((_document_type>0), concat(' AND document_type=', _document_type), ''), 
                         IF((_document_status>0), concat(' AND document_status=', _document_status), ''), 
						 IF((length(IFNULL(_document_number,''))>0), concat(' AND document_number like ''%', _document_number, '%'''), ''),
                         IF((_filter_data_type=1 AND _creation_date != NULL), concat(' AND creation_date=''',DATE_FORMAT(_creation_date, '%Y-%m-%d %H:%i:%s'),''), ''), 
                         IF((_filter_data_type=2 AND _payment_data != NULL), concat(' AND payment_data=''',DATE_FORMAT(_payment_data, '%Y-%m-%d %H:%i:%s'),''), ''), 
                         IF((_filter_data_type=3 AND _payment_expiry_data != NULL), concat(' AND payment_expiry_data=''',DATE_FORMAT(_payment_expiry_data, '%Y-%m-%d %H:%i:%s'),''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND inactive=', _inactive), ''),
                         IF((_moviment_type_credit=1 AND _moviment_type_debit=0), concat(' AND credit=', 1), ''),
                         IF((_moviment_type_credit=0 AND _moviment_type_debit=1), concat(' AND credit=', 0), ''),
                         IF((_moviment_type_nfe=1), concat(' AND IFNULL(nfe_number,0)>', 0), ''),
                         IF((length(IFNULL(_document_status_filter,''))>0), concat(' AND document_status in (',_document_status_filter, ''), ''),                         
                         ';');	
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;	
	ELSEIF(8=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				UPDATE document_movement SET document_parent_id=0, user_change=_user_change, change_date = _processing_date_time
				WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
    ELSEIF(9=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				UPDATE document_movement SET document_status=_document_status, user_change=_user_change, change_date = _processing_date_time
				WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(15=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				DECLARE _bank_account_id_older INT;
                DECLARE _document_value_older DECIMAL(19,4);
                DECLARE _credit_older BIT;
                DECLARE _document_status_older INT;
                DECLARE _is_change_bank_account BIT;
                DECLARE _is_change_document_value BIT;
                DECLARE _is_change_credit BIT;
                DECLARE _is_change_document_status BIT;
                DECLARE _count_alter_balance TINYINT DEFAULT 0;
                
                SELECT bank_account_id, document_value, credit, document_status INTO _bank_account_id_older, _document_value_older, _credit_older, _document_status_older FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type;
            
				SELECT (CASE WHEN _bank_account_id_older<>_bank_account_id THEN 1 ELSE 0 END), (CASE WHEN _bank_account_id_older<>_bank_account_id THEN 1 ELSE _count_alter_balance END) INTO _is_change_bank_account, _count_alter_balance;
                SELECT (CASE WHEN _document_value_older<>_document_value THEN 1 ELSE 0 END), (CASE WHEN _document_value_older<>_document_value THEN (_count_alter_balance+1)  ELSE _count_alter_balance END) INTO _is_change_document_value, _count_alter_balance;
                SELECT (CASE WHEN IFNULL(_credit_older,0)<>IFNULL(_credit,0) THEN 1 ELSE 0 END), (CASE WHEN IFNULL(_credit_older,0)<>IFNULL(_credit,0) THEN (_count_alter_balance+1) ELSE _count_alter_balance END) INTO _is_change_credit, _count_alter_balance;
                SELECT (CASE WHEN _document_status_older<>_document_status THEN 1 ELSE 0 END), (CASE WHEN _document_status_older<>_document_status THEN (_count_alter_balance+1) ELSE _count_alter_balance END) INTO _is_change_document_status, _count_alter_balance;
                IF(_count_alter_balance>1) THEN
					BEGIN
						SIGNAL SQLSTATE '45000' 
						SET MESSAGE_TEXT = 'Não é possível alterar os campos Conta Bancrária, Tipo Documento, Status Documento e Valor do Documento ao mesmo tempo.';
                    END; END IF;
                
				IF(_is_change_bank_account OR _is_change_document_value OR _is_change_credit) THEN
					/**return bank balance older value*/
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT IFNULL(CASE WHEN _credit_older=1 THEN (_document_value_older*-1) ELSE _document_value_older END,0) AS movement_balance, _bank_account_id_older AS bank_account_id) x ON ba.id=x.bank_account_id
					SET ba.bank_balance_available = (ba.bank_balance_available+x.movement_balance),
                    user_change = _user_change,
					change_date = _processing_date_time
                    WHERE  ba.accumulate_balance = 1;
					/**Insert bank statement*/ 
				
					INSERT INTO bank_account_statement(person_id, provider_id, launch_type, document_id, document_parent_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  					
                    SELECT
                    (SELECT person_id FROM company WHERE id=_company_id) person_id,
                    _provider_id, _document_type, _document_id, _document_parent_id, _document_value_older, 
					CONCAT('Extorno com alteração de documento:', CAST(_document_id AS CHAR),
					IF((_is_change_bank_account), CONCAT(' Conta: ', _bank_account_id_older, ' para: ', _bank_account_id), ''),
					IF((_is_change_document_value), CONCAT(' Valor: ', _document_value_older, ' para: ', _document_value), ''),
					IF((_is_change_credit), CONCAT(' Credito: ', IFNULL(_credit_older,0), ' para: ', IFNULL(_credit,0)), ''), 
					IF((_is_change_document_status), CONCAT(' Status Documento: ', IFNULL(_document_status_older,0), ' para: ', _document_status), '')), 0, _user_change, _processing_date_time, _user_change, _processing_date_time;
                   
					/**change bank balance to new value*/
					UPDATE bank_account ba 
					INNER JOIN 
					(SELECT IFNULL(CASE WHEN _credit THEN _document_value ELSE (_document_value*-1) END,0) AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
					SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),
					user_change = _user_change,
					change_date = _processing_date_time
                    WHERE  ba.accumulate_balance = 1;   
                    				   
					/**Insert bank statement*/
					INSERT INTO bank_account_statement(person_id, provider_id, launch_type, document_id, bank_account_id, document_parent_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
					
					SELECT 
                    (SELECT person_id FROM company WHERE id=_company_id), _provider_id, _document_type, _document_id,
                    _bank_account_id, _document_parent_id, _document_value, 
					CONCAT('Lançamento alteração de documento:', CAST(_document_id AS CHAR),
					IF((_is_change_bank_account), CONCAT(' Conta: ', _bank_account_id_older, ' para: ', _bank_account_id), ''),
					IF((_is_change_document_value), CONCAT(' Valor: ', _document_value_older, ' para: ', _document_value), ''),
					IF((_is_change_credit), CONCAT(' Credito: ', IFNULL(_credit_older,0), ' para: ', IFNULL(_credit,0)), ''), 
					IF((_is_change_document_status), CONCAT(' Status Documento: ', IFNULL(_document_status_older,0), ' para: ', _document_status), '')), 0, _user_change, _processing_date_time, _user_change, _processing_date_time;
                ELSEIF(_is_change_document_status) THEN
					IF(2=_document_status) THEN
						/**change bank balance to new value*/
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit THEN _document_value ELSE (_document_value*-1) END,0) AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),
                        user_change = _user_change,
						change_date = _processing_date_time
                        WHERE  ba.accumulate_balance = 1;   
                        
						/**Insert bank statement*/
                       
						INSERT INTO bank_account_statement(person_id, provider_id, launch_type, bank_account_id, document_id, document_parent_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
						SELECT 
                        (SELECT person_id FROM company WHERE id=_company_id),
                        _provider_id, _document_type, _bank_account_id, _document_id, _document_parent_id, _document_value, 
						CONCAT('Lançamento com alteração de documento:', CAST(_document_id AS CHAR),
						IF((_is_change_bank_account), CONCAT(' Conta: ', _bank_account_id_older, ' para: ', _bank_account_id), ''),
						IF((_is_change_document_value), CONCAT(' Valor: ', _document_value_older, ' para: ', _document_value), ''),
						IF((_is_change_credit), CONCAT(' Credito: ', IFNULL(_credit_older,0), ' para: ', IFNULL(_credit,0)), ''), 
						IF((_is_change_document_status), CONCAT(' Status Documento: ', _document_status_older, ' para: ', _document_status), '')), 0, _user_change, _processing_date_time, _user_change, _processing_date_time;
                    ELSEIF(2=_document_status_older) THEN
						/**change bank balance to new value*/
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit THEN (_document_value*-1) ELSE _document_value END,0) AS movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),
                        user_change = _user_change,
						change_date = _processing_date_time
                        WHERE  ba.accumulate_balance = 1;   
                        
						/**Insert bank statement*/
						INSERT INTO bank_account_statement(person_id, provider_id, launch_type, bank_account_id, document_id, document_parent_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
						SELECT 
                        (SELECT person_id FROM company WHERE id=_company_id),
                        _provider_id, _document_type, _bank_account_id, _document_id, _document_parent_id, _document_value, 
						CONCAT('Extorno com alteração de documento:', CAST(_document_id AS CHAR),
						IF((_is_change_bank_account), CONCAT(' Conta: ', _bank_account_id_older, ' para: ', _bank_account_id), ''),
						IF((_is_change_document_value), CONCAT(' Valor: ', _document_value_older, ' para: ', _document_value), ''),
						IF((_is_change_credit), CONCAT(' Credito: ', IFNULL(_credit_older,0), ' para: ', IFNULL(_credit,0)), ''), 
						IF((_is_change_document_status), CONCAT(' Status Documento: ', _document_status_older, ' para: ', _document_status), '')), 0, _user_change, _processing_date_time, _user_change, _processing_date_time;
                    END IF;
			END IF;
			
				UPDATE document_movement SET 					
					-- credit=_credit, 
                    company_id=_company_id, 
                    provider_id=_provider_id,
					bank_account_id=_bank_account_id, 
                    document_number=_document_number, 
					document_value=_document_value, 
                    document_note=_document_note, 
					document_status=_document_status, 
                    payment_value=_document_value, 
                    payment_status=_payment_status,
					payment_data=_payment_data, 
					payment_expiry_data=_payment_expiry_data, 
                    financial_group_id=_financial_group_id,
					financial_sub_group_id=_financial_sub_group_id,
                    user_change=_user_change, 
                    change_date=_processing_date_time
				WHERE
					document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(16=_operation) THEN
		IF (IFNULL(_document_parent_id,0)>0) THEN
			BEGIN
				DROP TEMPORARY TABLE IF EXISTS tmp_movement_values;
				DROP TEMPORARY TABLE IF EXISTS tmp_movement_filter;				
                /**Temporary table to insert tmp_movement_values*/
				CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_values
				(
					txt text
				);
				/**End Temporary table to insert tmp_movement_values*/
				
				/**Temporary table to insert bank_account_filter*/
				CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_filter
				(
					movment_id BIGINT NOT NULL
				);
				/**End Temporary table to insert bank_account_filter*/
				
				INSERT INTO tmp_movement_values VALUES(_document_note);
				SET @sql = concat("INSERT INTO tmp_movement_filter (movment_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_movement_values), ",", "'),('"),"');");
				PREPARE stmt1 FROM @sql;
				EXECUTE stmt1;

				UPDATE document_movement SET document_parent_id=_document_parent_id, user_change=_user_change, change_date = _processing_date_time
				WHERE document_parent_id in (SELECT movment_id FROM tmp_movement_filter);
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Agrupamento principal não informado.';
			END;
		END IF;
	ELSEIF(28=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Número de documento informado já existe.';
            END;
		ELSE 
			BEGIN
                INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();                
                INSERT INTO document_parent_generate_id(inactive) select 0;
				SET _document_parent_id = LAST_INSERT_ID();
                  
				/**Company_id is null */
				IF(IFNULL(_company_id,0)=0) THEN
					SELECT c.id INTO _company_id FROM person p
					INNER JOIN company c ON p.id=c.person_id
					WHERE p.bank_account_id=_bank_account_id LIMIT 1;
				END IF;
                        
				INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, 
                document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
                payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
                inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id, document_transaction_id)
                select _document_id, _document_parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, _bank_account_origin_id,
                _document_number, _document_value, _document_note, _document_status, _document_value, _payment_discount, 
                _payment_extra, _payment_residue, _payment_data, _payment_expiry_data, _payment_user, _nfe_number, 
                _payment_status, _generate_billet, _financial_group_id, _financial_sub_group_id,
                _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _financial_cost_center_id, _document_transaction_id;
                
                IF(2=_document_status) THEN
					BEGIN
						/**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit=1 THEN _document_value ELSE (_document_value*-1) END,0) as movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),  
                        user_change = _user_change,
						change_date = _processing_date_time
						WHERE  ba.accumulate_balance = 1;

						/**Insert bank account statement*/
					   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, bank_balance_available, document_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
					   SELECT 
                       (SELECT person_id FROM company WHERE id=_company_id) person_id,
                       _bank_account_id, _bank_account_origin_id, _provider_id, _document_type, _document_parent_id, _document_id, 0 bank_balance_available, IFNULL(CASE WHEN _credit=1 THEN _document_value ELSE (_document_value*-1) END,0) AS movement_balance,
					   _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_data;
				END; END IF;   
            END;
		END IF;
	ELSEIF(29=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id AND document_type=_document_type) THEN
			BEGIN
				SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Número de documento informado já existe.';
            END;
		ELSE 
			BEGIN
                INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID();                
                INSERT INTO document_parent_generate_id(inactive) select 0;
				SET _document_parent_id = LAST_INSERT_ID();
                  
				INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, 
                document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
                payment_data, payment_expiry_data, payment_user, nfe_number, payment_status, generate_billet, financial_group_id, financial_sub_group_id, 
                inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id, document_transaction_id)
                select _document_id, _document_parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, _bank_account_origin_id,
                _document_number, _document_value, _document_note, _document_status, _document_value, _payment_discount, 
                _payment_extra, _payment_residue, _payment_data, _payment_expiry_data, _payment_user, _nfe_number, 
                _payment_status, _generate_billet, _financial_group_id, _financial_sub_group_id,
                _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time, _financial_cost_center_id, _document_transaction_id;
                
                IF(2=_document_status) THEN
					BEGIN
						/**Update bank_balance_available from document value */
						UPDATE bank_account ba 
						INNER JOIN 
						(SELECT IFNULL(CASE WHEN _credit=1 THEN _document_value ELSE (_document_value*-1) END,0) as movement_balance, _bank_account_id AS bank_account_id) x ON ba.id=x.bank_account_id
						SET ba.bank_balance_available=(ba.bank_balance_available+x.movement_balance),
                        user_change = _user_change,
						change_date = _processing_date_time
						WHERE  ba.accumulate_balance = 1;

						/**Insert bank account statement*/
					   INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, bank_balance_available, document_id, document_value, document_note, inactive, user_creation, creation_date, user_change, change_date)  
					   SELECT 
                       (SELECT person_id FROM company WHERE id=_company_id) person_id,
                       _bank_account_id, _bank_account_origin_id, _provider_id, _document_type, _document_parent_id, _document_id, 0 bank_balance_available, IFNULL(CASE WHEN _credit=1 THEN _document_value ELSE (_document_value*-1) END,0) AS movement_balance,
					   _document_note, 0, _user_change, _processing_date_time, _user_change, _payment_data;
                       
				END; END IF;   
            END;
		END IF;
    END IF;
END$$
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
    8 - ungroup operation
    9 - changeStatus operation
*/

/*
START TRANSACTION;
SET SQL_SAFE_UPDATES = 0;

CALL pr_maintenance_document_movement(1, 0, 0, 2, 1, 182, 3837, 446, '202306172202513', 135798.8, 'TRANFERENCIA: EMP: RGB COFRE CAIXA (182) >> CONTA: RGB COFRE CAIXA (199) >>  % 40,00 >> CONTA: WHISKI >>  FORN: WHISKI', 2, 135798.8, 0.0, 0.0, 0, '2023-06-21 00:00:00', '2023-06-21 00:00:00', 0, null, 2, 0, '14.000', '14.007', 0, 0, null, null, null, null, null, null, 199, 0, 0.0, 0, 0  )

select * from bank_account_statement where provider_id = 3837;
ROLLBACK;
*/