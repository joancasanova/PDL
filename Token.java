public class Token<E> {
    private String type;
    private E attribute;

    // Constructor
    public Token(String type, E attribute) {
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
    public E getAttribute() {
        return attribute;
    }

    // Setter para el atributo
    public void setAttribute(E attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "< "+ type +","+ attribute + " >";
    }
}
