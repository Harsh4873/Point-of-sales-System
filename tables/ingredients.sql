CREATE TABLE IF NOT EXISTS ingredients (
    product_name VARCHAR(255) NOT NULL,
    inventory_name VARCHAR(255) NOT NULL,
    inventory_quantity NUMERIC NOT NULL
);

INSERT INTO ingredients (product_name, inventory_name, inventory_quantity) VALUES
--ENTREES (8oz, 16oz, 26oz, 5oz, 6oz, 26oz)

-- BLAZIN BOURBON CHICKEN (ounces)
('S_bourbon_chicken', 'chicken', 6),
('S_bourbon_chicken', 'green_bell_peppers', 0.5),
('S_bourbon_chicken', 'peppers', 0.25),
('S_bourbon_chicken', 'onions', 0.25),
('S_bourbon_chicken', 'bourbon_sauce', 1),
('S_bourbon_chicken', 'S_box', 1),

('M_bourbon_chicken', 'chicken', 11),
('M_bourbon_chicken', 'green_bell_peppers', 1.5),
('M_bourbon_chicken', 'peppers', 0.75),
('M_bourbon_chicken', 'onions', 0.75),
('M_bourbon_chicken', 'bourbon_sauce', 2),
('M_bourbon_chicken', 'M_box', 1),

('L_bourbon_chicken', 'chicken', 18),
('L_bourbon_chicken', 'green_bell_peppers', 2.5),
('L_bourbon_chicken', 'peppers', 1.25),
('L_bourbon_chicken', 'onions', 1.25),
('L_bourbon_chicken', 'bourbon_sauce', 3),
('L_bourbon_chicken', 'L_box', 1),

('K_bourbon_chicken', 'chicken', 3),
('K_bourbon_chicken', 'green_bell_peppers', 0.5),
('K_bourbon_chicken', 'peppers', 0.25),
('K_bourbon_chicken', 'onions', 0.25),
('K_bourbon_chicken', 'bourbon_sauce', 1),
('K_bourbon_chicken', 'K_box', 1),
('K_bourbon_chicken', 'K_apples', 1),

('N_bourbon_chicken', 'chicken', 4),
('N_bourbon_chicken', 'green_bell_peppers', 0.5),
('N_bourbon_chicken', 'peppers', 0.25),
('N_bourbon_chicken', 'onions', 0.25),
('N_bourbon_chicken', 'bourbon_sauce', 1),

('F_bourbon_chicken', 'chicken', 18),
('F_bourbon_chicken', 'green_bell_peppers', 2.5),
('F_bourbon_chicken', 'peppers', 1.25),
('F_bourbon_chicken', 'onions', 1.25),
('F_bourbon_chicken', 'bourbon_sauce', 3),
('F_bourbon_chicken', 'L_box', 1),


-- ORANGE CHICKEN (ounces)
('S_orange_chicken', 'chicken', 6),
('S_orange_chicken', 'orange_sauce', 2),
('S_orange_chicken', 'S_box', 1),

('M_orange_chicken', 'chicken', 13),
('M_orange_chicken', 'orange_sauce', 3),
('M_orange_chicken', 'M_box', 1),

('L_orange_chicken', 'chicken', 21),
('L_orange_chicken', 'orange_sauce', 5),
('L_orange_chicken', 'L_box', 1),

('K_orange_chicken', 'chicken', 4),
('K_orange_chicken', 'orange_sauce', 1),
('K_orange_chicken', 'K_box', 1),
('K_orange_chicken', 'K_apples', 1),

('N_orange_chicken', 'chicken', 5),
('N_orange_chicken', 'orange_sauce', 1),

('F_orange_chicken', 'chicken', 21),
('F_orange_chicken', 'orange_sauce', 5),
('F_orange_chicken', 'L_box', 1),


-- BLACK PEPPER STEAK (ounces)
('S_pepper_steak', 'beef', 5),
('S_pepper_steak', 'broccoli', 0.5),
('S_pepper_steak', 'onions', 0.5),
('S_pepper_steak', 'red_bell_peppers', 0.5),
('S_pepper_steak', 'mushrooms', 0.5),
('S_pepper_steak', 'pepper_sauce', 1),
('S_pepper_steak', 'S_box', 1),

