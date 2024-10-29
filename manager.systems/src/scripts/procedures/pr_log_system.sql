USE `manager`;
DROP procedure IF EXISTS `pr_log_system`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_log_system`(IN _operation INT,
								 IN _date_from DATETIME,
								 IN _date_to DATETIME,
                                 IN _system_id INT,
                                 IN _object_id BIGINT,
                                 IN _count INT,
                                 IN _id INT,
                                 IN _changeDate VARCHAR(50))
BEGIN	
	IF(3=_operation) THEN
		BEGIN
			SELECT lo.user_change, lo.object_id, u.name AS user_change_name, DATE_FORMAT(lo.change_date, '%d/%m/%Y %T') AS change_date, 
            s.system_description AS description
            FROM log_system lo
            INNER JOIN user u ON lo.user_change=u.id
            INNER JOIN system_identification s ON lo.system_id=s.id
            WHERE lo.system_id=_system_id AND
            lo.object_id=_object_id
            ORDER BY lo.change_date DESC limit _count;
        END;
	ELSEIF(4=_operation) THEN
		BEGIN
			SET @t1 = CONCAT('SELECT lo.*, u.name AS user_change_name, DATE_FORMAT(lo.change_date, ''%d/%m/%Y %T'') AS change_date_string, ',
            's.system_description AS description ',
            'FROM log_system lo ',
            'INNER JOIN user u ON lo.user_change=u.id ',
            'INNER JOIN system_identification s ON lo.system_id=s.id ',
            'WHERE 1=1 ',
             IF((IFNULL(_date_from,0) || IFNULL(_date_to,0)), concat(' AND lo.change_date BETWEEN ''', _date_from ,''' AND ''', _date_to ,''' '), ''),
             IF((_id>0), concat(' AND lo.id=', _id), ''),
             IF((IFNULL(_changeDate,0)), concat(' AND lo.change_date=''', STR_TO_DATE(_changeDate, '%d/%m/%Y %H:%i:%s'),''''), ''),
            ' ORDER BY lo.change_date DESC',';');  
      
       -- select @t1;
			PREPARE stmt3 FROM @t1;
			EXECUTE stmt3;
			DEALLOCATE PREPARE stmt3;
        END;
    END IF;/*end operation 1*/
END$$
-- CALL pr_log_system(4, null, null, 0, 0, null, 1032, '28/09/2022 18:42:26' )
--  CALL pr_log_system(4, '2022-07-19 00:00:00', '2022-09-25 23:59:59', 0, 0, null,null, null )
-- CALL pr_log_system(4, '2022-07-19 00:00:00', '2022-09-27 23:59:00', 0, 0, null, null, null )