use manager;
/**
select 
'insert into product_sub_group(' +
'id, ' + 
'product_group_id, ' + 
'description, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), codigo_subgrupo)+','
+convert(varchar(8), codigo_grupo)+','
+''''+descricao+''','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_cad_subgrupo;

select 
'insert into product_sub_group_integration_system(' +
'sub_group_id, ' + 
'integration_system_id, ' +
'legacy_id, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), codigo_subgrupo)+','
+convert(varchar(1), 1)+','
+convert(varchar(8), codigo_subgrupo)+','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_cad_subgrupo;
*/
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 1,1,'Maquinas',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 2,2,'Maquinas Offiline',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 3,3,'MONITORES',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 4,3,'TV',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 5,3,'CPU',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 6,3,'IMPRESSORAS',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 7,3,'DVR/CAMERAS',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 8,3,'PERIFERICOS',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 9,3,'FONTE',0,0,0,1,now(),1,now();
insert into product_sub_group(id, product_group_id, description, sale_price, conversion_factor, inactive, user_creation, creation_date, user_change, change_date) select 10,4,'COMPONENTES ELETRONICOS ',0,0,0,1,now(),1,now();

insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 1,1,1,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 2,1,2,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 3,1,3,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 4,1,4,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 5,1,5,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 6,1,6,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 7,1,7,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 8,1,8,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 9,1,9,0,1,now(),1,now();
insert into product_sub_group_integration_system(sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 10,1,10,0,1,now(),1,now();