('M_pepper_steak', 'beef', 10),
('M_pepper_steak', 'broccoli', 1),
('M_pepper_steak', 'onions', 1),
('M_pepper_steak', 'red_bell_peppers', 1),
('M_pepper_steak', 'mushrooms', 1),
('M_pepper_steak', 'pepper_sauce', 2),
('M_pepper_steak', 'M_box', 1),

('L_pepper_steak', 'beef', 15),
('L_pepper_steak', 'broccoli', 2),
('L_pepper_steak', 'onions', 2),
('L_pepper_steak', 'red_bell_peppers', 2),
('L_pepper_steak', 'mushrooms', 2),
('L_pepper_steak', 'pepper_sauce', 3),
('L_pepper_steak', 'L_box', 1),

('K_pepper_steak', 'beef', 3),
('K_pepper_steak', 'broccoli', 0.25),
('K_pepper_steak', 'onions', 0.25),
('K_pepper_steak', 'red_bell_peppers', 0.25),
('K_pepper_steak', 'mushrooms', 0.25),
('K_pepper_steak', 'pepper_sauce', 1),
('K_pepper_steak', 'K_box', 1),
('K_pepper_steak', 'K_apples', 1),

('N_pepper_steak', 'beef', 4),
('N_pepper_steak', 'broccoli', 0.25),
('N_pepper_steak', 'onions', 0.25),
('N_pepper_steak', 'red_bell_peppers', 0.25),
('N_pepper_steak', 'mushrooms', 0.25),
('N_pepper_steak', 'pepper_sauce', 1),

('F_pepper_steak', 'beef', 18),
('F_pepper_steak', 'broccoli', 2),
('F_pepper_steak', 'onions', 2),
('F_pepper_steak', 'red_bell_peppers', 2),
('F_pepper_steak', 'mushrooms', 2),
('F_pepper_steak', 'pepper_sauce', 3),
('F_pepper_steak', 'L_box', 1),


-- HONEY WALNUT SHRIMP (ounces)
('S_walnut_shrimp', 'shrimp', 6),
('S_walnut_shrimp', 'walnuts', 1),
('S_walnut_shrimp', 'honey_sauce', 1),
('S_walnut_shrimp', 'S_box', 1),

('M_walnut_shrimp', 'shrimp', 11),
('M_walnut_shrimp', 'walnuts', 3),
('M_walnut_shrimp', 'honey_sauce', 2),
('M_walnut_shrimp', 'M_box', 1),

('L_walnut_shrimp', 'shrimp', 18),
('L_walnut_shrimp', 'walnuts', 5),
('L_walnut_shrimp', 'honey_sauce', 3),
('L_walnut_shrimp', 'L_box', 1),

('K_walnut_shrimp', 'shrimp', 3),
('K_walnut_shrimp', 'walnuts', 1),
('K_walnut_shrimp', 'honey_sauce', 1),
('K_walnut_shrimp', 'K_box', 1),
('K_walnut_shrimp', 'K_apples', 1),

('N_walnut_shrimp', 'shrimp', 4),
('N_walnut_shrimp', 'walnuts', 1),
('N_walnut_shrimp', 'honey_sauce', 1),

('F_walnut_shrimp', 'shrimp', 18),
('F_walnut_shrimp', 'walnuts', 5),
('F_walnut_shrimp', 'honey_sauce', 3),
('F_walnut_shrimp', 'L_box', 1),


-- GRILLED TERIYAKI CHICKEN (ounces)
('S_teriyaki_chicken', 'chicken', 6),
('S_teriyaki_chicken', 'teriyaki_sauce', 2),
('S_teriyaki_chicken', 'S_box', 1),

('M_teriyaki_chicken', 'chicken', 13),
('M_teriyaki_chicken', 'teriyaki_sauce', 3),
('M_teriyaki_chicken', 'M_box', 1),

