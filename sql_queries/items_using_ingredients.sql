-- 15. Gives the products that use each ingredient listed in alphabetical order by ingredient (items_using_ingredients.sql)
SELECT inventory_name AS ingredient,
    REPLACE(product_name, 'L_', '') as product
FROM ingredients
WHERE product_name LIKE 'L%'
    AND product_name NOT LIKE '%fountain%'
ORDER BY inventory_name ASC,
    product_name ASC;