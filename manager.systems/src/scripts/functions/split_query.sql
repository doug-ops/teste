select 
	a.val
FROM
(
	SELECT
	  distinct SUBSTRING_INDEX(SUBSTRING_INDEX(vals, ',', n.digit+1), ',', -1) val
	FROM
	  (select "1,2,3,4,5" as vals) tt1
  INNER JOIN
	(SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3  UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) n
  ON LENGTH(REPLACE(vals, ',' , '')) <= LENGTH(vals)-n.digit 
  ORDER BY 1
) a, (SELECT @rownum:=0)b;