package ma.projet.classes;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employe_tache")
public class EmployeTache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tache_id")
    private Tache tache;

    @Column(nullable = false)
    private LocalDate dateDebutReelle;
    @Column(nullable = false)
    private LocalDate dateFinReelle;

    public EmployeTache() {
    }

    public EmployeTache(Employe e, Tache t, LocalDate ddr, LocalDate dfr) {
        this.employe = e;
        this.tache = t;
        this.dateDebutReelle = ddr;
        this.dateFinReelle = dfr;
    }

    public Long getId() {
        return id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Tache getTache() {
        return tache;
    }

    public LocalDate getDateDebutReelle() {
        return dateDebutReelle;
    }

    public LocalDate getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateDebutReelle(LocalDate d) {
        this.dateDebutReelle = d;
    }

    public void setDateFinReelle(LocalDate d) {
        this.dateFinReelle = d;
    }
}
