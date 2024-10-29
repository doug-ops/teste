
DROP FUNCTION IF EXISTS func_yearweek_to_date;

DELIMITER $

CREATE FUNCTION 
   func_yearweek_to_date (var_yearweek INTEGER UNSIGNED,
    var_weekday ENUM(
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday',
        'Sunday'
        ))
   RETURNS DATE
   DETERMINISTIC -- always returns same results for same input parameters
    BEGIN
        RETURN STR_TO_DATE(CONCAT(var_yearweek, var_weekday), '%x%v%W');
    END
$
DELIMITER ;
SELECT
    func_yearweek_to_date(202326, 'Monday'),
    func_yearweek_to_date(202326, 'Sunday')
;
