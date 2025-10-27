package ma.projet.beans;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "mariages")
public class Mariage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "homme_id")
    private Homme homme;

    @ManyToOne(optional = false)
    @JoinColumn(name = "femme_id")
    private Femme femme;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin; // null => en cours

    @Column(name = "nbrEnfants", nullable = false)
    private int nbEnfants;

    public Mariage() {
    }

    public Mariage(Homme h, Femme f, LocalDate debut, LocalDate fin, Integer enfants) {
        this.homme = h;
        this.femme = f;
        this.dateDebut = debut;
        this.dateFin = fin;
        this.nbEnfants = enfants;
    }

    public Long getId() {
        return id;
    }

    public Homme getHomme() {
        return homme;
    }

    public Femme getFemme() {
        return femme;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Integer getNbrEnfants() {
        return nbEnfants;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}
