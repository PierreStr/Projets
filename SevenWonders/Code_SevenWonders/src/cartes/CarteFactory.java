package cartes;

import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteFactory {

    public Carte CreateCarteRessource (String name, String type, Ressource gain, String[] allow, ImageView image, String age, Ressource... needs){
        
        if(type == "Brun"){
            return new CarteRessources(name, gain, allow, "Brun", image, age, needs);
        } else{
            return new CarteRessources(name, gain, allow, "Gris", image, age, needs);
        }
    }

    public Carte CreateCarteMilitaire (String name, int nbr_bouclier, String[] allow, ImageView image, String age, Ressource... needs){
        return new CarteMilitaire(name, nbr_bouclier, allow, "Rouge", image, age, needs);
    }

    public Carte CreateCarteCivil (String name, int nbr_pts, String[] allow, ImageView image, String age, Ressource... needs){
        return new CarteCivil(name, nbr_pts, allow, "Bleu", image, age, needs);
    }

    public Carte CreateCarteCommerce (String name, String type, int nbr_pieces, String[] allow, ImageView image, ImageView carteRedImP, ImageView carteRedImC, String age, Ressource... needs){
        return new CarteCommerce(name, type, nbr_pieces, allow, "Jaune", image, carteRedImP, carteRedImC, age, needs);
    }

    public Carte CreateCarteScientifique (String name, String type, int nbr_points, String[] allow, ImageView image, String age, Ressource... needs){
        return new CarteScientifique(name, type, nbr_points, allow, "Vert", image, age, needs);
    }

    public Carte CreateCarteGuilde (String name, String type, String[] allow, ImageView image, ImageView carteRedImP, ImageView carteRedImC, String age, Ressource... needs){
        return new CarteGuilde(name, type, allow, "Mauve", image, carteRedImP, carteRedImC, age, needs);
    }

}
