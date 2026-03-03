package functions;

import java.io.InputStream;
import java.util.ArrayList;

import cartes.CarteCommerce;
import cartes.CarteGuilde;
import designPatterns.ChoosingScene;
import designPatterns.PrincipalPane;
import designPatterns.SingletonPrymaryStage;
import designPatterns.UpdateParameters;
import items.CarteDecision;
import items.Commandant;
import items.Defausse;
import items.PlateauMilitaire;
import items.Player;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jetons.Jeton;
import merveilles.Merveille;


public class Affichage {
    
    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;


    // return une imageView de l'image à l'emplacement filePath
    public static ImageView makeImageView(String filePath, String ID){

        InputStream input = Affichage.class.getResourceAsStream("/images/"+filePath);

        if (input != null) {
            Image newImage = new Image(input);
            ImageView imageView = new ImageView(newImage);
            FonctionUtile.setScaleImage(imageView, factorL, factorl);
            imageView.setId(ID);

            return imageView;
        }

        return null;
    }


    public static ImageView makeImageView(String filePath){

        InputStream input = Affichage.class.getResourceAsStream("/images/"+filePath);

        if (input != null) {
            Image newImage = new Image(input);
            ImageView imageView = new ImageView(newImage);
            FonctionUtile.setScaleImage(imageView, factorL, factorl);
            return imageView;
        }

        return null;
    }

    public static ImageView makeImageView(String filePath, Boolean bool){

        InputStream input = Affichage.class.getResourceAsStream("/images/"+filePath);

        if (input != null) {
            Image newImage = new Image(input);
            ImageView imageView = new ImageView(newImage);
            if (bool){
                FonctionUtile.setScaleImage(imageView, factorL, factorl);
            }

            return imageView;
        }

        return null;
    }

    // Affiche le plateau de jeu
    // Return 6 StackPane dans l'ordre : 
    // ressources player, ressources commandant, commandant, plateau militaire, plateau jetons 
    // nbr cartes joueur, nbr cartes commandant, defausse
    public static StackPane[] makeBoardGame(String commandant, Pane pane, Stage primaryStage){
        
        // définition du fond 
        InputStream input = Affichage.class.getResourceAsStream("/images/fond.jpg");

        Image newImage = new Image(input);
        ImageView backgroundImageView = new ImageView(newImage);

        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        pane.getChildren().add(backgroundImageView);  

        // création des ImageViews
        ImageView IMcommandantView = makeImageView("commandants/"+commandant+".jpg");
        ImageView IMressPlayerView = makeImageView("commandants/ressourcesplayer.png");
        ImageView IMressCommandantView = makeImageView("commandants/ressourcescommandant.png");
        ImageView IMplateauJetonsView = makeImageView("jetons/plateaujetons.png");
        ImageView IMnbrCartesPlayerView = makeImageView("statistiques/tableaunbrcartes.png");
        ImageView IMnbrCartesCommandantView = makeImageView("statistiques/tableaunbrcartesadv.png");

        if (Player.getPlayer().afficheNextAction){
            ImageView IMnextAction = new ImageView(CarteDecision.getCartesDecisions().get(0).im);

            IMnextAction.setLayoutY(650*factorl);
            IMnextAction.setLayoutX(1375*factorL);

            PrincipalPane.setImageViewDecision(IMnextAction);

            pane.getChildren().add(IMnextAction);
        }

        // instanciation du boutton de la defausse
        Button button = ButtonFunctions.createButton("boutons/defausse.png", 978*factorL, 800*factorl);
        Defausse.getDefausse().button = button;
        button.setOnAction(event -> {
            Stage newStage = new Stage();

            ChoosingScene defausseScene = new ChoosingScene(Defausse.getDefausse().cartes, "autre chose");
            // Affiche la scène dans une nouvelle fenêtre
            newStage.setScene(defausseScene.scene);
            
            newStage.setTitle("Défausse");
            newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show(); 
        });

        // création des StackPane
        StackPane SPressPlayer = new StackPane(IMressPlayerView);
        StackPane SPcommandant = new StackPane(IMcommandantView);
        StackPane SPressCommandant = new StackPane(IMressCommandantView);
        StackPane SPplateauJetons = new StackPane(IMplateauJetonsView);
        StackPane SPnbrCartesPlayer = new StackPane(IMnbrCartesPlayerView);
        StackPane SPnbrCartesCommandant = new StackPane(IMnbrCartesCommandantView);

        SPcommandant.setLayoutX(1455*factorL);
        SPcommandant.setLayoutY(0);

        SPressCommandant.setLayoutX(1236*factorL);
        SPressCommandant.setLayoutY(0);

        PlateauMilitaire.getPlateauMilitaire().sp.setLayoutX(365*factorL);
        PlateauMilitaire.getPlateauMilitaire().sp.setLayoutY(0);

        SPplateauJetons.setLayoutX(452*factorL);
        SPplateauJetons.setLayoutY(800*factorl);

        SPnbrCartesPlayer.setLayoutX(225*factorL);
        SPnbrCartesPlayer.setLayoutY(820*factorl);

        SPnbrCartesCommandant.setLayoutX(1156*factorL);
        SPnbrCartesCommandant.setLayoutY(820*factorl);

        pane.getChildren().addAll(SPressPlayer, SPressCommandant, SPcommandant, PlateauMilitaire.getPlateauMilitaire().sp, SPplateauJetons,
        SPnbrCartesPlayer, SPnbrCartesCommandant);
        
        return new StackPane[]{SPressPlayer, SPressCommandant, SPcommandant, PlateauMilitaire.getPlateauMilitaire().sp, 
                               SPplateauJetons, SPnbrCartesPlayer, SPnbrCartesCommandant};
    }

