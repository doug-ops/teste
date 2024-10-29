
delete ucd 
from 
user_company ucd
inner join (
select us.id user_id, us.client_id, co.id company_id, co.social_name, co.client_id client_id_co from user us 
inner join user_company uc on us.id = uc.user_id
inner join company co on uc.company_id = co.id 
where us.client_id <> co.client_id
) x on ucd.user_id = x.user_id and ucd.company_id = x.company_id