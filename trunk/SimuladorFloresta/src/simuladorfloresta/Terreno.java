package simuladorfloresta;

public class Terreno {

    private int XMax;
    private int YMax;

    public Arvore[][] getArvores() {
        return arvores;
    }
    private Arvore[][] arvores;
    private int numArvores;
    private int numMaxArvores;

    public int getNumMaxArvores() {
        return numMaxArvores;
    }
    private final int POSICOES_POR_METRO = 2;

    /**
     * @param largura Largura do terreno em metros
     * @param comprimento comprimento do terreno em metros
     */
    public Terreno(int largura, int comprimento) {
        this.XMax = comprimento;
        this.YMax = largura;
        arvores = new Arvore[comprimento * POSICOES_POR_METRO][largura * POSICOES_POR_METRO];
        numMaxArvores
                = largura * POSICOES_POR_METRO
                * comprimento * POSICOES_POR_METRO;
    }

    /**
     * Adiciona uma arvore em uma posição aleattória do terreno
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
                    pos = new Posicao(x, y);
                    break;
                }
            }
        }
        if (pos == null) {
            for (int x = 0; x < this.XMax * POSICOES_POR_METRO; x++) {
                for (int y = 0; y < this.YMax * POSICOES_POR_METRO; y++) {
                    if (arvores[x][y] == null) {
                        pos = new Posicao(x, y);
                        break;
                    }
                }
            }
        }

        numArvores++;
        return pos;
    }
    
    public String ImprimeDados()
    {
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
}
