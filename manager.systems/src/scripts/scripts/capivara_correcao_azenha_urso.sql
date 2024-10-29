select 
*
-- sum(case when credit = 1 then document_value else document_value * -1 end)  
from document_movement where company_id = 394;

 document_parent_id = 383621;

387190;

select * from document_movement where document_id = 1405992

update document_movement set inactive = 1 where document_id = 1405992

select * from bank_account_statement where bank_account_id = 1565 and bank_account_origin_id = 221;


select * from company where id = 20;
select * from person where id = 5619;
select * from bank_account where id = 1565;

# id, bank_account_id, bank_account_origin_id, launch_type, provider_id, person_id, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date
-- 504324, 1565, 221, 2, 2217, 5619, 389762, 1405995, -6782.50, TRANSF. MOVIMENTO:389760 >> EMPRESA: AZENHA ROMA POA BRAVO >> EMPRESA: OPER. URSO, -19552.02, 0, 1, 2024-05-07 18:35:55, 1, 2024-05-07 18:35:55
delete from bank_account_statement where id = 504324;

update bank_account set bank_balance_available = bank_balance_available + 6782.50 where id = 1565;
select * from document_movement where company_id = 20 and document_value = 6782.50;

# id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id
-- '506219', '1405995', '389762', NULL, NULL, NULL, NULL, '2', '0', '20', '5512', '1565', '221', NULL, NULL, '1405995', '6782.5000', 'TRANSF. MOVIMENTO:389760 >> EMPRESA: AZENHA ROMA POA BRAVO >> EMPRESA: OPER. URSO', '2', '6782.5000', '0.0000', '0.0000', '0', '2024-05-07 18:35:55', '2024-04-15 07:57:29', '1', NULL, '0', '0', '0', '01.000', '01.001', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', '2024-05-07 18:35:55', '1', '2024-05-07 18:35:55', NULL
delete from document_movement where id = 506219;


select * from document_movement where company_id = 394 and document_id = 1382550;
# id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id
-- '506216', '1405992', '383621', NULL, NULL, NULL, NULL, '4', '0', '394', '219', '221', '0', NULL, NULL, '1405992', '2673.7500', '%75.00/F:219/PGTO LOJA %', '2', '2673.7500', '0.0000', '0.0000', '0', '2024-05-07 18:35:55', '2024-04-15 07:57:29', '1', NULL, '0', '0', '0', '06.000', '06.001', '0', NULL, NULL, '134', '1', '1', NULL, '1', '1', '2024-05-06 00:00:00', '1', '2024-05-07 18:35:55', NULL
-- '506217', '1405993', '389760', NULL, NULL, NULL, NULL, '4', '0', '394', '2379', '378', '0', NULL, NULL, '1405993', '356.5000', '%10.00/F:2379/HAB - J.B.', '2', '356.5000', '0.0000', '0.0000', '0', '2024-05-07 18:35:55', '2024-04-15 07:57:29', '1', NULL, '0', '0', '0', '06.000', '06.003', '0', NULL, NULL, '134', '1', '2', NULL, '0', '1', '2024-05-06 00:00:00', '1', '2024-05-07 18:35:55', NULL
-- '506218', '1405994', '389761', NULL, NULL, NULL, NULL, '2', '1', '394', '2217', '221', '1565', NULL, NULL, '1405994', '6782.5000', 'TRANSF. MOVIMENTO:389760 >> EMPRESA: AZENHA ROMA POA BRAVO >> EMPRESA: OPER. URSO', '2', '6782.5000', '0.0000', '0.0000', '0', '2024-05-07 18:35:55', '2024-04-15 07:57:29', '1', NULL, '0', '0', '0', '22.000', '22.001', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', '2024-05-07 18:35:55', '1', '2024-05-07 18:35:55', NULL
delete from document_movement where id = 506217;
select * from document_movement where id = 506217;

-- delete from document_movement where id = 506216;

select * from bank_account_statement where document_value = -356.50
select * from bank_account_statement where bank_account_id = 221 order by id;
# id, bank_account_id, bank_account_origin_id, launch_type, provider_id, person_id, document_parent_id, document_id, document_value, document_note, bank_balance_available, inactive, user_creation, creation_date, user_change, change_date
-- '504321', '221', '0', '4', '219', '2752', '383621', '1405992', '-2673.75', '%75.00/F:219/PGTO LOJA %', '891.25', '0', '1', '2024-05-07 18:35:55', '1', '2024-05-07 18:35:55'
-- '504323', '221', '1565', '2', '2217', '2752', '389761', '1405994', '6782.50', 'TRANSF. MOVIMENTO:389760 >> EMPRESA: AZENHA ROMA POA BRAVO >> EMPRESA: OPER. URSO', '7673.75', '0', '1', '2024-05-07 18:35:55', '1', '2024-05-07 18:35:55'
-- 	504322	378	0	4	2379	2752	389760	1405993	-356.50	%10.00/F:2379/HAB - J.B.	-12769.52	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
  
