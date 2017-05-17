package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;
import pl.edu.agh.util.ImageUtils;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class SobelFilter implements Filter {

    private final int maskHorizontal[][] =  {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    private final int maskVertical[][] = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};

    private final SobelFilterDirection direction;
    private int mask[][];

    public SobelFilter(SobelFilterDirection direction) {
        this.direction = direction;
    }

    @Override
    public void process(Image image) {
        if(direction.equals(SobelFilterDirection.VERTICAL)) {
            mask = maskVertical;
            sobelTransformation(image);
        } else if(direction.equals(SobelFilterDirection.HORIZONTAL)) {
            mask = maskHorizontal;
            sobelTransformation(image);
        } else{
            sobelTransformation(image);
        }
    }

    private void sobelTransformation(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int output[] = new int[imageWidth * imageHeight];
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                if(direction.equals(SobelFilterDirection.BOTH)){
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

    private int checkValue(int pixel) {
        if (pixel > 255) {
            return 255;
        } else if(pixel < 0) {
            return 0;
        }

        return pixel;
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

