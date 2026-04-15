import java.util.Scanner;
import java.util.List;

@SuppressWarnings("java:S106")
public class Batalha {
    private Jogador jogador;
    private Inimigo inimigo;
    private Scanner scn;
    private boolean jogadorFugiu;
    private boolean inimigoFugiu;

    public Batalha(Jogador jogador, Inimigo inimigo, Scanner scn) {
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.scn = scn;
        this.jogadorFugiu = false;
        this.inimigoFugiu = false;
    }

    public boolean iniciar() {
        System.out.println("\n+============================================+");
        System.out.println("|           BATALHA INICIADA!                |");
        System.out.println("+============================================+");
        inimigo.mostrarAscii();
        System.out.println(">> Um " + inimigo.getNome() + " apareceu!");
        if (inimigo.isEhBoss()) {
            Util.digitarLento(">> CUIDADO! Este e um inimigo poderoso!", 40);
        }
        Util.pausa(1500);

        boolean jogadorPrimeiro = jogador.getVelocidade() >= inimigo.getVelocidade();
        if (jogadorPrimeiro) {
            System.out.println(">> " + jogador.getNome() + " e mais rapido e ataca primeiro!");
        } else {
            System.out.println(">> " + inimigo.getNome() + " e mais rapido e ataca primeiro!");
        }
        Util.pausa(1000);

        int turno = 1;
        while (jogador.isVivo() && inimigo.isVivo() && !jogadorFugiu && !inimigoFugiu) {
            System.out.println("\n--- Turno " + turno + " ---");
            mostrarBarrasVida();

            if (jogadorPrimeiro) {
                processarEfeitosTurno(jogador);
                if (jogador.isVivo()) turnoJogador();
                if (inimigo.isVivo() && !jogadorFugiu) {
                    processarEfeitosTurno(inimigo);
                    if (inimigo.isVivo()) turnoInimigo();
                }
            } else {
                processarEfeitosTurno(inimigo);
                if (inimigo.isVivo()) turnoInimigo();
                if (jogador.isVivo() && !inimigoFugiu) {
                    processarEfeitosTurno(jogador);
                    if (jogador.isVivo()) turnoJogador();
                }
            }
            turno++;
        }

        return resolverResultado();
    }

    private void mostrarBarrasVida() {
        System.out.println("\n  " + jogador.getNome() + " HP: " + Util.barraVida(jogador.getHp(), jogador.getHpMax(), 20));
        System.out.println("  " + jogador.getNome() + " MP: " + Util.barraMana(jogador.getMana(), jogador.getManaMax(), 15));
        System.out.println("  " + inimigo.getNome() + " HP: " + Util.barraVida(inimigo.getHp(), inimigo.getHpMax(), 20));
    }

    private void turnoJogador() {
        if (!jogador.isVivo() || !inimigo.isVivo() || jogadorFugiu || inimigoFugiu) return;

        System.out.println("\n+--- SEU TURNO ---+");
        System.out.println("  [1] Atacar");
        System.out.println("  [2] Usar Item");
        System.out.println("  [3] Fugir");
        System.out.println("  [4] Status");

        int escolha = lerEscolha(1, 4);

        switch (escolha) {
            case 1: menuAtaque(); break;
            case 2: menuItem(); break;
            case 3: tentarFugir(); break;
            case 4:
                jogador.mostrarStatus();
                turnoJogador();
                break;
        }
    }

    private void menuAtaque() {
        List<Habilidade> habs = jogador.getHabilidades();
        System.out.println("\n  Escolha uma habilidade:");
        for (int i = 0; i < habs.size(); i++) {
            Habilidade h = habs.get(i);
            String manaStr = h.getCustoMana() > 0 ? " [" + h.getCustoMana() + " MP]" : " [sem custo]";
            String tipoStr = h.isCura() ? " (CURA)" : "";
            System.out.println("  [" + (i + 1) + "] " + h.getNome() + manaStr + tipoStr + " - " + h.getDescricao());
        }
        System.out.println("  [0] Voltar");

        int escolha = lerEscolha(0, habs.size());
        if (escolha == 0) {
            turnoJogador();
            return;
        }

        Habilidade hab = habs.get(escolha - 1);

        if (!jogador.gastarMana(hab.getCustoMana())) {
            System.out.println(">> Mana insuficiente! Voce precisa de " + hab.getCustoMana() + " MP.");
            menuAtaque();
            return;
        }

        executarHabilidade(jogador, inimigo, hab);
    }

