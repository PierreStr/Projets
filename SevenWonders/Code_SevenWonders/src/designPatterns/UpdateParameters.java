package designPatterns;

import java.util.ArrayList;

import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jetons.Jeton;

public class UpdateParameters {

    public static UpdateParameters params;

    public Player p;
    public Commandant c;
    public AnchorPane[] ressourcePlayer;
    public AnchorPane[] ressourceCommandant;
    public AnchorPane[] nbrCartesPlayer;
    public AnchorPane[] nbrCartesCommandant;
    public VBox VBmerveilles;
    public VBox[] VBcartesreduites;
    public Jeton[] jetonsList;
    public VBox VBjetonsP;
    public VBox VBjetonsC;
    public ArrayList<Jeton> jetonsRestants;
    public PlateauMilitaire PM;
    public StackPane[] SP;

    // Constructor
    public UpdateParameters(Player p, Commandant c, AnchorPane[] ressourcePlayer, AnchorPane[] ressourceCommandant, 
                            AnchorPane[] nbrCartesPlayer, AnchorPane[] nbrCartesCommandant, VBox VBmerveilles, 
                            VBox[] VBcartesreduites, Jeton[] jetonsList, VBox VBjetonsP, VBox VBjetonsC, 
                            ArrayList<Jeton> jetonsRestants, PlateauMilitaire PM, StackPane[] SP) {
        this.p = p;
        this.c = c;
        this.ressourcePlayer = ressourcePlayer;
        this.ressourceCommandant = ressourceCommandant;
        this.nbrCartesPlayer = nbrCartesPlayer;
        this.nbrCartesCommandant = nbrCartesCommandant;
        this.VBmerveilles = VBmerveilles;
        this.VBcartesreduites = VBcartesreduites;
        this.jetonsList = jetonsList;
        this.VBjetonsP = VBjetonsP;
        this.VBjetonsC = VBjetonsC;
        this.jetonsRestants = jetonsRestants;
        this.PM = PM;
        this.SP = SP;
    }

    public static UpdateParameters getParameters(){
        return params;
    }

    public static void setParameters(UpdateParameters parameteres){
        params = parameteres;
    }
}
