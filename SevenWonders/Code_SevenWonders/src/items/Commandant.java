package items;
import static items.CarteDecision.getCartesDecisions;
import static items.CarteDecision.ind;

import java.util.ArrayList;

import cartes.Carte;
import designPatterns.PrincipalPane;
import designPatterns.Pyramide;
import designPatterns.UpdateParameters;
import functions.Affichage;
import functions.FonctionUtile;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import jetons.Jeton;

public class Commandant extends Player{

    public String couleurCommandant;
    public String symbole;
    public String name;
    public Jeton[] jetonsdonnes;

    private static Commandant commandant = new Commandant();

    public Commandant (String name, String couleurCommandant, String symbole, Jeton... jetons){

        super();
        this.nbr_pieces = 0;
        this.couleurCommandant = couleurCommandant;
        this.symbole = symbole;
        this.jetonsdonnes = jetons;

        this.name = name;
    }

    public Commandant (){
        super();
        this.nbr_pieces = 0;
    }

    public static Commandant getCommandant(){
        return commandant;
    }

    public static void setCommandant(Commandant mycommandant){
        commandant = mycommandant;
    }


    public Carte choose(CarteDecision carteDecision, ArrayList<Carte> cartesAccessibles){

        if (carteDecision.sens == "droite"){
            for (String str : carteDecision.priorites) {
                for (int i = cartesAccessibles.size()-1; i >= 0; i--) {
                    Carte carte = cartesAccessibles.get(i);
                    if (carte.couleur == str || (str == "comm" && carte.couleur == this.couleurCommandant)){
                        return carte;
                    }
                }
            }

            if (cartesAccessibles.size() == 0){return new Carte();}
            else{return cartesAccessibles.get(cartesAccessibles.size()-1);}

        } else {
            for (String str : carteDecision.priorites) {
                for (int i = 0; i < cartesAccessibles.size(); i++) {
                    Carte carte = cartesAccessibles.get(i);
                    if (carte.couleur == str || (str == "comm" && carte.couleur == this.couleurCommandant)){
                        return carte;
                    }
                }
            }
        }
        if (cartesAccessibles.size() == 0){return new Carte();}
        else{return cartesAccessibles.get(0);} 
    }


    public static void play(){
        

        Commandant comm = getCommandant();
        Player player = Player.getPlayer();
        PlateauMilitaire plateau = PlateauMilitaire.getPlateauMilitaire();


        CarteDecision carteDecision = getCartesDecisions().get(ind);
        
        ArrayList<Carte> cartesAccessibles = Pyramide.cartesAccessibles(Pyramide.getCurrentAgePyramide());

        Carte carte = comm.choose(carteDecision, cartesAccessibles);
        
        if (carte.age == ""){

        } else {
            carte.playCarte(comm, player, plateau);

            FonctionUtile.removecard(carte);
            Affichage.updateAll(UpdateParameters.getParameters());
            CarteDecision.increment();
            if (player.afficheNextAction){
                PrincipalPane.getImageViewDecision().setImage(CarteDecision.getCartesDecisions().get(ind).im);
            }
            
            Pyramide current = Pyramide.getCurrentAgePyramide();

            if (current.isNull() && !(current == Pyramide.getThirdAgePyramide())){
                Pyramide.upgradeAgePyramide();
                Pyramide.printpyramide(PrincipalPane.getPrincipalPane(), Pyramide.getCurrentAgePyramide());
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> play());
                pause.play();
            }
            
            if (current.isNull() && current == Pyramide.getThirdAgePyramide()){
                FonctionUtile.updatePointsAndDecideWinner();
            }
    
            if ((comm.symbole == carteDecision.symbole || comm.symbole == "tout") && carteDecision.symbole != "rien"){
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> play());
                pause.play();
            }
        }
        FonctionUtile.DisableCards(false);
    }
}