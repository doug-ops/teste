USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_integration_systems`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_integration_systems` (IN _operation INT, 
													   IN _object_id BIGINT, 
													   IN _object_type VARCHAR(10), 
                                                       IN _integration_system_id INT, 
                                                       IN _legacy_id VARCHAR(20), 
                                                       IN _inactive INT, 
                                                       IN _user_change BIGINT, 
													   IN _change_date DATETIME)
	BEGIN
		IF(_operation=1) THEN
			BEGIN
				IF ('COM'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM company_integration_system WHERE company_id = _object_id AND integration_system_id = _integration_system_id AND legacy_id = _legacy_id) THEN
							BEGIN
								UPDATE 
									company_integration_system
								SET 									
                                    inactive = _inactive,
                                    user_change = _user_change, 
									change_date = _change_date
								WHERE
									company_id=_object_id
                                    AND integration_system_id = _integration_system_id
                                    AND legacy_id = _legacy_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO company_integration_system(company_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists company integration system */ 
					END; /** End operation type 1 Company */
				ELSEIF ('FOR'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM provider_integration_system WHERE provider_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									provider_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									provider_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO provider_integration_system(provider_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists provider integration system */ 
					END;
				ELSEIF ('BANKAC'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM bank_account_integration_system WHERE bank_account_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									bank_account_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									bank_account_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO bank_account_integration_system(bank_account_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists bank account integration system */ 
					END;
				ELSEIF ('FINGRU'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM financial_group_integration_system WHERE financial_group_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									financial_group_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									financial_group_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists financial group integration system */ 
					END;
				ELSEIF ('FINSUBGRU'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM financial_sub_group_integration_system WHERE financial_sub_group_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									financial_sub_group_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									financial_sub_group_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists financial sub group integration system */ 
					END;
				ELSEIF ('PROGRU'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM product_group_integration_system WHERE group_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									product_group_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									group_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO product_group_integration_system(group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists provider integration system */ 
					END;
				ELSEIF ('PROSUBGRU'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM product_sub_group_integration_system WHERE sub_group_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									product_sub_group_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									sub_group_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists provider integration system */ 
					END;
				ELSEIF ('PRO'=_object_type) THEN
					BEGIN
						IF (SELECT 1 = 1 FROM product_integration_system WHERE product_id=_object_id 
										      AND integration_system_id=_integration_system_id) THEN
							BEGIN
								UPDATE 
									product_integration_system
								SET 
									legacy_id=_legacy_id, 
                                    inactive=_inactive,
                                    user_change=_user_change, 
									change_date=_change_date
								WHERE
									product_id=_object_id
                                    AND integration_system_id = _integration_system_id;
                            END;
						ELSE
							BEGIN
								INSERT INTO product_integration_system(product_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
								select _object_id, _integration_system_id, _legacy_id, _inactive, _user_change, _change_date, _user_change, _change_date;
                            END;
						END IF; /**End if exists provider integration system */ 
					END;
				END IF;
			END; /** End operation type 1 */
		ELSEIF(_operation=4) THEN
			BEGIN
				SET @t1 = CONCAT('SELECT ',
                'i.id as integration_system_id, i.description, ',
				IF(('COM'=_object_type), 'c.company_id as object_id, ''COM'' as object_type, c.legacy_id ',' '), 
                IF(('FOR'=_object_type), 'f.provider_id as object_id, ''FOR'' as object_type, f.legacy_id ',' '),
                IF(('BANKAC'=_object_type), 'ba.bank_account_id as object_id, ''BANKAC'' as object_type, ba.legacy_id ',' '), 
                IF(('FINGRU'=_object_type), 'fgru.financial_group_id as object_id, ''FINGRU'' as object_type, fgru.legacy_id ',' '), 
                IF(('FINSUBGRU'=_object_type), 'fsub.financial_sub_group_id as object_id, ''FINSUBGRU'' as object_type, fsub.legacy_id ',' '), 
                IF(('PROGRU'=_object_type), 'pgru.group_id as object_id, ''PROGRU'' as object_type, pgru.legacy_id ',' '), 
                IF(('PROSUBGRU'=_object_type), 'psub.sub_group_id as object_id, ''PROSUBGRU'' as object_type, psub.legacy_id ',' '), 
                IF(('PRO'=_object_type), 'pro.product_id as object_id, ''PRO'' as object_type, pro.legacy_id ',' '), 
				'FROM ', 
                'integration_system i ',
				IF(('COM'=_object_type), 'INNER JOIN company_integration_system c on i.id=c.integration_system_id ', ' '), 
				IF(('FOR'=_object_type), 'INNER JOIN provider_integration_system f on i.id=f.integration_system_id ', ' '),
                IF(('BANKAC'=_object_type), 'INNER JOIN bank_account_integration_system ba on i.id=ba.integration_system_id ', ' '), 
                IF(('FINGRU'=_object_type), 'INNER JOIN financial_group_integration_system fgru on i.id=fgru.integration_system_id ', ' '), 
                IF(('FINSUBGRU'=_object_type), 'INNER JOIN financial_sub_group_integration_system fsub on i.id=fsub.integration_system_id ', ' '), 
                IF(('PROGRU'=_object_type), 'INNER JOIN product_group_integration_system pgru on i.id=pgru.integration_system_id ', ' '), 
                IF(('PROSUBGRU'=_object_type), 'INNER JOIN product_sub_group_integration_system psub on i.id=psub.integration_system_id ', ' '), 
                IF(('PRO'=_object_type), 'INNER JOIN product_integration_system pro on i.id=pro.integration_system_id ', ' '), 
				'WHERE ', 
                IF(('COM'=_object_type), concat(' c.company_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND c.inactive= ', _inactive), '')),' '),
                IF(('FOR'=_object_type), concat(' f.provider_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND f.inactive= ', _inactive), '')),' '),
                IF(('BANKAC'=_object_type), concat(' ba.bank_account_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND ba.inactive= ', _inactive), '')),' '),
                IF(('FINGRU'=_object_type), concat(' fgru.financial_group_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND fgru.inactive= ', _inactive), '')),' '),
                IF(('FINSUBGRU'=_object_type), concat(' fsub.financial_sub_group_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND fsub.inactive= ', _inactive), '')),' '),
                IF(('PROGRU'=_object_type), concat(' pgru.group_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND pgru.inactive= ', _inactive), '')),' '),
                IF(('PROSUBGRU'=_object_type), concat(' psub.sub_group_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND psub.inactive= ', _inactive), '')),' '),
                IF(('PRO'=_object_type), concat(' pro.product_id=', _object_id, IF((_inactive IS NOT NULL and _inactive<2), concat(' AND pro.inactive= ', _inactive), '')),' ')
                );
				-- select @t1;
				PREPARE stmt3 FROM @t1;
				EXECUTE stmt3;
				DEALLOCATE PREPARE stmt3;
            END; /**End operation 5*/
		ELSEIF(_operation=5) THEN
			BEGIN
				SELECT 
					id, description
				FROM
					integration_system
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 5*/
		ELSEIF(_operation=6) THEN
			BEGIN
				IF ('COM'=_object_type) THEN
					BEGIN
						DELETE FROM  company_integration_system WHERE company_id = _object_id;
					END;
				ELSEIF ('FOR'=_object_type) THEN
					BEGIN
						UPDATE 
							provider_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							provider_id=_object_id;
					END;
				ELSEIF ('BANKAC'=_object_type) THEN
					BEGIN
						UPDATE 
							bank_account_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							bank_account_id=_object_id;
					END;
				ELSEIF ('FINGRU'=_object_type) THEN
					BEGIN
						UPDATE 
							financial_group_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							financial_group_id=_object_id;
					END;
				ELSEIF ('FINSUBGRU'=_object_type) THEN
					BEGIN
						UPDATE 
							financial_sub_group_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							financial_sub_group_id=_object_id;
					END;
				ELSEIF ('PROGRU'=_object_type) THEN
					BEGIN
						UPDATE 
							product_group_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							group_id=_object_id;
					END;
				ELSEIF ('PROSUBGRU'=_object_type) THEN
					BEGIN
						UPDATE 
							product_sub_group_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							sub_group_id=_object_id;
					END;
				ELSEIF ('PRO'=_object_type) THEN
					BEGIN
						UPDATE 
							product_integration_system
						SET 
                            inactive=_inactive,
                            user_change=_user_change, 
							change_date=_change_date
						WHERE 
							product_id=_object_id;
					END; /**End update product */
				END IF; /**End if delete object type */
            END; /**End operation 6*/
		END IF; /** End operation */
	END$$

call pr_maintenance_integration_systems
(
	6,  -- 01 - IN _operation INT, 
    3, -- 02 - IN _object_id BIGINT, 
	'COM', -- 03 - IN _object_type VARCHAR(10), 
    1, -- 04 - IN integration_system_id INT, 
    '1', -- 05 - IN legacy_id VARCHAR(10) ,
    1, -- 06 - IN _inactive INT, 
    1, -- 07 - IN _user_change BIGINT, 
	now() -- 08 - IN _change_date DATETIME
);