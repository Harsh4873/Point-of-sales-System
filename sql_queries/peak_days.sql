-- 3. "10 peak days" (peak_days.sql)
WITH daily_sales AS (
    SELECT DATE(time_stamp) AS sale_date,
        SUM(total) AS total_sales
    FROM orders
    GROUP BY DATE(time_stamp)
)
SELECT CONCAT(
        TO_CHAR(sale_date, 'DD Month'),
        ' has $',
        TO_CHAR(total_sales, 'FM999,999,999.00'),
        ' of sales'
    ) AS sales_summary
FROM daily_sales
ORDER BY total_sales DESC
LIMIT 10;