package modele.persistence;

import modele.xmldata.Demande;
import modele.xmldata.PlanDeVille;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Cette classe joue le role d'interface pour le chargement des fichiers XML
 * @author Mohamed El Mouctar HAIDARA
 */
public class ChargerXML {

    private ChargerXML(){}

    public static PlanDeVille chargePlanDeVille() throws JDOMException, SAXException, IOException {
        File file = OuvreurDeFichierXML.getInstance().ouvrirSelectionneurDeFichier();
        if (file == null) {
            return null;
        }

        return DeserialiseurXML.ouvrirPlanDeVille(file);
    }

    public static Demande chargeDemande(PlanDeVille planDeVille)
            throws JDOMException, SAXException, ParseException, IOException {
        File file = OuvreurDeFichierXML.getInstance().ouvrirSelectionneurDeFichier();
        if (file == null) {
            return null;
        }

        return DeserialiseurXML.ouvrirLivraison(file, planDeVille);
    }
}
