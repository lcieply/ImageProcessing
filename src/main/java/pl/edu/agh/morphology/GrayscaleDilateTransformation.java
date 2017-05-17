package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.morphology.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

import java.util.Arrays;

import static pl.edu.agh.util.ImageUtils.saveOutput;

/**
 * Created by Kamil on 2017-05-15.
 */
public class GrayscaleDilateTransformation implements Transformation {

    private int radius;

    public GrayscaleDilateTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public void process(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[] = new int[image.getWidth() * image.getHeight()];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                output[x+y*imageWidth] = getHighestPixelInMask(x, y, image);
            }
        }

        saveOutput(output, image);
    }

    private int getHighestPixelInMask(int x, int y, Image image) {
        int[] pixelsInMask = getPixelsInMask(x, y, image);
        return pixelsInMask[pixelsInMask.length-1];
    }

    private int[] getPixelsInMask(int x, int y, Image image) {
        int pixels[] = new int[4*radius*radius];
        for(int i = 0; i<pixels.length; i++) {
            pixels[i] = -1;
        }

        int i = 0;
        for(int maskY = y - radius; maskY <= y + radius; maskY++){
            for(int maskX = x - radius; maskX <= x + radius; maskX++){
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image) && isPixelInsideCircle(maskX, maskY, x, y)) {
                    pixels[i] = image.getRedPixel(maskX, maskY);
                    i++;
                }
            }
        }

        Arrays.sort(pixels);
        return pixels;
    }

    private boolean isPixelInsideCircle(int x, int y, double centerX, double centerY) {
        return Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2) < Math.pow(radius, 2);
    }
}
