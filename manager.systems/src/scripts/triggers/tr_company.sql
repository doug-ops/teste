DELIMITER $$
CREATE TRIGGER tr_company AFTER UPDATE
ON company
FOR EACH ROW
BEGIN
	 DECLARE v_bfore JSON;
     DECLARE v_after JSON;
     DECLARE v_user_change BIGINT; 
     DECLARE v_change_date DATETIME;
    
 	SET v_bfore = JSON_OBJECT('codigo', OLD.id, 'pessoa', OLD.person_id, 'nome', OLD.social_name, 'Nome Fantasia', OLD.fantasy_name,
    'Fechamento Negativo', CASE WHEN OLD.negative_close=0 THEN 'Não' ELSE 'Sim' END, 'Processa Movemento Automático', CASE WHEN OLD.process_movement_automatic=0 THEN 'Não' ELSE 'Sim' END, 
    'E=mail Cobrador', OLD.email_collector, 'E=mail Operador', OLD.email_operator, 'Código Antigo', OLD.id_old, 'Código Correto', 
    OLD.retaguarda_id, 'Tipo Estabelecimento', OLD.establishment_type_id, 'Usáurio Alterou', OLD.user_change, 'Data Alteração', OLD.change_date);
    
	SET v_after = JSON_OBJECT('Código', NEW.id, 'Código Pessoa', NEW.person_id, 'Nome', NEW.social_name, 'Nome Fantasia', NEW.fantasy_name,
    'Fechamento Negativo', CASE WHEN NEW.negative_close=0 THEN 'Não' ELSE 'Sim' END, 'Processa Movemento Automático', CASE WHEN NEW.process_movement_automatic=0 THEN 'Não' ELSE 'Sim' END, 
    'E=mail Cobrador', NEW.email_collector, 'E=mail Operador', NEW.email_operator, 'Código Antigo', NEW.id_old, 'Código Correto', 
    NEW.retaguarda_id, 'Tipo Estabelecimento', NEW.establishment_type_id, 'Usáurio Alterou', NEW.user_change, 'Data Alteração', NEW.change_date);
    
    INSERT INTO log_system(system_id, object_id, json_data_before, json_data_after, user_change, change_date, inactive)
	SELECT 1, NEW.id, v_bfore, v_after, NEW.user_change, NEW.change_date, 0;
    
END$$
DELIMITER $$

-- drop trigger tr_company

select * from log_system