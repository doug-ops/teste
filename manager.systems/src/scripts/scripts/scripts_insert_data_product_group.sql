use manager;
/**
´select 
'insert into product_group(' +
'id, ' + 
'description, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), codigo_grupo)+','
+''''+descricao+''','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_cad_grupo;

select 
'insert into product_group_integration_system(' +
'group_id, ' + 
'integration_system_id, ' +
'legacy_id, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), codigo_grupo)+','
+convert(varchar(1), 1)+','
+convert(varchar(8), codigo_grupo)+','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_cad_grupo;
*/
insert into product_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 1,'Maquinas',0,1,now(),1,now();
insert into product_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 2,'Maquinas Offiline',0,1,now(),1,now();
insert into product_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 3,'EQUIPAMENTOS',0,1,now(),1,now();
insert into product_group(id, description, inactive, user_creation, creation_date, user_change, change_date) select 4,'COMPONENTES ELETRÔNICOS ',0,1,now(),1,now();

insert into product_group_integration_system(group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 1,1,1,0,1,now(),1,now();
insert into product_group_integration_system(group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 2,1,2,0,1,now(),1,now();
insert into product_group_integration_system(group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 3,1,3,0,1,now(),1,now();
insert into product_group_integration_system(group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 4,1,4,0,1,now(),1,now();
