package mimer29or40.craftPlanner.gui.event;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import mimer29or40.craftPlanner.gui.Graph;

public class GraphEventHandler
{
    private static final double MAX_SCALE   = 20.0D;
    private static final double MIN_SCALE   = 0.25D;
    private static final double SCALE_DELTA = 1.1;

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double translateAnchorX;
    private double translateAnchorY;

    private Graph graphWindow;

    public GraphEventHandler(Graph graphWindow)
    {
        this.graphWindow = graphWindow;
    }

    public EventHandler<MouseEvent> mousePressedEvent = (event) ->
    {
        mouseAnchorX = event.getSceneX();
        mouseAnchorY = event.getSceneY();

        translateAnchorX = graphWindow.getTranslateX();
        translateAnchorY = graphWindow.getTranslateY();
    };

    public EventHandler<MouseEvent> mouseDraggedEvent = (event) ->
    {
        graphWindow.setTranslateX(translateAnchorX + event.getSceneX() - mouseAnchorX);
        graphWindow.setTranslateY(translateAnchorY + event.getSceneY() - mouseAnchorY);

        event.consume();
    };

    public EventHandler<ScrollEvent> scrollEvent = (event) ->
    { // TODO scrolling is wonky
//        double scale = graphWindow.getScale();
//        double oldScale = scale;
//
//        if (event.getDeltaY() < 0)
//        { scale /= SCALE_DELTA; }
//        else
//        { scale *= SCALE_DELTA; }
//
//        scale = clamp(scale, MIN_SCALE, MAX_SCALE);
//
//        graphWindow.setScale(scale);
//
//        double f = (scale / oldScale) - 1;
//        double dx = (event.getX() - (graphWindow.getBoundsInParent().getWidth() / 2 + graphWindow.getBoundsInParent().getMinX()));
//        double dy = (event.getY() - (graphWindow.getBoundsInParent().getHeight() / 2 + graphWindow.getBoundsInParent().getMinY()));
//
//        // note: pivot value must be untransformed, i. e. without scaling
////        graphWindow.setPivot(f * dx, f * dy);
//
//        event.consume();
    };

    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scrollPane)
    {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth();
        double hScrollProportion = (scrollPane.getHvalue() - scrollPane.getHmin()) / (scrollPane.getHmax() - scrollPane.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight();
        double vScrollProportion = (scrollPane.getVvalue() - scrollPane.getVmin()) / (scrollPane.getVmax() - scrollPane.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScrollPane(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset)
    {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0)
        {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        }
        else
        {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0)
        {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        }
        else
        {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    private static double clamp(double value, double min, double max)
    {
        if (Double.compare(value, min) < 0) return min;
        if (Double.compare(value, max) > 0) return max;
        return value;
    }
}
