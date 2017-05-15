package pl.edu.agh.filter;

import pl.edu.agh.image.Image;
import pl.edu.agh.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class SobelTransformation implements Transformation {

    private final boolean isRGB;
    private final int direction;
    private int mask[][];

    private final int maskHorizontal[][] =  {{1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}};

    private final int maskVertical[][] = {{1, 0, -1},
            {2, 0, -2},
            {1, 0, -1}};

    public SobelTransformation(int direction) {
        this.direction = direction;
        this.isRGB = false;
    }

    public SobelTransformation(int direction, boolean isRGB) {
        this.direction = direction;
        this.isRGB = isRGB;
    }

    public void transform(Image image) {
        if(direction == 1) {
            mask = maskVertical;
            runSobelTransformation(image);
        } else if(direction == 2) {
            mask = maskHorizontal;
            runSobelTransformation(image);
        } else{
            runSobelTransformation(image);
        }
    }

    private void runSobelTransformation(Image image) {
        if(isRGB) {
            sobelRGBTransformation(image);
        } else {
            sobelTransformation(image);
        }
    }

    private void sobelRGBTransformation(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[][] = new int[imageWidth * imageHeight][3];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                int newPixelValues[];
                if(direction == 3){
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

    private int checkValue(int pixel) {
        if (pixel > 255) {
            return 255;
        } else if(pixel < 0) {
            return 0;
        }

        return pixel;
    }

    private void sobelTransformation(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[] = new int[imageWidth * imageHeight];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                if(direction == 3){
                    output[x+y*imageWidth] = calculateBothNewPixelValue(x, y, image);
                } else {
                    output[x+y*imageWidth] = calculateNewPixelValue(x, y, image);
                }

            }
        }

        ImageUtils.saveOutput(output, image);
    }

    private int calculateBothNewPixelValue(int x, int y, Image image) {
        int result;
        int newValueX = 0;
        int newValueY = 0;

        for(int maskY = y - 1, j = 0; maskY <= y+1; ++maskY, ++j){
            for(int maskX = x - 1, i = 0; maskX <= x+1; ++maskX, ++i) {
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image)) {
                    int newPixelX = image.getRedPixel(maskX, maskY) * maskVertical[i][j];
                    int newPixelY = image.getRedPixel(maskX, maskY) * maskHorizontal[i][j];

                    newValueX+=newPixelX;
                    newValueY+=newPixelY;
                } else {
                    return 0;
                }
            }
        }

        result = checkValue((int)Math.sqrt(Math.pow(newValueX,2)+Math.pow(newValueY,2)));
        return result;

    }

    private int calculateNewPixelValue(int x, int y, Image image) {
        int newValue = 0;

        for(int maskY = y - 1, j = 0; maskY <= y+1; ++maskY, ++j){
            for(int maskX = x - 1, i = 0; maskX <= x+1; ++maskX, ++i) {
                if(ImageUtils.isPixelInsideImage(maskX, maskY, image)) {
                    int newPixel = image.getRedPixel(maskX, maskY) * mask[i][j];

                    newValue+=newPixel;
                } else {
                    return 0;
                }
            }
        }

        newValue = checkValue(newValue);
        return newValue;
    }
}

