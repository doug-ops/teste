update bank_account_statement up
inner join 
(
select 
doc.document_parent_id, doc.document_id, doc.payment_data,
-- st.document_parent_id, st.document_id, 
st.change_date 
from 
document_movement doc 
left join bank_account_statement st on doc.document_parent_id = st.document_parent_id and doc.document_id = st.document_id
where doc.payment_data  between '2023-07-01 00:00:00' and '2023-07-28 00:00:00'
and date(doc.payment_data) <> date(st.change_date)
) x on up.document_parent_id = x.document_parent_id and up.document_id = x.document_id
set up.change_date = x.payment_data;

SET SQL_SAFE_UPDATES = 0;