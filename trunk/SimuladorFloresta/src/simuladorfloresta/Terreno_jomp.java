package simuladorfloresta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import jomp.runtime.OMP;

public class Terreno_jomp {


    private int XMax;
    private int YMax;
    private Arvore[][] arvores;
    private int numArvores;
    private int numMaxArvores;
    private final int POSICOES_POR_METRO = 2;
    public static final int ARVORES_POR_METRO2 = 4;
    private static Terreno_jomp instancia;
    private Queue arvoresAmbiente;
    private Queue arvoresFotossintese;
    private boolean finalizarProcesso;

    public void setFinalizarProcesso(boolean finalizarProcesso) {
        this.finalizarProcesso = finalizarProcesso;
    }
    private Queue arvoresCorte;

    public int getNumArvores() {
        return numArvores;
    }

    public static Terreno_jomp getInstancia() {
        if (instancia == null) {
            instancia = new Terreno_jomp();
        }
        return instancia;
    }

    public int getNumMaxArvores() {
        return numMaxArvores;
    }

    /**
     * @param largura Largura do terreno em metros
     * @param comprimento comprimento do terreno em metros
     */
    public void Inicializa(int largura, int comprimento) {
        this.XMax = comprimento;
        this.YMax = largura;
        arvores = new Arvore[comprimento * POSICOES_POR_METRO][largura * POSICOES_POR_METRO];
        numMaxArvores = largura * POSICOES_POR_METRO
                * comprimento * POSICOES_POR_METRO;
        numArvores = 0;
        arvoresAmbiente = new ArrayDeque();
        arvoresFotossintese = new ArrayDeque();
        arvoresCorte = new ArrayDeque();
    }