('L_teriyaki_chicken', 'chicken', 21),
('L_teriyaki_chicken', 'teriyaki_sauce', 5),
('L_teriyaki_chicken', 'L_box', 1),

('K_teriyaki_chicken', 'chicken', 3),
('K_teriyaki_chicken', 'teriyaki_sauce', 1),
('K_teriyaki_chicken', 'K_box', 1),
('K_teriyaki_chicken', 'K_apples', 1),

('N_teriyaki_chicken', 'chicken', 5),
('N_teriyaki_chicken', 'teriyaki_sauce', 1),

('F_teriyaki_chicken', 'chicken', 21),
('F_teriyaki_chicken', 'teriyaki_sauce', 5),
('F_teriyaki_chicken', 'L_box', 1),


-- KUNG PAO CHICKEN (ounces)
('S_kung_pao_chicken', 'chicken', 4),
('S_kung_pao_chicken', 'peppers', 0.5),
('S_kung_pao_chicken', 'red_bell_peppers', 1),
('S_kung_pao_chicken', 'zucchini', 0.5),
('S_kung_pao_chicken', 'peanuts', 1),
('S_kung_pao_chicken', 'spicy_sauce', 1),
('S_kung_pao_chicken', 'S_box', 1),

('M_kung_pao_chicken', 'chicken', 8),
('M_kung_pao_chicken', 'peppers', 0.75),
('M_kung_pao_chicken', 'red_bell_peppers', 1.5),
('M_kung_pao_chicken', 'zucchini', 0.75),
('M_kung_pao_chicken', 'peanuts', 1),
('M_kung_pao_chicken', 'spicy_sauce', 2),
('M_kung_pao_chicken', 'M_box', 1),

('L_kung_pao_chicken', 'chicken', 14),
('L_kung_pao_chicken', 'peppers', 1.5),
('L_kung_pao_chicken', 'red_bell_peppers', 3),
('L_kung_pao_chicken', 'zucchini', 1.5),
('L_kung_pao_chicken', 'peanuts', 3),
('L_kung_pao_chicken', 'spicy_sauce', 3),
('L_kung_pao_chicken', 'L_box', 1),

('K_kung_pao_chicken', 'chicken', 2),
('K_kung_pao_chicken', 'peppers', 0.25),
('K_kung_pao_chicken', 'red_bell_peppers', 0.5),
('K_kung_pao_chicken', 'zucchini', 0.25),
('K_kung_pao_chicken', 'peanuts', 1),
('K_kung_pao_chicken', 'spicy_sauce', 1),
('K_kung_pao_chicken', 'K_box', 1),
('K_kung_pao_chicken', 'K_apples', 1),

('N_kung_pao_chicken', 'chicken', 3),
('N_kung_pao_chicken', 'peppers', 0.25),
('N_kung_pao_chicken', 'red_bell_peppers', 0.5),
('N_kung_pao_chicken', 'zucchini', 0.25),
('N_kung_pao_chicken', 'peanuts', 1),
('N_kung_pao_chicken', 'spicy_sauce', 1),

('F_kung_pao_chicken', 'chicken', 14),
('F_kung_pao_chicken', 'peppers', 1.5),
('F_kung_pao_chicken', 'red_bell_peppers', 3),
('F_kung_pao_chicken', 'zucchini', 1.5),
('F_kung_pao_chicken', 'peanuts', 3),
('F_kung_pao_chicken', 'spicy_sauce', 3),
('F_kung_pao_chicken', 'L_box', 1),


-- HONEY SEASAME CHICKEN (ounces)
('S_sesame_chicken', 'chicken', 6),
('S_sesame_chicken', 'yellow_bell_peppers', 0.5),
('S_sesame_chicken', 'string_beans', 0.5),
('S_sesame_chicken', 'honey_sauce', 1),
('S_sesame_chicken', 'S_box', 1),

('M_sesame_chicken', 'chicken', 11),
('M_sesame_chicken', 'yellow_bell_peppers', 1.5),
('M_sesame_chicken', 'string_beans', 1.5),
('M_sesame_chicken', 'honey_sauce', 2),
('M_sesame_chicken', 'M_box', 1),

