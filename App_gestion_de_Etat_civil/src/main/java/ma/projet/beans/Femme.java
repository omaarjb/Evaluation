package ma.projet.beans;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femmes")
public class Femme extends Personne {
    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {
    }

    public Femme(String nom, String prenom, String adresse, java.time.LocalDate naissance) {
        super(nom, prenom, adresse, naissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }
}
