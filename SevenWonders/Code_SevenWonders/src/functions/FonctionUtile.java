package functions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cartes.Carte;
import cartes.CarteCivil;
import cartes.CarteFactory;
import cartes.CarteGuilde;
import designPatterns.PrincipalPane;
import designPatterns.Pyramide;
import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import merveilles.Merveille;

import java.awt.Desktop;


public class FonctionUtile {

    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;

// return un élément pris au hasard dans mylist et le retire de la liste
    public static <T> T hasardElement(ArrayList<T> mylist) {
        int index = new Random().nextInt(mylist.size());
        return mylist.remove(index);
    }

// return Un tableau avec en 0 le premier age, en 1 le deuxieme en 2 le troisieme (déjà mélangé)
    public static Carte[][] makeCard(){
        
        // creation de la factory
        CarteFactory fact = new CarteFactory();
        
        ArrayList<Carte> Premierage = new ArrayList<Carte>();
        ArrayList<Carte> DeuxiemeAge = new ArrayList<Carte>();
        ArrayList<Carte> TroisiemeAge = new ArrayList<Carte>();
        ArrayList<Carte> Cartesguilde = new ArrayList<Carte>();

        // Description des ressources
        Ressource rien = new Ressource(0, "bois");

        Ressource simplebois = new Ressource(1, "bois");
        Ressource doublebois = new Ressource(2, "bois");
        Ressource triplebois = new Ressource(3, "bois");

        Ressource simplepierre = new Ressource(1, "pierre");
        Ressource doublepierre = new Ressource(2, "pierre");
        Ressource triplepierre = new Ressource(3, "pierre");

        Ressource simpleargile = new Ressource(1, "argile");
        Ressource doubleargile = new Ressource(2, "argile");
        Ressource tripleargile = new Ressource(3, "argile");

        Ressource simpleverre = new Ressource(1, "verre");
        Ressource doubleverre = new Ressource(2, "verre");

        Ressource simplepapyrus = new Ressource(1, "papyrus");
        Ressource doublepapyrus = new Ressource(2, "papyrus");

        Ressource unepiece = new Ressource(1, "piece");
        Ressource deuxpieces = new Ressource(2, "piece");
        Ressource troispieces = new Ressource(3, "piece");
        Ressource quatrepieces = new Ressource(4, "piece");
        Ressource huitpieces = new Ressource(8, "piece");

        String[] noAllow = new String[2];
        noAllow[0] = "no";
        noAllow[1] = "allow";

        //Premier Age/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ImageView image = Affichage.makeImageView("cartes/Chantier.png");
        Premierage.add(fact.CreateCarteRessource("Chantier", "Brun", simplebois, noAllow, image, "I", rien));
        image = Affichage.makeImageView("cartes/Exploitation.png");
        Premierage.add(fact.CreateCarteRessource("Exploitation", "Brun", simplebois, noAllow, image, "I", unepiece));
        image = Affichage.makeImageView("cartes/Bassin argileux.png");
        Premierage.add(fact.CreateCarteRessource("Bassin argileux", "Brun", simpleargile, noAllow, image, "I", rien));
        image = Affichage.makeImageView("cartes/Cavité.png");
        Premierage.add(fact.CreateCarteRessource("Cavité", "Brun", simpleargile, noAllow, image, "I", unepiece));
        image = Affichage.makeImageView("cartes/Gisement.png");
        Premierage.add(fact.CreateCarteRessource("Gisement", "Brun", simplepierre, noAllow, image, "I", rien));
        image = Affichage.makeImageView("cartes/Mine.png");
        Premierage.add(fact.CreateCarteRessource("Mine", "Brun", simplepierre, noAllow, image, "I", unepiece));
        image = Affichage.makeImageView("cartes/Verrerie.png");
        Premierage.add(fact.CreateCarteRessource("Verrerie", "Gris", simpleverre, noAllow, image, "I", unepiece));
        image = Affichage.makeImageView("cartes/Presse.png");
        Premierage.add(fact.CreateCarteRessource("Presse", "Gris", simplepapyrus, noAllow, image, "I", unepiece));

        image = Affichage.makeImageView("cartes/Théâtre.png");
        Premierage.add(fact.CreateCarteCivil("Théâtre", 3, new String[]{"masque", "allow"}, image, "I", rien));
        image = Affichage.makeImageView("cartes/Autel.png");
        Premierage.add(fact.CreateCarteCivil("Autel", 3, new String[]{"lune", "allow"}, image, "I", rien));
        image = Affichage.makeImageView("cartes/Bains.png");
        Premierage.add(fact.CreateCarteCivil("Bains", 3, new String[]{"goutte", "allow"}, image, "I", simplepierre));

        image = Affichage.makeImageView("cartes/Tour de garde.png");
        Premierage.add(fact.CreateCarteMilitaire("Tour de garde", 1, noAllow, image, "I", rien));
        image = Affichage.makeImageView("cartes/Ecuries.png");
        Premierage.add(fact.CreateCarteMilitaire("Ecuries", 1, new String[]{"fer", "allow"}, image, "I", simplebois));
        image = Affichage.makeImageView("cartes/Caserne.png");
        Premierage.add(fact.CreateCarteMilitaire("Caserne", 1, new String[]{"epee", "allow"}, image, "I", simpleargile));
        image = Affichage.makeImageView("cartes/Palissade.png");
        Premierage.add(fact.CreateCarteMilitaire("Palissade", 1, new String[]{"tour", "allow"}, image, "I", deuxpieces));

        image = Affichage.makeImageView("cartes/Atelier.png");
        Premierage.add(fact.CreateCarteScientifique("Atelier", "pendule", 1, noAllow, image, "I", simplepapyrus));
        image = Affichage.makeImageView("cartes/Apothicaire.png");
        Premierage.add(fact.CreateCarteScientifique("Apothicaire", "roue", 1, noAllow, image, "I", simpleverre));
        image = Affichage.makeImageView("cartes/Scriptorium.png");
        Premierage.add(fact.CreateCarteScientifique("Scriptorium", "plume", 0, new String[]{"livre", "allow"}, image, "I", deuxpieces));
        image = Affichage.makeImageView("cartes/Officine.png");
        Premierage.add(fact.CreateCarteScientifique("Officine", "mortier", 0, new String[]{"engrenage", "allow"}, image, "I", deuxpieces));

        image = Affichage.makeImageView("cartes/Dépôt de pierre.png");
        ImageView carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Dépôt de pierre.png");
        ImageView carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Dépôt de pierrea.png");
        Premierage.add(fact.CreateCarteCommerce("Dépôt de pierre", "pierre", 0, noAllow, image, carteRedImP, carteRedImC, "I", troispieces));
        image = Affichage.makeImageView("cartes/Dépôt d'argile.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Dépôt d argile.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Dépôt d argilea.png");
        Premierage.add(fact.CreateCarteCommerce("Dépôt d'argile", "argile", 0, noAllow, image, carteRedImP, carteRedImC, "I", troispieces));
        image = Affichage.makeImageView("cartes/Dépôt de bois.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Dépôt de bois.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Dépôt de boisa.png");
        Premierage.add(fact.CreateCarteCommerce("Dépôt de bois", "bois", 0, noAllow, image, carteRedImP, carteRedImC, "I", troispieces));
        image = Affichage.makeImageView("cartes/Taverne.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Taverne.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Tavernea.png");
        Premierage.add(fact.CreateCarteCommerce("Taverne", "pieces", 4, new String[]{"amphore", "allow"}, image, carteRedImP, carteRedImC, "I", rien));

        //Deuxieme Age/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        image = Affichage.makeImageView("cartes/Scierie.png");
        DeuxiemeAge.add(fact.CreateCarteRessource("Scierie", "Brun", doublebois, noAllow, image, "II", deuxpieces));
        image = Affichage.makeImageView("cartes/Briquetterie.png");
        DeuxiemeAge.add(fact.CreateCarteRessource("Briquetterie", "Brun", doubleargile, noAllow, image, "II", deuxpieces));
        image = Affichage.makeImageView("cartes/Carrière.png");
        DeuxiemeAge.add(fact.CreateCarteRessource("Carrière", "Brun", doublepierre, noAllow, image, "II", deuxpieces));
        image = Affichage.makeImageView("cartes/Soufflerie.png");
        DeuxiemeAge.add(fact.CreateCarteRessource("Soufflerie", "Gris", simpleverre, noAllow, image, "II", rien));
        image = Affichage.makeImageView("cartes/Séchoir.png");
        DeuxiemeAge.add(fact.CreateCarteRessource("Séchoir", "Gris", simplepapyrus, noAllow, image, "II", rien));

        image = Affichage.makeImageView("cartes/Tribunal.png");
        DeuxiemeAge.add(fact.CreateCarteCivil("Tribunal", 5, noAllow, image, "II", doublebois, simpleverre));
        image = Affichage.makeImageView("cartes/Statue.png");
        DeuxiemeAge.add(fact.CreateCarteCivil("Statue", 4, new String[]{"pilier", "masque"}, image, "II", doubleargile));
        image = Affichage.makeImageView("cartes/Temple.png");
        DeuxiemeAge.add(fact.CreateCarteCivil("Temple", 4, new String[]{"soleil", "lune"}, image, "II", simplebois, simplepapyrus));
        image = Affichage.makeImageView("cartes/Aqueduc.png");
        DeuxiemeAge.add(fact.CreateCarteCivil("Aqueduc", 5, new String[]{"no", "goutte"}, image, "II", triplepierre));
        image = Affichage.makeImageView("cartes/Rostres.png");
        DeuxiemeAge.add(fact.CreateCarteCivil("Rostres", 4, new String[]{"temple", "allow"}, image, "II", simplebois, simplepierre));

        image = Affichage.makeImageView("cartes/Muraille.png");
        DeuxiemeAge.add(fact.CreateCarteMilitaire("Muraille", 2, noAllow, image, "II", doublepierre));
        image = Affichage.makeImageView("cartes/Haras.png");
        DeuxiemeAge.add(fact.CreateCarteMilitaire("Haras", 1, new String[]{"no", "fer"}, image, "II", simpleargile, simplebois));
        image = Affichage.makeImageView("cartes/Baraquements.png");
        DeuxiemeAge.add(fact.CreateCarteMilitaire("Baraquements", 1, new String[]{"no", "epee"}, image, "II", troispieces));
        image = Affichage.makeImageView("cartes/Champ de tir.png");
        DeuxiemeAge.add(fact.CreateCarteMilitaire("Champ de tir", 2, new String[]{"cible", "allow"}, image, "II", simplepierre, simplebois, simplepapyrus));
        image = Affichage.makeImageView("cartes/Place d'armes.png");
        DeuxiemeAge.add(fact.CreateCarteMilitaire("Place d'armes", 2, new String[]{"casque", "allow"}, image, "II", doubleargile, simpleverre));

        image = Affichage.makeImageView("cartes/Bibliothèque.png");
        DeuxiemeAge.add(fact.CreateCarteScientifique("Bibliothèque", "plume", 2, new String[]{"no", "livre"}, image, "II", simplepierre, simplebois, simpleverre));
        image = Affichage.makeImageView("cartes/Dispensaire.png");
        DeuxiemeAge.add(fact.CreateCarteScientifique("Dispensaire", "mortier", 2, new String[]{"no", "engrenage"}, image, "II", simplepierre, doubleargile));
        image = Affichage.makeImageView("cartes/Ecole.png");
        DeuxiemeAge.add(fact.CreateCarteScientifique("Ecole", "roue", 1, new String[]{"harpe", "allow"}, image, "II", simplebois, doublepapyrus));
        image = Affichage.makeImageView("cartes/Laboratoire.png");
        DeuxiemeAge.add(fact.CreateCarteScientifique("Laboratoire", "pendule", 1, new String[]{"lampe", "allow"}, image, "II", simplebois, doubleverre));

        image = Affichage.makeImageView("cartes/Forum.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Forum.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Foruma.png");
        DeuxiemeAge.add(fact.CreateCarteCommerce("Forum", "secondaire", 0, noAllow, image, carteRedImP, carteRedImC, "II", troispieces, simpleargile));
        image = Affichage.makeImageView("cartes/Caravansérail.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Caravansérail.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Caravanséraila.png");
        DeuxiemeAge.add(fact.CreateCarteCommerce("Caravansérail", "primaire", 0, noAllow, image, carteRedImP, carteRedImC, "II", deuxpieces, simpleverre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Douanes.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Douanes.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Douanesa.png");
        DeuxiemeAge.add(fact.CreateCarteCommerce("Douanes", "reductionSecondaire", 0, noAllow, image, carteRedImP, carteRedImC, "II", quatrepieces));
        image = Affichage.makeImageView("cartes/Brasserie.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Brasserie.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Brasseriea.png");
        DeuxiemeAge.add(fact.CreateCarteCommerce("Brasserie", "pieces", 6, new String[]{"tonneau", "allow"}, image, carteRedImP, carteRedImC, "II", rien));

        //Troisieme Age/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        image = Affichage.makeImageView("cartes/Arsenal.png");
        TroisiemeAge.add(fact.CreateCarteMilitaire("Arsenal", 3, noAllow, image, "III", tripleargile, doublebois));
        image = Affichage.makeImageView("cartes/Prétoire.png");
        TroisiemeAge.add(fact.CreateCarteMilitaire("Prétoire", 3, noAllow, image, "III", huitpieces));
        image = Affichage.makeImageView("cartes/Fortifications.png");
        TroisiemeAge.add(fact.CreateCarteMilitaire("Fortifications", 2, new String[]{"no", "tour"}, image, "III", simpleargile, doublepierre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Atelier de siège.png");
        TroisiemeAge.add(fact.CreateCarteMilitaire("Atelier de siège", 2, new String[]{"no", "cible"}, image, "III", triplebois, simpleverre));
        image = Affichage.makeImageView("cartes/Cirque.png");
        TroisiemeAge.add(fact.CreateCarteMilitaire("Cirque", 2, new String[]{"no", "casque"}, image, "III", doubleargile, doublepierre));

        image = Affichage.makeImageView("cartes/Palace.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Palace", 7, noAllow, image, "III", simplebois, simpleargile, simplepierre, doubleverre));
        image = Affichage.makeImageView("cartes/Hotel de ville.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Hotel de ville", 7, noAllow, image, "III", doublebois, triplepierre));
        image = Affichage.makeImageView("cartes/Obélisque.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Obélisque", 5, noAllow, image, "III", doublepierre, simpleverre));
        image = Affichage.makeImageView("cartes/Jardins.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Jardins", 6, new String[]{"no", "pilier"}, image, "III", doubleargile, doublebois));
        image = Affichage.makeImageView("cartes/Panthéon.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Panthéon", 6, new String[]{"no", "soleil"}, image, "III", simpleargile, simplebois, doublepapyrus));
        image = Affichage.makeImageView("cartes/Sénat.png");
        TroisiemeAge.add(fact.CreateCarteCivil("Sénat", 5, new String[]{"no", "temple"}, image, "III", doubleargile, simplepierre, simplepapyrus));

