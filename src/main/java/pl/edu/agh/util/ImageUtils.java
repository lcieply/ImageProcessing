package pl.edu.agh.util;

import pl.edu.agh.image.Image;

/**
 * Created by Kamil Jureczka on 2017-05-10.
 */
public class ImageUtils {

    public static void saveOutput(int output[], Image image) {
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = output[x+y*image.getWidth()];
                image.setPixel(x, y, 255, pixel, pixel, pixel);
            }
        }
    }

    public static void saveOutputRGB(int output[][], Image image) {
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixelRed = output[x+y*image.getWidth()][0];
                int pixelGreen = output[x+y*image.getWidth()][1];
                int pixelBlue = output[x+y*image.getWidth()][2];

                image.setPixel(x, y, 0, pixelRed, pixelGreen, pixelBlue);
            }
        }
    }

    public static boolean isPixelInsideImage(int x, int y, Image image) {
        return y >= 0 && y < image.getHeight() && x >= 0 && x < image.getWidth();
    }
}
