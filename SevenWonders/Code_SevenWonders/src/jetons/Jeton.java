package jetons;

import functions.Affichage;
import items.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Jeton {

    public ImageView image;
    public ImageView smallImage;
    public static Jeton[] jetonsList = new Jeton[5];

    public Jeton(ImageView image, ImageView smallImage){
        this.image = image;
        this.smallImage = smallImage;
    }

    public Jeton(){
        this.image = Affichage.makeImageView("existepas");
        this.smallImage = null;
    }

    public static Jeton[] getJetons(){
        return jetonsList;
    }

    public static void setJetons(Jeton[] jetons){
        jetonsList = jetons;
    }

    public void removeFrom(Jeton[] jetonsList, StackPane SP){

        for (int i = 0; i < jetonsList.length; i++) {
            if (jetonsList[i] == this){
                jetonsList[i] = new Jeton();
                SP.getChildren().remove(this.image);
            }
        }
    }

    public void PlayJeton(Player p){
        System.err.println("Méthode de la class Jeton");
    }
} 
