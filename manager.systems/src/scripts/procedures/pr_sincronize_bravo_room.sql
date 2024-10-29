USE `manager`;
DROP PROCEDURE IF EXISTS `pr_sincronize_bravo_room`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_sincronize_bravo_room` (IN _operation INT,
											 IN _room_id BIGINT,
											 IN _room_description VARCHAR(100),
                                             IN _server_id INT,
                                             IN _quantity_machines INT,
                                             IN _inactive BIT)
BEGIN
	IF (1=_operation) THEN /** SAVE SINC BRAVO ROOM*/
		IF (SELECT 1 = 1 FROM bravo_room WHERE room_id = _room_id LIMIT 1) THEN
			BEGIN
				UPDATE bravo_room SET 
					room_description = _room_description, 
					server_id = _server_id, 
					quantity_machines = _quantity_machines, 
					inactive = _inactive
				WHERE 
					room_id = _room_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO bravo_room
				(
					room_id, room_description, server_id, quantity_machines, inactive
				)
				SELECT _room_id, _room_description, _server_id, _quantity_machines, _inactive;
			END;/** End insert */
		END IF; /** End operation 1 */
	END IF; 
END$$

CALL pr_sincronize_bravo_room(1, 1444, 'CP Buchecha Haiti', 20793, 6, 0);