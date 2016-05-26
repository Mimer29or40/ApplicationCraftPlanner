package mimer29or40.craftPlanner.gui.element;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import mimer29or40.craftPlanner.DataCache;
import mimer29or40.craftPlanner.model.Machine;
import mimer29or40.craftPlanner.model.Recipe;
import mimer29or40.craftPlanner.model.RecipeItem;

public class ElementTerminal extends Element
{
    private final Machine machine;
    private final Recipe  recipe;

    public ElementTerminal(Recipe recipe_)
    {
        super();

        recipe = recipe_;
        machine = DataCache.machines.get(recipe.category);

        setDimensions(machine.width + 2 * machine.border, machine.height + 2 * machine.border);

        Rectangle background = new Rectangle(getWidth(), getHeight());
        background.setStroke(Color.BLACK);
        background.setStrokeType(StrokeType.INSIDE);
        background.setFill(Color.LIGHTBLUE);
        setAlignment(background, Pos.CENTER);
        getChildren().add(background);

        if (machine.arrowPosition != null)
        {
            Polygon arrow = new Polygon( 4, 12,
                                        18, 12,
                                        18,  6,
                                        28, 16,
                                        18, 26,
                                        18, 20,
                                         4, 20);
            arrow.setTranslateX(machine.arrowPosition.getX() - getWidth() / 2 + machine.border);
            arrow.setTranslateY(machine.arrowPosition.getY() - getHeight() / 2 + machine.border);
            getChildren().add(arrow);
        }

        for (Point2D slotPos : machine.inputSlots.values())
            createSlot(slotPos.getX() - (getWidth() / 2) + machine.border, slotPos.getY() - (getHeight() / 2) + machine.border);

        for (Point2D slotPos : machine.outputSlots.values())
            createSlot(slotPos.getX() - (getWidth() / 2) + machine.border, slotPos.getY() - (getHeight() / 2) + machine.border);

        Text machineName = new Text(machine.name);
        setTextSize(machineName, 15, getWidth());
        machineName.setTranslateY(-machineName.getBoundsInParent().getHeight());
        setAlignment(machineName, Pos.TOP_CENTER);
        getChildren().add(machineName);

        Text recipeName = new Text(recipe.name);
        setTextSize(recipeName, 15, getWidth());
        recipeName.setTranslateY(recipeName.getBoundsInParent().getHeight());
        setAlignment(recipeName, Pos.BOTTOM_CENTER);
        getChildren().add(recipeName);
    }

    @Override
    public void setupElements()
    {
        for (RecipeItem inputItem : recipe.inputItems)
        {
            ElementItemTab inputItemTab = new ElementItemTab(DataCache.getItem(inputItem.id), inputItem.amount);
            inputItemTab.setPosition(
                    machine.inputSlots.get(inputItem.slot)
                                      .subtract(getWidth() / 2,getHeight() / 2)
                                      .add(machine.border, machine.border)
                                    );
            addElement(inputItemTab);
        }

        for (RecipeItem outputItem : recipe.outputItems)
        {
            ElementItemTab outoutItemTab = new ElementItemTab(DataCache.getItem(outputItem.id), outputItem.amount);
            outoutItemTab.setPosition(
                    machine.outputSlots.get(outputItem.slot)
                                       .subtract(getWidth() / 2, getHeight() / 2)
                                       .add(machine.border, machine.border)
                                     );
            addElement(outoutItemTab);
        }

        super.setupElements();
    }
}
