package mimer29or40.craftPlanner.util;

import javafx.scene.image.Image;

public class IconHelper
{
    public static Image getResizedImage(String image, int size)
    {
        return new Image("file:" + image, size, size, false, false);
    }
}
