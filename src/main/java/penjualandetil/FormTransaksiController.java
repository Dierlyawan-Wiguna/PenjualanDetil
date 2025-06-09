package penjualandetil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import org.hibernate.SessionFactory; // Import SessionFactory
import penjualandetil.entity.Product;
import penjualandetil.entity.User;
// Tidak perlu import Transaction dan TransactionDetail secara langsung di sini jika hanya digunakan via Command
import penjualandetil.util.HibernateUtil;

// Import untuk Service
import penjualandetil.service.ProductService;
import penjualandetil.service.UserService;
import penjualandetil.service.TransactionService;
import penjualandetil.service.impl.ProductServiceImpl;
import penjualandetil.service.impl.UserServiceImpl;
import penjualandetil.service.impl.TransactionServiceImpl;
import penjualandetil.service.decorator.LoggingProductServiceDecorator;

// Import untuk Command
import penjualandetil.command.Command;
import penjualandetil.command.SaveTransactionCommand;

import java.math.BigDecimal;
import java.util.List;
// java.util.Date sudah diimpor oleh SaveTransactionCommand jika diperlukan di sana

public class FormTransaksiController {

    @FXML
    private TextField textAutoGenerateIdTransaksi, textInputIDBuyer, textNamaBuyer, textTeleponBuyer;

    @FXML
    private TextField textKodeBarang, textNamaBarang, textGetHarga, textJumlahBarang1, textTotalHarga, textBayar, textKembalian;

    @FXML
    private TableView<ObservableList<String>> tableBarang;

    @FXML
    private TableColumn<ObservableList<String>, String> colKode, colNama, colJumlah, colSubtotal;

    @FXML
    private ListView<String> listItems;

    private ObservableList<String> productList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();

    private SessionFactory sessionFactory;
    // Deklarasi Service
    private ProductService productService;
    private UserService userService;
    private TransactionService transactionService;

    public void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();

        // Inisialisasi service implementasi asli
        ProductService basicProductService = new ProductServiceImpl(sessionFactory);
        // Bungkus dengan decorator logging
        productService = new LoggingProductServiceDecorator(basicProductService);

        userService = new UserServiceImpl(sessionFactory);
        transactionService = new TransactionServiceImpl(sessionFactory);

