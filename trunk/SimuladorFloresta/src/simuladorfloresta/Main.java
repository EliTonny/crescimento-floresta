package simuladorfloresta;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        try {
            Terreno ter = new Terreno(1, 1);
            ter.addArvore(new ArvorePauBrasil());
            ter.addArvore(new ArvorePauBrasil());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
