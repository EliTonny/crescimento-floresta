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
            Arvore arv;
            for (int i = 0; i < Gerenciador.NUM_CLICOS_DIA; i++) {
                arv = null;
                for (int x = 0; x < terreno.getArvores().length; x++) {
                    for (int y = 0; y < terreno.getArvores()[x].length; y++) {
                        arv = terreno.getArvores()[x][y];
                        if (arv != null) {
                            if (!Fotossintese(arv)) {
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

    private boolean Fotossintese(Arvore arv) throws Exception {
        boolean sucesso = true;
        if (arv.retiraAgua(arv.getAguaFotossintese())) {
            if (arv.retiraLuz(arv.getLuzFotossintese())) {
                if (arv.retiraSaisMinerais(arv.getSaisFotossintese())) {
                    arv.setEnergia(10);
                } else {
                    arv.setAgua(arv.getAguaFotossintese());
                    arv.setLuz(arv.getLuzFotossintese());
                    sucesso = false;
                }
            } else {
                arv.setAgua(arv.getAguaFotossintese());
                sucesso = false;
            }
        } else {
            sucesso = false;
        }
        return sucesso;
    }
}
