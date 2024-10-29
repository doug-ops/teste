USE `manager`;
DROP procedure IF EXISTS `pr_dashboard`;

DELIMITER $$
CREATE DEFINER=`bmaws`@`%` PROCEDURE `pr_dashboard`(IN _operation INT,
                             IN _date_from DATETIME,
                                   IN _date_to DATETIME,
                                                       IN _users_id VARCHAR(5000))
BEGIN
  DECLARE _timezone_database TINYINT DEFAULT 0;
  DECLARE _processing_date_time DATETIME DEFAULT NOW();
  
    SELECT IFNULL(timezone_database, 0) INTO _timezone_database FROM config_systems LIMIT 1;
  SET _processing_date_time = DATE_ADD(_processing_date_time, INTERVAL _timezone_database HOUR);
    
  /**Drop temporary tables*/
  DROP TEMPORARY TABLE IF EXISTS tmp_users_values;
  DROP TEMPORARY TABLE IF EXISTS tmp_users;
    DROP TEMPORARY TABLE IF EXISTS tmp_company;
  /**End Drop temporary tables*/
        
  /**Temporary table to insert tmp_users_values*/
  CREATE TEMPORARY TABLE IF NOT EXISTS tmp_users_values
  (
    txt text
  );
  /**End Temporary table to insert tmp_users_values*/
        
  /**Temporary table to insert tmp_users*/
  CREATE TEMPORARY TABLE IF NOT EXISTS tmp_users
  (
    user_id BIGINT NOT NULL
  );
    /**End Temporary table to insert tmp_users*/
    
    /**Temporary table to insert tmp_company*/
  CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company
  (
    company_id BIGINT NOT NULL
  );
    /**End Temporary table to insert tmp_company*/
    
  INSERT INTO tmp_users_values VALUES(_users_id);
  SET @sql = concat("INSERT INTO tmp_users (user_id) VALUES ('", REPLACE((SELECT group_concat(DISTINCT txt) as data FROM tmp_users_values), ",", "'),('"),"');");
  PREPARE stmt1 FROM @sql;
  EXECUTE stmt1;
    
    insert into tmp_company 
    SELECT company_id FROM user_company uc inner join tmp_users tu on uc.user_id = tu.user_id GROUP BY company_id;
    
  IF(1=_operation) THEN /*MovementGroups*/
    BEGIN
      select 
        x.product_group_id, 
        gr.description description,
        x.product_sub_group_id, 
        sgr.description sub_group_description,
        -- x.product_id, 
        x.total 
      from 
      (
        SELECT 
          po.product_group_id, 
                    po.product_sub_group_id, 
          IFNULL(sum(mo.document_value),0) total
        FROM 
          document_movement mo
          INNER JOIN product po on mo.product_id = po.id
                    INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
          AND mo.is_product_movement = 1
        GROUP BY 
          po.product_group_id, po.product_sub_group_id
      ) x
      left join product_group gr on x.product_group_id = gr.id
            left join product_sub_group sgr on x.product_group_id = sgr.product_group_id and x.product_sub_group_id = sgr.id;
    END;
  ELSEIF(2=_operation) THEN /*MovementSubgroups*/
    BEGIN
      select 
        x.product_group_id, 
        gr.description group_description,
        x.product_sub_group_id, 
        sgr.description description,
        -- x.product_id, 
        x.total 
      from 
      (
        SELECT 
          po.product_group_id, 
                    po.product_sub_group_id, 
          IFNULL(sum(mo.document_value),0) total
        FROM 
          document_movement mo
          INNER JOIN product po on mo.product_id = po.id
                    INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
          AND mo.is_product_movement = 1
        GROUP BY 
          po.product_group_id, po.product_sub_group_id
      ) x
      left join product_group gr on x.product_group_id = gr.id
            left join product_sub_group sgr on x.product_group_id = sgr.product_group_id and x.product_sub_group_id = sgr.id;
        END;
  ELSEIF(3=_operation) THEN /*MovementTopCompany*/
    BEGIN        
      select 
                1 rownum,
        x.company_id id, 
        co.social_name description,
        x.total
      from 
      (
        SELECT 
          mo.company_id, 
          IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
        FROM 
          document_movement mo
          INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
                    
        GROUP BY 
          mo.company_id
      ) x
      left join company co on x.company_id = co.id
      order by x.total;
            
            /*  select 
        y.rownum,
        y.id, 
        y.description,
        y.total
      from 
      ((select 
        ROW_NUMBER() OVER (ORDER BY x.total) rownum,
        x.company_id id, 
        co.social_name description,
        x.total
      from 
      (
        SELECT 
          mo.company_id, 
          IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
        FROM 
          document_movement mo
          INNER JOIN temp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
        GROUP BY 
          mo.company_id 
      ) x
      left join company co on x.company_id = co.id
      order by x.total
      LIMIT 5)

      UNION ALL

      (select 
        6 as rownum,
        999999 as id, 
        'OUTROS' as description,
        sum(z.total) as total
      from 
      (select 
        ROW_NUMBER() OVER (ORDER BY x.total) rownum,
        x.company_id id, 
        co.social_name description,
        x.total
      from 
      (
        SELECT 
          mo.company_id, 
          IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
        FROM 
          document_movement mo
          INNER JOIN temp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
        GROUP BY 
          mo.company_id 
      ) x
      left join company co on x.company_id = co.id
      order by x.total)z
      where z.rownum > 5))y;*/
    END;
  ELSEIF(4=_operation) THEN /*MovementAverageTopProductCompany*/
    BEGIN       
      select 
                1 rownum,
        x.company_id id, 
        co.social_name description,
        x.total
      from 
      (
        SELECT 
          mo.company_id, 
          IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
        FROM 
          document_movement mo
          INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
                    
        GROUP BY 
          mo.company_id
      ) x
      left join company co on x.company_id = co.id
      order by x.total;         
    END;
  ELSEIF(5=_operation) THEN /*MovementAverageTopCompany*/
    BEGIN       
      select 
                1 rownum,
        x.company_id id, 
        co.social_name description,
        x.total
      from 
      (
        SELECT 
          mo.company_id, 
          IFNULL(sum(case when (credit=1 and mo.document_value < 0) then (mo.document_value * -1) when (credit=1 and mo.document_value >= 0) then mo.document_value else (mo.document_value*-1) end),0) total                    
        FROM 
          document_movement mo
          INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
                    
        GROUP BY 
          mo.company_id
      ) x
      left join company co on x.company_id = co.id
      order by x.total;         
    END;
  ELSEIF(6=_operation) THEN /*MovementCompanyExecution*/
    BEGIN
      select 
        x.product_group_id, 
        gr.description group_description,
        x.product_sub_group_id, 
        sgr.description description,
        -- x.product_id, 
        x.total 
      from 
      (
        SELECT 
          po.product_group_id, 
                    po.product_sub_group_id, 
          IFNULL(sum(mo.document_value),0) total
        FROM 
          document_movement mo
          INNER JOIN product po on mo.product_id = po.id
                    INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
          AND mo.is_product_movement = 1
        GROUP BY 
          po.product_group_id, po.product_sub_group_id
      ) x
      left join product_group gr on x.product_group_id = gr.id
            left join product_sub_group sgr on x.product_group_id = sgr.product_group_id and x.product_sub_group_id = sgr.id;
        END;
  ELSEIF(7=_operation) THEN /*MovementProductExecution*/
    BEGIN
      select 
        x.product_group_id, 
        gr.description group_description,
        x.product_sub_group_id, 
        sgr.description description,
        -- x.product_id, 
        x.total 
      from 
      (
        SELECT 
          po.product_group_id, 
                    po.product_sub_group_id, 
          IFNULL(sum(mo.document_value),0) total
        FROM 
          document_movement mo
          INNER JOIN product po on mo.product_id = po.id
                    INNER JOIN tmp_company tc on mo.company_id = tc.company_id
        WHERE 
          mo.payment_data BETWEEN _date_from AND _date_to
          AND mo.is_product_movement = 1
        GROUP BY 
          po.product_group_id, po.product_sub_group_id
      ) x
      left join product_group gr on x.product_group_id = gr.id
            left join product_sub_group sgr on x.product_group_id = sgr.product_group_id and x.product_sub_group_id = sgr.id;
        END;
    END IF;
END$$
DELIMITER ;
