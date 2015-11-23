package vue;

import controleur.ControleurInterface;
import controleur.MainActivationObserverInterface;
import controleur.ModelObserver;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import modele.xmldata.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controleur de la TreeTableView qui affiche les livraisons et les horaires.
 * Elle passe par le controleur principale de la vue quand elle reçoie des
 * actions.
 */
public class VueTableLivraisonControleur implements Initializable, Visiteur, MainActivationObserverInterface,
        ModelObserver {

    @FXML
    TreeTableView<Visitable> tableViewFenetre;

    /**
     * Element racine de la Table qui contient tous les autres éléments.Il ne sera pas affiché dans la table
     */
    private TreeItem<Visitable> elementRacine = new TreeItem<>();

    /**
     * Controleur principale
     */
    private ControleurInterface controleurInterface;

    /**
     * Colonne livraison de la vue à gauche
     */
    @FXML
    private TreeTableColumn<Visitable, String> colonneLivraison;

    /**
     * Colonne horaire de passage de la vue à gauche
     */
    @FXML
    private TreeTableColumn<Visitable, String> colonneHoraire;

    /**
     * Mediateur pour la communication avec les autres controleurs de vue
     */
    private FenetrePrincipale mediateur;

    public void initialiserMediateur(FenetrePrincipale fenetrePrincipale) {
        this.mediateur = fenetrePrincipale;
    }

    /**
     * Initialise les caractéristiques de la table
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewFenetre.setRoot(elementRacine);
        elementRacine.setExpanded(true);
        tableViewFenetre.setShowRoot(false);
        initialiserEcouteurDeClic();
        initialiserColonneLivraison();
    }

    /**
     * Contruis la table des livraisons
     *
     * @param demande La demande de livraison chargée à partir d'un fichier XML
     */
    protected void construireVueTableLivraion(Demande demande) {
        for (Fenetre f : demande.getFenetres()) {
            elementRacine.getChildren().add(construireFenetreItem(f));
        }
    }

    private void ajouterStyleCssColonneLivraison() {
        // TODO : à completer pour le hover ou des trucs du genre
        colonneLivraison.setCellFactory(
                new Callback<TreeTableColumn<Visitable, String>, TreeTableCell<Visitable, String>>() {
                    @Override
                    public TreeTableCell<Visitable, String> call(TreeTableColumn<Visitable, String> param) {
                        return new TreeTableCell<Visitable, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                setText(item);
                                String style = null;
                                style = "-fx-font-weight: bold; -fx-text-fill: skyblue; -fx-underline: true;";
                                setStyle(style);
                            }
                        };
                    }
                });
    }

    /**
     * Initialise l'écouteur de clique sur un élement de la table
     */
    private void initialiserEcouteurDeClic() {
        tableViewFenetre.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newValue.getValue().accepterVisiteurObjet(this);
                });
    }

    /**
     * Initialise la methode de remplissage de la colonne 'Livraisons'
     */
    private void initialiserColonneLivraison() {
        colonneLivraison.setCellValueFactory((TreeTableColumn.CellDataFeatures<Visitable, String> param)
                -> new ReadOnlyStringWrapper(param.getValue().getValue().accepterVisiteurInformation(this)));
    }

    /**
     * Efface le contenu de la table de livraison table de livraison
     */
    private void effacerVueTableLivraison() {
        elementRacine.getChildren().clear();
    }

    /**
     * Contruis un élement correspondant à une fenetre et ses enfants
     */
    private static TreeItem<Visitable> construireFenetreItem(Fenetre fenetre) {

        // Récuperation des livraisons de la fenetre
        List<Livraison> livraisonList = new ArrayList<>();
        fenetre.getLivraisons().forEach((integer, livraison1) -> {
            livraisonList.add(livraison1);
        });

        // Construction des items de chaque livraison
        TreeItem<Visitable> elementRacine = new TreeItem<>(fenetre);
        for (Livraison l : livraisonList) {

            TreeItem<Visitable> livraisonTreeItem = new TreeItem<>(l);
            elementRacine.getChildren().add(livraisonTreeItem);
        }

        elementRacine.setExpanded(true);
        return elementRacine;
    }

    @Override
    public String recupererInformation(Fenetre fenetre) {
        int debut = fenetre.getTimestampDebut();
        int fin = fenetre.getTimestampFin();

        return convertirEnHeureLisible(debut) + " - " + convertirEnHeureLisible(fin);
    }

    @Override
    public String recupererInformation(Livraison livraison) {
        return livraison.getId() + " - Client " + livraison.getClientId() + " à " + livraison.getAdresse();
    }

    @Override
    public void recupererObject(Fenetre fenetre) {
        //TODO : appeler la vue principale pour qu'elle fasse un truc sur la map
        System.out.println(fenetre);
    }

    @Override
    public void recupererObject(Livraison livraison) {
        System.out.println(livraison);
        controleurInterface.cliqueSurLivraison(livraison.getId());
    }

    /**
     * Convertis un temps en seconde en HH:mm:ss
     */
    private static String convertirEnHeureLisible(int tempsEnSeconde) {
        int heure = tempsEnSeconde / 3600;
        int mn = (tempsEnSeconde % 3600) / 60;
        int sec = tempsEnSeconde % 60;
        return String.format("%02d:%02d:%02d", heure, mn, sec);
    }

    public void setControleurInterface(ControleurInterface controleurInterface) {
        this.controleurInterface = controleurInterface;
    }

    @Override
    public void notifyObserver(boolean disabled) {
        if (disabled) {
            effacerVueTableLivraison();
        }

    }

    /**
     * Notification déclenchée lors d'un changement dans le model
     */
    @Override
    public void notifyObserver() {
        effacerVueTableLivraison();
        construireVueTableLivraion(controleurInterface.getModel().getDemande());
    }

    public void initialiserObserveurs(){
        controleurInterface.ajouterDesactObserver(this);
        controleurInterface.ajouterModelObserver(this);
    }

}