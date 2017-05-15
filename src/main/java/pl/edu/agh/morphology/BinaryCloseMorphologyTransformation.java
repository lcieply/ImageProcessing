package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.interfaces.Transformation;

/**
 * Created by Kamil on 2017-05-15.
 */
public class BinaryCloseMorphologyTransformation implements Transformation {

    private int radius;
    private int foregroundColor;

    public BinaryCloseMorphologyTransformation(int radius, int foregroundColor) {
        this.radius = radius;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void transform(Image image) {
        Transformation transformation = new BinaryDilateTransformation(radius, foregroundColor);
        transformation.transform(image);

        transformation = new BinaryErodeTransformation(radius, foregroundColor);
        transformation.transform(image);
    }
}
