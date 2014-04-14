package simuladorfloresta;

import javax.swing.JOptionPane;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 100;

    public void Iniciar() {
        try {
            Terreno ter = new Terreno(1, 1);
            ter.addArvore(new ArvorePauBrasil());
            ter.addArvore(new ArvorePauBrasil());
            
            Ambiente amb = new Ambiente(ter);
            Fotossintese fot = new Fotossintese(ter);
            
            amb.start();
            fot.start();
            
            amb.join();
            fot.join();
            
            System.out.println("Processo finaizado");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
