CREATE DATABASE IF NOT EXISTS `manager`;
use manager;

drop table if exists `manager`.`address`;
drop table if exists `manager`.`user_company`;
drop table if exists `manager`.`user`;
drop table if exists `manager`.`company_integration_system`;
drop table if exists `manager`.`provider_integration_system`;
drop table if exists `manager`.`bank_account_integration_system`;
drop table if exists `manager`.`financial_group_integration_system`;
drop table if exists `manager`.`financial_sub_group_integration_system`;
drop table if exists `manager`.`product_group_integration_system`;
drop table if exists `manager`.`product_sub_group_integration_system`;
drop table if exists `manager`.`product_integration_system`;
drop table if exists `manager`.`integration_system`;
drop table if exists `manager`.`financial_sub_group`;
drop table if exists `manager`.`financial_group`;
drop table if exists `manager`.`financial_transfer_product_setting`;
drop table if exists `manager`.`financial_transfer_group_item_setting`;
drop table if exists `manager`.`financial_transfer_group_setting`;
drop table if exists `manager`.`product` ;
drop table if exists `manager`.`product_sub_group` ;
drop table if exists `manager`.`product_group` ;
drop table if exists `manager`.`bank_account`;
drop table if exists `manager`.`company`;
drop table if exists `manager`.`provider`;
drop table if exists `manager`.`person`;
drop table if exists `manager`.`access_profile`;
drop table if exists `manager`.`document_movement`;