    public synchronized boolean killArvore(Arvore arvore) {
        //Verifica-se se a arvore do terreno \u00e9 a mesma passada por parametros.
        //Essa verifica\u00e7\u00e3o \u00e9 necess\u00e1ria pois em alguns momentos, a arvore do terreno
        //pode morrer, e outro processo pode tentar matar a mesma arvore
        if(arvore == null)
            System.out.println("nula");
        if(arvore.isMorta())
            return true;
        if (arvore.equals(arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()])) {
            arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;
            this.numArvores--;
            System.out.println("Arvore Morta: " + arvore.getPosicao().toString());
            arvore.setMorta(true);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void addArvoreCorte(Arvore arvore) {
        if(arvore.isMorta())
        {
            System.out.println("Arvore Nula - addArvoreCorte");
            return;
        }
        if (!arvoresCorte.contains(arvore)) {
            arvoresCorte.add(arvore);
            notify();
        }
    }

    public synchronized Arvore retiraArvoreCorte() throws InterruptedException {
        while (arvoresCorte.isEmpty()) {
            wait(1000);
            if (finalizarProcesso) {
                return null;
            }
        }
        Object obj = arvoresCorte.poll();
        if(obj instanceof Arvore){
            return (Arvore) obj;
        } else {
            return null;
        }
    }

    public void setArvoreFotossintese(Arvore arvore) {
        arvoresFotossintese.add(arvore);
    }

    /**
     * Adiciona uma arvore em uma posi\u00e7\u00e3o aleat\u00f3ria do terreno
     *
     * @return Retorna false se n\u00e3o houver mais espa\u00e7o no terreno
     *
     */
    public synchronized boolean addArvore(Arvore arvore) throws Exception {
        Posicao pos;
        if (this.numArvores == this.numMaxArvores) {
            return false;
        }
        //arvore.setTerreno_jomp(this);
        pos = this.procuraPosicaoAleatoria();
        arvore.setPosicao(pos);
        arvores[pos.getX()][pos.getY()] = arvore;
        numArvores++;
        return true;
    }

    public void addArvoreFilha(Arvore arvoreFilha, Arvore arvoreMae) {
        //fazer calculo baseado na posicao da m\u00e3e, 
        //para definir a nova posicao d arvore filha
    }

    /**
     * M\u00e9todo que retorna uma posi\u00e7\u00e3o aleat\u00f3ria que esteja dispon\u00edvel no
     * terreno*
     */
    private Posicao procuraPosicaoAleatoria() throws Exception {
        Posicao pos = null;

        int XAleatorio = (int) (Math.random() * (this.XMax * POSICOES_POR_METRO));
        int YAleatorio = (int) (Math.random() * (this.YMax * POSICOES_POR_METRO));

        for (int x = XAleatorio; x < this.XMax * POSICOES_POR_METRO; x++) {
            for (int y = YAleatorio; y < this.YMax * POSICOES_POR_METRO; y++) {
                if (arvores[x][y] == null) {
                    pos = new Posicao(x, y);
                    return pos;

                }
            }
        }
        if (pos == null) {
            for (int x = 0; x < this.XMax * POSICOES_POR_METRO; x++) {
                for (int y = 0; y < this.YMax * POSICOES_POR_METRO; y++) {
                    if (arvores[x][y] == null) {
                        pos = new Posicao(x, y);
                        return pos;
                    }
                }
            }
        }
        return pos;
    }

    public String ImprimeDados() {
        String saida = "";
        for (int x = 0; x < this.XMax * POSICOES_POR_METRO; x++) {
            for (int y = 0; y < this.YMax * POSICOES_POR_METRO; y++) {
                if (arvores[x][y] != null) {
                    saida += "X: " + x + " Y:" + y;
                    saida += "\n";
                    saida += arvores[x][y].ImprimeDados();
                    saida += "\n";
                }
            }
        }
        return saida;
    }

    public ArrayList getArvoresEtapa(EnumEtapaProcesso etapa) {
        OMP.setNumThreads(10);
        ArrayList arvoresRetorno = new ArrayList();

// OMP PARALLEL BLOCK BEGINS
{
  __omp_Class0 __omp_Object0 = new __omp_Class0();
  // shared variables
  __omp_Object0.arvoresRetorno = arvoresRetorno;
  __omp_Object0.etapa = etapa;
  // firstprivate variables
  try {
    jomp.runtime.OMP.doParallel(__omp_Object0);
  } catch(Throwable __omp_exception) {
    System.err.println("OMP Warning: Illegal thread exception ignored!");
    System.err.println(__omp_exception);
  }
  // reduction variables
  // shared variables
  arvoresRetorno = __omp_Object0.arvoresRetorno;
  etapa = __omp_Object0.etapa;
}
// OMP PARALLEL BLOCK ENDS

        return arvoresRetorno;
    }

    public ArrayList getArvoresEtapa() {
        
        ArrayList arvoresRetorno = new ArrayList();
        
        OMP.setNumThreads(10);

// OMP PARALLEL BLOCK BEGINS
{
  __omp_Class4 __omp_Object4 = new __omp_Class4();
  // shared variables
  __omp_Object4.arvoresRetorno = arvoresRetorno;
  // firstprivate variables
  try {
    jomp.runtime.OMP.doParallel(__omp_Object4);
  } catch(Throwable __omp_exception) {
    System.err.println("OMP Warning: Illegal thread exception ignored!");
    System.err.println(__omp_exception);
  }
  // reduction variables
  // shared variables
  arvoresRetorno = __omp_Object4.arvoresRetorno;
}
// OMP PARALLEL BLOCK ENDS

        return arvoresRetorno;
    }

    Arvore retiraArvoreAmbiente() {
        return (Arvore)arvoresAmbiente.poll();
    }

    Arvore retiraArvoreFotossintese() {
        return (Arvore)arvoresFotossintese.poll();
    }

    void setArvoreAmbiente(Arvore arv) {
        arvoresAmbiente.add(arv);
    }
    
    public int CarregaArvoresDisponiveis() {
        Queue saida = new ArrayDeque();
        int numeroArvores = 0;
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[i].length; j++) {
                if (arvores[i][j] != null) {
                    saida.add(arvores[i][j]);
                    numeroArvores++;
                }
            }
        }
        this.arvoresAmbiente = saida;
        return numeroArvores;
    }
    public int CarregaArvoresDisponiveisOMP() {
        Queue saida = new ArrayDeque();
        int numeroArvores = 0;
        
        OMP.setNumThreads(arvores.length);

// OMP PARALLEL BLOCK BEGINS
{
  __omp_Class8 __omp_Object8 = new __omp_Class8();
  // shared variables
  __omp_Object8.saida = saida;
  // firstprivate variables
  try {
    jomp.runtime.OMP.doParallel(__omp_Object8);
  } catch(Throwable __omp_exception) {
    System.err.println("OMP Warning: Illegal thread exception ignored!");
    System.err.println(__omp_exception);
  }
  // reduction variables
  numeroArvores  += __omp_Object8._rd_numeroArvores;
  // shared variables
  saida = __omp_Object8.saida;
}
// OMP PARALLEL BLOCK ENDS

        
        this.arvoresAmbiente = saida;
        return numeroArvores;
    }

