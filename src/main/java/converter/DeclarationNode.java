package converter;

import java.text.MessageFormat;

public class DeclarationNode {
  private final Object[] objects;

  private final MessageFormat messageFormat;

  public DeclarationNode(String format, Object... nodeStrins) {
    this.messageFormat = new MessageFormat(format);
    this.objects = nodeStrins;
  }

  public Object getNodeString(int index) {
    Object object = this.objects[index];
    return (object == null) ? "" : object;
  }

  public String toString() {
    return this.messageFormat.format(this.objects);
  }
}
