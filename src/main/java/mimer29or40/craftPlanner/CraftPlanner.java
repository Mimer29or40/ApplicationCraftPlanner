package mimer29or40.craftPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mimer29or40.craftPlanner.util.Log;
import mimer29or40.craftPlanner.util.Settings;

import javax.swing.*;
import java.io.File;

public class CraftPlanner extends Application
{
    public static Stage mainStage;

    public static Settings appSettings;
    public static Settings userSettings;

    public static File currentDir;

    public static CraftPlanner instance;

    public static void main(String[] args)
    {
        instance = new CraftPlanner();
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        appSettings = new Settings("appSettings");
        userSettings = new Settings("userSettings");

        File currentDir = new File("");
        appSettings.setProperty("currentDir", currentDir.getAbsolutePath());

        try
        {
            appSettings.getProperty("dataDir");
        }
        catch (NullPointerException e)
        {
            appSettings.setProperty("dataDir", "");
        }

        if (appSettings.getProperty("dataDir").isEmpty())
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.showDialog(null, "Select Data Directory");
            File file = chooser.getSelectedFile();
            if (file != null)
                appSettings.setProperty("dataDir", file.getAbsolutePath());
        }

        if (appSettings.getProperty("dataDir").isEmpty())
        {
            Log.warn("Data Directory not specified. Stopping...");
            stop();
        }

        try
        {
            appSettings.getProperty("oreBlocks");
        }
        catch (NullPointerException e)
        {
            appSettings.setProperty("oreBlocks", "false");
        }

        DataCache.loadAllData();
//        DataCache.createRecipeMaps();

        mainStage = primaryStage;

        try
        {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource(Assets.FXML_MAIN).openStream());
            primaryStage.setTitle("Crafting Planner");
            Scene scene = new Scene(root, 1170, 440);
//            scene.getStylesheets().add("css/styleSheet.css");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(200);
            primaryStage.show();
        }
        catch (Exception e)
        {
            Log.error("An error occurred: %s", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void stop()
    {
        System.exit(0);
    }
}
