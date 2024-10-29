USE `manager`;
DROP procedure IF EXISTS `pr_process_document_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_document_movement`(IN _user BIGINT, IN _company_id BIGINT, IN _is_offline BIT, IN _movement_ids VARCHAR(1000), IN _is_preview BIT)
	BEGIN    
		/**Create global variables*/
		DECLARE _is_enabled_process_movements BIT;
		DECLARE _timezone_database TINYINT DEFAULT 0;
		DECLARE _processing_date_time DATETIME DEFAULT NOW();
		DECLARE _validation_message VARCHAR(5000) DEFAULT '';
		DECLARE _step_process TINYINT DEFAULT 1;
        DECLARE _document_parent_id BIGINT;
        DECLARE _document_parent_id_generated BIGINT;
        DECLARE _negative_close BIT;
        DECLARE _movement_total DECIMAL(19,2);
        DECLARE _movement_balance DECIMAL(19,2);
        DECLARE _total_movement DECIMAL(19,2);
        DECLARE _bank_account_company INT DEFAULT 0;
        DECLARE _payment_data DATETIME;
        DECLARE _count_day_last_movement INT;
        DECLARE _person_id BIGINT;
        DECLARE _bank_account_automatic_transfer_id INT;
		DECLARE _bank_account_origin_id INT;
        DECLARE _company_description VARCHAR(100);
        DECLARE _count_validate INT DEFAULT 0;
        DECLARE _count_week INT DEFAULT 1;
        DECLARE _counter INT DEFAULT 1;
        DECLARE _has_transf_hab TINYINT DEFAULT 0; 
        DECLARE _client_id TINYINT DEFAULT 0; 
        
		/**End Create global variables*/
        
		/**Drop temporary tables*/
		DROP TEMPORARY TABLE IF EXISTS tmp_document_movement;
		DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_filter;
        DROP TEMPORARY TABLE IF EXISTS tmp_bank_account_filter2;
		DROP TEMPORARY TABLE IF EXISTS tmp_financial_transfer;        
        DROP TEMPORARY TABLE IF EXISTS tmp_movement_values;
        DROP TEMPORARY TABLE IF EXISTS tmp_movement_filter;
        DROP TEMPORARY TABLE IF EXISTS tmp_movement_product;
		/**End Drop temporary tables*/

		SELECT 
    IFNULL(process_movements, 0), IFNULL(timezone_database, 0)
INTO _is_enabled_process_movements , _timezone_database FROM
    config_systems
