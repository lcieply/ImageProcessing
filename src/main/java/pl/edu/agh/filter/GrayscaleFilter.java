package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;

/**
 * Created by Lukasz on 21.05.2017.
 */
public class GrayscaleFilter implements Filter{

    @Override
    public void process(Image image) {
        for(int i = 0 ; i < image.getWidth(); i++){
            for(int j = 0 ; j < image.getHeight(); j++){
                int gray = (int) (image.getRedPixel(i, j) * 0.2989 + image.getGreenPixel(i, j) * 0.5870 + image.getBluePixel(i, j) * 0.1140);
                image.setRed(i, j, gray);
                image.setGreen(i, j, gray);
                image.setBlue(i, j, gray);
            }
        }
    }
}
