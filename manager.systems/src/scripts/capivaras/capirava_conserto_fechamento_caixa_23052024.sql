select sum(document_value) from cashier_closing_company_document where company_id = 106 and cashier_closing_id = 3;
select sum(document_value) from cashier_closing_company_document where company_id = 1606 and cashier_closing_id = 4;
select * from cashier_closing_company_launch where cashier_closing_id = 2 and company_id = 106;

-- skinia matheus 
select * from cashier_closing_company_launch where cashier_closing_id = 2 and company_id = 106;
-- puxou 500 do lancamento da semana anterior e nao deveria 
500.00

select * from cashier_closing_company_launch where cashier_closing_id = 2 and company_id = 1606;
392.00

-- 1259.50 saldo que deve ter que e o valor da duplicata da semana
-- esta com uma diferenca de 779 

select * from document_movement where company_id = 1606 and is_cashing_close = 1;
1295.00

select (903.0000 + 390.00)

select * from company where id = 1606

update document_movement set document_value = 1295, payment_value = 1295 where id = 514089
update bank_account_statement set document_value = -1295, bank_balance_available = 1259.50 where id =  512170
select * from bank_account_statement where bank_account_id = 2021

select * from bank_account where id = 2021
update bank_account set bank_balance_available = 1259.50 where id = 2021
select 872.50 + 480

select * from document_movement where company_id = 182 and bank_account_origin_id = 2021
update document_movement set document_value = document_value + 392.0000, payment_value = payment_value + 392.0000 where id = 514091

-- 243154.84
-- 243546.84
select * from bank_account where id = 199
update bank_account set bank_balance_available = bank_balance_available + 392 where id = 199
select 1295.00 - 903.0000

select * from bank_account_statement where bank_account_id = 199 and bank_account_origin_id = 2021


update bank_account_statement set document_value = document_value + 392.0000 where id = 512171

13450.5000
select * from document_movement where document_parent_id = 394217

select * from bank_account where id = 1051
update bank_account set bank_balance_available = bank_balance_available - 500 where id = 1051


update document_movement set document_value = document_value + 500, payment_value = payment_value + 500 where id = 514238

select * from bank_account_statement where bank_account_id = 1051 and bank_account_origin_id = 2021

update bank_account_statement set document_value = -13450, bank_balance_available = 0 where id =  512309

select * from document_movement where company_id = 182 and bank_account_origin_id = 1051
update document_movement set document_value = document_value + 500, payment_value = payment_value + 500 where id = 514240

-- 243546.84
select * from bank_account where id = 199
update bank_account set bank_balance_available = bank_balance_available + 500 where id = 199
select 1295.00 - 903.0000

select * from bank_account_statement where bank_account_id = 199 and bank_account_origin_id = 1051
update bank_account_statement set document_value = 13450, bank_balance_available = bank_balance_available + 500 where id =  512310


select * from document_movement where document_parent_id = 393550 

select * from cashier_closing_company_document where company_id = 1606 and cashier_closing_id = 4;


select * from cashier_closing_company where company_id = 1606 and cashier_closing_id = 4;
