@SuppressWarnings("java:S106")
public class Jogador extends Personagem {
    public enum Classe { MAGO, GUERREIRO, ARQUEIRO, PALADINO }

    private Classe classe;
    private int nivel;
    private int xp;
    private int xpParaProximoNivel;
    private Inventario inventario;
    private Item armaEquipada;
    private Item armaduraEquipada;
    private int ataqueBase;
    private int defesaBase;
    private Estatisticas stats;

    public Jogador(String nome, Classe classe) {
        super(nome, 100, 50, 10, 5, 10);
        this.classe = classe;
        nivel = 1;
        xpParaProximoNivel = 100;
        inventario = new Inventario();
        stats = new Estatisticas();
        ataqueBase = ataque;
        defesaBase = defesa;

        aplicarBonusClasse();
        adicionarHabilidadesIniciais();

        inventario.adicionarItem(new Item("Pocao de Vida Menor", Item.Tipo.POCAO_VIDA, 30, 15, "Recupera 30 HP"));
        inventario.adicionarItem(new Item("Pocao de Vida Menor", Item.Tipo.POCAO_VIDA, 30, 15, "Recupera 30 HP"));
        inventario.adicionarItem(new Item("Pocao de Mana Menor", Item.Tipo.POCAO_MANA, 20, 12, "Recupera 20 MP"));
    }

    private void aplicarBonusClasse() {
        switch (classe) {
            case MAGO:
                mana += 50; manaMax += 50;
                ataque += 3;
                velocidade += 2;
                break;
            case GUERREIRO:
                hp += 50; hpMax += 50;
                ataque += 7;
                defesa += 5;
                break;
            case ARQUEIRO:
                ataque += 5;
                velocidade += 8;
                defesa += 2;
                break;
            case PALADINO:
                hp += 30; hpMax += 30;
                mana += 20; manaMax += 20;
                ataque += 4;
                defesa += 4;
                break;
        }
        ataqueBase = ataque;
        defesaBase = defesa;
    }

    private void adicionarHabilidadesIniciais() {
        adicionarHabilidade(new Habilidade("Ataque Basico", ataque, 0, 5, "Ataque corpo a corpo simples",
            ">> *SLASH!*"));

        switch (classe) {
            case MAGO:
                adicionarHabilidade(new Habilidade("Bola de Fogo", 25, 15, 8, "Dispara uma esfera flamejante",
                    ">> *FWOOOOSH!* Chamas consomem o inimigo!",
                    Efeito.Tipo.QUEIMADURA, 4, 2));
                adicionarHabilidade(new Habilidade("Raio Glacial", 18, 10, 6, "Dispara um raio congelante",
                    ">> *CRAAACK!* Cristais de gelo se formam!"));
                adicionarHabilidade(new Habilidade("Cura Arcana", 25, 20, 4, "Canaliza energia curativa", true,
                    ">> *brilho dourado envolve " + nome + "*"));
                break;
            case GUERREIRO:
                adicionarHabilidade(new Habilidade("Golpe Devastador", 22, 12, 9, "Golpe com forca total",
                    ">> *CRAAASH!* O chao treme!"));
                adicionarHabilidade(new Habilidade("Furia de Guerra", 30, 20, 11, "Furia descontrolada!",
                    ">> *RAAAAAWR!* Golpes furiosos!"));
                adicionarHabilidade(new Habilidade("Grito de Batalha", 15, 8, 3, "Restaura moral e forca (+ATK)", true,
                    ">> *HOOORAH!* " + nome + " se sente revigorado!",
                    Efeito.Tipo.BUFF_ATAQUE, 5, 3));
                break;
            case ARQUEIRO:
                adicionarHabilidade(new Habilidade("Flecha Rapida", 16, 5, 5, "Disparo veloz e preciso",
                    ">> *FWIIT!* Flecha certeira!"));
                adicionarHabilidade(new Habilidade("Chuva de Flechas", 28, 18, 10, "Flechas caem do ceu",
                    ">> *FWIIIT FWIIT FWIIT!* Flechas cobrem o campo!"));
                adicionarHabilidade(new Habilidade("Tiro Envenenado", 20, 12, 7, "Flecha com veneno letal",
                    ">> *tsss...* O veneno se espalha!",
                    Efeito.Tipo.VENENO, 5, 3));
                break;
            case PALADINO:
                adicionarHabilidade(new Habilidade("Golpe Sagrado", 20, 10, 7, "Ataque com luz divina",
                    ">> *FLASH!* Luz sagrada explode!"));
                adicionarHabilidade(new Habilidade("Escudo Divino", 0, 15, 4, "Escudo protetor de luz", true,
                    ">> *Um escudo dourado brilha ao redor de " + nome + "*"));
                adicionarHabilidade(new Habilidade("Julgamento Final", 35, 25, 13, "Punicao divina devastadora",
                    ">> *RAIOS DE LUZ DESCEM DO CEU!*"));
                break;
        }
    }

    public void ganharXP(int quantidade) {
        xp += quantidade;
        System.out.println("\n>> +" + quantidade + " XP!");
        while (xp >= xpParaProximoNivel) {
            subirNivel();
        }
    }

