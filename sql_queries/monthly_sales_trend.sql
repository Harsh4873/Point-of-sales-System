-- 7. "Monthly sales trend" (monthly_sales_trend.sql)
SELECT CONCAT(
        TO_CHAR(month_start, 'FMMonth'),
        ' of ',
        TO_CHAR(month_start, 'YYYY'),
        ' has $',
        TO_CHAR(total_sales, 'FM999,999,999.00'),
        ' revenue and ',
        order_count,
        ' orders'
    ) AS monthly_sales_trend
FROM (
        SELECT DATE_TRUNC('month', time_stamp) AS month_start,
            COUNT(*) AS order_count,
            SUM(total) AS total_sales
        FROM orders
        GROUP BY DATE_TRUNC('month', time_stamp)
    ) AS monthly_data
ORDER BY month_start;