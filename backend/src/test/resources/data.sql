-- =========================
-- ROLE
-- =========================
INSERT INTO roles (id_role, role_name)
VALUES
    ('R_001', 'admin'),
    ('R_002', 'ThuNgan'),
    ('R_003', 'Bep'),
    ('R_004', 'Ban01');


-- =========================
-- USER
-- =========================
INSERT INTO users
(id_user, username, password_hash, full_name, role_id)
VALUES
    ('U_001', 'admin', 'a123456', 'Nguyễn Văn A', 'R_001'),
    ('U_002', 'thungan', 'a123456', 'Nguyễn Thị B', 'R_002'),
    ('U_003', 'bep', 'a123456', 'Nguyễn Văn C', 'R_003'),
    ('U_004', 'ban01', 'a123456', 'Bàn 01', 'R_004');


-- =========================
-- FOOD CATEGORY
-- =========================
INSERT INTO food_categories
(id_category, category_name)
VALUES
    ('LH001', 'Gà rán'),
    ('LH002', 'Nước uống'),
    ('LH003', 'Combo');


-- =========================
-- FOOD
-- =========================
INSERT INTO foods
(id_food, image_url_food, food_name, id_category, unit_price, description)
VALUES
    ('H001',
     '/images/ga_ran.jpg',
     'Đùi gà chiên giòn',
     'LH001',
     35000,
     'Đùi gà chiên giòn'),

    ('H002',
     '/images/pepsi.jpg',
     'Pepsi',
     'LH002',
     15000,
     'Nước ngọt Pepsi'),

    ('H003',
     '/images/combo1.jpg',
     'Combo gà + nước',
     'LH003',
     50000,
     'Combo tiết kiệm');


-- =========================
-- INGREDIENT
-- =========================
INSERT INTO ingredients
(id_ingredient,
 image_url_ingredient,
 ingredient_name,
 quantity_stock,
 unit,
 import_price)
VALUES
    ('NL001',
     '/images/thit_bo.jpg',
     'Thịt bò',
     10.00,
     'kg',
     100000),

    ('NL002',
     '/images/canh_ga.jpg',
     'Cánh gà',
     10,
     'cái',
     15000),

    ('NL003',
     '/images/dau_an.jpg',
     'Dầu ăn',
     30.00,
     'lít',
     30000);


-- =========================
-- FOOD INGREDIENT
-- =========================
INSERT INTO food_ingredients
(id_food, id_ingredient, quantity_used)
VALUES
    ('H001','NL001',1.00),
    ('H003','NL002',1.00);
