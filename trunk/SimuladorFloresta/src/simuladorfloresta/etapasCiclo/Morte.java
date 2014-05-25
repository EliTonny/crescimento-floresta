package simuladorfloresta.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;
import simuladorfloresta.Terreno;

public class Morte extends Etapa {

    public Morte(Armazem armazem) {
        super(armazem);
    }

    @Override
    public void executar(Arvore arvore) {
        try {
            if (arvore == null) {
                return;
            }
            if (arvore.getEnergia() < 0) {
                this.getTerreno().killArvore(arvore);
            }
        } catch (Exception ex) {
            Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
