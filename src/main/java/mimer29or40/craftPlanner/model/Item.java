package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import javafx.scene.image.Image;

public class Item
{
    public String name;
    public String id;
    public Image  icon;

    public Item() {}

    public JsonObject toJson()
    {
        JsonObject item = new JsonObject();

        item.addProperty("name", this.name);

        return item;
    }

    public Item fromJson(JsonObject recipeObject)
    {
        this.name = recipeObject.get("name").getAsString();
        this.id = recipeObject.get("id").getAsString();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("Item [name: %s, id: %s]", this.name, this.id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Item))
            return false;

        Item other = (Item) obj;
        return this.name.equals(other.name) &&
               this.id.equals(other.id);
    }
}
