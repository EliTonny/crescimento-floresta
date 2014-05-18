package simuladorfloresta2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import simuladorfloresta2.etapasCiclo.Adulta;
import simuladorfloresta2.etapasCiclo.Broto;
import simuladorfloresta2.etapasCiclo.Morte;
import simuladorfloresta2.etapasCiclo.Semente;

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

            Lenhador lenhador1 = new Lenhador("João");
            Lenhador lenhador2 = new Lenhador("Paulo");
            Lenhador lenhador3 = new Lenhador("Ricardo");

            lenhador1.start();
            lenhador2.start();
            lenhador3.start();

            long tempoInicial = System.currentTimeMillis();
            for (int i = 0; i < dias; i++) {
                ProximoDia();
                System.out.println("Dia " + (i + 1));
            }
            lenhador1.setFinalizado(true);
            lenhador2.setFinalizado(true);
            lenhador3.setFinalizado(true);
            ter.setFinalizarProcesso(true);
            
            lenhador1.join();
            lenhador2.join();
            lenhador3.join();

            System.out.println("Tempo de execução: " + ((System.currentTimeMillis() - tempoInicial) / 1000.0) + " segundos");
            System.out.println("Processo finalizado.");
            System.out.println(ter.ImprimeDados());

            System.out.println(lenhador1.getDados());
            System.out.println(lenhador2.getDados());
            System.out.println(lenhador3.getDados());
            System.out.println("Número de árvores no terreno: " + ter.getNumArvores());

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
        Armazem<Arvore> armMorte = new Armazem(ter.getArvoresEtapa());
        Armazem<Arvore> armSemente = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.SEMENTE));
        Armazem<Arvore> armBroto = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.BROTO));
        Armazem<Arvore> armAdulta = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.ADULTA));

        Morte morte1 = new Morte(armMorte);
        Morte morte2 = new Morte(armMorte);
        Morte morte3 = new Morte(armMorte);

        Semente semente1 = new Semente(armSemente);
        Semente semente2 = new Semente(armSemente);
        Semente semente3 = new Semente(armSemente);

        Broto broto1 = new Broto(armBroto);
        Broto broto2 = new Broto(armBroto);
        Broto broto3 = new Broto(armBroto);

        Adulta adulta1 = new Adulta(armAdulta);
        Adulta adulta2 = new Adulta(armAdulta);
        Adulta adulta3 = new Adulta(armAdulta);

        morte1.start();
        morte2.start();
        morte3.start();

        semente1.start();
        semente2.start();
        semente3.start();

        broto1.start();
        broto2.start();
        broto3.start();

        adulta1.start();
        adulta2.start();
        adulta3.start();

        morte1.join();
        morte2.join();
        morte3.join();

        semente1.join();
        semente2.join();
        semente3.join();

        broto1.join();
        broto2.join();
        broto3.join();

        adulta1.join();
        adulta2.join();
        adulta3.join();
    }

    public int getLarguraTerreno() {
        return larguraTerreno;
    }

    public int getComprimentoTerreno() {
        return comprimentoTerreno;
    }
}