package pl.edu.agh.morphology;

/**
 * Created by Lukasz on 21.05.2017.
 */

import pl.edu.agh.image.*;
import pl.edu.agh.image.Image;
import pl.edu.agh.morphology.interfaces.Transformation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class AffineTransformation implements Transformation{
    private double[][] transformMatrix;
    private AffineTransformOption transformOption;
    private List<Double> param;
    private void createTransformMatrix(){
        this.transformMatrix = new double[2][3];
        if(transformOption.equals(AffineTransformOption.ROTATE)){
            transformMatrix[0][0] = Math.cos(param.get(0));
            transformMatrix[0][1] = Math.sin(param.get(0));
            transformMatrix[0][2] = 0;
            transformMatrix[1][0] = -Math.sin(param.get(0));
            transformMatrix[1][1] = Math.cos(param.get(0));
            transformMatrix[1][2] = 0;
        }else if(transformOption.equals(AffineTransformOption.SHEAR_X)){
            transformMatrix[0][0] = 1;
            transformMatrix[0][1] = Math.tan(param.get(0));
            transformMatrix[0][2] = 0;
            transformMatrix[1][0] = 0;
            transformMatrix[1][1] = 1;
            transformMatrix[1][2] = 0;
        }else if(transformOption.equals(AffineTransformOption.SHEAR_Y)){
            transformMatrix[0][0] = 1;
            transformMatrix[0][1] = 0;
            transformMatrix[0][2] = 0;
            transformMatrix[1][0] = Math.tan(param.get(0));
            transformMatrix[1][1] = 1;
            transformMatrix[1][2] = 0;
        }
        else if(transformOption.equals(AffineTransformOption.SCALE)){
            transformMatrix[0][0] = param.get(0);
            transformMatrix[0][1] = 0;
            transformMatrix[0][2] = 0;
            transformMatrix[1][0] = 0;
            transformMatrix[1][1] = param.get(1);
            transformMatrix[1][2] = 0;
        }
        else if(transformOption.equals(AffineTransformOption.TRANSLATE)){
            transformMatrix[0][0] = 1;
            transformMatrix[0][1] = 0;
            transformMatrix[0][2] = param.get(0);
            transformMatrix[1][0] = 0;
            transformMatrix[1][1] = 1;
            transformMatrix[1][2] = param.get(1);
        }
    }
    public AffineTransformation(AffineTransformOption transformOption, List<Double> param)throws ParamException{
        if(param.size() >2 || param.size() < 1) throw new ParamException();
        if(param.size() == 1 && (!transformOption.equals(AffineTransformOption.ROTATE)  && !transformOption.equals(AffineTransformOption.SHEAR_X) && !transformOption.equals(AffineTransformOption.SHEAR_Y))) throw new ParamException();
        if(param.size() == 2 && ((!transformOption.equals(AffineTransformOption.SCALE)) && (!transformOption.equals(AffineTransformOption.TRANSLATE)))) throw new ParamException();
        this.transformOption = transformOption;
        this.param = param;
        createTransformMatrix();
    }
    public void setParam(List param) throws ParamException{
        if(param.size() >2 || param.size() < 1) throw new ParamException();
        if(param.size() != 1 && (!transformOption.equals(AffineTransformOption.ROTATE)  || !transformOption.equals(AffineTransformOption.SHEAR_X)  || !transformOption.equals(AffineTransformOption.SHEAR_Y))) throw new ParamException();
        if(param.size() != 2 && ((!transformOption.equals(AffineTransformOption.SCALE)) || (!transformOption.equals(AffineTransformOption.TRANSLATE)))) throw new ParamException();
        this.param = param;
        createTransformMatrix();
    }
    private int[] simulateDimension(Image img, double[][] matrix){
        int minX=0, minY=0, maxX=0, maxY=0;
        for(int i = 0 ; i < img.getWidth(); i++){
            for(int j = 0 ; j < img.getHeight(); j++){
                int x = Math.toIntExact(Math.round(matrix[0][0]*i+matrix[0][1]*j+matrix[0][2]));
                int y = Math.toIntExact(Math.round(matrix[1][0]*i+matrix[1][1]*j+matrix[1][2]));
                if(x < minX) minX = x;
                if(x > maxX) maxX = x;
                if(y < minY) minY = y;
                if(y > maxY) maxY = y;
            }
        }
        int[] result = new int[]{maxX-minX, maxY-minY, Math.abs(minX), Math.abs(minY)};
        return result;
    }

    private Color nearestNeighbourOperator(BufferedImage img, int x, int y){
        if(x==img.getWidth()-1)
            return new Color(img.getRGB(x-1, y));
        else
            return new Color(img.getRGB(x+1, y));

    }
    private void nearestNeighbour(BufferedImage img, boolean[][] pixelChanged){
        for(int i = 0 ; i < img.getWidth(); i++){
            for(int j = 0 ; j < img.getHeight(); j++){
                if(!pixelChanged[i][j])
                    img.setRGB(i, j, nearestNeighbourOperator(img, i, j).getRGB());
            }
        }
    }

    @Override
    public void process(Image img){
        int[] info = simulateDimension(img, transformMatrix);
        int width = info[0];
        int height = info[1];
        BufferedImage imgOut = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_RGB);
        boolean[][] pixelChanged = new boolean[imgOut.getWidth()][imgOut.getHeight()];
        for(int i = 0 ; i < img.getWidth(); i++){
            for(int j = 0 ; j < img.getHeight(); j++){
                int x = Math.toIntExact(Math.round(transformMatrix[0][0]*i+transformMatrix[0][1]*j+transformMatrix[0][2])) + info[2];
                int y = Math.toIntExact(Math.round(transformMatrix[1][0]*i+transformMatrix[1][1]*j+transformMatrix[1][2])) + info[3];
                imgOut.setRGB(x, y, img.getPixel(i, j));
                pixelChanged[x][y] = true;
            }
        }
        nearestNeighbour(imgOut, pixelChanged);
        img.setImage(imgOut);
    }
}