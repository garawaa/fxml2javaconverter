package converter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.ComboBoxBase;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
  public static Double[] getInsets(Node insets) {
    Node valueNode = insets.getAttributes().getNamedItem("top");
    double top = Double.valueOf((valueNode == null) ? "0.0" : valueNode.getNodeValue()).doubleValue();
    valueNode = insets.getAttributes().getNamedItem("right");
    double right = Double.valueOf((valueNode == null) ? "0.0" : valueNode.getNodeValue()).doubleValue();
    valueNode = insets.getAttributes().getNamedItem("bottom");
    double bottom = Double.valueOf((valueNode == null) ? "0.0" : valueNode.getNodeValue()).doubleValue();
    valueNode = insets.getAttributes().getNamedItem("left");
    double left = Double.valueOf((valueNode == null) ? "0.0" : valueNode.getNodeValue()).doubleValue();
    if (top == right && top == bottom && top == left)
      return new Double[] { Double.valueOf(top) };
    return new Double[] { Double.valueOf(top), Double.valueOf(right), Double.valueOf(bottom), Double.valueOf(left) };
  }

  public static String getVariableNameFor(StringList declarationList, Node node) {
    String newVariableName = node.getNodeName();
    newVariableName = Character.toLowerCase(newVariableName.charAt(0)) + newVariableName.substring(1);
    Node namedItem = node.getAttributes().getNamedItem("fx:id");
    if (namedItem != null)
      newVariableName = namedItem.getNodeValue();
    return getVariableNameFor(declarationList, newVariableName);
  }

  public static String getVariableNameFor(StringList declarationList, String className) {
    String newVariableName = className;
    for (Object object : declarationList) {
      DeclarationNode previousDeclaration = (DeclarationNode)object;
      String previousName = previousDeclaration.getNodeString(2).toString();
      if (previousName.equals(newVariableName)) {
        char last = previousName.charAt(previousName.length() - 1);
        if (Character.isDigit(last)) {
          int counter = Integer.valueOf(Character.toString(last));
          counter++;
          newVariableName = newVariableName.substring(0, newVariableName.length() - 1) + counter;
        } else {
          newVariableName = newVariableName + "0";
        }
      }
    }
    int dotIndex = newVariableName.lastIndexOf('.');
    dotIndex = (dotIndex < 0) ? 0 : (dotIndex + 1);
    return newVariableName.substring(dotIndex);
  }

  public static String arrayToValue(Object[] objects) {
    String arrayString = Arrays.toString(objects);
    return getValueFromAttribute(arrayString, "", "\\[", "\\]").toString();
  }

  public static Object getValueFromAttribute(String attributeString, Object defaultValue, String bracketOpen, String bracketClose) {
    Matcher matcher = Pattern.compile(bracketOpen + "(.*?)" + bracketClose).matcher(attributeString);
    return matcher.find() ? matcher.group(1) : defaultValue;
  }

  public static void parseChildrens(FXNode fXNode, String variableName, String dot) {
    StringList attributeList = fXNode.getAttributeList();
    StringList childList = fXNode.getChildList();
    NodeList childNodes = fXNode.getNode().getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node childNode = childNodes.item(i);
      if (childNode.getNodeType() == 1) {
        String newNodeName = childNode.getNodeName();
        String[] splits = newNodeName.split("\\.");
        if (splits.length > 1) {
          Node insets;
          Double[] inset;
          String insetString, methodName = splits[1];
          switch (methodName) {
            case "margin":
              methodName = "set" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
              insets = childNode.getChildNodes().item(1);
              inset = getInsets(insets);
              insetString = Arrays.toString((Object[])inset);
              insetString = "new Insets(" + insetString.substring(1, insetString.length() - 1) + ")";
              attributeList.add(splits[0] + "." + methodName + "(" + variableName + ", " + insetString + ");");
              break;
          }
        } else {
          NodeList styleSheets;
          int a;
          Node insets;
          Double[] inset;
          String insetString;
          Node selectedNode;
          Node cursorName;
          String nodeSelectedName;
          Node imageNode;
          Node image;
          String imageURL;
          String urlLocation;
          Node fontNode;
          String fontValue;
          String fontString;
          String listName;
          String listMethodName;
          NodeList childrens;
          ArrayList<String> points;
          int j;
          String value;
          String prefix;
          DeclarationNode declarationNode;
          String methodName;
          Node contentNode;
          NamedNodeMap namedNodeMap;
          String codeValue;
          Node codeNode;
          String nodeValue;
          int index;
          String attributeValue;
          String keyCodeImport;
          String keyCombinationImport;
          ArrayList<String> buttonTypes;
          int k;
          switch (newNodeName) {
            case "stylesheets":
              styleSheets = childNode.getChildNodes();
              for (a = 0; a < styleSheets.getLength(); a++) {
                Node styleSheet = styleSheets.item(a);
                if (styleSheet.getNodeType() == 1) {
                  Node node = styleSheet.getAttributes().getNamedItem("value");
                  String str = node.getNodeValue().replace("@", "");
                  str = "/" + MainClass.getPackageName().replace(".", "/") + "/" + str;
                  attributeList.add(variableName + dot + "getStylesheets().add(\"" + str + "\");");
                }
              }
              break;
            case "padding":
            case "opaqueInsets":
              newNodeName = "set" + Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              insets = childNode.getChildNodes().item(1);
              inset = getInsets(insets);
              insetString = "new Insets(" + arrayToValue((Object[])inset) + ")";
              attributeList.add(variableName + dot + newNodeName + "(" + insetString + ");");
              break;
            case "cursor":
            case "columnResizePolicy":
              newNodeName = "set" + Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              selectedNode = childNode.getChildNodes().item(1);
              cursorName = selectedNode.getAttributes().getNamedItem("fx:constant");
              nodeSelectedName = selectedNode.getNodeName();
              attributeList.add(variableName + dot + newNodeName + "(" + nodeSelectedName + "." + cursorName.getNodeValue() + ");");
              break;
            case "image":
              imageNode = childNode.getChildNodes().item(1);
              image = imageNode.getAttributes().getNamedItem("url");
              imageURL = image.getNodeValue().replace("@", "");
              urlLocation = "getClass().getResource(\"" + imageURL + "\").toExternalForm()";
              attributeList.add(variableName + dot + "setImage(new Image(" + urlLocation + "));");
              break;
            case "font":
              childNode = childNode.getChildNodes().item(1);
              fontNode = childNode.getAttributes().getNamedItem("name");
              fontValue = (fontNode == null) ? "" : ("\"" + fontNode.getNodeValue() + "\", ");
              fontNode = childNode.getAttributes().getNamedItem("size");
              fontValue = fontValue + ((fontNode == null) ? "14" : fontNode.getNodeValue());
              newNodeName = "set" + Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              fontString = "new Font(" + fontValue + ")";
              attributeList.add(variableName + dot + newNodeName + "(" + fontString + ");");
              break;
            case "points":
              listName = Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              listMethodName = "get" + listName + "()";
              childrens = childNode.getChildNodes();
              points = new ArrayList<>();
              for (j = 0; j < childrens.getLength(); j++) {
                Node children = childrens.item(j);
                if (children.getNodeType() == 1) {
                  String point = children.getAttributes().getNamedItem("fx:value").getNodeValue();
                  points.add(point);
                }
              }
              value = arrayToValue(points.toArray());
              prefix = variableName + dot + listMethodName;
              declarationNode = new DeclarationNode("{0}{1}({2});", new Object[] { prefix, ".addAll", value });
              childList.add(declarationNode);
              break;
            case "accelerator":
              methodName = Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              methodName = "set" + methodName;
              contentNode = childNode.getChildNodes().item(1);
              namedNodeMap = contentNode.getAttributes();
              codeValue = "";
              codeNode = namedNodeMap.getNamedItem("code");
              nodeValue = codeNode.getNodeValue();
              codeValue = codeValue + "KeyCode." + nodeValue;
              for (index = 0; index < namedNodeMap.getLength(); index++) {
                Node item = namedNodeMap.item(index);
                String nodeName = item.getNodeName();
                nodeValue = item.getNodeValue();
                if (!nodeName.equals("code"))
                  codeValue = codeValue + (nodeValue.equals("UP") ? "" : (", KeyCombination." + nodeName.toUpperCase() + "_" + nodeValue));
              }
              attributeValue = "new KeyCodeCombination(" + codeValue + ")";
              prefix = variableName + dot + methodName;
              declarationNode = new DeclarationNode("{0}({1});", new Object[] { prefix, attributeValue });
              attributeList.add(declarationNode);
              keyCodeImport = "javafx.scene.input.KeyCode";
              keyCombinationImport = "javafx.scene.input.KeyCombination";
              if (!MainClass.containsImport(keyCodeImport))
                MainClass.getImport().add(new DeclarationNode("{0} {1}{2}", new Object[] { "import", keyCodeImport, ";" }));
              if (!MainClass.containsImport(keyCombinationImport))
                MainClass.getImport().add(new DeclarationNode("{0} {1}{2}", new Object[] { "import", keyCombinationImport, ";" }));
              break;
            case "effect":
            case "contextMenu":
            case "graphic":
            case "topInput":
            case "tooltip":
            case "bottomInput":
            case "input":
            case "content":
            case "top":
            case "right":
            case "center":
            case "bottom":
            case "left":
            case "side":
            case "header":
            case "toggleGroup":
            case "expandableContent":
              methodName = Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              methodName = "set" + methodName;
              contentNode = childNode.getChildNodes().item(1);
              fXNode = new FXNode(contentNode, false);
              if (fXNode.isDeclared()) {
                attributeList.addAll(fXNode.getAttributeList());
                childList.addAll(fXNode.getChildList());
                prefix = variableName + dot + methodName;
                declarationNode = new DeclarationNode("{0}({1});", new Object[] { prefix, fXNode });
                attributeList.add(declarationNode);
              }
              break;
            case "buttonTypes":
              listName = Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              listMethodName = "get" + listName + "()";
              childrens = childNode.getChildNodes();
              buttonTypes = new ArrayList<>();
              for (k = 0; k < childrens.getLength(); k++) {
                Node children = childrens.item(k);
                if (children.getNodeType() == 1) {
                  Node fxValue = children.getAttributes().getNamedItem("fx:constant");
                  buttonTypes.add(children.getNodeName() + "." + fxValue.getNodeValue());
                }
              }
              prefix = variableName + dot + listMethodName;
              declarationNode = new DeclarationNode("{0}{1}({2});", new Object[] { prefix, ".addAll", arrayToValue(buttonTypes.toArray()) });
              attributeList.add(declarationNode);
              break;
            case "children":
            case "buttons":
            case "elements":
            case "tabs":
            case "menus":
            case "items":
            case "columns":
            case "panes":
            case "xAxis":
            case "yAxis":
            case "root":
            case "columnConstraints":
            case "rowConstraints":
              listName = Character.toUpperCase(newNodeName.charAt(0)) + newNodeName.substring(1);
              listMethodName = "get" + listName + "()";
              childrens = childNode.getChildNodes();
              for (k = 0; k < childrens.getLength(); k++) {
                Node children = childrens.item(k);
                if (children.getNodeType() == 1) {
                  Node source;
                  String includeName, nodeName = children.getNodeName();
                  switch (nodeName) {
                    case "fx:include":
                      source = children.getAttributes().getNamedItem("source");
                      includeName = source.getNodeValue();
                      includeName = includeName.substring(0, includeName.lastIndexOf('.'));
                      if (includeName != null && !includeName.isEmpty()) {
                        String includeNode = createInclude(includeName);
                        prefix = variableName + dot + listMethodName;
                        declarationNode = new DeclarationNode("{0}{1}({2});", new Object[] { prefix, ".add", includeNode });
                        childList.add(declarationNode);
                      }
                      break;
                    default:
                      fXNode = new FXNode(children, false);
                      if (fXNode.isDeclared()) {
                        attributeList.addAll(fXNode.getAttributeList());
                        childList.addAll(fXNode.getChildList());
                        prefix = variableName + dot + listMethodName;
                        declarationNode = new DeclarationNode("{0}{1}({2});", new Object[] { prefix, ".add", fXNode });
                        childList.add(declarationNode);
                      }
                      break;
                  }
                }
              }
              break;
          }
        }
      }
    }
  }

  public static String createInclude(String nodeName) {
    StringList declarationList = MainClass.getDeclarationList();
    String variableName = Character.toLowerCase(nodeName.charAt(0)) + nodeName.substring(1);
    variableName = getVariableNameFor(declarationList, variableName);
    DeclarationNode declarationNode = new DeclarationNode("{0} {1} {2}{3}", new Object[] { Modifier.toString(MainClass.getNodeModifier()), nodeName, variableName, ";" });
    declarationList.add(declarationNode);
    MainClass.getInitList().add(variableName + " = new " + nodeName + "();");
    return variableName;
  }

  public static Class<?> getFullClass(String className) {
    for (Object object : MainClass.getImport()) {
      DeclarationNode declarationNode = (DeclarationNode)object;
      String pName = declarationNode.getNodeString(1).toString();
      if (pName.endsWith(".*")) {
        pName = pName.substring(0, pName.length() - 1);
      } else {
        int len = pName.length() - className.length();
        pName = (len > 0) ? pName.substring(0, len) : pName;
      }
      Class<?> classFromName = getClassFromName(pName + className);
      if (classFromName != null)
        return classFromName;
    }
    return Node.class;
  }

  private static Class<?> getClassFromName(String fullName) {
    try {
      Class<?> loadClass = Class.forName(fullName, false, ClassLoader.getSystemClassLoader());
      return loadClass;
    } catch (Exception ex) {
      return null;
    }
  }

  public static void setAttributes(FXNode fXNode, String variableName, String dot) {
    Class<?> nodeClass = fXNode.getNodeClass();
    StringList attributeList = fXNode.getAttributeList();
    fXNode.getAttributeList();
    if (nodeClass != null) {
      NamedNodeMap attributes = fXNode.getNode().getAttributes();
      Method[] methods = nodeClass.getMethods();
      for (int index = 0; index < attributes.getLength(); index++) {
        Node attributeNode = attributes.item(index);
        String methodNodeName = attributeNode.getNodeName();
        String[] splits = methodNodeName.split("\\.");
        if (splits.length > 1) {
          Class<?> fullClass = getFullClass(splits[0]);
          Method[] staticClassMethods = fullClass.getMethods();
          methodNodeName = splits[1];
          methodNodeName = "set" + Character.toUpperCase(methodNodeName.charAt(0)) + methodNodeName.substring(1);
          for (Method staticClassMethod : staticClassMethods) {
            if (staticClassMethod.getName().equals(methodNodeName)) {
              Class<?>[] parameterTypes = staticClassMethod.getParameterTypes();
              Class<?>[] arrayOfClass1 = parameterTypes;
              int i = arrayOfClass1.length;
              byte b = 0;
              if (b < i) {
                Class<?> parameterType = arrayOfClass1[b];
                if (parameterType.isEnum()) {
                  attributeList.add(splits[0] + "." + methodNodeName + "(" + variableName + ", " + parameterType

                      .getName() + "." + attributeNode
                      .getNodeValue() + ");");
                  break;
                }
                String simpleName = attributeNode.getNodeValue();
                String attribute = splits[0] + "." + methodNodeName;
                if (Character.isUpperCase(simpleName.codePointAt(0))) {
                  Type[] types = staticClassMethod.getGenericParameterTypes();
                  String eventName = types[1].getTypeName().replace("$", ".");
                  attribute = attribute + "(" + variableName + ", " + eventName + "." + attributeNode.getNodeValue() + ");";
                } else {
                  attribute = attribute + "(" + variableName + ", " + attributeNode.getNodeValue() + ");";
                }
                attributeList.add(attribute);
                break;
              }
            }
          }
        } else {
          String nodeValue;
          String location;
          switch (methodNodeName) {
            case "styleClass":
              attributeList.add(variableName + dot + "getStyleClass().add(\"" + attributeNode.getNodeValue() + "\");");
              break;
            case "stylesheets":
              nodeValue = attributeNode.getNodeValue().replace("@", "");
              nodeValue = "/" + MainClass.getPackageName().replace(".", "/") + "/" + nodeValue;
              location = "\"" + nodeValue + "\"";
              attributeList.add(variableName + dot + "getStylesheets().add(" + location + ");");
              break;
            default:
              methodNodeName = "set" + Character.toUpperCase(methodNodeName.charAt(0)) + methodNodeName.substring(1);
              for (Method method : methods) {
                if (method.getName().equals(methodNodeName)) {
                  addAttribute(variableName + dot, method, attributeNode, methodNodeName, attributeList);
                  break;
                }
              }
              break;
          }
        }
      }
    } else {
      throw new NullPointerException("Class not found");
    }
  }

  public static void addAttribute(String prefix, Method method, Node node, String nodeName, StringList attributeList) {
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (Class<?> parameterType : parameterTypes) {
      Type[] types;
      int start, end;
      String splits[], variableName, methodName, simpleName = parameterType.getSimpleName();
      String value = node.getNodeValue();
      String eventName = "";
      switch (simpleName) {
        case "String":
          value = value.replace("\"", "'");
          nodeName = nodeName + "(\"" + value + "\");";
          attributeList.add(prefix + nodeName);
          break;
        case "ToggleGroup":
          value = value.substring(1);
          nodeName = nodeName + "(" + value + ");";
          attributeList.add(prefix + nodeName);
          break;
        case "EventHandler":
          types = method.getGenericParameterTypes();
          eventName = types[0].getTypeName();
          start = eventName.indexOf("<") + 1;
          end = eventName.indexOf(">");
          eventName = eventName.substring(start, end);
          splits = eventName.split(" ");
          eventName = splits[splits.length - 1];
          if (method.getDeclaringClass() == ComboBoxBase.class && eventName
            .equals("javafx.event.ActionEvent"))
            eventName = "javafx.event.Event";
          variableName = eventName.substring(eventName.lastIndexOf('.') + 1);
          variableName = Character.toLowerCase(variableName.charAt(0)) + variableName.substring(1);
          methodName = node.getNodeValue().substring(1);
          MainClass.addMethod(methodName, eventName, variableName);
          attributeList.add(prefix + nodeName + "(this::" + methodName + ");");
          break;
        default:
          if (value.equals("-Infinity"))
            value = "USE_PREF_SIZE";
          if (value.equals("1.7976931348623157E308"))
            value = "Double.MAX_VALUE";
          if (Character.isUpperCase(simpleName.codePointAt(0))) {
            types = method.getGenericParameterTypes();
            eventName = types[0].getTypeName().replace("$", ".");
            if (eventName.endsWith(".Paint")) {
              eventName = eventName.replace(".Paint", ".Color");
              if (value.startsWith("#"))
                value = "valueOf(\"" + value + "\")";
            }
            eventName = eventName + ".";
          }
          nodeName = nodeName + "(" + eventName + value + ");";
          attributeList.add(prefix + nodeName);
          break;
      }
    }
  }
}
