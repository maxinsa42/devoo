package controleur.etat;


import java.io.File;
import java.util.List;

import modele.xmldata.Livraison;
import controleur.ControleurDonnees;
import controleur.commande.CommandeException;

/**
 *
 * @author Maxou
 */
public class EtatEchange extends AbstractEtat
{

    private final ControleurDonnees donnees;

    public EtatEchange(ControleurDonnees donnees)
    {
        this.donnees = donnees;
    }

    @Override
    public EtatInterface cliqueSurLivraison(int livraisonId)
    {
    	return new EtatEchange2(donnees, livraisonId);
    }

    @Override
    public EtatInterface chargerPlan(File plan) throws CommandeException
    {
    	throw new RuntimeException("Cet état ne permet pas de charger un plan");
    }

    @Override
    public EtatInterface chargerLivraisons(File livraisons) throws CommandeException
    {
    	throw new RuntimeException("Cet état ne permet pas de charger une demande de livraison");
    }

    @Override
    public EtatInterface cliqueSurPlan(int intersectionId)
    {
    	// Trouver si l'intersection est une livraison
    	List<List<Livraison>> livraisons = donnees.getModele().getLivraisonsTournee();
    	for (List<Livraison> fenetre : livraisons) {
    		for (Livraison l : fenetre) {
    			if (l.getAdresse() == intersectionId) {
    				return new EtatEchange2(donnees, l.getId());
    			}
    		}
    	}
    	
    	throw new RuntimeException("L'intersection n'est pas une livraison");
    }

    @Override
    public EtatInterface cliqueCalculerTournee()
    {
    	throw new RuntimeException("Cet état ne permet pas de calculer la tournée");
    }

}