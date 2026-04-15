@SuppressWarnings("java:S106")
public class Mapa {
    private int zonaAtual;
    private int batalhasNaZona;
    private int batalhasParaBoss;

    public Mapa() {
        this.zonaAtual = 1;
        this.batalhasNaZona = 0;
        this.batalhasParaBoss = 3;
    }

    public int getZonaAtual() { return zonaAtual; }
    public int getBatalhasNaZona() { return batalhasNaZona; }
    public int getBatalhasParaBoss() { return batalhasParaBoss; }

    public String getNomeZona() {
        switch (zonaAtual) {
            case 1: return "Floresta Sombria";
            case 2: return "Cavernas Profundas";
            case 3: return "Castelo do Dragao";
            default: return "???";
        }
    }

    public void mostrarMapa() {
        System.out.println("\n+========== MAPA DE LIPIDIA ==========+");
        System.out.println("|                                      |");
        System.out.println("|  [" + (zonaAtual == 1 ? ">>>" : "   ") + "] 1. Floresta Sombria          |");
        System.out.println("|       |                               |");
        System.out.println("|  [" + (zonaAtual == 2 ? ">>>" : "   ") + "] 2. Cavernas Profundas        |");
        System.out.println("|       |                               |");
        System.out.println("|  [" + (zonaAtual == 3 ? ">>>" : "   ") + "] 3. Castelo do Dragao         |");
        System.out.println("|                                      |");
        System.out.println("+======================================+");
        System.out.println("  Zona atual: " + getNomeZona());
        System.out.println("  Batalhas: " + batalhasNaZona + "/" + batalhasParaBoss + " (Boss disponivel em " + (batalhasParaBoss - batalhasNaZona) + ")");
    }

    public void registrarBatalha() {
        batalhasNaZona++;
    }

    public boolean bossDisponivel() {
        return batalhasNaZona >= batalhasParaBoss;
    }

    public boolean avancarZona() {
        if (zonaAtual < 3) {
            zonaAtual++;
            batalhasNaZona = 0;
            batalhasParaBoss = 3 + zonaAtual;
            System.out.println("\n+======================================+");
            System.out.println("  NOVA ZONA DESBLOQUEADA!");
            System.out.println("  >> " + getNomeZona() + " <<");
            System.out.println("+======================================+");
            mostrarAsciiZona();
            return true;
        }
        return false;
    }

    public void mostrarAsciiZona() {
        switch (zonaAtual) {
            case 1:
                System.out.println("      /\\      /\\      /\\");
                System.out.println("     /  \\    /  \\    /  \\");
                System.out.println("    /    \\  /    \\  /    \\");
                System.out.println("   /  ()  \\/  ()  \\/  ()  \\");
                System.out.println("  /________\\________\\________\\");
                System.out.println("      ||       ||       ||");
                System.out.println("  ~~~~||~~~~~~~||~~~~~~~||~~~~");
                System.out.println("     FLORESTA SOMBRIA");
                break;
            case 2:
                System.out.println("    _____________________");
                System.out.println("   /                     \\");
                System.out.println("  /   ___           ___   \\");
                System.out.println(" |   /   \\  ~~~~~  /   \\   |");
                System.out.println(" |  | ~~~ |       | ~~~ |  |");
                System.out.println(" |   \\___/  ~~~~~  \\___/   |");
                System.out.println("  \\                       /");
                System.out.println("   \\_____________________/");
                System.out.println("     CAVERNAS PROFUNDAS");
                break;
            case 3:
                System.out.println("           |>>>");
                System.out.println("           |");
                System.out.println("      _  _/|\\_  _");
                System.out.println("     |;|_|;|;|_|;|");
                System.out.println("     \\\\.    |    ./");
                System.out.println("      \\\\:  | .:://");
                System.out.println("       ||  |  ||");
                System.out.println("       ||__|__||");
                System.out.println("       |_ _|_ _|");
                System.out.println("     CASTELO DO DRAGAO");
                break;
        }
    }

    public void restaurarEstado(int zona, int batalhas, int batalhasParaBoss) {
        this.zonaAtual = zona;
        this.batalhasNaZona = batalhas;
        this.batalhasParaBoss = batalhasParaBoss;
    }

    public boolean jogoCompleto() {
        return zonaAtual >= 3 && batalhasNaZona > batalhasParaBoss;
    }
}
