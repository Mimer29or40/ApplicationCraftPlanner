package mimer29or40.craftPlanner.guiTest.skin;

import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GConnectorStyle;
import de.tesis.dynaware.grapheditor.model.GConnector;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class MachineConnectorSkin extends GConnectorSkin
{
    private static final double SIZE = 10;

    private final Pane      root      = new Pane();
    private final Rectangle rectangle = new Rectangle();

    public MachineConnectorSkin(GConnector connector)
    {
        super(connector);

        root.setManaged(false);
        root.resize(SIZE, SIZE);
        root.setPickOnBounds(false);

        rectangle.setManaged(false);
    }

    @Override
    public double getWidth()
    {
        return SIZE;
    }

    @Override
    public double getHeight()
    {
        return SIZE;
    }

    @Override
    public void applyStyle(GConnectorStyle style)
    {

    }

    @Override
    public Node getRoot()
    {
        return null;
    }
}
