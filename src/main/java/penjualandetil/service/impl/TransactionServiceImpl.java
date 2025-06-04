package penjualandetil.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import penjualandetil.entity.Transaction;
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
            session.persist(transaction); // Atau save, tergantung versi Hibernate/JPA
            hibernateTransaction.commit();
        } catch (Exception e) {
            if (hibernateTransaction != null && hibernateTransaction.isActive()) {
                hibernateTransaction.rollback();
            }
            e.printStackTrace(); // Log error
            throw new Exception("Failed to save transaction: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}