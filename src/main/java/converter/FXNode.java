package converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.w3c.dom.Node;

public class FXNode {
  private final StringList attributeList;

  private final StringList childList;

  private final String nodeName;

  private String variableName;

  private final Class<?> nodeClass;

  private final Node node;

  private final boolean declared;

  private Constructor shortestConstructor;

  private int shortesetParameterCount;

  private boolean isFinal;

  public FXNode(Node node, boolean isRoot) {
    this.node = node;
    this.attributeList = new StringList();
    this.childList = new StringList();
    String elementName = node.getNodeName();
    elementName = elementName.equals("fx:root") ? node.getAttributes().getNamedItem("type").getNodeValue() : elementName;
    this.nodeName = elementName;
    this.nodeClass = Utils.getFullClass(this.nodeName);
    StringList declarationList = MainClass.getDeclarationList();
    this.isFinal = Modifier.isFinal(this.nodeClass.getModifiers());
    this.shortestConstructor = getShortestConstructor(this.nodeClass.getConstructors());
    this.shortesetParameterCount = (this.shortestConstructor == null) ? 0 : this.shortestConstructor.getParameterCount();
    isRoot = this.isFinal ? (!this.isFinal) : isRoot;
    this.variableName = (isRoot && this.shortesetParameterCount == 0) ? "" : Utils.getVariableNameFor(declarationList, node);
    String dot = (isRoot && this.shortesetParameterCount == 0) ? "" : ".";
    DeclarationNode declarationNode = new DeclarationNode("{0} {1} {2}{3}", new Object[] { Modifier.toString(MainClass.getNodeModifier()), this.nodeName, this.variableName, ";" });
    if (this.shortesetParameterCount == 0) {
      if (isRoot) {
        Utils.setAttributes(this, this.variableName, dot);
        Utils.parseChildrens(this, this.variableName, dot);
      } else {
        declarationList.add(declarationNode);
        MainClass.getInitList().add(this.variableName + " = new " + this.nodeName + "();");
        this.attributeList.add("");
        Utils.setAttributes(this, this.variableName, dot);
        Utils.parseChildrens(this, this.variableName, dot);
      }
      this.declared = true;
    } else {
      Object arg0, arg1, arg2;
      String widthString, heightString, arguments;
      switch (this.nodeName) {
        case "SubScene":
          arg0 = null;
          arg1 = Integer.valueOf(0);
          arg2 = Integer.valueOf(0);
          Utils.parseChildrens(this, this.variableName, dot);
          Utils.setAttributes(this, this.variableName, dot);
          declarationList.add(declarationNode);
          if (!this.childList.isEmpty()) {
            DeclarationNode childNode = (DeclarationNode)this.childList.get(0);
            arg0 = childNode.getNodeString(2);
          }
          widthString = this.variableName + ".setWidth";
          heightString = this.variableName + ".setHeight";
          for (Object attribute : this.attributeList) {
            String attributeString = attribute.toString();
            if (attributeString.startsWith(widthString)) {
              arg1 = Utils.getValueFromAttribute(attributeString, Integer.valueOf(100), "\\(", "\\)");
              continue;
            }
            if (attributeString.startsWith(heightString))
              arg2 = Utils.getValueFromAttribute(attributeString, Integer.valueOf(100), "\\(", "\\)");
          }
          this.attributeList.removeIf(object ->
              (object.toString().startsWith(widthString) || object.toString().startsWith(heightString)));
          arguments = arg0 + ", " + arg1 + ", " + arg2;
          MainClass.getInitList().add(this.variableName + " = new " + this.nodeName + "(" + arguments + ");");
          this.childList.clear();
          this.declared = true;
          return;
        case "AreaChart":
        case "BarChart":
        case "BubbleChart":
        case "LineChart":
        case "ScatterChart":
        case "StackedAreaChart":
        case "StackedBarChart":
          arg1 = null;
          Utils.parseChildrens(this, this.variableName, dot);
          Utils.setAttributes(this, this.variableName, dot);
          declarationList.add(declarationNode);
          if (!this.childList.isEmpty()) {
            DeclarationNode childNode = (DeclarationNode)this.childList.get(0);
            arg0 = childNode.getNodeString(2);
            if (this.childList.size() > 1) {
              childNode = (DeclarationNode)this.childList.get(1);
              arg1 = childNode.getNodeString(2);
            }
            arguments = arg0 + ", " + arg1;
            MainClass.getInitList().add(this.variableName + " = new " + this.nodeName + "(" + arguments + ");");
          }
          this.childList.clear();
          this.declared = true;
          return;
      }
      this.declared = false;
    }
  }

  public boolean isFinal() {
    return this.isFinal;
  }

  public int getShortestParameterCount() {
    return this.shortesetParameterCount;
  }

  public Constructor getShortestConstructor() {
    return this.shortestConstructor;
  }

  private Constructor getShortestConstructor(Constructor<?>[] constructors) {
    Constructor<?> shortest = (constructors.length > 0) ? constructors[0] : null;
    for (Constructor<?> constructor : constructors) {
      if (constructor.getParameterCount() < shortest.getParameterCount())
        shortest = constructor;
    }
    return shortest;
  }

  public boolean isDeclared() {
    return this.declared;
  }

  public Node getNode() {
    return this.node;
  }

  public String getNodeName() {
    return this.nodeName;
  }

  public String getVariableName() {
    return this.variableName;
  }

  public Class<?> getNodeClass() {
    return this.nodeClass;
  }

  public StringList getChildList() {
    return this.childList;
  }

  public String toString() {
    return this.variableName;
  }

  public StringList getAttributeList() {
    return this.attributeList;
  }
}
