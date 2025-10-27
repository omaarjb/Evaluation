package ma.projet.test;

import ma.projet.beans.*;
import ma.projet.config.HibernateConfig;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class TestEtatCivil {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(HibernateConfig.class);

        HommeService hommeSrv = ctx.getBean(HommeService.class);
        FemmeService femmeSrv = ctx.getBean(FemmeService.class);
        MariageService marSrv = ctx.getBean(MariageService.class);

        try {
            Homme h1 = hommeSrv.add(new Homme("SAFI", "SAID", "Adr A", LocalDate.of(1965, 1, 1)));

            Femme f1 = femmeSrv.add(new Femme("SALIMA", "RAMI", "Adr 1", LocalDate.of(1970, 1, 1)));
            Femme f2 = femmeSrv.add(new Femme("AMAL", "ALI", "Adr 2", LocalDate.of(1972, 2, 2)));
            Femme f3 = femmeSrv.add(new Femme("WAFA", "ALAOUI", "Adr 3", LocalDate.of(1975, 3, 3)));
            Femme f4 = femmeSrv.add(new Femme("KARIMA", "ALAMI", "Adr 4", LocalDate.of(1971, 4, 4)));

            marSrv.add(new Mariage(h1, f1, LocalDate.of(1990, 9, 3), null, 4));
            marSrv.add(new Mariage(h1, f2, LocalDate.of(1995, 9, 3), null, 2));
            marSrv.add(new Mariage(h1, f3, LocalDate.of(2000, 11, 4), null, 3));


            marSrv.add(new Mariage(h1, f4, LocalDate.of(1989, 9, 3), LocalDate.of(1990, 9, 3), 0));

            System.out.println("Nom : SAFI SAID");

            List<Object[]> dets = marSrv.mariagesHommeDetails(h1.getId());

            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Object[] r : dets) {
                String nomF = (String) r[0];
                String prenomF = (String) r[1];
                LocalDate dateDebut = (LocalDate) r[2];
                LocalDate dateFin = (LocalDate) r[3];
                int nbEnfants = (Integer) r[4];
                if (dateFin == null) {
                    System.out.printf("%d. Femme : %-13s Date Début : %-12s Nbr Enfants : %d%n",
                            i++, nomF + " " + prenomF, fmt(dateDebut), nbEnfants);
                }
            }

            System.out.println("\nMariages échoués :");
            i = 1;
            for (Object[] r : dets) {
                String nomF = (String) r[0];
                String prenomF = (String) r[1];
                LocalDate dateDebut = (LocalDate) r[2];
                LocalDate dateFin = (LocalDate) r[3];
                int nbEnfants = (Integer) r[4];
                if (dateFin != null) {
                    System.out.printf("%d. Femme : %-13s Date Début : %-12s %n   Date Fin : %-12s Nbr Enfants : %d%n",
                            i++, nomF + " " + prenomF, fmt(dateDebut), fmt(dateFin), nbEnfants);
                }
            }

        } finally {
            ctx.close();
            HibernateUtil.shutdown();
        }
    }

    private static String fmt(LocalDate d) {
        return String.format("%02d/%02d/%d", d.getDayOfMonth(), d.getMonthValue(), d.getYear());
    }
}
