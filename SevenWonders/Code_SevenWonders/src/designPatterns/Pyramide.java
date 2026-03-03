package designPatterns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import cartes.Carte;
import functions.Affichage;
import functions.Lancement;

public class Pyramide {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/";

    public final static ImageView[] ageI = new ImageView[]{
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png"),
        Affichage.makeImageView("cartes/CarteI.png")};
    public final static ImageView[] ageII = new ImageView[]{
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png"),
        Affichage.makeImageView("cartes/CarteII.png")};
    public final static ImageView[] ageIII = new ImageView[]{
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png"),
        Affichage.makeImageView("cartes/CarteIII.png")};

    public List<List<Carte>> matrix;
    public List<ImageView> imagesList;
    public String age;

    private static Pyramide firstAgePyramide = new Pyramide(new int[]{2, 3, 4, 5, 6});
    private static Pyramide secondAgePyramide = new Pyramide(new int[]{6, 5, 4, 3, 2});
    private static Pyramide thirdAgePyramide = new Pyramide(new int[]{2, 3, 4, 2, 4, 3, 2});

    private static Pyramide currentAgePyramide = firstAgePyramide;

    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;

    // Constructeur de la Pyramide
    public Pyramide (int[] rowsList){

        this.matrix = new ArrayList<>();

        for (int i : rowsList) {

            List<Carte> row = new ArrayList<>(i);

            for (int j = 0; j < i; j++) {
                row.add(new Carte());
            }
            matrix.add(row);
        }
        this.imagesList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            this.imagesList.add(new ImageView());
        }
    }

    // Obtenir la pyramide du premier age
    public static Pyramide getFirstAgePyramide() {
        return firstAgePyramide;
    }

    // Définir la pyramide du premier age
    public static void setFirstAgePyramide(Pyramide pyramide) {
        firstAgePyramide = pyramide;
    }

    // Obtenir la pyramide du deuxième age
    public static Pyramide getSecondAgePyramide() {
        return secondAgePyramide;
    }

    // Définir la pyramide du deuxième age
    public static void setSecondAgePyramide(Pyramide pyramide) {
        secondAgePyramide = pyramide;
    }

    // Obtenir la pyramide du troisième age
    public static Pyramide getThirdAgePyramide() {
        return thirdAgePyramide;
    }

    // Définir la pyramide du troisième age
    public static void setThirdAgePyramide(Pyramide pyramide) {
        thirdAgePyramide = pyramide;
    }

    // Obtenir la pyramide courrante
    public static Pyramide getCurrentAgePyramide(){
        return currentAgePyramide;
    }

    // Définir la pyramide courrante
    public static void setCurrentAgePyramide(Pyramide mypyramide){
        currentAgePyramide = mypyramide;
    }

    // Met à jour la pyramide courrante
    public static void upgradeAgePyramide(){
        if (currentAgePyramide == firstAgePyramide){
            setCurrentAgePyramide(secondAgePyramide);;
        } else if (currentAgePyramide == secondAgePyramide){
            setCurrentAgePyramide(thirdAgePyramide);
        }
    }


    // Obtenir la carte (row, column)
    public Carte get (int row, int column){
        return this.matrix.get(row).get(column);
    }

    // Définir la carte (row, column)
    public void set (int row, int column, Carte carte){
        this.matrix.get(row).set(column, carte);
    }

    // Return true si la pyramide est null ou false sinon
    public boolean isNull(){
        boolean bool = true;

        for (Carte carte : this.matrix.get(0)) {
            if (carte.age != ""){
                return false;
            }
        }

        return bool;
    }

    // Affiche la Pyramide donnée en argument (static version)
    public static void printpyramide(Pane pane, Pyramide pyramide){
        pyramide.printPyramide(pane);
    }

    // Affiche la Pyramide (this)
    public void printPyramide(Pane pane){
        for (int i = 0; i < this.imagesList.size(); i++) {
            if(pane.getChildren().contains(this.imagesList.get(i))){
                pane.getChildren().remove(this.imagesList.get(i));
            }
        }

        // Premier age
        if (this.matrix.get(3).size() == 5){
            int counter = 0;
            for (int i = 0; i < this.matrix.size(); i++) {
                for (int j = 0; j < this.matrix.get(i).size(); j++) {

                    Carte current = this.get(i, j);

                    ImageView myImageView = current.image; 
                    Carte bottom_left = current.bottom_left;
                    Carte bottom_right = current.bottom_right;
                    
                    if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                        current.isReturned = false;
                    }

                    if(current.isReturned){
                        myImageView = ageI[counter];
                        counter++;
                    }
                    myImageView.setLayoutX((390 + (4 - i)*70 + j * 140)*factorL);
                    myImageView.setLayoutY((255 + i*75)*factorl);

                    pane.getChildren().add(myImageView);
                    this.imagesList.add(myImageView);
                }
            }

        // deuxieme age
        } else if (this.matrix.get(3).size() == 3){

            int counter = 0;
            for (int i = 0; i < this.matrix.size(); i++) {
                for (int j = 0; j < this.matrix.get(i).size(); j++) {
                    
                    Carte current = this.get(i, j);
                    ImageView myImageView = current.image; 

                    Carte bottom_left = current.bottom_left;
                    Carte bottom_right = current.bottom_right;
                    
                    if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                        current.isReturned = false;
                    }

                    if(current.isReturned){
                        myImageView = ageII[counter];
                        counter++;
                    }
                    myImageView.setLayoutX((390 + i*70 + j * 140)*factorL);
                    myImageView.setLayoutY((255 + i*75)*factorl);

                    pane.getChildren().add(myImageView);
                    this.imagesList.add(myImageView);
                }
            }

        // troisieme age
        } else {

            int counter = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < this.matrix.get(i).size(); j++) {
                    
                    Carte current = this.get(i, j);

                    ImageView myImageView = current.image; 
                    Carte bottom_left = current.bottom_left;
                    Carte bottom_right = current.bottom_right;
                    
                    if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                        current.isReturned = false;
                    }

                    if(current.isReturned){
                        myImageView = ageIII[counter];
                        counter++;
                    }
                    myImageView.setLayoutX((390 + (4 - i)*70 + j * 140)*factorL);
                    myImageView.setLayoutY((180 + i*75)*factorl);

                    pane.getChildren().add(myImageView);
                    this.imagesList.add(myImageView);
                }
            }

            ImageView myImageView = this.get(3, 0).image; 
            Carte bottom_left = this.get(3, 0).bottom_left;
            Carte bottom_right = this.get(3, 0).bottom_right;
            
            if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                this.get(3, 0).isReturned = false;
            }  
            if(this.get(3,0).isReturned){
                myImageView = ageIII[counter];
                counter++;
            }
            myImageView.setLayoutX(600*factorL);
            myImageView.setLayoutY(405*factorl);

            ImageView myImageView1 = this.get(3, 1).image; 
            bottom_left = this.get(3, 1).bottom_left;
            bottom_right = this.get(3, 1).bottom_right;
            
            if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                this.get(3, 1).isReturned = false;
            }  
            if (this.get(3,1).isReturned){
                myImageView1 = ageIII[counter];
                counter++;
            }
            myImageView1.setLayoutX(880*factorL);
            myImageView1.setLayoutY(405*factorl);

            pane.getChildren().addAll(myImageView, myImageView1);
            this.imagesList.add(myImageView);
            this.imagesList.add(myImageView1);

            for (int i = 4; i < 7; i++) {
                for (int j = 0; j < this.matrix.get(i).size(); j++) {

                    Carte current = this.get(i, j);

                    myImageView = current.image; 
                    bottom_left = current.bottom_left;
                    bottom_right = current.bottom_right;
                    
                    if ((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "")){
                        current.isReturned = false;
                    }  
                    if(current.isReturned){
                        myImageView = ageIII[counter];
                        counter++;
                    }
                    myImageView.setLayoutX((390 + (i-2)*70 + j * 140)*factorL);
                    myImageView.setLayoutY((180 + i*75)*factorl);

                    pane.getChildren().add(myImageView);
                    this.imagesList.add(myImageView);
                }
            }
        }
    }

    // Return true si la carte à la place i,j est accessible (false sinon)
    public boolean isAccessibleAt(int i, int j){
        
        Carte thisOne = this.get(i, j);
        Carte bottom_left = thisOne.bottom_left;
        Carte bottom_right = thisOne.bottom_right;

        if((bottom_left == null || bottom_left.age == "") && (bottom_right == null || bottom_right.age == "") && thisOne.age != ""){

            return true;
        }

        return false;
    }

    // Return une ArrayList contenant les cartes accessibles de gauche à droite
    public static ArrayList<Carte> cartesAccessibles(Pyramide pyramide){

        ArrayList<Carte> myList = new ArrayList<>();

        if (pyramide.age == "I"){

            for (int i = 0; i < 6; i++) {
                for (int j = Math.max(0, 4-i); j < 5; j++) {
                    if (pyramide.isAccessibleAt(j, i+j-4)){
                        myList.add(pyramide.get(j, i+j-4));
                    }
                }
            }

        } else if (pyramide.age == "II"){

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < Math.min(5, 6-i); j++) {
                    if (pyramide.isAccessibleAt(j, i)){
                        myList.add(pyramide.get(j, i));
                    }
                }
            }

        } else {
            if (pyramide.isAccessibleAt(4, 0)){myList.add(pyramide.get(4, 0));}
            if (pyramide.isAccessibleAt(5, 0)){myList.add(pyramide.get(5, 0));}
            if (pyramide.isAccessibleAt(6, 0)){myList.add(pyramide.get(6, 0));}
            if (pyramide.isAccessibleAt(2, 0)){myList.add(pyramide.get(2, 0));}
            if (pyramide.isAccessibleAt(3, 0)){myList.add(pyramide.get(3, 0));}
            if (pyramide.isAccessibleAt(4, 1)){myList.add(pyramide.get(4, 1));}
            if (pyramide.isAccessibleAt(5, 1)){myList.add(pyramide.get(5, 1));}
            if (pyramide.isAccessibleAt(6, 1)){myList.add(pyramide.get(6, 1));}
            if (pyramide.isAccessibleAt(1, 0)){myList.add(pyramide.get(1, 0));}
            if (pyramide.isAccessibleAt(2, 1)){myList.add(pyramide.get(2, 1));}
            if (pyramide.isAccessibleAt(4, 2)){myList.add(pyramide.get(4, 2));}
            if (pyramide.isAccessibleAt(5, 2)){myList.add(pyramide.get(5, 2));}
            if (pyramide.isAccessibleAt(0, 0)){myList.add(pyramide.get(0, 0));}
            if (pyramide.isAccessibleAt(1, 1)){myList.add(pyramide.get(1, 1));}
            if (pyramide.isAccessibleAt(2, 2)){myList.add(pyramide.get(2, 2));}
            if (pyramide.isAccessibleAt(3, 1)){myList.add(pyramide.get(3, 1));}
            if (pyramide.isAccessibleAt(4, 3)){myList.add(pyramide.get(4, 3));}
            if (pyramide.isAccessibleAt(0, 1)){myList.add(pyramide.get(0, 1));}
            if (pyramide.isAccessibleAt(2, 3)){myList.add(pyramide.get(2, 3));}

        }

        return myList;
    }

    // Rempli une pyramide avec les cartes de cardsList
    public static void fillPyramide (Pyramide pyramide, Carte[] cardsList){

        int counter = 0;

        for (int i = 0; i < pyramide.matrix.size(); i++) {
            for (int j = 0; j < pyramide.matrix.get(i).size(); j++) {
                
                pyramide.set(i, j, cardsList[counter]);
                counter++;
                if(i%2 == 1){
                    pyramide.get(i, j).isReturned = true;
                }
            }
        }
    }

    // Définit les voisins de carte suivant neighbours
    public static void setNeighbour(Pyramide pyramide, Carte carte, int[][] neighbours){

        if (neighbours[0][0] != -1){
            carte.bottom_left = pyramide.get(neighbours[0][0], neighbours[0][1]);
        }
        if (neighbours[1][0] != -1){
            carte.bottom_right = pyramide.get(neighbours[1][0], neighbours[1][1]);
        }
    }

    // définit les voisins de chaque carte de la pyramide suivant l'age
    public static void neighbouringCards (Pyramide pyramide, int age){

        if (age == 1){

            for (int i = 0; i < pyramide.matrix.size(); i++) {
                for (int j = 0; j < pyramide.matrix.get(i).size(); j++) {

                    if( i+1 < pyramide.matrix.size()){
                        pyramide.get(i, j).bottom_left = pyramide.get(i+1, j);
                        pyramide.get(i, j).bottom_right = pyramide.get(i+1, j+1);
                    }else{
                        pyramide.get(i, j).bottom_left = null;
                        pyramide.get(i, j).bottom_right = null;
                    }
                }
            }

        } else if( age == 2){

            for (int i = 0; i < pyramide.matrix.size(); i++) {
                for (int j = 0; j < pyramide.matrix.get(i).size(); j++) {

                    if( i+1 < pyramide.matrix.size() && j-1 >= 0){
                        pyramide.get(i, j).bottom_left = pyramide.get(i+1, j-1);
                    }else{
                        pyramide.get(i, j).bottom_left = null;
                    }

                    if( i+1 < pyramide.matrix.size() && j < pyramide.matrix.get(i+1).size()){
                        pyramide.get(i, j).bottom_right = pyramide.get(i+1, j);
                    }else{
                        pyramide.get(i, j).bottom_right = null;
                    }
                }
            }
        } else {
            setNeighbour(pyramide, pyramide.get(0, 0), new int[][]{{1, 0},{1, 1}});
            setNeighbour(pyramide, pyramide.get(0, 1), new int[][]{{1, 1},{1, 2}});
            setNeighbour(pyramide, pyramide.get(1, 0), new int[][]{{2, 0},{2, 1}});
            setNeighbour(pyramide, pyramide.get(1, 1), new int[][]{{2, 1},{2, 2}});
            setNeighbour(pyramide, pyramide.get(1, 2), new int[][]{{2, 2},{2, 3}});
            setNeighbour(pyramide, pyramide.get(2, 0), new int[][]{{-1, -1},{3, 0}});
            setNeighbour(pyramide, pyramide.get(2, 1), new int[][]{{3, 0},{-1, -1}});
            setNeighbour(pyramide, pyramide.get(2, 2), new int[][]{{-1, -1},{3, 1}});
            setNeighbour(pyramide, pyramide.get(2, 3), new int[][]{{3, 1},{-1, -1}});
            setNeighbour(pyramide, pyramide.get(3, 0), new int[][]{{4, 0},{4, 1}});
            setNeighbour(pyramide, pyramide.get(3, 1), new int[][]{{4, 2},{4, 3}});
            setNeighbour(pyramide, pyramide.get(4, 0), new int[][]{{-1, -1},{5, 0}});
            setNeighbour(pyramide, pyramide.get(4, 1), new int[][]{{5, 0},{5, 1}});
            setNeighbour(pyramide, pyramide.get(4, 2), new int[][]{{5, 1},{5, 2}});
            setNeighbour(pyramide, pyramide.get(4, 3), new int[][]{{5, 2},{-1, -1}});
            setNeighbour(pyramide, pyramide.get(5, 0), new int[][]{{-1, -1},{6, 0}});
            setNeighbour(pyramide, pyramide.get(5, 1), new int[][]{{6, 0},{6, 1}});
            setNeighbour(pyramide, pyramide.get(5, 2), new int[][]{{6, 1},{-1, -1}});
            setNeighbour(pyramide, pyramide.get(6, 0), new int[][]{{-1, -1},{-1, -1}});
            setNeighbour(pyramide, pyramide.get(6, 1), new int[][]{{-1, -1},{-1, -1}});
        }
    }


    // Construit la pyramide du premier age
    public static Pyramide FirstAgePyramide(Carte[] cardsList){

        Pyramide pyramide = new Pyramide(new int[]{2, 3, 4, 5, 6});
        pyramide.age = "I";

        setFirstAgePyramide(pyramide);

        fillPyramide(pyramide, cardsList);
        neighbouringCards(pyramide, 1);

        return pyramide;
    }
    
    // Construit la pyramide du deuxième age
    public static Pyramide SecondAgePyramide(Carte[] cardsList){

        Pyramide pyramide = new Pyramide(new int[]{6, 5, 4, 3, 2});
        pyramide.age = "II";

        setSecondAgePyramide(pyramide);

        fillPyramide(pyramide, cardsList);
        neighbouringCards(pyramide, 2);

        return pyramide;
    }
    
    // Construit la pyramide du troisième age
    public static Pyramide ThirdAgePyramide(Carte[] cardsList){

        Pyramide pyramide = new Pyramide(new int[]{2, 3, 4, 2, 4, 3, 2});
        pyramide.age = "III";

        setThirdAgePyramide(pyramide);

        fillPyramide(pyramide, cardsList);
        neighbouringCards(pyramide, 3);

        return pyramide;
    }
}
