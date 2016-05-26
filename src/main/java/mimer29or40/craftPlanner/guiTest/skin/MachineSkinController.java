package mimer29or40.craftPlanner.guiTest.skin;

import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.SkinLookup;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.*;
import javafx.geometry.Side;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

public class MachineSkinController
{
    protected static final int NODE_INITIAL_X = 19;
    protected static final int NODE_INITIAL_Y = 19;

    protected final GraphEditor          graphEditor;
    protected final GraphEditorContainer graphEditorContainer;

    private static final int MAX_CONNECTOR_COUNT = 9;

    public MachineSkinController(final GraphEditor graphEditor, final GraphEditorContainer graphEditorContainer)
    {
        this.graphEditor = graphEditor;
        this.graphEditorContainer = graphEditorContainer;

        this.graphEditor.setNodeSkin("Machine", MachineNodeSkin.class);
    }

    public void addNode(final double windowXOffset, final double windowYOffset)
    {
        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setX(NODE_INITIAL_X + windowXOffset);
        node.setY(NODE_INITIAL_Y + windowYOffset);
        node.setWidth(64);
        node.setHeight(64);
        node.setType("Machine");

        final GConnector rightOutput = GraphFactory.eINSTANCE.createGConnector();
        rightOutput.setType(DefaultConnectorTypes.RIGHT_OUTPUT);
        node.getConnectors().add(rightOutput);

        final GConnector leftInput = GraphFactory.eINSTANCE.createGConnector();
        leftInput.setType(DefaultConnectorTypes.LEFT_INPUT);
        node.getConnectors().add(leftInput);

        Commands.addNode(graphEditor.getModel(), node);
    }

    public void addConnector(final Side position, final boolean input)
    {
        final String type = getType(position, input);

        final GModel model = graphEditor.getModel();
        final SkinLookup skinLookup = graphEditor.getSkinLookup();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);

        for (final GNode node : model.getNodes())
        {
            if (skinLookup.lookupNode(node).isSelected())
            {
                if (countConnectors(node, position) < MAX_CONNECTOR_COUNT)
                {
                    final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
                    connector.setType(type);

                    final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
                    command.append(AddCommand.create(editingDomain, node, connectors, connector));
                }
            }
        }

        if (command.canExecute()) editingDomain.getCommandStack().execute(command);
    }

    private int countConnectors(final GNode node, final Side side)
    {
        int count = 0;

        for (final GConnector connector : node.getConnectors())
        {
            if (side.equals(DefaultConnectorTypes.getSide(connector.getType())))
            {
                count++;
            }
        }

        return count;
    }

    private String getType(final Side position, final boolean input)
    {
        switch (position)
        {
            case TOP:
                if (input) return DefaultConnectorTypes.TOP_INPUT;
                else return DefaultConnectorTypes.TOP_OUTPUT;
            case RIGHT:
                if (input) return DefaultConnectorTypes.RIGHT_INPUT;
                else  return DefaultConnectorTypes.RIGHT_OUTPUT;
            case BOTTOM:
                if (input) return DefaultConnectorTypes.BOTTOM_INPUT;
                else return DefaultConnectorTypes.BOTTOM_OUTPUT;
            case LEFT:
                if (input) return DefaultConnectorTypes.LEFT_INPUT;
                else return DefaultConnectorTypes.LEFT_OUTPUT;
        }
        return null;
    }
}