        image = Affichage.makeImageView("cartes/Académie.png");
        TroisiemeAge.add(fact.CreateCarteScientifique("Académie", "cardan", 3, noAllow, image, "III", simplepierre, simplebois, doubleverre));
        image = Affichage.makeImageView("cartes/Etude.png");
        TroisiemeAge.add(fact.CreateCarteScientifique("Etude", "cardan", 3, noAllow, image, "III", simplepapyrus, doublebois, simpleverre));
        image = Affichage.makeImageView("cartes/Université.png");
        TroisiemeAge.add(fact.CreateCarteScientifique("Université", "globe", 2, new String[]{"no", "harpe"}, image, "III", simpleargile, simpleverre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Observatoire.png");
        TroisiemeAge.add(fact.CreateCarteScientifique("Observatoire", "globe", 2, new String[]{"no", "lampe"}, image, "III", simplepierre, doublepapyrus));

        image = Affichage.makeImageView("cartes/Chambre de commerce.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Chambre de commerce.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Chambre de commercea.png");
        TroisiemeAge.add(fact.CreateCarteCommerce("Chambre de commerce", "Gris", 3, noAllow, image, carteRedImP, carteRedImC, "III", doublepapyrus));
        image = Affichage.makeImageView("cartes/Port.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Port.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Porta.png");
        TroisiemeAge.add(fact.CreateCarteCommerce("Port", "Brun", 2, noAllow, image, carteRedImP, carteRedImC, "III", simplebois, simpleverre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Armurerie.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Armurerie.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Armureriea.png");
        TroisiemeAge.add(fact.CreateCarteCommerce("Armurerie", "Rouge", 1, noAllow, image, carteRedImP, carteRedImC, "III", doublepierre, simpleverre));
        image = Affichage.makeImageView("cartes/Phare.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Phare.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Pharea.png");
        TroisiemeAge.add(fact.CreateCarteCommerce("Phare", "Jaune", 1, new String[]{"no", "amphore"}, image, carteRedImP, carteRedImC, "III", doubleargile, simpleverre));
        image = Affichage.makeImageView("cartes/Arène.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/jaune/Arène.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/jaune/Arènea.png");
        TroisiemeAge.add(fact.CreateCarteCommerce("Arène", "Merveille", 2, new String[]{"no", "tonneau"}, image, carteRedImP, carteRedImC, "III", simpleargile, simplepierre, simplebois));

        //Cartes Guilde/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        image = Affichage.makeImageView("cartes/Guilde des commercants.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des commercants.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des commercantsa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des commercants", "Jaune", noAllow, image, carteRedImP, carteRedImC, "III", simpleargile, simplebois, simpleverre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Guilde des armateurs.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des armateurs.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des armateursa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des armateurs", "GrisBrun", noAllow, image, carteRedImP, carteRedImC, "III", simpleargile, simplepierre, simpleverre, simplepapyrus));
        image = Affichage.makeImageView("cartes/Guilde des batisseurs.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des batisseurs.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des batisseursa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des batisseurs", "Merveille", noAllow, image, carteRedImP, carteRedImC, "III", simpleargile, doublepierre, simpleverre, simplebois));
        image = Affichage.makeImageView("cartes/Guilde des magistrats.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des magistrats.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des magistratsa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des magistrats", "Bleu", noAllow, image, carteRedImP, carteRedImC, "III", simpleargile, doublebois, simplepapyrus));
        image = Affichage.makeImageView("cartes/Guilde des scientifiques.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des scientifiques.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des scientifiquesa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des scientifiques", "Vert", noAllow, image, carteRedImP, carteRedImC, "III", doubleargile, doublebois));
        image = Affichage.makeImageView("cartes/Guilde des usuriers.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des usuriers.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des usuriersa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des usuriers", "Piece", noAllow, image, carteRedImP, carteRedImC, "III", doublepierre, doublebois));
        image = Affichage.makeImageView("cartes/Guilde des tacticiens.png");
        carteRedImP = Affichage.makeImageView("cartes_reduites/mauve/Guilde des tacticiens.png");
        carteRedImC = Affichage.makeImageView("cartes_reduites/mauve/Guilde des tacticiensa.png");
        Cartesguilde.add(fact.CreateCarteGuilde("Guilde des tacticiens", "Rouge", noAllow, image, carteRedImP, carteRedImC, "III", simpleargile, doublepierre, simplepapyrus));

