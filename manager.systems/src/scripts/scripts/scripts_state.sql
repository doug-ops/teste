CREATE DATABASE IF NOT EXISTS `manager`;

use manager;

drop table if exists `manager`.`address_state`;
drop table if exists `manager`.`address_state_region`;

CREATE TABLE `manager`.`address_state_region` 
(
  address_region int(11) NOT NULL,
  description varchar(100) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (address_region)
);

insert into address_state_region (address_region, description) values (1, 'Norte');
insert into address_state_region (address_region, description) values (2, 'Nordeste');
insert into address_state_region (address_region, description) values (3, 'Sudeste');
insert into address_state_region (address_region, description) values (4, 'Sul');
insert into address_state_region (address_region, description) values (5, 'Centro-Oeste');

CREATE TABLE `manager`.`address_state` 
(
  address_state_ibge int(11) NOT NULL,
  description varchar(100) CHARACTER SET utf8 NOT NULL,
  state_acronym varchar(2) CHARACTER SET utf8 NOT NULL,
  address_region int(11) NOT NULL,
  PRIMARY KEY(address_state_ibge),
  FOREIGN KEY (address_region) REFERENCES address_state_region(address_region)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into address_state (address_state_ibge, description, state_acronym, address_region) values (12, 'Acre', 'AC', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (27, 'Alagoas', 'AL', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (16, 'Amapá', 'AP', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (13, 'Amazonas', 'AM', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (29, 'Bahia', 'BA', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (23, 'Ceará', 'CE', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (53, 'Distrito Federal', 'DF', 5);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (32, 'Espírito Santo', 'ES', 3);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (52, 'Goiás', 'GO', 5);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (21, 'Maranhão', 'MA', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (51, 'Mato Grosso', 'MT', 5);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (50, 'Mato Grosso do Sul', 'MS', 5);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (31, 'Minas Gerais', 'MG', 3);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (15, 'Pará', 'PA', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (25, 'Paraíba', 'PB', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (41, 'Paraná', 'PR', 4);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (26, 'Pernambuco', 'PE', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (22, 'Piauí', 'PI', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (33, 'Rio de Janeiro', 'RJ', 3);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (24, 'Rio Grande do Norte', 'RN', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (43, 'Rio Grande do Sul', 'RS', 4);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (11, 'Rondônia', 'RO', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (14, 'Roraima', 'RR', 1);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (42, 'Santa Catarina', 'SC', 4);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (35, 'São Paulo', 'SP', 3);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (28, 'Sergipe', 'SE', 2);
insert into address_state (address_state_ibge, description, state_acronym, address_region) values (17, 'Tocantins', 'TO', 1);