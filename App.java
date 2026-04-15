import java.util.Scanner;

@SuppressWarnings("java:S106")
public class App {
    private static Scanner scn = new Scanner(System.in);
    private static Jogador jogador;
    private static Mapa mapa;
    private static int dificuldade = 2; // 1=Facil, 2=Normal, 3=Dificil

    public static void main(String[] args) {
        mostrarTitulo();

        if (SalvarJogo.existeSave()) {
            System.out.println("\n>> Save encontrado! Deseja carregar? [1] Sim [2] Novo Jogo");
            if (lerEscolha(1, 2) == 1) {
                Object[] dados = SalvarJogo.carregar();
                if (dados != null) {
                    jogador = (Jogador) dados[0];
                    mapa = (Mapa) dados[1];
                } else {
                    criarPersonagem();
                    mapa = new Mapa();
                }
            } else {
                criarPersonagem();
                mapa = new Mapa();
            }
        } else {
            criarPersonagem();
            mapa = new Mapa();
        }
        mapa.mostrarAsciiZona();

        Util.digitarLento("\nSua jornada em Lipidia comeca agora, " + jogador.getNome() + "!");
        Util.digitarLento("Explore, lute, fique forte e derrote o Dragao Anciao!");
        Util.pausa(2000);

        boolean jogando = true;
        boolean vitoriaFinal = false;

        while (jogando && jogador.isVivo()) {
            int escolha = menuPrincipal();

            switch (escolha) {
                case 1:
                    jogando = explorar();
                    break;
                case 2:
                    if (mapa.bossDisponivel()) {
                        boolean venceuBoss = lutarBoss();
                        if (!jogador.isVivo()) {
                            jogando = false;
                        } else if (venceuBoss) {
                            if (mapa.getZonaAtual() == 3) {
                                vitoriaFinal = true;
                                jogando = false;
                            } else {
                                mapa.avancarZona();
                            }
                        }
                    } else {
                        System.out.println(">> Voce precisa de mais " +
                            (3 + mapa.getZonaAtual() - contarBatalhas()) +
                            " batalhas antes de enfrentar o boss!");
                        System.out.println(">> Explore mais a " + mapa.getNomeZona() + "!");
                    }
                    break;
                case 3:
                    Loja loja = new Loja(mapa.getZonaAtual(), scn);
                    loja.abrir(jogador);
                    break;
                case 4:
                    jogador.getInventario().mostrar();
                    menuUsarItem();
                    break;
                case 5:
                    jogador.mostrarStatus();
                    break;
                case 6:
                    mapa.mostrarMapa();
                    break;
                case 7:
                    descansar();
                    break;
                case 8:
                    SalvarJogo.salvar(jogador, mapa);
                    break;
                case 9:
                    System.out.println("\n>> Tem certeza que deseja sair? [1] Sim [2] Nao");
                    if (lerEscolha(1, 2) == 1) {
                        System.out.println(">> Deseja salvar antes de sair? [1] Sim [2] Nao");
                        if (lerEscolha(1, 2) == 1) {
                            SalvarJogo.salvar(jogador, mapa);
                        }
                        jogando = false;
                    }
                    break;
                default:
                    break;
            }
        }

        if (vitoriaFinal) {
            mostrarFinalVitoria();
        } else if (!jogador.isVivo()) {
            mostrarFinalDerrota();
        } else {
            System.out.println("\n>> Ate a proxima, aventureiro!");
        }

        scn.close();
    }

    private static void aplicarDificuldade(Inimigo inimigo) {
        if (dificuldade == 1) {
            inimigo.ajustarStats(0.75, 1.3);
        } else if (dificuldade == 3) {
            inimigo.ajustarStats(1.4, 0.7);
        }
    }

    public static int getDificuldade() { return dificuldade; }

    private static int contarBatalhas() {
        return mapa.getBatalhasNaZona();
    }

