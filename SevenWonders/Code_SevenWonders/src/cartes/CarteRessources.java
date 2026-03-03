package cartes;

import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class CarteRessources extends Carte{

    public Ressource gain;

    public CarteRessources (String name, Ressource gain, String[] allow, String couleur, ImageView image, String age, Ressource... needs){

        super(name, allow, couleur, image, age, needs);
        this.gain = gain;
    }

    @Override
    public void playCarte(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        String type = this.gain.getType();
        int qtt = this.gain.getQuantite();

        switch(type){
            case "bois":
                p.nbr_bois += qtt;
                p.deckRessourcePrimaire.add(this);
                break;
            
            case "argile":
                p.nbr_argile += qtt;
                p.deckRessourcePrimaire.add(this);
                break;

            case "pierre":
                p.nbr_pierre += qtt;
                p.deckRessourcePrimaire.add(this);
                break;

            case "verre":
                p.nbr_verre += qtt;
                p.deckRessourceSecondaire.add(this);
                break;

            case "papyrus":
                p.nbr_papyrus += qtt;
                p.deckRessourceSecondaire.add(this);
                break;

        }
    }

    public void DestroyCarte(){

        String type = this.gain.getType();
        int qtt = this.gain.getQuantite();
        Commandant comm = Commandant.getCommandant();

        switch(type){
            case "bois":
                comm.nbr_bois -= qtt;
                comm.deckRessourcePrimaire.remove(this);
                break;
            
            case "argile":
                comm.nbr_argile -= qtt;
                comm.deckRessourcePrimaire.remove(this);
                break;

            case "pierre":
                comm.nbr_pierre -= qtt;
                comm.deckRessourcePrimaire.remove(this);
                break;

            case "verre":
                comm.nbr_verre -= qtt;
                comm.deckRessourceSecondaire.remove(this);
                break;

            case "papyrus":
                comm.nbr_papyrus -= qtt;
                comm.deckRessourceSecondaire.remove(this);
                break;

        }
    }
}