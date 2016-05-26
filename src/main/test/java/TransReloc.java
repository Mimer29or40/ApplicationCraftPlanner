import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TransReloc extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
//        Group root = new Group();
//        Rectangle rect = new Rectangle(100, 50, Color.BLUE);
//        root.getChildren().add(rect);
//        VBox controlGroup = new VBox();
//        Slider relocX = new Slider(-100, 100, 0);
//        Slider relocY = new Slider(-100, 100, 0);
//        Slider transX = new Slider(-100, 100, 0);
//        Slider transY = new Slider(-100, 100, 0);
//        rect.layoutXProperty().bind(relocX.valueProperty());
//        rect.layoutYProperty().bind(relocY.valueProperty());
//        rect.translateXProperty().bind(transX.valueProperty());
//        rect.translateYProperty().bind(transY.valueProperty());
//        controlGroup.getChildren().addAll(relocX, relocY, transX, transY);
//        root.getChildren().add(controlGroup);
//        controlGroup.relocate(0, 300);
//        Scene scene = new Scene(root, 300, 400, Color.ALICEBLUE);
//        primaryStage.setScene(scene);
//        primaryStage.show();

        StackPane root = new StackPane();
        Rectangle rect = new Rectangle(100, 50, Color.BLUE);
        root.getChildren().add(rect);
        Scene scene = new Scene(root, 300, 300, Color.ALICEBLUE);

        rect.layoutXProperty().addListener((e) -> {
            System.out.println("Layout " + rect.getLayoutX() + ":" + rect.getLayoutY());
            System.out.println("Translate " + rect.getTranslateX() + ":" + rect.getTranslateY());
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
