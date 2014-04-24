package simuladorfloresta;

import java.util.ArrayList;
import simuladorfloresta.Arvore.EtapaProcesso;

public class Terreno {

    private int XMax;
    private int YMax;
    private Arvore[][] arvores;
    private int numArvores;
    private int numMaxArvores;
    private final int POSICOES_POR_METRO = 2;
    public static final int ARVORES_POR_METRO2 = 4;
    private static Terreno instancia;

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
    public void Inicializa(int largura, int comprimento) {
        this.XMax = comprimento;
        this.YMax = largura;
        arvores = new Arvore[comprimento * POSICOES_POR_METRO][largura * POSICOES_POR_METRO];
        numMaxArvores = largura * POSICOES_POR_METRO
                * comprimento * POSICOES_POR_METRO;

        numArvores = 0;
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
                    saida += arvores[x][y].ImprimeDado();
                    saida += "\n";
                }
            }
        }
        return saida;
    }

    public Arvore[] getArvoresEtapa(EtapaProcesso etapa) {
        ArrayList<Arvore> arvoresRetorno = new ArrayList();
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[0].length; j++) {
                if (arvores[i][j].getEtapa() == etapa) {
                    arvoresRetorno.add(arvores[i][j]);
                }
            }
        }
        Object[] objs = arvoresRetorno.toArray();
        Arvore[] arv1 = new Arvore[objs.length];
        for (int i = 0; i < objs.length; i++) {
            arv1[i] = (Arvore) objs[i];
        }
        return arv1;
    }

    public Arvore[] getArvoresEtapa() {
        ArrayList<Arvore> arvoresRetorno = new ArrayList();
        for (int i = 0; i < arvores.length; i++) {
            for (int j = 0; j < arvores[0].length; j++) {
                if (arvores[i][j] != null) {
                    arvoresRetorno.add(arvores[i][j]);
                }
            }
        }
        Object[] objs = arvoresRetorno.toArray();
        Arvore[] arv1 = new Arvore[objs.length];
        for (int i = 0; i < objs.length; i++) {
            arv1[i] = (Arvore) objs[i];
        }
        return arv1;
    }
}
