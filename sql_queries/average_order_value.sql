-- QUERY 5
SELECT CONCAT(
    '$ ',
    ROUND(AVG(total), 2),
    ' is the average order price'
)
FROM orders;
