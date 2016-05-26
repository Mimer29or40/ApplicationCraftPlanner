package mimer29or40.craftPlanner.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;

public class Machine
{
    public String name = "";
    public String id   = "";

    public int width  = 0;
    public int height = 0;

    public int border = 0;

    public Point2D arrowPosition = Point2D.ZERO;

    public Map<Integer, Point2D> inputSlots  = new HashMap<>();
    public Map<Integer, Point2D> outputSlots = new HashMap<>();

    public Machine(JsonObject object)
    {
        this.name = object.get("name").getAsString();
        this.id = object.get("id").getAsString();

        this.width = object.get("width").getAsInt();
        this.height = object.get("height").getAsInt();

        if (object.get("border") != null)
            this.border = object.get("border").getAsInt();

        if (object.get("arrowPos") != null)
        {
            JsonObject arrowPos = object.get("arrowPos").getAsJsonObject();
            this.arrowPosition = new Point2D(arrowPos.get("x").getAsInt(), arrowPos.get("y").getAsInt());
        }

        if (object.get("inputSlots") != null)
            for (JsonElement inputSlotElement : object.get("inputSlots").getAsJsonArray())
            {
                JsonObject inputSlot = (JsonObject) inputSlotElement;
                Point2D slotPos = new Point2D(inputSlot.get("x").getAsInt(), inputSlot.get("y").getAsInt());
                inputSlots.put(inputSlot.get("slot").getAsInt(), slotPos);
            }

        if (object.get("outputSlots") != null)
            for (JsonElement outputSlotElement : object.get("outputSlots").getAsJsonArray())
            {
                JsonObject outputSlot = (JsonObject) outputSlotElement;
                Point2D slotPos = new Point2D(outputSlot.get("x").getAsInt(), outputSlot.get("y").getAsInt());
                outputSlots.put(outputSlot.get("slot").getAsInt(), slotPos);
            }
    }
}
