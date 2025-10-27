package ma.projet.beans;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hommes")
public class Homme extends Personne {
    @OneToMany(mappedBy = "homme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages = new ArrayList<>();

    public Homme() {
    }

    public Homme(String nom, String prenom, String adresse, java.time.LocalDate naissance) {
        super(nom, prenom, adresse, naissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }
}
