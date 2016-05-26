package mimer29or40.craftPlanner.gui.element;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mimer29or40.craftPlanner.model.Item;

public class ElementItemTab extends Element
{
    private Item item;
    private int  itemAmount;

    public ElementItemTab(Item item, int itemAmount)
    {
        super();

        this.item = item;
        this.itemAmount = itemAmount;

        setDimensions(32, 32);

        ImageView itemIcon = new ImageView(item.icon);
        itemIcon.setScaleX(0.45);
        itemIcon.setScaleY(0.45);
        setAlignment(itemIcon, Pos.CENTER);
        this.getChildren().add(itemIcon);

        Text itemAmountText = new Text(String.valueOf(itemAmount));
        if (itemAmount == 1) itemAmountText.setText("");
        setTextSize(itemAmountText, 20, getWidth() - 2);
        itemAmountText.setTranslateX(-1);
        itemAmountText.setTranslateY(+1);
        itemAmountText.setStrokeWidth(0.5);
        itemAmountText.setStroke(Color.BLACK);
        itemAmountText.setFill(Color.WHITE);
        setAlignment(itemAmountText, Pos.BOTTOM_RIGHT);
        this.getChildren().add(itemAmountText);
    }
}
