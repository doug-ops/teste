USE `manager`;
DROP procedure IF EXISTS `pr_manager_login`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_manager_login` (IN access_data_user varchar(100), IN  access_data_password varchar(100))
BEGIN

select x.*, IFNULL(comp.social_name, 'EMPRESA NÃO ENCONTRADO') main_company_description, IFNULL(b.description,'CONTA NÃO ENCONTRADO') main_company_bank_account_description
from
(
	SELECT 
		u.id, u.name, p.email,
        u.expiration_date, 
        u.last_access_date,  
        u.client_id,
        p.access_profile_id, 
        ap.description as description_role, 
        ap.role, 
		apc.cash_closing_max_discount,
        p.main_company_id, 
        IFNULL((select pe.bank_account_id from company co 
	     inner join person pe on co.person_id = pe.id
		 where co.id = p.main_company_id limit 1),0) main_company_bank_account_id
	FROM
		person p
		INNER JOIN user u on p.id=u.person_id
        LEFT JOIN access_profile ap on p.access_profile_id=ap.id
        LEFT JOIN access_profile_config apc on ap.id = apc.access_profile_id
	WHERE
		u.access_data_user=access_data_user
        and u.access_data_password=access_data_password
        and p.inactive=0
        and u.expiration_date >= now()
) x
LEFT JOIN company comp on x.main_company_id=comp.id
LEFT JOIN bank_account b on x.main_company_bank_account_id=b.id;
END$$

