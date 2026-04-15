import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("java:S106")
public class Loja {
    private List<Item> estoque;
    private Scanner scn;

    public Loja(int zona, Scanner scn) {
        this.scn = scn;
        this.estoque = new ArrayList<>();
        montarEstoque(zona);
    }

    private void montarEstoque(int zona) {
        estoque.add(new Item("Pocao de Vida", Item.Tipo.POCAO_VIDA, 40, 20, "Recupera 40 HP"));
        estoque.add(new Item("Pocao de Vida Grande", Item.Tipo.POCAO_VIDA, 80, 45, "Recupera 80 HP"));
        estoque.add(new Item("Pocao de Mana", Item.Tipo.POCAO_MANA, 30, 18, "Recupera 30 MP"));
        estoque.add(new Item("Pocao de Mana Grande", Item.Tipo.POCAO_MANA, 60, 40, "Recupera 60 MP"));

        switch (zona) {
            case 1:
                estoque.add(new Item("Espada de Ferro", Item.Tipo.ARMA, 8, 60, "Espada basica mas confiavel"));
                estoque.add(new Item("Escudo de Madeira", Item.Tipo.ARMADURA, 5, 45, "Escudo simples"));
                estoque.add(new Item("Arco Curto", Item.Tipo.ARMA, 6, 50, "Arco leve e rapido"));
                break;
            case 2:
                estoque.add(new Item("Espada de Aco", Item.Tipo.ARMA, 14, 120, "Aco refinado e afiado"));
                estoque.add(new Item("Armadura de Couro", Item.Tipo.ARMADURA, 10, 100, "Couro resistente"));
                estoque.add(new Item("Cajado Arcano", Item.Tipo.ARMA, 12, 110, "Canaliza energia magica"));
                estoque.add(new Item("Cota de Malha", Item.Tipo.ARMADURA, 12, 130, "Protecao de metal trancado"));
                break;
            case 3:
                estoque.add(new Item("Espada Flamejante", Item.Tipo.ARMA, 22, 250, "Lamina envolta em chamas!"));
                estoque.add(new Item("Armadura de Placas", Item.Tipo.ARMADURA, 18, 220, "Placas de aco puro"));
                estoque.add(new Item("Cajado do Abismo", Item.Tipo.ARMA, 20, 240, "Poder das profundezas"));
                estoque.add(new Item("Elixir da Vida", Item.Tipo.POCAO_VIDA, 150, 80, "Recupera 150 HP"));
                estoque.add(new Item("Elixir Arcano", Item.Tipo.POCAO_MANA, 100, 70, "Recupera 100 MP"));
                break;
        }
    }

    public void abrir(Jogador jogador) {
        System.out.println("\n+=========================================+");
        System.out.println("|          BEM VINDO A LOJA!              |");
        System.out.println("|    \"Temos tudo que um heroi precisa!\"   |");
        System.out.println("+=========================================+");

        boolean naLoja = true;
        while (naLoja) {
            System.out.println("\n  Seu ouro: " + jogador.getInventario().getOuro() + " G");
            System.out.println("\n  [1] Comprar");
            System.out.println("  [2] Vender");
            System.out.println("  [0] Sair da loja");

            int opcao = lerEscolha(0, 2);
            if (opcao == 0) {
                naLoja = false;
                System.out.println(">> \"Volte sempre, aventureiro!\"");
            } else if (opcao == 1) {
                menuComprar(jogador);
            } else {
                menuVender(jogador);
            }
        }
    }

    private void menuComprar(Jogador jogador) {
        boolean comprando = true;
        while (comprando) {
            System.out.println("\n  Seu ouro: " + jogador.getInventario().getOuro() + " G");
            System.out.println("\n  Itens a venda:");
            for (int i = 0; i < estoque.size(); i++) {
                Item item = estoque.get(i);
                System.out.println("  [" + (i + 1) + "] " + item.getNome() + " - " + item.getDescricao()
                    + " | " + item.getPreco() + " G");
            }
            System.out.println("\n  [0] Voltar");

            int escolha = lerEscolha(0, estoque.size());
            if (escolha == 0) {
                comprando = false;
            } else {
                Item item = estoque.get(escolha - 1);
                if (jogador.getInventario().gastarOuro(item.getPreco())) {
                    jogador.getInventario().adicionarItem(
                        new Item(item.getNome(), item.getTipo(), item.getValor(), item.getPreco(), item.getDescricao()));
                    System.out.println(">> Voce comprou " + item.getNome() + "!");
                } else {
                    System.out.println(">> Ouro insuficiente! Voce precisa de " + item.getPreco() + " G.");
                }
            }
        }
    }

    private void menuVender(Jogador jogador) {
        List<Item> itens = jogador.getInventario().getItens();
        if (itens.isEmpty()) {
            System.out.println(">> \"Voce nao tem nada para vender, aventureiro!\"");
            return;
        }

        boolean vendendo = true;
        while (vendendo) {
            itens = jogador.getInventario().getItens();
            if (itens.isEmpty()) {
                System.out.println(">> \"Nao ha mais nada para vender!\"");
                return;
            }

            System.out.println("\n  Seu ouro: " + jogador.getInventario().getOuro() + " G");
            System.out.println("\n  Seus itens (venda por metade do preco):");
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.get(i);
                int precoVenda = item.getPreco() / 2;
                System.out.println("  [" + (i + 1) + "] " + item.getNome() + " - " + item.getDescricao()
                    + " | Venda: " + precoVenda + " G");
            }
            System.out.println("\n  [0] Voltar");

            int escolha = lerEscolha(0, itens.size());
            if (escolha == 0) {
                vendendo = false;
            } else {
                Item item = itens.get(escolha - 1);
                int precoVenda = item.getPreco() / 2;
                jogador.getInventario().adicionarOuro(precoVenda);
                jogador.getInventario().removerItem(escolha - 1);
                System.out.println(">> Voce vendeu " + item.getNome() + " por " + precoVenda + " G!");
            }
        }
    }

    private int lerEscolha(int min, int max) {
        while (true) {
            System.out.print(">> ");
            try {
                int escolha = Integer.parseInt(scn.nextLine().trim());
                if (escolha >= min && escolha <= max) return escolha;
            } catch (NumberFormatException ignored) {}
            System.out.println(">> Escolha invalida!");
        }
    }
}
