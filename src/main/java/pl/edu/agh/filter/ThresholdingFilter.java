package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;

/**
 * Created by Lukasz on 22.05.2017.
 */
public class ThresholdingFilter implements Filter{
    private int tresh;
    public ThresholdingFilter(int tresh){
        this.tresh = tresh;
    }

    @Override
    public void process(Image image) {
        for(int i = 0 ; i < image.getWidth(); i++){
            for(int j = 0 ; j < image.getHeight(); j++){
                double brightness = (0.2126*image.getRedPixel(i, j) + 0.7152*image.getBluePixel(i, j) + 0.0722*image.getBluePixel(i, j));
                if(brightness > tresh){
                    image.setBlue(i, j, 255);
                    image.setRed(i, j, 255);
                    image.setGreen(i, j, 255);
                }else{
                    image.setBlue(i, j, 0);
                    image.setRed(i, j, 0);
                    image.setGreen(i, j, 0);
                }
            }
        }
    }
}
