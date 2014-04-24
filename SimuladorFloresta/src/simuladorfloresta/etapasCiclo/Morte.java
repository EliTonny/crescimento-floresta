package simuladorfloresta.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;
import simuladorfloresta.Terreno;

public class Morte extends Etapa{

    public Morte(Armazem armazem) {
        super(armazem);
    }
    
    @Override
    public void executar(Arvore arvore) {
        if(arvore == null)
            return;
        
        try {
            if(arvore.getEnergia() < 0){
                Arvore[][] arvores = Terreno.getInstancia().getArvores();
                arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;
            }
        } catch (Exception ex) {
            Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
