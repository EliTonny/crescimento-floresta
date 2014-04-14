package simuladorfloresta;

import javax.swing.JOptionPane;

public class Fotossintese extends Thread {

    private Terreno terreno;

    public Fotossintese(Terreno terreno) {
        this.terreno = terreno;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < Gerenciador.NUM_CLICOS_DIA; i++) {
                Arvore arvAux = null;
                for (int x = 0; x < terreno.getArvores().length; x++) {
                    for (int y = 0; y < terreno.getArvores()[x].length; y++) {
                        arvAux = terreno.getArvores()[x][y];
                        if (arvAux != null) {
                            arvAux.retiraAgua(1);
                            arvAux.retiraLuz(1);
                            arvAux.retiraSaisMinerais(1);
                            arvAux.setEnergia(1);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro no run da Fotossintese");
        }
    }

    private int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
