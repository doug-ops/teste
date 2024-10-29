CREATE TABLE api_user(
	id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
   	name varchar(100) NOT NULL,
   	email varchar(50) NOT NULL,
   	username varchar(100) NULL,
	password varchar(100) NULL,
	account_non_expired bit NOT NULL,
	account_non_locked bit NOT NULL,
	credentials_non_expired bit NOT NULL,
   	inactive bit NOT NULL
);
CREATE TABLE api_role(
	id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
	alias varchar(20) NOT NULL,
	description varchar(50) NOT NULL,
    CONSTRAINT UC_api_role_alias UNIQUE (alias)
);
CREATE TABLE api_user_roles(
	user_id bigint NOT NULL,
	role_id int NOT NULL
);
ALTER TABLE api_user_roles ADD CONSTRAINT fk_api_user_roles_user FOREIGN KEY (user_id) REFERENCES api_user (id);
ALTER TABLE api_user_roles ADD CONSTRAINT fk_api_user_roles_role FOREIGN KEY (role_id) REFERENCES api_role (id);