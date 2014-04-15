package simuladorfloresta;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Arvore {
    
    private final int TEMPO_ESPERA = 1;
    private int agua;
    private int luz;
    private int saisMinerais;
    private int energia;
    private int tamanhoMax;
    private int raioMax;
    private Terreno terreno;
    private Posicao posicao;

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    private Lock lock;
    private Condition temAgua;
    private Condition temLuz;
    private Condition temSaisMinerais;

    public Arvore(int tamanhoMax, int raioMax) {
        lock = new ReentrantLock();
        temAgua = lock.newCondition();
        temLuz = lock.newCondition();
        temSaisMinerais = lock.newCondition();
        this.tamanhoMax = tamanhoMax;
        this.raioMax = raioMax;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }

    public boolean retiraAgua(int qtd) throws Exception {
        lock.lock();
        try {
            while (agua < qtd) {
                if (!temAgua.await(TEMPO_ESPERA, TimeUnit.SECONDS)) {
                    return false;
                }
            }
            agua -= qtd;
            msg("Retirou " + qtd + " água. Total:" + agua);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void setAgua(int qtd) {
        lock.lock();
        try {
            //falta fazer cálculo para verificar quanto 
            //a planta consegue absorver considerando
            //que existem outras plantas ao redor

            this.agua += qtd;
            temAgua.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean retiraLuz(int qtd) throws Exception {
        lock.lock();
        try {
            while (luz < qtd) {
                if (!temLuz.await(TEMPO_ESPERA, TimeUnit.SECONDS)) {
                    return false;
                }
            }
            luz -= qtd;
            msg("Retirou " + qtd + " luz. Total:" + luz);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void setLuz(int qtd) {
        lock.lock();
        try {

            //falta fazer cálculo para verificar quanto 
            //a planta consegue absorver considerando
            //que existem outras plantas ao redor
            this.luz += qtd;
            temLuz.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean retiraSaisMinerais(int qtd) throws Exception {
        lock.lock();
        try {
            while (saisMinerais < qtd) {
                if (!temSaisMinerais.await(TEMPO_ESPERA, TimeUnit.SECONDS)) {
                    return false;
                }
            }
            saisMinerais -= qtd;
            msg("Retirou " + qtd + " Sais. Total:" + saisMinerais);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void setSaisMinerais(int qtd) {
        lock.lock();
        try {

            //falta fazer cálculo para verificar quanto 
            //a planta consegue absorver considerando
            //que existem outras plantas ao redor
            this.saisMinerais += qtd;
            temSaisMinerais.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void retiraEnergia(int qtd) throws Exception {
        while (energia < qtd) {
            wait();
        }
        energia -= qtd;
        msg("Retirou " + qtd + " energia. Total:" + energia);
    }

    public synchronized void setEnergia(int qtd) {
        this.energia += qtd;
        notifyAll();
    }

    private void msg(String msg) {
        System.out.println(msg);
    }

    public String ImprimeDado() {
        String saida = "";
        saida += "Agua:" + this.agua + "\n";
        saida += "Luz:" + this.luz + "\n";
        saida += "Sais:" + this.saisMinerais + "\n";
        return saida;
    }
}
