package ma.projet.classes;

import jakarta.persistence.*;

@Entity
@Table(name = "lignes_commande")
public class LigneCommandeProduit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private int quantite;

    public LigneCommandeProduit() {}
    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande; this.produit = produit; this.quantite = quantite;
    }

    public Long getId() { return id; }
    public Commande getCommande() { return commande; }
    public Produit getProduit() { return produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
