package mimer29or40.craftPlanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import mimer29or40.craftPlanner.model.Item;
import mimer29or40.craftPlanner.model.Machine;
import mimer29or40.craftPlanner.model.Recipe;
import mimer29or40.craftPlanner.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache
{
    public static final Map<String, Item>   items     = new HashMap<>();

    public static final Map<String, List<Recipe>> recipes = new HashMap<>();

    public static final Map<String, Machine> machines = new HashMap<>();

    public static void loadAllData()
    {
        Log.info("Loading Data...");
        clearData();

        File dataDir = new File(CraftPlanner.appSettings.getProperty("dataDir"));

        if (!dataDir.exists())
        {
            Log.error("Data directory does not exist. Export the data before running this.");
            CraftPlanner.instance.stop();
        }

        File recipesFile = new File(dataDir, "Recipes.json");
        File itemsFile   = new File(dataDir, "Items.json");

        File iconDir = new File(dataDir, "itemIcons");
        if (!iconDir.exists() && !iconDir.mkdir())
        {
            Log.error("Could not find iconIcons dir");
            CraftPlanner.instance.stop();
        }

        File machinesDir = new File(dataDir, "machines");
        if (!machinesDir.exists() && !machinesDir.mkdir())
        {
            Log.error("Could not find machines dir");
            CraftPlanner.instance.stop();
        }

        JsonParser parser = new JsonParser();

        // Read Data files
        JsonElement recipesElement = JsonNull.INSTANCE;
        JsonElement itemsElement = JsonNull.INSTANCE;
        try
        {
            recipesElement = parser.parse(new FileReader(recipesFile));
            itemsElement = parser.parse(new FileReader(itemsFile));
        }
        catch (FileNotFoundException e)
        {
            Log.warn("Could not find Recipes.json or Items.json");
            CraftPlanner.instance.stop();
        }

        // Load Machines
        Log.info("Loading Machines...");
        File[] machineFiles = machinesDir.listFiles();
        if (machineFiles != null)
        {
            for (File machineFile : machineFiles)
                try
                {
                    JsonObject machineElement = parser.parse(new FileReader(machineFile)).getAsJsonObject();
                    Machine machine = new Machine(machineElement);
                    Log.info("Machine[%s] created", machine.name);
                    machines.put(machine.id, machine);
                }
                catch (IOException e)
                {
                    Log.warn("An error occurred while parsing machine %s. It will be ignored", machineFile.getName());
                }
        }

        if (machines.size() < 1)
        {
            Log.error("No machines found. Make sure that the files are correct");
            CraftPlanner.instance.stop();
        }

        // Load Items
        Log.info("Loading Items...");
        for (JsonElement itemObject : itemsElement.getAsJsonArray())
        {
            Item item = new Item().fromJson(itemObject.getAsJsonObject());
            try
            {
                item.icon = new Image(new File(iconDir, item.id + ".png").toURI().toURL().openStream());
            }
            catch (Exception e)
            {
                Log.error("Could not find image for item: %s %s", item.name, e.getMessage());
            }
            items.put(item.id, item);
        }
        Log.info("Found %s Items", items.size());

        // Load Recipes
        Log.info("Loading Recipes...");
        for (JsonElement recipeObject : recipesElement.getAsJsonArray())
        {
            Recipe recipe = new Recipe().fromJson(recipeObject.getAsJsonObject());
            recipes.putIfAbsent(recipe.id, new ArrayList<>());
            recipes.get(recipe.id).add(recipe);
        }
        Log.info("Found Recipes for %s Items", recipes.size());

        Log.info("Finished Loading Data");
    }

    public static void clearData()
    {
        items.clear();
        recipes.clear();
    }

//    public static void createRecipeMap(String itemId)
//    {
//        if (items.containsKey(itemId) && machineList.get(itemId) != null)
//        {
////            recipeMap = getRecipeTrees(null, itemId);
////            Log.info(items.get(itemId).name + " depth:" + recipeMap.getDepth());
//////            printInfo(recipeMap, "");
////            recipeMap.printTree();
//
//            RecipeTree tree = new RecipeTree(outputs.get(itemId));
//
//            createSubTrees(tree);
//            tree.printTree();
//
//            recipeMaps.put(itemId, tree);
//        }
//    }
//
//    public static void createRecipeMaps()
//    {
//        for (String itemId : items.keySet())
//        {
//            if (machineList.get(itemId) != null)
//            {
//                RecipeTree tree = new RecipeTree(outputs.get(itemId));
//
//                createSubTrees(tree);
//                getRecipeTrees(null, itemId);
//                tree.printTree();
//
//                recipeMaps.put(itemId, tree);
//            }
//        }
//    }
//
//    private static void createSubTrees(RecipeTree parent)
//    {
//        Machine machine = parent.getReference();
//        Log.info("                                                     " + machine.toString());
//
//        if (machine instanceof Input) return;
//
//        for (Item item : machine.inputItems.values())
//        {
//            if (item != null)
//            {
//                if (machineList.get(item.id) != null && machineList.get(item.id).size() > 0)
//                {
//                    for (Machine newMachine : machineList.get(item.id))
//                    {
//                        RecipeTree child = new RecipeTree(newMachine);
//                        if (parent.isDuplicate(child))
//                        {
//                            child = new RecipeTree(inputs.get(item.id));
//                        }
//                        createSubTrees(child);
//                        parent.addChildNode(child);
//                    }
//                }
//                else
//                    parent.addChildNode(new RecipeTree(inputs.get(item.id)));
//            }
//        }
//    }
//
//    private static RecipeTree getRecipeTrees(RecipeTree parent, List<Machine> machines)
//    {
//        List<RecipeTree> potentialTrees = new ArrayList<>();
//
//        for (Machine machine : machines)
//        {
//            RecipeTree currentTree;
//
//            if (parent == null && machine instanceof Output)
//                currentTree = new RecipeTree(machine);
//            else if (parent != null)
//            {
//                currentTree = new RecipeTree(machine);
//
//                if (parent.getTop().equals(currentTree) &&)
//            }
//
//
//            if (parent != null && parent.getReference().equals(machine))
//                currentTree = new RecipeTree(items.get(machine.id));
//            else
//            {
//                currentTree = new RecipeTree(machine);
//
//                for (RecipeItem inputItem : machine.inputItems)
//                    getRecipeTrees(currentTree, inputItem.id);
//
//                for (RecipeItem inputFluid : machine.inputFluids)
//                    getRecipeTrees(currentTree, inputFluid.id);
//            }
//            potentialTrees.add(currentTree);
//        }
//
//        RecipeTree selectedTree = null;
//        int level = Integer.MAX_VALUE;
//        for (RecipeTree currentTree : potentialTrees)
//        {
//            if (currentTree.getDepth() < level) selectedTree = currentTree;
//        }
//
//        return selectedTree;
//    }
//
//    private static RecipeTree getRecipeTrees(RecipeTree parent, String itemId)
//    {
////        Log.info("Item " + itemId);
//        RecipeTree tree;
//        if (parent == null)
//        {
//            tree = new RecipeTree(outputs.get(itemId));
//
//            for (Item inputItem : tree.getReference().inputItems.values())
//                getRecipeTrees(tree, inputItem.id);
//        }
//        else
//        {
//            RecipeTree testTree;
//            for (Machine machine : machineList.get(itemId))
//            {
//                testTree = new RecipeTree(machine);
//                if (testTree.getDepth() <)
//            }
//        }
//
//
//
//
//
//
//
//        if (recipes.containsKey(itemId))
//        {
////            if (storageBlocks.contains(itemId) || woolBlocks.contains(itemId))
////                tree = new RecipeTree(items.get(itemId));
////            else
//            if (parent != null && parent.getReference().equals(recipes.get(itemId)))
//                tree = new RecipeTree(items.get(itemId));
//            else
//                tree = new RecipeTree(recipes.get(itemId));
//        }
//        else
//            tree = new RecipeTree(items.get(itemId));
//
//        if (parent != null)
//            parent.addChildNode(tree);
//
////        Log.debug("Tree Depth: " + tree.getLevel());
//        if (tree.getLevel() >= 100)
//            return tree;
//
//        if (tree.getReference() instanceof Recipe)
//        {
//            Recipe recipe = (Recipe) tree.getReference();
//
//            for (RecipeItem inputItem : recipe.inputItems)
//                getRecipeTrees(tree, inputItem.id);
//
//            for (RecipeItem inputFluid : recipe.inputFluids)
//                getRecipeTrees(tree, inputFluid.id);
//        }
//
//        return tree;
//    }

    public static Item getItem(String itemId)
    {
        Item item = items.get(itemId);
        if (item == null)
        {
            Log.error("Item [%s] was not found", itemId);
            return null;
        }
        return item;
    }

    public static List<Recipe> getRecipe(String recipeId)
    {
        List<Recipe> recipe = recipes.get(recipeId);
        if (recipe == null)
        {
            Log.error("Recipe [%s] was not found", recipeId);
            return null;
        }
        return recipe;
    }
}
