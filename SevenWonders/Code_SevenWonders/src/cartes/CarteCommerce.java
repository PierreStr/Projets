package cartes;

import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteCommerce extends Carte{
    
    public String type;
    public int nbr_pieces;
    public int nbr_points;
    public ImageView carteRedImP;
    public ImageView carteRedImC;

    public CarteCommerce (String name, String type, int nbr_pieces, String[] allow, String couleur, ImageView image, ImageView carteRedImP, ImageView carteRedImC, String age, Ressource... needs){
        super(name, allow, couleur, image, age, needs);
        this.type = type;
        this.nbr_pieces = nbr_pieces;
        this.nbr_points = 3;
        this.carteRedImP = carteRedImP;
        this.carteRedImC = carteRedImC;
    }

    @Override
    public void playCarte(Player p, Player a, PlateauMilitaire plateauMilitaire) {

        switch(this.type){
            case "Brun":
                p.nbr_pieces += p.deckRessourcePrimaire.size() * this.nbr_pieces;
                break;

            case "Gris":
                p.nbr_pieces += p.deckRessourceSecondaire.size() * this.nbr_pieces;
                break;
                
            case "Rouge":
                p.nbr_pieces += p.deckMilitaire.size() * this.nbr_pieces;
                break;
                
            case "Jaune":
                p.nbr_pieces += p.deckCommerce.size() * this.nbr_pieces;
                break;
                
            case "Merveille":
                p.nbr_pieces += p.merveillesConstruites.size() * this.nbr_pieces;
                break;
                
            case "primaire":
                p.jokerprimaire = true;
                break;
                
            case "secondaire":
                p.jokersecondaire = true;
                break;
            
            case "pieces":
                p.nbr_pieces += this.nbr_pieces;

            case "reductionSecondaire":
                p.reductions.add("verre");
                p.reductions.add("papyrus");
                
            default:
                p.reductions.add(this.type);
        }
        if(!(this.allow[0].equals("no"))){
            p.allows.add(this.allow[0]);
        }

        p.deckCommerce.add(this);
    }
}