package simuladorfloresta2.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta2.Armazem;
import simuladorfloresta2.Arvore;
import simuladorfloresta2.EnumEtapaProcesso;

public class Semente extends Etapa {

    public Semente(Armazem<Arvore> armazem) {
        super(armazem);
    }

    @Override
    public void executar(Arvore arvore) {
        if (arvore == null) {
            return;
        }

        try {
            if (arvore.retiraSaisMinerais(10)) {
                if (arvore.retiraAgua(10)) {
                    if (!arvore.setTamanho(1)) {
                        arvore.setSaisMinerais(10);
                        arvore.setAgua(10);
                    }
                } else {
                    arvore.setSaisMinerais(10);
                }
                if (arvore.getTamanho() == 2) {
                    arvore.addGalho(false);
                    arvore.setEtapa(EnumEtapaProcesso.BROTO);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Semente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