    // Initialise les AnchorPanes pour les ressources 
    public static AnchorPane[] displayRessources(Player p, StackPane SP){
        AnchorPane[] ListAnchorPane = new AnchorPane[8]; // Crée un tableau de 8 éléments

        for (int i = 0; i < ListAnchorPane.length; i++) {

            AnchorPane anchorPane = new AnchorPane();
            ImageView myImageView;

            if((i == 0 && !(p instanceof Commandant))){
                myImageView = makeImageView("statistiques/7.png");
            } else {
                myImageView = makeImageView("statistiques/0.png");
            }                

            anchorPane.getChildren().add(myImageView);
            
            if(i%2 == 0){
                AnchorPane.setTopAnchor(myImageView, (22.0+ 39*(i))*factorl);

                if(!(p instanceof Commandant)){
                    AnchorPane.setLeftAnchor(myImageView, 205.0*factorL);
                } else{
                    AnchorPane.setLeftAnchor(myImageView, 22.0*factorL);
                }  
            }else{
                AnchorPane.setTopAnchor(myImageView, (22.0+ 39*(i-1))*factorl);

                if(!(p instanceof Commandant)){
                    AnchorPane.setLeftAnchor(myImageView, 300.0*factorL);
                } else{
                    
                    AnchorPane.setLeftAnchor(myImageView, 117.0*factorL);
                } 
            }
            ListAnchorPane[i] = anchorPane; // Ajoute l'AnchorPane créé au tableau
            SP.getChildren().add(anchorPane);
        }
        return ListAnchorPane;
    }

