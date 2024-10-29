USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_document_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_document_movement`(IN _operation INT,
													IN _id BIGINT,
													IN _parent_id BIGINT,
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
                                                    IN _nfe_serie_number INT, 
                                                    IN _generate_billet BIT, 
                                                    IN _financial_group_id INT,
													IN _financial_sub_group_id INT,
													IN _inactive BIT,
                                                    IN _user_change BIGINT,
                                                    IN _creation_date DATETIME,
                                                    IN _filter_data_type INT, 
                                                    IN _moviment_type_credit BIT, 
                                                    IN _moviment_type_debit BIT, 
                                                    IN _moviment_type_nfe BIT,
                                                    IN _document_status_filter VARCHAR(50))
BEGIN	
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type) THEN
			BEGIN
				UPDATE document_movement SET 					
					credit=_credit, 
                    company_id=_company_id, 
                    provider_id=_provider_id,
					bank_account_id=_bank_account_id, 
                    document_number=_document_number, 
					document_value=_document_value, 
                    document_note=_document_note, 
					document_status=_document_status, 
                    payment_value=_payment_value, 
					payment_discount=_payment_discount, 
                    payment_extra=_payment_extra, 
					payment_residue=_payment_residue, 
                    payment_data=_payment_data, 
					payment_expiry_data=_payment_expiry_data, 
                    payment_user=_payment_user, 
					nfe_number=_nfe_number, 
                    nfe_serie_number=_nfe_serie_number, 
					generate_billet=_generate_billet, 
                    financial_group_id=_financial_group_id,
					financial_sub_group_id=_financial_sub_group_id,
                    inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE
					document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type;
            END;
		ELSE 
			BEGIN
				INSERT INTO document_movement(document_id, document_parent_id, document_type, credit, company_id, provider_id, bank_account_id, 
                document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra,
                payment_residue, payment_data, payment_expiry_data, payment_user, nfe_number, nfe_serie_number, generate_billet, 
                financial_group_id, financial_sub_group_id, inactive, user_creation, creation_date, user_change, change_date)
                select _id, _parent_id, _document_type, _credit, _company_id, _provider_id, _bank_account_id, 
                _document_number, _document_value, _document_note, _document_status, _payment_value, _payment_discount, 
                _payment_extra, _payment_residue, _payment_data, _payment_expiry_data, _payment_user, _nfe_number, 
                _nfe_serie_number, _generate_billet, _financial_group_id, _financial_sub_group_id,
                _inactive, _user_change, _creation_date, _user_change, _creation_date;
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type) THEN
			BEGIN
				UPDATE document_movement SET inactive=_inactive, user_change=_user_change, change_date=now()
				WHERE document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type;
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
			document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type;
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
		IF (SELECT 1 = 1 FROM document_movement WHERE document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type) THEN
			BEGIN
				UPDATE document_movement SET document_parent_id=0, user_change=_user_change, change_date=now()
				WHERE document_id=_id AND document_parent_id=_parent_id AND document_type=_document_type;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
    END IF;
END$$

-- CALL pr_maintenance_document_movement(1, 8, 3, 1, 0, 11, 2002, 11, '745', 1000.0, 'TRANSF. 745', 1, 1000.0, 0.0, 0.0, 0, '2015-09-01 07:59:00', '2015-09-01 07:59:00', 9999, 0, 0, 0, 13, 13, 0, 0, '2015-09-01 07:59:00', null, null, null, null, null  );
-- select count(*) from document_movement;

/**
call pr_maintenance_financial_group
(
5, -- 1 - IN _operation INT
0, -- 2 - IN _financial_group_id_from int, 
0, -- 3 - IN _financial_group_id_to int,
'', -- 4 - IN _description varchar(100), 
0, -- 5 - IN _inactive int, 
1 -- 6 - IN _user_change BIGINT 
);
*/
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
    8 - ungroup operation
*/