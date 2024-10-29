USE `manager`;
DROP procedure IF EXISTS `pr_dashboard`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_dashboard`(IN _operation INT)
BEGIN	
	IF(1=_operation) THEN
		BEGIN
			select 
				x.company_id, x.company_description, x.execution_period, 
				DATE_FORMAT(y.reading_date,'%d/%m/%Y') as reading_date, y.is_offline, 1 count_products
			from 
			(
			select 
				c.id as company_id, max(c.social_name) as company_description, ifnull(f.execution_period,15) execution_period
			from 
				financial_transfer_group_setting f
				inner join person p on f.bank_account_origin_id=p.bank_account_id
				inner join company c on p.id=c.person_id
			group by c.id, f.execution_period
			) x
			inner join 
			(
			select
				pm.company_id,
				max(pm.reading_date) as reading_date,
				max(pm.is_offline) as is_offline
			from product_movement pm
			where IFNULL(pm.processing,0)=1
			group by pm.company_id
			) y on x.company_id=y.company_id
			where  date_add(y.reading_date, INTERVAL x.execution_period DAY)<now();
        /**
			select 
				x.company_description,
				x.reading_date, 
				x.is_offline, 
				count(*) as count_products
			from
			(
				select 
				company_description, 
				DATE_FORMAT(reading_date,'%d/%m/%Y') as reading_date,
				is_offline
				from 
				product_movement
				where IFNULL(processing,0)=0
			) x
			group by 
				x.company_description, 
				x.reading_date, 
				x.is_offline;
                */
		END;
    END IF;
END$$

CALL pr_dashboard
(
	1 -- 01 - IN _operation INT
);