package simuladorfloresta2;

public class Ambiente {

    private Ambiente() {
    }

    public static void Processa(Arvore arvore) {
        arvore.setAgua(getAgua());
        arvore.setSaisMinerais(getSaisMinerais());
        arvore.setLuz(getLuz());
    }

    private static int getAgua() {
        return getRandom(0, 10);
    }

    private static int getLuz() {
        return getRandom(0, 10);
    }

    private static int getSaisMinerais() {
        return getRandom(0, 10);
    }

    private static int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
