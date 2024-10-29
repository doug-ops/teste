USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_financial_sub_group`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_financial_sub_group` (IN _operation INT,
													   IN _financial_sub_group_id_from VARCHAR(7), 
													   IN _financial_sub_group_id_to VARCHAR(7),
                                                       IN _financial_group_id varchar(100),
													   IN _description varchar(100), 
													   IN _inactive int, 
													   IN _user_change BIGINT, 
                                                       IN _revenue_source INT,
													   IN _revenue_type INT)
BEGIN	
	/**Set global variables*/
    DECLARE _timezone_database TINYINT DEFAULT 0;
	DECLARE _processing_date_time DATETIME DEFAULT NOW();
    DECLARE v_financial_sub_group_id VARCHAR(7);
    DECLARE _validation_message VARCHAR(5000) DEFAULT '';
	SELECT IFNULL(timezone_database,0) INTO _timezone_database from config_systems LIMIT 1;
	SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
	/**End Set global variables */  
	
	IF(1=_operation) THEN
	select id into v_financial_sub_group_id from financial_sub_group where financial_group_id = _financial_group_id and description = _description;
    
	if(v_financial_sub_group_id is not null and v_financial_sub_group_id <> _financial_sub_group_id_from) then
		BEGIN
			SET _validation_message = concat('Não foi possível criar Sub-Grupo Financeiro, pois a descrição informada já existe no Sub-Grupo.');
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = _validation_message;
		END;
	END IF;
		IF (SELECT 1 = 1 FROM financial_sub_group where id=_financial_sub_group_id_from AND financial_group_id=_financial_group_id) THEN
			BEGIN
				UPDATE financial_sub_group SET
					financial_group_id=_financial_group_id,
					description=_description, 
                    revenue_source_id=_revenue_source,
                    revenue_type_id=_revenue_type,
                    inactive=_inactive,  user_change=_user_change, change_date=_processing_date_time
				WHERE
					id=_financial_sub_group_id_from AND financial_group_id=_financial_group_id;
            END;
		ELSE 
			BEGIN
				INSERT INTO financial_sub_group(id, description, financial_group_id, revenue_source_id, revenue_type_id, inactive, user_creation, creation_date, user_change, change_date)
                select _financial_sub_group_id_from, _description, _financial_group_id, _revenue_source, _revenue_type, _inactive, _user_change, _processing_date_time, _user_change, _processing_date_time;
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM financial_sub_group where id=_financial_sub_group_id_from AND financial_group_id=_financial_group_id) THEN
			BEGIN
				UPDATE financial_sub_group SET inactive=_inactive,  user_change=_user_change, change_date=_processing_date_time
				WHERE id=_financial_sub_group_id_from AND financial_group_id=_financial_group_id;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(13=_operation) THEN
		BEGIN
			UPDATE financial_sub_group SET inactive=_inactive,  user_change=_user_change, change_date=_processing_date_time
			WHERE financial_group_id=_financial_group_id;
		END;
	ELSEIF(3=_operation) THEN
        SELECT 
			s.id, s.description, s.financial_group_id, s.revenue_source, s.revenue_type, g.description as description_financial_group, s.inactive, 
            s.user_creation, s.creation_date, s.user_change, u.name as user_change_name, s.change_date
		FROM
			financial_sub_group s
            inner join financial_group g on s.financial_group_id=g.id
            left join user u on s.user_change=u.id
		WHERE 
			s.id=_financial_sub_group_id_from;
	ELSEIF(4=_operation) THEN
		SET @t1 = CONCAT('SELECT s.id, s.description, s.financial_group_id, g.description as description_financial_group, s.revenue_source_id, rs.description AS revenue_source, s.revenue_type_id, rt.description AS revenue_type, s.inactive, s.user_change, s.change_date FROM financial_sub_group s INNER JOIN financial_group g ON s.financial_group_id=g.id INNER JOIN revenue_source rs ON s.revenue_source_id=rs.id INNER JOIN revenue_type rt ON s.revenue_type_id=rt.id ', 
						 ' WHERE 1=1 ', 
                         IF((_financial_sub_group_id_from>0 and _financial_sub_group_id_to>0), concat(' AND s.id between ', _financial_sub_group_id_from, ' AND ', _financial_sub_group_id_to), ''), 
						 IF((length(IFNULL(_financial_group_id,''))>0), concat(' AND g.id in(', _financial_group_id, ')'), ''),
                         IF((length(IFNULL(_description,''))>0), concat(' AND s.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND s.inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;
	ELSEIF(5=_operation) THEN
		SET @t1 = CONCAT('SELECT s.id, s.description FROM financial_sub_group s inner join financial_group g on s.financial_group_id=g.id ', 
						 ' WHERE 1=1 ', 
                         IF((_financial_sub_group_id_from>0 and _financial_sub_group_id_to>0), concat(' AND s.id between ', _financial_sub_group_id_from, ' AND ', _financial_sub_group_id_to), ''), 
						 IF((length(IFNULL(_financial_group_id,''))>0), concat(' AND g.id in(', _financial_group_id, ')'), ''),
                         IF((length(IFNULL(_description,''))>0), concat(' AND s.description like ''%', _description, '%'''), ''),
                         IF((_inactive IS NOT NULL and _inactive<2), concat(' AND s.inactive=', _inactive), ''),
                         ';');
                         
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;
	ELSEIF(20=_operation) THEN
		BEGIN
				SELECT 
					max(id) as id
				FROM
					financial_sub_group
				WHERE
					financial_group_id=_financial_group_id;
            END; /**End operation 20*/		
    END IF;
END$$


/**
call pr_maintenance_financial_sub_group
(
3, -- 1 - IN _operation INT
1, -- 2 - IN _financial_sub_group_id_from VARCHAR(7), 
1, -- 3 - IN _financial_sub_group_id_to VARCHAR(7),
1, -- 4 - IN _financial_group_id_to varchar(100),
'Teste', -- 5 - IN _description varchar(100), 
0, -- 6 - IN _inactive int, 
1,  -- 7 - IN _user_change BIGINT,
1, -- IN _revenue_source INT,
1 -- IN _revenue_type INT
);
*/
/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
    5 - get all combobox operation
*/
-- CALL pr_maintenance_financial_sub_group(1, '06.002', '04.002', '06.000', 'TESTE SUB GROUP', 0, 1, '4', '1'  )