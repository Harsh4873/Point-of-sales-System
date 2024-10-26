-- 1. "52 weeks of sales history" (52_weeks_sales_history.sql)
WITH weekly_orders AS (
    SELECT 
        EXTRACT(WEEK FROM time_stamp) AS week_number,
        time_stamp
    FROM 
        orders
    WHERE 
        time_stamp >= CURRENT_DATE - INTERVAL '52 weeks'
)
SELECT 
    CONCAT('week ', week_number, ' has ', COUNT(*), ' orders') AS sales_summary
FROM 
    weekly_orders
GROUP BY 
    week_number
ORDER BY 
    week_number;