    private static void mostrarTitulo() {
        System.out.println("\n");
        System.out.println("  +-------------------------------------------------+");
        System.out.println("  |                                                 |");
        System.out.println("  |     L   I   P   I   D   I   A                  |");
        System.out.println("  |                                                 |");
        System.out.println("  |        ___                                      |");
        System.out.println("  |       /   \\   Cronicas de um Heroi             |");
        System.out.println("  |      | === |                                    |");
        System.out.println("  |       \\___/   Um RPG de Aventura               |");
        System.out.println("  |        | |                                      |");
        System.out.println("  |       /| |\\                                    |");
        System.out.println("  |      /_| |_\\                                   |");
        System.out.println("  |                                                 |");
        System.out.println("  +-------------------------------------------------+");
        System.out.println();
        Util.digitarLento("  Pressione ENTER para comecar...", 25);
        scn.nextLine();
    }

    private static void criarPersonagem() {
        Util.limparTela();
        Util.digitarLento("\n>> Bem vindo(a) a LIPIDIA, aventureiro!");
        Util.digitarLento(">> Antes de tudo, como devo chama-lo(a)?");
        System.out.print("\n  Nome: ");
        String nome = scn.nextLine().trim();
        if (nome.isEmpty()) {
            nome = "Heroi";
        }

        Util.limparTela();
        System.out.println("\n+============ ESCOLHA SUA CLASSE ============+");
        System.out.println("|                                             |");
        System.out.println("|  [1] MAGO                                   |");
        System.out.println("|      Alto dano magico, muita mana           |");
        System.out.println("|      Habilidades: Bola de Fogo, Raio Glacial|");
        System.out.println("|      HP: Baixo | ATK: Alto | DEF: Baixa    |");
        System.out.println("|                                             |");
        System.out.println("|  [2] GUERREIRO                              |");
        System.out.println("|      Tanque resistente, dano fisico alto    |");
        System.out.println("|      Habilidades: Golpe Devastador, Furia   |");
        System.out.println("|      HP: Alto | ATK: Alto | DEF: Alta      |");
        System.out.println("|                                             |");
        System.out.println("|  [3] ARQUEIRO                               |");
        System.out.println("|      Rapido e preciso, ataques a distancia  |");
        System.out.println("|      Habilidades: Flecha Rapida, Chuva      |");
        System.out.println("|      HP: Medio | ATK: Medio | VEL: Alta    |");
        System.out.println("|                                             |");
        System.out.println("|  [4] PALADINO                               |");
        System.out.println("|      Hibrido: combate e cura divina         |");
        System.out.println("|      Habilidades: Golpe Sagrado, Escudo     |");
        System.out.println("|      HP: Alto | ATK: Medio | DEF: Media    |");
        System.out.println("|                                             |");
        System.out.println("+==============================================+");

        int classeEscolha = lerEscolha(1, 4);
        Jogador.Classe classe;
        switch (classeEscolha) {
            case 1: classe = Jogador.Classe.MAGO; break;
            case 2: classe = Jogador.Classe.GUERREIRO; break;
            case 3: classe = Jogador.Classe.ARQUEIRO; break;
            case 4: classe = Jogador.Classe.PALADINO; break;
            default: classe = Jogador.Classe.GUERREIRO; break;
        }

        jogador = new Jogador(nome, classe);

        Util.limparTela();
        System.out.println("\n+============ DIFICULDADE ============+");
        System.out.println("|                                      |");
        System.out.println("|  [1] FACIL                           |");
        System.out.println("|      Inimigos mais fracos            |");
        System.out.println("|      Mais recompensas                |");
        System.out.println("|                                      |");
        System.out.println("|  [2] NORMAL                          |");
        System.out.println("|      Experiencia balanceada          |");
        System.out.println("|                                      |");
        System.out.println("|  [3] DIFICIL                         |");
        System.out.println("|      Inimigos mais fortes            |");
        System.out.println("|      Menos recompensas               |");
        System.out.println("|                                      |");
        System.out.println("+======================================+");
        dificuldade = lerEscolha(1, 3);

        Util.limparTela();
        System.out.println("\n+============================================+");
        Util.digitarLento("  " + nome + ", o(a) " + jogador.getNomeClasse() + " de Lipidia!", 40);
        System.out.println("+============================================+");
        jogador.mostrarStatus();
        Util.pausa(2000);
    }

