package simuladorfloresta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import jomp.runtime.OMP;

public class Terreno {

    private int XMax;
    private int YMax;
    private Arvore[][] arvores;
    private int numArvores;
    private int numMaxArvores;
    private final int POSICOES_POR_METRO = 2;
    public static final int ARVORES_POR_METRO2 = 4;
    private static Terreno instancia;
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

    public static Terreno getInstancia() {
        if (instancia == null) {
            instancia = new Terreno();
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
        //Verifica-se se a arvore do terreno é a mesma passada por parametros.
        //Essa verificação é necessária pois em alguns momentos, a arvore do terreno
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
        if (arvore.isMorta()) {
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
     * Adiciona uma arvore em uma posição aleatória do terreno
     *
     * @return Retorna false se não houver mais espaço no terreno
     *
     */
    public synchronized boolean addArvore(Arvore arvore) throws Exception {
        Posicao pos;
        if (this.numArvores == this.numMaxArvores) {
            return false;
        }
        //arvore.setTerreno(this);
        pos = this.procuraPosicaoAleatoria();
        arvore.setPosicao(pos);
        arvores[pos.getX()][pos.getY()] = arvore;
        numArvores++;
        return true;
    }

    public void addArvoreFilha(Arvore arvoreFilha, Arvore arvoreMae) {
        //fazer calculo baseado na posicao da mãe, 
        //para definir a nova posicao d arvore filha
    }

    /**
     * Método que retorna uma posição aleatória que esteja disponível no
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

        //omp parallel reduction(+:numeroArvores)
        {
            int myId = OMP.getThreadNum();
            for (int i = 0; i < arvores[myId].length; i++) {
                if (arvores[myId][i] != null) {
                    //omp critical
                    {
                        saida.add(arvores[myId][i]);
                    }
                    numeroArvores++;
                }
            }
        }

        this.arvoresAmbiente = saida;
        return numeroArvores;
    }
}
