package pl.edu.agh;

import pl.edu.agh.binary.BinaryColor;
import pl.edu.agh.filter.RGBSobelFilter;
import pl.edu.agh.filter.SobelFilter;
import pl.edu.agh.filter.SobelFilterDirection;
import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;
import pl.edu.agh.image.ImageImpl;
import pl.edu.agh.morphology.BinaryOpenMorphologyTransformation;
import pl.edu.agh.morphology.GrayscaleCloseMorphologyTransformation;
import pl.edu.agh.morphology.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

import java.io.IOException;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class ExampleMain {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ExampleMain.class.getClassLoader();
        String binaryPath = classLoader.getResource("binary.bmp").getPath();
        String monochromPath = classLoader.getResource("monochrom.jpg").getPath();
        String rgbPath = classLoader.getResource("rgb.bmp").getPath();

        Image binaryImage = new ImageImpl(binaryPath);
        Image monochromeImage = new ImageImpl(monochromPath);
        Image rgbImage = new ImageImpl(rgbPath);

        System.out.println("Trwa probne przetwarzanie obrazow...");

        //===============FILTRATIONS======================

        Filter rgbSobelFilter = new RGBSobelFilter(SobelFilterDirection.BOTH);
        Image tmpRgbImage = ImageUtils.copyOf(rgbImage);
        rgbSobelFilter.process(tmpRgbImage);
        tmpRgbImage.writeImage(getOutputDirectory(rgbPath, "rgbSobel"));

        Filter sobelFilter = new SobelFilter(SobelFilterDirection.BOTH);
        Image tmpMonochromeImage = ImageUtils.copyOf(monochromeImage);
        sobelFilter.process(tmpMonochromeImage);
        tmpMonochromeImage.writeImage(getOutputDirectory(monochromPath, "sobel"));

        //================MORPHOLOGY=======================

        Transformation binaryOpenMorphologyTransformation = new BinaryOpenMorphologyTransformation(5, BinaryColor.WHITE);
        Image tmpBinaryImage = ImageUtils.copyOf(binaryImage);
        binaryOpenMorphologyTransformation.process(tmpBinaryImage);
        tmpBinaryImage.writeImage(getOutputDirectory(binaryPath, "binaryOpen"));

        Transformation grayscaleCloseMorphologyTransformation = new GrayscaleCloseMorphologyTransformation(5);
        Image tmpMonochromeImage2 = ImageUtils.copyOf(monochromeImage);
        grayscaleCloseMorphologyTransformation.process(tmpMonochromeImage2);
        tmpMonochromeImage2.writeImage(getOutputDirectory(binaryPath, "monochromeClose"));

        System.out.println("Przetwarzanie obrazow zakonczone!");
    }

    private static String getOutputDirectory(String inputDirectory, String additionalInfo) {
        String fileExtension = inputDirectory.substring(inputDirectory.lastIndexOf('.'));
        String filePath = inputDirectory.substring(0, (inputDirectory.lastIndexOf('.')));
        return filePath+"_"+additionalInfo+"_output" + fileExtension;
    }
}
