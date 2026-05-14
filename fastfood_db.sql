create database fastfood_db;
-- 1. BẢNG ĐỘC LẬP (Không phụ thuộc khóa ngoại, chạy trước)
-- Bảng roles (Quyền)
INSERT INTO roles (id_role, role_name) VALUES 
('R_001', 'ADMIN'),
('R_002', 'Thu ngân'),
('R_003', 'Bếp'),
('R_004', 'Khách hàng');

-- Bảng food_categories (Danh mục món ăn)
INSERT INTO food_categories (id_category, category_name) VALUES 
('LH001', 'FOOD'),
('LH002', 'DRINK'),
('LH003', 'COMBO');

-- Bảng nguyên liệu
INSERT INTO ingredients (id_ingredient, image_url_ingredient, ingredient_name, unit, quantity_stock, import_price) VALUES 
('NL001', '/images/dui_ga.jpg', 'Đùi gà', 'Cái', 100.00,10000),
('NL002', '/images/canh_ga.jpg', 'Cánh gà', 'Cái', 100.00, 10000),
('NL003', '/images/ga_xay.jpg', 'Gà xay', 'Kg', 40.00, 10000),
('NL004', '/images/bo_xay.jpg', 'Bò xay', 'Kg', 40.00, 10000),
('NL005', '/images/tom_xay.jpg', 'Tôm xay', 'Kg', 20.00, 10000),
('NL006', '/images/vo_banh_burger.jpg', 'Vỏ bánh burger', 'Cái', 300.00, 10000),
('NL007', '/images/my_y.jpg', 'Mỳ ý 100g', 'Túi', 300.00, 10000),
('NL008', '/images/khoai_tay.jpg', 'Khoai tây', 'Kg', 50.00, 10000),
('NL009', '/images/bot_chien_gion.jpg', 'Bột chiên giòn', 'Túi', 200.00, 10000),
('NL010', '/images/bot_chien_xu.jpg', 'Bột chiên xù', 'Túi', 200.00, 10000),
('NL011', '/images/coca.jpg', 'Coca', 'Chai', 200.00, 10000),
('NL012', '/images/7up.jpg', '7up', 'Chai', 200.00, 10000),
('NL013', '/images/fanta.jpg', 'Fanta', 'Chai', 200.00, 10000),
('NL014', '/images/pepsi.jpg', 'Pepsi', 'Chai', 200.00, 10000);

-- 2. BẢNG CẤP 1 (Phụ thuộc vào các bảng trên)
-- Bảng users (Nhân viên)
INSERT INTO users (id_user, full_name, password_hash, username , role_id) VALUES 
('U_001', 'Nguyễn Văn A', '123456', 'ADMIN' , 'R_001'),
('U_002', 'Nguyễn Thị B', '123456', 'ThuNgan' , 'R_002'),
('U_003', 'Nguyễn Văn C', '123456', 'Bep' , 'R_003'),
('U_004', 'Bàn 01', '123456', 'Ban01' , 'R_004');

INSERT INTO users (id_user, full_name, password_hash, username , role_id) VALUES 
('U_005', 'Bàn 02', '123456', 'Ban02' , 'R_004');

INSERT INTO users (id_user, full_name, password_hash, username , role_id) VALUES 
('U_006', 'Bàn 03', '123456', 'Ban03' , 'R_004');

INSERT INTO users (id_user, full_name, password_hash, username , role_id) VALUES 
('U_007', 'Bàn 04', '123456', 'Ban04' , 'R_004');


-- Bảng Món ăn 
INSERT INTO foods (id_food, image_url_food, food_name, id_category, unit_price, description) VALUES 
-- Món lẻ
('H001', '/images/dui_ga_chien_gion.jpg', 'Đùi gà chiên giòn', 'LH001', 45000.00, 'Đùi gà chiên giòn rụn, ngọt từng thớ thịt'),
('H002', '/images/canh_ga_chien_gion_sot_cay.jpg', 'Cánh gà chiên giòn sốt cay', 'LH001', 35000.00, 'Cánh gà giòn từng miếng, đậm vị cay'),
('H003', '/images/hamburger_ga.jpg', 'Hamburger Gà chiên', 'LH001', 60000.00, 'Gà chiên giòn, phô mai Cheddar'),
('H004', '/images/hamburger_bo.jpg', 'Hamburger Bò Phô Mai', 'LH001', 70000.00, 'Bò nướng lửa hồng, phô mai Cheddar'),
('H005', '/images/hamburger_tom.jpg', 'Hamburger Tôm Chiên', 'LH001', 70000.00, 'Tôm chiên, phô mai Cheddar'),
('H006', '/images/my_y_sot.jpg', 'Mỳ ý', 'LH001', 30000.00, 'Mỳ đẫm sốt chuẩn vị'),
('H007', '/images/khoai_tay_chien.jpg', 'Khoai tây chiên to', 'LH001', 30000.00, 'Khoai chiên giòn rụn'),
('H008', '/images/khoai_tay_chien.jpg', 'Khoai tây chiên vừa', 'LH001', 25000.00, 'Khoai chiên giòn rụn'),
('H009', '/images/khoai_tay_chien.jpg', 'Khoai tây chiên nhỏ', 'LH001', 20000.00, 'Khoai chiên giòn rụn'),

-- Nước ngọt
('H010', '/images/coca.jpg', 'Coca Cola', 'LH002', 15000.00, 'Nước ngọt có ga mát lạnh'),
('H011', '/images/7up.jpg', '7Up', 'LH002', 15000.00, 'Nước ngọt vị chanh'),
('H012', '/images/fanta.jpg', 'Fanta', 'LH002', 15000.00, 'Nước ngọt vị cam'),
('H013', '/images/pepsi.jpg', 'Pepsi', 'LH002', 15000.00, 'Nước ngọt có ga mát lạnh'),

