USE `manager`;
DROP procedure IF EXISTS `pr_generate_document_movement_id`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_generate_document_movement_id` (IN _generateDocumentParentId BIT, IN _generateDocumentId BIT)
BEGIN

	DECLARE _document_parent_id BIGINT;
    DECLARE _document_id BIGINT;
    
	IF(IFNULL(_generateDocumentParentId,0) = 1) THEN
		INSERT INTO document_parent_generate_id(inactive) select 0;
		SET _document_parent_id = LAST_INSERT_ID(); 
	END IF;
    
	IF(IFNULL(_generateDocumentId,0) = 1) THEN
		INSERT INTO document_generate_id(inactive) select 0;
		SET _document_id = LAST_INSERT_ID();  
	END IF;
    
    SELECT _document_parent_id AS document_parent_id, _document_id AS document_id;
END$$

-- CALL pr_generate_document_movement_id(0, 1);