CREATE TABLE `manager`.`address` 
(
  address_zip_code bigint(9) NOT NULL,
  address_zip_code_side varchar(1) CHARACTER SET utf8 NOT NULL,
  address_street varchar(100) CHARACTER SET utf8 NOT NULL,
  address_district varchar(100) CHARACTER SET utf8 NOT NULL,
  address_city_ibge int(11) NOT NULL,
  address_state_ibge int(11) NOT NULL,
  address_country_ibge int(11) NOT NULL,
  address_gia varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  inactive bit(1) NOT NULL,
  user_creation bigint(20) NOT NULL,
  creation_date datetime NOT NULL,
  user_change bigint(20) NOT NULL,
  change_date datetime NOT NULL,
  FOREIGN KEY (address_city_ibge) REFERENCES address_city(address_city_ibge),
  FOREIGN KEY (address_state_ibge) REFERENCES address_state(address_state_ibge),
  FOREIGN KEY (address_country_ibge) REFERENCES address_country(address_country_ibge),
  PRIMARY KEY(address_zip_code, address_zip_code_side)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`person` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  person_type varchar(2) CHARACTER SET utf8 NOT NULL,
  object_type varchar(3) CHARACTER SET utf8 NOT NULL,
  access_profile_id int(11) NOT NULL,
  cpf_cnpj bigint(20) NULL,
  ie varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  address_zip_code bigint(9) DEFAULT NULL,
  address_zip_code_side varchar(1) CHARACTER SET utf8 NULL,
  address_street_number varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  address_street_complement varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  email varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  site varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  bank_account_id INT NULL,
  credit_provider_id BIGINT NULL,
  debit_provider_id BIGINT NULL,
  register_type TINYINT NULL,
  inactive bit(1) NOT NULL,
  user_creation bigint(20) NOT NULL,
  creation_date datetime NOT NULL,
  user_change bigint(20) NOT NULL,
  change_date datetime NOT NULL,
  legacy_id BIGINT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ALTER TABLE person ADD credit_provider_id BIGINT NULL;
-- ALTER TABLE person ADD debit_provider_id BIGINT NULL;
 
CREATE TABLE `manager`.`company` 
(
	id BIGINT NOT NULL,
	person_id BIGINT NOT NULL,
	social_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	fantasy_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    negative_close BIT NOT NULL,
    process_movement_automatic BIT NULL,
	FOREIGN KEY (person_id) REFERENCES person(id), 
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_social_name ON company(social_name);

CREATE TABLE `manager`.`provider` 
(
	id BIGINT NOT NULL,
	person_id BIGINT NOT NULL,
	social_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	fantasy_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    financial_group_id VARCHAR(7) NULL,
    financial_sub_group_id VARCHAR(7) NULL,
	FOREIGN KEY (person_id) REFERENCES person(id),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_social_name ON provider(social_name);

CREATE TABLE `manager`.`user` 
(
	id BIGINT NOT NULL,
	person_id BIGINT NOT NULL,
	name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    alias_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    rg varchar(20) CHARACTER SET utf8 DEFAULT NULL,
    access_data_user varchar(100) CHARACTER SET utf8 NOT NULL,
    access_data_password varchar(100) CHARACTER SET utf8  NOT NULL,
	expiration_date datetime DEFAULT NULL,
	last_access_date datetime DEFAULT NULL,
	FOREIGN KEY (person_id) REFERENCES person(id),
    PRIMARY KEY(id),
    CONSTRAINT UC_acess_user UNIQUE (access_data_user)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_name ON user(name);

CREATE TABLE `manager`.`integration_system` 
(
	id INT NOT NULL,
	description varchar(50) CHARACTER SET utf8 NOT NULL,
    inactive bit(1) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into integration_system select 1, 'DATAMAXI', 0;
insert into integration_system select 2, 'QMANIA', 0;
insert into integration_system select 3, 'BRAVO', 0;

CREATE TABLE `manager`.`company_integration_system` 
(
	company_id BIGINT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id INT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(company_id, integration_system_id, inactive), 
	FOREIGN KEY (company_id) REFERENCES company(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`provider_integration_system` 
(
	provider_id BIGINT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id INT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(provider_id, integration_system_id, inactive), 
	FOREIGN KEY (provider_id) REFERENCES provider(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `manager`.`person` 
(
  id,
  person_type,
  object_type,
  access_profile_id,
  cpf_cnpj,
  ie,
  address_street_number,
  address_street_complement,
  address_zip_code,
  email,
  site,
  inactive,
  user_creation,
  creation_date,
  user_change,
  change_date
)
select 
  1 as id,
  'F' as person_type,
  'USU' as object_type,
  1 as access_profile_id,
  0 as cpf_cnpj,
  '' as ie,
  '' as address_street_number,
  '' as address_street_complement,
  0 as address_zip_code,
  '' as email,
  '' as site,
  0 as inactive,
  0 as user_creation,
  now() as creation_date,
  0 as user_change,
  now() as change_date;
  
insert into `manager`.`user`(id, person_id, name, alias_name, rg, access_data_user, access_data_password, expiration_date, last_access_date) select 1, 1, 'Manager', 'Manager', '', 'adm@manager.com', '123456',   '2020-07-01 00:00:00', null;

drop table if exists `manager`.`user_company`;
CREATE TABLE `manager`.`user_company` 
(
	user_id BIGINT NOT NULL,
	company_id BIGINT NOT NULL,
    inactive BIT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	FOREIGN KEY (user_id) REFERENCES person(id),
   	FOREIGN KEY (company_id) REFERENCES company(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`product_group` 
(
	id INT NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`product_group_integration_system` 
(
	group_id INT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id INT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(group_id, integration_system_id, inactive), 
	FOREIGN KEY (group_id) REFERENCES product_group(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`product_sub_group` 
(
	id INT NOT NULL,
    product_group_id INT NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
   	sale_price NUMERIC(19,2) NOT NULL,
	conversion_factor NUMERIC(19,2) NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (product_group_id) REFERENCES product_group(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`product_sub_group_integration_system` 
(
	sub_group_id INT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id INT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(sub_group_id, integration_system_id, inactive), 
	FOREIGN KEY (sub_group_id) REFERENCES product_sub_group(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`product` 
(
	id BIGINT NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	sale_price NUMERIC(19,2) NOT NULL,
	cost_price NUMERIC(19,2) NOT NULL,
	conversion_factor NUMERIC(19,2) NOT NULL,
    product_group_id INT NOT NULL,
    product_sub_group_id INT NOT NULL,
	company_id BIGINT NULL,
    input_movement BIGINT NULL,
    output_movement BIGINT NULL,
    clock_movement BIGINT NULL,
   	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id),
	FOREIGN KEY (company_id) REFERENCES company(id),
   	FOREIGN KEY (product_group_id) REFERENCES product_group(id),
   	FOREIGN KEY (product_sub_group_id) REFERENCES product_sub_group(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE product ADD clock_movement BIGINT NULL;
ALTER TABLE product MODIFY clock_movement BIGINT NULL AFTER output_movement;

select * from product;

CREATE TABLE `manager`.`product_integration_system` 
(
	product_id BIGINT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id BIGINT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(product_id, integration_system_id, inactive), 
	FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists access_profile;
CREATE TABLE `manager`.`access_profile` 
(
	id INT NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	role VARCHAR(50) CHARACTER SET utf8 NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `manager`.`access_profile`(id, description, role, inactive, user_creation, creation_date, user_change, change_date) select 3, 'Administrativo', 'ADM', 0, 1, now(), 1, now();
insert into `manager`.`access_profile`(id, description, role, inactive, user_creation, creation_date, user_change, change_date) select 2, 'Operador', 'OPE', 0, 1, now(), 1, now();
insert into `manager`.`access_profile`(id, description, role, inactive, user_creation, creation_date, user_change, change_date) select 1, 'Sem Acesso', 'SEM', 0, 1, now(), 1, now();
select * from `manager`.`access_profile`;

drop table if exists permission;
CREATE TABLE `manager`.`permission` 
(
	id INT NOT NULL,
    parent_id INT NOT NULL,
   	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	role VARCHAR(100) CHARACTER SET utf8 NOT NULL, /**MENU_CADASTRO*/
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
    PRIMARY KEY(id, parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO permission SELECT 1, 0, 'Cadastros', 'MENU_CADASTROS', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 2, 1, 'Empresa', 'MENU_CADASTROS_EMPRESA', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 3, 1, 'Fornecedor', 'MENU_CADASTROS_FORNECEDOR', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 4, 1, 'Grupo de Produto', 'MENU_CADASTROS_GRUPO_PRODUTO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 5, 1, 'SubGrupo de Produto', 'MENU_CADASTROS_SUBGRUPO_PRODUTO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 6, 1, 'Produto', 'MENU_CADASTROS_PRODUTO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 7, 1, 'Usuario', 'MENU_CADASTROS_USUARIO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 8, 1, 'Perfil Acesso', 'MENU_CADASTROS_PERFIL_ACESSO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 9, 1, 'Conta Bancaria', 'MENU_CADASTROS_CONTA_BANCARIA', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 10, 1, 'Grupo Financeiro', 'MENU_CADASTROS_GRUPO_FINANCEIRO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 11, 1, 'SubGrupo Financeiro', 'MENU_CADASTROS_SUBGRUPO_FINANCEIRO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 12, 1, 'Configuracao Transferencia', 'MENU_CADASTROS_CONFIGURACAO_TRANSFERENCIA', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 13, 0, 'Movimentos', 'MENU_MOVIMENTOS', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 14, 13, 'Consulta Documentos', 'MENU_MOVIMENTOS_CONSULTA_DOCUMENTOS', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 16, 2, 'Editar', 'MENU_CADASTROS_EMPRESA_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 17, 2, 'Listar', 'MENU_CADASTROS_EMPRESA_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 18, 2, 'Editar', 'MENU_CADASTROS_FORNECEDOR_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 19, 2, 'Listar', 'MENU_CADASTROS_FORNECEDOR_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 18, 2, 'Editar', 'MENU_CADASTROS_FORNECEDOR_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 19, 2, 'Listar', 'MENU_CADASTROS_FORNECEDOR_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 20, 2, 'Editar', 'MENU_CADASTROS_GRUPO_PRODUTO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 21, 2, 'Listar', 'MENU_CADASTROS_GRUPO_PRODUTO_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 22, 2, 'Editar', 'MENU_CADASTROS_SUBGRUPO_PRODUTO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 23, 2, 'Listar', 'MENU_CADASTROS_SUBGRUPO_PRODUTO_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 24, 2, 'Editar', 'MENU_CADASTROS_PRODUTO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 25, 2, 'Listar', 'MENU_CADASTROS_PRODUTO_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 26, 2, 'Editar', 'MENU_CADASTROS_USUARIO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 27, 2, 'Listar', 'MENU_CADASTROS_USUARIO_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 28, 2, 'Editar', 'MENU_CADASTROS_PERFIL_ACESSO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 29, 2, 'Listar', 'MENU_CADASTROS_PERFIL_ACESSO_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 30, 2, 'Editar', 'MENU_CADASTROS_CONTA_BANCARIA_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 31, 2, 'Listar', 'MENU_CADASTROS_CONTA_BANCARIA_LISTAR', 0, 1, NOW(), 1, now();

insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 32, 2, 'Editar', 'MENU_CADASTROS_GRUPO_FINANCEIRO_EDITAR', 0, 1, NOW(), 1, now();
insert into permission(id, parent_id, description, role, inactive, user_creation, creation_date, user_change, change_date)
SELECT 33, 2, 'Listar', 'MENU_CADASTROS_GRUPO_FINANCEIRO_LISTAR', 0, 1, NOW(), 1, now();

INSERT INTO permission SELECT 130, 6, 'Movimento Produto', 'MENU_OFF_LINE_MOVIMENTO_PRODUTO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 131, 6, 'Processa Empresa', 'MENU_OFF_LINE_PROCESSA_EMPRESA', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 132, 6, 'Retirada Produto', 'MENU_OFF_LINE_RETIRADA_PRODUTO', 0, 1, NOW(), 1, now();
INSERT INTO permission SELECT 133, 6, 'Fechamento Caixa', 'MENU_OFF_LINE_FECHAMENTO_CAIXA', 0, 1, NOW(), 1, now();


drop table if exists access_profile_permission;
CREATE TABLE `manager`.`access_profile_permission` 
(
	id INT NOT NULL,
    parent_id INT NOT NULL,
    access_profile_id INT NOT NULL,
    permission BIT NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
    PRIMARY KEY(id, parent_id, access_profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`bank_account` 
(
	id INT NOT NULL,
    description varchar(100) NOT NULL,
    bank_balance_available decimal(19,2) NOT NULL,
    bank_limit_available decimal(19,2) NULL,
    bank_code varchar(10) NULL,
    bank_angency varchar(10) NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_bank_account ON person(bank_account_id);

CREATE TABLE `manager`.`bank_account_integration_system` 
(
	bank_account_id INT NOT NULL,
    integration_system_id INT NOT NULL,
    legacy_id INT NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(bank_account_id, integration_system_id, inactive), 
	FOREIGN KEY (bank_account_id) REFERENCES bank_account(id),
    FOREIGN KEY (integration_system_id) REFERENCES integration_system(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`bank_account_statement`; 
CREATE TABLE `manager`.`bank_account_statement` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	bank_account_id INT NULL,
    person_id BIGINT NOT NULL,
    document_parent_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    document_value DECIMAL(19,2) NOT NULL,
    document_note VARCHAR(1000) CHARACTER SET utf8 NOT NULL,
    bank_balance_available DECIMAL(19,2) NOT NULL,
    inactive BIT(1) NOT NULL,
	user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,    
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table financial_sub_group;
drop table financial_group;

CREATE TABLE `manager`.`financial_group` 
(
	id VARCHAR(7) NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`financial_sub_group` 
(
	id VARCHAR(7) NOT NULL,
    financial_group_id VARCHAR(7) NOT NULL,
	description VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    revenue_source_id INT NOT NULL,
	revenue_type_id INT NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id, financial_group_id),
    FOREIGN KEY (financial_group_id) REFERENCES financial_group(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`financial_transfer_product_setting`;
drop table if exists `manager`.`financial_transfer_group_item_setting`;
drop table if exists `manager`.`financial_transfer_group_setting`;
CREATE TABLE `manager`.`financial_transfer_group_setting` 
(
	id INT NOT NULL AUTO_INCREMENT,
    bank_account_origin_id INT NOT NULL, -- table bank_account.id
    bank_account_destiny_id INT NOT NULL,  -- table bank_account.id
    bank_account_automatic_transfer_id INT NULL,  -- table bank_account.id
    execution_order TINYINT NOT NULL,
   	description VARCHAR(100) NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT UC_financial_transfer_group_setting UNIQUE (bank_account_origin_id, bank_account_destiny_id, execution_order), 
    FOREIGN KEY (bank_account_origin_id) REFERENCES bank_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`financial_transfer_group_item_setting` 
(
	id INT NOT NULL AUTO_INCREMENT,
	group_id INT NOT NULL, -- table financial_transfer_group_setting.id
	bank_account_origin_id INT NOT NULL,  -- table bank_account.id
    bank_account_destiny_id INT NOT NULL,  -- table bank_account.id
    execution_order TINYINT NOT NULL,
    description_transfer VARCHAR(100) NOT NULL,
   	provider_id BIGINT NOT NULL,  -- table provader.id
    transfer_type TINYINT NOT NULL, -- 1 - porcentagem, 2 - valor
    value_transfer DECIMAL(19,2) NOT NULL,
    bank_account_id INT NULL, -- table bank_account.id
    credit_debit BIT NOT NULL,
    transfer_state TINYINT NOT NULL,
    is_over_total BIT NOT NULL,
    expense TINYINT NOT NULL, -- 0 - movement, 1 - diary, 2 - weekly, 3 - monthly, 4 - variable expense
    is_use_remaining_balance BIT NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL, -- table user.id
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL, -- table user.id
    change_date DATETIME NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT UC_financial_transfer_group_item_setting UNIQUE (group_id, bank_account_origin_id, bank_account_destiny_id, execution_order), 
    FOREIGN KEY (bank_account_origin_id) REFERENCES bank_account(id),
	FOREIGN KEY (group_id) REFERENCES financial_transfer_group_setting(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`financial_transfer_product_setting` 
(
    group_id INT NOT NULL, -- table financial_transfer_group_setting.id
	bank_account_origin_id INT NOT NULL,  -- table bank_account.id
    bank_account_destiny_id INT NOT NULL,  -- table bank_account.id
    product_id BIGINT NOT NULL, -- table product.id
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL, -- table user.id
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,  -- table user.id
    change_date DATETIME NOT NULL,
    PRIMARY KEY(group_id, bank_account_origin_id, bank_account_destiny_id, product_id), 
	FOREIGN KEY (bank_account_origin_id) REFERENCES bank_account(id),
	FOREIGN KEY (group_id) REFERENCES financial_transfer_group_setting(id),
   	FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`document_movement`;
CREATE TABLE `manager`.`document_movement` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	document_id BIGINT NOT NULL,
    document_parent_id BIGINT NOT NULL,
    document_type INT NOT NULL, -- 1 - Duplicata, 2 - Movimento Maquina 3 - Documento Fiscal
	credit BIT NOT NULL, -- 1 - CREDITO - 2 DEBITO 
  	company_id BIGINT NULL, -- table company.id
    provider_id BIGINT NULL, -- table provider.id
    bank_account_id INT NULL, -- table bank_account.id
    document_number VARCHAR(20) CHARACTER SET utf8 NULL, 
    document_value DECIMAL(19,4) NULL,
    document_note VARCHAR(1000) CHARACTER SET utf8 NULL,
    document_status INT NOT NULL, -- 1 -- Aberto, 2 -- Baixado, 3 - Conferido, 4 - Pendente, 5 - Provisionado 
    payment_value DECIMAL(19,4) NULL,
    payment_discount  DECIMAL(19,4) NULL,
    payment_extra  DECIMAL(19,4) NULL,
    payment_residue BIT NOT NULL,
    payment_data DATETIME NULL,
    payment_expiry_data DATETIME NULL,
    payment_user BIGINT NULL,
    payment_status TINYINT NULL,
    nfe_number BIGINT NULL,
    nfe_serie_number INT NULL,
    generate_billet BIT NOT NULL,
    financial_group_id VARCHAR(7) NULL,
    financial_sub_group_id VARCHAR(7) NULL,
    is_product_movement BIT NULL,
    product_id BIGINT,
    product_description VARCHAR(100),
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
create index ix_document_id_parent_type on document_movement(document_id, document_parent_id, document_type);

-- alter table document_movement add document_parent_id BIGINT NULL;
select * from document_movement;

drop table if exists `manager`.`jobs`;
CREATE TABLE `manager`.`jobs` 
(
	id INT NOT NULL,
    parent_id INT NOT NULL,
    description VARCHAR(100) CHARACTER SET utf8 NOT NULL, 
    sync_timer VARCHAR(20) CHARACTER SET utf8 NULL, 
    item_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
	records_processed INT NULL,
    initial_record VARCHAR(20) CHARACTER SET utf8 NULL, 
    final_record VARCHAR(20) CHARACTER SET utf8 NULL, 
    initial_version_record varbinary(255) NULL,
    final_version_record varbinary(255) NULL,
	processing_status BIT NULL,
    processing_message VARCHAR(500) CHARACTER SET utf8 NULL, 
    processing_data DATETIME NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	PRIMARY KEY(id, parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO  jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
SELECT '1', '1', 'Sinc Documents', '0 0/1 * * * ?', 'tb_fin_duplicatas', 0, '0', '0', 0x0, 0x0, '0', NULL, NULL, 1, '1', '2019-10-22 01:28:32', '1', '2019-10-22 01:28:32';
INSERT INTO  jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
SELECT '2', '1', 'Sinc Product Movement', '0 0/1 * * * ?', 'product_movement', 0, '0', '0', NULL, NULL, '0', NULL, NULL, 1, '1', '2019-10-22 01:28:32', '1', '2019-10-22 01:28:32';
INSERT INTO  jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
SELECT '3', '1', 'Process Movement', '0 0/1 * * * ?', 'product_movement', 0, '0', '0', NULL, NULL, '0', NULL, NULL, 1, '1', '2019-10-22 01:28:32', '1', '2019-10-22 01:28:32';
INSERT INTO  jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
SELECT '4', '1', 'Send Email Movement', '0 0/1 * * * ?', 'send_email_movement', 0, '0', '0', NULL, NULL, '0', NULL, NULL, 1, '1', '2019-10-22 01:28:32', '1', '2019-10-22 01:28:32';

update jobs set sync_timer='0 0/1 * * * ?' where id=1;
update jobs set sync_timer='0 0/10 * * * ?', inactive=0 where id=2;
update jobs set sync_timer='0 0/1 * * * ?' where id=3;
update jobs set sync_timer='0 0/1 * * * ?', inactive=0 where id=4;

select * from jobs;
CALL pr_maintenance_job(4, null, null, null, null, null, null, null, null, null, null, null, null, 0, null);

DROP TABLE IF EXISTS `manager`.`product_movement`;
CREATE TABLE `manager`.`product_movement` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    movement_legacy_id BIGINT NOT NULL,
    legacy_system_id INT NOT NULL, -- 1 - QUARTER HORSE
    product_id BIGINT NOT NULL,
    company_id BIGINT NULL,
    company_description VARCHAR(100) NOT NULL,
    credit_in_initial BIGINT NULL,
    credit_in_final BIGINT NOT NULL,
    credit_out_initial BIGINT NULL,
    credit_out_final BIGINT NOT NULL,
    credit_clock_initial BIGINT NOT NULL,
    credit_clock_final BIGINT NOT NULL,
    movement_type VARCHAR(1) NOT NULL,
    initial_date DATETIME NOT NULL,
    reading_date DATETIME NOT NULL,
    processing BIT NOT NULL,
    processing_date DATETIME NULL,
    document_in_id BIGINT NULL,
    document_out_id BIGINT NULL,
    document_parent_id BIGINT NULL,
    is_offline BIT NOT NULL,
    has_error BIT NOT NULL,
    note VARCHAR(1000) NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE INDEX ix_product_movement_legacy_id on product_movement(movement_legacy_id);
select * from product_movement;
ALTER TABLE product_movement ADD document_parent_id BIGINT NULL;
ALTER TABLE product_movement ADD credit_clock_initial BIGINT NULL;
ALTER TABLE product_movement ADD credit_clock_final BIGINT NULL;
ALTER TABLE product_movement ADD is_offline BIT NULL;
ALTER TABLE product_movement MODIFY document_parent_id BIGINT NULL AFTER document_out_id;
ALTER TABLE product_movement MODIFY credit_clock_initial BIGINT NULL AFTER credit_out_final;
ALTER TABLE product_movement MODIFY credit_clock_final BIGINT NULL AFTER credit_clock_initial;
ALTER TABLE product_movement MODIFY is_offline BIT NULL AFTER document_parent_id;

DROP TABLE IF EXISTS `manager`.`config_systems`;
CREATE TABLE `manager`.`config_systems` 
(
	remote_sinc BIT NOT NULL,
    process_movements BIT NOT NULL,
    send_email_documents BIT NOT NULL,
    timezone_database TINYINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO config_systems(remote_sinc, process_movements, send_email_documents, timezone_database)
SELECT 1, 1, 0, -3;
select * from `manager`.`config_systems`; 
/**11/02/2020*/
ALTER TABLE document_movement ADD is_product_movement BIT NULL;
ALTER TABLE document_movement MODIFY is_product_movement BIT NULL AFTER financial_sub_group_id;

drop table if exists `manager`.`provider_ungroup`;
CREATE TABLE `manager`.`provider_ungroup` 
(
	provider_id BIGINT NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(provider_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`document_generate_id`;
CREATE TABLE `manager`.`document_generate_id` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    inactive BIT NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`document_parent_generate_id`;
CREATE TABLE `manager`.`document_parent_generate_id` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    inactive BIT NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `manager`.`send_email_document`;
CREATE TABLE `manager`.`send_email_document` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) CHARACTER SET utf8 NOT NULL,
    document_parent_id BIGINT NOT NULL,
    is_send BIT NOT NULL,
    inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**10/04/2020*/
use manager;
ALTER TABLE document_movement ADD payment_status TINYINT NULL;
ALTER TABLE document_movement MODIFY payment_status TINYINT NULL AFTER payment_user;

/**02/05/2020*/
drop table if exists `manager`.`person_bank_account`;
CREATE TABLE `manager`.`person_bank_account` 
(
	person_id BIGINT NOT NULL,
	bank_account_id INT NOT NULL,
    inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	FOREIGN KEY (person_id) REFERENCES person(id),
   	FOREIGN KEY (bank_account_id) REFERENCES bank_account(id), 
    UNIQUE(bank_account_id, inactive)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE person ADD register_type TINYINT NULL;
ALTER TABLE person MODIFY register_type TINYINT NULL AFTER debit_provider_id;

/**04/05/2020*/
ALTER TABLE bank_account_statement ADD person_id BIGINT NOT NULL;
ALTER TABLE bank_account_statement MODIFY person_id BIGINT NOT NULL AFTER bank_account_id;
ALTER TABLE company ADD process_movement_automatic BIT NULL;

/**07/05/2020*/
ALTER TABLE product_movement ADD initial_date DATETIME NULL;
ALTER TABLE product_movement MODIFY initial_date DATETIME NULL AFTER reading_date;
ALTER TABLE product_movement ADD has_error BIT NULL;
ALTER TABLE product_movement ADD note VARCHAR(1000) NULL;
ALTER TABLE product_movement MODIFY has_error BIT NULL AFTER is_offline;
ALTER TABLE product_movement MODIFY note VARCHAR(1000) NULL AFTER has_error;

/**02/06/2020*/
use manager;
drop table if exists `manager`.`revenue_source`;
CREATE TABLE `manager`.`revenue_source` 
(
	id INT NOT NULL PRIMARY KEY,
	description VARCHAR(50) NOT NULL,
    inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO revenue_source(id, description, inactive, user_creation, creation_date, user_change, change_date)
SELECT 1, 'RECEITA', 0, 1, NOW(),1,NOW();
INSERT INTO revenue_source(id, description, inactive, user_creation, creation_date, user_change, change_date)
SELECT 2, 'DESPESA', 0, 1, NOW(),1,NOW();
INSERT INTO revenue_source(id, description, inactive, user_creation, creation_date, user_change, change_date)
SELECT 3, 'TRANSFERENCIA', 0, 1, NOW(),1,NOW();

drop table if exists `manager`.`revenue_type`;
CREATE TABLE `manager`.`revenue_type` 
(
	id INT NOT NULL PRIMARY KEY,
	description VARCHAR(50) NOT NULL,
    inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO revenue_type(id, description, inactive, user_creation, creation_date, user_change, change_date)
SELECT 1, 'VARIÁVEL', 0, 1, NOW(),1,NOW();
INSERT INTO revenue_type(id, description, inactive, user_creation, creation_date, user_change, change_date)
SELECT 2, 'FIXO', 0, 1, NOW(),1,NOW();

/**10/06/2020*/
ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN bank_account_automatic_transfer_id INT NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY bank_account_automatic_transfer_id INT NULL AFTER bank_account_destiny_id;

/** 11/06/2020 */
ALTER TABLE `manager`.`bank_account_statement` ADD COLUMN bank_balance_available DECIMAL(19,2) NOT NULL;
ALTER TABLE `manager`.`bank_account_statement` MODIFY bank_balance_available DECIMAL(19,2) NOT NULL AFTER document_note;
ALTER TABLE provider MODIFY COLUMN financial_group_id VARCHAR(7);
ALTER TABLE provider MODIFY COLUMN financial_sub_group_id VARCHAR(7);

/** 01/07/2020 - Alterar em producao*/
SET SQL_SAFE_UPDATES = 0;
update document_movement set financial_group_id=null WHERE financial_group_id=0;
update document_movement set financial_sub_group_id=null WHERE financial_sub_group_id=0;

/** 13/10/2020 */
ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN execution_period tinyint NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY execution_period tinyint NULL AFTER description;
update financial_transfer_group_setting set execution_period=15;

/**19/10/2020*/
alter table product_integration_system modify legacy_id varchar(20) not null;

/**26/10/2020*/
alter table product add enable_clock_movement bit not null;
alter table product MODIFY enable_clock_movement bit not null AFTER clock_movement;


/**24/11/2020*/
CREATE TABLE `manager`.`bank_account_out_document_parent` 
(
	bank_account_id INT NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    PRIMARY KEY(bank_account_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
insert into bank_account_out_document_parent select 156, 0, 1, now(), 1, now();

/**02/12/2020*/
alter table document_movement add product_id BIGINT null;
alter table document_movement MODIFY product_id BIGINT null AFTER is_product_movement;
alter table document_movement add product_description VARCHAR(100) null;
alter table document_movement MODIFY product_description VARCHAR(100) null AFTER product_id;

/**30/03/2021*/
alter table send_email_document add company_id BIGINT null;
alter table send_email_document MODIFY company_id BIGINT null AFTER document_parent_id;

/**13/05/2021*/
alter table document_movement add group_transfer_id INT NULL;
alter table document_movement MODIFY group_transfer_id INT NULL NULL AFTER product_description;
alter table document_movement add group_execution_order TINYINT NULL;
alter table document_movement MODIFY group_execution_order TINYINT NULL AFTER group_transfer_id;
alter table document_movement add group_item_execution_order TINYINT NULL;
alter table document_movement MODIFY group_item_execution_order TINYINT NULL AFTER group_execution_order;

/**10/07/2021*/
alter table company add column id_old bigint; 
alter table company add column retaguarda_id bigint; 

/** 21/10/2021 */
ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN execution_initial_period date NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY execution_initial_period date AFTER execution_period;

/** 30/10/2021 */
CREATE TABLE `manager`.`company_movement_execution` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    initial_date_week DATE NOT NULL,
    final_date_week DATE NOT NULL,
    week_year INT NOT NULL,
	week_day INT NULL,            
    company_id BIGINT NOT NULL,
	company_description VARCHAR(100) NOT NULL,
    bank_account_origin_id INT NOT NULL, 
    bank_account_destiny_id INT NOT NULL, 
    execution_initial_period DATE NULL,    
    execution_period INT NOT NULL,
    execution_date	DATETIME NULL,
    document_parent_id BIGINT NULL,
    executed BIT NOT NULL,
    negative BIT NOT NULL,
    low_movement BIT NOT NULL,
	inactive BIT NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/** 07/11/2021 */
ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN fixed_day BIT NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY fixed_day BIT AFTER execution_initial_period;

/** 01/12/2021 */
alter table product_group add transfer_hab decimal(5,2) null;

/** 16/12/2021 */
ALTER TABLE `manager`.`company` ADD COLUMN email_collector varchar(100) NULL;
ALTER TABLE `manager`.`company` MODIFY email_collector varchar(100) AFTER process_movement_automatic;
ALTER TABLE `manager`.`company` ADD COLUMN email_operator varchar(100) NULL;
ALTER TABLE `manager`.`company` MODIFY email_operator varchar(100) AFTER email_collector;

/** 17/12/2021 */
alter table user add exit_access_date datetime null;

/** 22/12/2021 */
alter table bank_account_statement modify bank_balance_available decimal(19,2) null;


/** 04/03/2022 */
ALTER TABLE `manager`.`company_movement_execution` ADD COLUMN document_note varchar(1000) NULL;

/** 08/03/2022 */
ALTER TABLE `manager`.`company_movement_execution` ADD COLUMN expiry_data DATE NULL;

/** 09/03/2022 */
ALTER TABLE `manager`.`company_movement_execution` ADD COLUMN motive varchar(20) NULL;

/** 22/03/2022 */
ALTER TABLE `manager`.`company_movement_execution` MODIFY execution_date DATETIME AFTER week_day;

/**13/04/2022 */
ALTER TABLE `manager`.`company_movement_execution` MODIFY expiry_data DATE AFTER id;

/**13/04/2022 */
ALTER TABLE `manager`.`company_movement_execution` MODIFY company_id BIGINT AFTER id;
ALTER TABLE `manager`.`company_movement_execution` MODIFY company_description varchar(100) AFTER company_id;
ALTER TABLE `manager`.`company_movement_execution` MODIFY expiry_data DATE AFTER company_description;
ALTER TABLE `manager`.`company_movement_execution` MODIFY document_parent_id BIGINT AFTER execution_date;
ALTER TABLE `manager`.`company_movement_execution` MODIFY motive INT AFTER document_parent_id;
ALTER TABLE `manager`.`company_movement_execution` MODIFY document_note	varchar(1000) AFTER motive;
ALTER TABLE `manager`.`company_movement_execution` drop column execution_initial_period;
ALTER TABLE `manager`.`company_movement_execution` drop column execution_period;
ALTER TABLE `manager`.`company_movement_execution` drop column executed;
ALTER TABLE `manager`.`company_movement_execution` drop column negative;
ALTER TABLE `manager`.`company_movement_execution` drop column low_movement;

/**06/08/2022*/
alter table document_movement modify column financial_group_id varchar(7) null;
alter table document_movement modify column financial_sub_group_id varchar(7) null;


/**19/08/2022 */
DROP TABLE IF EXISTS `person_type`;
CREATE TABLE `person_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `object_type` varchar(3) NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**19/08/2022 */
ALTER TABLE `manager`.`person` ADD COLUMN person_type_id INT NULL;

/**26/08/2022 */
DROP TABLE IF EXISTS `log_system`;
CREATE TABLE `log_system` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `system_id` INT NOT NULL,
  `object_id` BIGINT NOT NULL,
  `json_data_before` JSON NOT NULL,
  `json_data_after` JSON NOT NULL,
  `user_change` BIGINT NOT NULL,
  `change_date` DATETIME NOT NULL,
  `inactive` BIT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**26/08/2022 */
DROP TABLE IF EXISTS `system_identification`;
CREATE TABLE `system_identification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `system_description` varchar(500) NOT NULL,
  `user_creation` BIGINT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `inactive` BIT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**26/08/2022 */
insert into system_identification(id, system_description, user_creation, creation_date, inactive)
value (1, 'Cadastro Empresa', 1, now(), 0);
insert into system_identification(id, system_description, user_creation, creation_date, inactive)
value (2, 'Cadstro Fornecedor', 1, now(), 0);
insert into system_identification(id, system_description, user_creation, creation_date, inactive)
value (3, 'Cadstro Usuario', 1, now(), 0);
insert into system_identification(id, system_description, user_creation, creation_date, inactive)
value (4, 'Cadastro Produto', 1, now(), 0);
insert into system_identification(id, system_description, user_creation, creation_date, inactive)
value (5, 'Consulta e Baixa', 1, now(), 0);

/**26/08/2022 */
ALTER TABLE `manager`.`company` ADD COLUMN user_change BIGINT NULL;
ALTER TABLE `manager`.`company` ADD COLUMN change_date DATETIME NULL;

/**31/08/2022 */
ALTER TABLE company
CHANGE person_company_type_id person_type_id int DEFAULT NULL;

ALTER TABLE company
CHANGE company_type_id establishment_type_id int DEFAULT NULL;

/**19/08/2022 */
DROP TABLE IF EXISTS `send_email_erros`;
CREATE TABLE `send_email_erros` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `process_description` varchar(500) NOT NULL,
  `error_note` varchar(500) NOT NULL,
  `email` varchar(100) NOT NULL,
  `creation_date` datetime NOT NULL,
  `has_send` bit(1) NOT NULL,
  `send_date` datetime NULL,
  `inactive` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/**19/08/2022 */
insert into jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
value (6, 1, 'Send Email Error', '0 0/1 * * * ?', 'send_email_error', 0, 0, 0, NULL, NULL, 1, NULL, now(), 0, 1, now(), 1, now());

/**31/08/2022 */
DROP TABLE IF EXISTS `financial_group_type`;
CREATE TABLE `financial_group_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into financial_group_type(description, inactive, user_creation, creation_date)
value
('Vendas', 0, 1, now()),    
('Compras', 0,1, now()), 
('Despesas',0,1, now()),    
('Investimentos',0,1, now()),    
('Ativo Fixo',0, 1, now()),     
('Outros',0,1, now()) ;  

ALTER TABLE `manager`.`financial_sub_group` ADD COLUMN financial_group_type_id INT NULL;
ALTER TABLE `manager`.`financial_sub_group` MODIFY financial_group_type_id INT AFTER revenue_type_id;

/**14/09/2022 */
DROP TABLE IF EXISTS `user_parent`;
CREATE TABLE `user_parent` (
  `user_parent_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `creation_user` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `change_user` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  `inactive` bit(1) NOT NULL,
  PRIMARY KEY (`user_parent_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**Create 14/09/2022 */
ALTER TABLE `manager`.`person` ADD COLUMN blocked BIT(1) not NULL;

/** Create 27/10/2022 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN bank_account_origin_id INT NULL;
ALTER TABLE `manager`.`document_movement` MODIFY bank_account_origin_id INT AFTER bank_account_id;

ALTER TABLE `manager`.`bank_account_statement` ADD COLUMN bank_account_origin_id INT NULL;
ALTER TABLE `manager`.`bank_account_statement` MODIFY bank_account_origin_id INT AFTER bank_account_id;

ALTER TABLE `manager`.`document_movement` ADD COLUMN document_transfer_id BIGINT NULL;
ALTER TABLE `manager`.`document_movement` MODIFY document_transfer_id BIGINT AFTER bank_account_origin_id;

/**Create 19/11/2022 */
DROP TABLE IF EXISTS `financial_cost_center`;
CREATE TABLE `financial_cost_center` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `creation_user` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `change_user` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  `inactive` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/**Create 20/11/2022 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN financial_cost_center_id INT NULL;

/**Create 02/12/2022 */
ALTER TABLE `manager`.`bank_account_statement` ADD COLUMN provider_id BIGINT;
ALTER TABLE `manager`.`bank_account_statement` MODIFY provider_id BIGINT AFTER bank_account_origin_id;

/**Create 25/01/2023 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN document_parent_id_origin BIGINT NULL;
ALTER TABLE `manager`.`document_movement` ADD COLUMN document_id_origin BIGINT NULL;
ALTER TABLE `manager`.`document_movement` MODIFY document_parent_id_origin BIGINT AFTER document_parent_id;
ALTER TABLE `manager`.`document_movement` MODIFY document_id_origin BIGINT AFTER document_parent_id_origin;
ALTER TABLE `manager`.`document_movement` ADD COLUMN is_document_transfer bit(1) NULL;
ALTER TABLE `manager`.`document_movement` MODIFY is_document_transfer bit(1) AFTER document_transfer_id;

/**Create 24/02/2023 */
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `creation_user` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `change_user` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  `inactive` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into client(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 1, 'Santana', 1, now(), 1, now(), 0;

insert into client(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 2, 'Ford Ka', 1, now(), 1, now(), 0;

ALTER TABLE `manager`.`user` ADD COLUMN client_id int NULL;
ALTER TABLE `manager`.`user` MODIFY client_id int AFTER alias_name;

ALTER TABLE `manager`.`company` ADD COLUMN client_id int NULL;
ALTER TABLE `manager`.`company` MODIFY client_id int AFTER fantasy_name;

ALTER TABLE `manager`.`product` ADD COLUMN client_id int NULL;
ALTER TABLE `manager`.`product` MODIFY client_id int AFTER company_id;

/** Create 18/03/2023 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN cashier_closing_date datetime NULL;
ALTER TABLE `manager`.`document_movement` MODIFY cashier_closing_date datetime AFTER group_item_execution_order;

/** Create 05/09/2023 */
ALTER TABLE `manager`.`user` ADD COLUMN main_company_id BIGINT NULL;

/**Create 22/05/2023 */
DROP TABLE IF EXISTS `client`;
CREATE TABLE `document_launch_type` (
  `id` int NOT NULL,
  `description` varchar(50) NOT NULL,
  `creation_user` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `change_user` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  `inactive` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


insert into document_launch_type(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 1, 'Despesas', 1, now(), 1, now(), 0;

insert into document_launch_type(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 2, 'Transferencia Saldo', 1, now(), 1, now(), 0;

insert into document_launch_type(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 3, 'Lancamentos', 1, now(), 1, now(), 0;

insert into document_launch_type(id, description, creation_user, creation_date, change_user, change_date, inactive)
select 4, 'Movimento Produto', 1, now(), 1, now(), 0;

/** Date create 27/05/2023 */
ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN week_day int NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY week_day int AFTER fixed_day;

ALTER TABLE `manager`.`financial_transfer_group_setting` ADD COLUMN automatic_processing bit NULL;
ALTER TABLE `manager`.`financial_transfer_group_setting` MODIFY automatic_processing bit AFTER week_day;

ALTER TABLE `manager`.`financial_transfer_group_item_setting` ADD COLUMN move_launch_type int NULL;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` MODIFY move_launch_type int AFTER transfer_type;

/** Date create 13/06/2023 */
ALTER TABLE `manager`.`financial_transfer_group_item_setting` ADD COLUMN financial_group_id VARCHAR(7) NULL;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` MODIFY financial_group_id VARCHAR(7) AFTER provider_id;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` ADD COLUMN financial_sub_group_id VARCHAR(7) NULL;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` MODIFY financial_sub_group_id VARCHAR(7) AFTER financial_group_id;

/** Date create 20/06/2023 */
ALTER TABLE `manager`.`bank_account` ADD COLUMN accumulate_balance BIT NULL;
ALTER TABLE `manager`.`bank_account` MODIFY accumulate_balance BIT AFTER bank_angency;

/**Create 24/06/2023 */
ALTER TABLE `manager`.`bank_account_statement` ADD COLUMN launch_type TINYINT;
ALTER TABLE `manager`.`bank_account_statement` MODIFY launch_type TINYINT AFTER bank_account_origin_id;

/** Create 08/02/2023 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN document_transaction_id BIGINT NULL;
ALTER TABLE `manager`.`document_movement` MODIFY document_transaction_id BIGINT AFTER document_id_origin;

drop table if exists `manager`.`document_transaction_generate_id`;
CREATE TABLE `manager`.`document_transaction_generate_id` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    inactive BIT NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/** Create 08/05/2023 */
CREATE INDEX ix_document_transaction_id ON document_movement(document_transaction_id);

/** Create 22/08/2023 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN is_cashing_close BIT NULL;
ALTER TABLE `manager`.`document_movement` MODIFY is_cashing_close BIT AFTER document_transaction_id;

/** Create at 22/09/2023 */
CREATE TABLE `manager`.`bravo_room` 
(
    room_id INT NOT NULL,
    room_description VARCHAR(100) NOT NULL,
	server_id INT NOT NULL,
    quantity_machines INT NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(room_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `manager`.`bravo_room_item` 
(
    room_id INT NOT NULL,
    machine_id BIGINT NOT NULL,
	machine_description VARCHAR(100) NOT NULL,
    machine_contract VARCHAR(50) NOT NULL,
    input_movement BIGINT NOT NULL,
    output_movement BIGINT NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(room_id, machine_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/** Date create 30/10/2023 */
insert into `manager`.`access_profile`(id, description, role, inactive, user_creation, creation_date, user_change, change_date) select 5, 'Sub-Operador', 'SUBOPE', 0, 1, now(), 1, now();

/** Date create 20/11/2023 */
CREATE TABLE `manager`.`person_bank_account_permission` 
(
	user_id BIGINT NOT NULL,
	bank_account_id INT NOT NULL,
    inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	FOREIGN KEY (user_id) REFERENCES user(id),
   	FOREIGN KEY (bank_account_id) REFERENCES bank_account(id), 
    UNIQUE(bank_account_id, user_id, inactive)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/** Date create 06/02/2024 */
drop table if exists access_profile_config;
CREATE TABLE `manager`.`access_profile_config` 
(
    access_profile_id INT NOT NULL,
    cash_closing_max_discount decimal(19,2) NOT NULL,
	inactive BIT NOT NULL,
    user_creation BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	user_change BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
    PRIMARY KEY(access_profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE cashier_closing;
CREATE TABLE `manager`.`cashier_closing` 
(
    cashier_closing_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id  BIGINT NOT NULL,
	week_year INT NOT NULL,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL,
    cashier_closing_status TINYINT  NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	launch_user BIGINT NULL,
    launch_date DATETIME NULL,
	close_user BIGINT NULL,
    close_date DATETIME NULL,
    note VARCHAR(2000) NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(cashier_closing_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_cashier_closing_week_year_user_id on cashier_closing(week_year, user_id);
    
DROP TABLE cashier_closing_company;
    
CREATE TABLE `manager`.`cashier_closing_company` 
(
    cashier_closing_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    week_year INT NOT NULL,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL,
    company_id BIGINT  NOT NULL, 
    movement_value DECIMAL(19,4)  NOT NULL,
    pending_movement_value DECIMAL(19,4)  NOT NULL, 
    discount_total DECIMAL(19,4) NOT NULL,
    payment_total DECIMAL(19,4) NOT NULL,
    pending_movement_after_value DECIMAL(19,4) NOT NULL,
    cashier_closing_status TINYINT NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	launch_user BIGINT NULL,
    launch_date DATETIME NULL,
	close_user BIGINT NULL,
    close_date DATETIME NULL,
    note VARCHAR(2000) NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(cashier_closing_id, week_year, user_id, company_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_cashier_closing_company_week_year_user_id_company_id on cashier_closing_company(week_year, user_id, company_id);

DROP TABLE cashier_closing_company_document;

CREATE TABLE `manager`.`cashier_closing_company_document` 
(
	cashier_closing_id BIGINT NOT NULL,
	user_id  BIGINT NOT NULL,
    week_year INT NOT NULL,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL,
    company_id BIGINT  NOT NULL, 
    product_id BIGINT  NOT NULL, 
	document_movement_id BIGINT NOT NULL,
    document_parent_id BIGINT NOT NULL,
    residue BIT NULL,
    product_movement BIT NULL,
    credit BIT NULL,
    document_value DECIMAL(19,4)  NOT NULL, 
    document_note varchar(1000) NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(cashier_closing_id, week_year, user_id, company_id, document_movement_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_cashier_closing_company_document_week_year_user_id_company_id on cashier_closing_company_document(week_year, user_id, company_id, document_movement_id);

DROP TABLE cashier_closing_company_launch;
    
CREATE TABLE `manager`.`cashier_closing_company_launch` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    cashier_closing_id BIGINT NOT NULL,
    cashier_closing_status TINYINT NOT NULL,
    user_operator BIGINT NOT NULL,
    company_id BIGINT NOT NULL, 
    bank_account_id INT NOT NULL, 
    provider_id BIGINT NOT NULL,
    financial_group_id	VARCHAR(7) NOT NULL,
	financial_sub_group_id VARCHAR(7) NOT NULL,    
    credit BIT NOT NULL,
    expense BIT NOT NULL,
    document_status TINYINT NOT NULL,
    payment_data DATETIME NOT NULL,
    document_value DECIMAL(19,2) NOT NULL,
    document_note VARCHAR(1000) NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
	launch_user BIGINT NULL,
    launch_date DATETIME NULL,
	close_user BIGINT NULL,
    close_date DATETIME NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX ix_cashier_closing_company_launch on cashier_closing_company_launch(cashier_closing_id, user_operator, company_id);

CREATE TABLE `manager`.`cashier_closing_document_resume`
(
	cashier_closing_id BIGINT NOT NULL,
    week_year INT NOT NULL,
	document_movement_id BIGINT NOT NULL,
    company_id BIGINT NULL, 
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(cashier_closing_id, week_year, document_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* Date 09/03/2024 */
alter table user add cash_closing_max_discount decimal(19,2) NULL;

/* Date 12/05/2024 */
alter table financial_transfer_group_setting add execution_days varchar(50) null;
ALTER TABLE financial_transfer_group_setting MODIFY execution_days varchar(50) AFTER week_day;

alter table financial_transfer_group_setting add execution_time varchar(5) null;
ALTER TABLE financial_transfer_group_setting MODIFY execution_time varchar(5) AFTER execution_days;


/* Date 15/05/2024 */
INSERT INTO jobs(id, parent_id, description, sync_timer, item_name, records_processed, initial_record, final_record, initial_version_record, final_version_record, processing_status, processing_message, processing_data, inactive, user_creation, creation_date, user_change, change_date)
select 8, 1, 'Generate Cashing Closing Movement', '0 0/30 * * * ?', 'generate_cashing_closing_movement', 0, 0, 0, NULL, NULL, 1, NULL, NULL, 0, 1, '2024-05-14 17:01:27', 1, '2024-05-14 17:01:27';

/* Date 27/05/2024 */
-- drop table `manager`.`cashier_closing_company_launch_attachment`;
CREATE TABLE `manager`.`cashier_closing_company_launch_attachment` 
(
	id BIGINT NOT NULL AUTO_INCREMENT,
    cashier_closing_id BIGINT NOT NULL,
    user_operator BIGINT NOT NULL,
    attachment_description VARCHAR(1000) NOT NULL,
    attachment_type VARCHAR(5) NOT NULL, 
    file_data MEDIUMBLOB NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    inactive BIT NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- drop table `manager`.`document_movement_attachment`;
CREATE TABLE `manager`.`document_movement_attachment` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    document_parent_id BIGINT NOT NULL,
    company_d BIGINT NOT NULL,
    attachment_description VARCHAR(1000) NOT NULL,
    attachment_type VARCHAR(5) NOT NULL,
    file_data MEDIUMBLOB NOT NULL,
    user_creation BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_change BIGINT NOT NULL,
    change_date DATETIME NOT NULL,
    inactive BIT NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;

/** Date create 06/06/2024 */
ALTER TABLE `financial_transfer_group_setting` ADD COLUMN fixed_day_month BIT NULL;
ALTER TABLE `financial_transfer_group_setting` MODIFY fixed_day_month BIT AFTER week_day;

SET SQL_SAFE_UPDATES = 0;
update financial_transfer_group_setting set fixed_day = 1 where execution_period = 7;
update financial_transfer_group_setting set fixed_day_month = 1 where execution_period <> 7;
select * from financial_transfer_group_setting where fixed_day_month = 1 and execution_days is null;

/** 30 e 31 dias */
update financial_transfer_group_setting g
inner join (
select id, bank_account_origin_id, bank_account_destiny_id, cast(DATE_FORMAT(execution_initial_period, "%d") as SIGNED) dia,  execution_initial_period from financial_transfer_group_setting where fixed_day_month = 1 and execution_days is null and execution_initial_period is not null 
and execution_period in (30, 31)
) f on g.id = f.id and g.bank_account_origin_id = f.bank_account_origin_id and g.bank_account_destiny_id = f.bank_account_destiny_id
set g.execution_days = f.dia;

/** 15 e 16 dias */
update financial_transfer_group_setting g
inner join (
select id, bank_account_origin_id, bank_account_destiny_id, cast(DATE_FORMAT(execution_initial_period, "%d") as SIGNED) dia,  execution_initial_period from financial_transfer_group_setting where fixed_day_month = 1 and execution_days is null and execution_initial_period is not null and execution_period in (15, 16)
) f on g.id = f.id and g.bank_account_origin_id = f.bank_account_origin_id and g.bank_account_destiny_id = f.bank_account_destiny_id
set g.execution_days = '1,16';


/** 15/06/2024 */
ALTER TABLE `manager`.`document_movement` ADD COLUMN is_automatic_transfer bit(1) NULL;
ALTER TABLE `manager`.`document_movement` MODIFY is_automatic_transfer bit(1) AFTER is_document_transfer;

/** 09/07/2024 */
ALTER TABLE `manager`.`financial_transfer_group_item_setting` ADD COLUMN group_product_id INT NULL;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` MODIFY group_product_id INT AFTER financial_sub_group_id;

/** 17/07/2024 */
ALTER TABLE `manager`.`financial_transfer_group_item_setting` ADD COLUMN sub_group_product_id INT NULL;
ALTER TABLE `manager`.`financial_transfer_group_item_setting` MODIFY sub_group_product_id INT AFTER group_product_id;

/** 05/09/2024 */
-- drop table `manager`.`game_type`;
CREATE TABLE `manager`.`game_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- drop table `manager`.`machine_type`;
CREATE TABLE `manager`.`machine_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `manager`.`product` ADD COLUMN alias_product varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;
ALTER TABLE `manager`.`product` MODIFY alias_product varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci AFTER enable_clock_movement;
ALTER TABLE `manager`.`product` ADD COLUMN game_type INT NULL;
ALTER TABLE `manager`.`product` MODIFY game_type INT AFTER alias_product;
ALTER TABLE `manager`.`product` ADD COLUMN machine_type INT NULL;
ALTER TABLE `manager`.`product` MODIFY machine_type INT AFTER game_type;

/**Create 12/09/2024 */
ALTER TABLE `manager`.`company` ADD COLUMN financial_cost_center_id INT NULL;
ALTER TABLE `manager`.`company` MODIFY financial_cost_center_id INT AFTER person_type_id;

-- Creation date 14/09/2024
-- drop table `manager`.`room_server`;
CREATE TABLE `manager`.`room_server` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` varchar(50) NOT NULL,
  `server_id` varchar(50) NOT NULL,
  `server_name` varchar(50) NOT NULL,
  `quantity_machines` int NULL,
  `period_billing` varchar(50) NULL,
  `start_billing` varchar(50) NULL,
  `integration_system_id` int NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- drop table `manager`.`room_server_machine`;
CREATE TABLE `manager`.`room_server_machine` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` varchar(50) NOT NULL,
  `machine_id` varchar(50) NOT NULL,
  `machine_name` varchar(50) NOT NULL,
  `contract_id` int NULL,
  `contract_name` varchar(50) NULL,
  `period` varchar(50) NULL,
  `expires` varchar(50) NULL,
  `game_quantity` varchar(50) NULL,
  `input` bigint NULL,
  `output` bigint NULL,
  `integration_system_id` int NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- drop table `manager`.`room_server_machine_billing`;
CREATE TABLE `manager`.`room_server_machine_billing` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `machine_id` varchar(50) NOT NULL,
  `machine_name` varchar(50) NOT NULL,
  `input` decimal(19,2) NULL,
  `output` decimal(19,2) NULL,
  `integration_system_id` int NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- drop table `manager`.`contract_billing`;
CREATE TABLE `manager`.`contract_billing` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(50) NOT NULL,
  `contract_name` varchar(50) NOT NULL,
  `machine_quantity` int NULL,
  `contract_origin_id` varchar(50) NOT NULL,
  `contract_destiny_id` varchar(50) NOT NULL,
  `contract_value` decimal(19,2) NOT NULL,
  `contract_value_type` tinyint NOT NULL, -- 0 percentage, 1 value
  `reduce_expense` bit NULL, 
  `integration_system_id` int NOT NULL,
  `inactive` bit(1) NOT NULL,
  `user_creation` bigint NOT NULL,
  `creation_date` datetime NOT NULL,
  `user_change` bigint NOT NULL,
  `change_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;