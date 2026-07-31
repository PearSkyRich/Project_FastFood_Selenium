package com.fastfood.service.impl;

import com.fastfood.dto.request.StockReceiptDetailRequest;
import com.fastfood.dto.request.StockReceiptRequest;
import com.fastfood.dto.response.StockReceiptResponse;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.entity.transaction.StockReceipt;
import com.fastfood.entity.transaction.StockReceiptDetail;
import com.fastfood.entity.transaction.StockReceiptDetailId;
import com.fastfood.repository.IngredientRepository;
import com.fastfood.repository.OrderDetailRepository;
import com.fastfood.repository.StockReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private StockReceiptRepository stockReceiptRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    private InventoryImpl inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryImpl(ingredientRepository, stockReceiptRepository, orderDetailRepository);
    }

    @Test
    void updateStockReceipt_whenChoToDaNhap_shouldIncreaseStock() {
        Ingredient ingredient = buildIngredient("NL001", new BigDecimal("10"));
        StockReceipt receipt = buildReceipt("PN001", "CHO", new ArrayList<>());

        StockReceiptRequest request = StockReceiptRequest.builder()
                .receiptDate(LocalDate.now())
                .supplierName("NCC A")
                .status("DA_NHAP")
                .createdBy("U_001")
                .details(List.of(buildDetailRequest("NL001", "2")))
                .build();

        when(stockReceiptRepository.findById("PN001")).thenReturn(Optional.of(receipt));
        when(ingredientRepository.findById("NL001")).thenReturn(Optional.of(ingredient));
        when(stockReceiptRepository.save(any(StockReceipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockReceiptResponse response = inventoryService.updateStockReceipt("PN001", request);

        assertNotNull(response);
        assertEquals("DA_NHAP", response.getStatus());
        assertEquals(new BigDecimal("12"), ingredient.getQuantityStock());
        verify(ingredientRepository, times(1)).save(ingredient);
    }

    @Test
    void updateStockReceipt_whenChoToHoanTra_shouldNotIncreaseStock() {
        Ingredient ingredient = buildIngredient("NL001", new BigDecimal("10"));
        StockReceipt receipt = buildReceipt("PN001", "CHO", new ArrayList<>());

        StockReceiptRequest request = StockReceiptRequest.builder()
                .receiptDate(LocalDate.now())
                .supplierName("NCC A")
                .status("HOAN_TRA")
                .createdBy("U_001")
                .details(List.of(buildDetailRequest("NL001", "2")))
                .build();

        when(stockReceiptRepository.findById("PN001")).thenReturn(Optional.of(receipt));
        when(ingredientRepository.findById("NL001")).thenReturn(Optional.of(ingredient));
        when(stockReceiptRepository.save(any(StockReceipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockReceiptResponse response = inventoryService.updateStockReceipt("PN001", request);

        assertNotNull(response);
        assertEquals("HOAN_TRA", response.getStatus());
        assertEquals(new BigDecimal("10"), ingredient.getQuantityStock());
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    void deleteStockReceipt_whenDaNhap_shouldRollbackStock() {
        Ingredient ingredient = buildIngredient("NL001", new BigDecimal("10"));

        StockReceipt receipt = buildReceipt("PN001", "DA_NHAP", new ArrayList<>());
        StockReceiptDetail detail = StockReceiptDetail.builder()
                .id(new StockReceiptDetailId("PN001", "NL001"))
                .stockReceipt(receipt)
                .ingredient(ingredient)
                .quantityImport(new BigDecimal("3"))
                .importPrice(new BigDecimal("10000"))
                .build();
        receipt.getDetails().add(detail);

        when(stockReceiptRepository.findById("PN001")).thenReturn(Optional.of(receipt));

        inventoryService.deleteStockReceipt("PN001");

        assertEquals(new BigDecimal("7"), ingredient.getQuantityStock());
        verify(ingredientRepository, times(1)).save(ingredient);
        verify(stockReceiptRepository, times(1)).delete(receipt);
    }
    @Test
    void createStockReceipt_success() {

        Ingredient ingredient =
                buildIngredient("NL001", BigDecimal.TEN);

        StockReceiptRequest request =
                StockReceiptRequest.builder()
                        .receiptDate(LocalDate.now())
                        .supplierName("NCC A")
                        .status("CHO")
                        .createdBy("U001")
                        .details(List.of(
                                buildDetailRequest("NL001", "5")
                        ))
                        .build();

        when(stockReceiptRepository.findMaxIdReceipt())
                .thenReturn("PN001");

        when(ingredientRepository.findById("NL001"))
                .thenReturn(Optional.of(ingredient));

        when(stockReceiptRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        StockReceiptResponse response =
                inventoryService.createStockReceipt(request);

        assertNotNull(response);

        assertEquals(
                "PN002",
                response.getIdReceipt()
        );
        assertEquals(
                "CHO",
                response.getStatus()
        );
        verify(stockReceiptRepository)
                .save(any(StockReceipt.class));
    }

    @Test
    void createStockReceipt_emptyDetails_throwException(){

        StockReceiptRequest request =
                StockReceiptRequest.builder()
                        .supplierName("NCC")
                        .details(new ArrayList<>())
                        .build();

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> inventoryService.createStockReceipt(request)
                );
        assertEquals(
                "Phiếu nhập phải có ít nhất 1 nguyên liệu",
                ex.getMessage()
        );
    }

    @Test
    void createStockReceipt_ingredientNotFound(){

        StockReceiptRequest request =
                StockReceiptRequest.builder()
                        .details(List.of(
                                buildDetailRequest("NL999","2")
                        ))
                        .build();

        when(stockReceiptRepository.findMaxIdReceipt())
                .thenReturn(null);

        when(ingredientRepository.findById("NL999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> inventoryService.createStockReceipt(request)
        );
    }

    @Test
    void getAllStockReceipts_success(){

        StockReceipt receipt =
                buildReceipt(
                        "PN001",
                        "CHO",
                        new ArrayList<>()
                );

        when(stockReceiptRepository.findAll())
                .thenReturn(List.of(receipt));

        List<StockReceiptResponse> result =
                inventoryService.getAllStockReceipts();

        assertEquals(
                1,
                result.size()
        );
        assertEquals(
                "PN001",
                result.get(0).getIdReceipt()
        );
    }

    @Test
    void updateStockReceipt_notFound(){

        when(stockReceiptRepository.findById("PN999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> inventoryService.updateStockReceipt(
                        "PN999",
                        new StockReceiptRequest()
                )
        );
    }

    @Test
    void updateStockReceipt_alreadyReceived_throwException(){

        StockReceipt receipt =
                buildReceipt(
                        "PN001",
                        "DA_NHAP",
                        new ArrayList<>()
                );

        when(stockReceiptRepository.findById("PN001"))
                .thenReturn(Optional.of(receipt));

        assertThrows(
                RuntimeException.class,
                () -> inventoryService.updateStockReceipt(
                        "PN001",
                        new StockReceiptRequest()
                )
        );
    }

    @Test
    void deleteStockReceipt_whenCho_shouldNotRollback(){

        Ingredient ingredient =
                buildIngredient(
                        "NL001",
                        BigDecimal.TEN
                );


        StockReceipt receipt =
                buildReceipt(
                        "PN001",
                        "CHO",
                        new ArrayList<>()
                );

        when(stockReceiptRepository.findById("PN001"))
                .thenReturn(Optional.of(receipt));

        inventoryService.deleteStockReceipt("PN001");

        assertEquals(
                BigDecimal.TEN,
                ingredient.getQuantityStock()
        );

        verify(
                ingredientRepository,
                never()
        ).save(any());
    }

    @Test
    void getLowStockItems_success(){

        Ingredient ingredient =
                buildIngredient(
                        "NL001",
                        BigDecimal.valueOf(5)
                );

        when(ingredientRepository.findAll())
                .thenReturn(List.of(ingredient));

        var result =
                inventoryService.getLowStockItems();

        assertEquals(
                1,
                result.size()
        );

        assertTrue(
                result.get(0).isLowStock()
        );
    }

    @Test
    void getConsumptionHistory_success(){

        Object[] row = {
                Date.valueOf("2026-01-01"),
                "NL001",
                "image.png",
                "Thịt",
                "kg",
                BigDecimal.TEN,
                BigDecimal.valueOf(2)
        };


        when(orderDetailRepository
                .getIngredientConsumptionHistory(
                        any(LocalDate.class),
                        any(LocalDate.class)
                ))
                .thenReturn(List.<Object[]>of(row));

        var result =
                inventoryService.getConsumptionHistory(
                        LocalDate.now(),
                        LocalDate.now()
                );

        assertEquals(1, result.size());

        assertEquals(
                1,
                result.get(0)
                        .getItems()
                        .size()
        );
    }


    private Ingredient buildIngredient(String id, BigDecimal stock) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient(id);
        ingredient.setIngredientName("Nguyen lieu test");
        ingredient.setUnit("kg");
        ingredient.setQuantityStock(stock);
        return ingredient;
    }

    private StockReceipt buildReceipt(String id, String status, List<StockReceiptDetail> details) {
        StockReceipt receipt = new StockReceipt();
        receipt.setIdReceipt(id);
        receipt.setReceiptDate(LocalDate.now());
        receipt.setSupplierName("NCC");
        receipt.setStatus(status);
        receipt.setCreatedBy("U_001");
        receipt.setDetails(details);
        return receipt;
    }

    private StockReceiptDetailRequest buildDetailRequest(String ingredientId, String qty) {
        return StockReceiptDetailRequest.builder()
                .ingredientId(ingredientId)
                .quantityImport(new BigDecimal(qty))
                .importPrice(new BigDecimal("10000"))
                .build();
    }
}

