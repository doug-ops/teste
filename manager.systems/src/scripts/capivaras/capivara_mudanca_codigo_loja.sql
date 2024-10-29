select * from company where id > 1717;

update company set social_name='OPER. URSO_bk', fantasy_name='OPER. URSO_bk' where id = 100153;

insert into company(id, person_id, social_name, fantasy_name, client_id, negative_close, process_movement_automatic, email_collector, email_operator, id_old, retaguarda_id, establishment_type_id, person_type_id, change_date, user_change)
select 20, 5619, 'OPER. URSO', 'OPER. URSO', 2, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-05 12:08:23', 7;



select * from company where id = 17;
select * from document_movement where company_id = 100153;
select * from company_integration_system where company_id = 100153;
select * from user_company  where company_id = 100153;
select * from product where company_id = 100008;
select * from product_movement where company_id = 100153;
select * from send_email_document where company_id = 100153;
select * from company_movement_execution where company_id = 100153;
select * from cashier_closing_header where company_id = 100153;
select * from cashier_closing_item where company_id = 100153;

SET SQL_SAFE_UPDATES = 0;
update company_integration_system set company_id = 20 where company_id = 100153;
update document_movement set company_id = 20 where company_id = 100153;
update user_company set company_id = 20 where company_id = 100153;
update product set company_id = 20 where company_id = 100153;
update product_movement set company_id = 20 where company_id = 100153;
update send_email_document set company_id = 20 where company_id = 100153;
update company_movement_execution set company_id = 20 where company_id = 100153;
update cashier_closing_header set company_id = 20 where company_id = 100153;
update cashier_closing_item set company_id = 20 where company_id = 100153;
delete from company where id = 100153;

/**
# id, person_id, social_name, fantasy_name, client_id, negative_close, process_movement_automatic, email_collector, email_operator, id_old, retaguarda_id, establishment_type_id, person_type_id, change_date, user_change
2 - 100001	2	OFICINA SP CAPI	OFICINA SP CAPI	2	0	0			1			1	2023-03-01 16:11:17	1
7 - 100002	5875	OFICINA RJ KRK	OFICINA RJ KRK	2	0	0							2023-03-20 17:38:35	7    
8 - '100003', '9', 'OFICINA RJ AGR', 'OFICINA RJ AGR', '2', '0', '0', NULL, NULL, '3', NULL, NULL, '1', '2023-08-05 12:23:39', '7'
10 - '100004', '5991', 'OFICINA CURI', 'OFICINA CURI', '2', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2023-04-25 11:14:03', '7'
15 - '100006', '14', 'OFICINA SP EDI', 'OFICINA SP EDI', '2', '0', '0', NULL, NULL, '6', NULL, NULL, '1', '2023-06-07 09:35:30', '7'
16 - '100007', '6202', 'MATERIAL PERDA CHUVA', 'MATERIAL PERDA CHUVA', '2', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-13 09:40:21', '7'
17 - '100008', '6239', 'OFICINA ESTOQUE BJ', 'OFICINA ESTOQUE BJ', '2', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2023-07-03 14:42:31', '7'
18 - '100009', '6327', 'OPER. SANTA MARTA', 'OPER. SANTA MARTA', '2', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2023-07-13 14:06:39', '7'
19 - '100128', '2462', 'TROPICAL', 'TROPICAL', '2', '0', '0', NULL, NULL, '128', NULL, NULL, '1', '2023-03-21 15:57:46', '7'
20 - '100153', '5619', 'OPER. URSO', 'OPER. URSO', '2', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-05 12:08:23', '7'
*/