package ma.projet.classes;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projets")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chef_id")
    private Employe chefDeProjet;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tache> taches = new ArrayList<>();

    public Projet() {
    }

    public Projet(String nom, LocalDate dateDebut, Employe chef) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.chefDeProjet = chef;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate d) {
        this.dateDebut = d;
    }

    public Employe getChefDeProjet() {
        return chefDeProjet;
    }

    public void setChefDeProjet(Employe e) {
        this.chefDeProjet = e;
    }

    public List<Tache> getTaches() {
        return taches;
    }
}
