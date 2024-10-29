USE `manager`;
DROP PROCEDURE IF EXISTS `pr_sincronize_bravo_room_item`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_sincronize_bravo_room_item` (IN _operation INT,
												  IN _room_id INT,
												  IN _machine_id BIGINT,
												  IN _machine_description VARCHAR(100),
												  IN _machine_contract VARCHAR(50),
												  IN _input_movement BIGINT,
												  IN _output_movement BIGINT,
												  IN _inactive BIT)
BEGIN
	IF (1=_operation) THEN /** SAVE SINC BRAVO ROOM*/
		IF (SELECT 1 = 1 FROM bravo_room_item WHERE room_id = _room_id AND machine_id = _machine_id LIMIT 1) THEN
			BEGIN
				UPDATE bravo_room_item SET 
					machine_description = _machine_description, 
					machine_contract = _machine_contract, 
					input_movement = _input_movement, 
                    output_movement = _output_movement,
					inactive = _inactive
				WHERE 
					room_id = _room_id
                    AND machine_id = _machine_id;
			END; /** End Update */
		ELSE
			BEGIN
				INSERT INTO bravo_room_item
				(
					room_id, machine_id, machine_description, machine_contract, input_movement, output_movement, inactive
				)
				SELECT _room_id, _machine_id, _machine_description, _machine_contract, _input_movement, _output_movement, _inactive;
			END;/** End insert */
		END IF; /** End operation 1 */
	END IF; 
END$$

CALL pr_sincronize_bravo_room_item(1, 1444, 19108, 'Flaming Hot Brazil M', 'SPA', 740021, 268572, 0);
