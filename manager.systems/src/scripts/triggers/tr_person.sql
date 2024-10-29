DELIMITER $$
CREATE TRIGGER tr_person AFTER UPDATE
ON person
FOR EACH ROW
BEGIN
	 DECLARE v_bfore JSON;
     DECLARE v_after JSON;
	
 	SET v_bfore = JSON_OBJECT('Código', OLD.id, 'Tipo Pessoa', OLD.person_type, 'Tipo Objeto', OLD.object_type, 'Código Perfil de Acesso', OLD.access_profile_id, 
    'Cpf/Cnpj', OLD.cpf_cnpj, 'Inscrição Estadual', OLD.ie, 'Cep', OLD.address_zip_code, 'Endereço', OLD.address_zip_code_side,
    'Numero', OLD.address_street_number, 'Complemento', OLD.address_street_complement, 'E-mail', OLD.email, 
    'Local', OLD.site, 'Conta Bancária', OLD.bank_account_id, 'Ativo', CASE WHEN OLD.inactive = 0 THEN 'Sim' ELSE 'Não' END, 'Usuário Criou', OLD.user_creation, 
    'Data Criou', DATE_FORMAT(OLD.creation_date, '%d/%m/%Y %T'), 'Usáurio Alterou', OLD.user_change, 'Data Alteração',  DATE_FORMAT(OLD.change_date, '%d/%m/%Y %T'), 
    'Código Antigo', OLD.legacy_id, 'Código Fornecedor Crédito', OLD.credit_provider_id, ' Código Fornecedor Débito', OLD.debit_provider_id, 
    'Tipo Registro', OLD.register_type, 'Código tipo Pessoa', OLD.person_type_id);
    
	SET v_after = JSON_OBJECT('Código', NEW.id, 'Tipo Pessoa', NEW.person_type, 'Tipo Objeto', NEW.object_type, 'Código Perfil de Acesso', NEW.access_profile_id, 
    'Cpf/Cnpj', NEW.cpf_cnpj, 'Inscrição Estadual', NEW.ie, 'Cep', NEW.address_zip_code, 'Endereço', NEW.address_zip_code_side,
    'Numero', NEW.address_street_number, 'Complemento', NEW.address_street_complement, 'E-mail', NEW.email, 
    'Local', NEW.site, 'Conta Bancária', NEW.bank_account_id, 'Ativo', CASE WHEN NEW.inactive = 0 THEN 'Sim' ELSE 'Não' END, 'Usuário Criou', NEW.user_creation, 
    'Data Criou', DATE_FORMAT(NEW.creation_date, '%d/%m/%Y %T'), 'Usáurio Alterou', NEW.user_change, 'Data Alteração',  DATE_FORMAT(NEW.change_date, '%d/%m/%Y %T'), 
    'Código Antigo', NEW.legacy_id, 'Código Fornecedor Crédito', NEW.credit_provider_id, ' Código Fornecedor Débito', NEW.debit_provider_id, 
    'Tipo Registro', NEW.register_type, 'Código tipo Pessoa', NEW.person_type_id);
    
	IF('COM'=NEW.object_type)THEN
		BEGIN
			INSERT INTO log_system(system_id, object_id, json_data_before, json_data_after, user_change, change_date, inactive)
			SELECT 1, NEW.id, v_bfore, v_after, NEW.user_change, NEW.change_date, 0;
		END;
	ELSEIF ('FOR'=NEW.object_type) THEN
		BEGIN 
			INSERT INTO log_system(system_id, object_id, json_data_before, json_data_after, user_change, change_date, inactive)
			SELECT 2, NEW.id, v_bfore, v_after, NEW.user_change, NEW.change_date, 0;
        END;
	ELSEIF ('USU'=NEW.object_type) THEN
		BEGIN 
			INSERT INTO log_system(system_id, object_id, json_data_before, json_data_after, user_change, change_date, inactive)
			SELECT 3, NEW.id, v_bfore, v_after, NEW.user_change, NEW.change_date, 0;
        END;
    END IF;
   
END$$ 
DELIMITER $$
-- drop trigger tr_person