    private static int menuPrincipal() {
        System.out.println("\n+========= " + mapa.getNomeZona().toUpperCase() + " =========+");
        System.out.println("  HP: " + Util.barraVida(jogador.getHp(), jogador.getHpMax(), 15));
        System.out.println("  MP: " + Util.barraMana(jogador.getMana(), jogador.getManaMax(), 15));
        System.out.println("  Ouro: " + jogador.getInventario().getOuro() + " G");
        System.out.println("+--------------------------------------+");
        System.out.println("  [1] Explorar (batalha aleatoria)");
        if (mapa.bossDisponivel()) {
            System.out.println("  [2] >> ENFRENTAR O BOSS! <<");
        } else {
            System.out.println("  [2] Enfrentar Boss (bloqueado)");
        }
        System.out.println("  [3] Loja");
        System.out.println("  [4] Inventario");
        System.out.println("  [5] Status");
        System.out.println("  [6] Mapa");
        System.out.println("  [7] Descansar (recupera HP/MP)");
        System.out.println("  [8] Salvar jogo");
        System.out.println("  [9] Sair do jogo");
        System.out.println("+--------------------------------------+");

        return lerEscolha(1, 9);
    }

    private static boolean explorar() {
        String[] eventosExplora = {
            "Voce caminha pela " + mapa.getNomeZona() + "...",
            "O ambiente fica tenso... algo se move nas sombras...",
            "Um barulho estranho ecoa...",
            "Voce sente uma presenca hostil se aproximando...",
            "Folhas se agitam... passos pesados se aproximam..."
        };

        Util.digitarLento("\n>> " + eventosExplora[Util.rolar(eventosExplora.length) - 1]);
        Util.pausa(1000);

        if (Util.rolar(5) == 1) {
            return eventoEspecial();
        }

        Inimigo inimigo = Inimigo.criarInimigoAleatorio(jogador.getNivel(), mapa.getZonaAtual());
        aplicarDificuldade(inimigo);
        Batalha batalha = new Batalha(jogador, inimigo, scn);
        boolean resultado = batalha.iniciar();

        if (resultado && jogador.isVivo()) {
            mapa.registrarBatalha();
            if (mapa.bossDisponivel()) {
                System.out.println("\n>> *** O BOSS da " + mapa.getNomeZona() + " esta pronto para ser enfrentado! ***");
            }
        }

        return jogador.isVivo();
    }

    private static boolean eventoEspecial() {
        int evento = Util.rolar(8);
        switch (evento) {
            case 1:
                int ouro = 15 + Util.rolar(30);
                System.out.println("\n>> Voce encontrou um bau escondido!");
                System.out.println(">> +" + ouro + " de ouro!");
                jogador.getInventario().adicionarOuro(ouro);
                return true;
            case 2:
                System.out.println("\n>> Voce encontrou uma fonte magica brilhante!");
                System.out.println(">> Sua vida e mana foram restauradas!");
                jogador.curar(jogador.getHpMax());
                jogador.recuperarMana(jogador.getManaMax());
                return true;
            case 3:
                Item raro = new Item("Pocao Rara", Item.Tipo.POCAO_VIDA, 100, 50, "Pocao rara! Recupera 100 HP");
                System.out.println("\n>> Voce encontrou algo brilhando no chao...");
                System.out.println(">> " + raro.getNome() + "!");
                jogador.getInventario().adicionarItem(raro);
                return true;
            case 4:
                System.out.println("\n>> EMBOSCADA! Um inimigo poderoso aparece!");
                Inimigo forte = Inimigo.criarInimigoAleatorio(jogador.getNivel() + 1, mapa.getZonaAtual());
                aplicarDificuldade(forte);
                Batalha batalha = new Batalha(jogador, forte, scn);
                boolean resultado = batalha.iniciar();
                if (resultado) {
                    mapa.registrarBatalha();
                }
                return jogador.isVivo();
            case 5:
                return eventoMercadorMisterioso();
            case 6:
                return eventoArmadilha();
            case 7:
                return eventoEnigma();
            default:
                return eventoNpcPedindoAjuda();
        }
    }

