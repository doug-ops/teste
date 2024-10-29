USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_financial_group`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_financial_group` (IN _operation INT,
												   IN _financial_group_id_from VARCHAR(7), 
												   IN _financial_group_id_to VARCHAR(7),
                                                   IN _description VARCHAR(100), 
                                                   IN _inactive INT, 
                                                   IN _user_change BIGINT)
BEGIN	
	/**Set global variables*/
    DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
	DECLARE v_financial_group_id VARCHAR(7);
    DECLARE _validation_message VARCHAR(5000) DEFAULT '';
	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
	/**End Set global variables */  

	IF(1=_operation) THEN
    select id into v_financial_group_id from financial_group where description = _description;
    
    if(v_financial_group_id is not null and v_financial_group_id <> _financial_group_id_from) then
		BEGIN
			SET _validation_message = concat('Não foi possível criar Grupo Financeiro, pois a descrição informada já existe no Grupo: ', v_financial_group_id);
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = _validation_message;
		END;
	END IF;
		IF (SELECT 1 = 1 FROM financial_group where id=_financial_group_id_from) THEN
			BEGIN
				UPDATE financial_group SET 
					description=_description, inactive=_inactive,  user_change=_user_change, change_date=_processing_date_time
				WHERE
					id=_financial_group_id_from;
            END;
		ELSE 
			BEGIN
				INSERT INTO financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date)
                select _financial_group_id_from, _description, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM financial_group where id=_financial_group_id_from) THEN
			BEGIN
				UPDATE financial_group SET inactive=_inactive,  user_change=_user_change, change_date=_processing_date_time
				WHERE id=_financial_group_id_from;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
		SET @t1 = CONCAT('SELECT gr.id, gr.description, gr.inactive, gr.user_creation, gr.creation_date, gr.user_change, gr.change_date, ',
						  'sg.id AS financial_sub_group_id, sg.description AS description_financial_sub_group, sg.inactive AS inactive_financial_sub_group,  ',
                          'sg.revenue_source_id, rs.description AS revenue_source, ',
                          'sg.revenue_type_id, rt.description AS revenue_type ',
						  'FROM financial_group gr ',
						 'LEFT JOIN financial_sub_group sg ON gr.id=sg.financial_group_id and sg.inactive=0 ',
                         'LEFT JOIN revenue_source rs ON sg.revenue_source_id=rs.id ',
                         'LEFT JOIN revenue_type rt ON sg.revenue_type_id=rt.id ',
						 ' WHERE 1=1 ', 
                         IF((_financial_group_id_from>0 and _financial_group_id_to>0), concat(' AND gr.id between ', _financial_group_id_from, ' AND ', _financial_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND gr.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND gr.inactive=', _inactive), ''),
                         ';');
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT gr.id, gr.description, gr.inactive, gr.user_change, gr.change_date, ',
						  'sg.id AS financial_sub_group_id, sg.description AS description_financial_sub_group, sg.inactive AS inactive_financial_sub_group,  ',
                          'sg.revenue_source_id, rs.description AS revenue_source, ',
                          'sg.revenue_type_id, rt.description AS revenue_type ',
						  'FROM financial_group gr ',
						 'LEFT JOIN financial_sub_group sg ON gr.id=sg.financial_group_id ',
                         'LEFT JOIN revenue_source rs ON sg.revenue_source_id=rs.id ',
                         'LEFT JOIN revenue_type rt ON sg.revenue_type_id=rt.id ',
						 ' WHERE 1=1 ', 
                         IF((_financial_group_id_from>0 and _financial_group_id_to>0), concat(' AND gr.id between ', _financial_group_id_from, ' AND ', _financial_group_id_to), ''), 
						 IF((length(IFNULL(_description,''))>0), concat(' AND gr.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND gr.inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3; 
	ELSEIF(5=_operation) THEN
			BEGIN
				SELECT 
					id, description
				FROM
					financial_group
				WHERE
					inactive=0
				ORDER BY 
					description;
            END; /**End operation 5*/				 
	ELSEIF(20=_operation) THEN
			BEGIN
				SELECT 
					max(id) as id
				FROM
					financial_group;
            END; /**End operation 20*/	
    END IF;
END$$

/**
call pr_maintenance_financial_group
(
1, -- 1 - IN _operation INT
'', -- 2 - IN _financial_group_id_from VARCHAR(7), 
0, -- 3 - IN _financial_group_id_to VARCHAR(7),
'FINANCIAL', -- 4 - IN _description VARCHAR(100), 
0, -- 5 - IN _inactive INT, 
1 -- 6 - IN _user_change BIGINT 
);
*/
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/-- CALL pr_maintenance_financial_group(1, '27.000', '27.000', 'GROUP PRIMEIRO', 0, 1  )