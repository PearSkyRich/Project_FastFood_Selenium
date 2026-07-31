-- =========================
-- ROLE
-- =========================
INSERT INTO roles (id_role, role_name)
VALUES
    ('R_001', 'ADMIN'),
    ('R_002', 'ThuNgan'),
    ('R_003', 'Bep'),
    ('R_004', 'Ban');


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
    ('CAT001', 'Gà rán'),
    ('CAT002', 'Nước uống'),
    ('CAT003', 'Combo');


-- =========================
-- FOOD
-- =========================
INSERT INTO foods
(id_food, image_url_food, food_name, id_category, unit_price, description)
VALUES
    ('F001',
     '/images/ga_ran.jpg',
     'Đùi gà chiên giòn',
     'CAT001',
     35000,
     'Đùi gà chiên giòn'),

    ('F002',
     '/images/pepsi.jpg',
     'Pepsi',
     'CAT002',
     15000,
     'Nước ngọt Pepsi'),

    ('F003',
     '/images/combo1.jpg',
     'Combo gà + nước',
     'CAT003',
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
     20.00,
     'kg',
     100000),

    ('NL002',
     '/images/canh_ga.jpg',
     'Cánh gà',
     50.00,
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
    ('F001','NL002',1.00),
    ('F001','NL003',0.05),
    ('F003','NL002',1.00);


-- =========================
-- ORDER
-- =========================
INSERT INTO orders
(id_order,
 table_number,
 customer_name,
 order_time,
 status,
 created_by)
VALUES
    ('ORD001',
     'N01',
     'Khách bàn 01',
     CURRENT_TIMESTAMP,
     'PENDING',
     'U_004');


-- =========================
-- ORDER DETAIL
-- =========================
INSERT INTO order_details
(order_id,
 food_id,
 quantity,
 unit_price,
 status)
VALUES
    ('ORD001',
     'F001',
     2,
     35000,
     'PENDING'),

    ('ORD001',
     'F002',
     1,
     15000,
     'SERVED');


-- =========================
-- SALES INVOICE
-- =========================
INSERT INTO sales_invoices
(id_invoice,
 order_id,
 customer_phone,
 payment_date,
 payment_method,
 total_amount)
VALUES
    ('INV001',
     'ORD001',
     '0988888888',
     CURRENT_TIMESTAMP,
     'CASH',
     85000);


-- =========================
-- STOCK RECEIPT
-- =========================
INSERT INTO stock_receipts
(id_receipt,
 receipt_date,
 supplier_name,
 status,
 created_by)
VALUES
    ('REC001',
     CURRENT_DATE,
     'Nhà cung cấp ABC',
     'COMPLETED',
     'U_001');


-- =========================
-- STOCK RECEIPT DETAIL
-- =========================
INSERT INTO stock_receipt_details
(receipt_id,
 ingredient_id,
 quantity_import,
 import_price)
VALUES
    ('REC001',
     'NL002',
     20.00,
     15000),

    ('REC001',
     'NL003',
     10.00,
     30000);