    private void executarHabilidade(Personagem atacante, Personagem defensor, Habilidade hab) {
        System.out.println("\n>> " + atacante.getNome() + " usa '" + hab.getNome() + "'!");
        Util.suspense();

        int rolagem = Util.rolarD20();
        boolean critico = rolagem == 20;
        boolean acertou = rolagem >= hab.getDificuldade();

        if (acertou) {
            System.out.println(hab.getEfeitoVisual());

            if (hab.isCura()) {
                int curaTotal = hab.getDano();
                if (critico) {
                    curaTotal *= 2;
                    System.out.println(">> CURA CRITICA!");
                }
                atacante.curar(curaTotal);
                System.out.println(">> " + atacante.getNome() + " recuperou " + curaTotal + " HP!");
                if (atacante == jogador) jogador.getStats().registrarCura(curaTotal);
                if (hab.temEfeito()) {
                    aplicarEfeitoHabilidade(hab, atacante);
                }
            } else {
                int danoBase = hab.getDano() + atacante.getAtaque() / 3;
                int variacao = Util.randomEntre(-2, 4);
                int danoTotal = danoBase + variacao;
                if (critico) {
                    danoTotal *= 2;
                    System.out.println(">> *** ACERTO CRITICO! DANO DOBRADO! ***");
                    if (atacante == jogador) jogador.getStats().registrarCritico();
                }
                defensor.receberDano(danoTotal);
                if (atacante == jogador) jogador.getStats().registrarDanoCausado(danoTotal);
                if (defensor == jogador) jogador.getStats().registrarDanoRecebido(danoTotal);
                if (hab.temEfeito()) {
                    aplicarEfeitoHabilidade(hab, defensor);
                }
            }

            if (critico && !hab.isCura()) {
                System.out.println(">> [D20 = " + rolagem + "] CRITICO!");
            } else {
                System.out.println(">> [D20 = " + rolagem + "] Sucesso!");
            }
        } else {
            System.out.println(">> [D20 = " + rolagem + "] Errou o ataque!");
            if (rolagem == 1) {
                System.out.println(">> *** FALHA CRITICA! ***");
            }
        }
        Util.pausa(1500);
    }

    private void menuItem() {
        Inventario inv = jogador.getInventario();
        List<Item> itens = inv.getItens();

        if (itens.isEmpty()) {
            System.out.println(">> Inventario vazio!");
            turnoJogador();
            return;
        }

        System.out.println("\n  Escolha um item para usar:");
        for (int i = 0; i < itens.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + itens.get(i));
        }
        System.out.println("  [0] Voltar");

        int escolha = lerEscolha(0, itens.size());
        if (escolha == 0) {
            turnoJogador();
            return;
        }

