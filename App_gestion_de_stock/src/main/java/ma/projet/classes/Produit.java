package ma.projet.classes;

import jakarta.persistence.*;

@Entity
@Table(name = "produits")
@NamedQuery(name = "Produit.prixSup100",
        query = "SELECT p FROM Produit p WHERE p.prix > 100")
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private float prix;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Produit() {}
    public Produit(String reference, float prix, Categorie categorie) {
        this.reference = reference; this.prix = prix; this.categorie = categorie;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
}
