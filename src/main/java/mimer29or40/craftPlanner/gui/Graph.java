package mimer29or40.craftPlanner.gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import mimer29or40.craftPlanner.gui.element.Element;

import java.util.ArrayList;
import java.util.List;

public class Graph extends StackPane
{
    public List<Element> elements = new ArrayList<>();

    public double padding = 10;

    public Graph()
    {
        setPadding(new Insets(10, 10, 10, 10));
        setMinSize(100, 100);
        setStyle("-fx-border-color: black;");
    }

    public void setupElements()
    {
        for (Element element : elements)
            element.setupElements();
        repositionChildren();
        fitToChildren();
    }

    public void addElement(Element element)
    {
        elements.add(element);
        getChildren().add(element);
        layoutChildren();
    }

    public void clearElements()
    {
        for (Element element : elements)
            element.clearElements();
        elements.clear();
        getChildren().clear();
    }

    public double getScale()
    {
        return getScaleX();
    }

    public void setScale(double scale)
    {
        setScaleX(scale);
        setScaleY(scale);
    }

    public void repositionChildren()
    {
        double totalX = 0, totalY = 0;

        for (Element element : elements)
        {
            totalX += element.getX();
            totalY += element.getY();
        }

        final double averageX = totalX / elements.size();
        final double averageY = totalY / elements.size();

        for (Element element : elements)
            element.setPosition(element.getX() - averageX, element.getY() - averageY);
    }

    public void fitToChildren()
    {
        double minX = 0;
        double minY = 0;
        double maxX = 0;
        double maxY = 0;

        for (Element element : elements)
        {
            Bounds bounds = element.getBoundsInParent();
            if (bounds.getMinX() < minX) minX = bounds.getMinX();
            if (bounds.getMinY() < minY) minY = bounds.getMinY();
            if (bounds.getMaxX() > maxX) maxX = bounds.getMaxX();
            if (bounds.getMaxY() > maxY) maxY = bounds.getMaxY();

//            if (element.getX() - element.getWidth() / 2 < minX) minX = element.getX() - element.getWidth() / 2;
//            if (element.getY() - element.getHeight() / 2 < minY) minY = element.getY() - element.getHeight() / 2;
//            if (element.getX() + element.getWidth() / 2 > maxX) maxX = element.getX() + element.getWidth() / 2;
//            if (element.getY() + element.getHeight() / 2 > maxY) maxY = element.getY() + element.getHeight() / 2;
        } // TODO Make this better somehow???

        double newWidth = 2 * padding + (maxX - minX);
        double newHeight = 2 * padding + (maxY - minY);
        resize(newWidth, newHeight);
        setMinSize(newWidth, newHeight);
        setPrefSize(newWidth, newHeight);
    }
}
