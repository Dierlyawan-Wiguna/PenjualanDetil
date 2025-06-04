package penjualandetil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ensure your FXML file is named FormTransaksi.fxml and is in src/main/resources/penjualandetil
            URL fxmlLocation = getClass().getResource("/penjualandetil/FormTransaksi.fxml");
            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML file. Make sure it's in src/main/resources/penjualandetil/");
                return;
            }
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Form Transaksi Penjualan");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Show a more user-friendly error if needed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Could not load the application interface.");
            alert.setContentText("There was an error loading the FXML file. Please check the logs.\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}