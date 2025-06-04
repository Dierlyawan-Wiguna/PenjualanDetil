package penjualandetil.command;

import penjualandetil.entity.Product;
import penjualandetil.entity.Transaction;
import penjualandetil.entity.TransactionDetail;
import penjualandetil.entity.User;
import penjualandetil.service.ProductService;
import penjualandetil.service.TransactionService;
import penjualandetil.service.UserService;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.util.Date;

public class SaveTransactionCommand implements Command {
    private final TransactionService transactionService;
    private final UserService userService;
    private final ProductService productService;

    private final int transactionId;
    private final int userId;
    private final ObservableList<ObservableList<String>> tableData;
    private final String paymentStatus;

    public SaveTransactionCommand(TransactionService transactionService, UserService userService, ProductService productService,
                                  int transactionId, int userId, ObservableList<ObservableList<String>> tableData, String paymentStatus) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.productService = productService;
        this.transactionId = transactionId;
        this.userId = userId;
        this.tableData = tableData;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public void execute() throws Exception {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new Exception("User dengan ID " + userId + " tidak ditemukan.");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionId(transactionId);
        newTransaction.setUser(user);
        newTransaction.setTransactionDate(new Date());
        newTransaction.setPaymentStatus(paymentStatus);

        BigDecimal totalTransactionAmount = BigDecimal.ZERO;

        for (ObservableList<String> rowData : tableData) {
            int productId = Integer.parseInt(rowData.get(0));
            int quantity = Integer.parseInt(rowData.get(2));

            Product product = productService.findProductById(productId);
            if (product == null) {
                throw new Exception("Produk dengan ID " + productId + " tidak ditemukan.");
            }

            TransactionDetail detail = new TransactionDetail(
                    newTransaction,
                    product,
                    quantity,
                    product.getPrice()
            );
            newTransaction.addTransactionDetail(detail);
            totalTransactionAmount = totalTransactionAmount.add(detail.getSubtotal());
        }
        newTransaction.setTotalAmount(totalTransactionAmount);
        transactionService.saveTransaction(newTransaction);
    }
}