package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

import java.util.Arrays;

import static pl.edu.agh.util.ImageUtils.saveOutput;

/**
 * Created by Kamil on 2017-05-15.
 */
public class GrayscaleErodeTransformation implements Transformation {

    private int radius;
    private int foregroundColor;

    public GrayscaleErodeTransformation(int radius) {
        this.radius = radius;
    }

    public GrayscaleErodeTransformation(int radius, int foregroundColor) {
        this.radius = radius;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void transform(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[] = new int[imageWidth * imageHeight];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                output[x+y*imageWidth] = getLowestPixelInMask(x, y, image);
            }
        }

        saveOutput(output, image);
    }

    private int getLowestPixelInMask(int x, int y, Image image) {
        int[] pixelsInMask = getPixelsInMask(x, y, image);
        int i = 0;
        while (pixelsInMask[i] == -1) {
            i++;
        }

        return pixelsInMask[i];
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