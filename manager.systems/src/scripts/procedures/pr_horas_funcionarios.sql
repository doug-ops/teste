USE `rochapisos`;
DROP procedure IF EXISTS `pr_horas_funcionarios`;

DELIMITER $$
USE `rochapisos`$$

CREATE PROCEDURE `pr_horas_funcionarios`(IN _operation INT,
												IN _user_id BIGINT,
												IN _inactive INT, 
                                                IN _user_change BIGINT,
                                                IN _date_from DATETIME,
												IN _date_to DATETIME)
BEGIN
	IF (1=_operation) THEN /** SAVE */
		IF (SELECT 1 = 1 FROM user_company WHERE user_id=_user_id AND company_id=_company_id limit 1) THEN
			BEGIN
				UPDATE user_company SET inactive=_inactive, user_change=_user_change, change_date=NOW()
				WHERE user_id=_user_id AND company_id=_company_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO user_company
				(
					user_id, company_id, inactive, user_creation, creation_date, user_change, change_date
				)
				SELECT _user_id, _company_id, _inactive, _user_change, NOW(), _user_change, NOW();
			END;/** End insert */
		END IF; /** End operation 1 */
	ELSEIF (2=_operation) THEN /** INACTIVE BY USER */
		BEGIN
			UPDATE user_company SET inactive=_inactive, user_change=_user_change, change_date=NOW()
			WHERE user_id=_user_id;
		END; /** End operation 2 */
	ELSEIF(3=_operation) THEN /** GET */
		BEGIN
			SELECT 
				uc.user_id,
				c.id company_id, 
                c.process_movement_automatic,
				p.person_type_id,
                (case when p.person_type_id = 1 then 'Empresa' 
                     when p.person_type_id = 2 then 'Operador' 
                     when p.person_type_id = 3 then 'Interno'
                     else 'Outro' end) person_type_description,
				uc.inactive,
				c.social_name company_description, 
                (CASE WHEN x.expiry_data IS NULL THEN ""  ELSE DATE_FORMAT(x.expiry_data,'%d/%m/%Y') END) AS expiry_data_string
				FROM company c
                INNER JOIN person p ON c.person_id = p.id
				INNER JOIN user_company uc on c.id=uc.company_id                  
				LEFT JOIN 
				(
					SELECT 
					cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id, 
					MIN(cm.expiry_data) expiry_data
					FROM company_movement_execution cm
					INNER JOIN user_company uc ON cm.company_id=uc.company_id AND user_id=_user_id
					WHERE cm.execution_date IS NULL
                    AND cm.inactive=0
					GROUP BY cm.company_id, cm.bank_account_origin_id, cm.bank_account_destiny_id
				) x ON c.id = x.company_id
				WHERE 
				uc.user_id = _user_id
				AND uc.inactive = 0
                AND p.inactive = 0
				ORDER BY x.expiry_data;
		END; /** End operation 3 */
	ELSEIF (4=_operation) THEN /** GET ALL */
		BEGIN
			SELECT id_user, name_user, data_processo, hora_manha_entrada,hora_manha_saida,hora_tarde_entrada,hora_tarde_saida,
					hora_extra_entrada,hora_extra_saida
		    FROM registro_ponto_horas WHERE data_processo between DATE(_date_from) and DATE(_date_to) and id_user = _user_id;
		END; /** End operation 4 */
	END IF; 
END$$