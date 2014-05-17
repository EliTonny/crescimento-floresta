package simuladorfloresta2.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta2.Armazem;
import simuladorfloresta2.Arvore;

public abstract class Etapa extends Thread {

    private Armazem<Arvore> armazem;

    public Etapa(Armazem<Arvore> armazem) {
        this.armazem = armazem;
    }

    public abstract void executar(Arvore arvore);

    @Override
    public void run() {
        while (armazem.getHaElementos().tryAcquire()) {
            try {
                executar(armazem.retira());
            } catch (Exception ex) {
                Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