    private void subirNivel() {
        xp -= xpParaProximoNivel;
        nivel++;
        xpParaProximoNivel = (int)(xpParaProximoNivel * 1.5);

        int bonusHp = 15 + Util.rolar(10);
        int bonusMana = 8 + Util.rolar(5);
        int bonusAtk = 2 + Util.rolar(3);
        int bonusDef = 1 + Util.rolar(2);

        hpMax += bonusHp;
        hp = hpMax;
        manaMax += bonusMana;
        mana = manaMax;
        ataqueBase += bonusAtk;
        ataque = ataqueBase + (armaEquipada != null ? armaEquipada.getValor() : 0);
        defesaBase += bonusDef;
        defesa = defesaBase + (armaduraEquipada != null ? armaduraEquipada.getValor() : 0);

        System.out.println("\n+===================================+");
        System.out.println("|        SUBIU DE NIVEL!            |");
        System.out.println("|    " + nome + " agora e nivel " + nivel + "!       ");
        System.out.println("|  HP Max: +" + bonusHp + "  MP Max: +" + bonusMana);
        System.out.println("|  ATK: +" + bonusAtk + "  DEF: +" + bonusDef);
        System.out.println("+===================================+");
        Util.pausa(2000);

        if (nivel == 3) {
            Habilidade nova = getHabilidadeNivel3();
            if (nova != null) {
                adicionarHabilidade(nova);
                System.out.println(">> NOVA HABILIDADE DESBLOQUEADA: " + nova.getNome() + "!");
            }
        }
        if (nivel == 5) {
            Habilidade nova = getHabilidadeNivel5();
            if (nova != null) {
                adicionarHabilidade(nova);
                System.out.println(">> NOVA HABILIDADE DESBLOQUEADA: " + nova.getNome() + "!");
            }
        }
    }

    private Habilidade getHabilidadeNivel3() {
        switch (classe) {
            case MAGO: return new Habilidade("Meteoro", 40, 30, 12, "Invoca um meteoro flamejante!",
                ">> *O CEU ESCURECE... BOOOOOM!*");
            case GUERREIRO: return new Habilidade("Terremoto", 35, 25, 11, "Golpeia o chao com furia!",
                ">> *CRAAACK! O chao se parte!*");
            case ARQUEIRO: return new Habilidade("Flecha de Trovao", 32, 20, 9, "Flecha carregada com raio!",
                ">> *ZZZZAP! Relampago atinge o alvo!*");
            case PALADINO: return new Habilidade("Resurreicao", 50, 30, 5, "Cura poderosa com luz divina", true,
                ">> *Luz intensa cura todas as feridas!*");
            default: return null;
        }
    }

    private Habilidade getHabilidadeNivel5() {
        switch (classe) {
            case MAGO: return new Habilidade("Apocalipse Arcano", 60, 45, 14, "Poder arcano devastador!",
                ">> *TODA A REALIDADE TREME!*");
            case GUERREIRO: return new Habilidade("Execucao", 55, 35, 13, "Golpe final implacavel!",
                ">> *UM UNICO GOLPE PERFEITO!*");
            case ARQUEIRO: return new Habilidade("Tiro Perfurante", 50, 30, 11, "Perfura qualquer armadura!",
                ">> *A FLECHA ATRAVESSA TUDO!*");
            case PALADINO: return new Habilidade("Ira dos Deuses", 55, 40, 12, "Todo o poder divino concentrado!",
                ">> *OS CEUS SE ABREM!*");
            default: return null;
        }
    }

    public void equiparArma(Item arma) {
        if (armaEquipada != null) {
            ataque -= armaEquipada.getValor();
            inventario.adicionarItem(armaEquipada);
        }
        armaEquipada = arma;
        ataque = ataqueBase + arma.getValor();
    }

    public void equiparArmadura(Item armadura) {
        if (armaduraEquipada != null) {
            defesa -= armaduraEquipada.getValor();
            inventario.adicionarItem(armaduraEquipada);
        }
        armaduraEquipada = armadura;
        defesa = defesaBase + armadura.getValor();
    }

    public void descansar() {
        hp = hpMax;
        mana = manaMax;
        System.out.println(">> " + nome + " descansou e recuperou toda sua vida e mana!");
    }

    public void restaurarEstado(int nivel, int xp, int xpProx, int hp, int hpMax,
                                 int mana, int manaMax, int ataque, int defesa, int velocidade, Inventario inv) {
        this.nivel = nivel;
        this.xp = xp;
        this.xpParaProximoNivel = xpProx;
        this.hp = hp;
        this.hpMax = hpMax;
        this.mana = mana;
        this.manaMax = manaMax;
        this.ataque = ataque;
        this.ataqueBase = ataque;
        this.defesa = defesa;
        this.defesaBase = defesa;
        this.velocidade = velocidade;
        this.inventario = inv;
    }

    public Inventario getInventario() { return inventario; }
    public Estatisticas getStats() { return stats; }
    public Classe getClasse() { return classe; }
    public int getNivel() { return nivel; }
    public int getXp() { return xp; }
    public int getXpParaProximoNivel() { return xpParaProximoNivel; }

    public String getNomeClasse() {
        switch (classe) {
            case MAGO: return "Mago";
            case GUERREIRO: return "Guerreiro";
            case ARQUEIRO: return "Arqueiro";
            case PALADINO: return "Paladino";
            default: return "Desconhecido";
        }
    }

    @Override
    public void mostrarStatus() {
        System.out.println("\n+=========== " + nome + " ===========+");
        System.out.println("  Classe: " + getNomeClasse() + " | Nivel: " + nivel);
        System.out.println("  XP: " + xp + "/" + xpParaProximoNivel);
        System.out.println("  HP: " + Util.barraVida(hp, hpMax, 20));
        System.out.println("  MP: " + Util.barraMana(mana, manaMax, 20));
        System.out.println("  ATK: " + ataque + " | DEF: " + defesa + " | VEL: " + velocidade);
        if (armaEquipada != null) System.out.println("  Arma: " + armaEquipada.getNome());
        if (armaduraEquipada != null) System.out.println("  Armadura: " + armaduraEquipada.getNome());
        System.out.println("+================================+");
    }
}
