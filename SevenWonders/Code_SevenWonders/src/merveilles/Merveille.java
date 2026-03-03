package merveilles;

import java.util.HashSet;

import cartes.Carte;
import designPatterns.ChoosingScene;
import designPatterns.PrincipalPane;
import designPatterns.Pyramide;
import designPatterns.UpdateParameters;
import functions.Affichage;
import functions.FonctionUtile;
import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Merveille {
    
    public String name;
    public Ressource[] needs;
    public ImageView image;

    public Merveille (String merveille, ImageView image, Ressource... needs){
        this.name = merveille;
        this.needs = needs;
        this.image = image;
    }


    public static void setActionOnMerveilleClicked(Merveille merveille, Carte carte){
        merveille.image.setOnMouseClicked(event -> {

            Player player = Player.getPlayer();
            Commandant comm = Commandant.getCommandant();
            PlateauMilitaire plateau = PlateauMilitaire.getPlateauMilitaire();
            int cout = merveille.canPlayMerveille(player, comm);

            if (cout > -1 && cout < player.nbr_pieces){
                player.nbr_pieces -= cout;
                merveille.playMerveille(player, comm, plateau);
                player.merveillesConstruites.add(merveille);

                for (Merveille merv : player.merveillesAConstruire) {
                    merv.image.setDisable(true);
                }
                player.merveillesAConstruire.remove(merveille);

                FonctionUtile.DisableCards(false);

                FonctionUtile.removecard(carte);
                Affichage.updateAll(UpdateParameters.getParameters());

                if (!(merveille instanceof CircusMaximus || merveille instanceof LaGrandeBibliotheque ||
                      merveille instanceof LeMausolee    || merveille instanceof LaStatueZeus)){
                    
                    if (Pyramide.getCurrentAgePyramide().isNull()){

                        if (Pyramide.getCurrentAgePyramide() == Pyramide.getThirdAgePyramide()){
                            FonctionUtile.updatePointsAndDecideWinner();
                        } else {
                            Pyramide.upgradeAgePyramide();
                            Pyramide.printpyramide(PrincipalPane.getPrincipalPane(), Pyramide.getCurrentAgePyramide());
                            FonctionUtile.DisableCards(true);
                            PauseTransition pause = new PauseTransition(Duration.seconds(1));
                            pause.setOnFinished(e -> ChoosingScene.createChoosingScene());
                            pause.play();
                        } 
                        
                    } else {
                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                        pause.setOnFinished(e -> {
                            if (!player.rejouer){
                                Commandant.play();
                            }
                        });
                        pause.play();
                    }
                }      
            }
        });
    }


    public int canPlayMerveille(Player p, Commandant a){

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

        for (int i = 0; i < this.needs.length; i++) {
            
            String type = needs[i].getType();
            int qtt = needs[i].getQuantite();

            try{

                // Si le joueur n'a pas assez d'un type et qu'il a une carte commerce joker (cas primaire)
                // On retire un à qtt de ce dont il a pas assez puis on bloc la capacité (alreadyUse)
                if (type == "bois" || type == "pierre" || type == "argile"){
                    qtt = Carte.jokerRessource(p, a, type, qtt, alreadyUsePrimaire, p.jokerprimaire, this.needs);
                    qtt = Carte.jokerRessource(p, a, type, qtt, alreadyUsePrimairemerveille, p.jokerprimairemerveille, this.needs);
                
                // Si le joueur n'a pas assez d'un type et qu'il a une carte commerce joker (cas secondaire)
                } else if (type == "verre" || type == "papyrus"){
                    qtt = Carte.jokerRessource(p, a, type, qtt, alreadyUseSecondaire, p.jokersecondaire, this.needs);
                    qtt = Carte.jokerRessource(p, a, type, qtt, alreadyUseSecondairemerveille, p.jokersecondairemerveille, this.needs);
                }

                if (p.jetons[1]){
                    qtt = Carte.jokerRessource(p, a, type, qtt, false, true, this.needs);
                    qtt = Carte.jokerRessource(p, a, type, qtt, false, true, this.needs);
                }
                
                if(p.getNbrType(type) < qtt){    // Si le joeur n'a pas la bonne qtt
                    qtt -= p.getNbrType(type);

                    if(p.reductions.contains(type) && nbr_pieces >= qtt){   // Si avec des pieces (commerce)
                        total += qtt;                                       // avec ou sans reduction
                                                                                 
                    }else if(!p.reductions.contains(type) && nbr_pieces >= qtt * (2 + a.getNbrType(type))){
                        total += qtt * (2 + a.getNbrType(type));
    
                    } else {
                        return -1;
                    }
    
                    nbr_pieces = p.nbr_pieces - total;
                    
                }
            }catch(Exception e){
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        if (a.jetons[2]){
            a.nbr_pieces += total;
        }

        return total;
    }


    public static int canConstructMerveille(Player p, Commandant a, Merveille merveille){
        return merveille.canPlayMerveille(p, a);
    }

    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        System.err.println("Méthode de la classe Merveille");
    }
}