LIMIT 1;
		SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
		/**End Set global variables */    
        
        /**Verify if process is enabled*/
		IF(0=_is_offline AND 0=_is_enabled_process_movements) THEN
			BEGIN
				SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Processamento de movimentos inativo.';
			END;
		END IF; /**End if process movements is active */
        
        /**Temporary table to insert documents*/
		CREATE TEMPORARY TABLE IF NOT EXISTS tmp_document_movement
		(
			execution_order INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
			movement_id BIGINT NOT NULL,
			is_product_movement BIT NOT NULL,
            movement_error BIT NOT NULL,
			document_id BIGINT NOT NULL,
			document_parent_id BIGINT NOT NULL,
			document_type INT NOT NULL,
			credit BIT NOT NULL,
			company_id BIGINT NULL,
			provider_id BIGINT NULL,
			bank_account_id INT NULL,
			document_value DECIMAL(19,4) NULL,
			document_note VARCHAR(1000) CHARACTER SET utf8 NULL,
			document_status INT NOT NULL,
			payment_residue BIT NOT NULL,
			payment_data DATETIME NULL,
			financial_group_id VARCHAR(7) NULL,
			financial_sub_group_id VARCHAR(7) NULL, 
            credit_initial BIGINT, 
            credit_final BIGINT,
            product_id BIGINT,
            group_id INT, 
            product_description VARCHAR(100) NULL,
            group_execution_order TINYINT NULL,
            group_item_execution_order TINYINT NULL,
            creation_date DATETIME NULL,
            transfer_hab  NUMERIC(6,2),
            is_clock_movement BIT NULL,
            product_group_id INT NULL,
            product_sub_group_id INT NULL
		);
		/**End Temporary table to insert documents*/
        
        /**Temporary table to insert bank_account_filter*/
		CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_filter
		(
			bank_account_origin_id INT NOT NULL,
			bank_account_destiny_id INT NOT NULL, 
            company_id BIGINT NOT NULL,
            document_parent_id BIGINT NOT NULL
		);
        
        /**for payment_residual*/
		CREATE TEMPORARY TABLE IF NOT EXISTS tmp_bank_account_filter2
		(
			bank_account_origin_id INT NOT NULL,
			bank_account_destiny_id INT NOT NULL, 
            company_id BIGINT NOT NULL,
            document_parent_id BIGINT NOT NULL
		);
		/**End Temporary table to insert bank_account_filter*/
        
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
        
		/**Temporary table to insert tmp_movement_product*/
		CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement_product
		(
            product_id BIGINT NOT NULL
		);
		/**End Temporary table to insert tmp_movement_product*/
        
		INSERT INTO tmp_movement_values VALUES(_movement_ids);
		SET @sql = concat("INSERT INTO tmp_movement_filter (movment_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_movement_values), ",", "'),('"),"');");
		PREPARE stmt1 FROM @sql;
		EXECUTE stmt1;
                
        /** TODO colocar dados de loja nessa tabela */
        INSERT INTO tmp_movement_product
        SELECT pm.product_id FROM product_movement pm INNER JOIN tmp_movement_filter tmf ON pm.id=tmf.movment_id GROUP BY pm.product_id;
        
        
				/** VERIFICA SE TODOS OS PRODUTOS CONFIGURADOS NA LOJA ESTÃO NA CONFIGURACAO DE TRANSFERENCIA */
                
                /**
				SELECT 
					count(*) qtde INTO _count_validate 
                FROM 
                (
					SELECT 
						cof.company_id, cof.bank_account_origin_id, po.id product_id
                    FROM
					(
						SELECT 
							co.id company_id, pe.bank_account_id bank_account_origin_id
						FROM 
							product_movement pm
							INNER JOIN tmp_movement_filter mf ON pm.id=mf.movment_id
							INNER JOIN company co ON (CASE WHEN 1=_is_offline THEN pm.company_id=co.id ELSE pm.company_description=co.social_name END)
                            INNER JOIN person pe ON co.person_id=pe.id 
						GROUP BY 
							co.id, pe.bank_account_id
                    ) cof     
					INNER JOIN product po ON cof.company_id=po.company_id					
                ) cp
                LEFT JOIN financial_transfer_product_setting ps ON  cp.bank_account_origin_id = ps.bank_account_origin_id
                AND cp.product_id = ps.product_id AND ifnull(ps.inactive,0)=0
                WHERE ps.product_id IS NULL;
                
                IF(_count_validate > 0) THEN 
					SET _validation_message = CONCAT('Existe(m) produto(s) configurado(s) na loja sem configuração de transfêrencia!');
                    CALL pr_send_email_erros(1, null, 'CONFIGURAÇÃO DE TRANSFÊRENCIA',CONCAT(_validation_message,'\n','CODIGO DA LOJA: ',_company_id,',\n',
					'OFFLINE: ', CASE WHEN _is_offline = 0 then 'Não' ELSE 'Sim' END,',\n', 'ID(s) MOVEMENTO(s) PROCESSADO(s): ',_movement_ids,',\n',
                    'USUÁRIO: ',_user,',\n', 'ORIGEM DO ERRO: ', CASE WHEN _is_preview = 0 then 'Processa movimento produto' ELSE 'Preview movimento produto' END),
                    'douglinhas.mariano@gmail.com', now(), 0, now(), 0);
                   
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message;                
				END IF; 
                */
            
                SET _count_validate = 0;
				/** VERIFICA SE TODOS OS PRODUTOS DA CONFIGURACAO DE TRANSFERENCIA ESTÃO LANÇADOS */
                /**
				SELECT 
					count(*) qtde INTO _count_validate
				FROM 
				(
					SELECT 
						co.id company_id, ps.product_id
					FROM 
						product_movement pm
						INNER JOIN tmp_movement_filter mf ON pm.id=mf.movment_id
						INNER JOIN company co ON (CASE WHEN 1=_is_offline THEN pm.company_id=co.id ELSE pm.company_description=co.social_name END)
						INNER JOIN person pe ON co.person_id=pe.id 
						INNER JOIN product po ON co.id=po.company_id
						INNER JOIN financial_transfer_product_setting ps ON pe.bank_account_id = ps.bank_account_origin_id
						AND po.id = ps.product_id AND ifnull(ps.inactive,0)=0
					GROUP BY 
						co.id, ps.product_id
				) conf
				LEFT JOIN tmp_movement_product pmf ON conf.product_id = pmf.product_id
				WHERE 
					pmf.product_id IS NULL;

				IF(_count_validate > 0) THEN 
					SET _validation_message = CONCAT('Existe(m) produto(s) na configuração de transfêrencia sem lançamento!');
                    CALL pr_send_email_erros(1, null, 'PRODUTO(s) SEM LANÇAMENTO(s)',CONCAT(_validation_message,'\n','CODIGO DA LOJA: ',_company_id,',\n',
					'OFFLINE: ', CASE WHEN _is_offline = 0 then 'Não' ELSE 'Sim' END,',\n', 'ID(s) MOVEMENTO(s) PROCESSADO(s): ',_movement_ids,',\n',
                    'USUÁRIO: ',_user,',\n', 'ORIGEM DO ERRO: ', CASE WHEN _is_preview = 0 then 'Processa movimento produto' ELSE 'Preview movimento produto' END),
                    'douglinhas.mariano@gmail.com', now(), 0, now(), 0);
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message; 
				END IF;
                */
                
		/**Step 1 - Get movement products to process*/
		IF(1=_step_process) THEN
			BEGIN
				/**VARIABLES CURSOR*/
                DECLARE _has_movements_pending_process BIT DEFAULT 0;
				DECLARE _has_movements BIT DEFAULT 0;
				DECLARE _movement_id BIGINT;
				DECLARE _bank_account_destiny_id INT;
				DECLARE _credit_provider_id BIGINT; 
				DECLARE _debit_provider_id BIGINT;
                DECLARE _document_in_id BIGINT;
                DECLARE _document_out_id BIGINT;
                DECLARE _document_clock_id BIGINT;
                DECLARE _credit_in_initial BIGINT;
                DECLARE _credit_in_final BIGINT;
                DECLARE _credit_out_initial BIGINT;
                DECLARE _credit_out_final BIGINT;
                DECLARE _credit_clock_initial BIGINT;
                DECLARE _credit_clock_final BIGINT;
                DECLARE _conversion_factor NUMERIC(19,2);
                DECLARE _product_id BIGINT;
                DECLARE _product_description VARCHAR(100);
                DECLARE _movement_type VARCHAR(1);
                DECLARE _movement_legacy_id BIGINT;
                DECLARE _initial_date_movement DATE;
                DECLARE _transfer_hab  NUMERIC(6,2); 
                DECLARE _product_group_id INT;
                DECLARE _product_sub_group_id INT;
                
                /**Cursor to search products*/
				DECLARE produtos_cursor CURSOR FOR
				SELECT
					pm.id as movement_id, IFNULL(pe.bank_account_id,0) as bank_account_origin_id, 0 as bank_account_destiny_id, co.id as company_id, 
                    pe.credit_provider_id, pe.debit_provider_id, pm.document_in_id, pm.document_out_id, pm.document_parent_id, 
                    (CASE WHEN _is_offline = 1 THEN IFNULL(pm.credit_in_initial, po.input_movement) ELSE po.input_movement END) AS _credit_in_initial, pm.credit_in_final, (CASE WHEN _is_offline = 1 THEN IFNULL(pm.credit_out_initial, po.output_movement) ELSE po.output_movement END) AS credit_out_initial, pm.credit_out_final, po.clock_movement AS credit_clock_initial, (CASE WHEN IFNULL(po.enable_clock_movement,0) = 0 THEN po.clock_movement ELSE pm.credit_clock_final END) credit_clock_final,
                    IFNULL(po.conversion_factor,su.conversion_factor), pm.product_id, pm.movement_type, pm.initial_date, pm.reading_date, pm.movement_legacy_id, co.social_name, 
                    po.description, ifnull(gu.transfer_hab,0) transfer_hab, po.product_group_id, po.product_sub_group_id
				FROM
					product_movement pm
                    INNER JOIN tmp_movement_filter mf ON pm.id=mf.movment_id
					INNER JOIN product po ON pm.product_id=po.id
                    INNER JOIN product_group gu ON po.product_group_id = gu.id
					INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
					INNER JOIN company co ON (CASE WHEN 1=_is_offline THEN pm.company_id=co.id ELSE pm.company_description=co.social_name END)
					INNER JOIN person pe ON co.person_id=pe.id
                ORDER BY
					pm.id;
                    
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_movements=1;
				OPEN produtos_cursor;
				meuLoop: LOOP
				FETCH produtos_cursor INTO _movement_id, _bank_account_origin_id, _bank_account_destiny_id, _company_id, _credit_provider_id, _debit_provider_id, 
                _document_in_id, _document_out_id, _document_parent_id, _credit_in_initial, _credit_in_final, _credit_out_initial, _credit_out_final, _credit_clock_initial, _credit_clock_final, 
                _conversion_factor, _product_id, _movement_type, _initial_date_movement, _payment_data, _movement_legacy_id, _company_description, _product_description, _transfer_hab, _product_group_id, _product_sub_group_id;
                
				IF _has_movements = 1 THEN
					LEAVE meuLoop;
				END IF;
                
                /**Has movements pending to process*/
                SET _has_movements_pending_process = 1;
                
				SELECT negative_close, person_id INTO _negative_close, _person_id FROM company WHERE id = _company_id LIMIT 1;
                
                IF(IFNULL(_document_parent_id,0)=0) THEN
					BEGIN
						IF(IFNULL(_document_parent_id_generated,0)=0) THEN
							BEGIN
								INSERT INTO document_parent_generate_id(inactive) select 0;
								SET _document_parent_id_generated = LAST_INSERT_ID();                            
                            END; 
						END IF;
						SET _document_parent_id = _document_parent_id_generated;
                    END; END IF;
                    
				/**Insert bank account filter*/ 
                REPLACE INTO tmp_bank_account_filter(bank_account_origin_id, bank_account_destiny_id, company_id, document_parent_id)
                SELECT _bank_account_origin_id, _bank_account_destiny_id, _company_id, _document_parent_id;
                /**End Insert bank account filter*/
                
				/**Insert bank account filter to payment residue*/ 
                REPLACE INTO tmp_bank_account_filter2(bank_account_origin_id, bank_account_destiny_id, company_id, document_parent_id)
                SELECT _bank_account_origin_id, _bank_account_destiny_id, _company_id, _document_parent_id;
                /**End Insert bank account filter*/
                
                IF(IFNULL(_document_in_id,0)=0) THEN
					BEGIN
						INSERT INTO document_generate_id(inactive) select 0;
						SET _document_in_id = LAST_INSERT_ID(); 
                    END; END IF;       
                                
 				INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
				document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, credit_initial, credit_final, product_id, product_description, creation_date, transfer_hab
                ,product_group_id, product_sub_group_id)
                SELECT _movement_id, 1 AS is_product_movement, (IFNULL(_credit_in_initial,0)>IFNULL(_credit_in_final,0)), _document_in_id AS document_id,
				_document_parent_id, 4 AS document_type, 1 AS credit, _company_id, _credit_provider_id AS provider_id, _bank_account_origin_id AS bank_account_id, 
                ((IFNULL(_credit_in_final,0)-IFNULL(_credit_in_initial,0))*IFNULL(_conversion_factor,1)) AS document_value, 
                CONCAT('PROD:', CAST(_product_id AS CHAR), '/EI:',CAST(IFNULL(_credit_in_initial,0) AS CHAR),'/EF:',CAST(IFNULL(_credit_in_final,0) AS CHAR)) AS document_note, 
                (CASE WHEN _movement_type='A' THEN 2 ELSE 1 END) AS document_status, 0 AS payment_residue, _payment_data, '01.000' AS financial_group_id, '01.001' AS financial_sub_group_id, 
                IFNULL(_credit_in_initial,0), IFNULL(_credit_in_final,0), _product_id, _product_description, _initial_date_movement, _transfer_hab, _product_group_id, _product_sub_group_id;
                
				IF(IFNULL(_document_out_id,0)=0) THEN
					BEGIN
						INSERT INTO document_generate_id(inactive) select 0;
						SET _document_out_id = LAST_INSERT_ID(); 
                    END; END IF;

				INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
				document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, credit_initial, credit_final, product_id, product_description, creation_date, transfer_hab
                ,product_group_id, product_sub_group_id)
                SELECT _movement_id, 1 AS is_product_movement, (IFNULL(_credit_out_initial,0)>IFNULL(_credit_out_final,0)), _document_out_id AS document_id,
				_document_parent_id, 4 AS document_type, 0 AS credit, _company_id, _debit_provider_id AS provider_id, _bank_account_origin_id AS bank_account_id, 
                ((IFNULL(_credit_out_final,0)-IFNULL(_credit_out_initial,0))*IFNULL(_conversion_factor,1)) AS document_value, 
                CONCAT('PROD:', CAST(_product_id AS CHAR), '/SI:',CAST(IFNULL(_credit_out_initial,0) AS CHAR),'/SF:',CAST(IFNULL(_credit_out_final,0) AS CHAR)) AS document_note, 
                (CASE WHEN _movement_type='A' THEN 2 ELSE 1 END) AS document_status, 0 AS payment_residue, _payment_data, '07.000' AS financial_group_id, '07.001' AS financial_sub_group_id, 
                IFNULL(_credit_out_initial,0), IFNULL(_credit_out_final,0), _product_id, _product_description, _initial_date_movement, _transfer_hab, _product_group_id, _product_sub_group_id;
                
                IF(IFNULL(_credit_clock_initial,0)<>IFNULL(_credit_clock_final,0)) THEN
					BEGIN
						IF(IFNULL(_document_clock_id,0)=0) THEN
							BEGIN
								INSERT INTO document_generate_id(inactive) select 0;
								SET _document_clock_id = LAST_INSERT_ID(); 
							END; END IF;
						INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
						document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, credit_initial, credit_final, product_id, product_description, creation_date, transfer_hab, is_clock_movement
                        ,product_group_id, product_sub_group_id)
						SELECT _movement_id, 1 AS is_product_movement, (IFNULL(_credit_clock_initial,0)>IFNULL(_credit_clock_final,0)), _document_clock_id AS document_id,
						_document_parent_id, 4 AS document_type, 0 AS credit, _company_id, _debit_provider_id AS provider_id, _bank_account_origin_id AS bank_account_id, 
						((IFNULL(_credit_clock_final,0)-IFNULL(_credit_clock_initial,0))*IFNULL(_conversion_factor,1)) AS document_value, 
						CONCAT('PROD:', CAST(_product_id AS CHAR), '/CI:',CAST(IFNULL(_credit_clock_initial,0) AS CHAR),'/CF:',CAST(IFNULL(_credit_clock_final,0) AS CHAR)) AS document_note, 
						(CASE WHEN _movement_type='A' THEN 2 ELSE 1 END) AS document_status, 0 AS payment_residue, _payment_data, '07.000' AS financial_group_id, '07.001' AS financial_sub_group_id, 
						IFNULL(_credit_clock_initial,0), IFNULL(_credit_clock_final,0), _product_id, _product_description, _initial_date_movement, _transfer_hab, 1 is_clock_movement, _product_group_id, _product_sub_group_id;
					END;
				END IF;
				/**Verify if exists erro initial movement major final movement*/
				SELECT 
    GROUP_CONCAT(DISTINCT CASE
            WHEN
                movement_error = 1 AND credit = 1
            THEN
                CONCAT('Entrada inicial: ',
                        IFNULL(_credit_in_initial, 0),
                        ' maior que Entrada final: ',
                        IFNULL(_credit_in_final, 0))
            WHEN
                movement_error = 1 AND credit = 0
            THEN
                CONCAT('Saida inicial: ',
                        IFNULL(_credit_out_initial, 0),
                        ' maior que Saida final: ',
                        IFNULL(_credit_out_final, 0))
            ELSE ''
        END)
