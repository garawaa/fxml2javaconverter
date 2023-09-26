import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBoxBase;
import javafx.stage.Stage;

import java.io.IOException;

public class Fxml2JavaConverterApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
//        root = loader.load();
        root = new MainLayout(getParameters());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FXML to Java Converter");
        primaryStage.show();

    }
}
