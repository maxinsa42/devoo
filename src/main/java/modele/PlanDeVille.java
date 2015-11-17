package modele;

import java.util.HashMap;
import java.util.Map;

/**
 * Réprensente le plan de la ville chargé à partir d'un fichier XML
 * @author Mohamed El Mouctar HAIDARA
 */
public class PlanDeVille {

    /**
     * Répresente l'ensemble des intersections du plan sous la forme d'une MAP
     */
    private Map<Integer,Intersection> intersections;

    public PlanDeVille(){
        intersections = new HashMap<>();
    }

    public PlanDeVille(Map<Integer, Intersection> intersections) {
        this.intersections = intersections;
    }


    /**
     * Ajout une intersection à la map
     * @param intersection L'intersection à ajouter
     */
    public void addInstersection(Intersection intersection){
        intersections.put(intersection.getId(),intersection);
    }

    /**
     * Récupère l'intersection correspondant à l'id passé en paramètre
     */
    public Intersection getIntersection(int id){
        return intersections.get(id);
    }

    @Override
    public String toString() {
        return "PlanDeVille{" +
                "intersections=" + intersections +
                '}';
    }
}
