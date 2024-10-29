USE `manager`;
DROP PROCEDURE IF EXISTS `pr_cash_statement_report`;                                                                                                

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_cash_statement_report`(IN _companys_ids VARCHAR(500),
											IN _date_from DATETIME, 
											IN _date_to DATETIME,
											IN _document_type_ids VARCHAR(500),
											IN _user_childrens_parent VARCHAR(5000))
BEGIN    

	DECLARE INITIAL_WEEK_YEAR_BEFORE int;
	DECLARE INITIAL_WEEK_YEAR int;
    DECLARE FINAL_WEEK_YEAR int;
    
    SELECT YEARWEEK(DATE_ADD(_date_from , INTERVAL -7 DAY)), YEARWEEK(_date_from), YEARWEEK(_date_to) INTO INITIAL_WEEK_YEAR_BEFORE, INITIAL_WEEK_YEAR, FINAL_WEEK_YEAR;

	/**Drop temporary tables*/
	 DROP TEMPORARY TABLE IF EXISTS tmp_cash_movement;
     DROP TEMPORARY TABLE IF EXISTS tmp_company_filter;
     DROP TEMPORARY TABLE IF EXISTS tmp_user_filter;
	/**End Drop temporary tables*/

	/**Temporary table to insert tmp_cash_movement*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_cash_movement
	(
        movement_type tinyint not null, 
		week_year int not null,
        payment_data datetime not null,
        cashier_closing_date datetime null,
        creation_date datetime not null,
        change_date datetime not null,
        document_parent_id bigint not null,
        document_id bigint not null,
        document_type tinyint not null,
        credit bit not null,
        company_id bigint not null,
        provider_id bigint not null,
        bank_account_id bigint not null, 
        document_transfer_id bigint not null, 
        is_document_transfer bit not null,
        document_value decimal not null,
        payment_residue bit not null,
        document_status tinyint not null
	);
	/**End Temporary table to insert tmp_cash_movement*/
    
    /**Temporary table to insert tmp_company_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_company_filter*/
    
	/**Temporary table to insert tmp_user_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_user_filter
	(
		user_id BIGINT NOT NULL
	);
	/**End Temporary table to insert tmp_user_filter*/

    IF(_user_childrens_parent is not null) THEN
		BEGIN
			SET @sql = concat("INSERT INTO tmp_user_filter(user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT x.txt) as data FROM (select _user_childrens_parent txt) x), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
        END;
	END IF;
    
	IF(_companys_ids is not null) THEN
		BEGIN
			SET @sql = concat("INSERT INTO tmp_company_filter(company_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT x.txt) as data FROM (select _companys_ids txt) x), ",", "'),('"),"');");
			PREPARE stmt1 FROM @sql;
			EXECUTE stmt1;
        END;
	END IF;
    
--    select * from document_movement dm 
--    INNER JOIN (select company_id from user_company where user_id in (8) group by company_id) uc ON uc.company_id = dm.company_id
--    where creation_date payment_residue = 1
    
    insert into tmp_cash_movement(movement_type, week_year, payment_data, cashier_closing_date, creation_date, change_date, document_parent_id, document_id, document_type,
	credit, company_id, provider_id, bank_account_id, document_transfer_id, is_document_transfer, document_value, payment_residue, document_status)
    select 
		1 movement_type, YEARWEEK(dm.payment_data) week_year, dm.payment_data, dm.cashier_closing_date, dm.creation_date, dm.change_date, dm.document_parent_id, dm.document_id, dm.document_type, 
        IFNULL(dm.credit,0) credit, dm.company_id, dm.provider_id, dm.bank_account_id, IFNULL(dm.document_transfer_id,0) document_transfer_id, IFNULL(dm.is_document_transfer,0) is_document_transfer, 
        dm.document_value, IFNULL(dm.payment_residue,0) payment_residue, dm.document_status 
	from 
		document_movement dm 
       INNER JOIN (select uci.company_id from user_company uci where uci.user_id in (select uf.user_id from tmp_user_filter uf group by uf.user_id) group by uci.company_id) uc ON uc.company_id = dm.company_id
	where 		
		dm.payment_data between DATE_ADD(_date_from , INTERVAL -14 DAY) and _date_to 
        and IFNULL(dm.payment_residue,0) = 1 AND IFNULL(dm.document_transfer_id,0) > 0 AND dm.inactive = 0
        and dm.inactive = 0;
        
	insert into tmp_cash_movement(movement_type, week_year, payment_data, cashier_closing_date, creation_date, change_date, document_parent_id, document_id, document_type,
	credit, company_id, provider_id, bank_account_id, document_transfer_id, is_document_transfer, document_value, payment_residue, document_status)
    select 
		2 movement_type, YEARWEEK(dm.payment_data) week_year, dm.payment_data, dm.cashier_closing_date, dm.creation_date, dm.change_date, dm.document_parent_id, dm.document_id, dm.document_type, 
        IFNULL(dm.credit,0) credit, dm.company_id, dm.provider_id, dm.bank_account_id, IFNULL(dm.document_transfer_id,0) document_transfer_id, IFNULL(dm.is_document_transfer,0) is_document_transfer, 
        dm.document_value, IFNULL(dm.payment_residue,0) payment_residue, dm.document_status 
	from 
		document_movement dm 
		INNER JOIN tmp_company_filter cf ON cf.company_id = dm.company_id
	where 
		dm.payment_data between DATE_ADD(_date_from , INTERVAL -14 DAY) and _date_to
        and IFNULL(dm.payment_residue,0) = 0 AND IFNULL(dm.document_transfer_id,0) = 0
        and dm.inactive = 0;
   
select * from tmp_cash_movement;        
    
END$$

CALL pr_cash_statement_report('182', '2023-06-01 00:00:00', '2023-06-30 23:59:59','1,2,3,4','8');