    private static boolean eventoMercadorMisterioso() {
        System.out.println("\n>> Um mercador encapuzado aparece das sombras...");
        Util.digitarLento(">> \"Psiu... heroi... tenho algo especial pra voce...\"", 35);

        Item[] itensRaros = {
            new Item("Elixir Lendario", Item.Tipo.POCAO_VIDA, 120, 40, "Pocao lendaria! Recupera 120 HP"),
            new Item("Essencia Arcana", Item.Tipo.POCAO_MANA, 80, 35, "Essencia pura! Recupera 80 MP"),
            new Item("Adaga Sombria", Item.Tipo.ARMA, 16, 70, "Lamina forjada nas sombras"),
            new Item("Amuleto Protetor", Item.Tipo.ARMADURA, 12, 65, "Amuleto magico de protecao")
        };
        Item oferta = itensRaros[Util.rolar(itensRaros.length) - 1];
        int precoDesconto = oferta.getPreco();

        System.out.println(">> Ele oferece: " + oferta.getNome() + " - " + oferta.getDescricao());
        System.out.println(">> Preco especial: " + precoDesconto + " G (seu ouro: " + jogador.getInventario().getOuro() + " G)");
        System.out.println(">> [1] Comprar [2] Recusar");

        if (lerEscolha(1, 2) == 1) {
            if (jogador.getInventario().gastarOuro(precoDesconto)) {
                jogador.getInventario().adicionarItem(oferta);
                System.out.println(">> \"Otimo negocio, heroi!\" O mercador desaparece nas sombras.");
            } else {
                System.out.println(">> \"Sem ouro? Que pena...\" O mercador desaparece.");
            }
        } else {
            System.out.println(">> \"Talvez na proxima...\" O mercador desaparece nas sombras.");
        }
        return true;
    }

    private static boolean eventoArmadilha() {
        System.out.println("\n>> Voce pisa em algo estranho... CLICK!");
        Util.suspense();

        int rolagem = Util.rolarD20();
        if (rolagem >= 10) {
            System.out.println(">> [D20 = " + rolagem + "] Voce desviou da armadilha a tempo!");
            System.out.println(">> Ufa! Isso foi perto...");
        } else {
            int dano = 10 + Util.rolar(15) + mapa.getZonaAtual() * 5;
            jogador.receberDano(dano);
            System.out.println(">> [D20 = " + rolagem + "] A armadilha te acertou!");
            if (!jogador.isVivo()) {
                System.out.println(">> A armadilha foi fatal...");
                return false;
            }
        }
        return true;
    }

    private static boolean eventoEnigma() {
        String[][] enigmas = {
            {"Tenho cidades mas nao tenho casas, tenho florestas mas nao tenho arvores, tenho agua mas nao tenho peixes. O que sou?", "mapa"},
            {"Quanto mais se tira, maior fica. O que e?", "buraco"},
            {"O que tem cabeca e cauda mas nao tem corpo?", "moeda"},
            {"Qual e a coisa que quanto mais seca, mais molhada fica?", "toalha"},
            {"O que passa diante do sol sem fazer sombra?", "vento"}
        };

        int indice = Util.rolar(enigmas.length) - 1;
        String pergunta = enigmas[indice][0];
        String resposta = enigmas[indice][1];

        System.out.println("\n>> Uma esfinge magica aparece bloqueando o caminho!");
        Util.digitarLento(">> \"Responda minha charada e seras recompensado...\"", 35);
        System.out.println("\n>> " + pergunta);
        System.out.print(">> Sua resposta: ");
        String respostaJogador = scn.nextLine().trim().toLowerCase();

        if (respostaJogador.contains(resposta)) {
            int recompensa = 30 + Util.rolar(40);
            System.out.println("\n>> \"Correto! Impressionante, heroi!\"");
            System.out.println(">> +" + recompensa + " de ouro!");
            jogador.getInventario().adicionarOuro(recompensa);
        } else {
            System.out.println("\n>> \"Errado! A resposta era: " + resposta + "\"");
            System.out.println(">> A esfinge desaparece sem recompensa.");
        }
        return true;
    }

