package items;

import java.io.File;
import java.util.Arrays;

import functions.Affichage;
import functions.Lancement;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlateauMilitaire {
    
    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/";

    public int[] situationMilitaire;
    public int position;
    public boolean[] saccagePieces;
    public ImageView imagePion;
    public StackPane sp;
    public ImageView saccagepieces2d = Affichage.makeImageView("militaire/saccagepieces2d.png");
    public ImageView saccagepieces5d = Affichage.makeImageView("militaire/saccagepieces5d.png");
    public ImageView saccagepieces2g = Affichage.makeImageView("militaire/saccagepieces2g.png");
    public ImageView saccagepieces5g = Affichage.makeImageView("militaire/saccagepieces5g.png");
    private static PlateauMilitaire plateau;

    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;
    
    public PlateauMilitaire (){
        this.situationMilitaire = new int[19];
        this.situationMilitaire[9] = 1;
        this.position = 9;
        this.saccagePieces = new boolean[4];
        this.imagePion = Affichage.makeImageView("militaire/pion.png");
        Arrays.fill(this.saccagePieces, false);
        ImageView imageView = Affichage.makeImageView("militaire/plateaumilitaire.png");
        
        
        this.sp = new StackPane(imageView);
        
        saccagepieces2d.setTranslateX(168*factorL);
        saccagepieces2d.setTranslateY(37*factorl);

        saccagepieces5d.setTranslateX(293*factorL);
        saccagepieces5d.setTranslateY(38*factorl);

        saccagepieces2g.setTranslateX(-167*factorL);
        saccagepieces2g.setTranslateY(37*factorl);

        saccagepieces5g.setTranslateX(-293*factorL);
        saccagepieces5g.setTranslateY(38*factorl);
        this.sp.getChildren().addAll(saccagepieces2d, saccagepieces5d, saccagepieces2g, saccagepieces5g);
    }

    public static PlateauMilitaire getPlateauMilitaire(){
        return plateau;
    }

    public static void setPlateauMilitaire(PlateauMilitaire plateauMilitaire){
        plateau = plateauMilitaire;
    }

    public void updatePlateauMilitaire(int deplacement, Player p, Player a){

        this.situationMilitaire[this.position + deplacement] = 1;
        this.situationMilitaire[this.position] = 0;
        this.position += deplacement;

        if(this.position > 0 && this.position < 4 && !this.saccagePieces[0]){
            a.nbr_pieces = Math.max(a.nbr_pieces - 5, 0);
            this.saccagePieces[0] = true;
            sp.getChildren().remove(saccagepieces5g);

        } else if(this.position > 3 && this.position < 7 && !this.saccagePieces[1]){
            a.nbr_pieces = Math.max(a.nbr_pieces - 2, 0);
            this.saccagePieces[1] = true;
            sp.getChildren().remove(saccagepieces2g);

        } else if(this.position > 11 && this.position < 15 && !this.saccagePieces[2]){
            a.nbr_pieces = Math.max(a.nbr_pieces - 2, 0);
            this.saccagePieces[2] = true;
            sp.getChildren().remove(saccagepieces2d);
            
        } else if(this.position > 14 && this.position < 18 && !this.saccagePieces[2]){
            a.nbr_pieces = Math.max(a.nbr_pieces - 5, 0);
            this.saccagePieces[3] = true;
            sp.getChildren().remove(saccagepieces5d);
        }
    }


    // avance le pion de la guerre dans le sens de p avec le nbr de boucliers
    public void AvanceeMilitaire(Player p, Player a, int boucliers){
        if(p instanceof Commandant && this.position - boucliers > 0){

            this.updatePlateauMilitaire(-boucliers, p, a);

        } else if(!(p instanceof Commandant) && this.position + boucliers < 18){

            this.updatePlateauMilitaire(boucliers, p, a);
        
        // Si celui qui a avancé le pion gagne
        } else if(!(p instanceof Commandant) && this.position + boucliers >= 18){
            p.win[0] = true;
            this.situationMilitaire[18] = 1;
            this.situationMilitaire[this.position] = 0;
            this.position = 18;
        } else if(p instanceof Commandant && this.position - boucliers <= 0){
            p.win[0] = true;
            this.situationMilitaire[0] = 1;
            this.situationMilitaire[this.position] = 0;
            this.position = 0;
        }
    }
}
