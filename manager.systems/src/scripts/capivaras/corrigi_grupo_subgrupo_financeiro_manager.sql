use manager;

-- update IN financial_group_id = '24.000', financial_sub_group_id='24.001'
update provider x 
inner join 
(
	select 
		p.credit_provider_id, 
		p.debit_provider_id, 
		pc.financial_group_id cred_financial_group_id, 
		pc.financial_sub_group_id cred_financial_sub_group_id,
		pd.financial_group_id deb_financial_group_id, 
		pd.financial_sub_group_id deb_financial_sub_group_id
	from 
		person p 
		inner join provider pc on p.credit_provider_id = pc.id
		inner join provider pd on p.debit_provider_id = pd.id
	where 
		p.object_type='COM' 
		and p.credit_provider_id is not null 
		and p.debit_provider_id is not null
    ) filtro on x.id = filtro.credit_provider_id
    set x.financial_group_id = '24.000', x.financial_sub_group_id='24.001';
    
-- update OUT financial_group_id = '24.000', financial_sub_group_id='24.002'
update provider x 
inner join 
(
	select 
		p.credit_provider_id, 
		p.debit_provider_id, 
		pc.financial_group_id cred_financial_group_id, 
		pc.financial_sub_group_id cred_financial_sub_group_id,
		pd.financial_group_id deb_financial_group_id, 
		pd.financial_sub_group_id deb_financial_sub_group_id
	from 
		person p 
		inner join provider pc on p.credit_provider_id = pc.id
		inner join provider pd on p.debit_provider_id = pd.id
	where 
		p.object_type='COM' 
		and p.credit_provider_id is not null 
		and p.debit_provider_id is not null
    ) filtro on x.id = filtro.debit_provider_id
    set x.financial_group_id = '24.000', x.financial_sub_group_id='24.002';

-- Corrigi grupo e subgrupo financeiro in out
SET SQL_SAFE_UPDATES = 0;    
update document_movement set financial_group_id='24.000', financial_sub_group_id='24.001' where is_product_movement = 1 and document_note like '%/EI:%';
update document_movement set financial_group_id='24.000', financial_sub_group_id='24.002' where is_product_movement = 1 and document_note like '%/SI:%';

-- Verifica se tem grupo financeiro de in out errado
select * from document_movement where is_product_movement = 1 and financial_group_id <> '24.000';

-- Verifica se tem sub grupo financeiro de in out errado
select * from document_movement where is_product_movement = 1 and financial_sub_group_id not in ('24.001', '24.002');

-- Corrige grupo financeiro e sub grupo das transferencia
update provider x 
inner join 
(select provider_id from financial_transfer_group_item_setting group by provider_id) filtro
on x.id=filtro.provider_id
set x.financial_group_id = '24.000', x.financial_sub_group_id='24.003';

-- Verifica se tem grupo financeiro de trabsferencia errado
select * from document_movement x 
inner join (select provider_id from financial_transfer_group_item_setting group by provider_id) filtro
on x.provider_id=filtro.provider_id
where financial_group_id <> '24.000';

-- Verifica se tem sub grupo financeiro de trabsferencia errado
select * from document_movement x 
inner join (select provider_id from financial_transfer_group_item_setting group by provider_id) filtro
on x.provider_id=filtro.provider_id
where financial_sub_group_id <> '24.003';

-- verificar HAB
select * from provider where social_name like '%HAB%'

UPDATE provider SET financial_group_id='22.000', financial_sub_group_id='22.001' where id = 112
update document_movement SET financial_group_id='22.000', financial_sub_group_id='22.001' where provider_id = 112

SET SQL_SAFE_UPDATES = 0;
UPDATE provider SET financial_group_id='25.000', financial_sub_group_id='25.001' where social_name like '%HAB%';


select * from provider_ungroup;

select * from document_movement where provider_id in (
239,
243,
278,
2086,
2087,
2530,
2531
)

update document_movement set financial_group_id='25.000', financial_sub_group_id='25.001' where provider_id in (
239,
243,
278,
2086,
2087,
2530,
2531
)