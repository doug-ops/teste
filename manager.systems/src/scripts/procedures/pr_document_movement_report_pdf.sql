USE manager;
DROP procedure IF EXISTS `pr_document_movement_report_pdf`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_document_movement_report_pdf`(IN _operation INT,
												   IN _date_from DATETIME,
												   IN _date_to DATETIME,
												   IN _companys_id VARCHAR(5000),
												   IN _documents_parent_id VARCHAR(21845))
BEGIN	
	IF(1=_operation) THEN
		SET @t1 = CONCAT('SELECT doc.company_id, IFNULL(co.social_name, ''NAO ENCONTRADO'') as company_description, ',  
						'doc.document_note, doc.document_parent_id, doc.document_id, DATE_FORMAT(doc.payment_data,''%d/%m/%Y'') as payment_data, doc.document_type, ',
                        '(case when (doc.credit = 1 and doc.document_value < 0) then (doc.document_value * -1) else doc.document_value end) document_value, ',
                        'doc.credit, doc.document_status, DATE_FORMAT(doc.creation_date,''%d/%m/%Y %H:%i'') as creation_date ',
						'FROM ', 
						'document_movement doc ', 
                        'INNER JOIN (SELECT document_parent_id, company_id FROM document_movement mi ',
						'WHERE 1=1 ', 
                         concat('and mi.payment_data BETWEEN ''',DATE_FORMAT(_date_from, '%Y-%m-%d %H:%i:%s'),'''',' AND ''',DATE_FORMAT(_date_to, '%Y-%m-%d %H:%i:%s'),''' '),
						 IF((LENGTH(_companys_id)>0), concat(' AND mi.company_id in (', _companys_id,')'), ''),  
                         IF((LENGTH(_documents_parent_id)>0), concat(' AND mi.document_parent_id in ( ', _documents_parent_id, ')'), ''),
						'GROUP BY mi.document_parent_id, mi.company_id)x ON x.document_parent_id=doc.document_parent_id AND x.company_id = doc.company_id '             
						'LEFT JOIN company co on doc.company_id=co.id ',
						'order by document_parent_id, document_id;'); 
				 
			-- select @t1;
            
        PREPARE stmt3 FROM @t1;
		EXECUTE stmt3;
		DEALLOCATE PREPARE stmt3;			 
    END IF;
END$$

/**
	1 - get operation
    1 - get all operation
*/
-- CALL pr_document_movement_report_pdf(1,'2021-08-03 00:00:00','2021-08-03 23:59:00', null,null);

-- select distinct document_parent_id from document_movement where document_parent_id in (95175, 100528, 90340, 82651, 107971, 74244, 74031) order by document_parent_id
