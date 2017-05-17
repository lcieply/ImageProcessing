package pl.edu.agh.morphology;

import pl.edu.agh.binary.BinaryColor;
import pl.edu.agh.image.Image;
import pl.edu.agh.morphology.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

import static pl.edu.agh.util.ImageUtils.saveOutput;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class BinaryErodeTransformation implements Transformation {

    private int radius;
    private BinaryColor foregroundColor;

    public BinaryErodeTransformation(int radius, BinaryColor foregroundColor) {
        this.radius = radius;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void process(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int actualValue = foregroundColor.getValue();  //BLACK - 0, WHITE - 255
        int reverseValue = (actualValue == 255) ? 0 : 255;

        int output[] = new int[imageWidth * imageHeight];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                if(image.getRedPixel(x, y) == actualValue){
                    if(isPixelWithSelectedValueInMask(x, y, reverseValue, image)){
                        output[x+y*imageWidth] = reverseValue;
                    } else {
                        output[x+y*imageWidth] = actualValue;
                    }
                }else{
                    output[x+y*imageWidth] = reverseValue;
                }
            }
        }

        saveOutput(output, image);
    }

    private boolean isPixelWithSelectedValueInMask(int x, int y, int targetValue, Image image) {
        for(int maskY = y - radius; maskY <= y + radius; maskY++){
            for(int maskX = x - radius; maskX <= x + radius; maskX++){
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image) && isPixelInsideCircle(maskX, maskY, x, y)) {
                    if(image.getRedPixel(maskX, maskY) == targetValue){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isPixelInsideCircle(int x, int y, double centerX, double centerY) {
        return Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2) < Math.pow(radius, 2);
    }
}
