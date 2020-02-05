import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

/**
 * 
 * Testing ImageHandler
 *
 */
class ImageHandlerTest {

	@Test
	/**
	 * Testing if the height of the image is correct
	 */
	void testResize_height() {
		BufferedImage outputImage = null; 
		ImageHandler image = new ImageHandler();
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
            outputImage = image.read(outputImagePath);      
        assertEquals(outputImage.getHeight(), 100);

	}
	
	@Test
	/**
	 * Testing if the width of the image is correct
	 */
	void testResize_width() {
		BufferedImage outputImage = null; 
		ImageHandler image = new ImageHandler();
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
            outputImage = image.read(outputImagePath);
        assertEquals(outputImage.getWidth(), 100);
        
	}

}
