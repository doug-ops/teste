select 
c.social_name EMPRESA,
f.bank_account_origin_id CONTA, 
CASE WHEN IFNULL(f.fixed_day,0) = 1 THEN 'SEMANAL' ELSE 'MENSAL' END EXECUCAO, 
CASE WHEN IFNULL(f.fixed_day,0) = 0 THEN '-'
	 WHEN IFNULL(f.week_day,-1) = -1 THEN 'NAO INFORMADO'
     WHEN IFNULL(f.week_day,-1) = 0 THEN 'SEGUNDA'
     WHEN IFNULL(f.week_day,-1) = 1 THEN 'TERCA'
     WHEN IFNULL(f.week_day,-1) = 2 THEN 'QUARTA'
     WHEN IFNULL(f.week_day,-1) = 3 THEN 'QUINTA'
     WHEN IFNULL(f.week_day,-1) = 4 THEN 'SEXTA'
     WHEN IFNULL(f.week_day,-1) = 5 THEN 'SABADO'
     WHEN IFNULL(f.week_day,-1) = 6 THEN 'DOMINGO' END DIA_SEMANA,
-- IFNULL(f.fixed_day_month,0) mensal, 
IFNULL(f.execution_days,'') DIA_MES 
from 
financial_transfer_group_setting f
inner join person p on f.bank_account_origin_id = p.bank_account_id
inner join company c on p.id = c.person_id
where f.inactive = 0 and ifnull(f.bank_account_destiny_id,0) = 0
ORDER BY c.social_name;