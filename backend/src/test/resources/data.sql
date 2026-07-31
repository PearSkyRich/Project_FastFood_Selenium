INSERT INTO roles(id_role, role_name)
VALUES
    ('R_001','ADMIN');


INSERT INTO users(
    id_user,
    fullname,
    username,
    password,
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
    name,
    image_url_food,
    price,
    id_category
)
VALUES
    (
        'H001', NULL, 'Burger', NULL, 50000.00, 'LH001'
    );