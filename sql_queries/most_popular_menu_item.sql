-- 4. "Top 10 Most popular Menu Items" (most_popular_item.sql)
WITH product_totals AS (
    SELECT product_name,
        SUM(quantity) AS total_quantity
    FROM order_details
    GROUP BY product_name
)
SELECT CONCAT(product_name, ' has ', total_quantity, ' orders') AS product_summary
FROM product_totals
ORDER BY total_quantity DESC
LIMIT 10;