create table tmp_company_remove(company_id bigint not null, person_id bigint, credit_provider_id bigint, debit_provider_id bigint, bank_account_id bigint);

drop table tmp_company_remove;
select * from tmp_company_remove;

-- 1 - Inserir os codigos de loja na tabela tmp_company_remove
-- 2 - Atualizar os codigos person_id, credit_provider_id, debit_provider_id, bank_account_id na tabela tmp_company_remove
SET SQL_SAFE_UPDATES = 0;
update tmp_company_remove cr
inner join
(
select 
	c.id company_id, c.person_id, 
    p.credit_provider_id, p.debit_provider_id, p.bank_account_id
from 
	company c 
    inner join person p on c.person_id = p.id    
    -- where c.id = 65
) x on x.company_id = cr.company_id
set cr.person_id = x.person_id, cr.credit_provider_id = x.credit_provider_id,
cr.debit_provider_id = x.debit_provider_id, cr.bank_account_id = x.bank_account_id;

-- 3 - Deletar provider_integration_system para credit_provider_id
DELETE 
    ps 
FROM 
    provider_integration_system ps INNER JOIN 
         (SELECT 
           po.provider_id 
         FROM 
             provider_integration_system po 
             inner join tmp_company_remove cr on po.provider_id = cr.credit_provider_id
         GROUP BY po.provider_id) dubids on dubids.provider_id  = ps.provider_id;

-- 4 - Deletar provider para credit_provider_id
DELETE 
    ps 
FROM 
    provider ps INNER JOIN 
         (SELECT 
           id 
         FROM 
             provider po 
             inner join tmp_company_remove cr on po.id = cr.credit_provider_id
         GROUP BY id) dubids on dubids.id = ps.id;
 
-- 5 - Deletar provider_integration_system para debit_provider_id
DELETE 
    ps 
FROM 
    provider_integration_system ps INNER JOIN 
         (SELECT 
           po.provider_id 
         FROM 
             provider_integration_system po 
             inner join tmp_company_remove cr on po.provider_id = cr.debit_provider_id
         GROUP BY po.provider_id) dubids on dubids.provider_id  = ps.provider_id;         
  
-- 6 - Deletar provider para debit_provider_id  
DELETE 
    ps 
FROM 
    provider ps INNER JOIN 
         (SELECT 
           id 
         FROM 
             provider po 
             inner join tmp_company_remove cr on po.id = cr.debit_provider_id
         GROUP BY id) dubids on dubids.id = ps.id;
      
-- 7 - Deletar financial_transfer_product_setting para bank_account_id        
DELETE 
    ps 
FROM 
    financial_transfer_product_setting ps 
    INNER JOIN 
         (SELECT 
           group_id, bank_account_origin_id,  bank_account_destiny_id
         FROM 
             financial_transfer_product_setting ba 
             inner join tmp_company_remove cr on ba.bank_account_origin_id = cr.bank_account_id and ba.bank_account_destiny_id = 0
         GROUP BY group_id, bank_account_origin_id,  bank_account_destiny_id) dubids on dubids.group_id = ps.group_id 
         and dubids.bank_account_origin_id = ps.bank_account_origin_id
         and dubids.bank_account_destiny_id = ps.bank_account_destiny_id;
 
-- 8 - Deletar financial_transfer_group_item_setting para bank_account_id   
DELETE 
    ps 
FROM 
    financial_transfer_group_item_setting ps 
    INNER JOIN 
         (SELECT 
           group_id, bank_account_origin_id,  bank_account_destiny_id
         FROM 
             financial_transfer_group_item_setting ba 
             inner join tmp_company_remove cr on ba.bank_account_origin_id = cr.bank_account_id and ba.bank_account_destiny_id = 0
         GROUP BY group_id, bank_account_origin_id,  bank_account_destiny_id) dubids on dubids.group_id = ps.group_id 
         and dubids.bank_account_origin_id = ps.bank_account_origin_id
         and dubids.bank_account_destiny_id = ps.bank_account_destiny_id;

-- 9 - Deletar financial_transfer_group_setting para bank_account_id           
DELETE 
    ps 
FROM 
    financial_transfer_group_setting ps 
    INNER JOIN 
         (SELECT 
           id, bank_account_origin_id,  bank_account_destiny_id
         FROM 
             financial_transfer_group_setting ba 
             inner join tmp_company_remove cr on ba.bank_account_origin_id = cr.bank_account_id and ba.bank_account_destiny_id = 0
         GROUP BY id, bank_account_origin_id,  bank_account_destiny_id) dubids on dubids.id = ps.id 
         and dubids.bank_account_origin_id = ps.bank_account_origin_id
         and dubids.bank_account_destiny_id = ps.bank_account_destiny_id;
    
-- 10 - Deletar bank_account para bank_account_id           
DELETE 
    ps 
FROM 
    bank_account ps INNER JOIN 
         (SELECT 
           id 
         FROM 
             bank_account ba 
             inner join tmp_company_remove cr on ba.id = cr.bank_account_id
         GROUP BY id) dubids on dubids.id = ps.id;
   
-- 11 - Deletar company_integration_system para company_id        
DELETE 
    ps 
FROM 
    company_integration_system ps INNER JOIN 
         (SELECT 
           ca.company_id 
         FROM 
             company_integration_system ca 
             inner join tmp_company_remove cr on ca.company_id = cr.company_id
         GROUP BY company_id) dubids on dubids.company_id = ps.company_id;

-- 12 - Deletar company para company_id           
DELETE 
     ps 
 FROM 
     company ps INNER JOIN 
          (SELECT 
            id 
          FROM 
              company ca 
              inner join tmp_company_remove cr on ca.id = cr.company_id
          GROUP BY id) dubids on dubids.id = ps.id;	
 

-- 12 - Deletar user_company para person_id  
DELETE 
     ps 
 FROM 
     user_company ps INNER JOIN 
          (SELECT 
            ca.id 
          FROM 
              person ca 
              inner join tmp_company_remove cr on ca.id = cr.person_id
          GROUP BY id) dubids on dubids.id = ps.user_id;
          
DELETE 
     ps 
 FROM 
     person_bank_account ps INNER JOIN 
          (SELECT 
            id 
          FROM 
              person ca 
              inner join tmp_company_remove cr on ca.id = cr.person_id
          GROUP BY id) dubids on dubids.id = ps.person_id;
          
 
 -- 13 - Deletar person para person_id  
DELETE 
     ps 
 FROM 
     person ps INNER JOIN 
          (SELECT 
            id 
          FROM 
              person ca 
              inner join tmp_company_remove cr on ca.id = cr.person_id
          GROUP BY id) dubids on dubids.id = ps.id;
 
-- 14 - Deletar document_movement para company_id 
DELETE 
    ps 
FROM 
    document_movement ps INNER JOIN 
         (SELECT 
           ca.company_id
         FROM 
             document_movement ca 
             inner join tmp_company_remove cr on ca.company_id = cr.company_id
         GROUP BY company_id) dubids on dubids.company_id = ps.company_id;
         
-- 15 - Deletar bank_account_statement para bank_account_id          
DELETE 
    ps 
FROM 
    bank_account_statement ps INNER JOIN 
         (SELECT 
           id 
         FROM 
             bank_account_statement ba 
             inner join tmp_company_remove cr on ba.bank_account_id = cr.bank_account_id
         GROUP BY id) dubids on dubids.id = ps.id;