    private static boolean eventoNpcPedindoAjuda() {
        System.out.println("\n>> Voce encontra um viajante ferido no caminho!");
        Util.digitarLento(">> \"Por favor, heroi... me ajude! Fui atacado por monstros...\"", 35);
        System.out.println("\n>> [1] Ajudar o viajante (gastar 20 HP para curar)");
        System.out.println(">> [2] Ignorar e seguir em frente");

        if (lerEscolha(1, 2) == 1) {
            if (jogador.getHp() > 25) {
                jogador.receberDanoDireto(20);
                int recompensa = Util.rolar(3);
                switch (recompensa) {
                    case 1:
                        int ouroRecompensa = 40 + Util.rolar(30);
                        System.out.println(">> \"Muito obrigado! Tome estas moedas como agradecimento!\"");
                        System.out.println(">> +" + ouroRecompensa + " de ouro!");
                        jogador.getInventario().adicionarOuro(ouroRecompensa);
                        break;
                    case 2:
                        Item presente = new Item("Pocao do Viajante", Item.Tipo.POCAO_VIDA, 70, 40, "Pocao especial! Recupera 70 HP");
                        System.out.println(">> \"Tome esta pocao rara que eu carregava!\"");
                        jogador.getInventario().adicionarItem(presente);
                        break;
                    default:
                        int xpBonus = 30 + Util.rolar(20);
                        System.out.println(">> \"Deixe-me compartilhar minha sabedoria com voce, heroi!\"");
                        System.out.println(">> O viajante te ensina tecnicas de combate!");
                        jogador.ganharXP(xpBonus);
                        break;
                }
            } else {
                System.out.println(">> Voce esta muito fraco para ajudar... melhor seguir em frente.");
            }
        } else {
            System.out.println(">> Voce ignora o viajante e segue seu caminho.");
        }
        return true;
    }

    private static boolean lutarBoss() {
        System.out.println("\n+============================================+");
        System.out.println("|         PREPARAR PARA O BOSS!              |");
        System.out.println("+============================================+");
        Util.digitarLento(">> Voce esta prestes a enfrentar um inimigo terrivel...", 35);
        System.out.println(">> Deseja continuar? [1] Sim [2] Nao");
        if (lerEscolha(1, 2) == 2) {
            return false;
        }

        Inimigo boss = Inimigo.criarBoss(mapa.getZonaAtual(), jogador.getNivel());
        aplicarDificuldade(boss);
        Batalha batalha = new Batalha(jogador, boss, scn);
        return batalha.iniciar();
    }

    private static void menuUsarItem() {
        if (jogador.getInventario().getItens().isEmpty()) {
            return;
        }
        System.out.println("\n  Deseja usar um item? [1] Sim [2] Nao");
        if (lerEscolha(1, 2) == 1) {
            System.out.println("  Qual item? (numero)");
            int escolha = lerEscolha(1, jogador.getInventario().getItens().size());
            jogador.getInventario().usarItem(escolha - 1, jogador);
        }
    }