        inv.usarItem(escolha - 1, jogador);
        Util.pausa(1000);
    }

    private void tentarFugir() {
        System.out.println(">> " + jogador.getNome() + " tenta fugir!");
        Util.suspense();

        int dificuldade = inimigo.isEhBoss() ? 18 : 12;
        int rolagem = Util.rolarD20();

        if (rolagem >= dificuldade) {
            System.out.println(">> [D20 = " + rolagem + "] Fuga bem-sucedida!");
            jogador.getStats().registrarFuga();
            jogadorFugiu = true;
        } else {
            System.out.println(">> [D20 = " + rolagem + "] Nao conseguiu fugir!");
        }
        Util.pausa(1000);
    }

    private void turnoInimigo() {
        if (!jogador.isVivo() || !inimigo.isVivo() || jogadorFugiu || inimigoFugiu) return;

        System.out.println("\n+--- TURNO DO " + inimigo.getNome().toUpperCase() + " ---+");
        Util.pausa(1000);

        if (!inimigo.isEhBoss() && Util.rolar(10) == 1) {
            System.out.println(">> " + inimigo.getNome() + " tenta fugir!");
            Util.suspense();
            if (Util.testeChance(16)) {
                System.out.println(">> " + inimigo.getNome() + " fugiu da batalha!");
                inimigoFugiu = true;
                return;
            } else {
                System.out.println(">> Nao conseguiu fugir!");
            }
        } else {
            Habilidade hab = inimigo.escolherAcao();

            if (hab.isCura() && inimigo.getHp() > inimigo.getHpMax() * 0.6) {
                hab = inimigo.getHabilidades().get(0);
            }

            if (hab.isCura()) {
                executarHabilidadeInimigo(inimigo, jogador, hab);
            } else {
                executarHabilidade(inimigo, jogador, hab);
            }
        }
    }

    private void executarHabilidadeInimigo(Inimigo ini, Jogador jog, Habilidade hab) {
        System.out.println("\n>> " + ini.getNome() + " usa '" + hab.getNome() + "'!");
        Util.suspense();
        int rolagem = Util.rolarD20();
        if (rolagem >= hab.getDificuldade()) {
            System.out.println(hab.getEfeitoVisual());
            ini.curar(hab.getDano());
            System.out.println(">> " + ini.getNome() + " recuperou " + hab.getDano() + " HP!");
        } else {
            System.out.println(">> A habilidade falhou!");
        }
        Util.pausa(1500);
    }

    private boolean resolverResultado() {
        jogador.limparEfeitos();
        inimigo.limparEfeitos();
        System.out.println("\n+============================================+");
        if (!jogador.isVivo()) {
            System.out.println("|            VOCE FOI DERROTADO!             |");
            System.out.println("+============================================+");
            jogador.getStats().registrarDerrota();
            return false;
        } else if (jogadorFugiu) {
            System.out.println("|            VOCE FUGIU DA BATALHA!          |");
            System.out.println("+============================================+");
            return true;
        } else if (inimigoFugiu) {
            System.out.println("|         O INIMIGO FUGIU DA BATALHA!        |");
            System.out.println("+============================================+");
            int ouroGanho = inimigo.getOuroRecompensa() / 2;
            jogador.getInventario().adicionarOuro(ouroGanho);
            System.out.println(">> Voce encontrou " + ouroGanho + " de ouro no chao.");
            return true;
        } else {
            System.out.println("|              VITORIA GLORIOSA!             |");
            System.out.println("+============================================+");
            recompensas();
            return true;
        }
    }

    private void recompensas() {
        jogador.getStats().registrarVitoria(inimigo.getNome());
        int xp = inimigo.getXpRecompensa();
        int ouro = inimigo.getOuroRecompensa();
        jogador.ganharXP(xp);
        jogador.getInventario().adicionarOuro(ouro);
        jogador.getStats().registrarOuroGanho(ouro);
        System.out.println(">> +" + ouro + " de ouro!");

        if (Util.rolar(100) <= 30) {
            Item drop = gerarDrop();
            if (drop != null) {
                jogador.getInventario().adicionarItem(drop);
                System.out.println(">> O inimigo deixou cair: " + drop.getNome() + "!");
            }
        }
        Util.pausa(2000);
    }

    private Item gerarDrop() {
        int tipo = Util.rolar(4);
        switch (tipo) {
            case 1: return new Item("Pocao de Vida", Item.Tipo.POCAO_VIDA, 40, 20, "Recupera 40 HP");
            case 2: return new Item("Pocao de Mana", Item.Tipo.POCAO_MANA, 30, 18, "Recupera 30 MP");
            case 3: return new Item("Elixir Misterioso", Item.Tipo.POCAO_VIDA, 60, 35, "Recupera 60 HP");
            default: return new Item("Pocao de Mana Maior", Item.Tipo.POCAO_MANA, 50, 30, "Recupera 50 MP");
        }
    }

    private void processarEfeitosTurno(Personagem p) {
        if (!p.getEfeitos().isEmpty()) {
            p.processarEfeitos();
        }
    }

    private void aplicarEfeitoHabilidade(Habilidade hab, Personagem alvo) {
        if (hab.temEfeito()) {
            String nomeEfeito;
            switch (hab.getTipoEfeito()) {
                case VENENO: nomeEfeito = "Veneno"; break;
                case QUEIMADURA: nomeEfeito = "Queimadura"; break;
                case BUFF_ATAQUE: nomeEfeito = "Buff de Ataque"; break;
                case BUFF_DEFESA: nomeEfeito = "Buff de Defesa"; break;
                default: nomeEfeito = "Efeito"; break;
            }
            alvo.adicionarEfeito(new Efeito(hab.getTipoEfeito(), nomeEfeito, hab.getValorEfeito(), hab.getTurnosEfeito()));
        }
    }

    private int lerEscolha(int min, int max) {
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
