-- 6. "Top 5 menu items by revenue" (top_5_items_by_revenue.sql)
SELECT CONCAT(
        product_name,
        ' has $',
        TO_CHAR(SUM(quantity * price), 'FM999,999,999.00'),
        ' in revenue'
    ) AS revenue_summary
FROM order_details
GROUP BY product_name
ORDER BY SUM(quantity * price) DESC
LIMIT 5;