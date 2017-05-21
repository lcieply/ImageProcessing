package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;

/**
 * Created by Lukasz on 21.05.2017.
 */
public class RGB_CMY_Filter implements Filter{
    @Override
    public void process(Image image) {
        for(int i = 0 ; i < image.getWidth(); i++){
            for(int j = 0 ; j < image.getHeight(); j++){
                image.setRed(i, j, 255-image.getRedPixel(i ,j));
                image.setGreen(i, j, 255-image.getGreenPixel(i ,j));
                image.setBlue(i, j, 255-image.getBluePixel(i ,j));
            }
        }
    }
}
