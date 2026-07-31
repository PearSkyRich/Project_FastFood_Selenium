package com.fastfood.integration.service;


import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.entity.transaction.StockReceipt;
import com.fastfood.entity.transaction.StockReceiptDetail;
import com.fastfood.entity.transaction.StockReceiptDetailId;
import com.fastfood.repository.IngredientRepository;
import com.fastfood.repository.StockReceiptRepository;
import com.fastfood.service.IInventoryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class InventoryIntegrationTest {


    @Autowired
    private IInventoryService inventoryService;


    @Autowired
    private IngredientRepository ingredientRepository;


    @Autowired
    private StockReceiptRepository stockReceiptRepository;



    @Test
    void deleteStockReceipt_DaNhap_ShouldRollbackStock(){

        // Tạo nguyên liệu

        Ingredient ingredient = new Ingredient();

        ingredient.setIdIngredient("NL001");
        ingredient.setIngredientName("Thịt bò");
        ingredient.setUnit("kg");
        ingredient.setQuantityStock(
                BigDecimal.valueOf(10)
        );
        ingredient.setImportPrice(
                BigDecimal.valueOf(10000)
        );

        ingredientRepository.save(ingredient);

        // Tạo phiếu nhập

        StockReceipt receipt =
                new StockReceipt();

        receipt.setIdReceipt("PN001");
        receipt.setReceiptDate(
                LocalDate.now()
        );
        receipt.setSupplierName("NCC Test");
        receipt.setStatus("DA_NHAP");
        receipt.setCreatedBy("ADMIN");

        receipt.setDetails(
                new ArrayList<>()
        );

        StockReceiptDetail detail =
                new StockReceiptDetail();

        detail.setId(
                new StockReceiptDetailId(
                        "PN001",
                        "NL001"
                )
        );

        detail.setStockReceipt(receipt);

        detail.setIngredient(ingredient);

        detail.setQuantityImport(
                BigDecimal.valueOf(3)
        );

        detail.setImportPrice(
                BigDecimal.valueOf(10000)
        );

        receipt.getDetails()
                .add(detail);
        stockReceiptRepository.save(receipt);

        // Gọi service thật

        inventoryService.deleteStockReceipt(
                "PN001"
        );

        // Kiểm tra rollback kho
        Ingredient result =
                ingredientRepository
                        .findById("NL001")
                        .orElseThrow();

        assertEquals(
                0,
                result.getQuantityStock()
                        .compareTo(BigDecimal.valueOf(7))
        );

        assertFalse(
                stockReceiptRepository
                        .findById("PN001")
                        .isPresent()
        );

    }
}