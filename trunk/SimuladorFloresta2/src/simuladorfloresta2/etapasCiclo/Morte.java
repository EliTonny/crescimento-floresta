package simuladorfloresta2.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta2.Armazem;
import simuladorfloresta2.Arvore;
import simuladorfloresta2.Terreno;

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
                Terreno.getInstancia().killArvore(arvore);
            }
        } catch (Exception ex) {
            Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