('L_sesame_chicken', 'chicken', 18),
('L_sesame_chicken', 'yellow_bell_peppers', 2.5),
('L_sesame_chicken', 'string_beans', 2.5),
('L_sesame_chicken', 'honey_sauce', 3),
('L_sesame_chicken', 'L_box', 1),

('K_sesame_chicken', 'chicken', 3),
('K_sesame_chicken', 'yellow_bell_peppers', 0.5),
('K_sesame_chicken', 'string_beans', 0.5),
('K_sesame_chicken', 'honey_sauce', 1),
('K_sesame_chicken', 'K_box', 1),
('K_sesame_chicken', 'K_apples', 1),

('N_sesame_chicken', 'chicken', 4),
('N_sesame_chicken', 'yellow_bell_peppers', 0.5),
('N_sesame_chicken', 'string_beans', 0.5),
('N_sesame_chicken', 'honey_sauce', 1),

('F_sesame_chicken', 'chicken', 18),
('F_sesame_chicken', 'yellow_bell_peppers', 2.5),
('F_sesame_chicken', 'string_beans', 2.5),
('F_sesame_chicken', 'honey_sauce', 3),
('F_sesame_chicken', 'L_box', 1),


-- BEIJING BEEF (ounces)
('S_beijing_beef', 'beef', 6),
('S_beijing_beef', 'red_bell_peppers', 0.5),
('S_beijing_beef', 'onions', 0.5),
('S_beijing_beef', 'sweet_sauce', 1),
('S_beijing_beef', 'S_box', 1),

('M_beijing_beef', 'beef', 11),
('M_beijing_beef', 'red_bell_peppers', 1.5),
('M_beijing_beef', 'onions', 1.5),
('M_beijing_beef', 'sweet_sauce', 2),
('M_beijing_beef', 'M_box', 1),

('L_beijing_beef', 'beef', 18),
('L_beijing_beef', 'red_bell_peppers', 2.5),
('L_beijing_beef', 'onions', 2.5),
('L_beijing_beef', 'sweet_sauce', 3),
('L_beijing_beef', 'L_box', 1),

('K_beijing_beef', 'beef', 3),
('K_beijing_beef', 'red_bell_peppers', 0.5),
('K_beijing_beef', 'onions', 0.5),
('K_beijing_beef', 'sweet_sauce', 1),
('K_beijing_beef', 'K_box', 1),
('K_beijing_beef', 'K_apples', 1),

('N_beijing_beef', 'beef', 4),
('N_beijing_beef', 'red_bell_peppers', 0.5),
('N_beijing_beef', 'onions', 0.5),
('N_beijing_beef', 'sweet_sauce', 1),

('F_beijing_beef', 'beef', 18),
('F_beijing_beef', 'red_bell_peppers', 2.5),
('F_beijing_beef', 'onions', 2.5),
('F_beijing_beef', 'sweet_sauce', 3),
('F_beijing_beef', 'L_box', 1),


-- MUSHROOM CHICKEN (ounces)
('S_mushroom_chicken', 'chicken', 6),
('S_mushroom_chicken', 'mushrooms', 0.5),
('S_mushroom_chicken', 'zucchini', 0.5),
('S_mushroom_chicken', 'ginger_soy_sauce', 1),
('S_mushroom_chicken', 'S_box', 1),

('M_mushroom_chicken', 'chicken', 11),
('M_mushroom_chicken', 'mushrooms', 1.5),
('M_mushroom_chicken', 'zucchini', 1.5),
('M_mushroom_chicken', 'ginger_soy_sauce', 2),
('M_mushroom_chicken', 'M_box', 1),

('L_mushroom_chicken', 'chicken', 18),
('L_mushroom_chicken', 'mushrooms', 2.5),
('L_mushroom_chicken', 'zucchini', 2.5),
('L_mushroom_chicken', 'ginger_soy_sauce', 3),
('L_mushroom_chicken', 'L_box', 1),

