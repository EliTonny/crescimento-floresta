package simuladorfloresta;

import javax.swing.JOptionPane;
import simuladorfloresta.etapasCiclo.Morte;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 50;
    public static Gerenciador instancia;
    private Terreno ter;
    private Ambiente amb;
    private Fotossintese fot;
    private int larguraTerreno;
    private int comprimentoTerreno;

    public static Gerenciador getinstancia() {
        if(instancia == null){
            instancia = new Gerenciador();
        }
        return instancia;
    }
    
    private Gerenciador(){
    }

    public void Iniciar(
            int larguraTerreno,
            int comprimentoTerreno,
            int numArvores,
            int dias) {
        try {
            this.larguraTerreno = larguraTerreno;
            this.comprimentoTerreno = comprimentoTerreno;
            
            ter = Terreno.getInstancia();
            amb = new Ambiente(ter);
            fot = new Fotossintese(ter);

            for (int i = 0; i < numArvores; i++) {
                if (!ter.addArvore(new ArvorePauBrasil())) {
                    throw new Exception("Numero de arvores excede o limite permitido("
                            + ter.getNumMaxArvores()
                            + ") para o terreno");
                }
            }

            for (int i = 0; i < dias; i++) {
                ProximoDia();
                System.out.println("Dia " + (i + 1));
            }
            System.out.println("Processo finalizado.\n");
            System.out.println(ter.ImprimeDados());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void ProximoDia() throws InterruptedException {
        amb = new Ambiente(ter);
        fot = new Fotossintese(ter);

        amb.start();
        fot.start();

        amb.join();
        Thread.sleep(1);
        fot.setFinalizar(true);
        fot.join();
        
        Armazem armMorte = new Armazem(ter.getArvoresEtapa());
        
        /*Morte morte1 = new Morte(armMorte);
        Morte morte2 = new Morte(armMorte);
        Morte morte3 = new Morte(armMorte);
        
        morte1.start();
        morte2.start();
        morte3.start();*/
    }

    public int getLarguraTerreno() {
        return larguraTerreno;
    }

    public int getComprimentoTerreno() {
        return comprimentoTerreno;
    }
}
