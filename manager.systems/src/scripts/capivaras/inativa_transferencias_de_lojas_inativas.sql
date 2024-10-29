DROP TEMPORARY TABLE IF EXISTS tmp_inactive;
CREATE TEMPORARY TABLE IF NOT EXISTS tmp_inactive
(
	bank_account_origin_id int
);

insert into tmp_inactive
select 
p.bank_account_id
from 
person p
inner join company c on p.id = c.person_id
where 
p.inactive=1 and 
p.object_type='COM'
order by c.id;

update financial_transfer_group_setting s 
inner join tmp_inactive i on s.bank_account_origin_id = i.bank_account_origin_id
set inactive = 1;

update financial_transfer_group_item_setting s 
inner join tmp_inactive i on s.bank_account_origin_id = i.bank_account_origin_id
set inactive = 1;

update financial_transfer_product_setting s 
inner join tmp_inactive i on s.bank_account_origin_id = i.bank_account_origin_id
set inactive = 1;

update financial_transfer_product_setting s 
inner join tmp_inactive i on s.bank_account_origin_id = i.bank_account_origin_id
set inactive = 1;

update bank_account s 
inner join tmp_inactive i on s.id = i.bank_account_origin_id
set inactive = 1;


DROP TEMPORARY TABLE IF EXISTS tmp_inactive;
CREATE TEMPORARY TABLE IF NOT EXISTS tmp_inactive
(
	provider_id bigint
);

insert into tmp_inactive
select 
p.credit_provider_id
from 
person p
inner join company c on p.id = c.person_id
where 
p.inactive=1 and 
p.object_type='COM'
order by c.id;

insert into tmp_inactive
select 
p.debit_provider_id
from 
person p
inner join company c on p.id = c.person_id
where 
p.inactive=1 and 
p.object_type='COM'
order by c.id;

update person p 
inner join provider o on o.person_id = p.id 
inner join tmp_inactive i on o.id = i.provider_id 
set inactive = 1; 