('K_mushroom_chicken', 'chicken', 3),
('K_mushroom_chicken', 'mushrooms', 0.5),
('K_mushroom_chicken', 'zucchini', 0.5),
('K_mushroom_chicken', 'ginger_soy_sauce', 1),
('K_mushroom_chicken', 'K_box', 1),
('K_mushroom_chicken', 'K_apples', 1),

('N_mushroom_chicken', 'chicken', 4),
('N_mushroom_chicken', 'mushrooms', 0.5),
('N_mushroom_chicken', 'zucchini', 0.5),
('N_mushroom_chicken', 'ginger_soy_sauce', 1),

('F_mushroom_chicken', 'chicken', 18),
('F_mushroom_chicken', 'mushrooms', 2.5),
('F_mushroom_chicken', 'zucchini', 2.5),
('F_mushroom_chicken', 'ginger_soy_sauce', 3),
('F_mushroom_chicken', 'L_box', 1),


-- SWEETFIRE CHICKEN (ounces)
('S_sweetfire_chicken', 'chicken', 6),
('S_sweetfire_chicken', 'red_bell_peppers', 0.5),
('S_sweetfire_chicken', 'onions', 0.25),
('S_sweetfire_chicken', 'pineapples', 0.25),
('S_sweetfire_chicken', 'sweet_sauce', 1),
('S_sweetfire_chicken', 'S_box', 1),

('M_sweetfire_chicken', 'chicken', 11),
('M_sweetfire_chicken', 'red_bell_peppers', 1),
('M_sweetfire_chicken', 'onions', 0.5),
('M_sweetfire_chicken', 'pineapples', 0.5),
('M_sweetfire_chicken', 'sweet_sauce', 2),
('M_sweetfire_chicken', 'M_box', 1),

('L_sweetfire_chicken', 'chicken', 18),
('L_sweetfire_chicken', 'red_bell_peppers', 2.5),
('L_sweetfire_chicken', 'onions', 1.25),
('L_sweetfire_chicken', 'pineapples', 1.25),
('L_sweetfire_chicken', 'sweet_sauce', 3),
('L_sweetfire_chicken', 'L_box', 1),

('K_sweetfire_chicken', 'chicken', 3),
('K_sweetfire_chicken', 'red_bell_peppers', 0.5),
('K_sweetfire_chicken', 'onions', 0.25),
('K_sweetfire_chicken', 'pineapples', 0.25),
('K_sweetfire_chicken', 'sweet_sauce', 1),
('K_sweetfire_chicken', 'K_box', 1),
('K_sweetfire_chicken', 'K_apples', 1),

('N_sweetfire_chicken', 'chicken', 4),
('N_sweetfire_chicken', 'red_bell_peppers', 0.5),
('N_sweetfire_chicken', 'onions', 0.25),
('N_sweetfire_chicken', 'pineapples', 0.25),
('N_sweetfire_chicken', 'sweet_sauce', 1),

('F_sweetfire_chicken', 'chicken', 18),
('F_sweetfire_chicken', 'red_bell_peppers', 2.5),
('F_sweetfire_chicken', 'onions', 1.25),
('F_sweetfire_chicken', 'pineapples', 1.25),
('F_sweetfire_chicken', 'sweet_sauce', 3),
('F_sweetfire_chicken', 'L_box', 1),


-- STRING BEAN CHICKEN (ounces)
('S_string_bean_chicken', 'chicken', 6),
('S_string_bean_chicken', 'string_beans', 0.5),
('S_string_bean_chicken', 'onions', 0.5),
('S_string_bean_chicken', 'ginger_soy_sauce', 1),
('S_string_bean_chicken', 'S_box', 1),

('M_string_bean_chicken', 'chicken', 11),
('M_string_bean_chicken', 'string_beans', 1.5),
('M_string_bean_chicken', 'onions', 1.5),
('M_string_bean_chicken', 'ginger_soy_sauce', 2),
('M_string_bean_chicken', 'M_box', 1),

('L_string_bean_chicken', 'chicken', 18),
('L_string_bean_chicken', 'string_beans', 2.5),
('L_string_bean_chicken', 'onions', 2.5),
('L_string_bean_chicken', 'ginger_soy_sauce', 3),
('L_string_bean_chicken', 'L_box', 1),