    // Update les ressources du player p si on donne la bonne list d'AnchorPane
    public static void updateRessources(Player p, AnchorPane[] ListAP){

        int[] ListOfNum = new int[8];

        if(p instanceof Commandant){
            ListOfNum[0] = p.nbr_bois;
            ListOfNum[1] = p.nbr_pieces;
            ListOfNum[2] = p.nbr_argile;
            ListOfNum[3] = p.nbr_pierre;
            ListOfNum[4] = p.nbr_papyrus;
            ListOfNum[5] = p.nbr_verre;
            ListOfNum[6] = p.points;
            ListOfNum[7] = p.nbr_boucliers;
        } else{
            ListOfNum[0] = p.nbr_pieces;
            ListOfNum[1] = p.nbr_bois;
            ListOfNum[2] = p.nbr_pierre;
            ListOfNum[3] = p.nbr_argile;
            ListOfNum[4] = p.nbr_verre;
            ListOfNum[5] = p.nbr_papyrus;
            ListOfNum[6] = p.nbr_boucliers;
            ListOfNum[7] = p.points;
        }
        for (int i = 0; i < 8; i++) {
            double top = AnchorPane.getTopAnchor(ListAP[i].getChildren().get(0));
            double left = AnchorPane.getLeftAnchor(ListAP[i].getChildren().get(0));
            ListAP[i].getChildren().clear();
            ImageView myImageView = makeImageView("statistiques/"+String.valueOf(ListOfNum[i])+".png");
            ListAP[i].getChildren().add(myImageView);
            AnchorPane.setTopAnchor(myImageView, top);
            AnchorPane.setLeftAnchor(myImageView, left);
        }
    }

    // Initialise les AnchorPanes pour le nbr de cartes
    public static AnchorPane[] displayNbrCartes(Player p, StackPane SP){
        AnchorPane[] ListAnchorPane = new AnchorPane[6]; // Crée un tableau de 6 éléments

        for (int i = 0; i < ListAnchorPane.length; i++) {
            AnchorPane anchorPane = new AnchorPane();
            ImageView myImageView = makeImageView("statistiques/0p.png");
            anchorPane.getChildren().add(myImageView);

            if(i%2 == 0){
                AnchorPane.setTopAnchor(myImageView, 15.0*factorl);

                if(!(p instanceof Commandant)){
                    AnchorPane.setLeftAnchor(myImageView, (45.0+ 31*(i))*factorL);

                } else{
                    AnchorPane.setLeftAnchor(myImageView, (28.0+ 31*(i))*factorL);
                }  
            }else{
                AnchorPane.setTopAnchor(myImageView, 45.0*factorl);

                if(!(p instanceof Commandant)){
                    AnchorPane.setLeftAnchor(myImageView, (45.0+ 31*(i-1))*factorL);

                } else{
                    AnchorPane.setLeftAnchor(myImageView, (28.0+ 31*(i-1))*factorL);
                } 
            }
            SP.getChildren().add(anchorPane);
            ListAnchorPane[i] = anchorPane;
        }

        return ListAnchorPane;
    }

    // Change les nbr de cartes de la couleur spécifiée
    public static void updateNbrCartes(Player p, AnchorPane[] ListAP){
        int[] ListOfNum = new int[6];

        if(p instanceof Commandant){
            ListOfNum[0] = p.deckCommerce.size();
            ListOfNum[1] = p.deckCivil.size();
            ListOfNum[2] = p.deckMilitaire.size();
            ListOfNum[3] = p.deckScientifique.size();
            ListOfNum[4] = p.deckRessourceSecondaire.size();
            ListOfNum[5] = p.deckRessourcePrimaire.size();
        } else{
            ListOfNum[0] = p.deckRessourceSecondaire.size();
            ListOfNum[1] = p.deckRessourcePrimaire.size();
            ListOfNum[2] = p.deckMilitaire.size();
            ListOfNum[3] = p.deckScientifique.size();
            ListOfNum[4] = p.deckCommerce.size();
            ListOfNum[5] = p.deckCivil.size();
        }
        for (int i = 0; i < 6; i++) {
            double top = AnchorPane.getTopAnchor(ListAP[i].getChildren().get(0));
            double left = AnchorPane.getLeftAnchor(ListAP[i].getChildren().get(0));
            ListAP[i].getChildren().clear();
            ImageView myImageView = makeImageView("statistiques/"+String.valueOf(ListOfNum[i])+"p.png");
            ListAP[i].getChildren().add(myImageView);
            AnchorPane.setTopAnchor(myImageView, top);
            AnchorPane.setLeftAnchor(myImageView, left);
        }
    }

