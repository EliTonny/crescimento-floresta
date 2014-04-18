package simuladorfloresta;

import javax.swing.JOptionPane;

public class Gerenciador {

    public static final int NUM_CLICOS_DIA = 50;
    Terreno ter = new Terreno(1, 1);
    Ambiente amb = new Ambiente(ter);
    Fotossintese fot = new Fotossintese(ter);

    public void Iniciar(int dias) {
        try {
            ter = new Terreno(10, 10);

            for (int i = 0; i < 100; i++) {
                if (!ter.addArvore(new ArvorePauBrasil())) {
                    throw new Exception("Numero de arvores excede o limite permitido(" 
                            + ter.getNumMaxArvores() 
                            + ") para o terreno");
                }
            }

            for (int i = 0; i < dias; i++) {
                ProximoDia();
                System.out.println("Dia " + (i + 1));
            }
            System.out.println("Processo finalizado.");
            System.out.println(ter.ImprimeDados());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void ProximoDia() throws InterruptedException {
        amb = new Ambiente(ter);
        fot = new Fotossintese(ter);

        amb.start();
        fot.start();

        amb.join();
        fot.setFinalizar(true);
        fot.join();
    }
}
