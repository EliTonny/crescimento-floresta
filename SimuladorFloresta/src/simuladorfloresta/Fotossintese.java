package simuladorfloresta;

import javax.swing.JOptionPane;

public class Fotossintese extends Thread {

    private Terreno terreno;
    private boolean finalizar;

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }

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
                            if (arvAux.retiraAgua(100)) {
                                if (arvAux.retiraLuz(1)) {
                                    if (arvAux.retiraSaisMinerais(1)) {
                                        arvAux.setEnergia(10);
                                    } else {
                                        arvAux.setAgua(100);
                                        arvAux.setLuz(1);
                                        if (this.finalizar) {
                                            return;
                                        }
                                    }
                                } else {
                                    arvAux.setAgua(100);
                                    if (this.finalizar) {
                                        return;
                                    }
                                }
                            } else {
                                if (this.finalizar) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro no run da Fotossintese");
        }
    }
}
