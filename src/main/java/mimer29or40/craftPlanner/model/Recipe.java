package mimer29or40.craftPlanner.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe
{
    public String name;
    public String id;
    public String category;

    public final List<RecipeItem> inputItems   = new ArrayList<>();
    public final List<RecipeItem> outputItems  = new ArrayList<>();
    public final List<RecipeItem> inputFluids  = new ArrayList<>();
    public final List<RecipeItem> outputFluids = new ArrayList<>();

    public Recipe() {}

    public JsonObject toJson()
    {
        JsonObject recipe = new JsonObject();

        recipe.addProperty("name", this.name);
        recipe.addProperty("category", this.category);
        recipe.addProperty("id", this.id);

        JsonArray inputs = new JsonArray();
        for (RecipeItem inputItem : this.inputItems)
        { inputs.add(inputItem.toJson()); }
        recipe.add("inputs", inputs);

        JsonArray outputs = new JsonArray();
        for (RecipeItem outputItem : this.outputItems)
        { outputs.add(outputItem.toJson()); }
        recipe.add("outputs", outputs);

        JsonArray fluidInputs = new JsonArray();
        for (RecipeItem inputFluid : this.inputFluids)
        { fluidInputs.add(inputFluid.toJson()); }
        recipe.add("fluidInputs", fluidInputs);

        JsonArray fluidOutputs = new JsonArray();
        for (RecipeItem outputFluid : this.outputFluids)
        { fluidOutputs.add(outputFluid.toJson()); }
        recipe.add("fluidOutputs", fluidOutputs);

        return recipe;
    }

    public Recipe fromJson(JsonObject recipeObject)
    {
        this.name = recipeObject.get("name").getAsString();
        this.category = recipeObject.get("category").getAsString();
        this.id = recipeObject.get("id").getAsString();

        JsonArray inputs = recipeObject.getAsJsonArray("inputs");
        if (!inputs.isJsonNull())
        {
            for (JsonElement inputItem : inputs)
            { this.inputItems.add(new RecipeItem().fromJson(inputItem.getAsJsonObject())); }
        }

        JsonArray outputs = recipeObject.getAsJsonArray("outputs");
        if (!outputs.isJsonNull())
        {
            for (JsonElement outputItem : outputs)
            { this.outputItems.add(new RecipeItem().fromJson(outputItem.getAsJsonObject())); }
        }

        JsonArray fluidInputs = recipeObject.getAsJsonArray("fluidInputs");
        if (!fluidInputs.isJsonNull())
        {
            for (JsonElement fluidInput : fluidInputs)
            { this.inputFluids.add(new RecipeItem().fromJson(fluidInput.getAsJsonObject())); }
        }

        JsonArray fluidOutputs = recipeObject.getAsJsonArray("fluidOutputs");
        if (!fluidOutputs.isJsonNull())
        {
            for (JsonElement fluidOutput : fluidOutputs)
            { this.outputFluids.add(new RecipeItem().fromJson(fluidOutput.getAsJsonObject())); }
        }

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("Recipe [name: %s, id: %s, category: %s]", this.name, this.id, this.category);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Recipe))
            return false;

        Recipe other = (Recipe) obj;
        return this.name.equals(other.name) &&
               this.id.equals(other.id) &&
               this.category.equals(other.category) &&
               this.inputItems.equals(other.inputItems) &&
               this.inputFluids.equals(other.inputFluids) &&
               this.outputItems.equals(other.outputItems) &&
               this.outputFluids.equals(other.outputFluids);
    }
}
