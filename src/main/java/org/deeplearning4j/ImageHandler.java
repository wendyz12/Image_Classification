package org.deeplearning4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Class ImageHandler to read, resize and write the files
 */

public class ImageHandler {
    /**
     * Read the image given input path
     * @param inputImagePath
     * @return
     */
    public BufferedImage read(String inputImagePath, String outputImagePath) {
        BufferedImage inputImage = null;
        try {
            File inputFile = new File(inputImagePath);
            inputImage = ImageIO.read(inputFile);
            double heightO = inputImage.getHeight();
            double widthO = inputImage.getWidth();
            double hwRatio = heightO/widthO;

            // creates output image
            BufferedImage outputImage = new BufferedImage(300, (int) (300*hwRatio),inputImage.getType());
            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, 300, (int)(300*hwRatio), null);
            g2d.dispose();
            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
            // writes to output file
            ImageIO.write(outputImage, formatName, new File(outputImagePath));

            System.out.println("image saved");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("error in reading file");
        }
        return inputImage;
    }

}
