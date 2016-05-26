package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;


public class RecipeItem
{
    public String name;
    public String id;

    public int   slot;
    public int   amount;
    public float probability;

    public RecipeItem() {}

    public JsonObject toJson()
    {
        JsonObject item = new JsonObject();

        item.addProperty("name", this.name);
        item.addProperty("id", this.id);
        item.addProperty("slot", this.slot);
        item.addProperty("amount", this.amount);
        item.addProperty("probability", this.probability);

        return item;
    }

    public RecipeItem fromJson(JsonObject recipeObject)
    {
        this.name = recipeObject.get("name").getAsString();
        this.id = recipeObject.get("id").getAsString();
        this.slot = recipeObject.get("slot").getAsInt();
        this.amount = recipeObject.get("amount").getAsInt();
        this.probability = recipeObject.get("probability").getAsFloat();

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
        if (!(obj instanceof RecipeItem))
            return false;

        RecipeItem other = (RecipeItem) obj;
        return this.name.equals(other.name) &&
               this.id.equals(other.id) &&
               this.slot == other.slot &&
               this.amount == other.amount &&
               this.probability == other.probability;
    }
}
