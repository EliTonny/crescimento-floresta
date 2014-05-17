package simuladorfloresta2.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta2.Armazem;
import simuladorfloresta2.Arvore;
import simuladorfloresta2.EnumEtapaProcesso;
import simuladorfloresta2.Galho;

public class Broto extends Etapa {

    public Broto(Armazem<Arvore> armazem) {
        super(armazem);
    }

    @Override
    public void executar(Arvore arvore) {
        if (arvore == null) {
            return;
        }
        try {
            //Custo de vida
            arvore.retiraEnergia(60);
            if (arvore.getEnergia() > 50) {
                arvore.retiraEnergia(50);
                arvore.setTamanho(5);
            }
            for (Galho galhos : arvore.getGalhos()) {
                if (arvore.getEnergia() > 100) {
                    galhos.addFolha();
                }
            }
            //Crescer galho
            if (arvore.getEnergia() > 500) {
                arvore.addGalho(true);
            }

            if (arvore.getTamanho() >= 200) {
                arvore.setEtapa(EnumEtapaProcesso.ADULTA);
            }
        } catch (Exception ex) {
            Logger.getLogger(Broto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
