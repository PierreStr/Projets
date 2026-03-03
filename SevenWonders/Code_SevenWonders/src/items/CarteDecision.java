package items;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.image.Image;

public class CarteDecision {

    public String sens;
    public String[] priorites;
    public String symbole;
    public Image im;

    public static ArrayList<CarteDecision> listCartesDecisions = makeCards();
    public static int ind = 0;

    private CarteDecision(Image im, String sens, String[] priorites, String symbole){

        this.im = im;
        this.sens = sens;
        this.priorites = priorites;
        this.symbole = symbole;
    }


    public static ArrayList<CarteDecision> getCartesDecisions(){
        return listCartesDecisions;
    }


    public static void setCartesDecisions(ArrayList<CarteDecision> mylistCartesDecisions){
        listCartesDecisions = mylistCartesDecisions;
    }


    public static ArrayList<CarteDecision> makeCards(){
        
        ArrayList<CarteDecision> myList = new ArrayList<>();

        String[] vertRougeComm = new String[]{"Vert", "Rouge", "comm"};
        String[] rougeVertComm = new String[]{"Rouge", "Vert", "comm"};
        String[] commVertRouge = new String[]{"comm", "Vert", "Rouge"};
        String[] commRougeVert = new String[]{"comm", "Rouge", "Vert"};

        
        Image image = new Image("file:src/resources/images/commandants/gvrc.png");
        myList.add(new CarteDecision(image,"gauche", vertRougeComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/gvrc.png"),"gauche", vertRougeComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/gvrc.png"),"gauche", vertRougeComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/grvc.png"),"gauche", rougeVertComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/grvc.png"),"gauche", rougeVertComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/gcrvtriangle.png"),"gauche", commRougeVert, "triangle"));
        
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/dvrc.png"),"droite", vertRougeComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/dvrc.png"),"droite", vertRougeComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/drvc.png"),"droite", rougeVertComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/drvc.png"),"droite", rougeVertComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/drvc.png"),"droite", rougeVertComm, "rien"));
        myList.add(new CarteDecision(new Image("file:src/resources/images/commandants/dcvrrond.png"),"droite", commVertRouge, "rond"));

        return myList;
    }


    public static ArrayList<CarteDecision> shuffle(ArrayList<CarteDecision> mylistCartesDecisions){

        Collections.shuffle(mylistCartesDecisions);

        return mylistCartesDecisions;  
    }


    public static void increment(){
        ind = (ind + 1)%12;
    }

}