package simuladorfloresta;

public class Lenhador extends Thread{
    private boolean finalizado;
    
    @Override
    public void run(){
        try{
            while(!finalizado){
                Arvore arvore = Terreno.getInstancia().retiraArvoreCorte();
                Thread.sleep(100);
                Arvore[][] arvores = Terreno.getInstancia().getArvores();
                System.out.println("Cortou arvore \n" + arvore.ImprimeDados());
                arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;                
            }
        } catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
}
