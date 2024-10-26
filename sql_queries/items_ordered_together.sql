-- 8. "Items often ordered together" (items_ordered_together.sql)
SELECT 
    CONCAT(
        od1.product_name,
        ' and ',
        od2.product_name,
        ' were ordered together ',
        COUNT(*),
        ' times'
    ) AS items_ordered_together
FROM 
    order_details od1
JOIN 
    order_details od2 ON od1.order_id = od2.order_id AND od1.product_name < od2.product_name
GROUP BY 
    od1.product_name, od2.product_name
ORDER BY 
    COUNT(*) DESC
LIMIT 10;