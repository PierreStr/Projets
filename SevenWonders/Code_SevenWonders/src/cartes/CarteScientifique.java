package cartes;

import java.util.HashSet;

import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteScientifique extends Carte{
    
    public String type;
    public int nbr_points;

    public CarteScientifique (String name, String type, int nbr_points, String[] allow, String couleur, ImageView image, String age, Ressource... needs){
        super(name, allow, couleur, image, age, needs);
        this.type = type;
        this.nbr_points = nbr_points;
    }


    @Override
    public void playCarte(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += this.nbr_points;                                  // Ajoute les pts

        boolean bool = p.AvanceeScientifique.contains(this.type);     //  dis si le joueur peut prendre un jeton
        p.AvanceeScientifique.add(this.type);                         //  ajout a la liste
        
        HashSet<String> mySet = new HashSet<>(p.AvanceeScientifique); 
        if ( mySet.size() == 6) {                                     //  Regarde si le joueur a gagné ou non
            p.win[1] = true;
        }

        p.canTakeJeton = bool;

        if(!(this.allow[0].equals("no"))){
            p.allows.add(this.allow[0]);
        }

        p.deckScientifique.add(this);
    }
}
