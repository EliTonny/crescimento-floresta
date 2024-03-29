package simuladorfloresta;

import javax.swing.JOptionPane;
import jomp.runtime.OMP;
import simuladorfloresta.etapasCiclo.Adulta;
import simuladorfloresta.etapasCiclo.Broto;
import simuladorfloresta.etapasCiclo.Morte;
import simuladorfloresta.etapasCiclo.Semente;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 50;
    public static Gerenciador instancia;
    private Terreno_jomp ter;
    private int larguraTerreno;
    private int comprimentoTerreno;

    public static Gerenciador getinstancia() {
        if (instancia == null) {
            instancia = new Gerenciador();
        }
        return instancia;
    }

    public void Iniciar(
            int larguraTerreno,
            int comprimentoTerreno,
            int numArvores,
            int dias) {
        try {
            this.larguraTerreno = larguraTerreno;
            this.comprimentoTerreno = comprimentoTerreno;

            ter = Terreno_jomp.getInstancia();
            ter.Inicializa(larguraTerreno, comprimentoTerreno);

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

    private void ProximoDia() throws Exception {
        int numArvoresProcessadas = 0;
        int numArvores = ter.CarregaArvoresDisponiveisOMP();
        boolean finalizou;
        int numCiclos = numArvores * Gerenciador.NUM_CLICOS_DIA;
        OMP.setNumThreads(20);
        System.out.println("Numero de arvores que serão processadas: " + numArvores);
        //omp parallel sections
        {
            //omp section
            {
                System.out.println("Ambiente");
                Arvore arv;
                int contador = 0;
                while (numArvoresProcessadas < numCiclos) {
                    //omp critical
                    {
                        arv = ter.retiraArvoreAmbiente();
                    }
                    if (arv != null) {
                        System.out.println("Ambiente processou arvore numero " + contador++);
                        numArvoresProcessadas++;
                        Ambiente.Processa(arv);
                        //omp critical
                        {
                            ter.setArvoreFotossintese(arv);
                        }
                    }
                }
                finalizou = true;
                System.out.println("Fim ambiente");
            }
            //omp section
            {
                System.out.println("Fotossintese");
                Arvore arv;
                int contador = 0;
                while (!finalizou) {
                    //omp critical
                    {
                        arv = ter.retiraArvoreFotossintese();
                    }
                    if (arv != null) {
                        System.out.println("Fotossintese processou arvore numero " + contador++);
                        Fotossintese.Processa(arv);
                        //omp critical
                        {
                            ter.setArvoreAmbiente(arv);
                        }
                    }
                }
                System.out.println("Fim fotossintese");
            }
        }

        System.out.println("Fim ambiente e fotossintese");
        Thread.sleep(3000);
        Armazem armMorte = new Armazem(ter.getArvoresEtapa());
        Armazem armSemente = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.SEMENTE));
        Armazem armBroto = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.BROTO));
        Armazem armAdulta = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.ADULTA));
        /*Morte morte1 = new Morte(armMorte);
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
         Adulta adulta3 = new Adulta(armAdulta);*/

        /*morte1.start();
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
         adulta3.join();*/
    }

    public int getLarguraTerreno() {
        return larguraTerreno;
    }

    public int getComprimentoTerreno() {
        return comprimentoTerreno;
    }

    private void x(String x) {
        System.out.println(x);
    }
}