INTO _validation_message FROM
    tmp_document_movement;
                    
				IF(LENGTH(_validation_message)>0) THEN
					BEGIN
						SIGNAL SQLSTATE '45000'
						SET MESSAGE_TEXT = _validation_message;
					END;
				END IF; 
				/**End if Verify if exists erro initial movement major final movement*/
                
			  END LOOP meuLoop;
			  CLOSE produtos_cursor;                
              
              SET _has_transf_hab = IFNULL((SELECT 1 FROM tmp_document_movement WHERE is_product_movement = 1 and IFNULL(transfer_hab,0)>0 LIMIT 1),0);
              SET _client_id = (SELECT client_id FROM company where id = _company_id LIMIT 1);
				              
			  SET _step_process=2;              
              IF(_has_movements_pending_process=0) THEN
					BEGIN
						SET _validation_message = 'Sem movimentos para processamento.';
						SIGNAL SQLSTATE '45000'
						SET MESSAGE_TEXT = _validation_message;
					END; 
			  END IF;
                    
				SET _bank_account_company = IFNULL((SELECT ba.id FROM company co 
											INNER JOIN person pe ON co.person_id=pe.id 
											INNER JOIN bank_account ba ON pe.bank_account_id=ba.id
											WHERE co.id=_company_id),0);
				
                 if(_bank_account_company=0) THEN
					BEGIN
						SET _validation_message = CONCAT('Empresa: ', _company_id, ' sem nehuma conta vinculada');
						SIGNAL SQLSTATE '45000'
						SET MESSAGE_TEXT = _validation_message;
					END; END IF;				
                    
                    SET _count_day_last_movement = DATEDIFF(DATE(_payment_data), DATE(_initial_date_movement));
                    IF(_count_day_last_movement<0) THEN
						BEGIN
							SET _validation_message = CONCAT('Nao foi possivel calcular a despesa diaria da Empresa: ', _company_id);
							SIGNAL SQLSTATE '45000'
							SET MESSAGE_TEXT = _validation_message;
                        END; END IF;
            END; /**End Step 1 - Get movement products to process*/
		END IF;
        /**Step 2 - Process config transfer*/    
		IF(2=_step_process) THEN
			BEGIN
				/**VARIABLES CURSOR*/
				DECLARE _has_transfer BIT DEFAULT 0;
                DECLARE _has_transfer_bank_account BIT DEFAULT 0; 
				DECLARE _group_id INT;
                DECLARE _bank_account_origin_id INT;
				DECLARE _bank_account_destiny_id INT;
				DECLARE _group_execution_order TINYINT; 
				DECLARE _group_description VARCHAR(100); 
				DECLARE _group_item_id INT; 
				DECLARE _group_item_execution_order TINYINT;
				DECLARE _description_transfer VARCHAR(100); 
				DECLARE _provider_id BIGINT; 
				DECLARE	_transfer_type TINYINT;
				DECLARE	_value_transfer DECIMAL(19,2); 
				DECLARE	_credit_debit BIT; 
				DECLARE	_transfer_state TINYINT; 
				DECLARE	_is_over_total BIT; 
				DECLARE	_expense TINYINT; 
				DECLARE	_is_use_remaining_balance BIT;
                DECLARE _last_group_id INT DEFAULT 0;
                DECLARE _bank_account_id INT;
                DECLARE _document_id BIGINT;
   				DECLARE	_document_value DECIMAL(19,2); 
                DECLARE	_residue_value DECIMAL(19,2) DEFAULT 0; 
                DECLARE _document_parent_id_residue_generated BIGINT;
                DECLARE	_payment_residue BIT;
                DECLARE _document_id_old BIGINT;
                DECLARE _document_parent_id_old BIGINT;
                DECLARE _is_balance_percentage BIT DEFAULT 0;
                DECLARE _max_group_id INT DEFAULT 0;                
                DECLARE _max_execution_order INT DEFAULT 0;  
                DECLARE _initial_date_movement DATE;
                DECLARE _count_group_transfer INT DEFAULT 0; 
                DECLARE _group_product_id INT DEFAULT 0; 
                DECLARE _sub_group_product_id INT DEFAULT 0; 
                DECLARE	_movement_balance_sub_group DECIMAL(19,2); 
                DECLARE	_movement_total_sub_group DECIMAL(19,2); 
                
                /**Cursor to search config  transfer*/
                /**
				DECLARE config_transfer_cursor CURSOR FOR
                SELECT ftg.id AS group_id, ftgi.bank_account_origin_id, ftg.bank_account_destiny_id, ftg.bank_account_automatic_transfer_id, ftg.execution_order AS group_execution_order, ftg.description AS group_description, 
					   ftgi.id AS group_item_id, ftgi.execution_order AS group_item_execution_order, ftgi.description_transfer, ftgi.provider_id, ftgi.transfer_type, ftgi.value_transfer, 
                       ftgi.credit_debit, ftgi.transfer_state, ftgi.is_over_total, ftgi.expense, ftgi.is_use_remaining_balance, ftgi.bank_account_id, 0 AS payment_residue, 0 AS document_old_id, 0 AS document_parent_old_id
				FROM
					(SELECT bank_account_origin_id, bank_account_destiny_id FROM tmp_bank_account_filter GROUP BY bank_account_origin_id, bank_account_destiny_id)  f 
					INNER JOIN financial_transfer_group_setting ftg ON f.bank_account_origin_id=ftg.bank_account_origin_id AND f.bank_account_destiny_id=ftg.bank_account_destiny_id AND IFNULL(ftg.inactive,0)=0
					LEFT JOIN financial_transfer_group_item_setting ftgi ON ftg.id=ftgi.group_id AND IFNULL(ftgi.inactive,0)=0
				ORDER BY  
					payment_residue desc, group_execution_order ASC, group_item_execution_order ASC;
				*/
				DECLARE config_transfer_cursor CURSOR FOR
                (
                SELECT ftg.id AS group_id, ftgi.bank_account_origin_id, ftg.bank_account_destiny_id, ftg.bank_account_automatic_transfer_id, ftg.execution_order AS group_execution_order, ftg.description AS group_description, 
					   ftgi.id AS group_item_id, ftgi.execution_order AS group_item_execution_order, ftgi.description_transfer, ftgi.provider_id, ftgi.transfer_type, ftgi.value_transfer, 
                       ftgi.credit_debit, ftgi.transfer_state, ftgi.is_over_total, ftgi.expense, ftgi.is_use_remaining_balance, ftgi.bank_account_id, 0 AS payment_residue, 0 AS document_old_id, 0 AS document_parent_old_id,
                       ftgi.group_product_id, ftgi.sub_group_product_id
				FROM
					(SELECT bank_account_origin_id, bank_account_destiny_id FROM tmp_bank_account_filter GROUP BY bank_account_origin_id, bank_account_destiny_id)  f 
					INNER JOIN financial_transfer_group_setting ftg ON f.bank_account_origin_id=ftg.bank_account_origin_id AND f.bank_account_destiny_id=ftg.bank_account_destiny_id AND IFNULL(ftg.inactive,0)=0
					LEFT JOIN financial_transfer_group_item_setting ftgi ON ftg.id=ftgi.group_id AND IFNULL(ftgi.inactive,0)=0
                    WHERE ftgi.provider_id <> 2379
				)
                UNION ALL
                (
                SELECT ftg.id AS group_id, ftgi.bank_account_origin_id, ftg.bank_account_destiny_id, ftg.bank_account_automatic_transfer_id, ftg.execution_order AS group_execution_order, ftg.description AS group_description, 
					   ftgi.id AS group_item_id, ftgi.execution_order AS group_item_execution_order, ftgi.description_transfer, ftgi.provider_id, ftgi.transfer_type, IFNULL(doc.document_value,0) AS value_transfer, 
                       ftgi.credit_debit, ftgi.transfer_state, ftgi.is_over_total, ftgi.expense, ftgi.is_use_remaining_balance, ftgi.bank_account_id, 1 AS payment_residue, doc.document_id AS document_old_id, doc.document_parent_id AS document_parent_old_id,
                       ftgi.group_product_id, ftgi.sub_group_product_id
				FROM
					(SELECT bank_account_origin_id, bank_account_destiny_id FROM tmp_bank_account_filter2 GROUP BY bank_account_origin_id, bank_account_destiny_id)  f 
					INNER JOIN financial_transfer_group_setting ftg ON f.bank_account_origin_id=ftg.bank_account_origin_id AND f.bank_account_destiny_id=ftg.bank_account_destiny_id -- AND IFNULL(ftg.inactive,0)=0
					LEFT JOIN financial_transfer_group_item_setting ftgi ON ftg.id=ftgi.group_id AND IFNULL(ftgi.inactive,0)=0
                    INNER JOIN document_movement doc ON ftgi.provider_id = doc.provider_id AND ftgi.bank_account_origin_id = doc.bank_account_id and doc.document_status=1 and doc.document_type = 4 AND IFNULL(doc.payment_residue,0) = 0 -- and doc.inactive=0
				)
                UNION ALL 
                (
					select 0 group_id, _bank_account_company bank_account_origin_id, 0 bank_account_destiny_id, 0 bank_account_automatic_transfer_id, 0 group_execution_order, 'HAB' group_description, 
					0 group_item_id, 0 group_item_execution_order, 'HAB' description_transfer, 2379 provider_id, 1 transfer_type, 10 value_transfer, 0 credit_debit, 1 transfer_state, 
					1 is_over_total, 0 expense, 0 is_use_remaining_balance, (CASE WHEN _client_id = 1 THEN 156 ELSE 378 END) bank_account_id, 0 payment_residue, 0 document_old_id, 0 document_parent_old_id, 0 group_product_id, 0 sub_group_product_id                
                    where 1 = _has_transf_hab
                )
				ORDER BY  
                    group_execution_order ASC, group_item_execution_order ASC, payment_residue DESC;
                    
                
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_transfer=1;
				OPEN config_transfer_cursor;
				meuLoop: LOOP
				FETCH config_transfer_cursor INTO _group_id, _bank_account_origin_id, _bank_account_destiny_id,  _bank_account_automatic_transfer_id, _group_execution_order, _group_description, _group_item_id, 
                _group_item_execution_order, _description_transfer, _provider_id, _transfer_type, _value_transfer, _credit_debit, _transfer_state, _is_over_total, _expense, 
                _is_use_remaining_balance, _bank_account_id, _payment_residue, _document_id_old, _document_parent_id_old, _group_product_id, _sub_group_product_id;
                
				IF _has_transfer = 1 THEN
					LEAVE meuLoop;
				END IF;
                                                
                SET _has_transfer_bank_account = 1;
                                                
				SELECT 
					MAX(payment_data)
					INTO _initial_date_movement 
				FROM
					document_movement
				WHERE
					bank_account_id = _bank_account_origin_id
					AND company_id = _company_id;                    
								
				IF(_last_group_id <> _group_id AND _group_id <> 0) THEN
					BEGIN        
						IF(1 = IFNULL((SELECT 1 FROM financial_transfer_product_setting WHERE bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id AND group_id = _group_id AND inactive = 0 LIMIT 1),0)) THEN
							BEGIN
								UPDATE tmp_document_movement SET group_id=_group_id, 
								group_execution_order=_group_execution_order, group_item_execution_order=_group_item_execution_order
								WHERE product_id IN (SELECT product_id FROM financial_transfer_product_setting WHERE bank_account_origin_id = _bank_account_origin_id AND bank_account_destiny_id = _bank_account_destiny_id AND group_id = _group_id AND inactive = 0);
								SET _last_group_id = _group_id;
                                
                                SET _count_group_transfer = IFNULL((SELECT count(*) qtde FROM
                                (SELECT bank_account_origin_id, bank_account_destiny_id FROM tmp_bank_account_filter GROUP BY bank_account_origin_id, bank_account_destiny_id)  f 
								INNER JOIN financial_transfer_group_setting ftg ON f.bank_account_origin_id=ftg.bank_account_origin_id AND f.bank_account_destiny_id=ftg.bank_account_destiny_id AND IFNULL(ftg.inactive,0)=0),0);
                                
                                IF(_count_group_transfer = 1) THEN
									UPDATE tmp_document_movement SET 
										group_id = _group_id, group_execution_order = _group_execution_order, 
										group_item_execution_order = _group_item_execution_order							
									WHERE 
										is_product_movement = 1;
                                END IF;
                                
								SET _movement_total=(SELECT SUM(CASE WHEN credit=1 THEN document_value ELSE (document_value * -1) END) FROM tmp_document_movement WHERE group_id=_group_id AND IFNULL(payment_residue,0)=0 AND is_product_movement=1);                        
                            END;
                        ELSE
							BEGIN	
                                UPDATE tmp_document_movement SET 
									group_id = _group_id, group_execution_order = _group_execution_order, 
									group_item_execution_order = _group_item_execution_order							
                                WHERE 
									is_product_movement = 1
                                    AND group_id IS NULL;
                                    SET _movement_total=(SELECT SUM(CASE WHEN credit=1 THEN document_value ELSE (document_value * -1) END) FROM tmp_document_movement WHERE group_id=_group_id AND IFNULL(payment_residue,0)=0 AND is_product_movement=1);                        
							END;
                        END IF;
                    END;
				ELSE
					BEGIN 
                        SET _movement_total=(SELECT SUM(CASE WHEN credit=1 THEN document_value ELSE (document_value * -1) END) FROM tmp_document_movement WHERE group_id=_group_id AND IFNULL(payment_residue,0)=0 AND is_product_movement=1);
                    END;
				END IF;									
			                          
                IF(_is_offline=0) THEN
					BEGIN
						SELECT SUM(CASE WHEN credit=1 THEN document_value ELSE (document_value * -1) END) INTO _movement_balance FROM tmp_document_movement WHERE IFNULL(payment_residue,0)=0;
                    END;
				ELSE
					BEGIN      
						IF(SELECT 1 = 1  FROM tmp_document_movement WHERE is_product_movement = 1 AND group_id = _group_id LIMIT 1) THEN
							SELECT SUM(CASE WHEN credit=1 THEN document_value ELSE (document_value * -1) END) INTO _movement_balance FROM tmp_document_movement
							WHERE group_id=_group_id AND bank_account_id=_bank_account_origin_id AND IFNULL(payment_residue,0)=0;										                                               
                        END IF;
						           
						/**TODO: VERIFICAR SE NAO E GRUPO PRINCIPAL PARA EXECUTAR ELE*/
                        if(IFNULL(_movement_balance,0)<=0) THEN
							BEGIN								
								SELECT MAX(ftg.execution_order) INTO _max_execution_order FROM financial_transfer_group_setting ftg  
								WHERE ftg.bank_account_origin_id=_bank_account_origin_id AND ftg.bank_account_destiny_id=_bank_account_destiny_id AND IFNULL(ftg.inactive,0)=0;													                            
							SELECT 
								MAX(ftg.id)
							INTO _max_group_id FROM
								financial_transfer_group_setting ftg
							WHERE
								ftg.bank_account_origin_id = _bank_account_origin_id
									AND ftg.bank_account_destiny_id = _bank_account_destiny_id
									AND IFNULL(ftg.inactive, 0) = 0
									AND ftg.execution_order = _max_execution_order;								
                                
                                IF(_max_group_id=_group_id) THEN
									BEGIN										
										SELECT
                                         SUM(CASE WHEN credit=1 THEN document_value ELSE (CASE WHEN is_product_movement=1 THEN (document_value * -1) ELSE document_value END)  END) 
                                         INTO _movement_balance 
                                        FROM tmp_document_movement
										WHERE bank_account_id=_bank_account_origin_id AND IFNULL(payment_residue,0)=0;                                                                                
                                    END;
								END IF;                                                                    
                            END; 
						END IF;
                    END;
				END IF; /** End _is_offline */
                                
                IF(IFNULL(_document_id_old,0)>0 AND IFNULL(_document_parent_id_old,0)>0 AND _is_preview = 0) THEN
					BEGIN
						DELETE FROM document_movement WHERE document_id=_document_id_old AND document_parent_id=_document_parent_id_old AND document_status=1 AND document_type=4;
                    END; END IF;
                    
                    
				IF(IFNULL(_payment_residue,0) <> 1 AND _provider_id <> 2379 AND IFNULL(_group_product_id,0) > 0 AND IFNULL(_sub_group_product_id,0) > 0) THEN
					BEGIN
                                    DECLARE	_movement_balance_sub_group DECIMAL(19,2); 

						SET _movement_total_sub_group = (SELECT 
															SUM(CASE WHEN credit = 1 THEN document_value ELSE (document_value * -1)  END) 
														 FROM tmp_document_movement 
                                                         WHERE product_group_id = _group_product_id AND product_sub_group_id = _sub_group_product_id AND is_product_movement = 1);                                                         
                                                         
						SET _movement_balance_sub_group = (SELECT 
															SUM(CASE WHEN credit = 1 THEN document_value ELSE (document_value * -1)  END) 
														 FROM tmp_document_movement 
                                                         WHERE product_group_id = _group_product_id AND product_sub_group_id = _sub_group_product_id);

						SET _document_value = (CASE WHEN IFNULL(_transfer_type,0) = 1 THEN 
																						(CASE WHEN IFNULL(_is_over_total,0) = 1 
																							  THEN (_movement_total_sub_group * (_value_transfer/100.00)) 
																							  ELSE (_movement_balance_sub_group * (_value_transfer/100.00)) END) 
													ELSE (CASE WHEN _expense = 0 THEN _value_transfer WHEN _expense = 1 THEN (_value_transfer * _count_day_last_movement) ELSE _value_transfer END) END);			

                    END;
				ELSE 
					BEGIN
                                    SET _document_value = (CASE WHEN IFNULL(_payment_residue,0)=1 THEN _value_transfer 
									   ELSE CASE WHEN IFNULL(_transfer_type,0)=1 THEN 
                                       (CASE WHEN IFNULL(_is_over_total,0)=1 
                                       THEN (_movement_total * (_value_transfer/100.00)) 
                                       ELSE (_movement_balance * (_value_transfer/100.00)) END) 
									   ELSE (CASE WHEN _expense=0 THEN _value_transfer WHEN _expense=1 THEN (_value_transfer * _count_day_last_movement) ELSE _value_transfer END)  END END);			
                    END;
                END IF;
                                    
                SET _is_balance_percentage = (CASE WHEN _payment_residue = 1 THEN 0 ELSE CASE WHEN _transfer_type = 1 THEN 1 ELSE 0  END END);	
                             
			   IF (_expense = 3 and _payment_residue = 0) THEN
				BEGIN                                 				
                    IF (SELECT 1 = 1 FROM document_movement WHERE 
                    payment_data BETWEEN DATE_ADD(CAST(_payment_data AS DATE),INTERVAL -DAY(CAST(_payment_data AS DATE))+1 DAY)
					AND LAST_DAY(CAST(_payment_data AS DATE)) AND provider_id=_provider_id AND bank_account_id=_bank_account_id 
                    AND company_id=_company_id LIMIT 1) THEN
                    BEGIN
						SET _document_value = 0;
                    END; END IF;  
                    
				IF (SELECT 1 = 1 FROM tmp_document_movement WHERE 
                    payment_data BETWEEN DATE_ADD(CAST(_payment_data AS DATE),INTERVAL -DAY(CAST(_payment_data AS DATE))+1 DAY)
					AND LAST_DAY(CAST(_payment_data AS DATE)) AND provider_id=_provider_id AND bank_account_id=_bank_account_id 
                    AND company_id=_company_id LIMIT 1) THEN
                    BEGIN
						SET _document_value = 0;
                    END; END IF;  
                    
                END; END IF;
                
               IF(_movement_balance<_document_value AND !_is_balance_percentage) THEN
					BEGIN
						IF(_movement_balance >= 0) THEN
							BEGIN
								SET _residue_value = (_document_value-_movement_balance);
								SET _document_value = (_document_value-_residue_value);
                            END;
						END IF;						
                    END; END IF;             
                                   
				INSERT INTO document_generate_id(inactive) select 0;
				SET _document_id = LAST_INSERT_ID(); 
                            
                SET _document_parent_id_generated = _document_parent_id; 
                IF (SELECT 1 = 1 FROM provider_ungroup WHERE provider_id = _provider_id AND inactive=0 LIMIT 1) THEN /* Calculate HAB */
					BEGIN
						INSERT INTO document_parent_generate_id(inactive) select 0;
						SET _document_parent_id_generated = LAST_INSERT_ID();                              

						SELECT IFNULL((SELECT IFNULL(SUM(CASE WHEN credit = 0 then (document_value * -1) else document_value end),0) * (MAX(transfer_hab)/100) FROM tmp_document_movement WHERE is_product_movement = 1 and IFNULL(transfer_hab,0)>0),0) INTO _document_value;                        
					END; 
				END IF; /* End Calculate HAB */

				IF(_document_value > 0 || _is_balance_percentage) THEN
					BEGIN								
                        IF (SELECT 1 = 1 FROM bank_account_out_document_parent WHERE bank_account_id=_bank_account_id LIMIT 1) THEN
							BEGIN
								INSERT INTO document_parent_generate_id(inactive) select 0;
								SET _document_parent_id_generated = LAST_INSERT_ID();  
                                
                                SELECT IFNULL((SELECT IFNULL(SUM(CASE WHEN credit = 0 then (document_value * -1) else document_value end),0) * (MAX(transfer_hab)/100) FROM tmp_document_movement WHERE is_product_movement = 1 and IFNULL(transfer_hab,0)>0),0) INTO _document_value;
							END; 
						END IF; 
                        
						INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
						document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, group_id, group_execution_order, group_item_execution_order, creation_date
                        ,product_group_id, product_sub_group_id)
						SELECT 0 AS _movement_id, 0 AS is_product_movement, 0 AS movement_error, _document_id, _document_parent_id_generated, 4 AS document_type, (CASE WHEN _document_value < 0 THEN 1 ELSE 0 END) AS credit, 
						_company_id, _provider_id, _bank_account_id, (case when _document_value < 0 then (_document_value * -1) else _document_value end),
						CONCAT((CASE WHEN _transfer_type=1 THEN '%' ELSE 'R$' END),  CAST(_value_transfer AS CHAR), '/F:',CAST(_provider_id AS CHAR), '/',_description_transfer, CASE WHEN _payment_residue THEN ' - RESIDUO ' ELSE '' END) AS document_note, 
						2 AS document_status, 0 AS payment_residue, _payment_data, IFNULL((SELECT financial_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_group_id, 
						IFNULL((SELECT financial_sub_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_sub_group_id, _group_id, _group_execution_order, _group_item_execution_order, IFNULL(_initial_date_movement, _payment_data)
                        ,_group_product_id, _sub_group_product_id;                        
                    
						-- EXPENSE = 2	
						IF (_expense = 2 and _payment_residue = 0) THEN
							BEGIN    
								SET _count_week = TRUNCATE(IFNULL((IFNULL(_count_day_last_movement, 7) / 7), 1), 0);	
                                
								IF(WEEKOFYEAR(_payment_data) > WEEKOFYEAR(_initial_date_movement)) THEN 
									BEGIN
										SET _count_week = (WEEKOFYEAR(_payment_data) - WEEKOFYEAR(_initial_date_movement));
                                    END;
                                END IF;
                                
								IF(_count_week >= 2 or _count_day_last_movement > 11) THEN
									BEGIN
										SET _counter = 1;
										WHILE _counter < _count_week DO 											
                                            
											INSERT INTO document_generate_id(inactive) select 0;
											SET _document_id = LAST_INSERT_ID(); 
                
											INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
											document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, group_id, group_execution_order, group_item_execution_order, creation_date
                                            ,product_group_id, product_sub_group_id)
											SELECT 0 AS _movement_id, 0 AS is_product_movement, 0 AS movement_error, _document_id, _document_parent_id_generated, 4 AS document_type, (CASE WHEN _document_value < 0 THEN 1 ELSE 0 END) AS credit, 
											_company_id, _provider_id, _bank_account_id, (case when _document_value < 0 then (_document_value * -1) else _document_value end),
											CONCAT((CASE WHEN _transfer_type=1 THEN '%' ELSE 'R$' END),  CAST(_value_transfer AS CHAR), '/F:',CAST(_provider_id AS CHAR), '/',_description_transfer, ' - RESIDUO ') AS document_note, 
											2 AS document_status, 0 AS payment_residue, _payment_data, IFNULL((SELECT financial_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_group_id, 
											IFNULL((SELECT financial_sub_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_sub_group_id, _group_id, _group_execution_order, _group_item_execution_order, IFNULL(_initial_date_movement, _payment_data)
                                            ,_group_product_id, _sub_group_product_id;     

											SET _counter = _counter + 1;
										END WHILE;
									END;
								END IF;  
							END; 
						END IF;
                        
                        -- EXPENSE = 5	
						IF (_expense = 5 and _payment_residue = 0) THEN
							BEGIN    
								SET _count_week = TRUNCATE(IFNULL((IFNULL(_count_day_last_movement, 15) / 15), 1), 0);
								IF(_count_week >= 2) THEN
									BEGIN
										SET _counter = 1;
										WHILE _counter < _count_week DO 
											
											INSERT INTO document_generate_id(inactive) select 0;
											SET _document_id = LAST_INSERT_ID(); 
                
											INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
											document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, group_id, group_execution_order, group_item_execution_order, creation_date
                                            ,product_group_id, product_sub_group_id)
											SELECT 0 AS _movement_id, 0 AS is_product_movement, 0 AS movement_error, _document_id, _document_parent_id_generated, 4 AS document_type, (CASE WHEN _document_value < 0 THEN 1 ELSE 0 END) AS credit, 
											_company_id, _provider_id, _bank_account_id, (case when _document_value < 0 then (_document_value * -1) else _document_value end),
											CONCAT((CASE WHEN _transfer_type=1 THEN '%' ELSE 'R$' END),  CAST(_value_transfer AS CHAR), '/F:',CAST(_provider_id AS CHAR), '/',_description_transfer, ' - RESIDUO ') AS document_note, 
											2 AS document_status, 0 AS payment_residue, _payment_data, IFNULL((SELECT financial_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_group_id, 
											IFNULL((SELECT financial_sub_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_sub_group_id, _group_id, _group_execution_order, _group_item_execution_order, IFNULL(_initial_date_movement, _payment_data)
                                            ,_group_product_id, _sub_group_product_id;

											SET _counter = _counter + 1;
										END WHILE;
									END;
								END IF;  
							END; 
						END IF;
						
                    END; END IF;
                    
				IF(_residue_value > 0 AND _transfer_type = 2) THEN
					BEGIN
						IF(IFNULL(_negative_close,0) = 0) THEN
							BEGIN
								INSERT INTO document_parent_generate_id(inactive) select 0;
								SET _document_parent_id_residue_generated = LAST_INSERT_ID();                            
                            END;
						ELSE 
							BEGIN
								SET _document_parent_id_residue_generated = _document_parent_id;
                            END;
						END IF;
                        
						INSERT INTO document_generate_id(inactive) select 0;
						SET _document_id = LAST_INSERT_ID(); 
                        
						INSERT INTO tmp_document_movement(movement_id, is_product_movement, movement_error, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
						document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, group_id, group_execution_order, group_item_execution_order, creation_date
                        ,product_group_id, product_sub_group_id)
						SELECT 0 AS _movement_id, 0 AS is_product_movement, 0 AS movement_error, _document_id, _document_parent_id_residue_generated, 4 AS document_type, 0 AS credit, 
						_company_id, _provider_id, _bank_account_id, _residue_value AS document_value,
						CONCAT((CASE WHEN _transfer_type=1 THEN '%' ELSE 'R$' END),  CAST(_value_transfer AS CHAR), '/F:',CAST(_provider_id AS CHAR), '/',_description_transfer, ' - RESIDUO') AS document_note, 
						CASE WHEN IFNULL(_negative_close,0) = 0 THEN 1 ELSE 2 END AS document_status, CASE WHEN IFNULL(_negative_close,0) = 0 THEN 1 ELSE 0 END AS payment_residue, _payment_data, IFNULL((SELECT financial_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_group_id, 
						IFNULL((SELECT financial_sub_group_id FROM provider WHERE id=_provider_id LIMIT 1),0) AS financial_sub_group_id, _group_id, _group_execution_order, _group_item_execution_order, IFNULL(_initial_date_movement, _payment_data)
                        ,_group_product_id, _sub_group_product_id;
                    END; END IF;     
													
				SELECT 
					SUM(CASE
						WHEN credit = 1 THEN document_value
						ELSE (document_value * - 1)
					END) INTO _movement_balance FROM
				tmp_document_movement
				WHERE
					IFNULL(payment_residue, 0) = 0;
				
                SET _document_value = 0;
                SET _residue_value = 0;
                SET _is_over_total = 0;

			  END LOOP meuLoop;
			  CLOSE config_transfer_cursor;
SELECT 
    SUM(CASE
        WHEN credit = 1 THEN document_value
        ELSE (CASE
            WHEN is_product_movement = 1 THEN (document_value * - 1)
            ELSE document_value
        END)
    END)
INTO _movement_balance FROM
    tmp_document_movement
WHERE
    bank_account_id = _bank_account_origin_id
        AND IFNULL(payment_residue, 0) = 0;
			  
              IF(_has_transfer_bank_account=0) THEN
					BEGIN
						SET _validation_message = CONCAT('Não existe configuração de transferência para a conta da Empresa: ', (SELECT CONCAT(social_name, ' (', id, ')') FROM company WHERE id=_company_id));
						SIGNAL SQLSTATE '45000'
						SET MESSAGE_TEXT = _validation_message;
					END; END IF;
                
			  IF(_is_preview=1) THEN
				BEGIN
					SET _step_process=5;
                END; 
			  ELSE
				BEGIN
					SET _step_process=3;
			  END; END IF;                
              
            END; END IF; /**End Step 2 - Process config transfer*/
		/**Step 3 - Process Validations*/    
		IF(3=_step_process) THEN
			BEGIN				
            
                select IFNULL(sum((case when credit = 1 then document_value else (document_value * -1) end)),0) INTO _total_movement from tmp_document_movement 
                WHERE IFNULL(payment_residue, 0) = 0 AND provider_id NOT IN (SELECT provider_id FROM provider_ungroup WHERE inactive = 0 GROUP BY provider_id);
              
                IF((_movement_balance<0 || _total_movement < 0) && _negative_close=0) THEN 
					BEGIN
						
						SET _validation_message = CONCAT('Movimento não pode ser processado negativo: ', (case when _movement_balance < 0 then _movement_balance else _total_movement end)); 
                        
                        /** Update movement company negative note */
                        CALL pr_process_romaneio(1, _company_id, null, 1, _validation_message, 0); 
						SIGNAL SQLSTATE '45000'
						SET MESSAGE_TEXT = _validation_message; 
					END; 
				END IF;
                SET _step_process=4;
            END; END IF; /**End Step 3 - Process Validations*/
		/**Step 4 - Save Documents*/    
		IF(4=_step_process) THEN
			BEGIN
				/**VARIABLES CURSOR*/
                DECLARE _has_documents BIT DEFAULT 0;
                DECLARE _execution_order INTEGER;
                DECLARE _movement_id BIGINT DEFAULT 0;
				DECLARE _document_id BIGINT;
				DECLARE _document_parent_id BIGINT;
				DECLARE _document_type INT;
				DECLARE _credit BIT;
				DECLARE _company_id BIGINT;
				DECLARE _provider_id BIGINT;
				DECLARE _bank_account_id INT;
				DECLARE _document_value DECIMAL(19,4);
				DECLARE _document_note VARCHAR(1000);
				DECLARE _document_status INT;
				DECLARE _payment_residue BIT;
				DECLARE _payment_data DATETIME;
				DECLARE _financial_group_id VARCHAR(7);
				DECLARE _financial_sub_group_id VARCHAR(7);
                DECLARE _credit_initial BIGINT;
                DECLARE _credit_final BIGINT;
                DECLARE _product_id BIGINT;
                DECLARE _is_product_movement BIT;
                DECLARE _document_id_transfer BIGINT;
				DECLARE _document_parent_id_transfer BIGINT;
                DECLARE _company_id_transfer BIGINT;
                DECLARE _credit_provider_id_transfer BIGINT;
                DECLARE _debit_provider_id_transfer BIGINT;
                DECLARE _document_value_transfer DECIMAL(19,4);
                DECLARE _company_description_transfer VARCHAR(100);
                DECLARE _financial_group_id_transfer VARCHAR(7);
                DECLARE _financial_subgroup_id_transfer VARCHAR(7);
                DECLARE _person_id_transfer BIGINT;
                DECLARE _product_description VARCHAR(100);
                DECLARE _group_id INT; 
				DECLARE _group_execution_order TINYINT;
				DECLARE _group_item_execution_order TINYINT;
                DECLARE _document_parent_principal_id BIGINT;
                DECLARE _creation_date DATETIME;
                DECLARE _is_clock_movement BIT;
                
                /**Cursor to save documents*/
				DECLARE save_documents_cursor CURSOR FOR
                SELECT IFNULL(group_id,0) group_id, execution_order, group_execution_order, group_item_execution_order, movement_id, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, IFNULL(document_value,0) document_value, document_note, 
                document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id, credit_initial, credit_final, product_id, is_product_movement, product_description, creation_date, is_clock_movement
                FROM tmp_document_movement ORDER BY group_execution_order, group_item_execution_order, execution_order;
					
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_documents=1;
				OPEN save_documents_cursor;
				meuLoop: LOOP
				FETCH save_documents_cursor INTO _group_id, _execution_order, _group_execution_order, _group_item_execution_order, _movement_id, _document_id, _document_parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, _document_value, 
                _document_note, _document_status, _payment_residue, _payment_data, _financial_group_id, _financial_sub_group_id, _credit_initial, _credit_final, _product_id, _is_product_movement, _product_description, _creation_date, _is_clock_movement;
                
				IF _has_documents = 1 THEN
					LEAVE meuLoop;
				END IF;     
                
                if(_is_product_movement) THEN
					SET _document_parent_principal_id = _document_parent_id;
				END IF;
                
                IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id LIMIT 1) THEN
					BEGIN /**Update document*/
						UPDATE document_movement dm SET 
						dm.document_type=_document_type, dm.credit=_credit, dm.company_id=_company_id, dm.provider_id=_provider_id, dm.bank_account_id=_bank_account_id, 
						dm.document_number=_document_id, dm.document_value=_document_value, dm.document_note=_document_note, dm.document_status=_document_status, 
						dm.payment_value=_document_value, dm.payment_discount=0, dm.payment_extra=0, dm.payment_residue=0, dm.payment_data=_processing_date_time, 
						dm.payment_expiry_data=_payment_data, dm.payment_user=_user, dm.nfe_number=0, dm.nfe_serie_number=0, dm.generate_billet=0, 
						dm.financial_group_id=_financial_group_id, dm.financial_sub_group_id=_financial_sub_group_id, dm.inactive=0, dm.user_change=_user, 
						dm.change_date=_processing_date_time, dm.is_product_movement = _is_product_movement,
                        dm.group_transfer_id=_group_id, dm.group_execution_order=_group_execution_order, dm.group_item_execution_order=_group_item_execution_order
						WHERE dm.document_id=_document_id AND dm.document_parent_id=_document_parent_id;
					END; /**End Update document*/
				ELSE
					BEGIN /**Insert document*/
						INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_number, document_value,
						document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, 
						nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, inactive, user_creation, creation_date, user_change, change_date, 
                        product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order)
						SELECT _document_id, _document_parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, 0 bank_account_origin_id, _document_id AS document_number, _document_value, 
						_document_note, _document_status, _document_value AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, _processing_date_time, 
						_payment_data AS payment_expiry_data, _user AS payment_user, 0 AS nfe_number, 0 AS nfe_serie_number, 0 AS generate_billet, _financial_group_id, 
						_financial_sub_group_id, _is_product_movement, 0 AS inactive, _user AS user_creation, _creation_date AS creation_date, _user AS user_change, 
                        _processing_date_time AS change_date, _product_id, _product_description,
                        _group_id, _group_execution_order, _group_item_execution_order;
					END; /**End Insert document*/
				END IF;
						
				 IF (_document_status = 2) THEN
					/**Insert bank account statement*/
					INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)  
					SELECT _person_id, _bank_account_id, 0, _provider_id, 4 _launch_type, _document_parent_id, _document_id, (case when _credit then _document_value else (_document_value*-1) end), _document_note, 
					((SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1)+(case when _credit then _document_value else (_document_value*-1) end)),  0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date;
				
					/**Update bank_balance_available from movement balance */
					UPDATE bank_account 
					SET 
						bank_balance_available = (bank_balance_available + (CASE
							WHEN _credit THEN _document_value
							ELSE (_document_value * - 1)
						END))
					WHERE
						id = _bank_account_id;
                END IF;
                
                /**Update document in and out and parent id from produtct movement */
                IF(_movement_id>0) THEN
					BEGIN
						UPDATE product_movement SET 
							document_in_id=(CASE WHEN _credit=1 THEN _document_id ELSE document_in_id END), 
							document_out_id=(CASE WHEN _credit=0 THEN _document_id ELSE document_out_id END), 
							document_parent_id=_document_parent_id,
   							credit_in_initial=(CASE WHEN _credit=1 THEN _credit_initial ELSE credit_in_initial END),
  							credit_out_initial=(CASE WHEN _credit=0 THEN _credit_initial ELSE credit_out_initial END), 
                            processing_date = _processing_date_time,
                            company_id=_company_id,
                            processing=1                            
                        WHERE id=_movement_id;
                        
					UPDATE product 
					SET 
						input_movement = (CASE
							WHEN _credit = 1 THEN _credit_final
							ELSE input_movement
						END),
						output_movement = (CASE
							WHEN (_credit = 0 AND IFNULL(_is_clock_movement,0) = 0) THEN _credit_final
							ELSE output_movement
						END),
                        clock_movement = (CASE
							WHEN (_credit = 0 AND IFNULL(_is_clock_movement,0) = 1) THEN _credit_final
							ELSE clock_movement
						END)
					WHERE
						id = _product_id;
            
                    END; END IF; /**End Update document in and out and parent id from produtct movement */
			   END LOOP meuLoop;
               
			   /** Automatic Transfer Configured */
               IF(IFNULL(_bank_account_automatic_transfer_id,0)>0) THEN
			   BEGIN                  
					SELECT IFNULL(SUM(CASE WHEN credit THEN document_value ELSE (document_value*-1) END),0) INTO _document_value_transfer FROM document_movement WHERE document_parent_id=_document_parent_principal_id;
                                        
                    IF(IFNULL(_document_value_transfer,0)<>0) THEN
						BEGIN
							INSERT INTO document_parent_generate_id(inactive) select 0;
							SET _document_parent_id_transfer = LAST_INSERT_ID();                            
							
							INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id_transfer = LAST_INSERT_ID();
                            
   							SELECT 
								IFNULL(p.debit_provider_id, 0)
							INTO _debit_provider_id_transfer FROM
								company c
									INNER JOIN
								person p ON c.person_id = p.id
							WHERE
								p.bank_account_id = _bank_account_origin_id
							LIMIT 1;
                            
   							SELECT 
								c.id, c.social_name, p.id, IFNULL(p.credit_provider_id, 0)
							INTO _company_id_transfer , _company_description_transfer , _person_id_transfer , _credit_provider_id_transfer FROM
								company c
									INNER JOIN
								person p ON c.person_id = p.id
							WHERE
								p.bank_account_id = _bank_account_automatic_transfer_id
							LIMIT 1;
                            
							SELECT 
								IFNULL(financial_group_id, ''),
								IFNULL(financial_sub_group_id, '')
							INTO _financial_group_id_transfer , _financial_subgroup_id_transfer FROM
								provider
							WHERE
								id = _debit_provider_id_transfer;
                            
   							INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_number, document_value,
							document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, 
							nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, inactive, user_creation, creation_date, user_change, change_date, product_id, product_description, is_automatic_transfer, document_id_origin)
							SELECT _document_id_transfer, _document_parent_id_transfer, 2 AS document_type, (case when _document_value_transfer < 0 then 1 else 0 end) AS credit, _company_id, _debit_provider_id_transfer, _bank_account_origin_id, _bank_account_automatic_transfer_id, 
							_document_id_transfer AS document_number, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS document_value, CONCAT('TRANSF. MOVIMENTO:',_document_parent_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							2 AS document_status, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, _processing_date_time, 
							_payment_data AS payment_expiry_data, _user AS payment_user, 0 AS nfe_number, 0 AS nfe_serie_number, 0 AS generate_billet, 
							'22.000' _financial_group_id_transfer, '22.001' _financial_subgroup_id_transfer, 0 _is_product_movement, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date, _product_id, _product_description, 1 _is_automatic_transfer, _document_parent_principal_id;
                                    		                                            
							/**Insert bank account statement*/
							INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)  
							SELECT _person_id, _bank_account_origin_id, _bank_account_automatic_transfer_id, _debit_provider_id_transfer, 2 _launch_type, _document_parent_id_transfer, _document_id_transfer, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end), CONCAT('TRANSF. MOVIMENTO:',_document_parent_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							((SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_origin_id LIMIT 1)+(case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end)),  0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date;

							/**Update bank_balance_available from movement balance */
							UPDATE bank_account 
							SET 
								bank_balance_available = (bank_balance_available + (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else (_document_value_transfer * -1) end))
							WHERE
								id = _bank_account_origin_id;
                            
   							INSERT INTO document_parent_generate_id(inactive) select 0;
							SET _document_parent_id_transfer = LAST_INSERT_ID();                            
							
							INSERT INTO document_generate_id(inactive) select 0;
							SET _document_id_transfer = LAST_INSERT_ID();
                            
							SELECT 
								IFNULL(financial_group_id, ''),
								IFNULL(financial_sub_group_id, '')
							INTO _financial_group_id_transfer , _financial_subgroup_id_transfer FROM
								provider
							WHERE
								id = _credit_provider_id_transfer;
                                                                            
  							INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_number, document_value,
							document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, 
							nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, inactive, user_creation, creation_date, user_change, change_date, product_id, product_description, is_automatic_transfer, document_id_origin)
							SELECT _document_id_transfer, _document_parent_id_transfer, 2 AS document_type, (case when _document_value_transfer < 0 then 0 else 1 end) AS credit, _company_id_transfer, _credit_provider_id_transfer, _bank_account_automatic_transfer_id, _bank_account_origin_id,
							_document_id_transfer AS document_number, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS document_value, CONCAT('TRANSF. MOVIMENTO:',_document_parent_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							2 AS document_status, (case when _document_value_transfer < 0 then (_document_value_transfer * -1) else _document_value_transfer end) AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, _processing_date_time, 
							_payment_data AS payment_expiry_data, _user AS payment_user, 0 AS nfe_number, 0 AS nfe_serie_number, 0 AS generate_billet, 
							_financial_group_id_transfer, _financial_subgroup_id_transfer, 0 _is_product_movement, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date, _product_id, _product_description, 0 _is_automatic_transfer, _document_parent_principal_id;
							
							/**Insert bank account statement*/
							INSERT INTO bank_account_statement(person_id, bank_account_id, bank_account_origin_id, provider_id, launch_type, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date)  
							SELECT _person_id_transfer, _bank_account_automatic_transfer_id, _bank_account_origin_id, _debit_provider_id_transfer, 2 _launch_type, _document_parent_id_transfer, (_document_id_transfer), _document_value_transfer, CONCAT('TRANSF. MOVIMENTO:',_document_parent_id, ' >> EMPRESA: ',_company_description, ' >> ', 'EMPRESA: ', _company_description_transfer) AS document_note, 
							((SELECT IFNULL(bank_balance_available,0) FROM bank_account WHERE id=_bank_account_id LIMIT 1)+_document_value_transfer),  0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, _user AS user_change, _processing_date_time AS change_date;

							/**Update bank_balance_available from movement balance */
							UPDATE bank_account 
							SET 
								bank_balance_available = (bank_balance_available + (_document_value_transfer))
							WHERE
								id = _bank_account_automatic_transfer_id;   
                            
                            /** SET principal document parent with automatic transfer */
                            UPDATE document_movement SET is_automatic_transfer = 1 WHERE document_parent_id = _document_parent_principal_id and company_id = _company_id;
                            
					END; END IF; /** End if _document_value_transfer > 0 */
               END; END IF; /** End if _bank_account_automatic_transfer_id > 0 */
               
               CLOSE save_documents_cursor;
               
			   /** Insert send email document  */
			   IF(_company_id = 106 or _company_id = 130) THEN
					INSERT INTO send_email_document(email, document_parent_id, company_id, is_send, inactive, user_creation, creation_date, user_change, change_date) 
				    SELECT 'evmb01@protonmail.com' AS email, _document_parent_principal_id, _company_id, 0, 0, _user, _processing_date_time, _user, _processing_date_time; 
				ELSE
					INSERT INTO send_email_document(email, document_parent_id, company_id, is_send, inactive, user_creation, creation_date, user_change, change_date) 
				    SELECT p.email, _document_parent_principal_id, _company_id, 0, 0, _user, _processing_date_time, _user, _processing_date_time
				    FROM company c INNER JOIN person p ON c.person_id=p.id where c.id=_company_id and POSITION("@" IN IFNULL(p.email,''))>0;
				END IF;
                
               SELECT IFNULL(SUM(CASE WHEN credit THEN document_value ELSE (document_value*-1) END),0) INTO _document_value_transfer FROM document_movement WHERE document_parent_id=_document_parent_principal_id;
               CALL pr_process_romaneio(3, _company_id, _document_parent_principal_id, 1, 'Movimento executado com sucesso', 0); 
               			   
               SELECT _document_parent_principal_id AS document_parent_id;
                
            END; END IF; /**End Step 4 - Save Documents*/
			/**Step 5 - Get Preview*/    
		IF(5=_step_process) THEN
			BEGIN
                        
				/**Get bank_balance_available */
                SELECT ba.id AS bank_account_id, ba.description AS bank_account_description, ba.bank_balance_available FROM company co
				INNER JOIN person pe ON co.person_id=pe.id
				INNER JOIN bank_account ba ON pe.bank_account_id=ba.id
				WHERE co.id=_company_id;
				/**Get Company data */                
				SELECT 
    id, social_name AS company_description
FROM
    company
WHERE
    id = _company_id
LIMIT 1;

	/**Get preview data */
	SELECT 
		tm.document_parent_id,
		tm.document_id,
		tm.bank_account_id,
		tm.execution_order,
		IFNULL(tm.group_id,0) group_id,
		IFNULL((SELECT 
						description
					FROM
						financial_transfer_group_setting
					WHERE
						id = tm.group_id
					LIMIT 1),
				'SEM AGRUPAMENTO') AS description_group,
		tm.credit,
		tm.document_value,
		tm.document_note,
		DATE_FORMAT(tm.payment_data, '%d/%m/%Y %H:%i') AS payment_data,
		tm.credit_initial,
		tm.credit_final,
		is_product_movement,
		product_id,
		product_description,
		tm.group_execution_order,
		tm.group_item_execution_order, 
		tm.transfer_hab,
        tm.product_group_id,
        tm.product_sub_group_id
FROM
    tmp_document_movement tm
WHERE
    tm.provider_id NOT IN (
    SELECT 
		provider_id
	FROM
        provider_ungroup
	WHERE
        inactive = 0
    GROUP BY provider_id)
ORDER BY tm.is_product_movement DESC, tm.group_execution_order , tm.group_item_execution_order , tm.execution_order;
            END; END IF; /**End Step 5 - Get Preview*/
    END$$
DELIMITER ;

/**
START TRANSACTION;
SET SQL_SAFE_UPDATES = 0;

CALL pr_process_document_movement 
(
	7, -- 01 - IN _user BIGINT, 
	1884, -- 02 - IN company_id BIGINT, 
	1, -- 03 - IN _is_offline BIT, 
    '160974,170851,170852,170853,170854', -- IN _movement_ids VARCHAR(500),
    1 -- 04 - IN _is_preview BIT
);
ROLLBACK;
*/