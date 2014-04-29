package simuladorfloresta.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;
import simuladorfloresta.Terreno;

public class Adulta extends Etapa {

    private boolean boaParaCorte;
    public Adulta(Armazem armazem) {
        super(armazem);
    }

    @Override
    public void executar(Arvore arvore) {
        if (arvore == null) {
            return;
        }
        try {
            //Custo de Vida
            arvore.retiraEnergia(60);
            if (arvore.getEnergia() > 100) {
                arvore.setTamanho(5);
            }
            if (!boaParaCorte && arvore.getTamanho() >= 400) {
                boaParaCorte = true;
                Terreno.getInstancia().addArvoreCorte(arvore);
            }
        } catch (Exception ex) {
            Logger.getLogger(Adulta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
