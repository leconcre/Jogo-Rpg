@SuppressWarnings("java:S106")
public class Efeito {
    public enum Tipo { VENENO, QUEIMADURA, BUFF_ATAQUE, BUFF_DEFESA }

    private Tipo tipo;
    private String nome;
    private int valor;
    private int turnosRestantes;

    public Efeito(Tipo tipo, String nome, int valor, int turnos) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = valor;
        this.turnosRestantes = turnos;
    }

    public Tipo getTipo() { return tipo; }
    public String getNome() { return nome; }
    public int getValor() { return valor; }
    public int getTurnosRestantes() { return turnosRestantes; }

    public boolean isAtivo() { return turnosRestantes > 0; }

    public void aplicar(Personagem alvo) {
        if (!isAtivo()) return;

        switch (tipo) {
            case VENENO:
                alvo.receberDanoDireto(valor);
                System.out.println(">> " + alvo.getNome() + " sofre " + valor + " de dano por veneno!");
                break;
            case QUEIMADURA:
                alvo.receberDanoDireto(valor);
                System.out.println(">> " + alvo.getNome() + " sofre " + valor + " de dano por queimadura!");
                break;
            case BUFF_ATAQUE:
            case BUFF_DEFESA:
                break;
        }
        turnosRestantes--;
        if (turnosRestantes == 0) {
            System.out.println(">> O efeito de " + nome + " acabou em " + alvo.getNome() + ".");
        }
    }

    @Override
    public String toString() {
        return nome + " (" + turnosRestantes + " turnos)";
    }
}
