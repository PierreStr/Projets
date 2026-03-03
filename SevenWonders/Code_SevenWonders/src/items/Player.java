package items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cartes.Carte;
import cartes.CarteCommerce;
import cartes.CarteGuilde;
import jetons.Jeton;
import merveilles.Merveille;

public class Player {

    private static Player player = new Player();
    
    public int nbr_bois;    //
    public int nbr_argile;  //  Ressources primaire du joeur
    public int nbr_pierre;  //

    public int nbr_verre;   //  Ressources secondaire du joeur
    public int nbr_papyrus; //

    public int nbr_pieces;  // nombres de pieces du joueur
    public int nbr_boucliers;// nombre de boucliers du joueur

    public boolean jokerprimaire;             //
    public boolean jokersecondaire;           // 
    public boolean jokerprimairemerveille;    // bonus des cartes commerces du joueur
    public boolean jokersecondairemerveille;  // 
    public List<String> reductions;        //  

    public ArrayList<String> allows;

    public ArrayList<Carte> deckRessourcePrimaire;      //
    public ArrayList<Carte> deckRessourceSecondaire;    //
    public ArrayList<Carte> deckMilitaire;              //  Listes des cartes que possède le joueur
    public ArrayList<CarteCommerce> deckCommerce;               //
    public ArrayList<Carte> deckCivil;                  //  
    public ArrayList<CarteGuilde> deckGuilde;                 //
    public ArrayList<Carte> deckScientifique;           //
    public ArrayList<String> AvanceeScientifique;       //  Liste des avancées scientifique du joueur
    public ArrayList<Merveille> merveillesConstruites;      //  Liste des merveilles construites par le joueur
    public ArrayList<Merveille> merveillesAConstruire;

    public boolean canTakeJeton;
    public boolean[] jetons;   // dans l'ordre : [Agriculture, Architecture, Economie, Loi, Maconnerie, Mathématiques, Philosophie, Stratégie, Théologie, Urbanisme]
                               // (même ordre que dans les règles)
    public ArrayList<Jeton> playersJetons;
    public boolean[] bonusGuilde; // dans l'ordre: [GrisBrun, Rrouge, Jaune, Vert, Bleu, Merveille, Piece]
    public int points;            // nombres de points du joueur
    public boolean rejouer;       // true si le joueur peut rejouer
    public boolean[] win;           // Etat du joueur: gagne ou gagne pas (encore) pour militaire, scientifique, civil (ca c'est à la fin donc bon)

    public Boolean afficheNextAction;

    public Player (){

        this.nbr_bois = 0;
        this.nbr_argile = 0;
        this.nbr_pierre = 0;

        this.nbr_verre = 0;
        this.nbr_papyrus = 0;

        this.nbr_pieces = 7;
        this.nbr_boucliers = 0;

        this.jokerprimaire = false;
        this.jokersecondaire = false;
        this.jokerprimairemerveille = false;
        this.jokersecondairemerveille = false;
        this.reductions = new ArrayList<String>();

        this.allows = new ArrayList<>();

        this.deckRessourcePrimaire = new ArrayList<>();
        this.deckRessourceSecondaire = new ArrayList<>();
        this.deckMilitaire = new ArrayList<>();
        this.deckCommerce = new ArrayList<>();
        this.deckCivil = new ArrayList<>();
        this.deckGuilde = new ArrayList<>();
        this.deckScientifique = new ArrayList<>();
        this.AvanceeScientifique = new ArrayList<>();
        this.merveillesConstruites = new ArrayList<>();
        this.merveillesAConstruire = new ArrayList<>();

        this.canTakeJeton = false;
        this.jetons = new boolean[10];
        Arrays.fill(this.jetons, false);
        this.playersJetons = new ArrayList<>(); 
        
        this.bonusGuilde = new boolean[7]; 
        Arrays.fill(this.bonusGuilde, false);
        this.win = new boolean[]{false, false};
        this.rejouer = false;
        this.points = 0;

        this.afficheNextAction = false;
    }

    public static Player getPlayer(){
        return player;
    }

    public static void setPlayer(Player myplayer){
        player = myplayer;
    }

    public int getNbrType(String type) throws Exception{

        switch(type){
            case "bois":
                return this.nbr_bois;
            
            case "argile":
                return this.nbr_argile;

            case "pierre":
                return this.nbr_pierre;

            case "verre":
                return this.nbr_verre;

            case "papyrus":
                return this.nbr_papyrus;

            case "piece":
                return this.nbr_pieces;
        }
        
        throw new Exception("Ne demande pas un type existant");
    }

}