// OMP PARALLEL REGION INNER CLASS DEFINITION BEGINS
private class __omp_Class8 extends jomp.runtime.BusyTask {
  // shared variables
  Queue saida;
  // firstprivate variables
  // variables to hold results of reduction
  int _rd_numeroArvores;

  public void go(int __omp_me) throws Throwable {
  // firstprivate variables + init
  // private variables
  // reduction variables, init to default
  int numeroArvores = 0;
    // OMP USER CODE BEGINS

        {
            int myId = OMP.getThreadNum();
            for (int i = 0; i < arvores[myId].length; i++) {
                if (arvores[myId][i] != null) {
                     // OMP CRITICAL BLOCK BEGINS
                     synchronized (jomp.runtime.OMP.getLockByName("")) {
                     // OMP USER CODE BEGINS

                    {
                        saida.add(arvores[myId][i]);
                    }
                     // OMP USER CODE ENDS
                     }
                     // OMP CRITICAL BLOCK ENDS

                    numeroArvores++;
                }
            }
        }
    // OMP USER CODE ENDS
  // call reducer
  numeroArvores = (int) jomp.runtime.OMP.doPlusReduce(__omp_me, numeroArvores);
  // output to _rd_ copy
  if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
    _rd_numeroArvores = numeroArvores;
  }
  }
}
// OMP PARALLEL REGION INNER CLASS DEFINITION ENDS



// OMP PARALLEL REGION INNER CLASS DEFINITION BEGINS
private class __omp_Class4 extends jomp.runtime.BusyTask {
  // shared variables
  ArrayList arvoresRetorno;
  // firstprivate variables
  // variables to hold results of reduction

  public void go(int __omp_me) throws Throwable {
  // firstprivate variables + init
  // private variables
  // reduction variables, init to default
    // OMP USER CODE BEGINS

        {
             { // OMP FOR BLOCK BEGINS
             // copy of firstprivate variables, initialized
             // copy of lastprivate variables
             // variables to hold result of reduction
             boolean amLast=false;
             {
               // firstprivate variables + init
               // [last]private variables
               // reduction variables + init to default
               // -------------------------------------
               jomp.runtime.LoopData __omp_WholeData6 = new jomp.runtime.LoopData();
               jomp.runtime.LoopData __omp_ChunkData5 = new jomp.runtime.LoopData();
               __omp_WholeData6.start = (long)( 0);
               __omp_WholeData6.stop = (long)( arvores.length);
               __omp_WholeData6.step = (long)(1);
               jomp.runtime.OMP.setChunkStatic(__omp_WholeData6);
               while(!__omp_ChunkData5.isLast && jomp.runtime.OMP.getLoopStatic(__omp_me, __omp_WholeData6, __omp_ChunkData5)) {
               for(;;) {
                 if(__omp_WholeData6.step > 0) {
                    if(__omp_ChunkData5.stop > __omp_WholeData6.stop) __omp_ChunkData5.stop = __omp_WholeData6.stop;
                    if(__omp_ChunkData5.start >= __omp_WholeData6.stop) break;
                 } else {
                    if(__omp_ChunkData5.stop < __omp_WholeData6.stop) __omp_ChunkData5.stop = __omp_WholeData6.stop;
                    if(__omp_ChunkData5.start > __omp_WholeData6.stop) break;
                 }
                 for(int i = (int)__omp_ChunkData5.start; i < __omp_ChunkData5.stop; i += __omp_ChunkData5.step) {
                   // OMP USER CODE BEGINS
 {
                for (int j = 0; j < arvores[0].length; j++) {
                    if (arvores[i][j] != null) {
                         // OMP CRITICAL BLOCK BEGINS
                         synchronized (jomp.runtime.OMP.getLockByName("")) {
                         // OMP USER CODE BEGINS

                        {
                            arvoresRetorno.add(arvores[i][j]);
                        }
                         // OMP USER CODE ENDS
                         }
                         // OMP CRITICAL BLOCK ENDS

                    }
                }
            }
                   // OMP USER CODE ENDS
                   if (i == (__omp_WholeData6.stop-1)) amLast = true;
                 } // of for 
                 if(__omp_ChunkData5.startStep == 0)
                   break;
                 __omp_ChunkData5.start += __omp_ChunkData5.startStep;
                 __omp_ChunkData5.stop += __omp_ChunkData5.startStep;
               } // of for(;;)
               } // of while
               // call reducer
               jomp.runtime.OMP.doBarrier(__omp_me);
               // copy lastprivate variables out
               if (amLast) {
               }
             }
             // set global from lastprivate variables
             if (amLast) {
             }
             // set global from reduction variables
             if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
             }
             } // OMP FOR BLOCK ENDS

        }
    // OMP USER CODE ENDS
  // call reducer
  // output to _rd_ copy
  if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
  }
  }
}
// OMP PARALLEL REGION INNER CLASS DEFINITION ENDS



