
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public abstract class MainBase extends VBox {

    protected final HBox hBox;
    protected final Label label;
    protected final Label label0;
    protected final HBox hBox0;
    protected final TextField inputTextField;
    protected final Tooltip tooltip;
    protected final Button inputBrowse;
    protected final Label label1;
    protected final HBox hBox1;
    protected final TextField outputTextField;
    protected final Button outputBrowse;
    protected final Label label2;
    protected final HBox hBox2;
    protected final TextField classNameTextField;
    protected final Label label3;
    protected final HBox hBox3;
    protected final TextField packageNameTextField;
    protected final HBox hBox4;
    protected final Button convertBtn;
    protected final HBox hBox5;
    protected final Label label4;

    public MainBase() {

        hBox = new HBox();
        label = new Label();
        label0 = new Label();
        hBox0 = new HBox();
        inputTextField = new TextField();
        tooltip = new Tooltip();
        inputBrowse = new Button();
        label1 = new Label();
        hBox1 = new HBox();
        outputTextField = new TextField();
        outputBrowse = new Button();
        label2 = new Label();
        hBox2 = new HBox();
        classNameTextField = new TextField();
        label3 = new Label();
        hBox3 = new HBox();
        packageNameTextField = new TextField();
        hBox4 = new HBox();
        convertBtn = new Button();
        hBox5 = new HBox();
        label4 = new Label();

        setMinHeight(340.0);
        setPrefHeight(340.0);
        setPrefWidth(600.0);

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setPrefHeight(36.0);
        hBox.setPrefWidth(580.0);

        label.setText("FXML to Java Converter");
        label.setFont(new Font(29.0));

        label0.setText("Input file path");

        hBox0.setPrefHeight(36.0);
        hBox0.setPrefWidth(600.0);

        HBox.setHgrow(inputTextField, javafx.scene.layout.Priority.ALWAYS);
        inputTextField.setFocusTraversable(false);
        inputTextField.setPrefHeight(31.0);
        inputTextField.setPrefWidth(474.0);
        inputTextField.setPromptText("required");

        tooltip.setText("You can drop a fxml file here");
        inputTextField.setTooltip(tooltip);

        inputBrowse.setMnemonicParsing(false);
        inputBrowse.setOnAction(this::chooseInputFile);
        inputBrowse.setText("Browse");

        label1.setText("Output file path");

        hBox1.setPrefHeight(40.0);
        hBox1.setPrefWidth(600.0);

        HBox.setHgrow(outputTextField, javafx.scene.layout.Priority.ALWAYS);
        outputTextField.setPrefHeight(31.0);
        outputTextField.setPrefWidth(471.0);
        outputTextField.setPromptText("required");

        outputBrowse.setDisable(true);
        outputBrowse.setMnemonicParsing(false);
        outputBrowse.setOnAction(this::chooseOutputFile);
        outputBrowse.setText("Change");

        label2.setText("ClassName");

        hBox2.setPrefHeight(35.0);
        hBox2.setPrefWidth(580.0);

        HBox.setHgrow(classNameTextField, javafx.scene.layout.Priority.ALWAYS);
        classNameTextField.setPrefHeight(31.0);
        classNameTextField.setPrefWidth(509.0);
        classNameTextField.setPromptText("required");

        label3.setText("PackageName (optional)");

        hBox3.setPrefHeight(42.0);
        hBox3.setPrefWidth(580.0);

        HBox.setHgrow(packageNameTextField, javafx.scene.layout.Priority.ALWAYS);
        packageNameTextField.setPrefHeight(31.0);
        packageNameTextField.setPrefWidth(511.0);
        packageNameTextField.setPromptText("optional");

        hBox4.setAlignment(javafx.geometry.Pos.CENTER);
        hBox4.setPrefHeight(51.0);
        hBox4.setPrefWidth(580.0);

        convertBtn.setMnemonicParsing(false);
        convertBtn.setOnAction(this::handleConvert);
        convertBtn.setText("Convert");
        hBox4.setPadding(new Insets(6.0, 0.0, 0.0, 0.0));

        hBox5.setAlignment(javafx.geometry.Pos.CENTER);
        hBox5.setPrefHeight(100.0);
        hBox5.setPrefWidth(200.0);

        label4.setText("Implemented By Garawaa(garawaa@gmail.com)");
        setPadding(new Insets(10.0));

        hBox.getChildren().add(label);
        getChildren().add(hBox);
        getChildren().add(label0);
        hBox0.getChildren().add(inputTextField);
        hBox0.getChildren().add(inputBrowse);
        getChildren().add(hBox0);
        getChildren().add(label1);
        hBox1.getChildren().add(outputTextField);
        hBox1.getChildren().add(outputBrowse);
        getChildren().add(hBox1);
        getChildren().add(label2);
        hBox2.getChildren().add(classNameTextField);
        getChildren().add(hBox2);
        getChildren().add(label3);
        hBox3.getChildren().add(packageNameTextField);
        getChildren().add(hBox3);
        hBox4.getChildren().add(convertBtn);
        getChildren().add(hBox4);
        hBox5.getChildren().add(label4);
        getChildren().add(hBox5);

    }

    protected abstract void chooseInputFile(javafx.event.ActionEvent actionEvent);

    protected abstract void chooseOutputFile(javafx.event.ActionEvent actionEvent);

    protected abstract void handleConvert(javafx.event.ActionEvent actionEvent);

}
