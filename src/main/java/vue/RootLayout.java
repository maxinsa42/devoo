package vue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import modele.persistence.DeserialiseurXML;
import modele.xmldata.Fenetre;
import modele.xmldata.Intersection;
import modele.xmldata.PlanDeVille;

import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import controleur.ControleurInterface;

/**
 * Cette classe joue le rôle de bindind pour la fenetre principale de l'application.
 * C'est ici qu'on spécifiera les écouteurs et consorts.
 * Remarque : Les écouteurs peuvent être spécifiés directement dans le fichier xml aussi
 */
public class RootLayout implements Initializable {

    /**
     * Vue à gauche qui affiche les livraisons
     */
    @FXML
    private TreeTableView<Fenetre> tableViewFenetre;

    @FXML
    private Pane canvasGraphique;

    /**
     * Controleur à appeler en cas de besoin
     */
    ControleurInterface controleurInterface;

    public void setControleurInterface(ControleurInterface controleurInterface) {
        this.controleurInterface = controleurInterface;
    }

    public RootLayout() {
    }

    /**
     * Ecouteur pour ouvrir le plan
     */
    @FXML
    private void ouvrirPlan(ActionEvent actionEvent) {
       controleurInterface.chargerPlan(null);

    }

    /**
     * Ecouteur pour la demande de livraison
     */
    @FXML
    private void ouvrirDemande(ActionEvent actionEvent) {
        controleurInterface.chargerLivraisons(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //resources.

    }

	private final double DIAMETRE_INTERSECTION = 7;
	private final int MARGE_INTERSECTION = 30;
	
	private Collection<Ellipse> intersectionsGraphiques = new ArrayList<Ellipse>();
	private double echelleXIntersection = 0;
	private double echelleYIntersection = 0;
	
	
	@FXML
	void clic_ajouterLivraison() throws JDOMException, IOException, SAXException {
		PlanDeVille planDeVille = DeserialiseurXML.ouvrirPlanDeVille(ClassLoader.getSystemClassLoader().getResourceAsStream("samples/plan10x10.xml"));
		construireGraphe(planDeVille);
	}
	
	@FXML
	void clic_echangerLivraison() {
		System.out.println(canvasGraphique.getHeight());
		System.out.println(canvasGraphique.getWidth());
	}
	
	private void construireGraphe(PlanDeVille plan) {
		
		Map<Integer, Intersection> toutesIntersections = plan.getIntersections();
		intersectionsGraphiques = new ArrayList<Ellipse>();
		
		echelleXIntersection = 0;
		echelleYIntersection = 0;
		for (Intersection i : toutesIntersections.values()) {
			echelleXIntersection = Math.max(echelleXIntersection, i.getX());
			echelleYIntersection = Math.max(echelleYIntersection, i.getY());
			intersectionsGraphiques.add(construireEllipse(i));
		}
		
		afficherToutesEllipses();
	}
	
	private Ellipse construireEllipse(Intersection i) {
		Ellipse intersection = new Ellipse(i.getX(), i.getY(), DIAMETRE_INTERSECTION, DIAMETRE_INTERSECTION);
		intersection.setFill(Color.YELLOW);
		return intersection;
	}

	private void afficherToutesEllipses() {
		for (Ellipse e : intersectionsGraphiques)
			afficherEllipse(e);
		
		echelleXIntersection = 0;
		echelleYIntersection = 0;
		for (Ellipse e : intersectionsGraphiques) {
			echelleXIntersection = Math.max(echelleXIntersection, e.getCenterX());
			echelleYIntersection = Math.max(echelleYIntersection, e.getCenterY());
		}
	}
	
	private void afficherEllipse(Ellipse e)	{
		double newX = e.getCenterX() * canvasGraphique.getWidth() / (echelleXIntersection + MARGE_INTERSECTION);
		double newY = e.getCenterY() * canvasGraphique.getHeight() / (echelleYIntersection + MARGE_INTERSECTION);
		
		e.setCenterX(newX);
		e.setCenterY(newY);
		
		canvasGraphique.getChildren().add(e);
	}
	
	final ChangeListener<Number> ecouteurDeRedimensionnement = new ChangeListener<Number>() {
		
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			
			canvasGraphique.getChildren().clear();
			afficherToutesEllipses();
		}
	};
}
