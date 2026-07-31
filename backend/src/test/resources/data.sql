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
        'a123456',
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


INSERT INTO food(
    id_food,
    name,
    price,
    id_category
)
VALUES
    (
        'H001',
        'Chicken Burger',
        50000,
        'LH001'
    );