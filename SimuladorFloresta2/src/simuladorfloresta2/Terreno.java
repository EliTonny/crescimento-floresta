package simuladorfloresta2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Terreno {

    private int XMax;
    private int YMax;
    private Arvore[][] arvores;
    private int numArvores;
    private int numMaxArvores;
    private final int POSICOES_POR_METRO = 2;
    public static final int ARVORES_POR_METRO2 = 4;
    private static Terreno instancia;
    private Queue<Arvore> arvoresAmbiente;
    private Queue<Arvore> arvoresFotossintese;
    private boolean finalizarProcesso;

    public void setFinalizarProcesso(boolean finalizarProcesso) {
        this.finalizarProcesso = finalizarProcesso;
    }
    private Queue<Arvore> arvoresCorte;

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
        arvoresAmbiente = new ArrayDeque<>();
        arvoresFotossintese = new ArrayDeque<>();
        arvoresCorte = new ArrayDeque<>();
    }

    public synchronized boolean killArvore(Arvore arvore) {
        //Verifica-se se a arvore do terreno é a mesma passada por parametros.
        //Essa verificação é necessária pois em alguns momentos, a arvore do terreno
        //pode morrer, e outro processo pode tentar matar a mesma arvore
        if (arvore.equals(arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()])) {
            arvores[arvore.getPosicao().getX()][arvore.getPosicao().getY()] = null;
            this.numArvores--;
            System.out.println("Arvore Morta: " + arvore.getPosicao().toString());
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
        return arvoresCorte.poll();
    }

    public Arvore retiraArvoreAmbiente() {
        while (arvoresAmbiente.isEmpty()) {
            return null;
        }
        return arvoresAmbiente.poll();
    }

    public void setArvoreAmbiente(Arvore arvore) {
        arvoresAmbiente.add(arvore);
    }

    public Arvore retiraArvoreFotossintese() {
        if (arvoresFotossintese.isEmpty()) {
            return null;
        }
        return arvoresFotossintese.poll();
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
        arvore.setTerreno(this);
        pos = this.procuraPosicaoAleatoria();
        arvore.setPosicao(pos);
        arvores[pos.getX()][pos.getY()] = arvore;
        numArvores++;

        arvoresAmbiente.add(arvore);
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

    public ArrayList<Arvore> getArvoresEtapa(EnumEtapaProcesso etapa) {
        ArrayList<Arvore> arvoresRetorno = new ArrayList();
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[0].length; j++) {
                if (arvores[i][j] != null && arvores[i][j].getEtapa() == etapa) {
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
