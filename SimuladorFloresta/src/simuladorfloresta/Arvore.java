package simuladorfloresta;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Arvore {
    private int agua;
    private int luz;
    private int saisMinerais;
    private int energia;
    private int tamanhoMax;
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

    public Arvore(int tamanhoMax) {
        lock = new ReentrantLock();
        temAgua = lock.newCondition();
        temLuz = lock.newCondition();
        temSaisMinerais = lock.newCondition();
        this.tamanhoMax = tamanhoMax;
    }
    
    public Terreno getTerreno() {
        return terreno;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }
    
    public void retiraAgua(int qtd) throws Exception {
        lock.lock();
        try {
            while (agua < qtd) {
                temAgua.await();
            }
            agua -= qtd;
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

    public void retiraLuz(int qtd) throws Exception{
        lock.lock();
        try {
            while (luz < qtd) {
                temLuz.await();
            }
            luz -= qtd;
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

    public void retiraSaisMinerais(int qtd) throws Exception {
        lock.lock();
        try {
            while (saisMinerais < qtd) {
                temSaisMinerais.await();
            }
            saisMinerais -= qtd;
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
    }

    public synchronized void setEnergia(int qtd) {
        this.energia += qtd;
        notifyAll();
    }
}
