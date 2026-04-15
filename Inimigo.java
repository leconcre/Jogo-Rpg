@SuppressWarnings("java:S106")
public class Inimigo extends Personagem {
    private int xpRecompensa;
    private int ouroRecompensa;
    private String ascii;
    private boolean ehBoss;

    public Inimigo(String nome, int hp, int mana, int ataque, int defesa, int velocidade,
                   int xpRecompensa, int ouroRecompensa, String ascii, boolean ehBoss) {
        super(nome, hp, mana, ataque, defesa, velocidade);
        this.xpRecompensa = xpRecompensa;
        this.ouroRecompensa = ouroRecompensa;
        this.ascii = ascii;
        this.ehBoss = ehBoss;

        adicionarHabilidade(new Habilidade("Ataque", ataque, 0, 6, "Ataque basico",
            ">> " + nome + " ataca com furia!"));
    }

    public int getXpRecompensa() { return xpRecompensa; }
    public int getOuroRecompensa() { return ouroRecompensa; }
    public boolean isEhBoss() { return ehBoss; }

    public void ajustarStats(double multStats, double multRecompensa) {
        this.hp = (int)(this.hp * multStats);
        this.hpMax = (int)(this.hpMax * multStats);
        this.ataque = (int)(this.ataque * multStats);
        this.defesa = (int)(this.defesa * multStats);
        this.xpRecompensa = (int)(this.xpRecompensa * multRecompensa);
        this.ouroRecompensa = (int)(this.ouroRecompensa * multRecompensa);
    }

    public void mostrarAscii() {
        if (ehBoss) {
            System.out.println("\n  *** BOSS ***");
        }
        System.out.println(ascii);
    }

    public Habilidade escolherAcao() {
        int indice = Util.rolar(habilidades.size()) - 1;
        return habilidades.get(indice);
    }

    public static Inimigo criarGoblin(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo g = new Inimigo("Goblin", 30 + 10 * mult, 0, 6 + 2 * mult, 2 + mult, 8,
            25 + 10 * mult, 10 + 5 * mult,
            "        ,      ,\n" +
            "       /(.-\"\"-.)\\\n" +
            "   |\\  \\/      \\/  /|\n" +
            "   | \\ / =.  .= \\ / |\n" +
            "   \\( \\   o\\/o   / )/\n" +
            "    \\_, '-/  \\-' ,_/\n" +
            "      /   \\__/   \\\n" +
            "      \\ \\__/\\__/ /\n" +
            "       \\__    __/\n",
            false);
        g.adicionarHabilidade(new Habilidade("Facada Suja", 8 + 3 * mult, 0, 8, "Ataque traicoeiro",
            ">> O Goblin ataca pelas costas!"));
        return g;
    }

    public static Inimigo criarEsqueleto(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo e = new Inimigo("Esqueleto", 40 + 15 * mult, 10, 8 + 3 * mult, 5 + 2 * mult, 5,
            35 + 12 * mult, 15 + 8 * mult,
            "      .--.\n" +
            "     /    \\\n" +
            "    | o  o |\n" +
            "    |  \\/  |\n" +
            "     \\____/\n" +
            "      |  |\n" +
            "     /|  |\\\n" +
            "    / |  | \\\n" +
            "      |  |\n" +
            "     /    \\\n" +
            "    /      \\\n",
            false);
        e.adicionarHabilidade(new Habilidade("Golpe de Osso", 10 + 4 * mult, 0, 7, "Ataque com osso afiado",
            ">> O Esqueleto golpeia com seu osso!"));
        return e;
    }

    public static Inimigo criarLobo(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo l = new Inimigo("Lobo Sombrio", 35 + 12 * mult, 0, 10 + 3 * mult, 3 + mult, 12,
            30 + 10 * mult, 12 + 6 * mult,
            "     /\\_/\\\n" +
            "    ( o.o )\n" +
            "     > ^ <\n" +
            "    /|   |\\\n" +
            "   (_|   |_)\n",
            false);
        l.adicionarHabilidade(new Habilidade("Mordida Feroz", 12 + 4 * mult, 0, 6, "Mordida poderosa!",
            ">> O Lobo morde com ferocidade!"));
        return l;
    }

