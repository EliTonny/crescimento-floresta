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
    private Queue arvoresCorte;
    
    public void setFinalizarProcesso(boolean finalizarProcesso) {
        this.finalizarProcesso = finalizarProcesso;
    }

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
        if (arvore == null) {
            System.out.println("nula");
        }
        if (arvore.isMorta()) {
            return true;
        }
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
        if (obj instanceof Arvore) {
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

        //xomp parallel shared(arvoresRetorno)
        {
            //xomp for
            for (int i = 0; i < arvores.length; i++) {
                for (int j = 0; j < arvores[0].length; j++) {
                    if (arvores[i][j] != null && arvores[i][j].getEtapa() == etapa) {
                        //xomp critical
                        {
                            arvoresRetorno.add(arvores[i][j]);
                        }
                    }
                }
            }
        }
        return arvoresRetorno;
    }

    public ArrayList getArvoresEtapa() {

        ArrayList arvoresRetorno = new ArrayList();

        OMP.setNumThreads(10);
        //xomp parallel shared(arvoresRetorno)
        {
            //xomp for
            for (int i = 0; i < arvores.length; i++) {
                for (int j = 0; j < arvores[0].length; j++) {
                    if (arvores[i][j] != null) {
                        //xomp critical
                        {
                            arvoresRetorno.add(arvores[i][j]);
                        }
                    }
                }
            }
        }
        return arvoresRetorno;
    }

    Arvore retiraArvoreAmbiente() {
        return (Arvore) arvoresAmbiente.poll();
    }

    Arvore retiraArvoreFotossintese() {
        return (Arvore) arvoresFotossintese.poll();
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
        ArrayDeque saida = new ArrayDeque();
        int numeroArvores = 0;

        OMP.setNumThreads(arvores.length);

// OMP PARALLEL BLOCK BEGINS
{
  __omp_Class0 __omp_Object0 = new __omp_Class0();
  // shared variables
  __omp_Object0.saida = saida;
  // firstprivate variables
  try {
    jomp.runtime.OMP.doParallel(__omp_Object0);
  } catch(Throwable __omp_exception) {
    System.err.println("OMP Warning: Illegal thread exception ignored!");
    System.err.println(__omp_exception);
  }
  // reduction variables
  numeroArvores  += __omp_Object0._rd_numeroArvores;
  // shared variables
  saida = __omp_Object0.saida;
}
// OMP PARALLEL BLOCK ENDS


        this.arvoresAmbiente = saida;
        return numeroArvores;
    }

// OMP PARALLEL REGION INNER CLASS DEFINITION BEGINS
private class __omp_Class0 extends jomp.runtime.BusyTask {
  // shared variables
  ArrayDeque saida;
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

}

