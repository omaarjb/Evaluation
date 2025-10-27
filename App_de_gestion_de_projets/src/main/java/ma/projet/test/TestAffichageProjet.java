package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.config.HibernateConfig;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TestAffichageProjet {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfig.class);

        EmployeService empSrv = context.getBean(EmployeService.class);
        ProjetService prjSrv = context.getBean(ProjetService.class);
        TacheService tchSrv = context.getBean(TacheService.class);
        EmployeTacheService etSrv = context.getBean(EmployeTacheService.class);

        try {
            Employe chef = empSrv.add(new Employe("test", "test", "0600000000", "test@test.com", "test"));

            Projet p = prjSrv.add(new Projet("projet1", LocalDate.of(2013, 1, 14), chef));

            Tache t1 = tchSrv.add(new Tache("Analyse", LocalDate.of(2013, 2, 5), LocalDate.of(2013, 2, 28), 1500.0, p));
            Tache t2 = tchSrv.add(new Tache("Conception", LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 30), 2000.0, p));
            Tache t3 = tchSrv.add(new Tache("Développement", LocalDate.of(2013, 4, 1), LocalDate.of(2013, 5, 5), 5000.0, p));

            etSrv.add(new EmployeTache(chef, t1, LocalDate.of(2013, 2, 10), LocalDate.of(2013, 2, 20)));
            etSrv.add(new EmployeTache(chef, t2, LocalDate.of(2013, 3, 10), LocalDate.of(2013, 3, 15)));
            etSrv.add(new EmployeTache(chef, t3, LocalDate.of(2013, 4, 10), LocalDate.of(2013, 4, 25)));

            System.out.printf("Projet : %d     Nom : %s     Date début : %s%n",
                    p.getId(), p.getNom(), formatDateFr(p.getDateDebut()));

            System.out.println("Liste des tâches:");
            System.out.println("Num   Nom            Date Début Réelle   Date Fin Réelle");

            List<Object[]> lignes = prjSrv.tachesRealiseesAvecDates(p.getId());
            lignes.sort(Comparator.comparingLong(o -> (Long) o[0]));

            for (Object[] r : lignes) {
                Long idTache = (Long) r[0];
                String nom = (String) r[1];
                String ddr = formatDateFr((LocalDate) r[2]);
                String dfr = formatDateFr((LocalDate) r[3]);
                System.out.printf("%-5d %-14s %-18s %s%n", idTache, nom, ddr, dfr);
            }
        } finally {
            context.close();
            HibernateUtil.shutdown();
        }
    }

    private static String formatDateFr(LocalDate d) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
        String s = d.format(f);
        return s.substring(0, 3) + s.substring(3, 4).toUpperCase() + s.substring(4);
    }
}
