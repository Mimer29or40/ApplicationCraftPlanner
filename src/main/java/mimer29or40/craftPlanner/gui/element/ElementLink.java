package mimer29or40.craftPlanner.gui.element;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;

public class ElementLink extends Element
{
    public ElementLink(Element start, Element end)
    {
        Point2D startPoint = new Point2D(start.getX() + start.getWidth() / 2, start.getY());
        Point2D endPoint = new Point2D(end.getX() - end.getWidth() / 2, end.getY());

        setDimensions(startPoint.getX() - endPoint.getX(), startPoint.getY() - endPoint.getY());

        Path path = new Path();
        Polygon startTriangle = new Polygon(15, 0, 5, 5, 5, -5);
        Polygon endTriangle = new Polygon(15, 0, 5, 5, 5, -5);
        if (startPoint.getY() >= endPoint.getY())
        {
            setPosition(startPoint.getX() + getWidth() / 2, startPoint.getY() - getHeight() / 2);
            path.getElements().addAll(new MoveTo(0, getHeight()),
                                      new LineTo(getWidth() - 25, getHeight()),
                                      new LineTo(getWidth() - 25, 0),
                                      new LineTo(getWidth(), 0));
            startTriangle.setTranslateX(10 - getWidth() / 2);
            startTriangle.setTranslateY(getHeight() / 2);
            endTriangle.setTranslateX(getWidth() / 2 - 10);
            endTriangle.setTranslateY(-getHeight() / 2);
        }
        else
        {
            setPosition(startPoint.getX() + getWidth() / 2, endPoint.getY() - getHeight() / 2);
            path.getElements().addAll(new MoveTo(0, 0),
                                      new LineTo(getWidth() - 25, 0),
                                      new LineTo(getWidth() - 25, getHeight()),
                                      new LineTo(getWidth(), getHeight()));
            startTriangle.setTranslateX(10 - getWidth() / 2);
            startTriangle.setTranslateY(-getHeight() / 2);
            endTriangle.setTranslateX(getWidth() / 2 - 10);
            endTriangle.setTranslateY(getHeight() / 2);
        }
        getChildren().addAll(path, startTriangle, endTriangle);
        toBack();
    }
}
