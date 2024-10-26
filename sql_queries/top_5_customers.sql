-- 11. Top 5 customers by revenue, including order count (top_5_customers.sql)
SELECT 
    CONCAT(
        name,
        ' has ordered ',
        order_count,
        ' times and spent $',
        TO_CHAR(total_spent, 'FM999,999,999.00'),
        ' in total.'
    ) AS top_customers
FROM (
    SELECT 
        name,
        SUM(total) AS total_spent,
        COUNT(*) AS order_count
    FROM 
        orders
    GROUP BY 
        name
    ORDER BY 
        order_count DESC
    LIMIT 5
) AS top_customers;