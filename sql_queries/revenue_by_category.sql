-- 9. "Revenue by menu item category" (revenue_by_category.sql)
SELECT 
    category || ' $' || TO_CHAR(total_revenue, 'FM999,999,999.00') AS category_revenue
FROM (
    SELECT 
        CASE 
            WHEN product_name LIKE 'S_%' THEN 'Small:'
            WHEN product_name LIKE 'M_%' THEN 'Medium:'
            WHEN product_name LIKE 'L_%' THEN 'Large:'
            WHEN product_name LIKE 'K_%' THEN 'Kids:'
            WHEN product_name LIKE 'N_%' THEN 'Normal:'
            WHEN product_name LIKE 'F_%' THEN 'Family:'
            ELSE 'Other:'
        END AS category,
        SUM(quantity * price) AS total_revenue
    FROM 
        order_details
    GROUP BY 
        CASE 
            WHEN product_name LIKE 'S_%' THEN 'Small:'
            WHEN product_name LIKE 'M_%' THEN 'Medium:'
            WHEN product_name LIKE 'L_%' THEN 'Large:'
            WHEN product_name LIKE 'K_%' THEN 'Kids:'
            WHEN product_name LIKE 'N_%' THEN 'Normal:'
            WHEN product_name LIKE 'F_%' THEN 'Family:'
            ELSE 'Other:'
        END
) AS category_totals
ORDER BY 
    total_revenue DESC;