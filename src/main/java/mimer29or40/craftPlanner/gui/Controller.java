package mimer29or40.craftPlanner.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import mimer29or40.craftPlanner.CraftPlanner;
import mimer29or40.craftPlanner.DataCache;
import mimer29or40.craftPlanner.gui.element.ElementLink;
import mimer29or40.craftPlanner.gui.element.ElementTerminal;
import mimer29or40.craftPlanner.model.Item;
import mimer29or40.craftPlanner.util.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable
{
    private Settings settings = CraftPlanner.appSettings;
    private Settings userData = CraftPlanner.userSettings;

    @FXML
    private MenuItem      menuExport;
    @FXML
    private MenuItem      menuClear;
    @FXML
    private MenuItem      menuClose;
    @FXML
    private MenuItem      menuDataDir;
    @FXML
    private MenuItem      menuReload;

    @FXML
    private TextField          filter;
    @FXML
    private ListView<Item>     itemList;
    private FilteredList<Item> filteredList;
    @FXML
    private Button             buttonAddItem;

    @FXML
    private GraphContainer graphContainer;

    private Graph graph;

    ////////////////////////
    //* Button Functions *//
    ////////////////////////

    @FXML
    public void export()
    {
        WritableImage image = graphContainer.snapshot(new SnapshotParameters(), null);
        File file = new File("./output.png");
        try
        {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        }
        catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Could not Export the graphContainer!");
            alert.showAndWait();
        }
    }

    @FXML
    public void clear()
    {
        graph.clearElements();
    }

    @FXML
    public void close()
    {
        CraftPlanner.instance.stop();
    }

    @FXML
    public void selectDataDirectory()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select Data Directory");
        File file = chooser.getSelectedFile();

        if (file != null)
            settings.setProperty("dataDir", file.getPath());
    }

    @FXML
    public void reload()
    {
        DataCache.loadAllData();
    }

    public void init()
    {
        loadConfigs();

        graph = new Graph();
        graphContainer.setGraph(graph);

        filter.textProperty().addListener((observable, oldValue, newValue) ->
                                                  filteredList.setPredicate(item ->
                                                                            {
                                                                                if (newValue == null || newValue.isEmpty()) return true;
                                                                                String filter = newValue.toLowerCase();
                                                                                return item.name.toLowerCase().contains(filter);
                                                                            }));
        loadItemList();
        itemList.setOnMouseClicked(event ->
                                   {
                                       if (event.getClickCount() == 2)
                                       {
                                           Item selectedItem = itemList.getSelectionModel().getSelectedItem();
                                           loadItemTree(selectedItem.id);
                                       }
                                   });
        buttonAddItem.setOnAction(event ->
                                  {
                                      Item selectedItem = itemList.getSelectionModel().getSelectedItem();
                                      if (selectedItem != null)
                                      {
                                          loadItemTree(selectedItem.id);
                                      }
                                  });

//        ElementTerminus elementTerminus = new ElementTerminus("input", DataCache.getItem("minecraft:iron_block"));
//        graph.addElement(elementTerminus);

        ElementTerminal elementTerminal = new ElementTerminal(DataCache.getRecipe("minecraft:iron_ingot").get(1));
        graph.addElement(elementTerminal);

        ElementTerminal elementTerminal1 = new ElementTerminal(DataCache.getRecipe("minecraft:iron_block").get(0));
        elementTerminal1.setPosition(220, 100);
        graph.addElement(elementTerminal1);

//        ElementItemTab itemTab = new ElementItemTab(DataCache.getItem("test"), 9);
//        graph.addElement(itemTab);

        ElementLink elementLink = new ElementLink(elementTerminal, elementTerminal1);
        graph.addElement(elementLink);

        graph.setupElements();
    }

    private void loadItemTree(String itemId)
    {
//        Log.info("Loading Item Tree: %s", itemId);
//        graphWindow.clearElements();
//
//        EndPointElement output = new EndPointElement("output", DataCache.getItem(itemId));
//        graphWindow.addElement(output);
//
//        if (DataCache.recipes.get(output.item.id) == null)
//        {
//            EndPointElement input = new EndPointElement("input", DataCache.getItem(output.item.id));
//            input.setPosition(output.getX() - 100, output.getY());
//            graphWindow.addElement(input);
//
//            graphWindow.addElement(new LinkElement(input, output));
//        }
//        else
//        {
//            int numberOfRecipes = DataCache.recipes.get(output.item.id).size();
//            for (int i = 0; i < numberOfRecipes; i++)
//            {
//                Recipe recipe = DataCache.recipes.get(output.item.id).get(i);
//                MachineElement machine = new MachineElement(recipe);
//                double xPos = output.getX() - output.getWidth() / 2 - machine.getWidth() / 2 - 50;
//                double yPos = output.getY() + 100 * i - 100 * (numberOfRecipes - 1) / 2;
//                machine.setPosition(xPos, yPos);
//                graphWindow.addElement(machine);
//
//                graphWindow.addElement(new LinkElement(machine, output));
//            }
//        }
//
//        graphWindow.setupElements();
    }

    private void loadConfigs()
    {

    }

    private void loadItemList()
    {
        List<Item> totalList = DataCache.items.keySet().stream().map(DataCache.items::get).collect(Collectors.toList());

        Collections.sort(totalList, (item1, item2) -> item1.name.compareTo(item2.name));

        filteredList = new FilteredList<>(FXCollections.observableList(totalList), p -> true);

        itemList.setItems(filteredList);

        itemList.setCellFactory(l -> new ListCell<Item>()
        {
            @Override
            protected void updateItem(Item item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                {
                    setGraphic(null);
                    setText(null);
                }
                else
                {
                    setGraphic(new ImageView(item.icon));
                    setText(item.name);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (Platform.isFxApplicationThread())
            init();
        else
            Platform.runLater(this::init);
    }
}