    public static Inimigo criarBruxa(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo b = new Inimigo("Bruxa das Sombras", 45 + 15 * mult, 40, 12 + 3 * mult, 4 + 2 * mult, 7,
            50 + 15 * mult, 25 + 10 * mult,
            "       ___\n" +
            "      /   \\\n" +
            "     / ~~~ \\\n" +
            "    |  o o  |\n" +
            "    |   ~   |\n" +
            "     \\_____/\n" +
            "    /|     |\\\n" +
            "   / |     | \\\n" +
            "      | | |\n",
            false);
        b.adicionarHabilidade(new Habilidade("Bola Sombria", 15 + 5 * mult, 8, 8, "Magia das trevas",
            ">> A Bruxa lanca uma esfera de escuridao!"));
        b.adicionarHabilidade(new Habilidade("Maldicao", 20 + 5 * mult, 15, 11, "Maldicao terrivel!",
            ">> A Bruxa sussurra palavras sombrias...",
            Efeito.Tipo.VENENO, 4, 3));
        return b;
    }

    public static Inimigo criarOrc(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo o = new Inimigo("Orc Brutal", 60 + 20 * mult, 5, 14 + 4 * mult, 8 + 3 * mult, 4,
            55 + 18 * mult, 30 + 12 * mult,
            "       .-\"\"\"-.\n" +
            "      /       \\\n" +
            "     | \\_ _/ |\n" +
            "     |  (o o)  |\n" +
            "      \\  ===  /\n" +
            "       '-----'\n" +
            "      /||   ||\\\n" +
            "     / ||   || \\\n",
            false);
        o.adicionarHabilidade(new Habilidade("Machado Brutal", 18 + 5 * mult, 0, 9, "Golpe de machado!",
            ">> O Orc desfere um golpe devastador com seu machado!"));
        return o;
    }

    public static Inimigo criarCavaleiroDasMortes(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo c = new Inimigo("Cavaleiro das Mortes", 80 + 25 * mult, 30, 16 + 5 * mult, 12 + 4 * mult, 6,
            70 + 20 * mult, 40 + 15 * mult,
            "      .---.\n" +
            "     / ___ \\\n" +
            "    | |   | |\n" +
            "    | | X | |\n" +
            "     \\|___|/\n" +
            "      |   |\n" +
            "    --|   |--\n" +
            "   /  |   |  \\\n" +
            "      |   |\n" +
            "     /     \\\n" +
            "    /_______\\\n",
            false);
        c.adicionarHabilidade(new Habilidade("Corte Sombrio", 20 + 6 * mult, 10, 8, "Espada amaldicoada!",
            ">> A espada brilha com energia sombria!"));
        c.adicionarHabilidade(new Habilidade("Aura da Morte", 25 + 7 * mult, 20, 12, "Aura devastadora!",
            ">> Uma onda de escuridao se espalha!"));
        return c;
    }

    public static Inimigo criarTroll(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo t = new Inimigo("Troll da Montanha", 120 + 30 * mult, 20, 18 + 5 * mult, 10 + 3 * mult, 3,
            100 + 30 * mult, 60 + 20 * mult,
            "          __\n" +
            "         /  \\\n" +
            "        | @@ |\n" +
            "        | \\/ |\n" +
            "         \\--/\n" +
            "        /|  |\\\n" +
            "       / |  | \\\n" +
            "      /  |  |  \\\n" +
            "         |  |\n" +
            "        /|  |\\\n" +
            "       / |  | \\\n" +
            "      /  ~~~~  \\\n" +
            "      [  BOSS  ]\n",
            true);
        t.adicionarHabilidade(new Habilidade("Pancada Esmagadora", 25 + 8 * mult, 0, 7, "Punho gigante!",
            ">> *BOOOOM!* O Troll esmaga o chao!"));
        t.adicionarHabilidade(new Habilidade("Regeneracao", 30, 15, 5, "Regenera sua carne", true,
            ">> O Troll se regenera!"));
        return t;
    }