('K_string_bean_chicken', 'chicken', 3),
('K_string_bean_chicken', 'string_beans', 0.5),
('K_string_bean_chicken', 'onions', 0.5),
('K_string_bean_chicken', 'ginger_soy_sauce', 1),
('K_string_bean_chicken', 'K_box', 1),
('K_string_bean_chicken', 'K_apples', 1),

('N_string_bean_chicken', 'chicken', 4),
('N_string_bean_chicken', 'string_beans', 0.5),
('N_string_bean_chicken', 'onions', 0.5),
('N_string_bean_chicken', 'ginger_soy_sauce', 1),

('F_string_bean_chicken', 'chicken', 18),
('F_string_bean_chicken', 'string_beans', 2.5),
('F_string_bean_chicken', 'onions', 2.5),
('F_string_bean_chicken', 'ginger_soy_sauce', 3),
('F_string_bean_chicken', 'L_box', 1),


-- BROCCOLI BEEF (ounces)
('S_broccoli_beef', 'beef', 6),
('S_broccoli_beef', 'broccoli', 1),
('S_broccoli_beef', 'ginger_soy_sauce', 1),
('S_broccoli_beef', 'S_box', 1),

('M_broccoli_beef', 'beef', 11),
('M_broccoli_beef', 'broccoli', 3),
('M_broccoli_beef', 'ginger_soy_sauce', 2),
('M_broccoli_beef', 'M_box', 1),

('L_broccoli_beef', 'beef', 18),
('L_broccoli_beef', 'broccoli', 5),
('L_broccoli_beef', 'ginger_soy_sauce', 3),
('L_broccoli_beef', 'L_box', 1),

('K_broccoli_beef', 'beef', 3),
('K_broccoli_beef', 'broccoli', 1),
('K_broccoli_beef', 'ginger_soy_sauce', 1),
('K_broccoli_beef', 'K_box', 1),
('K_broccoli_beef', 'K_apples', 1),

('N_broccoli_beef', 'beef', 4),
('N_broccoli_beef', 'broccoli', 1),
('N_broccoli_beef', 'ginger_soy_sauce', 1),

('F_broccoli_beef', 'beef', 18),
('F_broccoli_beef', 'broccoli', 5),
('F_broccoli_beef', 'ginger_soy_sauce', 3),
('F_broccoli_beef', 'L_box', 1),


-- BLACK PEPPER CHICKEN (ounces)
('S_pepper_chicken', 'chicken', 6),
('S_pepper_chicken', 'celery', 0.5),
('S_pepper_chicken', 'onions', 0.5),
('S_pepper_chicken', 'pepper_sauce', 1),
('S_pepper_chicken', 'S_box', 1),

('M_pepper_chicken', 'chicken', 11),
('M_pepper_chicken', 'celery', 1.5),
('M_pepper_chicken', 'onions', 1.5),
('M_pepper_chicken', 'pepper_sauce', 2),
('M_pepper_chicken', 'M_box', 1),

('L_pepper_chicken', 'chicken', 18),
('L_pepper_chicken', 'celery', 2.5),
('L_pepper_chicken', 'onions', 2.5),
('L_pepper_chicken', 'pepper_sauce', 3),
('L_pepper_chicken', 'L_box', 1),

('K_pepper_chicken', 'chicken', 3),
('K_pepper_chicken', 'celery', 0.5),
('K_pepper_chicken', 'onions', 0.5),
('K_pepper_chicken', 'pepper_sauce', 1),
('K_pepper_chicken', 'K_box', 1),
('K_pepper_chicken', 'K_apples', 1),

('N_pepper_chicken', 'chicken', 4),
('N_pepper_chicken', 'celery', 0.5),
('N_pepper_chicken', 'onions', 0.5),
('N_pepper_chicken', 'pepper_sauce', 1),

('F_pepper_chicken', 'chicken', 18),
('F_pepper_chicken', 'celery', 2.5),
('F_pepper_chicken', 'onions', 2.5),
('F_pepper_chicken', 'pepper_sauce', 3),
('F_pepper_chicken', 'L_box', 1),


