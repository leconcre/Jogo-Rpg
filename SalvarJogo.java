import java.io.*;
import java.util.List;

@SuppressWarnings("java:S106")
public class SalvarJogo {
    private static final String ARQUIVO_SAVE = "lipidia_save.txt";

    public static boolean salvar(Jogador jogador, Mapa mapa) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_SAVE))) {
            pw.println(jogador.getNome());
            pw.println(jogador.getClasse().name());
            pw.println(jogador.getNivel());
            pw.println(jogador.getXp());
            pw.println(jogador.getXpParaProximoNivel());
            pw.println(jogador.getHp());
            pw.println(jogador.getHpMax());
            pw.println(jogador.getMana());
            pw.println(jogador.getManaMax());
            pw.println(jogador.getAtaque());
            pw.println(jogador.getDefesa());
            pw.println(jogador.getVelocidade());
            pw.println(jogador.getInventario().getOuro());

            List<Item> itens = jogador.getInventario().getItens();
            pw.println(itens.size());
            for (Item item : itens) {
                pw.println(item.getNome() + "|" + item.getTipo().name() + "|" + item.getValor() + "|" + item.getPreco() + "|" + item.getDescricao());
            }

            pw.println(mapa.getZonaAtual());
            pw.println(mapa.getBatalhasNaZona());
            pw.println(mapa.getBatalhasParaBoss());

            System.out.println(">> Jogo salvo com sucesso!");
            return true;
        } catch (IOException e) {
            System.out.println(">> Erro ao salvar o jogo: " + e.getMessage());
            return false;
        }
    }

    public static Object[] carregar() {
        File file = new File(ARQUIVO_SAVE);
        if (!file.exists()) {
            System.out.println(">> Nenhum save encontrado!");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String nome = br.readLine();
            Jogador.Classe classe = Jogador.Classe.valueOf(br.readLine());
            int nivel = Integer.parseInt(br.readLine());
            int xp = Integer.parseInt(br.readLine());
            int xpProx = Integer.parseInt(br.readLine());
            int hp = Integer.parseInt(br.readLine());
            int hpMax = Integer.parseInt(br.readLine());
            int mana = Integer.parseInt(br.readLine());
            int manaMax = Integer.parseInt(br.readLine());
            int ataque = Integer.parseInt(br.readLine());
            int defesa = Integer.parseInt(br.readLine());
            int velocidade = Integer.parseInt(br.readLine());
            int ouro = Integer.parseInt(br.readLine());

            int numItens = Integer.parseInt(br.readLine());
            Inventario inv = new Inventario();
            inv.gastarOuro(inv.getOuro());
            inv.adicionarOuro(ouro);
            for (int i = 0; i < numItens; i++) {
                String[] partes = br.readLine().split("\\|");
                Item.Tipo tipo = Item.Tipo.valueOf(partes[1]);
                inv.adicionarItem(new Item(partes[0], tipo, Integer.parseInt(partes[2]), Integer.parseInt(partes[3]), partes[4]));
            }

            int zonaAtual = Integer.parseInt(br.readLine());
            int batalhasNaZona = Integer.parseInt(br.readLine());
            int batalhasParaBoss = Integer.parseInt(br.readLine());

            Jogador jogador = new Jogador(nome, classe);
            jogador.restaurarEstado(nivel, xp, xpProx, hp, hpMax, mana, manaMax, ataque, defesa, velocidade, inv);

            Mapa mapa = new Mapa();
            mapa.restaurarEstado(zonaAtual, batalhasNaZona, batalhasParaBoss);

            System.out.println(">> Jogo carregado com sucesso!");
            return new Object[] { jogador, mapa };
        } catch (Exception e) {
            System.out.println(">> Erro ao carregar o save: " + e.getMessage());
            return null;
        }
    }

    public static boolean existeSave() {
        return new File(ARQUIVO_SAVE).exists();
    }
}