        Carte[] premierAge = new Carte[20];
        Carte[] deuxiemeAge = new Carte[20];
        ArrayList<Carte> troisiemeage = new ArrayList<>();
        Carte[] troisiemeAge = new Carte[20];
        
        for (int i = 0; i < 20; i++) {
            premierAge[i] = hasardElement(Premierage);
            deuxiemeAge[i] = hasardElement(DeuxiemeAge);
            
            if(i < 17){
                troisiemeage.add(hasardElement(TroisiemeAge));
            } else{
                troisiemeage.add(hasardElement(Cartesguilde));
            }
        }

        for (int i = 0; i < 20; i++) {
            troisiemeAge[i] = hasardElement(troisiemeage);
        }

        return new Carte[][]{premierAge, deuxiemeAge, troisiemeAge};
    }

// définit l'action d'agrandissement d'image
    public static void agrandir(ImageView imageView, Pane pane){
        
        AnchorPane AP = new AnchorPane();

        ImageView VG = Affichage.makeImageView("merveille/"+imageView.getId()+"VG.png");

        AP.setMouseTransparent(true);

        double layoutXInScene = imageView.localToScene(imageView.getBoundsInLocal()).getMinX();

        if(layoutXInScene > 800.0){
            AnchorPane.setLeftAnchor(VG, layoutXInScene-277);
            AnchorPane.setTopAnchor(VG, imageView.getLayoutY()+134);
        } else{
            AnchorPane.setLeftAnchor(VG, layoutXInScene);
            AnchorPane.setTopAnchor(VG, imageView.getLayoutY()+134);
        }

        AP.setId("IDAnchorMerveilleAgrandie");

        AP.getChildren().add(VG);
        pane.getChildren().add(AP);

    }

