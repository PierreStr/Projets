package functions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import cartes.Carte;
import designPatterns.PrincipalPane;
import designPatterns.Pyramide;
import designPatterns.SingletonPrymaryStage;
import designPatterns.UpdateParameters;
import items.CarteDecision;
import items.Commandant;
import items.Defausse;
import items.PlateauMilitaire;
import items.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import jetons.*;
import merveilles.*;

public class Lancement {
        
    public static final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    public static final double L = screenBounds.getWidth();
    public static final double l = screenBounds.getHeight();
    static double factorL = L/1600;
    static double factorl = l/900;


    public static Pane ChoisirCommandants (){

        // POUR APRES /////////////////////////////////////////////////////////
        CircusMaximus circusMaximus = new CircusMaximus();
        LaGrandeBibliotheque laGrandeBibliotheque = new LaGrandeBibliotheque();
        LaStatueZeus laStatueZeus = new LaStatueZeus();
        LaViaAppia laViaAppia = new LaViaAppia();
        LeColosse leColosse = new LeColosse();
        LeMausolee leMausolee = new LeMausolee();
        LePiree lePiree = new LePiree();
        LesJardinsSuspendus lesJardinsSuspendus = new LesJardinsSuspendus();
        LeSphinx leSphinx = new LeSphinx();
        LesPyramides lesPyramides = new LesPyramides();
        LeTempleArtemis leTempleArtemis = new LeTempleArtemis();

        ArrayList<Merveille> merveilleList = new ArrayList<>(Arrays.asList(
            circusMaximus, laGrandeBibliotheque, laStatueZeus, laViaAppia, leColosse, leMausolee, lePiree, 
            lesJardinsSuspendus, leSphinx, lesPyramides, leTempleArtemis
        ));

        Player p = new Player();
        Player.setPlayer(p);
        // POUR APRES /////////////////////////////////////////////////////////

        Stage stage = SingletonPrymaryStage.getPrymaryStage();

        Pane pane = new Pane();

        HBox hbbutton = new HBox(100);
        hbbutton.layoutXProperty().bind(pane.widthProperty().subtract(hbbutton.widthProperty()).divide(2));
        hbbutton.layoutYProperty().bind(pane.heightProperty().subtract(hbbutton.heightProperty()).divide(2).add(320*factorl));
        hbbutton.setAlignment(Pos.CENTER);

        Button confirm = new Button();
        Image image = new Image("file:src/resources/images/boutons/confirmer.png");
        ImageView im = new ImageView(image);
        FonctionUtile.setScaleImage(im, factorL, factorl);
        confirm.setGraphic(im);
        confirm.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        Image checkedImage = new Image("file:src/resources/images/boutons/checkbox.png");
        Image uncheckedImage = new Image("file:src/resources/images/boutons/uncheckbox.png");

        // Créer une ImageView qui affichera les images
        ImageView imageView = new ImageView(uncheckedImage);
        FonctionUtile.setScaleImage(imageView, factorL, factorl);

        Button Checkbutton = new Button();
        Checkbutton.setGraphic(imageView);
        Checkbutton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        Checkbutton.setLayoutX(100);
        Checkbutton.setLayoutY(740);

        Checkbutton.setOnAction(event -> {
            if (imageView.getImage() == uncheckedImage){
                imageView.setImage(checkedImage);
                p.afficheNextAction = true;
            } else {
                imageView.setImage(uncheckedImage);
                p.afficheNextAction = false;
            }
        });

        hbbutton.getChildren().addAll(Checkbutton, confirm);

        InputStream input = Affichage.class.getResourceAsStream("/images/fondChoixCommandants.png");

        Image newImage = new Image(input);
        ImageView backgroundImageView = new ImageView(newImage);

        backgroundImageView.fitWidthProperty().bind(stage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stage.heightProperty());
        pane.getChildren().add(backgroundImageView);

        HBox hb = new HBox(16*factorL);
        hb.layoutXProperty().bind(pane.widthProperty().subtract(hb.widthProperty()).divide(2));
        hb.layoutYProperty().bind(pane.heightProperty().subtract(hb.heightProperty()).divide(2).add(50*factorl));



        Button[] buttonList = new Button[]{new Button(), new Button(), new Button(), new Button(), new Button()};

        Button aristoteButton = buttonList[0];
        aristoteButton.setGraphic(Affichage.makeImageView("commandants/aristotef.png"));
        aristoteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        aristoteButton.setOnAction(e -> {

            for (Button button : buttonList) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            aristoteButton.setStyle("-fx-background-color: brown;");

            confirm.setOnAction(event -> {
                Commandant c = new Commandant("aristote", "Gris", "rond", new JetonMathematique(), new JetonPhilosophie());
                Commandant.setCommandant(c);
                CarteDecision.setCartesDecisions(CarteDecision.shuffle(CarteDecision.makeCards()));
                try {
                        PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, new ArrayList<Merveille>(), new Merveille[]{null, null}, p));
                } catch (Exception e1) {
                    System.err.println("on s'en fiche");
                }
            });
        });

        Button bilkisButton = buttonList[1];
        bilkisButton.setGraphic(Affichage.makeImageView("commandants/bilkisf.png"));
        bilkisButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        bilkisButton.setOnAction(e -> {

            for (Button button : buttonList) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            bilkisButton.setStyle("-fx-background-color: brown;");

            confirm.setOnAction(event -> {
                Commandant c = new Commandant("bilkis", "Brun", "tout", new JetonEconomie());
                Commandant.setCommandant(c);
                CarteDecision.setCartesDecisions(CarteDecision.shuffle(CarteDecision.makeCards()));
                try {
                    PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, new ArrayList<Merveille>(), new Merveille[]{null, null}, p));
                } catch (Exception e1) {
                    System.err.println("on s'en fiche");
                }
            });
        });

        Button cesarButton = buttonList[2];
        cesarButton.setGraphic(Affichage.makeImageView("commandants/césarf.png"));
        cesarButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        cesarButton.setOnAction(e -> {

            for (Button button : buttonList) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            cesarButton.setStyle("-fx-background-color: brown;");

            confirm.setOnAction(event -> {

                Commandant c = new Commandant("cesar", "Mauve", "rien", new JetonStrategie());
                Commandant.setCommandant(c);
                CarteDecision.setCartesDecisions(CarteDecision.shuffle(CarteDecision.makeCards()));
                try {
                    PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, new ArrayList<Merveille>(), new Merveille[]{null, null}, p));
                } catch (Exception e1) {
                    System.err.println("on s'en fiche");
                }
            });
        });

        Button cleopatreButton = buttonList[3];
        cleopatreButton.setGraphic(Affichage.makeImageView("commandants/cléopatref.png"));
        cleopatreButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        cleopatreButton.setOnAction(e -> {

            for (Button button : buttonList) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            cleopatreButton.setStyle("-fx-background-color: brown;");

            confirm.setOnAction(event -> {
                Commandant c = new Commandant("cleopatre", "Bleu", "triangle", new JetonPhilosophie(), new JetonAgriculture());
                Commandant.setCommandant(c);
                CarteDecision.setCartesDecisions(CarteDecision.shuffle(CarteDecision.makeCards()));
                try {
                    PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, new ArrayList<Merveille>(), new Merveille[]{null, null}, p));
                } catch (Exception e1) {
                    System.err.println("on s'en fiche");
                }
            });
         });

        Button hammurabiButton = buttonList[4];
        hammurabiButton.setGraphic(Affichage.makeImageView("commandants/hammurabif.png"));
        hammurabiButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        hammurabiButton.setOnAction(e -> {

            for (Button button : buttonList) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            hammurabiButton.setStyle("-fx-background-color: brown;");

            confirm.setOnAction(event -> {

                Commandant c = new Commandant("hammurabi", "jaune", "rond", new JetonEconomie());
                Commandant.setCommandant(c);
                c.points += 5;
                CarteDecision.setCartesDecisions(CarteDecision.shuffle(CarteDecision.makeCards()));
                try {
                    PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, new ArrayList<Merveille>(), new Merveille[]{null, null}, p));
                } catch (Exception e1) {
                    System.err.println("on s'en fiche");
                }
            });
        });

        hb.getChildren().addAll(aristoteButton, bilkisButton, cesarButton, cleopatreButton, hammurabiButton);
        pane.getChildren().addAll(hb, hbbutton);
        
        return pane;
    }


    public static Pane ChoisirMerveilles (ArrayList<Merveille> merveilleList, ArrayList<Merveille> merveillesPlayer, 
                                           Merveille[] merveillesCommandant, Player p){

        Stage stage = SingletonPrymaryStage.getPrymaryStage();

        Pane pane = new Pane();

        InputStream input = Affichage.class.getResourceAsStream("/images/fondChoixMerveilles.png");
        Image newImage = new Image(input);
        ImageView backgroundImageView = new ImageView(newImage);

        backgroundImageView.fitWidthProperty().bind(stage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stage.heightProperty());
        pane.getChildren().add(backgroundImageView);

        HBox hb = new HBox(5*factorL);
        hb.layoutXProperty().bind(pane.widthProperty().subtract(hb.widthProperty()).divide(2));
        hb.layoutYProperty().bind(pane.heightProperty().subtract(hb.heightProperty()).divide(2).add(60*factorl));

        Button confirm = new Button();
        Image image = new Image("file:src/resources/images/boutons/confirmer.png");
        ImageView im = new ImageView(image);
        confirm.setGraphic(im);
        confirm.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        confirm.setLayoutX(700*factorL);
        confirm.setLayoutY(730*factorl);

        Merveille[] listTempoMervPlayer = new Merveille[2];

        Merveille[] firstListMerveilles = new Merveille[3];
        Button[] firstButtonsList = new Button[3];

        for (int i = 0; i < 3; i++) {
            firstListMerveilles[i] = FonctionUtile.hasardElement(merveilleList);
            firstButtonsList[i] = new Button();
        }

        for (int i = 0; i < 3; i++) {
            final int tempo = i;
            Button button = firstButtonsList[tempo];
            String chemin = "merveille/" +firstListMerveilles[tempo].name + "VG.png";
            Merveille thisMerv = firstListMerveilles[tempo];

            button.setGraphic(Affichage.makeImageView(chemin, thisMerv.name));
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            button.setOnAction(e -> {
                
                button.setStyle("-fx-background-color: brown;");
                
                if (listTempoMervPlayer[0] == null){
                    listTempoMervPlayer[0] = thisMerv;

                } else if (listTempoMervPlayer[1] == null && listTempoMervPlayer[0] != thisMerv){
                    listTempoMervPlayer[1] = thisMerv;

                    if (p.merveillesAConstruire.size() < 2){
                        confirm.setOnAction(event -> {

                            p.merveillesAConstruire.add(listTempoMervPlayer[0]);
                            merveillesPlayer.add(listTempoMervPlayer[0]);
                            p.merveillesAConstruire.add(listTempoMervPlayer[1]);
                            merveillesPlayer.add(listTempoMervPlayer[1]);

                            for (Merveille merv : firstListMerveilles) {
                                if (! merveillesPlayer.contains(merv)){
                                    merveillesCommandant[0] = merv;
                                }
                            }

                            PrincipalPane.getScene().setRoot(ChoisirMerveilles(merveilleList, merveillesPlayer, merveillesCommandant, p));
                        });
                    } else {
                        confirm.setOnAction(event -> {

                            p.merveillesAConstruire.add(listTempoMervPlayer[0]);
                            merveillesPlayer.add(listTempoMervPlayer[0]);
                            p.merveillesAConstruire.add(listTempoMervPlayer[1]);
                            merveillesPlayer.add(listTempoMervPlayer[1]);

                            for (Merveille merv : firstListMerveilles) {
                                if (! merveillesPlayer.contains(merv)){
                                    merveillesCommandant[1] = merv;
                                }
                            }

                            PrincipalPane.getScene().setRoot(lancerLeJeu(merveillesPlayer, merveillesCommandant));
                        });
                    }

                } else if (listTempoMervPlayer[0] != thisMerv && listTempoMervPlayer[1] != thisMerv){
                    Merveille tempoMerveille = listTempoMervPlayer[0];
                    listTempoMervPlayer[0] = listTempoMervPlayer[1];
                    listTempoMervPlayer[1] = thisMerv;

                    for (Button butt : firstButtonsList) {
                        if (butt.getGraphic().getId() == tempoMerveille.name){
                            butt.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                        }
                    }
                }
            });

            hb.getChildren().add(button);
        }

        pane.getChildren().addAll(hb, confirm);

        return pane;
    }



    public static Pane lancerLeJeu (ArrayList<Merveille> merveillesPlayer, Merveille[] listmerveillesCommandant){

        Merveille[] listmerveillesPlayer = new Merveille[4];
        for (int i = 0; i < 4; i++) {
            listmerveillesPlayer[i] = merveillesPlayer.get(i);
        }

        // ---------------
        // Pane principale
        // ---------------
        Pane principalPane = new Pane();
        PrincipalPane.setPrincipalPane(principalPane);


        // -------------------------------
        // Initialisation des items du jeu
        // -------------------------------
        PlateauMilitaire plateau = new PlateauMilitaire();
        PlateauMilitaire.setPlateauMilitaire(plateau);

        Player p = Player.getPlayer();

        Commandant c = Commandant.getCommandant();

        Defausse defausse = new Defausse();
        Defausse.setDefausse(defausse);


        // ---------------------------
        // Affichage du plateau de jeu
        // ---------------------------
        StackPane[] boards = Affichage.makeBoardGame(c.name, principalPane, SingletonPrymaryStage.getPrymaryStage());  


        // --------------------------
        // Affichage des statistiques
        // --------------------------
        AnchorPane[] APressourcesPlayer = Affichage.displayRessources(p, boards[0]); 
        AnchorPane[] APressourcesCommandant = Affichage.displayRessources(c, boards[1]);  
        AnchorPane[] APnbrCartesPlayer = Affichage.displayNbrCartes(p, boards[5]);         
        AnchorPane[] APnbrCartesCommandant = Affichage.displayNbrCartes(c, boards[6]);    


        // --------------------
        // Affichage des jetons
        // --------------------

        // initialisation de tous les jetons
        JetonLoi jetonLoi = new JetonLoi();
        JetonEconomie jetonEconomie = new JetonEconomie();
        JetonStrategie jetonStrategie = new JetonStrategie();
        JetonTheologie jetonTheologie = new JetonTheologie();
        JetonUrbanisme jetonUrbanisme = new JetonUrbanisme();
        JetonMaconnerie jetonMaconnerie = new JetonMaconnerie();
        JetonAgriculture jetonAgriculture = new JetonAgriculture();
        JetonPhilosophie jetonPhilosophie = new JetonPhilosophie();
        JetonArchitecture jetonArchitecture = new JetonArchitecture();
        JetonMathematique jetonMathematique = new JetonMathematique();

        VBox VBjetonsPlayer = Affichage.initialiseVBoxJetons(boards[0], p);
        VBox VBjetonsCommandant = Affichage.initialiseVBoxJetons(boards[2], c);
        
        ArrayList<Jeton> jetonsListComplete = new ArrayList<>(Arrays.asList(
            jetonAgriculture, jetonArchitecture, jetonEconomie, jetonLoi,
            jetonMaconnerie, jetonMathematique, jetonPhilosophie, jetonStrategie,
            jetonTheologie, jetonUrbanisme
        ));

        for (Jeton jeton : c.jetonsdonnes) {
            Affichage.displaySmallJetons(jeton, VBjetonsCommandant);
            for (Jeton myjeton : jetonsListComplete) {
                if (myjeton.getClass() == jeton.getClass()){
                    jetonsListComplete.remove(myjeton);
                    break;
                }
            }
            jeton.PlayJeton(c);
        }

        Jeton[] jetonsList = new Jeton[5];
        for (int i = 0; i < 5; i++) {
            jetonsList[i] = FonctionUtile.hasardElement(jetonsListComplete);
        }
        ArrayList<Jeton> jetonsRestants = jetonsListComplete;
        Jeton.setJetons(jetonsList);

        Affichage.displayJetons(jetonsList, boards[4]);


        // ------------------------------------------------
        // Initialisation des VBox pour les cartes réduites
        // ------------------------------------------------
        VBox[] VBoxCartesReduites = Affichage.initialiseVBoxCartesReduites(principalPane);


        // -------------------------
        // Affiche le pion militaire
        // -------------------------
        Affichage.updatePionMilitaire(plateau, boards[3]);


        // ------------------------ 
        // affichage des merveilles 
        // ------------------------ 

        for (int i = 0; i < listmerveillesCommandant.length; i++) {
            listmerveillesCommandant[i].playMerveille(c, p, plateau);
        }

        // Création des listes pour stocker les images et les transitions associées
        VBox VBmerveilles = Affichage.displayWonders(listmerveillesPlayer, listmerveillesCommandant, principalPane);

        
        // --------------------
        // Mise à jour générale
        // --------------------
        StackPane[] SP = new StackPane[]{boards[0], boards[2], boards[4], boards[3]};

        UpdateParameters params = new UpdateParameters(p, c, APressourcesPlayer, APressourcesCommandant, 
        APnbrCartesPlayer, APnbrCartesCommandant, VBmerveilles, VBoxCartesReduites, jetonsList, VBjetonsPlayer, 
        VBjetonsCommandant, jetonsRestants, plateau, SP);

        UpdateParameters.setParameters(params);
        Affichage.updateAll(params);


        // ---------------------------------------
        // Création des cartes et lancement du jeu
        // ---------------------------------------
        Carte[][] listCartesAges = FonctionUtile.makeCard();
        Carte[] firstAge = listCartesAges[0];
        Pyramide myfirstagepyramide = Pyramide.FirstAgePyramide(firstAge);
        Pyramide.SecondAgePyramide(listCartesAges[1]);
        Pyramide.ThirdAgePyramide(listCartesAges[2]);
        Pyramide.setCurrentAgePyramide(myfirstagepyramide);

        myfirstagepyramide.printPyramide(principalPane);

        Button retourMenu = ButtonFunctions.createButton("boutons/retourmenu.png", 500*factorl, 700*factorL);

        retourMenu.setOnAction(event -> {
            PrincipalPane.getScene().setRoot(PrincipalPane.getFirstPane());
        });

        FonctionUtile.DisableCards(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> Commandant.play());
        pause.play();

        // -----------------------------------------------------
        // Définition de la scène et fonctions de "mise en page"
        // -----------------------------------------------------
        return principalPane;
    }
}
