select 
cc.cashier_closing_id, cc.cashier_closing_status,  ccc.cashier_closing_status
from 
cashier_closing cc 
inner join cashier_closing_company ccc on cc.cashier_closing_id = ccc.cashier_closing_id
where cc.cashier_closing_status < 2

group by cc.cashier_closing_id, cc.cashier_closing_status, ccc.cashier_closing_status
order by cc.cashier_closing_id;

update cashier_closing cu set cu.cashier_closing_status = 2 where cashier_closing_id in (5, 6, 7, 9, 10, 11);