import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S106")
public class Estatisticas {
    private int batalhasVencidas;
    private int batalhasPerdidas;
    private int danoTotalCausado;
    private int danoTotalRecebido;
    private int ouroTotalGanho;
    private int ouroTotalGasto;
    private int curaTotal;
    private int criticosAcertados;
    private int fugasBemSucedidas;
    private Map<String, Integer> inimigosDerotados;
    private long tempoInicio;

    public Estatisticas() {
        this.inimigosDerotados = new HashMap<>();
        this.tempoInicio = System.currentTimeMillis();
    }

    public void registrarVitoria(String nomeInimigo) {
        batalhasVencidas++;
        inimigosDerotados.merge(nomeInimigo, 1, Integer::sum);
    }

    public void registrarDerrota() { batalhasPerdidas++; }
    public void registrarDanoCausado(int dano) { danoTotalCausado += dano; }
    public void registrarDanoRecebido(int dano) { danoTotalRecebido += dano; }
    public void registrarOuroGanho(int ouro) { ouroTotalGanho += ouro; }
    public void registrarOuroGasto(int ouro) { ouroTotalGasto += ouro; }
    public void registrarCura(int cura) { curaTotal += cura; }
    public void registrarCritico() { criticosAcertados++; }
    public void registrarFuga() { fugasBemSucedidas++; }

    public String getTempoDeJogo() {
        long ms = System.currentTimeMillis() - tempoInicio;
        long segundos = ms / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        segundos %= 60;
        minutos %= 60;

        if (horas > 0) return horas + "h " + minutos + "m " + segundos + "s";
        if (minutos > 0) return minutos + "m " + segundos + "s";
        return segundos + "s";
    }

    public void mostrar() {
        System.out.println("\n+============ ESTATISTICAS ============+");
        System.out.println("  Tempo de jogo: " + getTempoDeJogo());
        System.out.println("  Batalhas vencidas: " + batalhasVencidas);
        System.out.println("  Batalhas perdidas: " + batalhasPerdidas);
        System.out.println("  Dano total causado: " + danoTotalCausado);
        System.out.println("  Dano total recebido: " + danoTotalRecebido);
        System.out.println("  Cura total: " + curaTotal);
        System.out.println("  Criticos acertados: " + criticosAcertados);
        System.out.println("  Fugas bem-sucedidas: " + fugasBemSucedidas);
        System.out.println("  Ouro total ganho: " + ouroTotalGanho + " G");
        System.out.println("  Ouro total gasto: " + ouroTotalGasto + " G");
        if (!inimigosDerotados.isEmpty()) {
            System.out.println("+--------------------------------------+");
            System.out.println("  Inimigos derrotados:");
            for (Map.Entry<String, Integer> entry : inimigosDerotados.entrySet()) {
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("+======================================+");
    }

    public int getBatalhasVencidas() { return batalhasVencidas; }
    public int getDanoTotalCausado() { return danoTotalCausado; }
    public int getOuroTotalGanho() { return ouroTotalGanho; }
}
