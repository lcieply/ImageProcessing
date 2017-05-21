package pl.edu.agh;

import pl.edu.agh.binary.BinaryColor;
import pl.edu.agh.filter.*;
import pl.edu.agh.filter.interfaces.Filter;
import pl.edu.agh.image.Image;
import pl.edu.agh.image.ImageImpl;
import pl.edu.agh.morphology.*;
import pl.edu.agh.morphology.interfaces.Transformation;
import pl.edu.agh.util.ImageUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Kamil Jureczka on 2017-05-15.
 */
public class ExampleMain {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ExampleMain.class.getClassLoader();
        String binaryPath = classLoader.getResource("binary.bmp").getPath();
        String monochromPath = classLoader.getResource("monochrom.jpg").getPath();
        String rgbPath = classLoader.getResource("rgb.bmp").getPath();
        String convexPath = classLoader.getResource("convexHull.jpg").getPath();

        Image binaryImage = new ImageImpl(binaryPath);
        Image monochromeImage = new ImageImpl(monochromPath);
        Image rgbImage = new ImageImpl(rgbPath);
        Image convexHullImage = new ImageImpl(convexPath);

        System.out.println("Trwa probne przetwarzanie obrazow...");

        //===============FILTRATIONS======================

        //==========SOBEL========
        Filter rgbSobelFilter = new RGBSobelFilter(SobelFilterDirection.BOTH);
        Image tmpRgbImage = ImageUtils.copyOf(rgbImage);
        rgbSobelFilter.process(tmpRgbImage);
        tmpRgbImage.writeImage(getOutputDirectory(rgbPath, "rgbSobel"));
        Filter sobelFilter = new SobelFilter(SobelFilterDirection.BOTH);
        Image tmpMonochromeImage = ImageUtils.copyOf(monochromeImage);
        sobelFilter.process(tmpMonochromeImage);
        tmpMonochromeImage.writeImage(getOutputDirectory(monochromPath, "sobel"));

        //==========KIRSCH========
        KirschFilter kirschFilter = new KirschFilter(KirschFilterDirection.ALL);
        Image kirschFilterImage = ImageUtils.copyOf(rgbImage);
        kirschFilter.process(kirschFilterImage);
        kirschFilterImage.writeImage(getOutputDirectory(rgbPath, "rgbKirsch"));

        //================MORPHOLOGY=======================

        Transformation binaryOpenMorphologyTransformation = new BinaryOpenMorphologyTransformation(5, BinaryColor.WHITE);
        Image tmpBinaryImage = ImageUtils.copyOf(binaryImage);
        binaryOpenMorphologyTransformation.process(tmpBinaryImage);
        tmpBinaryImage.writeImage(getOutputDirectory(binaryPath, "binaryOpen"));

        Transformation grayscaleCloseMorphologyTransformation = new GrayscaleCloseMorphologyTransformation(5);
        Image tmpMonochromeImage2 = ImageUtils.copyOf(monochromeImage);
        grayscaleCloseMorphologyTransformation.process(tmpMonochromeImage2);
        tmpMonochromeImage2.writeImage(getOutputDirectory(binaryPath, "monochromeClose"));


        //================AFFINE=======================
        //==========ROTATE========
        try {
            Transformation affineTransformation = new AffineTransformation(AffineTransformOption.ROTATE, Arrays.asList(45.0));
            Image affineTransformImage = ImageUtils.copyOf(rgbImage);
            affineTransformation.process(affineTransformImage);
            affineTransformImage.writeImage(getOutputDirectory(rgbPath, "rgbAffineRotate"));
        } catch (ParamException e) {
            System.out.println("Parameters list exception!");
        }
        //==========SCALE========
        try {
            Transformation affineTransformation = new AffineTransformation(AffineTransformOption.SCALE, Arrays.asList(0.5, 0.5));
            Image affineTransformImage = ImageUtils.copyOf(rgbImage);
            affineTransformation.process(affineTransformImage);
            affineTransformImage.writeImage(getOutputDirectory(rgbPath, "rgbAffineScale"));
        } catch (ParamException e) {
            System.out.println("Parameters list exception!");
        }
        //==========SHEAR_X========
        try {
            Transformation affineTransformation = new AffineTransformation(AffineTransformOption.SHEAR_X, Arrays.asList(45.0));
            Image affineTransformImage = ImageUtils.copyOf(rgbImage);
            affineTransformation.process(affineTransformImage);
            affineTransformImage.writeImage(getOutputDirectory(rgbPath, "rgbAffineShearX"));
        } catch (ParamException e) {
            System.out.println("Parameters list exception!");
        }
        //==========SHEAR_Y========
        try {
            Transformation affineTransformation = new AffineTransformation(AffineTransformOption.SHEAR_Y, Arrays.asList(45.0));
            Image affineTransformImage = ImageUtils.copyOf(rgbImage);
            affineTransformation.process(affineTransformImage);
            affineTransformImage.writeImage(getOutputDirectory(rgbPath, "rgbAffineShearY"));
        } catch (ParamException e) {
            System.out.println("Parameters list exception!");
        }
        //==========TRANSLATE========
        try {
            Transformation affineTransformation = new AffineTransformation(AffineTransformOption.TRANSLATE, Arrays.asList(250.0, 250.0));
            Image affineTransformImage = ImageUtils.copyOf(rgbImage);
            affineTransformation.process(affineTransformImage);
            affineTransformImage.writeImage(getOutputDirectory(rgbPath, "rgbAffineTranslate"));
        } catch (ParamException e) {
            System.out.println("Parameters list exception!");
        }
        System.out.println("Przetwarzanie obrazow zakonczone!");

        //================CONVEX_HULL=======================
        Transformation convexHullTransformation = new ConvexHullTransformation();
        Image tmpConvexHullImage = ImageUtils.copyOf(convexHullImage);
        convexHullTransformation.process(tmpConvexHullImage);
        tmpConvexHullImage.writeImage(getOutputDirectory(convexPath, ""));

    }

    private static String getOutputDirectory(String inputDirectory, String additionalInfo) {
        String fileExtension = inputDirectory.substring(inputDirectory.lastIndexOf('.'));
        String filePath = inputDirectory.substring(0, (inputDirectory.lastIndexOf('.')));
        return filePath+"_"+additionalInfo+"_output" + fileExtension;
    }
}
