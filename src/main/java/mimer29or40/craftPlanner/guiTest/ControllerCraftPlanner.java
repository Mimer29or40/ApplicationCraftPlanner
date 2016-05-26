package mimer29or40.craftPlanner.guiTest;

import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.model.GraphFactory;
import de.tesis.dynaware.grapheditor.window.WindowPosition;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import mimer29or40.craftPlanner.CraftPlanner;
import mimer29or40.craftPlanner.DataCache;
import mimer29or40.craftPlanner.guiTest.skin.MachineSkinController;
import mimer29or40.craftPlanner.model.Item;
import mimer29or40.craftPlanner.util.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerCraftPlanner
{
    private Settings settings = CraftPlanner.appSettings;
    private Settings userData = CraftPlanner.userSettings;

    @FXML
    private MenuItem menuExport;
    @FXML
    private MenuItem menuClear;
    @FXML
    private MenuItem menuClose;
    @FXML
    private MenuItem menuDataDir;
    @FXML
    private MenuItem menuReload;
    @FXML
    private RadioMenuItem menuShowGrid;
    @FXML
    private RadioMenuItem menuSnapToGrid;
    @FXML
    private RadioMenuItem menuDetouredStyle;
    @FXML
    private RadioMenuItem menuGapedStyle;
    @FXML
    private Menu          menuZoomOptions;

    @FXML
    private ToggleButton minimapButton;

    @FXML
    private TextField          filter;
    @FXML
    private ListView<Item>     itemList;
    private FilteredList<Item> filteredList;
    @FXML
    private Button             buttonAddItem;

    @FXML
    private GraphEditorContainer graphEditorContainer;

    private final GraphEditor graphEditor = new DefaultGraphEditor();
    private final MachineSkinController skinController = new MachineSkinController(graphEditor, graphEditorContainer);

    private Scale scaleTransform;
    private double currentZoomFactor = 1;

    ////////////////////////
    //* Button Functions *//
    ////////////////////////

    @FXML
    public void export()
    {
        WritableImage image = graphEditorContainer.snapshot(new SnapshotParameters(), null);
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
            alert.setContentText("Could not Export the window!");
            alert.showAndWait();
        }
    }

    @FXML
    public void clear()
    {
        Commands.clear(graphEditor.getModel());
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

    @FXML
    public void setDetouredStyle()
    {
//        final Map<String, String> customProperties = graphEditor.getProperties().getCustomProperties();
//        customProperties.put(SimpleConnectionSkin.SHOW_DETOURS_KEY, Boolean.toString(true));
//        graphEditor.reload();
    }

    @FXML
    public void setGapedStyle()
    {
//        graphEditor.getProperties().getCustomProperties().remove(SimpleConnectionSkin.SHOW_DETOURS_KEY);
//        graphEditor.reload();
    }

    @FXML
    public void panToCenter()
    {
        graphEditorContainer.panTo(WindowPosition.CENTER);
    }

    //////////////////////
    //* Initialization *//
    //////////////////////

    public void initialize()
    {
        loadConfigs();
        initializeMenuBar();

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

        graphEditorContainer.setGraphEditor(graphEditor);
        graphEditorContainer.getMinimap().visibleProperty().bind(minimapButton.selectedProperty());

        graphEditor.setModel(GraphFactory.eINSTANCE.createGModel());

//        skinController.addNode(0, 0);
//        skinController.addNode(100, 100);
        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setX(0);
        node.setY(0);
        node.setWidth(96);
        node.setHeight(128);
        node.setType("Machine");

        final GConnector rightOutput = GraphFactory.eINSTANCE.createGConnector();
        rightOutput.setType(DefaultConnectorTypes.RIGHT_OUTPUT);
        node.getConnectors().add(rightOutput);

        for (int i = 0; i < 9; i++)
        {
            final GConnector leftInput = GraphFactory.eINSTANCE.createGConnector();
            leftInput.setType(DefaultConnectorTypes.LEFT_INPUT);
            node.getConnectors().add(leftInput);
        }

        Commands.addNode(graphEditor.getModel(), node);
    }

    private void loadConfigs()
    {

    }

    private void initializeMenuBar()
    {
        scaleTransform = new Scale(currentZoomFactor, currentZoomFactor, 0, 0);
        scaleTransform.yProperty().bind(scaleTransform.xProperty());

        graphEditor.getView().getTransforms().add(scaleTransform);

        final ToggleGroup connectionStyleGroup = new ToggleGroup();
        connectionStyleGroup.getToggles().addAll(menuDetouredStyle, menuGapedStyle);

        graphEditor.getProperties().gridVisibleProperty().bind(menuShowGrid.selectedProperty());
        graphEditor.getProperties().snapToGridProperty().bind(menuSnapToGrid.selectedProperty());

        initializeZoomOptions();
    }

    private void initializeZoomOptions()
    {
        final ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 1; i <= 16; i *= 2)
        {
            final RadioMenuItem zoomOption = new RadioMenuItem();
            double zoomFactor = i * 0.25;

            String zoom = (zoomFactor * 100) + "%";

            zoomOption.setText(zoom.replace(".0", ""));
            zoomOption.setOnAction(event -> setZoomFactor(zoomFactor));

            toggleGroup.getToggles().add(zoomOption);
            menuZoomOptions.getItems().add(zoomOption);

            if (i == 1) zoomOption.setSelected(true);
        }
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

    //////////////////////
    //* Other Function *//
    //////////////////////

    private void loadItemTree(String itemId)
    {

    }

    private void setZoomFactor(final double zoomFactor)
    {

        final double zoomFactorRatio = zoomFactor / currentZoomFactor;

        final double currentCenterX = graphEditorContainer.windowXProperty().get();
        final double currentCenterY = graphEditorContainer.windowYProperty().get();

        if (zoomFactor != 1)
        {
            // Cache-while-panning is sometimes very sluggish when zoomed in.
            graphEditorContainer.setCacheWhilePanning(false);
        }
        else
        {
            graphEditorContainer.setCacheWhilePanning(true);
        }

        scaleTransform.setX(zoomFactor);
        graphEditorContainer.panTo(currentCenterX * zoomFactorRatio, currentCenterY * zoomFactorRatio);
        currentZoomFactor = zoomFactor;
    }
}
