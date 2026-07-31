package com.fastfood.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        // Insert test user
        // INSERT INTO users (username, password_hash, full_name, role_id)
        // VALUES ('admin', '<hashed_password>', 'Admin User', 1);

        // Insert test category
        // INSERT INTO food_categories (id_category, category_name)
        // VALUES ('LH001', 'Test Category');

        // Insert test food
        // INSERT INTO foods (id_food, food_name, id_category, unit_price)
        // VALUES ('H001', 'Test Food', 'LH001', 50000);

        // Insert test ingredient
        // INSERT INTO ingredients (id_ingredient, ingredient_name, quantity_stock, import_price)
        // VALUES ('NL001', 'Test Ingredient', 100, 5000);
    }
}