select * from bravo_room;
SET SQL_SAFE_UPDATES = 0;
delete from company_integration_system where legacy_id = 397

insert into company_integration_system(company_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date)
select 1545, '3', 1250, 0, '1', '2023-09-18 14:32:57', '1', '2023-09-18 14:32:57';

select 
	r.*,
    i.machine_id, i.machine_description, p.id, p.description, p.company_id, c.social_name,
    ci.legacy_id
from 
bravo_room r
inner join bravo_room_item i ON r.room_id = i.room_id
inner join product p ON i.machine_id = p.id
inner join company c ON p.company_id = c.id
left join company_integration_system ci ON c.id = ci.company_id and ci.integration_system_id = 3
where c.id not in (1, 2, 8, 17, 43, 142, 182) and ci.legacy_id is null;

select 
m.company_id, m.company_description
from 
company_movement_execution m 
left join company_integration_system ci ON m.company_id = ci.company_id and ci.integration_system_id = 3
where 
m.week_year = 202338 and ci.company_id is null
order by company_description;


select c.* from  company_integration_system ci 
inner join company c on ci.company_id = c.id
where ci.legacy_id in (1078, 1477);

select ci.legacy_id, count(*) from company_integration_system ci group by ci.legacy_id having count(*) > 1;

select * from bravo_room r where r.room_description like '%VIADUTO%';

DELETE FROM company_integration_system ci 
where ci.legacy_id in (1078, 1477);

SELECT * FROM company_integration_system ci where ci.legacy_id in (1714);

select * from bravo_room r where r.room_description like '%TABACARIA%';