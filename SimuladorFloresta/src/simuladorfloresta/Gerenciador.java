package simuladorfloresta;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 50;
    public static Gerenciador instancia;
    private Terreno ter;
    private int larguraTerreno;
    private int comprimentoTerreno;
    AtomicBoolean ambienteFinalizado;

    public static Gerenciador getinstancia() {
        if (instancia == null) {
            instancia = new Gerenciador();
        }
        return instancia;
    }

    private Gerenciador() {
        ambienteFinalizado = new AtomicBoolean(false);
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
            ter.Inicializa(larguraTerreno, comprimentoTerreno, ambienteFinalizado);
            
            for (int i = 0; i < numArvores; i++) {
                if (!ter.addArvore(new ArvorePauBrasil())) {
                    throw new Exception("Numero de arvores excede o limite permitido("
                            + ter.getNumMaxArvores()
                            + ") para o terreno");
                }
            }

            long tempoInicial = System.currentTimeMillis();
            for (int i = 0; i < dias; i++) {
                ProximoDia();
                System.out.println("Dia " + (i + 1));
            }
            System.out.println("Tempo de execução: " + ((System.currentTimeMillis() - tempoInicial)/1000.0) + " segundos");
            System.out.println("Processo finalizado.");
            //System.out.println(ter.ImprimeDados());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void ProximoDia() throws InterruptedException {
        
        AtomicInteger numArvoresProcessadas = new AtomicInteger(0);
        ambienteFinalizado.set(false);
        
        Ambiente amb1 = new Ambiente(ter, ambienteFinalizado, numArvoresProcessadas);
        Ambiente amb2 = new Ambiente(ter, ambienteFinalizado, numArvoresProcessadas);
        Ambiente amb3 = new Ambiente(ter, ambienteFinalizado, numArvoresProcessadas);
        Ambiente amb4 = new Ambiente(ter, ambienteFinalizado, numArvoresProcessadas);
        
        Fotossintese fot1 = new Fotossintese(ter);
        Fotossintese fot2 = new Fotossintese(ter);
        Fotossintese fot3 = new Fotossintese(ter);
        Fotossintese fot4 = new Fotossintese(ter);

        amb1.start();
        amb2.start();
        amb3.start();
        amb4.start();
        
        fot1.start();
        fot2.start();
        fot3.start();
        fot4.start();

        amb1.join();
        amb2.join();
        amb3.join();
        amb4.join();
        
        fot1.join();
        fot2.join();
        fot3.join();
        fot4.join();

        this.ambienteFinalizado.set(false);
        /*Armazem armMorte = new Armazem(ter.getArvoresEtapa());
        
         Morte morte1 = new Morte(armMorte);
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
