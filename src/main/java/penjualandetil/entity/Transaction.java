package penjualandetil.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList; // Import ArrayList
import java.util.List;      // Import List

@Entity
@Table(name = "transactions") // Sesuaikan dengan nama tabel transaksi Anda
public class Transaction {

    @Id
    // Untuk transaction_id, jika Anda mengisinya manual dari textAutoGenerateIdTransaksi,
    // maka @GeneratedValue tidak diperlukan. Jika ingin auto-generate, gunakan strategy yang sesuai.
    // Kita asumsikan ID di-set manual untuk sekarang berdasarkan controller Anda.
    @Column(name = "transaction_id")
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY) // Relasi Many-to-One ke User
    @JoinColumn(name = "user_id", nullable = false) // Kolom foreign key di tabel transactions
    private User user; // Merepresentasikan pembeli

    @Column(name = "transaction_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Menyimpan tanggal dan waktu transaksi
    private Date transactionDate;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2) // Total keseluruhan transaksi
    private BigDecimal totalAmount;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus; // Misalnya "pending", "paid", "cancelled"

    // Relasi One-to-Many ke TransactionDetail
    // 'mappedBy = "transaction"' berarti field 'transaction' di TransactionDetail adalah pemilik relasi
    // CascadeType.ALL berarti operasi (persist, merge, remove) pada Transaction akan berimbas ke TransactionDetail terkait
    // orphanRemoval = true berarti jika TransactionDetail dihapus dari list ini, ia akan dihapus dari database
    @OneToMany(mappedBy = "transaction", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionDetail> transactionDetails = new ArrayList<>();


    // Default constructor (diperlukan oleh Hibernate)
    public Transaction() {
        this.transactionDate = new Date(); // Set tanggal transaksi saat objek dibuat
    }

    // Constructor lain jika diperlukan
    public Transaction(int transactionId, User user, BigDecimal totalAmount, String paymentStatus) {
        this.transactionId = transactionId;
        this.user = user;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.transactionDate = new Date();
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    // Helper method untuk menambahkan TransactionDetail
    public void addTransactionDetail(TransactionDetail detail) {
        transactionDetails.add(detail);
        detail.setTransaction(this);
    }

    // Helper method untuk menghapus TransactionDetail
    public void removeTransactionDetail(TransactionDetail detail) {
        transactionDetails.remove(detail);
        detail.setTransaction(null);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", user=" + (user != null ? user.getUsername() : "null") + // Hindari NullPointerException jika user null
                ", transactionDate=" + transactionDate +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}