// OMP PARALLEL REGION INNER CLASS DEFINITION BEGINS
private class __omp_Class0 extends jomp.runtime.BusyTask {
  // shared variables
  ArrayList arvoresRetorno;
  EnumEtapaProcesso etapa;
  // firstprivate variables
  // variables to hold results of reduction

  public void go(int __omp_me) throws Throwable {
  // firstprivate variables + init
  // private variables
  // reduction variables, init to default
    // OMP USER CODE BEGINS

        {
             { // OMP FOR BLOCK BEGINS
             // copy of firstprivate variables, initialized
             // copy of lastprivate variables
             // variables to hold result of reduction
             boolean amLast=false;
             {
               // firstprivate variables + init
               // [last]private variables
               // reduction variables + init to default
               // -------------------------------------
               jomp.runtime.LoopData __omp_WholeData2 = new jomp.runtime.LoopData();
               jomp.runtime.LoopData __omp_ChunkData1 = new jomp.runtime.LoopData();
               __omp_WholeData2.start = (long)( 0);
               __omp_WholeData2.stop = (long)( arvores.length);
               __omp_WholeData2.step = (long)(1);
               jomp.runtime.OMP.setChunkStatic(__omp_WholeData2);
               while(!__omp_ChunkData1.isLast && jomp.runtime.OMP.getLoopStatic(__omp_me, __omp_WholeData2, __omp_ChunkData1)) {
               for(;;) {
                 if(__omp_WholeData2.step > 0) {
                    if(__omp_ChunkData1.stop > __omp_WholeData2.stop) __omp_ChunkData1.stop = __omp_WholeData2.stop;
                    if(__omp_ChunkData1.start >= __omp_WholeData2.stop) break;
                 } else {
                    if(__omp_ChunkData1.stop < __omp_WholeData2.stop) __omp_ChunkData1.stop = __omp_WholeData2.stop;
                    if(__omp_ChunkData1.start > __omp_WholeData2.stop) break;
                 }
                 for(int i = (int)__omp_ChunkData1.start; i < __omp_ChunkData1.stop; i += __omp_ChunkData1.step) {
                   // OMP USER CODE BEGINS
 {
                for (int j = 0; j < arvores[0].length; j++) {
                    if (arvores[i][j] != null && arvores[i][j].getEtapa() == etapa) {
                         // OMP CRITICAL BLOCK BEGINS
                         synchronized (jomp.runtime.OMP.getLockByName("")) {
                         // OMP USER CODE BEGINS

                        {
                            arvoresRetorno.add(arvores[i][j]);
                        }
                         // OMP USER CODE ENDS
                         }
                         // OMP CRITICAL BLOCK ENDS

                    }
                }
            }
                   // OMP USER CODE ENDS
                   if (i == (__omp_WholeData2.stop-1)) amLast = true;
                 } // of for 
                 if(__omp_ChunkData1.startStep == 0)
                   break;
                 __omp_ChunkData1.start += __omp_ChunkData1.startStep;
                 __omp_ChunkData1.stop += __omp_ChunkData1.startStep;
               } // of for(;;)
               } // of while
               // call reducer
               jomp.runtime.OMP.doBarrier(__omp_me);
               // copy lastprivate variables out
               if (amLast) {
               }
             }
             // set global from lastprivate variables
             if (amLast) {
             }
             // set global from reduction variables
             if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
             }
             } // OMP FOR BLOCK ENDS

        }
    // OMP USER CODE ENDS
  // call reducer
  // output to _rd_ copy
  if (jomp.runtime.OMP.getThreadNum(__omp_me) == 0) {
  }
  }
}
// OMP PARALLEL REGION INNER CLASS DEFINITION ENDS

}

