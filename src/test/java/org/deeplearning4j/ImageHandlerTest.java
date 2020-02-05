package org.deeplearning4j;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Image Handler test for reading and resizing the image
 */
class ImageHandlerTest {
	ImageHandler ih = new ImageHandler();
	
	/**
	 * Test if the input image is read and check the size of the output image
	 * @throws IOException
	 */
    @Test
    void read() throws IOException {
    	BufferedImage inputImage;
    	String inputImagePath = "img/Afghan.jpg";
        String outputImagePath = "Afghan_resize.jpg";
        
        File outputFile = new File(outputImagePath);
        //In case the file exists we delete it
        if(outputFile.exists()) {
        	outputFile.delete();
        }
        inputImage = ih.read(inputImagePath, outputImagePath);
        BufferedImage outputImage = ImageIO.read(outputFile);
        assertNotNull(outputImage);
        assertEquals(255, outputImage.getHeight());
        assertEquals(300 , outputImage.getWidth());
        
    }
}
