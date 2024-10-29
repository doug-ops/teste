USE `manager`;
DROP procedure IF EXISTS `pr_send_email_erros`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_send_email_erros`(IN _operation INT,
									   IN _id BIGINT,
                                       IN _process_description VARCHAR(500),
                                       IN _error_note VARCHAR(500),
                                       IN _email VARCHAR(100),
                                       IN _creation_date DATETIME,
                                       IN _has_send BIT,
                                       IN _send_date DATETIME,
                                       IN _inactive BIT)
BEGIN	
	IF(1=_operation) THEN
		BEGIN
			DECLARE v_exist INT DEFAULT 0;
			IF (IFNULL(_id,0)) THEN
				BEGIN	
					UPDATE send_email_erros SET					
						send_date=_send_date,
						has_send= _has_send
					WHERE
						id=_id; 
				END;
			ELSE 
				BEGIN 
					SELECT 1=1 INTO v_exist FROM send_email_erros WHERE error_note=_error_note;
					IF(1 <> v_exist)THEN
						START TRANSACTION;
						INSERT INTO send_email_erros(process_description, error_note, email, creation_date, has_send, send_date, inactive)	
						SELECT _process_description, _error_note, _email, _creation_date, _has_send, _send_date, _inactive;
						COMMIT;
                    END IF;
				END;
			END IF;
        END;
	ELSEIF(4=_operation) THEN
		BEGIN
			SELECT 
				id, process_description, error_note, email, creation_date, has_send, send_date, inactive
			FROM
				send_email_erros
			WHERE 
				IFNULL(has_send,0)=_has_send
			ORDER BY id;
		END;
    END IF;/*end operation 1*/
END$$

-- CALL pr_send_email_erros(1, null, 'Teste', 'Produtos fora da configuracao de transferencia', 'douglasmariano.ds@gmail.com', now(), 0, now(), 0);
-- CALL pr_send_email_erros(1, 2, 'Teste', null, null, null, 1, now(), 0);
-- truncate table send_email_erros;

