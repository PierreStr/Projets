package cartes;

import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteGuilde extends Carte{
    
    public String type;
    public ImageView carteRedImP;
    public ImageView carteRedImC;

    public CarteGuilde (String name, String type, String[] allow, String couleur, ImageView image, ImageView carteRedImP, ImageView carteRedImC, String age, Ressource... needs){
        super(name, allow, couleur, image, age, needs);
        this.type = type;
        this.carteRedImP = carteRedImP;
        this.carteRedImC = carteRedImC;
    }

    @Override
    public void playCarte (Player p, Player a, PlateauMilitaire plateauMilitaire){

        int max = 0;

        switch(this.couleur){
            case "GrisBrun":
                max = Math.max(p.deckRessourcePrimaire.size() + p.deckRessourceSecondaire.size(), a.deckRessourcePrimaire.size() + a.deckRessourceSecondaire.size());
                p.bonusGuilde[0] = true;
                break;
            
            case "Rouge":
                max = Math.max(p.deckMilitaire.size(), a.deckMilitaire.size());
                p.bonusGuilde[1] = true;
                break;
            
            case "Jaune":
                max = Math.max(p.deckCommerce.size(), a.deckCommerce.size());
                p.bonusGuilde[2] = true;
                break;
            
            case "Vert":
                max = Math.max(p.deckScientifique.size(), a.deckScientifique.size());
                p.bonusGuilde[3] = true;
                break;
            
            case "Bleu":
                max = Math.max(p.deckCivil.size(), a.deckCivil.size());
                p.bonusGuilde[4] = true;
                break;
            
            case "Merveille":
                p.bonusGuilde[5] = true;
                break;
            
            case "Piece":
                p.bonusGuilde[6] = true;
                break;
        }

        p.nbr_pieces += max;
        p.deckGuilde.add(this);
    }


    public static void countPoints (Player p, Player a){

        int nbr_points = 0;

        for (CarteGuilde carte : p.deckGuilde) {

            int max = 0;

            switch(carte.couleur){
                case "GrisBrun":
                    max = Math.max(p.deckRessourcePrimaire.size() + p.deckRessourceSecondaire.size(), a.deckRessourcePrimaire.size() + a.deckRessourceSecondaire.size());
                    break;
                
                case "Rouge":
                    max = Math.max(p.deckMilitaire.size(), a.deckMilitaire.size());
                    break;
                
                case "Jaune":
                    max = Math.max(p.deckCommerce.size(), a.deckCommerce.size());
                    break;
                
                case "Vert":
                    max = Math.max(p.deckScientifique.size(), a.deckScientifique.size());
                    break;
                
                case "Bleu":
                    max = Math.max(p.deckCivil.size(), a.deckCivil.size());
                    break;
                
                case "Merveille":
                    break;
                
                case "Piece":
                    max = Math.max(p.nbr_pieces/3, a.nbr_pieces/3);
                    break;
            }
            nbr_points += max;
        }

        p.points += nbr_points;
    }
}