-- SUPER GREENS ENTREES (ounces)
('M_super_greens_entree', 'broccoli', 10), 
('M_super_greens_entree', 'kale', 3), 
('M_super_greens_entree', 'cabbage', 3), 
('M_super_greens_entree', 'M_box', 1), 

('L_super_greens_entree', 'broccoli', 16), 
('L_super_greens_entree', 'kale', 5), 
('L_super_greens_entree', 'cabbage', 5), 
('L_super_greens_entree', 'L_box', 1), 

('K_super_greens_entree', 'broccoli', 3), 
('K_super_greens_entree', 'kale', 1), 
('K_super_greens_entree', 'cabbage', 1), 
('K_super_greens_entree', 'K_box', 1), 
('K_super_greens_entree', 'K_apples', 1), 

('N_super_greens_entree', 'broccoli', 3), 
('N_super_greens_entree', 'kale', 1.5), 
('N_super_greens_entree', 'cabbage', 1.5), 

('F_super_greens_entree', 'broccoli', 16), 
('F_super_greens_entree', 'kale', 5), 
('F_super_greens_entree', 'cabbage', 5), 
('F_super_greens_entree', 'L_box', 1), 


-- SUPER GREENS SIDES (ounces)
('M_super_greens_entree', 'broccoli', 10), 
('M_super_greens_entree', 'kale', 3), 
('M_super_greens_entree', 'cabbage', 3), 
('M_super_greens_entree', 'M_box', 1), 

('L_super_greens_entree', 'broccoli', 16), 
('L_super_greens_entree', 'kale', 5), 
('L_super_greens_entree', 'cabbage', 5), 
('L_super_greens_entree', 'L_box', 1), 

('K_super_greens_entree', 'broccoli', 5), 
('K_super_greens_entree', 'kale', 1.5), 
('K_super_greens_entree', 'cabbage', 1.5), 

('N_super_greens_entree', 'broccoli', 7), 
('N_super_greens_entree', 'kale', 2), 
('N_super_greens_entree', 'cabbage', 2), 


-- FRIED RICE (ounces)
('M_fried_rice', 'rice', 10),
('M_fried_rice', 'eggs', 1),
('M_fried_rice', 'peas', 1),
('M_fried_rice', 'carrots', 1),
('M_fried_rice', 'green_onions', 1),
('M_fried_rice', 'soy_sauce', 2),
('M_fried_rice', 'M_box', 1),

('L_fried_rice', 'rice', 16),
('L_fried_rice', 'eggs', 1.75),
('L_fried_rice', 'peas', 1.75),
('L_fried_rice', 'carrots', 1.75),
('L_fried_rice', 'green_onions', 1.75),
('L_fried_rice', 'soy_sauce', 3),
('L_fried_rice', 'L_box', 1),

('K_fried_rice', 'rice', 6),
('K_fried_rice', 'eggs', 0.25),
('K_fried_rice', 'peas', 0.25),
('K_fried_rice', 'carrots', 0.25),
('K_fried_rice', 'green_onions', 0.25),
('K_fried_rice', 'soy_sauce', 1),

('N_fried_rice', 'rice', 8),
('N_fried_rice', 'eggs', 0.5),
('N_fried_rice', 'peas', 0.5),
('N_fried_rice', 'carrots', 0.5),
('N_fried_rice', 'green_onions', 0.5),
('N_fried_rice', 'soy_sauce', 1),


-- CHOW MEIN (ounces)
('M_chow_mein', 'noodles', 13),
('M_chow_mein', 'onions', 1),
('M_chow_mein', 'celery', 1),
('M_chow_mein', 'cabbage', 1),
('M_chow_mein', 'M_box', 1),

('L_chow_mein', 'noodles', 17),
('L_chow_mein', 'onions', 3),
('L_chow_mein', 'celery', 3),
('L_chow_mein', 'cabbage', 3),
('L_chow_mein', 'L_box', 1),