    public static Inimigo criarDragao(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo d = new Inimigo("Dragao Anciao", 200 + 50 * mult, 60, 25 + 8 * mult, 15 + 5 * mult, 8,
            200 + 50 * mult, 100 + 40 * mult,
            "                 __        __\n" +
            "                /  \\      /  \\\n" +
            "               / /\\ \\    / /\\ \\\n" +
            "              / /  \\ \\__/ /  \\ \\\n" +
            "             / /    \\____/    \\ \\\n" +
            "            / /   |  (__)  |   \\ \\\n" +
            "           / /    |  \\  /  |    \\ \\\n" +
            "            \\      \\  \\/  /      /\n" +
            "             \\      '----'      /\n" +
            "              \\   /|      |\\   /\n" +
            "               \\_/ |      | \\_/\n" +
            "                   |      |\n" +
            "                  /________\\\n" +
            "              [ BOSS FINAL ]\n",
            true);
        d.adicionarHabilidade(new Habilidade("Sopro de Fogo", 35 + 10 * mult, 15, 7, "Chamas infernais!",
            ">> *FWOOOOOOOOOSH!* Chamas cobrem TUDO!",
            Efeito.Tipo.QUEIMADURA, 6, 2));
        d.adicionarHabilidade(new Habilidade("Golpe de Cauda", 20 + 6 * mult, 0, 5, "Cauda devastadora!",
            ">> *WHAM!* A cauda gigante golpeia!"));
        d.adicionarHabilidade(new Habilidade("Rugido Draconico", 30 + 8 * mult, 20, 10, "Rugido que abala a alma!",
            ">> *ROOOOOOAAAAAR!* O mundo treme!"));
        return d;
    }

    public static Inimigo criarLich(int nivelJogador) {
        int mult = nivelJogador;
        Inimigo l = new Inimigo("Lich Rei", 160 + 40 * mult, 80, 22 + 7 * mult, 12 + 4 * mult, 7,
            150 + 40 * mult, 80 + 30 * mult,
            "       .-\"\"\"-.     \n" +
            "      /       \\    \n" +
            "     |  X   X  |   \n" +
            "     |    A    |   \n" +
            "      \\_______/    \n" +
            "     /|| crown ||\\ \n" +
            "    / ||       || \\\n" +
            "      ||       ||  \n" +
            "      ||       ||  \n" +
            "    [  BOSS LICH  ]\n",
            true);
        l.adicionarHabilidade(new Habilidade("Raio da Morte", 30 + 9 * mult, 20, 9, "Energia necrotica pura!",
            ">> *Um raio verde perfura o ar!*"));
        l.adicionarHabilidade(new Habilidade("Invocar Mortos", 15 + 4 * mult, 10, 5, "Ossos se levantam!",
            ">> *Esqueletos emergem do chao!*"));
        l.adicionarHabilidade(new Habilidade("Drenar Vida", 25 + 7 * mult, 15, 8, "Rouba forca vital!", true,
            ">> *Energia vital e sugada!*"));
        return l;
    }

    public static Inimigo criarInimigoAleatorio(int nivelJogador, int zona) {
        int tipo;
        switch (zona) {
            case 1:
                tipo = Util.rolar(3);
                switch (tipo) {
                    case 1: return criarGoblin(nivelJogador);
                    case 2: return criarLobo(nivelJogador);
                    default: return criarEsqueleto(nivelJogador);
                }
            case 2:
                tipo = Util.rolar(3);
                switch (tipo) {
                    case 1: return criarOrc(nivelJogador);
                    case 2: return criarBruxa(nivelJogador);
                    default: return criarEsqueleto(nivelJogador);
                }
            case 3:
                tipo = Util.rolar(3);
                switch (tipo) {
                    case 1: return criarCavaleiroDasMortes(nivelJogador);
                    case 2: return criarBruxa(nivelJogador);
                    default: return criarOrc(nivelJogador);
                }
            default:
                return criarGoblin(nivelJogador);
        }
    }

    public static Inimigo criarBoss(int zona, int nivelJogador) {
        switch (zona) {
            case 1: return criarTroll(nivelJogador);
            case 2: return criarLich(nivelJogador);
            case 3: return criarDragao(nivelJogador);
            default: return criarTroll(nivelJogador);
        }
    }
}
