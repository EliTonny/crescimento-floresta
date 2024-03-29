package simuladorfloresta2;

import javax.swing.JOptionPane;
import jomp.runtime.OMP;
import simuladorfloresta2.etapasCiclo.Adulta;
import simuladorfloresta2.etapasCiclo.Broto;
import simuladorfloresta2.etapasCiclo.Morte;
import simuladorfloresta2.etapasCiclo.Semente;

public class Gerenciador_jomp {


    public static final int NUM_CLICOS_DIA = 50;
    public static Gerenciador_jomp instancia;
    private Terreno ter;
    private int larguraTerreno;
    private int comprimentoTerreno;

    public static Gerenciador_jomp getinstancia() {
        if (instancia == null) {
            instancia = new Gerenciador_jomp();
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

            ter = Terreno.getInstancia();
            ter.Inicializa(larguraTerreno, comprimentoTerreno);

            for (int i = 0; i < numArvores; i++) {
                if (!ter.addArvore(new ArvorePauBrasil())) {
                    throw new Exception("Numero de arvores excede o limite permitido("
                            + ter.getNumMaxArvores()
                            + ") para o terreno");
                }
            }

            Lenhador lenhador1 = new Lenhador("Jo\u00e3o");
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

            System.out.println("Tempo de execu\u00e7\u00e3o: " + ((System.currentTimeMillis() - tempoInicial) / 1000.0) + " segundos");
            System.out.println("Processo finalizado.");
            System.out.println(ter.ImprimeDados());

            System.out.println(lenhador1.getDados());
            System.out.println(lenhador2.getDados());
            System.out.println(lenhador3.getDados());
            System.out.println("N\u00famero de \u00e1rvores no terreno: " + ter.getNumArvores());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void ProximoDia() throws Exception {

        Terreno terreno = Terreno.getInstancia();
        int numArvoresProcessadas = 0;

        boolean finalizou;
        int numCiclos = terreno.getNumArvores() * Gerenciador_jomp.NUM_CLICOS_DIA;

        //Pelo q percebi, ele usar uma thread para cada se\u00e7\u00e3o
        //n\u00e3o adianda colocar mais threads.
        OMP.setNumThreads(2);

// OMP PARALLEL BLOCK BEGINS
{
  __omp_Class0 __omp_Object0 = new __omp_Class0();
  // shared variables
  __omp_Object0.numCiclos = numCiclos;
  __omp_Object0.numArvoresProcessadas = numArvoresProcessadas;
  __omp_Object0.terreno = terreno;
  // firstprivate variables
  try {
    jomp.runtime.OMP.doParallel(__omp_Object0);
  } catch(Throwable __omp_exception) {
    System.err.println("OMP Warning: Illegal thread exception ignored!");
    System.err.println(__omp_exception);
  }
  // reduction variables
  // shared variables
  numCiclos = __omp_Object0.numCiclos;
  finalizou = __omp_Object0.finalizou;
  numArvoresProcessadas = __omp_Object0.numArvoresProcessadas;
  terreno = __omp_Object0.terreno;
}
// OMP PARALLEL BLOCK ENDS


        Armazem armMorte = new Armazem(ter.getArvoresEtapa());
        Armazem armSemente = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.SEMENTE));
        Armazem armBroto = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.BROTO));
        Armazem armAdulta = new Armazem(ter.getArvoresEtapa(EnumEtapaProcesso.ADULTA));

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

// OMP PARALLEL REGION INNER CLASS DEFINITION BEGINS
private class __omp_Class0 extends jomp.runtime.BusyTask {
  // shared variables
  int numCiclos;
  boolean finalizou;
  int numArvoresProcessadas;
  Terreno terreno;
  // firstprivate variables
  // variables to hold results of reduction

  public void go(int __omp_me) throws Throwable {
  // firstprivate variables + init
  // private variables
  // reduction variables, init to default
    // OMP USER CODE BEGINS

                  { // OMP SECTIONS BLOCK BEGINS
                  // copy of firstprivate variables, initialized
                  // copy of lastprivate variables
                  // variables to hold result of reduction
                  boolean amLast=false;
                  {
                    // firstprivate variables + init
                    // [last]private variables
                    // reduction variables + init to default
                    // -------------------------------------
                    __ompName_1: while(true) {
                    switch((int)jomp.runtime.OMP.getTicket(__omp_me)) {
                    // OMP SECTION BLOCK 0 BEGINS
                      case 0: {
                    // OMP USER CODE BEGINS

            {
                Arvore arv;
                while (true) {
                     // OMP CRITICAL BLOCK BEGINS
                     synchronized (jomp.runtime.OMP.getLockByName("")) {
                     // OMP USER CODE BEGINS

                    {
                        if (numArvoresProcessadas >= numCiclos) {
                            break;
                        }
                        numArvoresProcessadas++;
                        arv = terreno.retiraArvoreAmbiente();
                    }
                     // OMP USER CODE ENDS
                     }
                     // OMP CRITICAL BLOCK ENDS

                    if (arv != null) {
                        Ambiente.Processa(arv);
                         // OMP CRITICAL BLOCK BEGINS
                         synchronized (jomp.runtime.OMP.getLockByName("")) {
                         // OMP USER CODE BEGINS

                        {
                            terreno.setArvoreFotossintese(arv);
                        }
                         // OMP USER CODE ENDS
                         }
                         // OMP CRITICAL BLOCK ENDS

                    }
                }
                finalizou = true;
            }
                    // OMP USER CODE ENDS
                      };  break;
                    // OMP SECTION BLOCK 0 ENDS
                    // OMP SECTION BLOCK 1 BEGINS
                      case 1: {
                    // OMP USER CODE BEGINS

            {
                Arvore arv;
                while (!finalizou) {
                     // OMP CRITICAL BLOCK BEGINS
                     synchronized (jomp.runtime.OMP.getLockByName("")) {
                     // OMP USER CODE BEGINS

                    {
                        arv = terreno.retiraArvoreFotossintese();
                    }
                     // OMP USER CODE ENDS
                     }
                     // OMP CRITICAL BLOCK ENDS

                    if (arv != null) {
                        Fotossintese.Processa(arv);
                         // OMP CRITICAL BLOCK BEGINS
                         synchronized (jomp.runtime.OMP.getLockByName("")) {
                         // OMP USER CODE BEGINS

                        {
                            terreno.setArvoreAmbiente(arv);
                        }
                         // OMP USER CODE ENDS
                         }
                         // OMP CRITICAL BLOCK ENDS

                    }
                }
            }
                    // OMP USER CODE ENDS
                    amLast = true;
                      };  break;
                    // OMP SECTION BLOCK 1 ENDS

                      default: break __ompName_1;
                    } // of switch
                    } // of while
                    // call reducer
                    jomp.runtime.OMP.resetTicket(__omp_me);
                    jomp.runtime.OMP.doBarrier(__omp_me);
                    // copy lastprivate variables out
                    if (amLast) {
                    }
                  }
                  // update lastprivate variables
                  if (amLast) {
                  }
                  // update reduction variables
                  if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
                  }
                  } // OMP SECTIONS BLOCK ENDS

    // OMP USER CODE ENDS
  // call reducer
  // output to _rd_ copy
  if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
  }
  }
}
// OMP PARALLEL REGION INNER CLASS DEFINITION ENDS

}

