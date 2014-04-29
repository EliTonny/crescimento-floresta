package simuladorfloresta;

public class Lenhador extends Thread{
    private boolean finalizado;
    
    @Override
    public void run(){
        try{
            while(!finalizado){
                Arvore arvore = Terreno.getInstancia().retiraArvoreCorte();
                Arvore[][] arvores = Terreno.getInstancia().getArvores();
                System.out.println("Cortou arvore \n" + arvore.ImprimeDados());
                arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;                
                Thread.sleep(10);
            }
        } catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
}
