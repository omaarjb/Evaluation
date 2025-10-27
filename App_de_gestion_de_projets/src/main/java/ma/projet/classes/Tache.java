package ma.projet.classes;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "taches")
@NamedQuery(name = "Tache.prixSup1000", query = "FROM Tache t WHERE t.prix > 1000")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateDebutPlanifiee;
    @Column(nullable = false)
    private LocalDate dateFinPlanifiee;

    @Column(nullable = false)
    private Double prix;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projet_id")
    private Projet projet;

    public Tache() {
    }

    public Tache(String nom, LocalDate ddp, LocalDate dfp, Double prix, Projet projet) {
        this.nom = nom;
        this.dateDebutPlanifiee = ddp;
        this.dateFinPlanifiee = dfp;
        this.prix = prix;
        this.projet = projet;
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

    public LocalDate getDateDebutPlanifiee() {
        return dateDebutPlanifiee;
    }

    public void setDateDebutPlanifiee(LocalDate d) {
        this.dateDebutPlanifiee = d;
    }

    public LocalDate getDateFinPlanifiee() {
        return dateFinPlanifiee;
    }

    public void setDateFinPlanifiee(LocalDate d) {
        this.dateFinPlanifiee = d;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet p) {
        this.projet = p;
    }
}