    // Affiche le simbole scientifique sci 
    public static void addSci(String sci, StackPane SP){

        AnchorPane AP = new AnchorPane();

        ImageView myImageView = makeImageView("statistiques/"+sci+".png");

        AP.getChildren().add(myImageView);

        double top = 0;
        double left = 0;

        switch (sci) {

            case "globe":
                top = 250.0;
                left = 10.0;
                break;
            
            case "cardan":
                top = 280.0;
                left = 10.0;
                break;
            
            case "pendule":
                top = 250.0;
                left = 40.0;
                break;
            
            case "plume":
                top = 275.0;
                left = 40.0;
                break;
                
            case "balance":
                top = 250.0;
                left = 70.0;
                break;
                
            case "mortier":
                top = 275.0;
                left = 70.0;
                break;

            case "roue":
                top = 250.0;
                left = 100.0;
                break;
        
            default:
                break;
        }

        AnchorPane.setTopAnchor(myImageView, top*factorl);
        AnchorPane.setLeftAnchor(myImageView, left*factorL);

        SP.getChildren().add(AP);
    }

    // Affiche les symbols allow
    public static void addAllow(String allow, StackPane SP){
        AnchorPane AP = new AnchorPane();

        ImageView myImageView = makeImageView("statistiques/"+allow+".png");

        AP.getChildren().add(myImageView);

        double top = 170.0;
        double left = 5.0;

        switch (allow) {

            case "masque":
                top = 174.0;
                left = 5.0;
                break;
            
            case "lune":
                top = 191.0;
                left = 5.0;
                break;
            
            case "soleil":
                top = 208.0;
                left = 5.0;
                break;
            
            case "goutte":
                top = 174.0;
                left = 25.0;
                break;
                
            case "temple":
                top = 191.0;
                left = 25.0;
                break;
                
            case "pilier":
                top = 208.0;
                left = 25.0;
                break;

            case "lampe":
                top = 174.0;
                left = 45.0;
                break;

            case "livre":
                top = 191.0;
                left = 45.0;
                break;
            
            case "harpe":
                top = 208.0;
                left = 45.0;
                break;
            
            case "engrenage":
                top = 174.0;
                left = 65.0;
                break;
            
            case "cible":
                top = 191.0;
                left = 65.0;
                break;
                
            case "epee":
                top = 208.0;
                left = 65.0;
                break;
                
            case "fer":
                top = 174.0;
                left = 85.0;
                break;

            case "tour":
                top = 191.0;
                left = 85.0;
                break;

            case "casque":
                top = 208.0;
                left = 85.0;
                break;
                
            case "amphore":
                top = 174.0;
                left = 105.0;
                break;

            case "tonneau":
                top = 191.0;
                left = 105.0;
                break;
        
            default:
                break;
        }

        AnchorPane.setTopAnchor(myImageView, top*factorl);
        AnchorPane.setLeftAnchor(myImageView, (left+5)*factorL);

        SP.getChildren().add(AP);

    }

    // Affiche les merveilles et return les VBox des merveilles
    public static VBox displayWonders(Merveille[] wondersPlayer, Merveille[] wondersCommandant, Pane pane){

        ArrayList<ImageView> merveillesPlayer = new ArrayList<>();
        ArrayList<ImageView> merveillesCommandant = new ArrayList<>();

        for (Merveille merveille : wondersPlayer) {
            merveillesPlayer.add(merveille.image);
        }

        merveillesCommandant.add(makeImageView("merveille/"+wondersCommandant[0].name+".png", wondersCommandant[0].name));
        merveillesCommandant.add(makeImageView("merveille/"+wondersCommandant[1].name+".png", wondersCommandant[1].name));
        merveillesCommandant.add(makeImageView("merveille/vide.png"));
        merveillesCommandant.add(makeImageView("merveille/vide.png"));

        VBox VBcommandantWonders = new VBox();
        VBox VBplayerWonders = new VBox();

        VBcommandantWonders.getChildren().addAll(merveillesCommandant);
        VBplayerWonders.getChildren().addAll(merveillesPlayer);

        VBplayerWonders.setLayoutX(0);
        VBplayerWonders.setLayoutY(312*factorl);

        VBcommandantWonders.setLayoutX(1375*factorL);
        VBcommandantWonders.setLayoutY(312*factorl);

        for (ImageView imageView : merveillesPlayer) {
            FonctionUtile.definedSetMouseEvent(imageView, pane);
        }
        for (ImageView imageView : merveillesCommandant){
            FonctionUtile.definedSetMouseEvent(imageView, pane);
        }

        pane.getChildren().addAll(VBplayerWonders, VBcommandantWonders);

        return VBplayerWonders;
    }

