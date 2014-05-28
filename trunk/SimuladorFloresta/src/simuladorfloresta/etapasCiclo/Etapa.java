package simuladorfloresta.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;
import simuladorfloresta.Terreno_jomp;

public abstract class Etapa {

    private Armazem armazem;
    private Terreno_jomp terreno;

    protected Terreno_jomp getTerreno() {
        return terreno;
    }

    public Etapa(Armazem armazem) {
        this.armazem = armazem;
        terreno = Terreno_jomp.getInstancia();
    }

    public abstract void executar(Arvore arvore);

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
