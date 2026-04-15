import java.util.Random;

@SuppressWarnings("java:S106")
public class Util {
    private static final Random rnd = new Random();

    public static int rolar(int lados) {
        return 1 + rnd.nextInt(lados);
    }

    public static int rolarD20() {
        return rolar(20);
    }

    public static boolean testeChance(int dificuldade) {
        return rolarD20() >= dificuldade;
    }

    public static void digitarLento(String texto, int delayMs) {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }

    public static void digitarLento(String texto) {
        digitarLento(texto, 30);
    }

    public static void pausa(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public static void suspense() {
        System.out.print(".");
        pausa(600);
        System.out.print(".");
        pausa(600);
        System.out.println(".");
        pausa(600);
    }

    public static void limparTela() {
        System.out.println("\n".repeat(3));
    }

    public static String barraVida(int atual, int max, int tamanho) {
        int preenchido = (int) ((double) atual / max * tamanho);
        if (preenchido < 0) preenchido = 0;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            if (i < preenchido) sb.append("#");
            else sb.append("-");
        }
        sb.append("] ").append(atual).append("/").append(max);
        return sb.toString();
    }

    public static String barraMana(int atual, int max, int tamanho) {
        int preenchido = (int) ((double) atual / max * tamanho);
        if (preenchido < 0) preenchido = 0;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            if (i < preenchido) sb.append("~");
            else sb.append("-");
        }
        sb.append("] ").append(atual).append("/").append(max);
        return sb.toString();
    }

    public static int randomEntre(int min, int max) {
        return min + rnd.nextInt(max - min + 1);
    }

    public static String centralizar(String texto, int largura) {
        if (texto.length() >= largura) return texto;
        int espaco = (largura - texto.length()) / 2;
        return " ".repeat(espaco) + texto;
    }
}
