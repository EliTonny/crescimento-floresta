package simuladorfloresta.etapasCiclo;

import java.util.logging.Level;
import java.util.logging.Logger;
import simuladorfloresta.Armazem;
import simuladorfloresta.Arvore;
import simuladorfloresta.Terreno;

public class Morte extends Thread implements Etapa{

    private Armazem armazem;
    
    public Morte(Armazem armazem){
        this.armazem = armazem;
    }
    
    @Override
    public void executar(Arvore arvore) {
        if(arvore == null)
            return;
        
        try {
            //Custo de vida
            arvore.retiraEnergia(10);
            if(arvore.getEnergia() < 0){
                Arvore[][] arvores = Terreno.getInstancia().getArvores();
                arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;
            }
        } catch (Exception ex) {
            Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        while(armazem.getHaElementos().tryAcquire()){
            try {
                Object obj = armazem.retira();
                if(obj instanceof Arvore){
                    executar((Arvore) obj);
                } else {
                    throw new Exception("Armazem com objetos incompativeis");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Morte.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
