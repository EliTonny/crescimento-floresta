package simuladorfloresta;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Fotossintese extends Thread {

    private Terreno terreno;
    //private boolean finalizar;

    /*public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }*/

    public Fotossintese(Terreno terreno) {
        this.terreno = terreno;
        //finalizar = false;
    }

    @Override
    public void run() {
        try {
            //int contador = 0;
            while (true) {
                //contador++;
                Arvore arv = this.terreno.retiraArvoreFotossintese();
                if (arv == null) {
                    //System.out.println("Retornou NULL no retiraArvoreFotossintese. " + contador);
                    break;
                }
                if (!Fotossintese(arv)) {
                   /* if (this.finalizar) {
                        break;
                    }*/
                }
                this.terreno.setArvoreAmbiente(arv);
            }
            //this.terreno.addArvore(new ArvorePauBrasil());

        } catch (InterruptedException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Fotossintese.class.getName()).log(Level.SEVERE, null, ex);
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
