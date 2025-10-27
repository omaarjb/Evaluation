package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.config.HibernateConfig;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestAffichageCommande {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);

        CategorieService catSrv = context.getBean(CategorieService.class);
        ProduitService prodSrv = context.getBean(ProduitService.class);
        CommandeService cmdSrv = context.getBean(CommandeService.class);

        try {
            Categorie cat = catSrv.add(new Categorie("HW", "Hardware"));

            Produit pES12 = prodSrv.add(new Produit("ES12", 120, cat));
            Produit pZR85 = prodSrv.add(new Produit("ZR85", 100, cat));
            Produit pEE85 = prodSrv.add(new Produit("EE85", 200, cat));

            Commande cmd = cmdSrv.add(new Commande(LocalDate.of(2013, 3, 14)));
            cmd.addLigne(pES12, 7);
            cmd.addLigne(pZR85, 14);
            cmd.addLigne(pEE85, 5);
            cmdSrv.update(cmd);

            String dateFr = formatDateFr(cmd.getDate());

            System.out.printf("Commande : %d     Date : %s%n", cmd.getId(), dateFr);
            System.out.println("Liste des produits :");
            System.out.println("Référence   Prix    Quantité");

            List<Object[]> rows = prodSrv.produitsParCommande(cmd.getId());
            List<String> ordre = Arrays.asList("ES12", "ZR85", "EE85");
            rows.sort(Comparator.comparingInt(r -> ordre.indexOf((String) r[0])));

            for (Object[] r : rows) {
                String ref = (String) r[0];
                float prix = (Float) r[1];
                int qte = (Integer) r[2];
                System.out.printf("%-11s %-7s %d%n", ref, ((int) prix) + " DH", qte);
            }
        } finally {
            context.close();
            HibernateUtil.shutdown();
        }
    }

    private static String formatDateFr(LocalDate d) {
        var formatter = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
        String formatted = d.format(formatter);
        return formatted.substring(0, 3) + formatted.substring(3, 4).toUpperCase() + formatted.substring(4);
    }
}
