package simuladorfloresta;

import javax.swing.JOptionPane;

public class Fotossintese extends Thread {

    private Terreno terreno;
    private boolean finalizar;
    private final int QTD_AGUA = 6;
    private final int QTD_LUZ = 6;
    private final int QTD_SAIS = 6;

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
                Arvore arvAux;
                for (int x = 0; x < terreno.getArvores().length; x++) {
                    for (int y = 0; y < terreno.getArvores()[x].length; y++) {
                        arvAux = terreno.getArvores()[x][y];
                        if (arvAux != null) {
                            if (arvAux.retiraAgua(QTD_AGUA)) {
                                if (arvAux.retiraLuz(QTD_LUZ)) {
                                    if (arvAux.retiraSaisMinerais(QTD_SAIS)) {
                                        arvAux.setEnergia(10);
                                    } else {
                                        arvAux.setAgua(QTD_AGUA);
                                        arvAux.setLuz(QTD_LUZ);
                                        if (this.finalizar) {
                                            return;
                                        }
                                    }
                                } else {
                                    arvAux.setAgua(QTD_AGUA);
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
