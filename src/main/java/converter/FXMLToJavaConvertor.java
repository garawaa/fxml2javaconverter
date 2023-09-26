package converter;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FXMLToJavaConvertor {
  private final DocumentBuilderFactory documentBuilderFactory;

  private final DocumentBuilder documentBuilder;

  private Document document;

  private Exception exception;

  public FXMLToJavaConvertor() throws ParserConfigurationException {
    this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
    this.documentBuilderFactory.setNamespaceAware(true);
    this.documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
  }

  private boolean convert(MainClass mainClass, Document document) {
    NodeList childNodes = document.getChildNodes();
    for (int index = 0; index < childNodes.getLength(); index++) {
      Node item = childNodes.item(index);
      switch (item.getNodeType()) {
        case 7:
          MainClass.getImport().add(new DeclarationNode("{0} {1}{2}", new Object[] { "import", item.getNodeValue(), ";" }));
          break;
        case 1:
          mainClass.setRootNode(item);
          break;
      }
    }
    return true;
  }

  public boolean convert(MainClass mainClass, InputStream inputStream) {
    try {
      this.document = this.documentBuilder.parse(new InputSource(new InputStreamReader(inputStream, "UTF-8")));
      return convert(mainClass, this.document);
    } catch (SAXException|java.io.IOException ex) {
      this.exception = ex;
      return false;
    }
  }

  public boolean convert(MainClass mainClass, File file) {
    try {
      this.document = this.documentBuilder.parse(file);
      return convert(mainClass, this.document);
    } catch (SAXException|java.io.IOException ex) {
      this.exception = ex;
      return false;
    }
  }

  public boolean convert(MainClass mainClass, String uri) {
    try {
      this.document = this.documentBuilder.parse(uri);
      return convert(mainClass, this.document);
    } catch (SAXException|java.io.IOException ex) {
      this.exception = ex;
      return false;
    }
  }

  public Exception getException() {
    return this.exception;
  }
}
