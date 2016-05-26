package mimer29or40.craftPlanner.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class GraphContainer extends ScrollPane
{
    private Graph graph;

    private final StackPane zoomPane = new StackPane();

    private static final double SCALE_DELTA = 1.1D;
    private static final double SCALE_MIN   = 0.25D;
    private static final double SCALE_MAX   = 20.0D;

    public GraphContainer()
    {
        final Group scrollContent = new Group(zoomPane);
        setContent(scrollContent);

        viewportBoundsProperty().addListener((observable, oldValue, newValue) -> zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight()));

        zoomPane.setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) return;

            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
            double scale = graph.getScale() * scaleFactor;

            if (Double.compare(scale, SCALE_MIN) < 0) scale = SCALE_MIN;
            if (Double.compare(scale, SCALE_MAX) > 0) scale = SCALE_MAX;

            graph.setScale(scale);

            // moveBy viewport so that old center remains in the center after the scaling
            repositionContainer(scrollContent, scaleFactor);
        });

        // Panning via drag....
        setPannable(true);
    }

    public void setGraph(Graph graph)
    {
        this.graph = graph;
        this.zoomPane.getChildren().clear();
        this.zoomPane.getChildren().add(new Group(graph));
    }

    private void repositionContainer(Node scrollContent, double scaleFactor)
    {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - getViewportBounds().getWidth();
        double hScrollProportion = (getHvalue() - getHmin()) / (getHmax() - getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);

        double extraHeight = scrollContent.getLayoutBounds().getHeight() - getViewportBounds().getHeight();
        double vScrollProportion = (getVvalue() - getVmin()) / (getVmax() - getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);

        if (extraWidth > 0)
        {
            double halfWidth = getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            setHvalue(getHmin() + newScrollXOffset * (getHmax() - getHmin()) / extraWidth);
        }
        else
        {
            setHvalue(getHmin());
        }

        if (extraHeight > 0)
        {
            double halfHeight = getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            setVvalue(getVmin() + newScrollYOffset * (getVmax() - getVmin()) / extraHeight);
        }
        else
        {
            setHvalue(getHmin());
        }
    }
}
