import converter.FXMLToJavaConvertor;
import converter.MainClass;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Controller {

    @FXML
    private TextField inputTextField;

    @FXML
    private TextField outputTextField;

    @FXML
    private TextField classNameTextField;

    @FXML
    private TextField packageNameTextField;
    @FXML
    private Button inputBrowse;
    @FXML
    private Button outputBrowse;

    @FXML
    private Button convertBtn;

    @FXML
    void chooseInputFile(ActionEvent event) {
        Window window = null;
        window = ((Node) event.getTarget()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your fxml file");
        try {
            String inputPath = inputTextField.getText();
            File initial = new File(inputPath);
            fileChooser.setInitialDirectory(new File(initial.getParent()));
        } catch (Exception e) {
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("FXML", "*.fxml")
        );
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            String inputPath = file.getAbsolutePath();
            inputTextField.setText(inputPath);
            String parentPath = file.getParent().trim();
            if (parentPath.charAt(parentPath.length() - 1)!= File.separatorChar){
                parentPath = parentPath + File.separator;
            }
            String fileName = file.getName();
            String[] fn = fileName.split("\\.");
            String className = fn[0].trim();
            className = className.substring(0, 1).toUpperCase()+className.substring(1)+"Base";
            classNameTextField.setText(className);
            String outputFilePath = parentPath+className+".java";
            outputTextField.setText(outputFilePath);
        }
    }

    @FXML
    void chooseOutputFile(ActionEvent event) {
        Window window = null;
        window = ((Node) event.getTarget()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        String outputFilePath = outputTextField.getText();
        File initial = new File(outputFilePath);
        fileChooser.setInitialDirectory(new File(initial.getParent()));
        fileChooser.setInitialFileName(initial.getName());
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JAVA", "*.java")
        );
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            String outputPath = file.getAbsolutePath();
            outputTextField.setText(outputPath);
        }
    }

    @FXML
    void handleConvert(ActionEvent event) throws ParserConfigurationException {
        Window window = null;
        window = ((Node) event.getTarget()).getScene().getWindow();
        Alert alert;
        String inputPath = inputTextField.getText();
        String outputPath = outputTextField.getText();
        String className = classNameTextField.getText();
        String packageName = packageNameTextField.getText();

        try {
            MainClass mainClass = new MainClass(className, packageName);
            mainClass.setClassModifiers(new int[] { 1, 1024 });
            mainClass.setNodeModifiers(new int[] { 4, 16 });
            mainClass.setMethodModifiers(new int[] { 4, 1024 });
            FXMLToJavaConvertor convertor = new FXMLToJavaConvertor();
            InputStream inputStream = new FileInputStream(inputPath);
            convertor.convert(mainClass, inputStream);
            String content = mainClass.toString();
            File file = new File(outputPath);

            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(content);
            writer.close();

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(content);
//            bw.close();
//            fw.close();
            alert = new Alert(Alert.AlertType.INFORMATION, "Process completed!", ButtonType.OK);
            alert.show();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(window);
            alert.show();
        }
    }


    @FXML
    void initialize() {
        StringProperty inputStringProperty = inputTextField.textProperty();
        StringProperty outputStringProperty = outputTextField.textProperty();
        StringProperty classNameStringProperty = classNameTextField.textProperty();
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            File file = new File(newValue);
            boolean b = !file.exists() || !file.isFile();
            outputBrowse.setDisable(b);

        });
        inputTextField.setFocusTraversable(false);
        inputTextField.setOnDragOver(de->{
            Dragboard db = de.getDragboard();
            if (db.hasFiles()&&de.getGestureSource()!=inputTextField) {

                File file = de.getDragboard().getFiles().get(0);
                String fileName = file.getName();
                String[] fn = fileName.split("\\.");
                if (file.isDirectory() || fn.length < 2 || !fn[1].equals("fxml") ) {
                    de.consume();
                    return;
                }
                de.acceptTransferModes(TransferMode.LINK);
                de.consume();
            }
        });
        inputTextField.setOnDragDropped(de->{
            Dragboard db = de.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0);
                String inputPath = file.getAbsolutePath();
                inputTextField.setText(inputPath);
                String parentPath = file.getParent().trim();
                if (parentPath.charAt(parentPath.length() - 1)!= File.separatorChar){
                    parentPath = parentPath + File.separator;
                }
                String fileName = file.getName();
                String[] fn = fileName.split("\\.");
                String className = fn[0].trim();
                className = className.substring(0, 1).toUpperCase()+className.substring(1)+"Base";
                classNameTextField.setText(className);
                String outputFilePath = parentPath+className+".java";
                outputTextField.setText(outputFilePath);
            }
            de.setDropCompleted(success);
            de.consume();
        });
        inputBrowse.setDefaultButton(true);

        convertBtn.disableProperty().bind(Bindings.or(inputStringProperty.isEmpty(), outputStringProperty.isEmpty()).or(classNameStringProperty.isEmpty()).or(outputBrowse.disableProperty()));
        outputTextField.disableProperty().bind(inputStringProperty.isEmpty());
        classNameTextField.disableProperty().bind(Bindings.or(inputStringProperty.isEmpty(), outputStringProperty.isEmpty()));
        packageNameTextField.disableProperty().bind(classNameStringProperty.isEmpty());
    }
}

