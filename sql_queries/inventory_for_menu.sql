-- 13. Gives count of how many distinct items appear within each entree, side, and extra menu item (inventory_for_menu.sql)
SELECT CONCAT(
        REPLACE(product_name, 'L_', ''),
        ' uses ',
        COUNT(inventory_name),
        ' items'
    ) AS product_inventory_summary
FROM ingredients
WHERE product_name LIKE 'L%'
    AND product_name NOT LIKE '%fountain%'
GROUP BY product_name
ORDER BY COUNT(inventory_name) DESC;
