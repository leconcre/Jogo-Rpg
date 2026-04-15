import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S106")
public class Inventario {
    private List<Item> itens;
    private int ouro;
    private static final int LIMITE_ITENS = 15;

    public Inventario() {
        this.itens = new ArrayList<>();
        this.ouro = 50;
    }

    public void adicionarItem(Item item) {
        if (itens.size() >= LIMITE_ITENS) {
            System.out.println(">> Inventario cheio! (" + LIMITE_ITENS + "/" + LIMITE_ITENS + ")");
            System.out.println(">> Descarte um item ou venda na loja.");
            return;
        }
        itens.add(item);
    }

    public boolean inventarioCheio() {
        return itens.size() >= LIMITE_ITENS;
    }

    public int getLimite() { return LIMITE_ITENS; }

    public void removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
        }
    }

    public int getOuro() { return ouro; }
    public void adicionarOuro(int quantidade) { ouro += quantidade; }
    public boolean gastarOuro(int quantidade) {
        if (ouro >= quantidade) {
            ouro -= quantidade;
            return true;
        }
        return false;
    }

    public List<Item> getItens() { return itens; }

    public Item usarItem(int indice, Jogador jogador) {
        if (indice < 0 || indice >= itens.size()) return null;
        Item item = itens.get(indice);

        switch (item.getTipo()) {
            case POCAO_VIDA:
                jogador.curar(item.getValor());
                System.out.println(">> " + jogador.getNome() + " usou " + item.getNome() + " e recuperou " + item.getValor() + " HP!");
                itens.remove(indice);
                return item;
            case POCAO_MANA:
                jogador.recuperarMana(item.getValor());
                System.out.println(">> " + jogador.getNome() + " usou " + item.getNome() + " e recuperou " + item.getValor() + " MP!");
                itens.remove(indice);
                return item;
            case ARMA:
                jogador.equiparArma(item);
                System.out.println(">> " + jogador.getNome() + " equipou " + item.getNome() + "! (+" + item.getValor() + " ATK)");
                itens.remove(indice);
                return item;
            case ARMADURA:
                jogador.equiparArmadura(item);
                System.out.println(">> " + jogador.getNome() + " equipou " + item.getNome() + "! (+" + item.getValor() + " DEF)");
                itens.remove(indice);
                return item;
            default:
                System.out.println(">> Item especial! Guarde para o momento certo.");
                return null;
        }
    }

    public void mostrar() {
        System.out.println("\n+========= INVENTARIO =========+");
        System.out.println("  Ouro: " + ouro + " G");
        System.out.println("  Espaco: " + itens.size() + "/" + LIMITE_ITENS);
        System.out.println("+------------------------------+");
        if (itens.isEmpty()) {
            System.out.println("  (vazio)");
        } else {
            for (int i = 0; i < itens.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + itens.get(i));
            }
        }
        System.out.println("+==============================+");
    }

    public boolean temPocaoVida() {
        return itens.stream().anyMatch(i -> i.getTipo() == Item.Tipo.POCAO_VIDA);
    }

    public boolean temPocaoMana() {
        return itens.stream().anyMatch(i -> i.getTipo() == Item.Tipo.POCAO_MANA);
    }
}