-- Combo
('H014', '/images/combo_garan.jpg', 'Combo Gà Rán (1 Gà + 1 Khoai + 1 Nước)', 'LH003', 100000.00, '1 Đùi gà, 1 Khoai vừa, 1 Coca'),
('H015', '/images/combo_burger.jpg', 'Combo Burger Bò (1 Burger + 1 Khoai + 1 Nước)', 'LH003', 100000.00, '1 Burger Bò, 1 Khoai vừa, 1 Pepsi'),
('H016', '/images/combo_myy.jpg', 'Combo Mỳ Ý (1 Mỳ + 1 Cánh gà + 1 Nước)', 'LH003', 75000.00, '1 Mỳ Ý, 1 Cánh gà, 1 7Up'),
('H017', '/images/combo_garan.jpg', 'Combo Gia Đình (4 Gà + 2 Khoai to + 4 Nước)', 'LH003', 250000.00, 'Dành cho 4 người ăn'),
('H018', '/images/combo_burger.jpg', 'Combo Cặp Đôi (2 Burger + 1 Khoai to + 2 Nước)', 'LH003', 190000.00, 'Dành cho 2 người');

-- BẢNG CHUẨN HÓA ĐỊNH MỨC NGUYÊN LIỆU (food_ingredients)
select * from ingredients;
INSERT INTO food_ingredients (id_food, id_ingredient, quantity_used) VALUES 
-- 1. Đùi gà chiên giòn (H001): 1 Đùi gà + Bột chiên giòn
('H001', 'NL001', 1.00), 
('H001', 'NL009', 0.05),

-- 2. Cánh gà chiên giòn (H002): 1 Cánh gà + Bột chiên giòn
('H002', 'NL002', 1.00), 
('H002', 'NL009', 0.05),

-- 3. Hamburger Gà chiên (H003): 1 Vỏ bánh + 0.15kg Gà xay + Bột xù
('H003', 'NL006', 1.00), 
('H003', 'NL003', 0.15),
('H003', 'NL010', 0.05),

-- 4. Hamburger Bò Phô Mai (H004): 1 Vỏ bánh + 0.15kg Bò xay
('H004', 'NL006', 1.00), 
('H004', 'NL004', 0.15),

-- 5. Hamburger Tôm Chiên (H005): 1 Vỏ bánh + 0.15kg Tôm xay + Bột xù
('H005', 'NL006', 1.00), 
('H005', 'NL005', 0.15),
('H005', 'NL010', 0.05),

-- 6. Mỳ ý (H006): 1 Túi mỳ + 0.1kg Bò xay (làm nước xốt)
('H006', 'NL007', 1.00), 
('H006', 'NL004', 0.10),

-- 7. Khoai tây chiên to (H007): 0.25kg Khoai
('H007', 'NL008', 0.25),

-- 8. Khoai tây chiên vừa (H008): 0.15kg Khoai
('H008', 'NL008', 0.15),

-- 9. Khoai tây chiên nhỏ (H009): 0.1kg Khoai
('H009', 'NL008', 0.10),

-- 10. Nước ngọt (H010 -> H013): Mỗi loại tốn 1 chai tương ứng
('H010', 'NL011', 1.00), -- Coca
('H011', 'NL012', 1.00), -- 7up
('H012', 'NL013', 1.00), -- Fanta
('H013', 'NL014', 1.00), -- Pepsi 
-- =========================================================
-- ĐỊNH MỨC CHO CÁC COMBO (Cộng dồn nguyên liệu từ các món đơn)
-- =========================================================

-- 11. Combo Gà Rán H014 (1 Đùi gà + 1 Khoai vừa + 1 Coca)
('H014', 'NL001', 1.00), -- 1 Đùi gà
('H014', 'NL009', 0.05), -- Bột chiên
('H014', 'NL008', 0.15), -- Khoai vừa (0.15kg)
('H014', 'NL011', 1.00), -- 1 Coca

-- 12. Combo Burger Bò H015 (1 Burger Bò + 1 Khoai vừa + 1 Pepsi)
('H015', 'NL006', 1.00), -- 1 Vỏ bánh
('H015', 'NL004', 0.15), -- Bò xay cho 1 Burger
('H015', 'NL008', 0.15), -- Khoai vừa (0.15kg)
('H015', 'NL014', 1.00), -- 1 Pepsi

-- 13. Combo Mỳ Ý H016 (1 Mỳ + 1 Cánh gà + 1 7Up)
('H016', 'NL007', 1.00), -- 1 Túi Mỳ
('H016', 'NL004', 0.10), -- Bò xay (làm xốt mỳ)
('H016', 'NL002', 1.00), -- 1 Cánh gà
('H016', 'NL009', 0.05), -- Bột chiên cho cánh gà
('H016', 'NL012', 1.00), -- 1 7Up

-- 14. Combo Gia Đình H017 (4 Đùi Gà + 2 Khoai to + 4 Coca)
('H017', 'NL001', 4.00), -- 4 Đùi gà
('H017', 'NL009', 0.20), -- Bột chiên (0.05 * 4)
('H017', 'NL008', 0.50), -- 2 Khoai to (0.25 * 2)
('H017', 'NL011', 4.00), -- 4 Coca

-- 15. Combo Cặp Đôi H018 (2 Burger Bò + 1 Khoai to + 2 Pepsi)
('H018', 'NL006', 2.00), -- 2 Vỏ bánh
('H018', 'NL004', 0.30), -- Bò xay cho 2 Burger (0.15 * 2)
('H018', 'NL008', 0.25), -- 1 Khoai to
('H018', 'NL014', 2.00); -- 2 Pepsi