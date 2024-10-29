USE `manager`;
DROP procedure IF EXISTS `pr_generate_document_transaction_id`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_generate_document_transaction_id` ()
BEGIN

	DECLARE _document_transaction_id BIGINT;
    
	INSERT INTO document_transaction_generate_id(inactive) select 0;
	SET _document_transaction_id = LAST_INSERT_ID(); 
    
    SELECT _document_transaction_id AS document_transaction_id;
END$$

-- CALL pr_generate_document_transaction_id;