INSERT INTO roles(id_role, role_name)
VALUES
    ('R_001','ADMIN');


INSERT INTO users(
    id_user,
    full_name,
    username,
    password_hash,
    role_id
)
VALUES
    (
        'U_001',
        'Nguyen Van A',
        'ADMIN',
        'a123456',
        'R_001'
    );
INSERT INTO ingredients (
    id_ingredient,
    image_url_ingredient,
    ingredient_name,
    quantity_stock,
    unit,
    import_price
) VALUES
      ('NL001', NULL, 'Thịt bò', 7.00, 'kg', 10000.00),
      ('NL002', '/images/canh_ga.jpg', 'Cánh gà', 46.00, 'Cái', NULL),
      ('NL003', '/images/ga_xay.jpg', 'Gà xay', 39.55, 'Kg', NULL),
      ('NL004', '/images/bo_xay.jpg', 'Bò xay', 39.85, 'Kg', NULL),
      ('NL005', '/images/tom_xay.jpg', 'Tôm xay', 19.70, 'Kg', NULL),
      ('NL006', '/images/vo_banh_burger.jpg', 'Vỏ bánh burger', 294.00, 'Cái', NULL),
      ('NL007', '/images/my_y.jpg', 'Mỳ ý 100g', 300.00, 'Túi', NULL),
      ('NL008', '/images/khoai_tay.jpg', 'Khoai tây', 49.50, 'Kg', NULL),
      ('NL009', '/images/bot_chien_gion.jpg', 'Bột chiên giòn', 191.45, 'Túi', NULL),
      ('NL010', '/images/bot_chien_xu.jpg', 'Bột chiên xù', 199.75, 'Túi', NULL),
      ('NL011', '/images/coca.jpg', 'Coca', 200.00, 'Chai', NULL),
      ('NL012', '/images/7up.jpg', '7up', 199.00, 'Chai', NULL),
      ('NL013', '/images/fanta.jpg', 'Fanta', 200.00, 'Chai', NULL),
      ('NL014', '/images/pepsi.jpg', 'Pepsi', 199.00, 'Chai', NULL);

INSERT INTO food_categories(
    id_category,
    category_name
)
VALUES
    (
        'LH001',
        'Chicken'
    );


INSERT INTO foods(
    id_food,
    description,
    food_name,
    image_url_food,
    unit_price,
    id_category
)
VALUES
    (
        'H001', 'Burger gà', 'Burger', '/images/hamburger_ga.jpg', 50000.00, 'LH001'
    );

INSERT INTO food_ingredients (
    quantity_used,
    id_ingredient,
    id_food
) VALUES
      (1.00, 'NL001', 'H001');
