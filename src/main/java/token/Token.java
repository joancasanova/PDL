package token;

public class Token {
    private TokenType tipo;
    private Object atributo;

    public Token(TokenType tipo, Object atributo) {
        this.tipo = tipo;
        this.atributo = atributo;
    }

    public Object getAtributo() {
        return this.atributo;
    }

    public void setAtributo(Object atributo) {
        this.atributo = atributo;
    }

    public void setTipo(TokenType tipo) {
        this.tipo = tipo;
    }

    // Getter para el tipo
    public TokenType getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "<" + tipo + ", " + atributo + ">";
    }
}
