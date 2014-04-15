package simuladorfloresta;

import javax.swing.JOptionPane;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 50;

    public void Iniciar() {
        try {
            Terreno ter = new Terreno(1, 1);
            ArvorePauBrasil ar = new ArvorePauBrasil();
            ArvorePauBrasil ar2 = new ArvorePauBrasil();
            ter.addArvore(ar);
            ter.addArvore(ar2);
            
            Ambiente amb = new Ambiente(ter);
            Fotossintese fot = new Fotossintese(ter);
            
            amb.start();
            fot.start();
            
            amb.join();
            fot.setFinalizar(true);
            System.out.println("Ambiente finaizado");
            fot.join();
            System.out.println(ar.ImprimeDado());
            System.out.println(ar2.ImprimeDado());
            System.out.println("Fotossintese finaizado");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
