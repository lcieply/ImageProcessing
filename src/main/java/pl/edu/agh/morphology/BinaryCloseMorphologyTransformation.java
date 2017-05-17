package pl.edu.agh.morphology;

import pl.edu.agh.binary.BinaryColor;
import pl.edu.agh.image.Image;
import pl.edu.agh.morphology.interfaces.Transformation;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class BinaryCloseMorphologyTransformation implements Transformation {

    private int radius;
    private BinaryColor foregroundColor;

    public BinaryCloseMorphologyTransformation(int radius, BinaryColor foregroundColor) {
        this.radius = radius;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void process(Image image) {
        Transformation transformation = new BinaryDilateTransformation(radius, foregroundColor);
        transformation.process(image);

        transformation = new BinaryErodeTransformation(radius, foregroundColor);
        transformation.process(image);
    }
}
