USE `manager`;
DROP procedure IF EXISTS `pr_process_document_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_process_document_movement`(
													IN _operation INT, 
                                                    IN _user BIGINT
											   )
BEGIN	        
   	DECLARE _process_movements BIT;
	DECLARE _timezone_database TINYINT DEFAULT 0;
    DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE _validation_message VARCHAR(5000) DEFAULT '';
    
    DROP TEMPORARY TABLE IF EXISTS tmp_movement;
    DROP TEMPORARY TABLE IF EXISTS tmp_document_movement;
    DROP TEMPORARY TABLE IF EXISTS tmp_financial_transfer;    

   	/** Set global variables */
    SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR); 
	SELECT IFNULL(process_movements,0), IFNULL(timezone_database,0) INTO _process_movements, _timezone_database from config_systems LIMIT 1;
    	
    /**Verify if process is enabled*/
	IF(0=_process_movements) THEN
		BEGIN
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Processamento de movimentos inativo.';
		END;
	END IF; /**End if process movements is active */
    
    /**Temporary table to insert documents*/
    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_document_movement 
	(
		movement_id BIGINT NOT NULL,
        is_product_moviment BIT NOT NULL,
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
		financial_group_id INT NULL,
		financial_sub_group_id INT NULL
	);
    
    /**Temporary table to handle confg transfer*/
    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_financial_transfer
	(
		execution_order TINYINT NOT NULL, 
        company_id BIGINT NOT NULL,
		group_id INT NOT NULL,
		bank_account_origin_id INT NOT NULL,
		bank_account_destiny_id INT NOT NULL,
		group_execution_order TINYINT NOT NULL,
		group_description VARCHAR(100) NOT NULL,
		group_item_id INT NOT NULL,
        group_item_execution_order TINYINT NOT NULL,
        description_transfer VARCHAR(100) NOT NULL,
        provider_id BIGINT NOT NULL,
        transfer_type TINYINT NOT NULL,
		value_transfer DECIMAL(19,2) NOT NULL,
		credit_debit BIT NOT NULL,
		transfer_state TINYINT NOT NULL,
		is_over_total BIT NOT NULL,
		expense TINYINT NOT NULL,
		is_use_remaining_balance BIT NOT NULL
    );
    
    /**Temporary table to handler process*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_movement
	(
		movement_id BIGINT NOT NULL,
		document_id BIGINT NOT NULL,
		document_parent_id BIGINT NOT NULL,
		product_id BIGINT NOT NULL,
		company_id BIGINT NOT NULL,
		bank_account_id INT NOT NULL,
		initial_credit BIGINT NOT NULL,
		final_credit BIGINT NOT NULL,
		conversion_factor DECIMAL(19,2) NOT NULL,
		total_document DECIMAL(19,2) NULL, 
		credit BIT NULL, -- 0 - Debit, 1 - Credit
		movement_type INT NOT NULL, -- 1 - Input, 2 Output
		provider_id BIGINT NOT NULL,				
		reading_date DATETIME NOT NULL,
		document_note VARCHAR(100) NOT NULL, 
		movement_error BIT NULL, 
        execution_order TINYINT NOT NULL,
        is_product_moviment BIT NOT NULL,
        is_update BIT NOT NULL
	);
    
    /**Operation to execute config transfer*/
	IF(10=_operation) THEN
		BEGIN			          
			DECLARE _count TINYINT DEFAULT 0;
            DECLARE _count_financial_transfer TINYINT DEFAULT 0;
            DECLARE _count_document TINYINT DEFAULT 0;
            DECLARE _has_config_transfer BIT DEFAULT 0;
            DECLARE _document_id BIGINT DEFAULT 0;
            DECLARE _document_parent_id BIGINT DEFAULT 0;
            DECLARE _is_product_moviment BIT DEFAULT 0;
            DECLARE _balance_processing DECIMAL(19,2) DEFAULT 0;
            DECLARE _negative_close BIT DEFAULT 0;
            
            /**VARIABLES CURSOR*/
            DECLARE _movement_id BIGINT;
            DECLARE _bank_account_origin_id INT;
			DECLARE _bank_account_destiny_id INT;
			DECLARE _company_id BIGINT;
            DECLARE _credit_provider_id BIGINT; 
			DECLARE _debit_provider_id BIGINT;
			DECLARE _group_id INT;
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
   			DECLARE	_process_total DECIMAL(19,2) DEFAULT 0;
            /**Cursor to search products and config transfer to process*/            
            DECLARE transfer_cursor CURSOR FOR 
            SELECT 
				x.movement_id, 
                x.bank_account_origin_id, 
                x.bank_account_destiny_id,
                x.company_id, 
                x.credit_provider_id, 
                x.debit_provider_id, 
				y.group_id,
				y.group_execution_order, 
				y.group_description, 
				y.group_item_id, 
				y.group_item_execution_order, 
				y.description_transfer, 
				y.provider_id, 
				y.transfer_type, 
				y.value_transfer, 
				y.credit_debit, 
				y.transfer_state, 
				y.is_over_total, 
				y.expense, 
				y.is_use_remaining_balance
			FROM 
            (
				SELECT 
					pm.id as movement_id, pe.bank_account_id as bank_account_origin_id, 0 as bank_account_destiny_id, co.id as company_id, IFNULL(pe.credit_provider_id,0) as credit_provider_id, IFNULL(pe.debit_provider_id,0) as debit_provider_id
				FROM  	
					product_movement pm
					INNER JOIN product po ON pm.product_id=po.id
					INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
					INNER JOIN company co ON pm.company_description=co.social_name
					INNER JOIN person pe ON co.person_id=pe.id
				WHERE
					pm.processing=0
				ORDER BY 
					pm.id
				LIMIT 1
            ) x
            INNER JOIN 
            (
				select 
					ftg.id as group_id,
					ftg.bank_account_origin_id, 
					ftg.bank_account_destiny_id, 
					ftg.execution_order as group_execution_order, 
					ftg.description as group_description, 
					ftgi.id as group_item_id, 
					ftgi.execution_order as group_item_execution_order, 
					ftgi.description_transfer, 
					ftgi.provider_id, 
					ftgi.transfer_type, 
					ftgi.value_transfer, 
					ftgi.credit_debit, 
					ftgi.transfer_state, 
					ftgi.is_over_total, 
					ftgi.expense, 
					ftgi.is_use_remaining_balance 
				from 
					financial_transfer_group_setting ftg
					inner join financial_transfer_group_item_setting ftgi on ftg.id=ftgi.group_id
					left join financial_transfer_product_setting ftp on ftg.id=ftp.group_id
					and ftg.bank_account_origin_id=ftp.bank_account_origin_id 
					and ftg.bank_account_destiny_id=ftp.bank_account_destiny_id
			) y 
            on x.bank_account_origin_id=y.bank_account_origin_id
			and x.bank_account_destiny_id=y.bank_account_destiny_id
            ORDER BY y.group_execution_order ASC, y.group_item_execution_order;
            
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET _has_config_transfer=1;
			OPEN transfer_cursor;
            meuLoop: LOOP
			FETCH transfer_cursor INTO 
			_movement_id, 
			_bank_account_origin_id, 
			_bank_account_destiny_id,
			_company_id, 
			_credit_provider_id, 
			_debit_provider_id, 
			_group_id,
			_group_execution_order, 
			_group_description, 
			_group_item_id, 
			_group_item_execution_order, 
			_description_transfer, 
			_provider_id, 
			_transfer_type, 
			_value_transfer, 
			_credit_debit, 
			_transfer_state, 
			_is_over_total, 
			_expense, 
			_is_use_remaining_balance;
            
			IF _has_config_transfer = 1 THEN
				LEAVE meuLoop;
			END IF;
            
            IF(0=_count_document) THEN
				BEGIN
					SET _count_document = 1;
                END;
			END IF;				
            
            INSERT INTO tmp_document_movement(movement_id, is_product_moviment, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
			document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id)
			SELECT pm.id AS movement_id, 1 AS is_product_moviment, (CASE WHEN IFNULL(pm.document_in_id,0)=0 THEN DATE_FORMAT(DATE_ADD(_processing_date_time, INTERVAL _count_document SECOND), '%Y%m%d%H%i%s') ELSE pm.document_in_id END) AS document_id,
			(CASE WHEN IFNULL(pm.document_parent_id,0)=0 THEN DATE_FORMAT(_processing_date_time, '%Y%m%d%H%i%s') ELSE pm.document_parent_id END) AS document_parent_id,
			1 AS document_type, 1 AS credit, _company_id AS company_id, _credit_provider_id AS provider_id, _bank_account_origin_id AS bank_account_id, 
            ((IFNULL(pm.credit_in_final,0)-IFNULL(po.input_movement,0))*IFNULL(po.conversion_factor,su.conversion_factor)) AS document_value,
            CONCAT(CAST(pm.product_id AS CHAR), '/EI:',CAST(IFNULL(po.input_movement,0) AS CHAR),'/EF:',CAST(IFNULL(pm.credit_in_final,0) AS CHAR)) AS document_note,
            (CASE WHEN movement_type='A' THEN 2 ELSE 1 END) AS document_status, 0 AS payment_residue, pm.reading_date AS payment_data, 0 AS financial_group_id, 0 AS financial_sub_group_id
			FROM  	
				product_movement pm
				INNER JOIN product po ON pm.product_id=po.id
				INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
			WHERE
				pm.id=_movement_id;
                
			SET _count_document = (_count_document+1);
                
			INSERT INTO tmp_document_movement(movement_id, is_product_moviment, document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
			document_value, document_note, document_status, payment_residue, payment_data, financial_group_id, financial_sub_group_id)
			SELECT pm.id AS movement_id, 1 AS is_product_moviment, (CASE WHEN IFNULL(pm.document_in_id,0)=0 THEN DATE_FORMAT(DATE_ADD(_processing_date_time, INTERVAL _count_document SECOND), '%Y%m%d%H%i%s') ELSE pm.document_in_id END) AS document_id,
			(CASE WHEN IFNULL(pm.document_parent_id,0)=0 THEN DATE_FORMAT(_processing_date_time, '%Y%m%d%H%i%s') ELSE pm.document_parent_id END) AS document_parent_id,
			1 AS document_type, 0 AS credit, _company_id AS company_id, _credit_provider_id AS provider_id, _bank_account_origin_id AS bank_account_id, 
            ((IFNULL(pm.credit_out_final,0)-IFNULL(po.output_movement,0))*IFNULL(po.conversion_factor,su.conversion_factor)) AS document_value,
            CONCAT(CAST(pm.product_id AS CHAR), '/SI:',CAST(IFNULL(po.output_movement,0) AS CHAR),'/SF:',CAST(IFNULL(pm.credit_out_final,0) AS CHAR)) AS document_note,
            (CASE WHEN movement_type='A' THEN 2 ELSE 1 END) AS document_status, 0 AS payment_residue, pm.reading_date AS payment_data, 0 AS financial_group_id, 0 AS financial_sub_group_id
			FROM  	
				product_movement pm
				INNER JOIN product po ON pm.product_id=po.id
				INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
			WHERE
				pm.id=_movement_id;
                
			select * from tmp_document_movement;
            
			INSERT INTO tmp_movement(movement_id, document_id, document_parent_id, product_id, company_id, bank_account_id, initial_credit, final_credit, conversion_factor, total_document, credit, movement_type, provider_id, reading_date, document_note, execution_order, is_product_moviment, is_update)
			SELECT pm.id, (CASE WHEN IFNULL(pm.document_in_id,0)=0 THEN DATE_FORMAT(DATE_ADD(_processing_date_time, INTERVAL _count_document SECOND), '%Y%m%d%H%i%s') ELSE pm.document_in_id END) AS document_in_id, 
			(CASE WHEN IFNULL(pm.document_parent_id,0)=0 THEN DATE_FORMAT(_processing_date_time, '%Y%m%d%H%i%s') ELSE pm.document_parent_id END) AS document_parent_id,
			pm.product_id, _company_id AS company_id, _bank_account_origin_id AS bank_account_id , IFNULL(po.input_movement,0) AS initial_credit, 
			IFNULL(pm.credit_in_final,0) AS final_credit, IFNULL(po.conversion_factor,su.conversion_factor) AS conversion_factor, 
			NULL AS total_document, 1 AS credit, (CASE WHEN movement_type='A' THEN 2 ELSE 1 END) AS movement_type, _credit_provider_id AS provider_id, pm.reading_date, 
			CONCAT(CAST(pm.product_id AS CHAR), '/SI:',CAST(IFNULL(po.output_movement,0) AS CHAR),'/SF:',CAST(IFNULL(pm.credit_out_final,0) AS CHAR)) AS document_note,  
			_count_document AS _execution_order, 1 AS is_product_moviment, (CASE WHEN IFNULL(pm.document_in_id,0)=0 THEN 0 ELSE 1 END) AS is_update
			FROM  	
				product_movement pm
				INNER JOIN product po ON pm.product_id=po.id
				INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
			WHERE
				pm.id=_movement_id;
                        
			SET _count_document = (_count_document+1);
                    
			INSERT INTO tmp_movement(movement_id, document_id, document_parent_id, product_id, company_id, bank_account_id, initial_credit, final_credit, conversion_factor, total_document, credit, movement_type, provider_id, reading_date, document_note, execution_order, is_product_moviment, is_update)
			SELECT pm.id, (CASE WHEN IFNULL(pm.document_out_id,0)=0 THEN DATE_FORMAT(DATE_ADD(_processing_date_time, INTERVAL _count_document SECOND), '%Y%m%d%H%i%s') ELSE pm.document_out_id END) AS document_out_id,  
			(CASE WHEN IFNULL(pm.document_parent_id,0)=0 THEN DATE_FORMAT(_processing_date_time, '%Y%m%d%H%i%s') ELSE pm.document_parent_id END) AS document_parent_id,
			pm.product_id, _company_id AS company_id, _bank_account_origin_id AS bank_account_id , IFNULL(po.output_movement,0) AS initial_credit, 
			IFNULL(pm.credit_out_final,0) AS final_credit, IFNULL(po.conversion_factor,su.conversion_factor) AS conversion_factor, 
			NULL AS total_document, 0 AS credit, (CASE WHEN movement_type='A' THEN 2 ELSE 1 END) AS movement_type, _debit_provider_id AS provider_id, pm.reading_date, 
			CONCAT(CAST(pm.product_id AS CHAR), '/SI:',CAST(IFNULL(po.output_movement,0) AS CHAR),'/SF:',CAST(IFNULL(pm.credit_out_final,0) AS CHAR)) AS document_note, 
			_count_document AS _execution_order, 1 AS is_product_moviment, (CASE WHEN IFNULL(pm.document_out_id,0)=0 THEN 0 ELSE 1 END) AS is_update
			FROM  	
				product_movement pm
				INNER JOIN product po ON pm.product_id=po.id
				INNER JOIN product_sub_group su ON po.product_sub_group_id = su.id
			WHERE
				pm.id=_movement_id;
                        
			UPDATE 
				tmp_movement 
			SET 
				total_document=((final_credit-initial_credit)*conversion_factor), 
				-- .=(((final_credit-initial_credit)*conversion_factor)>=0), 
				movement_error=(initial_credit>final_credit);
                    
			/**Verify if exists erro initial movement major final movement*/
			SELECT 
				GROUP_CONCAT(DISTINCT CASE WHEN movement_error=1 AND credit=1 
					THEN CONCAT('Movimento de entrada inicial: ', initial_credit, ' maior que Movimento de entrada final: ', final_credit)
					WHEN movement_error=1 AND credit=0 
					THEN CONCAT('Movimento de saida inicial: ', initial_credit, ' maior que Movimento de saida final: ', final_credit)
					ELSE '' END) INTO _validation_message
			FROM tmp_movement;
                    
			IF(LENGTH(_validation_message)>0) THEN
				BEGIN
					SIGNAL SQLSTATE '45000'
					SET MESSAGE_TEXT = _validation_message;
				END;
			END IF; 
			/**End if Verify if exists erro initial movement major final movement*/
            
            SET _count_financial_transfer = (_count_financial_transfer + 1);
            /**Insert financial transfer config*/
            INSERT INTO tmp_financial_transfer(execution_order, company_id, group_id, bank_account_origin_id, bank_account_destiny_id, group_execution_order,
			group_description, group_item_id, group_item_execution_order, description_transfer, provider_id, transfer_type,
			value_transfer, credit_debit, transfer_state, is_over_total, expense, is_use_remaining_balance)
            SELECT _count_financial_transfer, _company_id, _group_id, _bank_account_origin_id, _bank_account_destiny_id, _group_execution_order,
			_group_description, _group_item_id, _group_item_execution_order, _description_transfer, _provider_id, _transfer_type,
			_value_transfer, _credit_debit, _transfer_state, _is_over_total, _expense, _is_use_remaining_balance;
            
		  END LOOP meuLoop;
          CLOSE transfer_cursor;
                        
		IF(0=_movement_id) THEN
			BEGIN
				SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Nenhum processamento de movimentos pendente de execucao.';
			END;
		END IF; /**End if process movements is active */
          
		/**Process financial transfer*/
        SELECT negative_close INTO _negative_close FROM company where id=_company_id LIMIT 1;
        
        SELECT SUM(CASE WHEN credit=1 THEN total_document ELSE total_document * -1 END) INTO _balance_processing FROM tmp_movement;
        IF(_balance_processing<0 && _negative_close=0) THEN 
			BEGIN
                SET _validation_message = CONCAT('Movimento não pode ser processado com saldo negativo: ',_balance_processing); 
				SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = _validation_message; 
			END; END IF;
            
		SET _count = 1;
        /**Populate document_parent_id*/
        SELECT document_parent_id INTO _document_parent_id FROM tmp_movement WHERE execution_order=_count;
		SELECT _document_parent_id;
        
        SELECT * FROM tmp_financial_transfer;
		select * from tmp_movement;

        /**While process financial transfer */
        WHILE _count <= _count_financial_transfer DO
			SET _count_document = (_count_document+1);
            
			SET _count = (_count + 1);
		END WHILE; /**End While process financial transfer */            
            
		SET _count = 1;
		/**While Save documents */
        WHILE _count <= _count_document DO
			SELECT document_id, document_parent_id, IFNULL(is_product_moviment,0) AS is_product_moviment INTO _document_id, _document_parent_id, _is_product_moviment FROM  tmp_movement WHERE execution_order=_count;
                
			IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_document_id AND document_parent_id=_document_parent_id LIMIT 1) THEN
				BEGIN /**Update document*/
					UPDATE document_movement dm
					INNER JOIN 
					(SELECT document_id, document_parent_id, 1 AS document_type, credit, company_id, provider_id, bank_account_id, 
					document_id AS document_number, total_document AS document_value, document_note, movement_type AS document_status, 
					total_document AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, reading_date AS payment_data, 
					reading_date AS payment_expiry_data, _user AS payment_user, NULL AS nfe_number, NULL AS nfe_serie_number, 0 AS generate_billet, 
					NULL AS financial_group_id, NULL AS financial_sub_group_id, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, 
					_user AS user_change, _processing_date_time AS change_date
					FROM 
						tmp_movement
					WHERE 
						execution_order=_count) m
					ON dm.document_id=m.document_id AND dm.document_parent_id=m.document_parent_id
					SET 
					dm.document_type=m.document_type, dm.credit=m.credit, dm.company_id=m.company_id, dm.provider_id=m.provider_id, dm.bank_account_id=m.bank_account_id, 
					dm.document_number=m.document_id, dm.document_value=m.document_value, dm.document_note=m.document_note, dm.document_status=m.document_status, 
					dm.payment_value=m.document_value, dm.payment_discount=m.payment_discount, dm.payment_extra=m.payment_extra, dm.payment_residue=m.payment_residue, 
					dm.payment_data=m.payment_data, dm.payment_expiry_data=m.payment_data, dm.payment_user=m.payment_user, dm.nfe_number=m.nfe_number, dm.nfe_serie_number=m.nfe_serie_number, 
					dm.generate_billet=m.generate_billet, dm.financial_group_id=m.financial_group_id, dm.financial_sub_group_id=m.financial_sub_group_id,  
					dm.inactive=m.inactive, dm.user_change=m.user_change, dm.change_date=m.change_date;
				END; /**End Update document*/
			ELSE
				BEGIN /**Insert document*/
					INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
					document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, 
					payment_data, payment_expiry_data, payment_user, nfe_number, nfe_serie_number, generate_billet, financial_group_id, 
					financial_sub_group_id, inactive, user_creation, creation_date, user_change, change_date)
					SELECT document_id, document_parent_id, 1 AS document_type, credit, company_id, provider_id, bank_account_id, 
					document_id AS document_number, total_document AS document_value, document_note, movement_type AS document_status, 
					total_document AS payment_value, 0 AS payment_discount, 0 AS payment_extra, 0 AS payment_residue, reading_date AS payment_data, 
					reading_date AS payment_expiry_data, _user AS payment_user, NULL AS nfe_number, NULL AS nfe_serie_number, 0 AS generate_billet, 
					NULL AS financial_group_id, NULL AS financial_sub_group_id, 0 AS inactive, _user AS user_creation, _processing_date_time AS creation_date, 
					_user AS user_change, _processing_date_time AS change_date
					FROM 
						tmp_movement0
					WHERE 
						execution_order=_count;
				END; /**End Insert document*/
			END IF;
            
            IF(_is_product_moviment=1) THEN
				BEGIN /**Update document in and out and parent id from produtct movement */
					UPDATE product_movement 
					INNER JOIN 
					(
						SELECT movement_id, document_id, document_parent_id as document_parent, credit
						FROM 
							tmp_movement
						WHERE 
							execution_order=_count) m
					ON id=m.movement_id
					SET document_in_id=(CASE WHEN credit=1 THEN m.document_id ELSE document_in_id END), document_out_id=(CASE WHEN credit=0 THEN m.document_id ELSE document_out_id END), document_parent_id=m.document_parent;
            END; END IF; /**End Update document in and out and parent id from produtct movement */            
			SET _count = (_count + 1);
		END WHILE; /**End While Save documents */
          
                
                DROP TEMPORARY TABLE IF EXISTS tmp_financial_transfer;
                DROP TEMPORARY TABLE IF EXISTS tmp_movement;
           
           /**
           CREATE TEMPORARY TABLE tmp_movement
            select * from tmp_movement;
			DROP TEMPORARY TABLE tmp_movement;
            */
		END; /** End operation = 10 */
    END IF; /** End if operarion */
END$$

SET SQL_SAFE_UPDATES = 0;
CALL pr_process_document_movement(10, 1);