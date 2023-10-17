public class Token {
    public TokenType tipo;
    public Object atributo;

    public Token(TokenType tipo, Object atributo) {
        this.tipo = tipo;
        this.atributo = atributo;
    }
    
    // Getter para el tipo
    public TokenType getTipo() {
        return tipo;
    }

    // Setter para el tipo
    public void setTipo(TokenType tipo) {
        this.tipo = tipo;
    }

    // Getter para el atributo
    public Object getAtributo() {
        return atributo;
    }

    // Setter para el atributo
    public void setAtributo(Object atributo) {
        this.atributo = atributo;
    }

    @Override
    public String toString() {
        return "<" + tipo + ", " + atributo + ">";
    }
}