// définit l'action de supresssion d'image (de l'anchor créée avec la fonction au dessus)
    public static void suprimer(Pane pane){
        for (Node node : pane.getChildren()) {
            if(node.getId() =="IDAnchorMerveilleAgrandie"){
                pane.getChildren().remove(node);
                break;
            }
        }
    }

// définit les actions à faire pour les events "Entered" et "Exited" de la souris sur imageView
    public static void definedSetMouseEvent(ImageView imageView, Pane pane){
        imageView.setOnMouseEntered((MouseEvent event) -> {
            
            try{
                TimeUnit.SECONDS.sleep(1);
                
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            
            FonctionUtile.agrandir(imageView, pane);
        });
        imageView.setOnMouseExited((MouseEvent event) -> {
            FonctionUtile.suprimer(pane);
        });
    }

// rend les cartes non cliquables
    public static void DisableCards(boolean bool){

        Pyramide FirstAgePyramide = Pyramide.getFirstAgePyramide();
        Pyramide SecondAgePyramide = Pyramide.getSecondAgePyramide();
        Pyramide ThirdAgePyramide = Pyramide.getThirdAgePyramide();

        for (int i = 0; i < FirstAgePyramide.matrix.size(); i++) {
            for (int j = 0; j < FirstAgePyramide.matrix.get(i).size(); j++) {
                FirstAgePyramide.get(i, j).image.setDisable(bool);
            }
        }

        for (int i = 0; i < SecondAgePyramide.matrix.size(); i++) {
            for (int j = 0; j < SecondAgePyramide.matrix.get(i).size(); j++) {
                SecondAgePyramide.get(i, j).image.setDisable(bool);
            }
        }
    
        for (int i = 0; i < ThirdAgePyramide.matrix.size(); i++) {
            for (int j = 0; j < ThirdAgePyramide.matrix.get(i).size(); j++) {
                ThirdAgePyramide.get(i, j).image.setDisable(bool);
            }
        }
    }

// enlève la carte spécifiée
    public static void removecard(Carte carte){

        carte.image.setImage(new Image("file:///"+"cartes/existepas.png"));

        Pyramide pyramide = Pyramide.getCurrentAgePyramide();

        carte.age = "";

        Pane pane = PrincipalPane.getPrincipalPane();
        Pyramide.printpyramide(pane, pyramide);
    }

// définit les actions à faire pour l'event "Clicked" de la souris sur la carte donnée
    public static void ImageViewClicked(Carte carte){

        Carte bottom_left = carte.bottom_left;
        Carte bottom_right = carte.bottom_right;

        Player player = Player.getPlayer();
        for (Merveille merveille : player.merveillesAConstruire) {
                merveille.image.setDisable(false);
            } 

        if((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){

            ButtonFunctions.createCardStage(carte.name, carte);
        }
    }


    public static void updatePointsAndDecideWinner (){

        Player player = Player.getPlayer();
        Commandant comm = Commandant.getCommandant();
        PlateauMilitaire plateau = PlateauMilitaire.getPlateauMilitaire();

        // points des cartes guilde
        CarteGuilde.countPoints(player, comm);
        CarteGuilde.countPoints(comm, player);

        // points pour le nombre de pieces
        player.points += player.nbr_pieces/3;
        comm.points += comm.nbr_pieces/3;

        // points pour l'avancée militaire
        if(plateau.position > 0 && plateau.position < 4){
            comm.points += 10;
        } else if(plateau.position > 3 && plateau.position < 7){
            comm.points += 5;
        } else if(plateau.position > 6 && plateau.position < 9){
            comm.points += 2;
        } else if(plateau.position > 9 && plateau.position < 12){
            player.points += 2;
        } else if(plateau.position > 11 && plateau.position < 15){
            player.points += 5;
        } else if(plateau.position > 14 && plateau.position < 18){
            player.points += 10;
        }
        
        ImageView image = null;
        Button retourMenu = ButtonFunctions.createButton("boutons/retourmenu.png", 500*factorl, 687.5*factorL);

        retourMenu.setOnAction(event -> {
            PrincipalPane.getScene().setRoot(PrincipalPane.getFirstPane());
        });

        if (player.points > comm.points|| 
           (comm.points == player.points && CarteCivil.countPoint(player) > CarteCivil.countPoint(comm))){

            image = Affichage.makeImageView("fin/victoire civil.png");

        } else if (comm.points > player.points || 
                  (comm.points == player.points && CarteCivil.countPoint(comm) > CarteCivil.countPoint(player))){

            image = Affichage.makeImageView("fin/défaite civil.png");
            
        } else {
            image = Affichage.makeImageView("fin/égalité civil.png");
        }

        makeScaleTransition(image);
        
        PrincipalPane.getPrincipalPane().getChildren().add(image);
    }


    public static void makeScaleTransition (ImageView image){

        image.setScaleX(0.1);
        image.setScaleY(0.1);

        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(2)); // Duration of the animation
        scaleTransition.setNode(image);
        scaleTransition.setFromX(0.1);
        scaleTransition.setFromY(0.1);
        scaleTransition.setToX(1*factorL);
        scaleTransition.setToY(1*factorl);
        image.setLayoutX(465*factorL);
        image.setLayoutY(92.5*factorl);
        scaleTransition.play();
    }


    public static void setScaleImage(ImageView image, double factorL, double factorl){

        double x = image.getBoundsInParent().getWidth()*factorL;
        double y = image.getBoundsInParent().getHeight()*factorl;

        image.setFitWidth(x);
        image.setFitHeight(y);

        image.setPreserveRatio(false);
    }


    public static void openPDF(File file){
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer l'erreur d'ouverture du fichier PDF
            }
        } else {
            System.out.println("Desktop n'est pas supporté sur cette plateforme.");
        }
    }
}