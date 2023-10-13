public class Token {
    private String type;
    private String attribute;

    // Constructor
    public Token(String type, String attribute) {
        this.type = type;
        this.attribute = attribute;
    }

    // Getter para el tipo
    public String getType() {
        return type;
    }

    // Setter para el tipo
    public void setType(String type) {
        this.type = type;
    }

    // Getter para el atributo
    public String getAttribute() {
        return attribute;
    }

    // Setter para el atributo
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", attribute='" + attribute + '\'' +
                '}';
    }
}
