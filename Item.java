public class Item {
    public enum Tipo { POCAO_VIDA, POCAO_MANA, ARMA, ARMADURA, ESPECIAL }

    private String nome;
    private Tipo tipo;
    private int valor;
    private int preco;
    private String descricao;

    public Item(String nome, Tipo tipo, int valor, int preco, String descricao) {
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.preco = preco;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public Tipo getTipo() { return tipo; }
    public int getValor() { return valor; }
    public int getPreco() { return preco; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return nome + " - " + descricao + " (Valor: " + valor + ")";
    }
}
