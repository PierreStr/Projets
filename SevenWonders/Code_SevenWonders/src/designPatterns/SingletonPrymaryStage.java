package designPatterns;

import javafx.stage.Stage;

public class SingletonPrymaryStage {

    private static Stage prymaryStage = new Stage();

    public static Stage getPrymaryStage() {
        return prymaryStage;
    }

    public static void setPrymaryStage(Stage stage) {
        prymaryStage = stage;
    }
}
