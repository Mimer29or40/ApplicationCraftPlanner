package mimer29or40.craftPlanner.model;

import mimer29or40.craftPlanner.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecipeTree
{
    private RecipeTree       parent   = null;
    private List<RecipeTree> children = null;
    private Machine          reference;

    public RecipeTree(Machine machine)
    {
        this.reference = machine;
        this.children = new ArrayList<>();
    }

    public void remove()
    {
        if (parent != null)
        {
            parent.removeChild(this);
        }
    }

    private void removeChild(RecipeTree child)
    {
        if (children.contains(child))
        {
            for (RecipeTree grandChild : child.children)
                child.removeChild(grandChild);
            children.remove(child);
        }
    }

    public void removeAllChildren()
    {
        for (RecipeTree child : children)
            child.removeAllChildren();
        children.clear();
    }

    public void addChildNode(RecipeTree child)
    {
        child.parent = this;
        if (!children.contains(child))
            children.add(child);
    }

    public RecipeTree getTop()
    {
        if (parent == null)
            return this;
        RecipeTree p = parent;
        while (p.parent != null)
            p = p.parent;
        return p;
    }

    public int getLevel()
    {
        int level = 0;
        RecipeTree p = parent;
        while (p != null)
        {
            level++;
            p = p.parent;
        }
        return level;
    }

    public int getDepth()
    {
        if (children.isEmpty())
            return 0;

        int maxLevel = 1;
        for (RecipeTree child : children)
        {
            int level = child.getDepth();
            if (level > maxLevel) maxLevel = level;
        }
        return maxLevel;
    }

    public int walkTree(RecipeTreeCallback callbackHandler)
    {
        int code;
        code = callbackHandler.handleTreeNode(this);
        if (code != RecipeTreeCallback.CONTINUE)
            return code;
        for (Object aChildren : children)
        {
            RecipeTree child = (RecipeTree) aChildren;
            code = child.walkTree(callbackHandler);
            if (code >= RecipeTreeCallback.CONTINUE_PARENT)
            { return code; }
        }
        return code;
    }

    public int walkChildren(RecipeTreeCallback callbackHandler)
    {
        int code = 0;
        for (Object aChildren : children)
        {
            RecipeTree child = (RecipeTree) aChildren;
            code = callbackHandler.handleTreeNode(child);
            if (code >= RecipeTreeCallback.CONTINUE_PARENT)
            { return code; }
            if (code == RecipeTreeCallback.CONTINUE)
            {
                code = child.walkChildren(callbackHandler);
                if (code > RecipeTreeCallback.CONTINUE_PARENT)
                { return code; }
            }
        }
        return code;
    }

    public boolean isDuplicate(RecipeTree treeInQuestion)
    {
        if (parent == null)
            return this.equals(treeInQuestion);
        RecipeTree p = parent;
        while (p.parent != null)
        {
            if (p.equals(treeInQuestion)) return true;
            p = p.parent;
        }
        return false;
    }

    public Machine getReference()
    {
        return reference;
    }

    public void printTree()
    {
        printTree("");
    }

    public void printTree(String prefix)
    {
        if (reference != null)
            Log.info(prefix + reference.toString());
        for (RecipeTree child : children)
            child.printTree(prefix + " ");
    }

    @Override
    public String toString()
    {
        return String.format("RecipeTree [%s]", reference.toString());
    }

    @Override
    public boolean equals(Object object)
    {
        return object instanceof RecipeTree && this.reference.equals(((RecipeTree) object).reference);
    }
}

interface RecipeTreeCallback
{

    public static final int CONTINUE         = 0;
    public static final int CONTINUE_SIBLING = 1;
    public static final int CONTINUE_PARENT  = 2;
    public static final int BREAK            = 3;

    /**
     * @param node the current node to handle
     * @return 0 continue tree walk
     * 1 break this node (continue sibling)
     * 2 break this level (continue parent level)
     * 3 break tree walk
     */
    int handleTreeNode(RecipeTree node);

}
