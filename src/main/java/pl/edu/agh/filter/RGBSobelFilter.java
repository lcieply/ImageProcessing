package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;
import pl.edu.agh.util.ImageUtils;

/**
 * Created by Kamil Jureczka on 2017-05-17.
 */
public class RGBSobelFilter implements Filter {

    private final int maskHorizontal[][] =  {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    private final int maskVertical[][] = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};

    private final SobelFilterDirection direction;
    private int mask[][];

    public RGBSobelFilter(SobelFilterDirection direction) {
        this.direction = direction;
    }

    @Override
    public void process(Image image) {
        if(direction.equals(SobelFilterDirection.VERTICAL)) {
            mask = maskVertical;
            sobelRGBTransformation(image);
        } else if(direction.equals(SobelFilterDirection.HORIZONTAL)) {
            mask = maskHorizontal;
            sobelRGBTransformation(image);
        } else{
            sobelRGBTransformation(image);
        }
    }

    private void sobelRGBTransformation(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[][] = new int[imageWidth * imageHeight][3];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                int newPixelValues[];
                if(direction.equals(SobelFilterDirection.BOTH)){
                    newPixelValues = calculateBothNewPixelRGBValues(x, y, image);
                } else {
                    newPixelValues = calculateNewPixelRGBValues(x, y, image);
                }
                output[x+y*imageWidth][0] = newPixelValues[0];
                output[x+y*imageWidth][1] = newPixelValues[1];
                output[x+y*imageWidth][2] = newPixelValues[2];
            }
        }

        ImageUtils.saveOutputRGB(output, image);
    }

    private int[] calculateBothNewPixelRGBValues(int x, int y, Image image) {
        int result[] = new int[3];
        int newValueX[] = new int[3];
        int newValueY[] = new int[3];

        for(int maskY = y - 1, j = 0; maskY <= y+1; ++maskY, ++j){
            for(int maskX = x - 1, i = 0; maskX <= x+1; ++maskX, ++i) {
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image)) {
                    int redPixelX = image.getRedPixel(maskX, maskY) * maskVertical[j][i];
                    int greenPixelX = image.getGreenPixel(maskX, maskY) * maskVertical[j][i];
                    int bluePixelX = image.getBluePixel(maskX, maskY) * maskVertical[j][i];

                    int redPixelY = image.getRedPixel(maskX, maskY) * maskHorizontal[j][i];
                    int greenPixelY = image.getGreenPixel(maskX, maskY) * maskHorizontal[j][i];
                    int bluePixelY = image.getBluePixel(maskX, maskY) * maskHorizontal[j][i];

                    newValueX[0] += redPixelX;
                    newValueX[1] += greenPixelX;
                    newValueX[2] += bluePixelX;

                    newValueY[0] += redPixelY;
                    newValueY[1] += greenPixelY;
                    newValueY[2] += bluePixelY;
                } else {
                    result[0] = 0;
                    result[1] = 0;
                    result[2] = 0;

                    return result;
                }
            }
        }

        result[0] = checkValue((int)Math.sqrt(Math.pow(newValueX[0],2)+Math.pow(newValueY[0],2)));
        result[1] = checkValue((int)Math.sqrt(Math.pow(newValueX[1],2)+Math.pow(newValueY[1],2)));
        result[2] = checkValue((int)Math.sqrt(Math.pow(newValueX[2],2)+Math.pow(newValueY[2],2)));

        return result;
    }

    private int checkValue(int pixel) {
        if (pixel > 255) {
            return 255;
        } else if(pixel < 0) {
            return 0;
        }

        return pixel;
    }

    private int[] calculateNewPixelRGBValues(int x, int y, Image image) {
        int newValue[] = new int[3];

        for(int maskY = y - 1, j = 0; maskY <= y+1; ++maskY, ++j){
            for(int maskX = x - 1, i = 0; maskX <= x+1; ++maskX, ++i) {
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image)) {
                    int redPixel = image.getRedPixel(maskX, maskY) * mask[j][i];
                    int greenPixel = image.getGreenPixel(maskX, maskY) * mask[j][i];
                    int bluePixel = image.getBluePixel(maskX, maskY) * mask[j][i];

                    newValue[0] += redPixel;
                    newValue[1] += greenPixel;
                    newValue[2] += bluePixel;
                } else {
                    newValue[0] = 0;
                    newValue[1] = 0;
                    newValue[2] = 0;

                    return newValue;
                }
            }
        }

        newValue[0] = checkValue(newValue[0]);
        newValue[1] = checkValue(newValue[1]);
        newValue[2] = checkValue(newValue[2]);

        return newValue;
    }
}
