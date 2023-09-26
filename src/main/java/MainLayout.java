import converter.FXMLToJavaConvertor;
import converter.MainClass;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainLayout extends MainBase{

    StringProperty inputStringProperty = inputTextField.textProperty();
    StringProperty outputStringProperty = outputTextField.textProperty();
    StringProperty classNameStringProperty = classNameTextField.textProperty();

    public MainLayout(Application.Parameters param) {
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
                String parentPath = file.getParent().trim();
                if (parentPath.charAt(parentPath.length() - 1)!= File.separatorChar){
                    parentPath = parentPath + File.separator;
                }
                String fileName = file.getName();
                String[] fn = fileName.split("\\.");
                if (fn[1].equals("fxml")) {
                    String className = fn[0].trim();
                    className = className.substring(0, 1).toUpperCase()+className.substring(1)+"Base";
                    inputTextField.setText(inputPath);
                    classNameTextField.setText(className);
                    String outputFilePath = parentPath+className+".java";
                    outputTextField.setText(outputFilePath);
                }
            }
            de.setDropCompleted(success);
            de.consume();
        });
        inputBrowse.setDefaultButton(true);
        convertBtn.disableProperty().bind(Bindings.or(inputStringProperty.isEmpty(), outputStringProperty.isEmpty()).or(classNameStringProperty.isEmpty()).or(outputBrowse.disableProperty()));
        outputTextField.disableProperty().bind(inputStringProperty.isEmpty());
        classNameTextField.disableProperty().bind(Bindings.or(inputStringProperty.isEmpty(), outputStringProperty.isEmpty()));
        packageNameTextField.disableProperty().bind(classNameStringProperty.isEmpty());
        //if console argument is available check it and complete textfields
        if (param.getUnnamed().size()>0){
            try {
                String pathFromParam = param.getUnnamed().get(0);
                File file = new File(pathFromParam);
                String inputPath = param.getUnnamed().get(0);
                String parentPath = file.getParent().trim();
                if (parentPath.charAt(parentPath.length() - 1)!= File.separatorChar){
                    parentPath = parentPath + File.separator;
                }
                String fileName = file.getName();
                String[] fn = fileName.split("\\.");

                if (fn[1].equals("fxml")) {
                    String className = fn[0].trim();
                    className = className.substring(0, 1).toUpperCase()+className.substring(1)+"Base";
                    inputTextField.setText(inputPath);
                    classNameTextField.setText(className);
                    String outputFilePath = parentPath+className+".java";
                    outputTextField.setText(outputFilePath);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }


        }

    }

    @Override
    protected void chooseInputFile(ActionEvent actionEvent) {
        Window window;
        window = ((Node) actionEvent.getTarget()).getScene().getWindow();
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


    @Override
    protected void chooseOutputFile(ActionEvent actionEvent) {
        Window window;
        window = ((Node) actionEvent.getTarget()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File initial = null;
        try {
            String outputFilePath = outputTextField.getText();
            initial = new File(outputFilePath);
            fileChooser.setInitialDirectory(new File(initial.getParent()));
        } catch (Exception e) {
        }
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



    @Override
    protected void handleConvert(ActionEvent actionEvent) {
        Window window;
        window = ((Node) actionEvent.getTarget()).getScene().getWindow();
        convert(window);
    }

    private void convert(Window window) {
        Alert alert;
        String inputPath = inputTextField.getText();
        String outputPath = outputTextField.getText();
        String className = classNameTextField.getText().trim();
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
            alert = new Alert(Alert.AlertType.INFORMATION, "Process completed!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(window);
            alert.show();
        }
    }

}
