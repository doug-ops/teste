/**
insert into document_movement_bk(id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id)
select 
id, document_id, document_parent_id, document_parent_id_origin, document_id_origin, document_transaction_id, is_cashing_close, document_type, credit, company_id, provider_id, bank_account_id, bank_account_origin_id, document_transfer_id, is_document_transfer, document_number, document_value, document_note, document_status, payment_value, payment_discount, payment_extra, payment_residue, payment_data, payment_expiry_data, payment_user, payment_status, nfe_number, nfe_serie_number, generate_billet, financial_group_id, financial_sub_group_id, is_product_movement, product_id, product_description, group_transfer_id, group_execution_order, group_item_execution_order, cashier_closing_date, inactive, user_creation, creation_date, user_change, change_date, financial_cost_center_id 
from document_movement where document_parent_id_origin = 411297
*/

    /** 
    Corrigi bank_account_origin_id para 1565 em vez de 47 no document_movement para movimentos de loja
    select * from document_movement d
    where 
    d.company_id <> 1
	and d.document_parent_id_origin = 411297
    and d.document_type = 2
    
    SET SQL_SAFE_UPDATES = 0;
    update document_movement d
    set d.bank_account_origin_id = 1565
    where 
    d.company_id <> 1
	and d.document_parent_id_origin = 411297
    */
    
        /**
     Corrigi bank_account_id para 1565 em vez de 47 no document_movement para movimentos de loja entrados na empresa 1
     
    select * from document_movement d
    where 
    d.company_id = 1
	and d.document_parent_id_origin = 411297
    and d.document_type = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%'
    
	update document_movement d
    set d.bank_account_id = 1565
        where 
    d.company_id = 1
	and d.document_parent_id_origin = 411297
    and d.document_type = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%'
    */
    
        /** Voltando saldo das lojas da conta cofre - colet. e creditando na 1565 
    select * from bank_account where id = 47
    -- 47	COFRE - COLET.	2276.01
    -- 61705.9000
    
    select * from bank_account where id = 1565
    -- 1565	COFRE POA GREMISTA	18439.22
    
    update bank_account set bank_balance_available = bank_balance_available - 61705.9000 where id = 47
    update bank_account set bank_balance_available = bank_balance_available + 61705.9000 where id = 1565
    
	select sum(case when credit = 1 then document_value else document_value * -1 end) document_value from document_movement d 
        where 
    d.company_id = 1
	and d.document_parent_id_origin = 411297
    and d.document_type = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%'
    */
    
        /** Atualizando a bank_account_origin_id para 1565 para extratos de loja 
    update bank_account_statement b
    inner join
    (
    select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 and 
    d.company_id <> 1 and d.document_type  = 2
    ) x 
    on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set b.bank_account_origin_id = 1565;
    */
    
    /** Atualizando a bank_account_id para 1565 para extratos da empresa 1 
    update bank_account_statement b
    inner join
    (
    select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 and 
	d.company_id = 1 and d.document_type  = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:%'

    ) x 
    on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set b.bank_account_id = 1565;
    */    
    
        /*
    -- Atualiza saldo despesa e residual 
	-- -1500
	select sum(case when credit = 1 then document_value else document_value * -1 end) document_value from document_movement d 
        where 
    d.document_parent_id_origin = 411297 and 
    d.company_id <> 1 and d.document_type  = 3
    
    select * from bank_account where id = 47
    -- 47	COFRE - COLET.	-59429.89
    
    select * from bank_account where id = 1565
    -- 1565	COFRE POA GREMISTA	80145.12
    
    update bank_account set bank_balance_available = bank_balance_available + 1500 where id = 47;
    update bank_account set bank_balance_available = bank_balance_available - 1500 where id = 1565;
    */
    
    /** Atualiza bank_acount_origin_id despesas e residual  das lojas
 update bank_account_statement b
    inner join
    (
    select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    d.bank_account_id
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 and 
    d.company_id <> 1 and d.document_type  = 3
    ) x 
    on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set b.bank_account_origin_id = 1565;
    */
    
      /** Atualiza bank_account_id da document_movement para o 1565 doa movimentos debitados da 47 
	update document_movement d 
    inner join 
    (
    select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    -- case when document_type = 2 then document_note else document_note end obs,
    document_note,
    d.user_change
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 and 
    d.company_id = 1 and d.document_type  = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> FORNECEDOR:%'
    and d.bank_account_id = 47
    ) x on d.id = x.id and  d.document_id = x.document_id and d.document_parent_id = x.document_parent_id
    set bank_account_id = 1565
    */
    
    /** Atualiza conta destino bank_account_id do extrato para despesas de fornecedor 
     update  bank_account_statement b
 inner join 
    (
    select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    -- case when document_type = 2 then document_note else document_note end obs,
    document_note,
    d.user_change
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 and 
    d.company_id = 1 and d.document_type  = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> FORNECEDOR:%'
    and d.bank_account_id = 1565
    ) x 
    on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set bank_account_id = 1565
    */    
    
 /** Atualiza sado de transferencias da conta 47  para 1565 
	-- -52967.1000
	select sum(case when credit = 1 then document_value else document_value * -1 end) document_value from document_movement d 
        where 
	d.document_parent_id_origin = 411297 and 
    d.company_id = 1 and d.document_type  = 2
    and d.document_note like 'TRANSF. FEC. CAIXA >> FORNECEDOR:%'
    and d.bank_account_id = 1565
    
    select * from bank_account where id = 47
    -- 47	COFRE - COLET.	-53111.09
    
    select * from bank_account where id = 1565
    -- 1565	COFRE POA GREMISTA	73826.32
    
    update bank_account set bank_balance_available = bank_balance_available + 52967.1000 where id = 47;
    update bank_account set bank_balance_available = bank_balance_available - 52967.1000 where id = 1565;
    
    */  
    
