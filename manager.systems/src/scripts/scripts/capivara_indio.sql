use manager;
SET SQL_SAFE_UPDATES = 0;
-- sum(case when credit = 1 then document_value else document_value * -1 end) 

-- 39.6600	
-- company_id = 1395														
-- bank_account_id = 1458
select 
*
-- sum(case when credit = 1 then document_value else document_value * -1 end)  
from document_movement where  document_parent_id =  389749;     
	-- 506199	1405954	389749					4	0	1395	219	1458	0			1405954	36.0000	%20.00/F:219/PGTO LOJA %	2	36.0000	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	06.000	06.001	0			752	1	1		0	1	2024-04-22 00:00:00	1	2024-05-07 16:51:01	
	-- 506200	1405955	389749					4	0	1395	247	1458	0			1405955	14.4000	%10.00/F:247/OPER. BANCA IND	2	14.4000	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	06.000	06.002	0			752	1	2		0	1	2024-04-22 00:00:00	1	2024-05-07 16:51:01	
	-- 506201	1405956	389749					4	0	1395	262	1458	0			1405956	71.2800	%55.00/F:262/AREA (CXA)	2	71.2800	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	06.000	06.002	0			752	1	3		0	1	2024-04-22 00:00:00	1	2024-05-07 16:51:01	
	-- 506202	1405952	389749					4	1	1395	5232	1458	0			1405952	841.0000	PROD:57081/EI:241643/EF:242484	2	841.0000	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	01.000	01.001	1	57081	CARLA RC	752	1	4		0	1	2024-04-18 00:00:00	1	2024-05-07 16:51:01	
	-- 506203	1405953	389749					4	0	1395	5233	1458	0			1405953	661.0000	PROD:57081/SI:162317/SF:162978	2	661.0000	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	07.000	07.001	1	57081	CARLA RC	752	1	4		0	1	2024-04-18 00:00:00	1	2024-05-07 16:51:01	
	-- 506204	1405957	389749					4	0	1395	273	1458	0			1405957	18.6600	%32.00/F:273/OPER IND CV + IDT (JAP/AGR)	2	18.6600	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	24.000	24.003	0			752	1	4		0	1	2024-04-22 00:00:00	1	2024-05-07 16:51:01	                                                                                             
    
-- delete from document_movement where  document_parent_id =  389749 and company_id = 1395 and bank_account_id = 1458;

-- select * from bank_account_statement where document_parent_id =  389749 and bank_account_id = 1458
	-- 504284	1458	0	4	219	5289	389749	1405954	-36.00	%20.00/F:219/PGTO LOJA %	-36.00	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
	-- 504285	1458	0	4	247	5289	389749	1405955	-14.40	%10.00/F:247/OPER. BANCA IND	-50.40	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
	-- 504286	1458	0	4	262	5289	389749	1405956	-71.28	%55.00/F:262/AREA (CXA)	-121.68	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
	-- 504287	1458	0	4	5232	5289	389749	1405952	841.00	PROD:57081/EI:241643/EF:242484	719.32	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
	-- 504288	1458	0	4	5233	5289	389749	1405953	-661.00	PROD:57081/SI:162317/SF:162978	58.32	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
	-- 504289	1458	0	4	273	5289	389749	1405957	-18.66	%32.00/F:273/OPER IND CV + IDT (JAP/AGR)	39.66	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
    
    -- delete from bank_account_statement where document_parent_id =  389749 and bank_account_id = 1458
    
    -- select * from document_movement where  document_parent_id =  389750; 
	-- 506205	1405958	389750					2	0	1395	5233	1458	382			1405958	39.6600	TRANSF. MOVIMENTO:389749 >> EMPRESA: INDIO >> EMPRESA:  OPER. RJ (AGR/JAP)	2	39.6600	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	22.000	22.001	0							0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01	    
    -- delete from document_movement where  document_parent_id =  389750 and company_id = 1395 and bank_account_id = 1458;
    
    -- select * from bank_account_statement where document_parent_id =  389750 and bank_account_id = 1458
	-- 504290	1458	382	2	5233	5289	389750	1405958	-39.66	TRANSF. MOVIMENTO:389749 >> EMPRESA: INDIO >> EMPRESA:  OPER. RJ (AGR/JAP)	0.00	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
    -- delete from bank_account_statement where document_parent_id =  389750 and bank_account_id = 1458
    
    select * from document_movement where  document_parent_id =  389751; 
    -- 506206	1405959	389751					2	1	298	2538	382	1458			1405959	39.6600	TRANSF. MOVIMENTO:389749 >> EMPRESA: INDIO >> EMPRESA:  OPER. RJ (AGR/JAP)	2	39.6600	0.0000	0.0000	0	2024-05-07 16:51:01	2024-05-07 16:49:52	1		0	0	0	01.000	01.001	0							0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01	
    -- delete from document_movement where  document_parent_id =  389751  and bank_account_id = 382;
    select * from bank_account_statement where document_parent_id =  389751 and bank_account_id = 382
	-- 504291	382	1458	2	5233	3240	389751	1405959	39.66	TRANSF. MOVIMENTO:389749 >> EMPRESA: INDIO >> EMPRESA:  OPER. RJ (AGR/JAP)	39.66	0	1	2024-05-07 16:51:01	1	2024-05-07 16:51:01
    -- delete from bank_account_statement where document_parent_id =  389751 and bank_account_id = 382
    
    -- 33280.26
    -- select * from bank_account where id = 382;
    delete from document_movement where  document_parent_id =  389758 and company_id = 1395 and bank_account_id = 1458;
    -- update bank_account set bank_balance_available =  bank_balance_available - 39.66 where id = 382;
    
    -- delete from bank_account_statement where document_parent_id =  389758 and bank_account_id = 1458
    