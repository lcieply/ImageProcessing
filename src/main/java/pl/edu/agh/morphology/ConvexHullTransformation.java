package pl.edu.agh.morphology;

import pl.edu.agh.image.Image;
import pl.edu.agh.image.ImageImpl;
import pl.edu.agh.morphology.interfaces.Transformation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by Lukasz on 21.05.2017.
 */
public class ConvexHullTransformation implements Transformation{
    private int[][] rotate3X3Mask45(int[][] mask) {
        int[][] resultMask = new int[][]{{mask[1][0], mask[0][0], mask[0][1]}, {mask[2][0], mask[1][1], mask[0][2]}, {mask[2][1], mask[2][2], mask[1][2]}};
        return resultMask;
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    private boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }
    private boolean convexHullOperator(BufferedImage img, int x, int y, int[][] mask, int imageWidth, int imageHeight){
        for(int i =  0, image_i = x - 1; i < 3; i++, image_i++){
            for(int j = 0, image_j = y - 1; j < 3; j++, image_j++){
                int good_i = image_i, good_j = image_j;
                if(image_i < 0 || image_i >= imageWidth) good_i = x;
                if(image_j < 0 || image_j >= imageHeight) good_j = y;
                Color fragmentColor = new Color(img.getRGB(good_i, good_j));
                if(mask[i][j] != 0)
                    if((mask[i][j] == 1 && fragmentColor.equals(Color.BLACK)) || (mask[i][j] == -1 && fragmentColor.equals(Color.WHITE)) || (mask[i][j] == 1 && !fragmentColor.equals(Color.WHITE))) return false;
            }
        }
        return true;
    }
    private void copyPixels(BufferedImage img, Image image){
        for(int i = 0 ; i < img.getWidth(); i++){
            for(int j = 0 ; j < img.getHeight(); j++){
                Color color = new Color(image.getPixel(i,j));
                img.setRGB(i,j, color.getRGB());
            }
        }
    }
    @Override
    public void process(Image img){
        BufferedImage imgOut = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        copyPixels(imgOut, img);
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage copy = null;
        int[][] mask = {{1, 1, 0}, {1, -1, 0}, {1, 0, -1}};
        do {
            for (int k = 0; k < 8; k++) {
                copy = deepCopy(imgOut);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        if(convexHullOperator(imgOut, i, j, mask, width, height)){
                            imgOut.setRGB(i, j, Color.WHITE.getRGB());
                        }
                    }
                }
                mask = rotate3X3Mask45(mask);
            }
        }while(!compareImages(imgOut, copy));
        img.setImage(imgOut);
    }

}
