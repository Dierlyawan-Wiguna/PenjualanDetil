package penjualandetil.service;

import penjualandetil.entity.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction) throws Exception;
}