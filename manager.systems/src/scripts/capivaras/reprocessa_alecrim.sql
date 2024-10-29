use manager;
select * from company where social_name like '%crim%'

select sum(case when credit = 1 then document_value else document_value * -1 end) total from document_movement where company_id = 511 and document_parent_id = 393741 and is_product_movement = 1

select 10106.0000 / 2
5053.00000000

select * from document_movement where company_id = 511 and document_parent_id = 393741
select 8573.00 - 465.00
select * from bank_account where id = 211
update bank_account set bank_balance_available = bank_balance_available - 465.00 where id = 211
8108.00
select * from bank_account_statement where bank_account_id = 211 and document_id = 1422735
select * from company where id = 511;
select * from person where id = 2722;

SET SQL_SAFE_UPDATES = 0;
update bank_account_statement set  bank_balance_available = (8258.00-150.00)  where bank_account_id = 211 and document_id = 1422737

select 8258.00 - 150.00
select 5053.00 - 4588.00

8723.00
update document_movement set document_value = 5053, payment_value = 5053 where id = 513269 and document_parent_id = 393741