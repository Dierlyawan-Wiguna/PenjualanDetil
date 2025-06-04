package penjualandetil.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction_details") // Sesuaikan dengan nama tabel detail transaksi Anda
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_detail_id")
    private int transactionDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false) // Foreign key ke tabel transactions
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Foreign key ke tabel products
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_at_transaction", nullable = false, precision = 10, scale = 2) // Harga produk saat transaksi
    private BigDecimal priceAtTransaction; // Ini bisa jadi harga produk saat itu

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2) // quantity * price_at_transaction
    private BigDecimal subtotal;

    // Default constructor (diperlukan oleh Hibernate)
    public TransactionDetail() {
    }

    // Constructor lain jika diperlukan
    public TransactionDetail(Transaction transaction, Product product, int quantity, BigDecimal priceAtTransaction) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
        this.priceAtTransaction = priceAtTransaction;
        this.subtotal = priceAtTransaction.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public int getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(int transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Jika quantity berubah, subtotal juga harus dihitung ulang jika priceAtTransaction sudah ada
        if (this.priceAtTransaction != null) {
            this.subtotal = this.priceAtTransaction.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    public BigDecimal getPriceAtTransaction() {
        return priceAtTransaction;
    }

    public void setPriceAtTransaction(BigDecimal priceAtTransaction) {
        this.priceAtTransaction = priceAtTransaction;
        // Jika harga berubah, subtotal juga harus dihitung ulang jika quantity sudah ada
        if (this.quantity > 0) { // Pastikan quantity valid
            this.subtotal = this.priceAtTransaction.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        // Sebaiknya subtotal dihitung otomatis, tapi setter bisa berguna dalam kasus tertentu
        this.subtotal = subtotal;
    }


    @Override
    public String toString() {
        return "TransactionDetail{" +
                "transactionDetailId=" + transactionDetailId +
                ", product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                ", priceAtTransaction=" + priceAtTransaction +
                ", subtotal=" + subtotal +
                '}';
    }
}