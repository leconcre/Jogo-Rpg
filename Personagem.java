import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S106")
public abstract class Personagem {
    protected String nome;
    protected int hp;
    protected int hpMax;
    protected int mana;
    protected int manaMax;
    protected int ataque;
    protected int defesa;
    protected int velocidade;
    protected List<Habilidade> habilidades;
    protected List<Efeito> efeitos;
    protected boolean vivo;

    public Personagem(String nome, int hp, int mana, int ataque, int defesa, int velocidade) {
        this.nome = nome;
        this.hp = hp;
        this.hpMax = hp;
        this.mana = mana;
        this.manaMax = mana;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        habilidades = new ArrayList<>();
        efeitos = new ArrayList<>();
        vivo = true;
    }

    public void receberDano(int dano) {
        int danoReal = Math.max(1, dano - defesa / 3);
        hp -= danoReal;
        if (hp <= 0) {
            hp = 0;
            vivo = false;
        }
        System.out.println(">> " + nome + " recebeu " + danoReal + " de dano!");
    }

    public void receberDanoDireto(int dano) {
        hp -= dano;
        if (hp <= 0) {
            hp = 0;
            vivo = false;
        }
    }

    public void curar(int quantidade) {
        hp = Math.min(hpMax, hp + quantidade);
    }

    public void recuperarMana(int quantidade) {
        mana = Math.min(manaMax, mana + quantidade);
    }

    public boolean gastarMana(int custo) {
        if (mana >= custo) {
            mana -= custo;
            return true;
        }
        return false;
    }

    public void adicionarHabilidade(Habilidade h) {
        habilidades.add(h);
    }

    public void adicionarEfeito(Efeito efeito) {
        efeitos.add(efeito);
        System.out.println(">> " + nome + " recebeu o efeito: " + efeito.getNome() + "!");
    }

    public void processarEfeitos() {
        for (int i = efeitos.size() - 1; i >= 0; i--) {
            Efeito efeito = efeitos.get(i);
            efeito.aplicar(this);
            if (!efeito.isAtivo()) {
                efeitos.remove(i);
            }
        }
    }

    public void limparEfeitos() {
        efeitos.clear();
    }

    public List<Efeito> getEfeitos() { return efeitos; }

    public String getNome() { return nome; }
    public int getHp() { return hp; }
    public int getHpMax() { return hpMax; }
    public int getMana() { return mana; }
    public int getManaMax() { return manaMax; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public int getVelocidade() { return velocidade; }
    public List<Habilidade> getHabilidades() { return habilidades; }
    public boolean isVivo() { return vivo; }

    public void mostrarStatus() {
        System.out.println("  " + nome);
        System.out.println("  HP: " + Util.barraVida(hp, hpMax, 20));
        System.out.println("  MP: " + Util.barraMana(mana, manaMax, 20));
        System.out.println("  ATK: " + ataque + " | DEF: " + defesa + " | VEL: " + velocidade);
    }
}
