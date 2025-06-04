package penjualandetil.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach; // Import BeforeEach
import static org.junit.jupiter.api.Assertions.*; // Import static untuk assertions

import java.math.BigDecimal;

class TransactionDetailTest {

    private Product sampleProduct;
    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        // Inisialisasi objek-objek dummy yang mungkin dibutuhkan
        sampleProduct = new Product("Test Product", new BigDecimal("100.00"));
        sampleProduct.setProductId(1); // Set ID jika diperlukan untuk logika lain

        sampleTransaction = new Transaction(); // Asumsi User tidak krusial untuk test subtotal ini
        sampleTransaction.setTransactionId(101);
    }

    @Test
    void testConstructorAndSubtotalCalculation() {
        int quantity = 3;
        BigDecimal priceAtTransaction = new BigDecimal("100.00");

        // Buat objek TransactionDetail menggunakan constructor
        TransactionDetail detail = new TransactionDetail(sampleTransaction, sampleProduct, quantity, priceAtTransaction);

        // Verifikasi nilai yang di-set oleh constructor
        assertNotNull(detail.getTransaction(), "Transaksi seharusnya tidak null");
        assertEquals(sampleTransaction, detail.getTransaction(), "Objek transaksi tidak sesuai");

        assertNotNull(detail.getProduct(), "Produk seharusnya tidak null");
        assertEquals(sampleProduct, detail.getProduct(), "Objek produk tidak sesuai");

        assertEquals(quantity, detail.getQuantity(), "Kuantitas tidak sesuai");
        assertEquals(priceAtTransaction, detail.getPriceAtTransaction(), "Harga saat transaksi tidak sesuai");

        // Verifikasi kalkulasi subtotal
        BigDecimal expectedSubtotal = new BigDecimal("300.00"); // 3 * 100.00
        assertEquals(0, expectedSubtotal.compareTo(detail.getSubtotal()), "Kalkulasi subtotal tidak sesuai");
        // Menggunakan compareTo untuk BigDecimal lebih aman daripada assertEquals langsung untuk nilai floating point
    }

    @Test
    void testSetQuantityUpdatesSubtotal() {
        TransactionDetail detail = new TransactionDetail();
        detail.setProduct(sampleProduct); // Butuh produk untuk mendapatkan harga
        detail.setPriceAtTransaction(new BigDecimal("50.00")); // Set harga saat transaksi

        detail.setQuantity(4); // Set kuantitas

        BigDecimal expectedSubtotal = new BigDecimal("200.00"); // 4 * 50.00
        assertNotNull(detail.getSubtotal(), "Subtotal seharusnya tidak null setelah setQuantity");
        assertEquals(0, expectedSubtotal.compareTo(detail.getSubtotal()), "Subtotal tidak terupdate dengan benar setelah setQuantity");
    }

    @Test
    void testSetPriceAtTransactionUpdatesSubtotal() {
        TransactionDetail detail = new TransactionDetail();
        detail.setProduct(sampleProduct);
        detail.setQuantity(2); // Set kuantitas terlebih dahulu

        detail.setPriceAtTransaction(new BigDecimal("75.00")); // Set harga saat transaksi

        BigDecimal expectedSubtotal = new BigDecimal("150.00"); // 2 * 75.00
        assertNotNull(detail.getSubtotal(), "Subtotal seharusnya tidak null setelah setPriceAtTransaction");
        assertEquals(0, expectedSubtotal.compareTo(detail.getSubtotal()), "Subtotal tidak terupdate dengan benar setelah setPriceAtTransaction");
    }
}