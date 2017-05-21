package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.*;
import pl.edu.agh.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Lukasz on 21.05.2017.
 */
public class KirschFilter{
    private KirschFilterDirection direction;
    public KirschFilter(KirschFilterDirection direction){
        this.direction = direction;
    }
    private int[][] rotate3X3Mask45(int[][] mask){
        int[][] resultMask = {{mask[1][0], mask[0][0], mask [0][1]}, {mask[2][0], mask[1][1], mask[0][2]}, {mask[2][1], mask[2][2], mask[1][2]}};
        return resultMask;
    }
    private Color kirschOperator(Image img, int x, int y, int[][] mask, int imageWidth, int imageHeight){
        int red = 0, green = 0, blue = 0;
        for(int i =  0, image_i = x - 1; i < 3; i++, image_i++){
            for(int j = 0, image_j = y - 1; j < 3; j++, image_j++){
                int good_i = image_i, good_j = image_j;
                if(image_i < 0 ) good_i = (image_i+1)*(-1);
                if(image_j < 0 ) good_j = (image_j+1)*(-1);
                if(image_i >= imageWidth) good_i = 2*imageWidth-2 - (image_i) + 1;
                if(image_j >= imageHeight) good_j = 2*imageHeight-2 - (image_j) + 1;
                Color fragmentColor = new Color(img.getPixel(good_i, good_j));

                red += fragmentColor.getRed() * mask[i][j];
                green += fragmentColor.getGreen() * mask[i][j];
                blue += fragmentColor.getBlue() * mask[i][j];
            }
        }
        if (red > 255) red = 255;
        if (green > 255) green = 255;
        if (blue > 255) blue = 255;
        if(red < 0) red = 0;
        if(green < 0) green = 0;
        if(blue < 0) blue = 0;
        return new Color(red, green, blue);
    }

    private void transformAllDirection(Image img){
        BufferedImage imgOut = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = 0 ; i < width; i++){
            for(int j = 0 ; j < height; j++){
                int[][] mask = {{5, 5, 5}, {-3, 0, -3}, {-3, -3, -3}};
                int maxRed = 0, maxGreen = 0, maxBlue = 0;
                for(int k = 0 ; k < 8; k++){
                    Color temporaryColor = kirschOperator(img, i, j, mask, width, height);
                    if(temporaryColor.getRed() > maxRed) maxRed = temporaryColor.getRed();
                    if(temporaryColor.getGreen() > maxGreen) maxGreen = temporaryColor.getGreen();
                    if(temporaryColor.getBlue() > maxBlue) maxBlue = temporaryColor.getBlue();
                    mask = rotate3X3Mask45(mask);
                }
                imgOut.setRGB(i, j, new Color(maxRed, maxGreen, maxBlue).getRGB());
            }
        }
        copyPixels(imgOut, img);
    }
    private void copyPixels(BufferedImage img, Image image){
        for(int i = 0 ; i < img.getWidth(); i++){
            for(int j = 0 ; j < img.getHeight(); j++){
                Color color = new Color(img.getRGB(i,j));
                image.setRed(i, j, color.getRed());
                image.setGreen(i, j, color.getGreen());
                image.setBlue(i, j, color.getBlue());
            }
        }
    }
    private void  transformInDirection(Image img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = 0 ; i < width; i++){
            for(int j = 0 ; j < height; j++){
                int[][] mask = this.direction.getMask();
                Color temporaryColor = kirschOperator(img, i, j, mask, width, height);
                img.setPixel(i, j, 255, temporaryColor.getRed(), temporaryColor.getGreen(), temporaryColor.getBlue());
            }
        }
    }
    public void process(Image image) {
        if(direction.equals(KirschFilterDirection.ALL)) transformAllDirection(image);
        else transformInDirection(image);
    }
}
