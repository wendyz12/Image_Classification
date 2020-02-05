

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
//reference :https://www.codejava.net/java-se/graphics/how-to-resize-images-in-java
/**
 * 
 * Class ImageHandler to read, resize and write the files 
 *
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
			String formatName = inputImagePath.substring(inputImagePath
	                .lastIndexOf(".") + 1);
			//same the impage to project foler
			ImageIO.write(inputImage , formatName, new File(outputImagePath));
			System.out.println("image saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error in reading file");
		}
        return inputImage;
	}
	
	/**
	 * Resizes the image and writes it
	 * @param inputImage
	 * @param outputImagePath
	 * @param scaledWidth
	 * @param scaledHeight
	 */
	public void resize(BufferedImage inputImage,
            String outputImagePath, int scaledWidth, int scaledHeight) 
            {
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
        // writes to output file
        try {
			ImageIO.write(outputImage, formatName, new File(outputImagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error in resize file");
		}
        
	}
	/**
	 * Main function
	 * @param args
	 */
	 public static void main(String[] args) {
		 	ImageHandler image = new ImageHandler();
//		 	swingUI chooseImage = new swingUI();
//		 	SwingUtilities.invokeLater(new swingUI());
//		 	String inputImagePath = chooseImage.inputPath;
//		 	String outputImagePath = chooseImage.inputPath + "_resize";
	        String inputImagePath = "test.png";
	        String outputImagePath = "test_resize.png";
	        // resize to a fixed width (not proportional)
			int scaledWidth = 100;
			int scaledHeight = 100;
			//read the initial picture
			BufferedImage inputImage = image.read(inputImagePath);
			//resize the picture and save it in the output imagine path
			image.resize(inputImage, outputImagePath, scaledWidth, scaledHeight);
			//read reszied output image for prediction
			BufferedImage outputImage = image.read(outputImagePath);
			System.out.println(outputImage.getHeight());
			System.out.println(outputImage.getWidth());
			
	    }
}
