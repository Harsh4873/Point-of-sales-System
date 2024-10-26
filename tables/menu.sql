CREATE TABLE IF NOT EXISTS menu (
    product_name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    type VARCHAR(255) NOT NULL
);

INSERT INTO menu (product_name, price, type) VALUES
-- BLAZIN BOURBON CHICKEN
('S_bourbon_chicken', 5.20, 'entree'),
('M_bourbon_chicken', 8.50, 'entree'),
('L_bourbon_chicken', 11.20, 'entree'),
('K_bourbon_chicken', 0, 'entree'),
('N_bourbon_chicken', 0, 'entree'),
('F_bourbon_chicken', 0, 'entree'),

-- ORANGE CHICKEN
('S_orange_chicken', 5.20, 'entree'),
('M_orange_chicken', 8.50, 'entree'),
('L_orange_chicken', 11.20, 'entree'),
('K_orange_chicken', 0, 'entree'),
('N_orange_chicken', 0, 'entree'),
('F_orange_chicken', 0, 'entree'),

-- BLACK PEPPER STEAK
('S_pepper_steak', 6.70, 'entree'),
('M_pepper_steak', 11.50, 'entree'),
('L_pepper_steak', 15.70, 'entree'),
('K_pepper_steak', 1.00, 'entree'),
('N_pepper_steak', 1.50, 'entree'),
('F_pepper_steak', 4.50, 'entree'),

-- HONEY WALNUT SHRIMP
('S_walnut_shrimp', 6.70, 'entree'),
('M_walnut_shrimp', 11.50, 'entree'),
('L_walnut_shrimp', 15.70, 'entree'),
('K_walnut_shrimp', 1.00, 'entree'),
('N_walnut_shrimp', 1.50, 'entree'),
('F_walnut_shrimp', 4.50, 'entree'),

-- GRILLED TERIYAKI CHICKEN
('S_teriyaki_chicken', 5.20, 'entree'),
('M_teriyaki_chicken', 8.50, 'entree'),
('L_teriyaki_chicken', 11.20, 'entree'),
('K_teriyaki_chicken', 0, 'entree'),
('N_teriyaki_chicken', 0, 'entree'),
('F_teriyaki_chicken', 0, 'entree'),

-- KUNG PAO CHICKEN
('S_kung_pao_chicken', 5.20, 'entree'),
('M_kung_pao_chicken', 8.50, 'entree'),
('L_kung_pao_chicken', 11.20, 'entree'),
('K_kung_pao_chicken', 0, 'entree'),
('N_kung_pao_chicken', 0, 'entree'),
('F_kung_pao_chicken', 0, 'entree'),

-- HONEY SESAME CHICKEN
('S_sesame_chicken', 5.20, 'entree'),
('M_sesame_chicken', 8.50, 'entree'),
('L_sesame_chicken', 11.20, 'entree'),
('K_sesame_chicken', 0, 'entree'),
('N_sesame_chicken', 0, 'entree'),
('F_sesame_chicken', 0, 'entree'),

-- BEIJING BEEF
('S_beijing_beef', 5.20, 'entree'),
('M_beijing_beef', 8.50, 'entree'),
('L_beijing_beef', 11.20, 'entree'),
('K_beijing_beef', 0, 'entree'),
('N_beijing_beef', 0, 'entree'),
('F_beijing_beef', 0, 'entree'),

-- MUSHROOM CHICKEN
('S_mushroom_chicken', 5.20, 'entree'),
('M_mushroom_chicken', 8.50, 'entree'),
('L_mushroom_chicken', 11.20, 'entree'),
('K_mushroom_chicken', 0, 'entree'),
('N_mushroom_chicken', 0, 'entree'),
('F_mushroom_chicken', 0, 'entree'),

-- SWEETFIRE CHICKEN
('S_sweetfire_chicken', 5.20, 'entree'),
('M_sweetfire_chicken', 8.50, 'entree'),
('L_sweetfire_chicken', 11.20, 'entree'),
('K_sweetfire_chicken', 0, 'entree'),
('N_sweetfire_chicken', 0, 'entree'),
('F_sweetfire_chicken', 0, 'entree'),

-- STRING BEAN CHICKEN
('S_string_bean_chicken', 5.20, 'entree'),
('M_string_bean_chicken', 8.50, 'entree'),
('L_string_bean_chicken', 11.20, 'entree'),
('K_string_bean_chicken', 0, 'entree'),
('N_string_bean_chicken', 0, 'entree'),
('F_string_bean_chicken', 0, 'entree'),

-- BROCCOLI BEEF
('S_broccoli_beef', 5.20, 'entree'),
('M_broccoli_beef', 8.50, 'entree'),
('L_broccoli_beef', 11.20, 'entree'),
('K_broccoli_beef', 0, 'entree'),
('N_broccoli_beef', 0, 'entree'),
('F_broccoli_beef', 0, 'entree'),

-- BLACK PEPPER CHICKEN
('S_pepper_chicken', 5.20, 'entree'),
('M_pepper_chicken', 8.50, 'entree'),
('L_pepper_chicken', 11.20, 'entree'),
('K_pepper_chicken', 0, 'entree'),
('N_pepper_chicken', 0, 'entree'),
('F_pepper_chicken', 0, 'entree'),

-- SUPER GREENS ENTREE
('M_super_greens_entree', 4.40, 'entree'),
('L_super_greens_entree', 5.40, 'entree'),
('K_super_greens_entree', 0, 'entree'),
('N_super_greens_entree', 0, 'entree'),
('F_super_greens_entree', 0, 'entree'),

-- SUPER GREENS SIDE
('M_super_greens_side', 4.40, 'side'),
('L_super_greens_side', 5.40, 'side'),
('K_super_greens_side', 0, 'side'),
('N_super_greens_side', 0, 'side'),

-- FRIED RICE
('M_fried_rice', 4.40, 'side'),
('L_fried_rice', 5.40, 'side'),
('K_fried_rice', 0, 'side'),
('N_fried_rice', 0, 'side'),

-- CHOW MEIN
('M_chow_mein', 4.40, 'side'),
('L_chow_mein', 5.40, 'side'),
('K_chow_mein', 0, 'side'),
('N_chow_mein', 0, 'side'),

-- WHITE RICE
('M_white_rice', 4.40, 'side'),
('L_white_rice', 5.40, 'side'),
('K_white_rice', 0, 'side'),
('N_white_rice', 0, 'side'),

-- CHICKEN EGG ROLL
('S_egg_roll', 2.00, 'appetizer'),
('L_egg_roll', 11.20, 'appetizer'),

-- VEGGIE SPRING ROLL
('S_spring_roll', 2.00, 'appetizer'),
('L_spring_roll', 11.20, 'appetizer'),

-- CREAM CHEESE RANGOON
('S_cc_rangoon', 2.00, 'appetizer'),
('L_cc_rangoon', 8.00, 'appetizer'),

-- APPLE PIE ROLL
('S_apple_pie_roll', 2.00, 'appetizer'),
('M_apple_pie_roll', 6.20, 'appetizer'),
('L_apple_pie_roll', 8.00, 'appetizer'),

-- FOUNTAIN DRINKS
('S_fountain_drink', 2.10, 'drink'),
('M_fountain_drink', 2.30, 'drink'),
('L_fountain_drink', 2.50, 'drink'),

-- BOTTLED WATER
('N_bottled_water', 2.30, 'drink'),

-- GATORADE
('N_gatorade', 2.70, 'drink');
