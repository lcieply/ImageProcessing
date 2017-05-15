package pl.edu.agh.image;

import java.io.IOException;

/**
 * Created by Kamil Jureczka on 2017-05-09.
 */
public interface Image {
    int getWidth();
    int getHeight();
    void setPixel(int x, int y, int alpha, int red, int green, int blue);
    int getPixel(int x, int y);
    void setRed(int x, int y, int red);
    void setGreen(int x, int y, int green);
    void setBlue(int x, int y, int blue);
    int getRedPixel(int x, int y);
    int getGreenPixel(int x, int y);
    int getBluePixel(int x, int y);
    void writeImage(String filePath) throws IOException;
}
