package simuladorfloresta;

import java.util.Random;

public class Ambiente extends Thread {

    private Terreno terreno;

    public Ambiente(Terreno terreno) {
        this.terreno = terreno;
    }

    @Override
    public void run() {
        for (int i = 0; i < Gerenciador.NUM_CLICOS_DIA; i++) {
            Arvore arvAux = null;
            for (int x = 0; x < terreno.getArvores().length; x++) {
                for (int y = 0; y < terreno.getArvores()[x].length; y++) {
                    arvAux = terreno.getArvores()[x][y];
                    if (arvAux != null) {
                        arvAux.setAgua(getAgua());
                        arvAux.setSaisMinerais(getSaisMinerais());
                        arvAux.setLuz(getLuz());
                    }
                }
            }
        }
    }

    private int getAgua() {
        return getRandom(0, 10);
    }

    private int getLuz() {
        return getRandom(0, 10);
    }

    private int getSaisMinerais() {
        return getRandom(0, 10);
    }

    private int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
