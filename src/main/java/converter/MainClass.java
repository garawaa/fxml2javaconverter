package converter;

import java.lang.reflect.Modifier;
import javafx.scene.Parent;
import org.w3c.dom.Node;

public class MainClass {
  private Class<?> extendedClass;

  private CodeBuilder mainBuilder;

  private static StringList methodList;

  private static StringList initList;

  private static StringList importList;

  private static StringList declarationList;

  private final StringList childList;

  private final StringList attributeList;

  private final String className;

  private static String packageName;

  private static int classModifier;

  private static int nodeModifier;

  private static int methodModifier;

  private static OnIncludeListener includeListener;

  public MainClass(String className, String packageName) {
    this.className = className;
    MainClass.packageName = packageName;
    importList = new StringList();
    initList = new StringList();
    this.attributeList = new StringList();
    this.childList = new StringList();
    methodList = new StringList();
    declarationList = new StringList();
    classModifier = 1;
    nodeModifier = 18;
    methodModifier = 2;
  }

  public void setClassModifiers(int... modifiers) {
    classModifier = 0;
    for (int modifier : modifiers)
      classModifier += modifier;
  }

  public void setNodeModifiers(int... modifiers) {
    nodeModifier = 0;
    for (int modifier : modifiers)
      nodeModifier += modifier;
  }

  public void setMethodModifiers(int... modifiers) {
    methodModifier = 0;
    for (int modifier : modifiers)
      methodModifier += modifier;
  }

  public static int getClassModifier() {
    return classModifier;
  }

  public static int getNodeModifier() {
    return nodeModifier;
  }

  public static int getMethodModifier() {
    return methodModifier;
  }

  private CodeBuilder createClass() {
    CodeBuilder classBuilder = new CodeBuilder(new String[] { Modifier.toString(classModifier) + " class " + this.className, "    " });
    String extendedClassName = (this.extendedClass == null) ? " {\n\n" : (" extends " + this.extendedClass.getSimpleName() + " {\n\n");
    classBuilder.appendWithoutIndent(extendedClassName);
    classBuilder.appendWithoutIndent(declarationList.toString(classBuilder.getIndent()));
    classBuilder.append(createConstructor(classBuilder.getIndent()));
    classBuilder.append("}\n");
    return classBuilder;
  }

  private CodeBuilder createConstructor(String indent) {
    String consModifier = "public";
    if (Modifier.isPrivate(classModifier))
      consModifier = "private";
    CodeBuilder constructorBuilder = new CodeBuilder(new String[] { consModifier + " " + this.className + "() {\n\n", indent + "    " });
    constructorBuilder.appendWithoutIndent(initList.toString(constructorBuilder.getIndent()));
    constructorBuilder.appendWithoutIndent(this.attributeList.toString(constructorBuilder.getIndent()));
    constructorBuilder.appendWithoutIndent(this.childList.toString(constructorBuilder.getIndent()));
    return constructorBuilder;
  }

  public static boolean containsImport(String toMatch) {
    return importList.stream().anyMatch(t -> {
          DeclarationNode importNode = (DeclarationNode)t;
          return importNode.getNodeString(1).equals(toMatch);
        });
  }

  public static StringList getDeclarationList() {
    return declarationList;
  }

  public static StringList getInitList() {
    return initList;
  }

  public StringList getAttributeList() {
    return this.attributeList;
  }

  public static String getPackageName() {
    return packageName;
  }

  public String toString() {
    String packageValue = (packageName == null || packageName.isEmpty()) ? "\n" : ("package " + packageName + ";\n\n");
    this.mainBuilder = new CodeBuilder(new String[] { packageValue });
    this.mainBuilder.append(importList.toString());
    this.mainBuilder.append(createClass());
    this.mainBuilder.append(methodList.toString("    "));
    this.mainBuilder.append("}\n");
    return this.mainBuilder.toString();
  }

  public static void addMethod(String methodName, String eventName, String variableName) {
    methodList.add("");
    boolean isAbstract = Modifier.isAbstract(methodModifier);
    String newMethod = Modifier.toString(methodModifier) + " void " + methodName + "(" + eventName + " " + variableName + ")" + (isAbstract ? ";" : " {");
    if (!methodList.contains(newMethod)) {
      methodList.add(newMethod);
      if (!isAbstract) {
        methodList.add("    //TODO");
        methodList.add("}");
      }
      if (!Modifier.isAbstract(classModifier))
        classModifier += 1024;
    }
  }

  public static StringList getImport() {
    return importList;
  }

  public void setRootNode(Node node) {
    FXNode fXNode = new FXNode(node, true);
    this.childList.addAll(filterList(fXNode.getChildList()));
    this.attributeList.addAll(filterList(fXNode.getAttributeList()));
    if (fXNode.getShortestParameterCount() == 0 && !fXNode.isFinal()) {
      this.extendedClass = fXNode.getNodeClass();
    } else {
      this.extendedClass = Parent.class;
      String parentImport = "javafx.scene.Parent";
      if (!containsImport(parentImport))
        importList.add(new DeclarationNode("{0} {1}{2}", new Object[] { "import", parentImport, ";" }));
      this.childList.add("getChildren().add(" + fXNode.getVariableName() + ");");
    }
  }

  private StringList filterList(StringList stringList) {
    if (stringList.isEmpty())
      return stringList;
    if (stringList.get(0).equals(""))
      stringList.remove(0);
    for (int i = 0; i < stringList.size(); i++) {
      if (stringList.get(i).equals("")) {
        int next = i + 1;
        if (next < stringList.size() &&
          stringList.get(next).equals(""))
          stringList.remove(next);
      }
    }
    int last = stringList.size() - 1;
    if (last > -1 && stringList.get(last).equals(""))
      stringList.remove(last);
    return stringList;
  }

  public void setIncludeListener(OnIncludeListener includeListener) {
    MainClass.includeListener = includeListener;
  }

  public static String getIncludeName(String fxmlFileName) {
    return (includeListener == null) ? null : includeListener.getIncludeExists(fxmlFileName);
  }

  public static interface OnIncludeListener {
    String getIncludeExists(String param1String);
  }
}
