@SuppressWarnings("java:S106")
public class Habilidade {
    private String nome;
    private int dano;
    private int custoMana;
    private int dificuldade;
    private String descricao;
    private boolean cura;
    private String efeitoVisual;
    private Efeito.Tipo tipoEfeito;
    private int valorEfeito;
    private int turnosEfeito;

    public Habilidade(String nome, int dano, int custoMana, int dificuldade, String descricao, String efeitoVisual) {
        this.nome = nome;
        this.dano = dano;
        this.custoMana = custoMana;
        this.dificuldade = dificuldade;
        this.descricao = descricao;
        this.cura = false;
        this.efeitoVisual = efeitoVisual;
    }

    public Habilidade(String nome, int dano, int custoMana, int dificuldade, String descricao, boolean cura, String efeitoVisual) {
        this(nome, dano, custoMana, dificuldade, descricao, efeitoVisual);
        this.cura = cura;
    }

    public Habilidade(String nome, int dano, int custoMana, int dificuldade, String descricao, String efeitoVisual,
                       Efeito.Tipo tipoEfeito, int valorEfeito, int turnosEfeito) {
        this(nome, dano, custoMana, dificuldade, descricao, efeitoVisual);
        this.tipoEfeito = tipoEfeito;
        this.valorEfeito = valorEfeito;
        this.turnosEfeito = turnosEfeito;
    }

    public Habilidade(String nome, int dano, int custoMana, int dificuldade, String descricao, boolean cura,
                       String efeitoVisual, Efeito.Tipo tipoEfeito, int valorEfeito, int turnosEfeito) {
        this(nome, dano, custoMana, dificuldade, descricao, efeitoVisual, tipoEfeito, valorEfeito, turnosEfeito);
        this.cura = cura;
    }

    public String getNome() { return nome; }
    public int getDano() { return dano; }
    public int getCustoMana() { return custoMana; }
    public int getDificuldade() { return dificuldade; }
    public String getDescricao() { return descricao; }
    public boolean isCura() { return cura; }
    public String getEfeitoVisual() { return efeitoVisual; }
    public boolean temEfeito() { return tipoEfeito != null; }
    public Efeito.Tipo getTipoEfeito() { return tipoEfeito; }
    public int getValorEfeito() { return valorEfeito; }
    public int getTurnosEfeito() { return turnosEfeito; }

    public void mostrarInfo() {
        System.out.println("  " + nome + " | Dano: " + dano + " | Mana: " + custoMana + " | " + descricao);
    }
}
