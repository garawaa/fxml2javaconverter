package converter;

public class CodeBuilder {
  private final StringBuilder stringBuilder;

  private final String indent;

  public CodeBuilder(String... string) {
    this.stringBuilder = new StringBuilder(string[0]);
    this.indent = (string.length > 1) ? string[1] : "";
  }

  public void append(Object object) {
    this.stringBuilder.append(this.indent);
    this.stringBuilder.append(object);
  }

  public void appendWithoutIndent(Object object) {
    this.stringBuilder.append(object);
  }

  public String getIndent() {
    return this.indent;
  }

  public String toString() {
    return this.stringBuilder.toString();
  }
}
