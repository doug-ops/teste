use manager;
/**
SET SQL_SAFE_UPDATES = 0;

select * from financial_sub_group_integration_system;
select * from financial_sub_group;
select * from financial_group;
select * from financial_group_integration_system;

delete from financial_sub_group_integration_system;
delete from financial_sub_group;
delete from financial_group_integration_system;
delete from financial_group;
*/

/**
select 
'insert into financial_group(' +
'id, ' + 
'description, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), grupo)+','
+''''+descricao+''','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_fin_grupos_estrutura where id=0;

select 
'insert into financial_group_integration_system(' +
'grupo, ' + 
'integration_system_id, ' +
'legacy_id, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), grupo)+','
+convert(varchar(1), 1)+','
+convert(varchar(8), grupo)+','
+convert(varchar(1), 0)+',1,now(),1,now();'  from tb_fin_grupos_estrutura where id=0;
*/
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 1,'VENDAS DIRETO JBL',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 2,'VENDAS OPERADORES',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 3,'DESPESAS OPERADORES',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 5,'DESPESAS LOJA',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 6,'DESPESAS FIXAS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 7,'DESPESAS VARIAVEIS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 8,'PESSOAS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 9,'IMPOSTOS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 10,'INVESTIMENTOS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 11,'VENDAS DE PRODUTOS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 12,'VENDAS SERVIÇOS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 13,'OUTROS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 14,'PROLABORES PL',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 15,'CARTÃO CREDITO',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 16,'RENDIMENTOS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 17,'VENDAS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 18,'SAIDAS',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 19,'PARTICIPAÇAO SOCIEDADE',0,1,now(),1,now();
insert into financial_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 20,'TRANSF BANCARIA',0,1,now(),1,now();

insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 1,1,1,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 2,1,2,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 3,1,3,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 5,1,5,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 6,1,6,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 7,1,7,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 8,1,8,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 9,1,9,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 10,1,10,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 11,1,11,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 12,1,12,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 13,1,13,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 14,1,14,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 15,1,15,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 16,1,16,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 17,1,17,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 18,1,18,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 19,1,19,0,1,now(),1,now();
insert into financial_group_integration_system(financial_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 20,1,20,0,1,now(),1,now();
