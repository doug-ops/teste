use manager;
select c.* from person p 
inner join company c on p.id = c.person_id
where p.person_type_id = 1;

select * from person_type

	SELECT c.id FROM company c
	INNER JOIN (
				SELECT company_id FROM user_company WHERE user_id IN 
                (
					SELECT x.id FROM
					(
						SELECT u.id FROM user_parent up 
						INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = CASE WHEN 7 = 7 THEN 27 ELSE 7 END
						and u.client_id = 2 
						WHERE up.inactive = 0 GROUP BY u.id
                        UNION ALL 
                        SELECT CASE WHEN 7 = 7 THEN 27 ELSE 7 END id
					) x
					GROUP BY id                       
				) GROUP BY company_id
			) uc ON c.id = uc.company_id
             INNER JOIN person p ON c.person_id = p.id AND p.inactive = 0
             WHERE p.person_type_id = 1
             GROUP BY c.id;
             
             update cashier_closing set cashier_closing_status = 2 where cashier_closing_id = 15
             select * from cashier_closing where cashier_closing_status < 2;
             
             select * from cashier_closing_company c where c.cashier_closing_id = 16 and c.cashier_closing_status < 2
             
             select ci.social_name, pe.person_type_id, c.* from cashier_closing_company c 
             INNER JOIN company ci ON c.company_id = ci.id
             INNER JOIN person pe ON ci.person_id = pe.id
             left join 
             (
             SELECT c.id company_id FROM company c
	INNER JOIN (
				SELECT company_id FROM user_company WHERE user_id IN 
                (
					SELECT x.id FROM
					(
						SELECT u.id FROM user_parent up 
						INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = CASE WHEN 8 = 7 THEN 27 ELSE 8 END
						and u.client_id = 1 
						WHERE up.inactive = 0 GROUP BY u.id
                        UNION ALL 
                        SELECT CASE WHEN 8 = 7 THEN 27 ELSE 8 END id
					) x
					GROUP BY id                       
				) GROUP BY company_id
			) uc ON c.id = uc.company_id
             INNER JOIN person p ON c.person_id = p.id AND p.inactive = 0
             WHERE p.person_type_id = 1
             GROUP BY c.id
             ) x on c.company_id = x.company_id
             
             where c.cashier_closing_id = 18
             AND movement_value = 0 and pending_movement_value = 0 and discount_total = 0 and payment_total = 0 and pending_movement_after_value = 0
             and x.company_id is null
             
             
             delete from cashier_closing_company where 
             cashier_closing_id = 18
             AND movement_value = 0 and pending_movement_value = 0 and discount_total = 0 and payment_total = 0 and pending_movement_after_value = 0
             and company_id in (
9
,22
,23
,24
,25
,26
,27
,142
,164
,176
,182
,273
,278
,279
,282
,283
,294
,351
,451
,452
,496
,668
,669
,1019
,1418);