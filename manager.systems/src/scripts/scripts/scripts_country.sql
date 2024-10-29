CREATE DATABASE IF NOT EXISTS `manager`;

use manager;

drop table if exists `manager`.`address_country`;
CREATE TABLE `manager`.`address_country` 
(
  address_country_ibge int(11) NOT NULL,
  description varchar(100) CHARACTER SET utf8 NOT NULL,
  country_acronym varchar(5) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY(address_country_ibge)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `manager`.`address_country`(address_country_ibge, description, country_acronym) values
(1058, 'Brasil', 'BR');