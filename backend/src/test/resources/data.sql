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


INSERT INTO food_category(
    id_category,
    name
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
    category_id
)
VALUES
    (
        'H001',
        'Chicken Burger',
        50000,
        'LH001'
    );