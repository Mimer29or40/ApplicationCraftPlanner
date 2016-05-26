package mimer29or40.craftPlanner.gui.element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Element extends StackPane
{
    public List<Element> subElements = new ArrayList<>();

    private DoubleProperty x = new SimpleDoubleProperty(this, "x", 0);
    private DoubleProperty y = new SimpleDoubleProperty(this, "y", 0);

    public Element()
    {
        super();

        translateXProperty().bind(this.x);
        translateYProperty().bind(this.y);
    }

    public void setupElements()
    {
        for (Element element : subElements)
            element.setupElements();
    }

    public void addElement(Element element)
    {
        subElements.add(element);
        getChildren().add(element);
    }

    public void clearElements()
    {
        for (Element element : subElements)
            element.clearElements();
        subElements.clear();
        getChildren().clear();
    }

    public Element setPosition(double x, double y)
    {
        this.x.set(x);
        this.y.set(y);
        return this;
    }

    public Element setPosition(Point2D point)
    {
        return setPosition(point.getX(), point.getY());
    }

    public Element setDimensions(double width, double height)
    {
        resize(Math.abs(width), Math.abs(height));
        setMinSize(Math.abs(width), Math.abs(height));
        setPrefSize(Math.abs(width), Math.abs(height));
        setMaxSize(Math.abs(width), Math.abs(height));
        return this;
    }

    public void setTextSize(Text text, int textSize, double limit)
    {
        text.setFont(new Font(textSize));
        while (text.getLayoutBounds().getWidth() > limit)
            text.setFont(new Font(text.getFont().getSize() - 0.1));
    }

    public double getX()
    {
        return this.x.get();
    }

    public double getY()
    {
        return this.y.get();
    }

    protected void createSlot(double x, double y)
    {
        Rectangle background = new Rectangle(32, 32);
        background.setStroke(Color.GREY);
        background.setStrokeType(StrokeType.INSIDE);
        background.setFill(new Color(1, 1, 1, 0.5));
        background.setTranslateX(x);
        background.setTranslateY(y);
        this.getChildren().add(background);
    }
}
