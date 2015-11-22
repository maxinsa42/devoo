package vue;

import controleur.Controleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.xmldata.Demande;

/**
 * Cette classe crée la fenetre principale avec ses enfants. Elle se charge aussi de créer le controleur.
 * Elle joue le role de mediateur pour la communication entre les differents controleur de la vue.
  * https://sourcemaking.com/design_patterns/mediator
 */
public class FenetrePrincipale extends Application {


    private VuePrincipale vuePrincipaleControleur;

    private VueLivraisonHoraireControleur vueLivraisonHoraireControleur;

    /**
     * Le controleur de l'application
     */
    private Controleur controleurApplication;


    private final int LARGEUR_FENETRE = 1000;

    private final int HAUTEUR_FENETRE = 668;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Chargement de la fenetre principale
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/RootLayout.fxml"));
        BorderPane root = fxmlLoader.load();
        vuePrincipaleControleur = (VuePrincipale) fxmlLoader.getController();

        // Création du controleur de l'application
        controleurApplication = new Controleur();

        // Passage du controleur de l'application au controleur de la vue
        vuePrincipaleControleur.setControleurInterface(controleurApplication);
        vuePrincipaleControleur.initialiserObserveurs();
        vuePrincipaleControleur.initialiserMediateur(this);


        // Chargement de la vue des livraison
        FXMLLoader fxmlLoader2 = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/VueLivraisonHoraire.fxml"));
        AnchorPane anchorPane = fxmlLoader2.load();
        vueLivraisonHoraireControleur = (VueLivraisonHoraireControleur) fxmlLoader2.getController();
        vueLivraisonHoraireControleur.setControleurInterface(controleurApplication);
        vueLivraisonHoraireControleur.initialiserMediateur(this);

        BorderPane centerBorderPane = (BorderPane) root.getCenter();
        centerBorderPane.setLeft(anchorPane.getChildren().get(0));

        primaryStage.setTitle("OptimodLyon");
        primaryStage.setScene(new Scene(root, LARGEUR_FENETRE, HAUTEUR_FENETRE));
        primaryStage.setMinWidth(LARGEUR_FENETRE);
        primaryStage.setMinHeight(HAUTEUR_FENETRE);

        primaryStage.widthProperty().addListener(vuePrincipaleControleur.ecouteurDeRedimensionnement);
        primaryStage.heightProperty().addListener(vuePrincipaleControleur.ecouteurDeRedimensionnement);

        primaryStage.show();
    }

    /**
     * Demande à la vue des livraions de contruire la vue
     */
    public void construireVueLivraison(Demande demande){
        vueLivraisonHoraireControleur.construireVueLivraion(demande);
    }


}
