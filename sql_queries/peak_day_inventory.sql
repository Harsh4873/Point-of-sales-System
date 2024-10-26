-- 12. 10 Most Popular Items on Peak Day from order_details to help decide inventory for game day (peak_day_inventory.sql)
WITH peak_day AS (
    SELECT DATE(time_stamp) AS order_date
    FROM orders
    GROUP BY DATE(time_stamp)
    ORDER BY COUNT(*) DESC
    LIMIT 1
),
popular_items AS (
    SELECT 
        od.product_name,
        SUM(od.quantity) AS total_quantity
    FROM 
        order_details od
    JOIN 
        orders o ON od.order_id = o.order_id
    JOIN 
        peak_day pd ON DATE(o.time_stamp) = pd.order_date
    GROUP BY 
        od.product_name
    ORDER BY 
        total_quantity DESC
    LIMIT 10
)
SELECT 
    CONCAT(
        product_name,
        ' was ordered ',
        total_quantity,
        ' times on the peak day'
    ) AS popular_item_summary
FROM 
    popular_items
ORDER BY 
    total_quantity DESC;