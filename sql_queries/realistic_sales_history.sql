-- 2. "Realistic sales history" (realistic_sales_history.sql)
WITH hourly_sales AS (
    SELECT CASE
            WHEN EXTRACT(HOUR FROM time_stamp) = 0 THEN '12 AM'
            WHEN EXTRACT(HOUR FROM time_stamp) < 12 THEN CONCAT(EXTRACT(HOUR FROM time_stamp),' AM')
            WHEN EXTRACT(HOUR FROM time_stamp) = 12 THEN '12 PM'
            ELSE CONCAT(EXTRACT(HOUR FROM time_stamp) - 12, ' PM')
        END AS hour_category,
        total
    FROM orders
)
SELECT CONCAT(
        hour_category,
        ' has ',
        COUNT(*)::text,
        ' orders totaling $',
        TO_CHAR(SUM(total), 'FM999,999,999.00')
    ) AS sales_summary
FROM hourly_sales
GROUP BY hour_category
ORDER BY SUM(total) DESC;