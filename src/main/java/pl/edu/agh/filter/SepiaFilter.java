package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;

/**
 * Created by Lukasz on 21.05.2017.
 */
public class SepiaFilter implements Filter {
    @Override
    public void process(Image image) {
        for(int i = 0 ; i < image.getWidth(); i++){
            for(int j = 0 ; j < image.getHeight(); j++){
                int red = (int) (image.getRedPixel(i, j) * 0.393 + image.getGreenPixel(i, j) * 0.769 + image.getBluePixel(i, j) * 0.189);
                int green = (int) (image.getRedPixel(i, j) * 0.349 + image.getGreenPixel(i, j) * 0.686 + image.getBluePixel(i, j) * 0.168);
                int blue = (int) (image.getRedPixel(i, j) * 0.272 + image.getGreenPixel(i, j) * 0.534 + image.getBluePixel(i, j) * 0.131);
                if(red > 255) red = 255;
                if(green > 255) green = 255;
                if(blue > 255) blue = 255;
                image.setRed(i, j, red);
                image.setGreen(i, j, green);
                image.setBlue(i, j, blue);
            }
        }
    }
}
