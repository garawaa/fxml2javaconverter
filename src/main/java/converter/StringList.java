package converter;

import java.util.ArrayList;

public class StringList extends ArrayList<Object> {
  public String toString(String indent) {
    if (!isEmpty()) {
      CodeBuilder codeBuilder = new CodeBuilder(new String[] { "", indent });
      forEach(object -> {
            String toValue = object.toString().trim();
            if (!toValue.isEmpty())
              codeBuilder.append(toValue);
            codeBuilder.appendWithoutIndent("\n");
          });
      codeBuilder.appendWithoutIndent("\n");
      return codeBuilder.toString();
    }
    return "";
  }

  public String toString() {
    return toString("");
  }
}