('K_chow_mein', 'noodles', 6),
('K_chow_mein', 'onions', 0.66),
('K_chow_mein', 'celery', 0.66),
('K_chow_mein', 'cabbage', 0.66),

('N_chow_mein', 'noodles', 8),
('N_chow_mein', 'onions', 1),
('N_chow_mein', 'celery', 1),
('N_chow_mein', 'cabbage', 1),


-- WHITE RICE (ounces)
('M_white_rice', 'rice', 16),
('M_white_rice', 'M_box', 1),

('L_white_rice', 'rice', 26),
('L_white_rice', 'L_box', 1),

('K_white_rice', 'rice', 8),

('N_white_rice', 'rice', 11),


-- CHICKEN EGG ROLL (ounces, ounces, wrappers)
('S_egg_roll', 'chicken', 1), 
('S_egg_roll', 'cabbage', 0.25), 
('S_egg_roll', 'carrots', 0.25), 
('S_egg_roll', 'green_onions', 0.25), 
('S_egg_roll', 'wonton_wrappers', 1), 
('S_egg_roll', 'waxed_bags', 1), 

('L_egg_roll', 'chicken', 6), 
('L_egg_roll', 'cabbage', 1.5), 
('L_egg_roll', 'carrots', 1.5), 
('L_egg_roll', 'green_onions', 1.5), 
('L_egg_roll', 'wonton_wrappers', 6), 
('L_egg_roll', 'waxed_bags', 2), 


-- VEGGIE SPRING ROLL (ounces, ounces, wrappers)
('S_spring_roll', 'noodles', 1.5), 
('S_spring_roll', 'cabbage', 0.25), 
('S_spring_roll', 'celery', 0.25), 
('S_spring_roll', 'carrots', 0.25), 
('S_spring_roll', 'green_onions', 0.25), 
('S_spring_roll', 'wonton_wrappers', 1), 
('S_spring_roll', 'waxed_bags', 1), 

('L_spring_roll', 'noodles', 9), 
('L_spring_roll', 'cabbage', 1.25), 
('L_spring_roll', 'celery', 1.25), 
('L_spring_roll', 'carrots', 1.25), 
('L_spring_roll', 'green_onions', 1.25), 
('L_spring_roll', 'wonton_wrappers', 6), 
('L_spring_roll', 'waxed_bags', 2), 


-- CREAM CHEESE RANGOON (ounces, ounces, wrappers)
('S_cc_rangoon', 'cc_rangoons', 3), 
('S_cc_rangoon', 'waxed_bags', 1), 

('L_cc_rangoon', 'cc_rangoons', 12), 
('L_cc_rangoon', 'waxed_bags', 2), 


-- APPLE PIE ROLL (apple, ounces, ounces)
('S_applie_pie_roll', 'apples', 0.25), 
('S_applie_pie_roll', 'spices', 0.25), 
('S_applie_pie_roll', 'pastry', 1), 
('S_applie_pie_roll', 'waxed_bags', 1), 

('M_applie_pie_roll', 'apples', 1), 
('M_applie_pie_roll', 'spices', 1), 
('M_applie_pie_roll', 'pastry', 4), 
('M_applie_pie_roll', 'waxed_bags', 1), 

('L_applie_pie_roll', 'apples', 1.5), 
('L_applie_pie_roll', 'spices', 1.5), 
('L_applie_pie_roll', 'pastry', 6), 
('L_applie_pie_roll', 'waxed_bags', 2), 


-- FOUNTAIN DRINKS
('S_fountain_drink', 'small_cup', 1), 
('S_fountain_drink', 'straw', 1), 
('S_fountain_drink', 'small_lid', 1), 

('M_fountain_drink', 'medium_cup', 1), 
('M_fountain_drink', 'straw', 1), 
('M_fountain_drink', 'medium_lid', 1), 

('L_fountain_drink', 'large_cup', 1),
('L_fountain_drink', 'straw', 1),
('L_fountain_drink', 'large_lid', 1),


-- BOTTLED WATER
('bottled_water', 'bottled_water', 1),


--GATORADE
('gatorade', 'gatorade', 1);
