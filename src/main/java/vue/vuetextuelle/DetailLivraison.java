package vue.vuetextuelle;

import modele.xmldata.Livraison;

/**
 * Gére l'affichage sous forme textuelle des details d'une livraison dans la TreeTableView.
 */
public class DetailLivraison extends ObjetVisualisable {

    private final Livraison livraison;

    public DetailLivraison(Livraison livraison){
        this.livraison = livraison;
    }

    @Override
    public String afficherCaracteriquesGloable() {
        return String.format("%d - Client %d à %d", livraison.getId(), livraison.getClientId(), livraison.getAdresse());
    }

    @Override
    public String afficherCaracteriqueSpeciale() {
        return String.valueOf(livraison.getHoraireDePassage());
    }

    @Override
    public void accepter(Visiteur v) {
        v.visit(this);
    }

    public Livraison getLivraison() {
        return livraison;
    }
}