    // Change la merveille donnée en argument en couleur
    public static void WonderColor(String merveille, VBox VB){
        for (Node node : VB.getChildren()) {
            if(node.getId() == merveille){
                Image myimage = new Image("merveille/"+merveille+".png");
                ((ImageView) node).setImage(myimage);
            }
        }
    }

    // Return les 2 VBox pour les cartes nécessaires (player en 0 et commandant en 1)
    public static VBox[] initialiseVBoxCartesReduites(Pane pane){

        VBox[] VBlist = new VBox[2];

        VBlist[0] = new VBox();
        VBlist[1] = new VBox();

        VBlist[0].setLayoutX(225*factorL);
        VBlist[0].setLayoutY(312*factorl);

        VBlist[1].setLayoutX(1239*factorL);
        VBlist[1].setLayoutY(312*factorl);

        pane.getChildren().addAll(VBlist);

        return VBlist;
    }

    // Affiche la carte pour le joueur donné
    public static void displayCarteCommerce(CarteCommerce carte, Player p, VBox[] listVB){

        if(p instanceof Commandant && (listVB[1].getChildren().isEmpty() || !listVB[1].getChildren().contains(carte.carteRedImC))){
            listVB[1].getChildren().add(carte.carteRedImC);
        }else if(!(p instanceof Commandant) && (listVB[0].getChildren().isEmpty() || !listVB[0].getChildren().contains(carte.carteRedImP))){
            listVB[0].getChildren().add(carte.carteRedImP);
        }
    }
    public static void displayCarteGuilde(CarteGuilde carte, Player p, VBox[] listVB){

        if(p instanceof Commandant && (listVB[1].getChildren().isEmpty() || !listVB[1].getChildren().contains(carte.carteRedImC))){
            listVB[1].getChildren().add(carte.carteRedImC);
        }else if((listVB[0].getChildren().isEmpty() || !listVB[0].getChildren().contains(carte.carteRedImP))){
            listVB[0].getChildren().add(carte.carteRedImP);
        }
    }

    // Affiche le pion du plateau militaire
    public static void updatePionMilitaire(PlateauMilitaire plateau, StackPane SP){

        int i = plateau.position - 9;

        plateau.imagePion.setTranslateY(-25.0 * factorl);
        plateau.imagePion.setTranslateX((2.0 + i * 42)*factorL);

        if (!SP.getChildren().contains(plateau.imagePion)){
            SP.getChildren().add(plateau.imagePion);
        }
    }

    // Affiche les jetons à la bonne place
    public static void displayJetons(Jeton[] jetonsList, StackPane SP){

        for (int i = 0; i < jetonsList.length; i++) {
            Jeton jeton = jetonsList[i];
            ImageView im = jeton.image;

            im.setTranslateY(3*factorl);
            im.setTranslateX((-200 + i*100)*factorL);
            if (!SP.getChildren().contains(im)){
                SP.getChildren().add(im);
            }
        }
    }
    
    // Initialise les VBox pour les petits jetons
    public static VBox initialiseVBoxJetons(StackPane SP, Player p){
        VBox VB = new VBox(-10*factorl);

        if (p instanceof Commandant){
            VB.setTranslateX(105*factorL);
        }
        
        SP.getChildren().add(VB);

        return VB;
    }

    // Affiche les jetons sur les stats du joueur
    public static void displaySmallJetons(Jeton jeton, VBox VBplayer){

        VBplayer.getChildren().add(jeton.smallImage);
    }

