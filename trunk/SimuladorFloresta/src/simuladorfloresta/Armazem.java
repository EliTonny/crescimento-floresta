package simuladorfloresta;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Armazem {

    private int tam;
    private int ini;
    private Object[] arm;
    private Semaphore acesso;
    private Semaphore haElementos;

    public Armazem(ArrayList<Arvore> lista) {
        this.tam = lista.size();
        this.arm = lista.toArray();
        this.acesso = new Semaphore(1);
        this.haElementos = new Semaphore(tam);
    }

    public Arvore retira() throws InterruptedException {
        
        acesso.acquire();
        Object x = arm[ini];
        ini = (ini + 1) % tam;
        acesso.release();
        return (Arvore) x;
    }

    public Semaphore getHaElementos() {
        return haElementos;
    }
}
