package simuladorfloresta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Terreno {

    private int XMax;
    private int YMax;
    private Arvore[][] arvores;
    private int numArvores;

    public int getNumArvores() {
        return numArvores;
    }
    private int numMaxArvores;
    private final int POSICOES_POR_METRO = 2;
    public static final int ARVORES_POR_METRO2 = 4;
    private static Terreno instancia;
    private Queue<Arvore> ArvoreAmbiente;
    private Queue<Arvore> ArvoreFotossintese;
    private Lock lockAmbiente;
    private Condition condHasArvoreAmbiente;
    private Lock lockFotossintese;
    private Condition condHasArvoreFotossintese;
    private AtomicBoolean finalizar;

    public static Terreno getInstancia() {
        if (instancia == null) {
            instancia = new Terreno();
            //instancia = new Terreno(Gerenciador.getinstancia().getLarguraTerreno(), Gerenciador.getinstancia().getComprimentoTerreno());
        }
        return instancia;
    }

    public Arvore[][] getArvores() {
        return arvores;
    }

    public int getNumMaxArvores() {
        return numMaxArvores;
    }

    /**
     * @param largura Largura do terreno em metros
     * @param comprimento comprimento do terreno em metros
     */
    public void Inicializa(int largura, int comprimento, AtomicBoolean finalizar) {
        this.XMax = comprimento;
        this.YMax = largura;
        arvores = new Arvore[comprimento * POSICOES_POR_METRO][largura * POSICOES_POR_METRO];
        numMaxArvores = largura * POSICOES_POR_METRO
                * comprimento * POSICOES_POR_METRO;
        this.finalizar = finalizar;
        numArvores = 0;
        ArvoreAmbiente = new ArrayDeque<>();
        ArvoreFotossintese = new ArrayDeque<>();
        lockAmbiente = new ReentrantLock();
        condHasArvoreAmbiente = lockAmbiente.newCondition();
        lockFotossintese = new ReentrantLock();
        condHasArvoreFotossintese = lockFotossintese.newCondition();

        /*for (int x = 0; x < this.arvores.length; x++) {
         for (int y = 0; y < this.arvores[x].length; y++) {
         if (arvores[x][y] != null) {
         ArvoreAmbiente.add(arvores[x][y]);
         }
         }
         }*/
    }

    //Depois de processar a arvore, faz um setArvoreFotossintese
    public Arvore retiraArvoreAmbiente() throws InterruptedException {
        //if Nao temArvoreAmbiente então wait
        lockAmbiente.lock();
        try {
            while (ArvoreAmbiente.isEmpty()) {
                if (!condHasArvoreAmbiente.await(1, TimeUnit.SECONDS)) {
                    //System.out.println("Saiu do retiraArvoreAmbiente");
                    return null;
                }
            }
            return ArvoreAmbiente.poll();
        } finally {
            lockAmbiente.unlock();
        }
    }

    public void setArvoreAmbiente(Arvore arvore) {
        lockAmbiente.lock();
        try {
            ArvoreAmbiente.add(arvore);
            condHasArvoreAmbiente.signalAll();
        } finally {
            lockAmbiente.unlock();
        }
    }

    //Depois de processar a arvore, faz um setArvoreAmbiente e um setArvoreCicloVida
    public Arvore retiraArvoreFotossintese() throws InterruptedException {
        lockFotossintese.lock();
        try {
            while (ArvoreFotossintese.isEmpty()) {
                if (finalizar.get()) {
                    return null;
                }
                if (!condHasArvoreFotossintese.await(200, TimeUnit.MILLISECONDS)) {
                    return null;
                }
                //condHasArvoreFotossintese.await();
            }
            return ArvoreFotossintese.poll();
        } finally {
            lockFotossintese.unlock();
        }
    }

    public void setArvoreFotossintese(Arvore arvore) {
        lockFotossintese.lock();
        try {
            ArvoreFotossintese.add(arvore);
            condHasArvoreFotossintese.signalAll();
        } finally {
            lockFotossintese.unlock();
        }
    }

    /**
     * Adiciona uma arvore em uma posição aleatória do terreno
     *
     * @return Retorna false se não houver mais espaço no terreno
     *
     */
    public boolean addArvore(Arvore arvore) throws Exception {
        Posicao pos;
        if (this.numArvores == this.numMaxArvores) {
            return false;
        }
        arvore.setTerreno(this);
        pos = this.procuraPosicaoAleatoria();
        arvore.setPosicao(pos);
        arvores[pos.getX()][pos.getY()] = arvore;

        ArvoreAmbiente.add(arvore);
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
                    numArvores++;
                    pos = new Posicao(x, y);
                    return pos;

                }
            }
        }
        if (pos == null) {
            for (int x = 0; x < this.XMax * POSICOES_POR_METRO; x++) {
                for (int y = 0; y < this.YMax * POSICOES_POR_METRO; y++) {
                    if (arvores[x][y] == null) {
                        numArvores++;
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

    public ArrayList<Arvore> getArvoresEtapa(EnumEtapaProcesso etapa) {
        ArrayList<Arvore> arvoresRetorno = new ArrayList();
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[0].length; j++) {
                if (arvores[i][j].getEtapa() == etapa) {
                    arvoresRetorno.add(arvores[i][j]);
                }
            }
        }
        return arvoresRetorno;
    }

    public ArrayList<Arvore> getArvoresEtapa() {
        ArrayList<Arvore> arvoresRetorno = new ArrayList();
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[0].length; j++) {
                if (arvores[i][j] != null) {
                    arvoresRetorno.add(arvores[i][j]);
                }
            }
        }
        return arvoresRetorno;
    }
}
