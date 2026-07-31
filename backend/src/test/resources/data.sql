INSERT INTO roles(id_role, role_name)
VALUES
    ('R_001','ADMIN');


INSERT INTO users(
    id_user,
    username,
    password_hash,
    role_id
)
VALUES
    (
        'U_001',
        'ADMIN',
        '$2a$12$YHD6AB9wIjMWVJetjp5fSOk2PIMEX2yTzL8t8qkUPbU47T2OR0WPW',
        'R_001'
    );


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
    name,
    price,
    id_category
)
VALUES
    (
        'H001', NULL, 'Burger', NULL, 50000.00, NULL
    );