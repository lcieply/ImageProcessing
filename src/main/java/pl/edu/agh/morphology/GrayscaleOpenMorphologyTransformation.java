package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.morphology.interfaces.Transformation;

/**
 * Created by Kamil on 2017-05-15.
 */
public class GrayscaleOpenMorphologyTransformation implements Transformation {

    private int radius;

    public GrayscaleOpenMorphologyTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public void process(Image image) {
        Transformation transformation = new GrayscaleErodeTransformation(radius);
        transformation.process(image);

        transformation = new GrayscaleDilateTransformation(radius);
        transformation.process(image);
    }
}
