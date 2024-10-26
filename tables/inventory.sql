CREATE TABLE IF NOT EXISTS inventory (
    inventory_name VARCHAR(255) NOT NULL,
    inventory_type VARCHAR(255) NOT NULL,
    quantity NUMERIC NOT NULL,
    recommended_quantity NUMERIC NOT NULL,
    gameday_quantity NUMERIC NOT NULL,
    batch_quantity NUMERIC NOT NULL
);

INSERT INTO inventory (inventory_name, inventory_type, quantity, recommended_quantity, gameday_quantity, batch_quantity) VALUES

-- PROTIEN (pounds)
('chicken', 'protein', 75, 50, 100, 4),
('beef', 'protein', 50, 30, 75, 4),
('shrimp', 'protein', 30, 20, 40, 3),

-- SIDES (pounds)
('rice', 'sides', 150, 100, 200, 10),
('noodles', 'sides', 150, 100, 200, 10),

-- SAUCES (gallons)
('bourbon_sauce', 'sauces', 5, 3, 6, 0.08),
('orange_sauce', 'sauces', 8, 5, 10, 0.08),
('pepper_sauce', 'sauces', 5, 3, 6, 0.08),
('teriyaki_sauce', 'sauces', 6, 4, 8, 0.08),
('honey_sauce', 'sauces', 4, 2, 4, 0.08),
('spicy_sauce', 'sauces', 5, 3, 6, 0.08),
('sweet_sauce', 'sauces', 5, 3, 6, 0.08),
('ginger_soy_sauce', 'sauces', 4, 2, 4, 0.08),
('soy_sauce', 'sauces', 6, 4, 8, 0.08),

--- PRODUCE (pounds)
('green_bell_peppers', 'produce', 20, 15, 30, 1),
('red_bell_peppers', 'produce', 20, 15, 30, 1),
('yellow_bell_peppers', 'produce', 15, 10, 20, 1),
('string_beans', 'produce', 25, 20, 40, 1),
('peppers', 'produce', 15, 10, 20, 1),
('onions', 'produce', 30, 20, 40, 1),
('broccoli', 'produce', 40, 30, 60, 1),
('mushrooms', 'produce', 20, 15, 30, 1),
('zucchini', 'produce', 15, 10, 20, 1),
('celery', 'produce', 10, 5, 10, 0.5),
('kale', 'produce', 10, 5, 10, 0.5),
('cabbage', 'produce', 25, 20, 40, 1),
('peas', 'produce', 15, 10, 20, 1),
('carrots', 'produce', 25, 20, 40, 1),
('green_onions', 'produce', 10, 5, 10, 0.5),
('apples', 'produce', 20, 15, 30, 1),
('pineapples', 'produce', 15, 10, 20, 1),
('eggs', 'produce', 20, 15, 30, 1),

-- OTHER INGREDIENTS (pounds)
('walnuts', 'other', 10, 5, 10, 1),
('peanuts', 'other', 15, 10, 20, 1),
('spices', 'other', 5, 3, 6, 0.5),

-- CONSUMABLE (units)
('cc_rangoons', 'consumables', 200, 150, 300, 20),
('wonton_wrappers', 'consumables', 500, 300, 600, 30),
('pastry', 'consumables', 100, 75, 150, 15),
('fortune_cookies', 'consumables', 1000, 750, 1500, 1),
('K_apples', 'consumables', 75, 50, 100, 1),
('soy_sauce_packet', 'consumables', 75, 50, 100, 1),
('hot_sauce_packet', 'consumables', 75, 50, 100, 1),
('hot_mustard_packet', 'consumables', 75, 50, 100, 1),
('teriyaki_packet', 'consumables', 75, 50, 100, 1),
('sweet_sour_packet', 'consumables', 75, 50, 100, 1),

-- SUPPLIES (units)
('small_cup', 'supplies', 500, 300, 600, 1),
('small_lid', 'supplies', 500, 300, 600, 1),
('medium_cup', 'supplies', 750, 500, 1000, 1),
('medium_lid', 'supplies', 750, 500, 1000, 1),
('large_cup', 'supplies', 500, 300, 600, 1),
('large_lid', 'supplies', 500, 300, 600, 1),
('straw', 'supplies', 2000, 1500, 3000, 1),
('napkins', 'supplies', 3000, 2000, 4000, 1),
('flatware', 'supplies', 1000, 750, 1500, 1),
('bowls', 'supplies', 1000, 750, 1500, 1),
('bowl_lids', 'supplies', 1000, 750, 1500, 1),
('plates', 'supplies', 500, 300, 600, 1),
('S_box', 'supplies', 500, 300, 600, 1),
('M_box', 'supplies', 750, 500, 1000, 1),
('L_box', 'supplies', 500, 300, 600, 1),
('K_box', 'supplies', 250, 150, 300, 1),
('waxed_bags', 'supplies', 1000, 750, 1500, 1),
('bags', 'supplies', 1000, 750, 1500, 1),

--  DRNKS (units)
('bottled_water', 'drinks', 200, 150, 300, 1),
('gatorade', 'drinks', 100, 75, 150, 1);
