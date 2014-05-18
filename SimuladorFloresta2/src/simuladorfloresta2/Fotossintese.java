package simuladorfloresta2;

public class Fotossintese {

    private Fotossintese() {
    }

    public static boolean Processa(Arvore arv) throws Exception {
        boolean sucesso = true;
        if (arv.retiraAgua(arv.getAguaFotossintese())) {
            if (arv.retiraLuz(arv.getLuzFotossintese())) {
                if (arv.retiraSaisMinerais(arv.getSaisFotossintese())) {
                    arv.setEnergia(10);
                } else {
                    arv.setAgua(arv.getAguaFotossintese());
                    arv.setLuz(arv.getLuzFotossintese());
                    sucesso = false;
                }
            } else {
                arv.setAgua(arv.getAguaFotossintese());
                sucesso = false;
            }
        } else {
            sucesso = false;
        }
        return sucesso;
    }
}
