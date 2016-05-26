package mimer29or40.craftPlanner.guiTest.skin;

import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class MachineNodeSkin extends GNodeSkin
{
    private final List<GConnectorSkin> rightConnectorSkins  = new ArrayList<>();
    private final List<GConnectorSkin> leftConnectorSkins   = new ArrayList<>();

    Rectangle background = new Rectangle();
    Rectangle border     = new Rectangle();

    public MachineNodeSkin(GNode node)
    {
        super(node);

        background.widthProperty().bind(getRoot().widthProperty());//.subtract(border.strokeWidthProperty().multiply(2)));
        background.heightProperty().bind(getRoot().heightProperty());//.subtract(border.strokeWidthProperty().multiply(2)));
        background.setStroke(Color.BLACK);
        background.setFill(new Color(1, 1, 1, 0.5));

        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());
        border.setStroke(Color.BLACK);
        border.setFill(null);

        getRoot().getChildren().addAll(background);
    }

    @Override
    public void setConnectorSkins(List<GConnectorSkin> connectorSkins)
    {
        rightConnectorSkins.stream().forEach(skin -> getRoot().getChildren().remove(skin.getRoot()));
        leftConnectorSkins.stream().forEach(skin -> getRoot().getChildren().remove(skin.getRoot()));

        if (connectorSkins != null)
        {
            for (final GConnectorSkin connectorSkin : connectorSkins)
            {
                final String type = connectorSkin.getConnector().getType();

                if (DefaultConnectorTypes.isRight(type)) rightConnectorSkins.add(connectorSkin);
                else if (DefaultConnectorTypes.isLeft(type)) leftConnectorSkins.add(connectorSkin);

                getRoot().getChildren().add(connectorSkin.getRoot());
            }
        }

        layoutConnectors();
    }

    @Override
    public void layoutConnectors()
    {
        layoutConnectors(rightConnectorSkins, true, getRoot().getWidth());
        layoutConnectors(leftConnectorSkins, true, 0);
//        layoutSelectionHalo();
    }

    @Override
    public Point2D getConnectorPosition(GConnectorSkin connectorSkin)
    {
        final Node connectorRoot = connectorSkin.getRoot();

        final Side side = DefaultConnectorTypes.getSide(connectorSkin.getConnector().getType());

        // The following logic is required because the connectors are offset slightly from the node edges.
        final double x, y;
        if (side.equals(Side.LEFT))
        {
            x = 0;
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        }
        else if (side.equals(Side.RIGHT))
        {
            x = getRoot().getWidth();
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        }
        else if (side.equals(Side.TOP))
        {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = 0;
        }
        else
        {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = getRoot().getHeight();
        }

        return new Point2D(x, y);
    }

    private void layoutConnectors(final List<GConnectorSkin> connectorSkins, final boolean vertical, final double offset)
    {

        final int count = connectorSkins.size();

        for (int i = 0; i < count; i++)
        {

            final GConnectorSkin skin = connectorSkins.get(i);
            final Node root = skin.getRoot();

            if (vertical)
            {

                final double offsetY = getRoot().getHeight() / (count + 1);
                final double offsetX = getMinorOffsetX(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel(offset - skin.getWidth() / 2 + offsetX));
                root.setLayoutY(GeometryUtils.moveOnPixel((i + 1) * offsetY - skin.getHeight() / 2));

            }
            else
            {

                final double offsetX = getRoot().getWidth() / (count + 1);
                final double offsetY = getMinorOffsetY(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel((i + 1) * offsetX - skin.getWidth() / 2));
                root.setLayoutY(GeometryUtils.moveOnPixel(offset - skin.getHeight() / 2 + offsetY));
            }
        }
    }

    private static final double MINOR_POSITIVE_OFFSET = 2;
    private static final double MINOR_NEGATIVE_OFFSET = -3;

    private double getMinorOffsetX(final GConnector connector)
    {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.LEFT_INPUT) || type.equals(DefaultConnectorTypes.RIGHT_OUTPUT))
        {
            return MINOR_POSITIVE_OFFSET;
        }
        else
        {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private double getMinorOffsetY(final GConnector connector)
    {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.TOP_INPUT) || type.equals(DefaultConnectorTypes.BOTTOM_OUTPUT))
        {
            return MINOR_POSITIVE_OFFSET;
        }
        else
        {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private void filterMouseDragged(final MouseEvent event)
    {
        if (event.isPrimaryButtonDown() && !isSelected())
        {
            event.consume();
        }
    }
}
