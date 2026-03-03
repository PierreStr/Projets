package cartes;

import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteMilitaire extends Carte{
    
    public int nbr_bouclier;

    public CarteMilitaire (String name, int nbr_bouclier, String[] allow, String couleur, ImageView image, String age, Ressource...needs){
        super(name, allow, couleur, image, age, needs);
        this.nbr_bouclier = nbr_bouclier;
    }


    @Override
    public void playCarte(Player p, Player a, PlateauMilitaire plateauMilitaire) {

        if (p.jetons[7]){
            this.nbr_bouclier += 1;
        }

        plateauMilitaire.AvanceeMilitaire(p, a, this.nbr_bouclier);
        p.nbr_boucliers += this.nbr_bouclier;

        if(!(this.allow[0].equals("no"))){
            p.allows.add(this.allow[0]);
        }

        p.deckMilitaire.add(this);
    }
}