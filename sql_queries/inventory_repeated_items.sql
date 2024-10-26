-- 14. Gives how many times select inventory items appear in each entree, side and extra (inventory_repeated_items.sql)
SELECT CONCAT(
        inventory_name,
        ' used in ',
        COUNT(DISTINCT product_name),
        ' recipes'
    ) AS inventory_usage_summary
FROM ingredients
WHERE product_name LIKE 'L%'
    AND product_name NOT LIKE '%fountain%'
GROUP BY inventory_name
ORDER BY COUNT(DISTINCT product_name) DESC;