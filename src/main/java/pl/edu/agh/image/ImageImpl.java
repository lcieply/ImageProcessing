package pl.edu.agh.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kamil Jureczka on 2017-05-09.
 */
public class ImageImpl implements Image {

    private BufferedImage image;
    private int pixels[];

    public ImageImpl(String fileName) throws IOException {
        this.image = ImageIO.read(new File(fileName));
        this.pixels = new int[image.getWidth()*image.getHeight()];
        initPixels();
    }

    private void initPixels(){
        PixelGrabber pg = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        try{
            pg.grabPixels();
        }catch(InterruptedException e){
            System.out.println("Error Occurred: "+e);
        }
    }

    @Override
    public int getRedPixel(int x, int y){
        return (pixels[x+(y*image.getWidth())] >> 16) & 0xFF;
    }

    @Override
    public int getGreenPixel(int x, int y) {
        return (pixels[x+(y*image.getWidth())] >> 8) & 0xFF;
    }

    @Override
    public int getBluePixel(int x, int y) {
        return pixels[x+(y*image.getWidth())] & 0xFF;
    }

    @Override
    public void setPixel(int x, int y, int alpha, int red, int green, int blue) {
        pixels[x+(y*image.getWidth())] = (alpha<<24) | (red<<16) | (green<<8) | blue;
        updateImagePixel(x, y);
    }

    private void updateImagePixel(int x, int y){
        image.setRGB(x, y, pixels[x+(y*image.getWidth())]);
    }

    @Override
    public void setRed(int x, int y, int red){
        pixels[x+(y*image.getWidth())] = (red<<16) | (pixels[x+(y*image.getWidth())] & 0xFF00FFFF);
        updateImagePixel(x,y);
    }

    @Override
    public void setGreen(int x, int y, int green){
        pixels[x+(y*image.getWidth())] = (green<<8) | (pixels[x+(y*image.getWidth())] & 0xFFFF00FF);
        updateImagePixel(x,y);
    }

    @Override
    public void setBlue(int x, int y, int blue){
        pixels[x+(y*image.getWidth())] = blue | (pixels[x+(y*image.getWidth())] & 0xFFFFFF00);
        updateImagePixel(x,y);
    }

    @Override
    public int getPixel(int x, int y) {
        return pixels[x+(y*image.getWidth())];
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void writeImage(String filePath) throws IOException {
        File file = new File(filePath);
        String fileType = filePath.substring(filePath.lastIndexOf('.')+1);
        ImageIO.write(image, fileType, file);
    }
}