delete from bank_account_statement where id = 504322;
select * from bank_account_statement where document_parent_id = 383621;

select * from  bank_account where id = 221;
# id, description, bank_balance_available, bank_limit_available, bank_code, bank_angency, accumulate_balance, inactive, user_creation, creation_date, user_change, change_date
-- '221', 'AZENHA ROMA POA BRAVO', '7673.75', '0.00', NULL, '0', '1', '0', '1', '2021-04-21 11:25:41', '7', '2024-04-15 18:32:52'

update bank_account set bank_balance_available = 0 where id = 221;

select * from  bank_account where id = 378;
use manager;
update bank_account set bank_balance_available = 1.02 where id = 394;

select * from  bank_account where id = 221;
504301	221	0	4	2216	2752	383621	1382531	54081.00	PROD:13710/EI:472204100/EF:477612200	54081.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504302	221	0	4	2217	2752	383621	1382532	-57112.00	PROD:13710/SI:374879400/SF:380590600	-3031.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504303	221	0	4	2216	2752	383621	1382533	6780.00	PROD:14156/EI:258242500/EF:258920500	3749.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504304	221	0	4	2217	2752	383621	1382534	-5736.00	PROD:14156/SI:197559100/SF:198132700	-1987.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504305	221	0	4	2216	2752	383621	1382535	7690.00	PROD:14314/EI:135699200/EF:136468200	5703.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504306	221	0	4	2217	2752	383621	1382536	-6296.00	PROD:14314/SI:101965300/SF:102594900	-593.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504307	221	0	4	2216	2752	383621	1382537	5170.00	PROD:16370/EI:237243200/EF:237760200	4577.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504308	221	0	4	2217	2752	383621	1382538	-3072.00	PROD:16370/SI:184608300/SF:184915500	1505.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504309	221	0	4	2216	2752	383621	1382539	20070.00	PROD:19189/EI:182205200/EF:184212200	21575.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504310	221	0	4	2217	2752	383621	1382540	-18584.00	PROD:19189/SI:139213900/SF:141072300	2991.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504311	221	0	4	2216	2752	383621	1382541	15780.00	PROD:21608/EI:122788700/EF:124366700	18771.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504312	221	0	4	2217	2752	383621	1382542	-19861.00	PROD:21608/SI:97019400/SF:99005500	-1090.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504313	221	0	4	2216	2752	383621	1382543	5940.00	PROD:21788/EI:73751400/EF:74345400	4850.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504314	221	0	4	2217	2752	383621	1382544	-5310.00	PROD:21788/SI:57585000/SF:58116000	-460.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504315	221	0	4	2216	2752	383621	1382545	4610.00	PROD:22099/EI:41698600/EF:42159600	4150.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504316	221	0	4	2217	2752	383621	1382546	-4334.00	PROD:22099/SI:35389300/SF:35822700	-184.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504317	221	0	4	2216	2752	383621	1382547	17410.00	PROD:22709/EI:70920000/EF:72661000	17226.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504318	221	0	4	2217	2752	383621	1382548	-17451.00	PROD:22709/SI:56844300/SF:58589400	-225.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504319	221	0	4	2216	2752	383621	1382549	12470.00	PROD:23303/EI:29242000/EF:30489000	12245.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55
504320	221	0	4	2217	2752	383621	1382550	-8680.00	PROD:23303/SI:23365400/SF:24233400	3565.00	0	1	2024-05-07 18:35:55	1	2024-05-07 18:35:55

-- 3565.0000

insert into manager.document_movement(id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id)
select id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id from manager_test.document_movement where document_parent_id = 383621;


select sum(document_value) from bank_account_statement where document_parent_id = 383621 and document_id in (1382531
,1382532
,1382533
,1382534
,1382535
,1382536
,1382537
,1382538
,1382539
,1382540
,1382541
,1382542
,1382543
,1382544
,1382545
,1382546
,1382547
,1382548
,1382549
,1382550) and 
id in (504301
,504302
,504303
,504304
,504305
,504306
,504307
,504308
,504309
,504310
,504311
,504312
,504313
,504314
,504315
,504316
,504317
,504318
,504319
,504320
)