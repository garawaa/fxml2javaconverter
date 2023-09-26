# fxml2javaconverter
Simple fxml to java code converter. You can drag a fxml file from file explorer to input text field or browse a fxml file and adjust some output path, class name, package name as you needed. Then click convert button. 
## Command line run
fxml2java.exe [path/to/fxmlfilename] 
or
java -jar fxml2java.jar [path/to/fxmlfilename]
# Main window screenshot
![Main](main.png)
# Intellij external tool setting
![Intellij](intellij-externaltool-setting.png)
After adding external tool setting you can convert a fxml file to java code from context menu in intellij ide.
# Usage
After generating a java base class, just create a java class by extending generated base class and use it as normal ui class. For example see it's source code.