    // Change toutes les stats en fonction des objects Player et Commandant
    /*
     * A faire: stats ressources, nbr cartes, merveilles (colorier), liste des cartes (VBox),
     *          allows, scientifiques, jetons, plateau militaire
     * 
     * SP: player, commandant, SPjetons, plateauMilitaire
     */
    public static void updateAll(UpdateParameters params){

        Player p = params.p;
        Commandant c = params.c;
        AnchorPane[] ressourcePlayer = params.ressourcePlayer;
        AnchorPane[] ressourceCommandant = params.ressourceCommandant;
        AnchorPane[] nbrCartesPlayer = params.nbrCartesPlayer;
        AnchorPane[] nbrCartesCommandant = params.nbrCartesCommandant;
        VBox VBmerveilles = params.VBmerveilles;
        VBox[] VBcartesreduites = params.VBcartesreduites;
        Jeton[] jetonsList = params.jetonsList;
        PlateauMilitaire PM = params.PM;
        StackPane[] SP = params.SP;

        updateRessources(p, ressourcePlayer);
        updateRessources(c, ressourceCommandant);

        updateNbrCartes(p, nbrCartesPlayer);
        updateNbrCartes(c, nbrCartesCommandant);

        for (Merveille merveille : p.merveillesConstruites) {
            WonderColor(merveille.name, VBmerveilles);
            p.merveillesAConstruire.remove(merveille);
        }

        for (CarteCommerce carte : p.deckCommerce) {
            displayCarteCommerce(carte, p, VBcartesreduites);
        }
        for (CarteCommerce carte : c.deckCommerce) {
            displayCarteCommerce(carte, c, VBcartesreduites);
        }
        for (CarteGuilde carte : p.deckGuilde) {
            displayCarteGuilde(carte, p, VBcartesreduites);
        }
        for (CarteGuilde carte : c.deckGuilde) {
            displayCarteGuilde(carte, c, VBcartesreduites);
        }

        for (String str : p.allows) {
            addAllow(str, SP[0]);
        }
        for (String str : c.allows) {
            addAllow(str, SP[1]);
        }

        for (String str : p.AvanceeScientifique) {
            addSci(str, SP[0]);
        }
        for (String str : c.AvanceeScientifique) {
            addSci(str, SP[1]);
        }

        displayJetons(jetonsList, SP[2]);

        if (p.canTakeJeton){
            FonctionUtile.DisableCards(true);

            for (Jeton jeton : jetonsList) {
                jeton.image.setDisable(false);
                ButtonFunctions.setJetonsAction(jeton, SP[2]);
            }
        }

        if (c.canTakeJeton){

            String sens = CarteDecision.getCartesDecisions().get(CarteDecision.ind).sens;

            Jeton jeton = new Jeton();

            if (sens == "gauche"){
                for (int i = 0; i < jetonsList.length; i++) {
                    if (jetonsList[i].smallImage != null){
                        jeton = jetonsList[i];
                        break;
                    }
                }
            } else {
                for (int i = jetonsList.length-1; i >= 0; i--) {
                    if (jetonsList[i].smallImage != null){
                        jeton = jetonsList[i];
                        break;
                    }
                }
            }

            jeton.PlayJeton(c);
            c.canTakeJeton = false;
            jeton.removeFrom(params.jetonsList, SP[2]);
            Affichage.updateAll(params);

            Affichage.displaySmallJetons(jeton, params.VBjetonsC);
        }
        
        updatePionMilitaire(PM, SP[3]); 
        

        ImageView image = null;
        if (p.win[0]){
            image = makeImageView("fin/victoire militaire.png");
        } else if (p.win[1]){
            image = makeImageView("fin/victoire scientifique.png");
        } else if (c.win[0]){
            image = makeImageView("fin/défaite militaire.png");
        } else if (c.win[1]){
            image = makeImageView("fin/défaite scientifique.png");
        }
        if (image != null){

            Button retourMenu = ButtonFunctions.createButton("boutons/retourmenu.png", 500*factorl, 700*factorL);

            retourMenu.setOnAction(event -> {
                PrincipalPane.getScene().setRoot(PrincipalPane.getFirstPane());
            });
            
            FonctionUtile.makeScaleTransition(image);

            PrincipalPane.getPrincipalPane().getChildren().add(image);
        }
    }
}