        loadProducts();
        configureTable();
    }

    private void loadProducts() {
        productList.clear();
        try {
            List<String> names = productService.getAllProductNames(); // Menggunakan service
            if (names != null && !names.isEmpty()) {
                productList.addAll(names);
            } else if (names == null) {
                showError("Service Error", "Gagal memuat produk dari service (null).");
            } else {
                // names is empty, which is a valid state, no error needed
            }
        } catch (Exception e) {
            showError("Load Product Error", "Terjadi kesalahan saat memuat produk: " + e.getMessage());
            e.printStackTrace();
        }
        listItems.setItems(productList);
    }

    private void configureTable() {
        colKode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0))); //
        colNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1))); //
        colJumlah.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2))); //
        colSubtotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3))); //
        tableBarang.setItems(tableData); //
    }

    @FXML
    void handleCariClick(ActionEvent event) {
        textNamaBuyer.clear(); // Clear previous results
        textTeleponBuyer.clear();
        if (textInputIDBuyer.getText().isEmpty()) {
            showError("Input Error", "ID Pembeli harus diisi.");
            return;
        }
        try {
            int userId = Integer.parseInt(textInputIDBuyer.getText());
            User user = userService.findUserById(userId); // Menggunakan service

            if (user != null) {
                textNamaBuyer.setText(user.getUsername());
                // Diubah: Menggunakan getNoTelepon() sesuai perubahan pada Entity User
                textTeleponBuyer.setText(user.getNoTelepon());
            } else {
                showError("Data Not Found", "User ID " + userId + " tidak ditemukan.");
            }
        } catch (NumberFormatException e) {
            showError("Input Error", "User ID harus berupa angka yang valid.");
        } catch (Exception e) {
            showError("Service Error", "Gagal mencari user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleListClick(MouseEvent event) {
        String selectedProductName = listItems.getSelectionModel().getSelectedItem();
        if (selectedProductName != null) {
            try {
                Product product = productService.findProductByName(selectedProductName); // Menggunakan service
                if (product != null) {
                    textKodeBarang.setText(String.valueOf(product.getProductId()));
                    textNamaBarang.setText(product.getName());
                    textGetHarga.setText(product.getPrice().toPlainString());
                } else {
                    clearProductFields();
                    showError("Data Not Found", "Produk '" + selectedProductName + "' tidak ditemukan detailnya.");
                }
            } catch (Exception e) {
                clearProductFields();
                showError("Service Error", "Gagal memuat detail produk: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleTambahClick(ActionEvent event) {
        if (textKodeBarang.getText().isEmpty() || textNamaBarang.getText().isEmpty() ||
                textGetHarga.getText().isEmpty() || textJumlahBarang1.getText().isEmpty()) {
            showError("Input Error", "Pastikan semua field produk (Kode, Nama, Harga, Jumlah) terisi sebelum menambahkan.");
            return;
        }

        String kode = textKodeBarang.getText();
        String nama = textNamaBarang.getText();
        String jumlahStr = textJumlahBarang1.getText();
        String hargaStr = textGetHarga.getText();

        try {
            int qty = Integer.parseInt(jumlahStr);
            if (qty <= 0) {
                showError("Input Error", "Jumlah barang harus lebih dari 0.");
                return;
            }

            // --- LOGIKA CEK STOK SEBELUM TAMBAH KE KERANJANG ---
            int productId = Integer.parseInt(kode);
            Product product = productService.findProductById(productId); // Dapatkan objek produk lengkap
            if (product.getStock() < qty) {
                showError("Stok Tidak Cukup", "Stok untuk '" + product.getName() + "' hanya tersisa " + product.getStock() + ".");
                return; // Hentikan proses jika stok kurang
            }
            // --- AKHIR LOGIKA CEK STOK ---

            BigDecimal price = new BigDecimal(hargaStr);
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));

            ObservableList<String> row = FXCollections.observableArrayList(kode, nama, jumlahStr, subtotal.toPlainString());
            tableData.add(row);

            updateTotalHarga();
            clearProductFields();
        } catch (NumberFormatException e) {
            showError("Input Error", "Jumlah dan harga harus berupa angka yang valid.");
        } catch (Exception e) {
            showError("Service Error", "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTotalHarga() {
        BigDecimal total = BigDecimal.ZERO;
        for (ObservableList<String> row : tableData) {
            try {
                total = total.add(new BigDecimal(row.get(3))); // Kolom subtotal
            } catch (NumberFormatException e) {
                System.err.println("Error parsing subtotal in tableData: " + row.get(3));
            }
        }
        textTotalHarga.setText(total.toPlainString());
    }

    private void clearProductFields() {
        textKodeBarang.clear();
        textNamaBarang.clear();
        textGetHarga.clear();
        textJumlahBarang1.clear();
    }

    @FXML
    void handleSimpanClick(ActionEvent event) {
        if (textAutoGenerateIdTransaksi.getText().isEmpty()) {
            showError("Input Error", "No. Transaksi harus diisi.");
            return;
        }
        if (textInputIDBuyer.getText().isEmpty()) {
            showError("Input Error", "ID Pembeli harus diisi.");
            return;
        }
        if (tableData.isEmpty()) {
            showError("Input Error", "Tidak ada barang dalam transaksi.");
            return;
        }

        try {
            int transactionId = Integer.parseInt(textAutoGenerateIdTransaksi.getText());
            int userId = Integer.parseInt(textInputIDBuyer.getText());

            // Membuat salinan tableData yang tidak dapat dimodifikasi untuk command
            // Ini penting jika command dieksekusi secara asynchronous atau jika tableData bisa berubah
            ObservableList<ObservableList<String>> currentTableData = FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(tableData) // Buat salinan baru
            );


            // Membuat objek command
            Command saveCommand = new SaveTransactionCommand(
                    transactionService,
                    userService,
                    productService,
                    transactionId,
                    userId,
                    currentTableData, // Gunakan salinan
                    "pending" // Status pembayaran awal
            );

            // Mengeksekusi command
            saveCommand.execute();

            showInfo("Success", "Transaction saved successfully.");
            clearFields(); // Bersihkan UI setelah berhasil

        } catch (NumberFormatException e) {
            showError("Input Error", "ID Transaksi atau ID Pembeli tidak valid: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Menangkap exception dari command.execute()
            showError("Operation Failed", "Gagal menyimpan transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBatalClick(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleTutupClick(ActionEvent event) {
        HibernateUtil.shutdown(); // Memanggil shutdown dari HibernateUtil
        System.exit(0);
    }

    private void clearFields() {
        textAutoGenerateIdTransaksi.clear();
        textInputIDBuyer.clear();
        textNamaBuyer.clear();
        textTeleponBuyer.clear();
        clearProductFields();
        textTotalHarga.setText("0.00"); // Set default setelah clear
        textBayar.clear();
        textKembalian.setText("0.00"); // Set default setelah clear
        tableData.clear();
        // updateTotalHarga(); // Sudah di-handle oleh textTotalHarga.setText("0.00") dan tableData.clear()
    }

    @FXML
    void handleBayarKeyReleased() {
        try {
            if (textTotalHarga.getText().isEmpty() || textBayar.getText().isEmpty()) {
                textKembalian.setText("0.00");
                return;
            }
            BigDecimal totalHarga = new BigDecimal(textTotalHarga.getText());
            BigDecimal bayar = new BigDecimal(textBayar.getText());

            if (bayar.compareTo(BigDecimal.ZERO) < 0) { // Tidak boleh bayar dengan minus
                textKembalian.setText("0.00");
                showError("Input Error", "Jumlah bayar tidak boleh negatif.");
                return;
            }

            BigDecimal kembalian = bayar.subtract(totalHarga);

            if (kembalian.compareTo(BigDecimal.ZERO) >= 0) {
                textKembalian.setText(String.format("%.2f", kembalian));
            } else {
                textKembalian.setText("0.00"); // Atau tampilkan kekurangan
            }
        } catch (NumberFormatException e) {
            textKembalian.setText("0.00");
            // Tidak perlu showError di sini karena bisa terjadi saat user sedang mengetik
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
