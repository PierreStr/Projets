package cartes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import functions.Affichage;
import functions.FonctionUtile;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.scene.image.ImageView;

public class Carte {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/";

    public String name;
    public Ressource[] needs;
    public String couleur;
    public String[] allow;   // en [0] la carte qui d'avant et en [1] la carte construite gratuitement

    // Pour la disposition
    public Carte bottom_left;
    public Carte bottom_right;
    public ImageView image;
    public Boolean isReturned;
    public String age;

    public Carte (String name, String[] allow, String couleur, ImageView image, String age, Ressource... needs){

        this.name = name;
        this.allow = allow;
        this.couleur = couleur;
        this.needs = needs;
        this.bottom_left = null;
        this.bottom_right = null;
        this.image = image;
        this.image.setOnMouseClicked(event -> FonctionUtile.ImageViewClicked(this));
        this.isReturned = false;
        this.age = age;
    }

    public Carte() {
        this.name = "";
        this.allow = null;
        this.couleur = null;
        this.needs = null;
        this.bottom_left = null;
        this.bottom_right = null;
        this.image = Affichage.makeImageView("cartes/existepas.png");
        this.isReturned = false;
        this.age = "";
    }

    public static boolean estUnDesTypesLesPlusChers(Player p, Player a, String type, Ressource[] needs) throws Exception{

        boolean reponse = false;
        int max = 0;
        String maxType = "coucou";

        for (Ressource ressource : needs) {
            String string = ressource.getType();
            if (p.reductions.contains(string) && max < 1){
                max = 1;
                maxType = string;
            } else if (max < 2 + a.getNbrType(string)){
                max = 2 + a.getNbrType(string);
                maxType = string;
            }
        }

        if (maxType == type){reponse = true;}

        return reponse;
    }

    public static int jokerRessource(Player p, Player a, String type, int qtt, boolean alreadyUse, boolean pjoker, Ressource[] needs) throws Exception{
        
        if(pjoker){
            
            if(!alreadyUse &&
                p.getNbrType(type) < qtt &&
                estUnDesTypesLesPlusChers(p, a, type, needs)
                ){
                alreadyUse = true;
                qtt-=1;
                
                for (int i = 0; i < needs.length; i++) {
                    if (needs[i].getType() == type){
                        needs[i].setQuantite(qtt-1);
                    }
                }
            }
        }
        return qtt;
    }

    // ATTENTION !!! return -1 si le joueur ne peut pas, n si le joueur paie n pieces
    public int canPlayCarte(Player p, Player a){

        ArrayList<Carte> deck = new ArrayList<>();
        ArrayList<CarteCommerce> deckCommerce = new ArrayList<>();
        switch(this.couleur){
            case "Rouge":
                deck = p.deckMilitaire;
                break;
            
            case "Jaune":
                deckCommerce = p.deckCommerce;
                break;
                
            case "Vert":
                deck = p.deckScientifique;
                break;
                
            case "Bleu":
                deck = p.deckCivil;
                break;
        }

        for (Carte carte : deck) {
            if (carte.allow[0] == this.allow[1]){
                if (p.jetons[9]){
                    p.nbr_pieces += 4;
                }
                return 0;
            }
        }

        for (CarteCommerce carte : deckCommerce) {
            if (carte.allow[0] == this.allow[1]){
                if (p.jetons[9]){
                    p.nbr_pieces += 4;
                }
                return 0;
            }
        }

        boolean alreadyUsePrimaire = false;
        boolean alreadyUseSecondaire = false;
        boolean alreadyUsePrimairemerveille = false;
        boolean alreadyUseSecondairemerveille = false;

        HashSet<String> typesNeeded = new HashSet<>();
        for (int i = 0; i < this.needs.length; i++) {
            String type = needs[i].getType();
            typesNeeded.add(type);
        }

        int nbr_pieces = p.nbr_pieces;
        int total = 0;

        int piecesNecessaire = 0;

        for (int i = 0; i < this.needs.length; i++) {
            
            String type = needs[i].getType();
            int qtt = needs[i].getQuantite();

            try{

                // Si le joueur n'a pas assez d'un type et qu'il a une carte commerce joker (cas primaire)
                // On retire un à qtt de ce dont il a pas assez puis on bloc la capacité (alreadyUse)
                if(type == "bois" || type == "pierre" || type == "argile"){
                    qtt = jokerRessource(p, a, type, qtt, alreadyUsePrimaire, p.jokerprimaire, this.needs);
                    qtt = jokerRessource(p, a, type, qtt, alreadyUsePrimairemerveille, p.jokerprimairemerveille, this.needs);
                
                // Si le joueur n'a pas assez d'un type et qu'il a une carte commerce joker (cas secondaire)
                } else if (type == "verre" || type == "papyrus"){
                    qtt = jokerRessource(p, a, type, qtt, alreadyUseSecondaire, p.jokersecondaire, this.needs);
                    qtt = jokerRessource(p, a, type, qtt, alreadyUseSecondairemerveille, p.jokersecondairemerveille, this.needs);
                }
                
                if(this.couleur == "bleu" && p.jetons[4]){
                    qtt = jokerRessource(p, a, type, qtt, false, true, this.needs);
                    qtt = jokerRessource(p, a, type, qtt, false, true, this.needs);
                }

                if(type != "piece" && p.getNbrType(type) < qtt){    // Si le joeur n'a pas la bonne qtt
                    qtt -= p.getNbrType(type);

                    if(p.reductions.contains(type) && nbr_pieces >= qtt){   // Si avec des pieces (commerce)
                        total += qtt;                                       // avec ou sans reduction
                                                                                 
                    }else if(!p.reductions.contains(type) && nbr_pieces >= qtt * (2 + a.getNbrType(type))){
                        total += qtt * (2 + a.getNbrType(type));
    
                    } else {
                        return -1;
                    }  
                }

                if (type == "piece"){
                    total += qtt;
                    piecesNecessaire = qtt;
                }

                nbr_pieces = p.nbr_pieces - total;

            }catch(Exception e){
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        if (a.jetons[2]){
            a.nbr_pieces += total - piecesNecessaire;
        }

        return total;
    }

    public static int canConstructCarte(Player p, Player a, Carte carte){
        return carte.canPlayCarte(p, a);
    }


    public void playCarte (Player p, Player a, PlateauMilitaire plateauMilitaire){
        System.out.println("Méthode de la classe Carte");
    }

    public void DestroyCarte(){
        System.out.println("Méthode de la classe Carte");
    }
}
