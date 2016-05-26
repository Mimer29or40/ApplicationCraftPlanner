package mimer29or40.craftPlanner.gui.element;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import mimer29or40.craftPlanner.DataCache;
import mimer29or40.craftPlanner.model.Item;
import mimer29or40.craftPlanner.model.Machine;

import java.util.ArrayList;
import java.util.List;

public class ElementTerminus extends Element
{
    public Item    item;
    public Machine machine;

    public ElementTerminus(String machineId, Item itemId)
    {
        super();

        item = itemId;
        machine = DataCache.machines.get(machineId);

        setDimensions(machine.width + 2 * machine.border, machine.height + 2 * machine.border);

        Rectangle background = new Rectangle(getWidth(), getHeight());
        background.setStroke(Color.BLACK);
        background.setStrokeType(StrokeType.INSIDE);
        background.setFill(Color.LIGHTBLUE);
        setAlignment(background, Pos.CENTER);
        this.getChildren().add(background);

        List<Point2D> slots = new ArrayList<>();
        slots.addAll(machine.inputSlots.values());
        slots.addAll(machine.outputSlots.values());
        for (Point2D slotPoint : slots)
            createSlot(slotPoint.getX() - getWidth() / 2 + machine.border, slotPoint.getY() - getHeight() / 2 + machine.border);

        Text machineName = new Text(machine.name);
        setTextSize(machineName, 15, getWidth());
        machineName.setTranslateY(-machineName.getBoundsInParent().getHeight());
        setAlignment(machineName, Pos.TOP_CENTER);
        this.getChildren().add(machineName);

        Text itemName = new Text(item.name);
        setTextSize(itemName, 15, 2 * getWidth());
        itemName.setTranslateY(itemName.getBoundsInParent().getHeight());
        setAlignment(itemName, Pos.BOTTOM_CENTER);
        this.getChildren().add(itemName);
    }

    @Override
    public void setupElements()
    {
        List<Point2D> slots = new ArrayList<>();
        slots.addAll(machine.inputSlots.values());
        slots.addAll(machine.outputSlots.values());
        for (Point2D slotPoint : slots)
        {
            ElementItemTab itemTab = new ElementItemTab(item, 1);
            double xPos = slotPoint.getX() - getWidth() / 2 + machine.border;
            double yPos = slotPoint.getY() - getHeight() / 2 + machine.border;
            itemTab.setPosition(xPos, yPos);
            addElement(itemTab);
        }

        super.setupElements();
    }
}
