/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import modele.xmldata.Model;
import modele.xmldata.PlanDeVille;

/**
 *
 * @author maex
 */
public interface EtatInterface
{
    public void cliqueSurPlan(int x, int y);

    public void cliqueSurListItem(int livraisonId);

    public boolean cliqueAnnuler();

    public boolean cliqueRetablir();

    public PlanDeVille chargerPlan(String chemin);

    public Model chargerLivraisons(String chemin, PlanDeVille plan);
}
