package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.interfaces.Transformation;

/**
 * Created by Kamil on 2017-05-15.
 */
public class GrayscaleCloseMorphologyTransformation implements Transformation {

    private int radius;

    public GrayscaleCloseMorphologyTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public void transform(Image image) {
        Transformation transformation = new GrayscaleDilateTransformation(radius);
        transformation.transform(image);

        transformation = new GrayscaleErodeTransformation(radius);
        transformation.transform(image);
    }
}
