package pl.edu.agh.filter;

import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;

import java.awt.*;

/**
 * Created by Lukasz on 22.05.2017.
 */
public class PastelFilter implements Filter{
    private int hueLevelCount = 6;
    private int satLevelCount = 7;
    private int valLevelCount = 4;
    private double[] hueLevel = new double[]{0.0, 80.0, 160.0, 240.0, 320.0, 360.0};
    private double[] satLevel = new double[]{0.0, 0.15, 0.3, 0.45, 0.6, 0.8, 1.0};
    private double[] valLevel = new double[]{0.0, 0.3, 0.6, 1.0};
    private double max(double r, double g, double b){
        return Math.max(Math.max(r, g), b);
    }
    private double min(double r, double g, double b){
        return Math.min(Math.min(r, g), b);
    }
    private double nearestLevel(double val, int mode){

        int levCount = 0;
        if(mode==0) levCount = hueLevelCount;
        if(mode==1) levCount = satLevelCount;
        if(mode==2) levCount = valLevelCount;
        for(int i = 0; i<levCount-1; i++){
            if(mode == 0){
                if(val >= hueLevel[i] && val <= hueLevel[i+1]){
                    return hueLevel[i+1];
                }
            }
            if(mode == 1){
                if(val >= satLevel[i] && val <= satLevel[i+1]){
                    return satLevel[i+1];
                }
            }
            if(mode == 2){
                if(val >= valLevel[i] && val <= valLevel[i+1]){
                    return valLevel[i+1];
                }
            }
        }
        return 0;
    }
    private int avarageIntensity(Image img, int x, int y){
        return (img.getRedPixel(x, y) + img.getGreenPixel(x ,y) + img.getBluePixel(x, y))/3;
    }
    private double clamp(double x, double min, double max){
        if(x < min) x = min;
        else if(x > max) x = max;
        return x;
    }
    private Color hsvToRgb(double hue, double sat, double val){
        double C = val*sat;
        double X = C * (1.0-Math.abs((hue/60.0)%2-1));
        double m = val - C;
        double r=0, g=0, b=0;
        if(hue >= 0 && hue < 60.0){
            r = C;
            g = X;
            b = 0;
        }
        else if(hue >= 60.0 && hue < 120.0) {
            r = X;
            g = C;
            b = 0;
        }
        else if(hue >= 120.0 && hue < 180.0) {
            r = 0;
            g = C;
            b = X;
        }
        else if(hue >= 180.0 && hue < 240.0) {
            r = 0;
            g = X;
            b = C;
        }
        else if(hue >= 240.0 && hue < 300.0) {
            r = X;
            g = 0;
            b = C;
        }
        else if(hue >= 300.0 && hue < 360.0) {
            r = C;
            g = 0;
            b = X;
        }
        int red = (int)((r+m)*255);
        int green = (int)((g+m)*255);
        int blue = (int)((b+m)*255);
        return new Color(red, green, blue);
    }
    @Override
    public void process(Image image) {
        for(int i = 0 ; i < image.getWidth(); i++){
            for(int j = 0 ; j < image.getHeight(); j++){
                double r = image.getRedPixel(i, j)/255.0;
                double g = image.getGreenPixel(i, j)/255.0;
                double b = image.getBluePixel(i, j)/255.0;
                double cMax = max(r, g, b);
                double cMin = min(r, g, b);
                double delta = cMax - cMin;
                double hue = 0.0;
                if(delta == 0.0) hue = 0.0;
                else if(cMax == r) hue = 60.0*(((g-b)/delta) % 6.0);
                else if(cMax == g) hue = 60.0*((b-r)/delta + 2.0);
                else if(cMax == b) hue = 60.0*((r-g)/delta + 4.0);
                double saturation = (cMax==0) ? 0: delta/cMax;
                double value = cMax;
                double[] vHSV = new double[]{nearestLevel(hue, 0), nearestLevel(saturation, 1), nearestLevel(value, 2)};
                Color color = hsvToRgb(vHSV[0], vHSV[1], vHSV[2]);
                image.setRed(i, j, color.getRed());
                image.setGreen(i, j, color.getGreen());
                image.setBlue(i, j, color.getBlue());
            }
        }
    }
}
