package cartes;

import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteCivil extends Carte{

    public int nbr_pts;
    
    public CarteCivil (String name, int nbr_pts, String[] allow, String couleur, ImageView image, String age, Ressource... needs){
        super(name, allow, couleur, image, age, needs);
        this.nbr_pts = nbr_pts;
    }

    @Override
    public void playCarte(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += this.nbr_pts;
        if(!(this.allow[0].equals("no"))){
            p.allows.add(this.allow[0]);
        }
        p.deckCivil.add(this);
    }


    public static int countPoint (Player p){
        int nbr_points = 0;
        for (Carte carte : p.deckCivil) {
            nbr_points += ((CarteCivil) carte).nbr_pts;  
        }
        return nbr_points;
    }
}
