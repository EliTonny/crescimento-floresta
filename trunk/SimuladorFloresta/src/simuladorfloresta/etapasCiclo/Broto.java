package simuladorfloresta.etapasCiclo;

import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;

public class Broto extends Etapa{

    public Broto(Armazem armazem) {
        super(armazem);
    }    

    @Override
    public void executar(Arvore arvore) {
        if(arvore == null)
            return;
        
    }
}
