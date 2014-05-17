package simuladorfloresta2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Armazem<T> {

    private int tam;
    private int ini;
    private Object[] arm;
    private Semaphore acesso;
    private Semaphore haElementos;

    public Armazem(ArrayList<T> lista) {
        this.tam = lista.size();
        this.arm = lista.toArray();
        this.acesso = new Semaphore(1);
        this.haElementos = new Semaphore(tam);
    }

    public T retira() throws InterruptedException {
        
        acesso.acquire();
        Object x = arm[ini];
        ini = (ini + 1) % tam;
        acesso.release();
        return (T) x;
    }

    public Semaphore getHaElementos() {
        return haElementos;
    }
}
