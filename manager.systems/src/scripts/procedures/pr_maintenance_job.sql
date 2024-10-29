USE `manager`;
DROP procedure IF EXISTS `pr_maintenance_job`;
 
DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_maintenance_job` (IN _operation INT,
									   IN _id INT,
									   IN _parent_id INT,
									   IN _description VARCHAR(100), 
                                       IN _sync_timer VARCHAR(20), 
                                       IN _records_processed INT,
									   IN _initial_record VARCHAR(20), 
                                       IN _final_record VARCHAR(20), 
                                       IN _initial_version_record BINARY(20),
                                       IN _final_version_record BINARY(20),
                                       IN _processing_status BIT,
									   IN _processing_message VARCHAR(500), 
                                       IN _processing_data DATETIME,
									   IN _inactive int, 
									   IN _user_change BIGINT)
BEGIN	
	IF(1=_operation) THEN
		IF (SELECT 1 = 1 FROM jobs where id=_id and parent_id=_parent_id) THEN
			BEGIN
				UPDATE jobs SET					
					description=_description,
                    records_processed=_records_processed,
				    initial_record=_initial_record,
				    final_record=_final_record,
                    initial_version_record=_initial_version_record,
                    final_version_record=_final_version_record,
				    processing_status=_processing_status,
				    processing_message=_processing_message,
				    processing_data=_processing_data,
                    inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE
					id=_id and parent_id=_parent_id;
            END;
		ELSE 
			BEGIN   
				INSERT INTO jobs(id, parent_id, description, sync_timer, records_processed, initial_record, initial_version_record, final_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
                select _id, _parent_id, _description, _sync_timer, _records_processed, _initial_record, _initial_version_record, _final_record, _final_version_record, _processing_status, _processing_message, _processing_data, _inactive, _user_change, now(), _user_change, now();
            END;
		END IF;
	ELSEIF(2=_operation) THEN
		IF (SELECT 1 = 1 FROM jobs WHERE id=_id and parent_id=_parent_id) THEN
			BEGIN
				UPDATE jobs SET inactive=_inactive,  user_change=_user_change, change_date=now()
				WHERE id=_id and parent_id=_parent_id;
            END;
		ELSE 
			BEGIN
					SIGNAL SQLSTATE '45000' 
                    SET MESSAGE_TEXT = 'Registro não encontrado.';
			END;
		END IF;
	ELSEIF(3=_operation) THEN
        SELECT 
			id, parent_id, description, sync_timer, item_name, records_processed, initial_record, initial_version_record, 
            final_record, final_version_record, processing_status, processing_message, processing_data, inactive, 
            user_creation, creation_date, user_change, change_date
		FROM
			jobs
		WHERE 
			id=_id and parent_id=_parent_id;
	ELSEIF(4=_operation) THEN
		SELECT 
			id, parent_id, description, sync_timer, item_name, records_processed, initial_record, initial_version_record, 
            final_record, final_version_record, processing_status, processing_message, processing_data, inactive, 
            user_creation, creation_date, user_change, change_date
		FROM
			jobs
		WHERE 
			inactive=_inactive;
    END IF;
END$$

/*
call pr_maintenance_job(
4, -- 01 - IN _operation INT,
1, -- 02 - IN _id INT,
0, -- 03 - IN _parent_id INT,
'Sincronia de Documentos', -- '04 - IN _description VARCHAR(20), 
'0 0/1 * * * ?', -- 05 - IN _sync_timer VARCHAR(20), 
NULL, -- 06 - IN _records_processed INT,
NULL, -- 07 - IN _initial_record VARCHAR(20), 
NULL, -- 08 - IN _final_record VARCHAR(20), 
'0x0', -- 09 - IN _initial_version_record TIMESTAMP,
'0X0', -- 10 - IN _final_version_record TIMESTAMP,
0, -- 11 - IN _processing_status BIT,
NULL, -- 12 - IN _processing_message VARCHAR(500), 
NULL, -- 13 - IN _processing_data DATETIME,
0, -- 14 - IN _inactive int, 
1 -- 15 - IN _user_change BIGINT
);
*/

/**
	1 - Save operation
    2 - inactive operation
    3 - get operation
    4 - get all operation
*/