    private static void descansar() {
        System.out.println("\n+========= ACAMPAMENTO =========+");
        System.out.println("  " + jogador.getNome() + " encontra um local seguro.");
        System.out.println("+-------------------------------+");
        System.out.println("  [1] Descansar (recupera HP/MP)");
        System.out.println("  [2] Treinar (ganhar XP)");
        System.out.println("  [3] Verificar equipamento");
        System.out.println("  [0] Voltar");
        System.out.println("+-------------------------------+");

        int opcao = lerEscolha(0, 3);
        switch (opcao) {
            case 1:
                Util.suspense();
                int custo = 10 * mapa.getZonaAtual();
                if (jogador.getInventario().gastarOuro(custo)) {
                    jogador.descansar();
                    System.out.println(">> (-" + custo + " ouro pela hospedagem)");
                } else {
                    int curaHP = jogador.getHpMax() / 4;
                    int curaMana = jogador.getManaMax() / 4;
                    jogador.curar(curaHP);
                    jogador.recuperarMana(curaMana);
                    System.out.println(">> Sem ouro para hospedagem. Descansou ao relento.");
                    System.out.println(">> Recuperou " + curaHP + " HP e " + curaMana + " MP.");
                }
                Util.pausa(1500);
                break;
            case 2:
                int custoTreino = 15 * mapa.getZonaAtual();
                System.out.println(">> Treinar custa " + custoTreino + " ouro. Continuar? [1] Sim [2] Nao");
                if (lerEscolha(1, 2) == 1) {
                    if (jogador.getInventario().gastarOuro(custoTreino)) {
                        int xpTreino = 15 + Util.rolar(10) * mapa.getZonaAtual();
                        Util.digitarLento(">> " + jogador.getNome() + " treina intensamente...", 35);
                        Util.suspense();
                        jogador.ganharXP(xpTreino);
                        System.out.println(">> (-" + custoTreino + " ouro pelo treino)");
                    } else {
                        System.out.println(">> Ouro insuficiente para treinar!");
                    }
                }
                break;
            case 3:
                jogador.mostrarStatus();
                jogador.getInventario().mostrar();
                break;
            case 0:
            default:
                break;
        }
    }

    private static void mostrarFinalVitoria() {
        Util.limparTela();
        System.out.println("\n");
        System.out.println("  +===================================================+");
        System.out.println("  |                                                   |");
        System.out.println("  |            PARABENS, HEROI DE LIPIDIA!            |");
        System.out.println("  |                                                   |");
        System.out.println("  |      Voce derrotou o Dragao Anciao e salvou       |");
        System.out.println("  |          o mundo de Lipidia da escuridao!         |");
        System.out.println("  |                                                   |");
        System.out.println("  |              *  *  *  *  *  *  *                  |");
        System.out.println("  |                                                   |");
        System.out.println("  +===================================================+");
        System.out.println();
        Util.digitarLento("  " + jogador.getNome() + ", o(a) " + jogador.getNomeClasse(), 50);
        Util.digitarLento("  Nivel: " + jogador.getNivel(), 50);
        Util.digitarLento("  Ouro final: " + jogador.getInventario().getOuro() + " G", 50);
        System.out.println();
        Util.digitarLento("  Sua lenda sera contada por geracoes!", 50);
        jogador.getStats().mostrar();
        Util.digitarLento("\n  Obrigado por jogar LIPIDIA: Cronicas de um Heroi!", 40);
        System.out.println();
    }

    private static void mostrarFinalDerrota() {
        Util.limparTela();
        System.out.println("\n");
        System.out.println("  +===================================================+");
        System.out.println("  |                                                   |");
        System.out.println("  |              VOCE FOI DERROTADO...                |");
        System.out.println("  |                                                   |");
        System.out.println("  |        A escuridao consome Lipidia...             |");
        System.out.println("  |       Mas todo heroi pode tentar de novo.         |");
        System.out.println("  |                                                   |");
        System.out.println("  +===================================================+");
        System.out.println();
        Util.digitarLento("  " + jogador.getNome() + " caiu no nivel " + jogador.getNivel(), 50);
        jogador.getStats().mostrar();
        Util.digitarLento("\n  Tente novamente, aventureiro!", 40);
        System.out.println();
    }

    private static int lerEscolha(int min, int max) {
        while (true) {
            System.out.print(">> ");
            try {
                int escolha = Integer.parseInt(scn.nextLine().trim());
                if (escolha >= min && escolha <= max) return escolha;
            } catch (NumberFormatException ignored) {}
            System.out.println(">> Escolha invalida! Digite um numero entre " + min + " e " + max);
        }
    }
}