/** Atualiza conta origem das despesas que estava a 47 para o 1565 
update bank_account_statement b
inner join 
(
select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    -- case when document_type = 2 then document_note else document_note end obs,
    document_note,
    d.user_change
    
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 
    and d.document_note like 'TRANSF. FEC. CAIXA >> FORNECEDOR:%'
    and d.bank_account_origin_id = 47
) x on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set bank_account_origin_id = 1565
    
update document_movement d 
inner join 
(
select 
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    -- case when document_type = 2 then document_note else document_note end obs,
    document_note,
    d.user_change
    
from 
	document_movement d 
    inner join company c on d.company_id = c.id
    inner join bank_account bd on d.bank_account_id = bd.id
    inner join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297 
    and d.document_note like 'TRANSF. FEC. CAIXA >> FORNECEDOR:%'
    and d.bank_account_origin_id = 47
) x on d.id = x.id and  d.document_id = x.document_id and d.document_parent_id = x.document_parent_id
    set bank_account_origin_id = 1565
*/    
    
/**
atualiza origem das despesas
update document_movement set bank_account_origin_id = 1565 where id in (529425, 529426, 529427)
*/

    /** Atualiza origem conta das despesas no extrato 
    select b.* from document_movement x 
    inner join bank_account_statement b on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    where x.id in (529425, 529426, 529427)
    
    update bank_account_statement b
    inner join document_movement x 
    -- inner join bank_account_statement b 
    on b.document_id = x.document_id and b.document_parent_id = x.document_parent_id
    set b.bank_account_origin_id = 1565
    where x.id in (529425, 529426, 529427)
    */
select 
	-- sum(case when credit = 1 then document_value else document_value * -1 end) document_value

	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    d.document_note

 from document_movement_bk d
 left join company c on d.company_id = c.id
 left join bank_account bd on d.bank_account_id = bd.id
 left join bank_account bo on d.bank_account_origin_id = bo.id
 where d.company_id <> 1
 and d.document_status = 2
 ;

select 
	-- sum(case when credit = 1 then d.document_value else d.document_value * -1 end) document_value
	d.id, d.document_id, d.document_parent_id, d.document_parent_id_origin,
    document_type, document_status,
	d.company_id id_empresa, 
    c.social_name empresa,
    d.bank_account_id id_conta_destino, 
    bd.description conta_destino,
    d.bank_account_origin_id id_conta_origem, 
    bo.description conta_origem,
	case when credit = 1 then d.document_value else d.document_value * -1 end valor,
    -- case when document_type = 2 then document_note else document_note end obs,
    d.document_note,
    d.user_change

from 
	document_movement d 
    -- left join bank_account_statement b on 
    -- d.document_id = b.document_id and 
    -- d.document_parent_id = b.document_parent_id
    left join company c on d.company_id = c.id
    left  join bank_account bd on d.bank_account_id = bd.id
    left join bank_account bo on d.bank_account_origin_id = bo.id
where 
	d.document_parent_id_origin = 411297
    and d.company_id = 1
    and d.document_status = 2
    -- and b.id is null
    
    (d.company_id = 1 or document_note like 'DESPESA%' or document_note like 'RESIDUAL%');
    
   --  = Conta Gremista entrou os 6462,79 e debitou as depesas (376, 400 e 1500 = 2276)
    1565
   -- ter no extrato a data que colocou no lancamento nao a data de efetivacao 
    
    select * from bank_account where id in (47,  1565)


select * from document_movement where id in (
529354,
529369,
529372,
529375,
529378,
529381,
529384,
529387
)
select * from bank_account_statement where document_value = -989.0000





/** Atualiza sado de transferencias da conta 47  para 1565 
	-- -2276.0000
    select  sum(case when credit = 1 then document_value else document_value * -1 end) document_value from document_movement x
    where x.id in (529425, 529426, 529427)

    
    select * from bank_account where id = 47
    -- 47	COFRE - COLET.	-143.99
    
    select * from bank_account where id = 1565
    -- 1565	COFRE POA GREMISTA	73826.32
    
    update bank_account set bank_balance_available = bank_balance_available - 2276.0000 where id = 47;
    update bank_account set bank_balance_available = bank_balance_available + 2276.0000 where id = 1565;
    
    
