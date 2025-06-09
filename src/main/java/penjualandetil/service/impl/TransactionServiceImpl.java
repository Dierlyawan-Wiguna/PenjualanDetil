package penjualandetil.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import penjualandetil.entity.Product; // Import Product
import penjualandetil.entity.Transaction;
import penjualandetil.entity.TransactionDetail; // Import TransactionDetail
import penjualandetil.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {
    private final SessionFactory sessionFactory;

    public TransactionServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveTransaction(Transaction transaction) throws Exception {
        Session session = null;
        org.hibernate.Transaction hibernateTransaction = null;
        try {
            session = sessionFactory.openSession();
            hibernateTransaction = session.beginTransaction();

            // --- LOGIKA PEMBARUAN STOK DITAMBAHKAN DI SINI ---
            for (TransactionDetail detail : transaction.getTransactionDetails()) {
                // Dapatkan produk yang terkait dengan detail transaksi ini
                Product product = session.get(Product.class, detail.getProduct().getProductId());
                if (product == null) {
                    throw new Exception("Produk dengan ID " + detail.getProduct().getProductId() + " tidak ditemukan.");
                }

                int requestedQuantity = detail.getQuantity();
                int currentStock = product.getStock();

                // Periksa apakah stok mencukupi
                if (currentStock < requestedQuantity) {
                    throw new Exception("Stok untuk produk '" + product.getName() + "' tidak mencukupi. Sisa stok: " + currentStock);
                }

                // Kurangi stok
                product.setStock(currentStock - requestedQuantity);
                session.merge(product); // Gunakan merge untuk memperbarui entitas yang sudah ada
            }
            // --- AKHIR DARI LOGIKA PEMBARUAN STOK ---

            session.persist(transaction); // Simpan transaksi dan detailnya (via cascade)
            hibernateTransaction.commit();
        } catch (Exception e) {
            if (hibernateTransaction != null && hibernateTransaction.isActive()) {
                hibernateTransaction.rollback();
            }
            e.printStackTrace(); // Log error
            // Melempar kembali exception agar dapat ditangani oleh controller
            throw new Exception("Gagal menyimpan transaksi: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
