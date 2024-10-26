-- 10. "Busiest hour of the year" (busiest_hour.sql)
WITH time_blocks AS (
    SELECT 
        DATE_TRUNC('day', time_stamp) AS date,
        CASE 
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 0 AND 3 THEN '12 AM - 4 AM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 4 AND 7 THEN '4 AM - 8 AM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 8 AND 11 THEN '8 AM - 12 PM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 12 AND 15 THEN '12 PM - 4 PM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 16 AND 19 THEN '4 PM - 8 PM'
            ELSE '8 PM - 12 AM'
        END AS time_block,
        COUNT(*) AS order_count
    FROM 
        orders
    GROUP BY 
        DATE_TRUNC('day', time_stamp),
        CASE 
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 0 AND 3 THEN '12 AM - 4 AM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 4 AND 7 THEN '4 AM - 8 AM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 8 AND 11 THEN '8 AM - 12 PM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 12 AND 15 THEN '12 PM - 4 PM'
            WHEN EXTRACT(HOUR FROM time_stamp) BETWEEN 16 AND 19 THEN '4 PM - 8 PM'
            ELSE '8 PM - 12 AM'
        END
)
SELECT 
    CONCAT(
        TO_CHAR(date, 'Month DD, YYYY'), 
        ' from ', 
        time_block, 
        ' was the busiest with ', 
        order_count, 
        ' orders'
    ) AS busiest_time_block
FROM 
    time_blocks
ORDER BY 
    order_count DESC
LIMIT 1;