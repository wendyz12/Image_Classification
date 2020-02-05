package org.deeplearning4j;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * 
 * User Interface Test
 *
 */
class UserInterfaceTest {
	private Robot robot;
	private RobotClass robotClass;
	private Runtime runtime;
	String inputPath = "img/Afghan.jpg";
    String outputImagePath = "Afghan_resize.jpg";
    
    /**
     * Test if the image is processed after pressing choose image button
     * Has reference to Robot class which should be in the same folder
     * Please do not move mouse manually during testing. It takes a minute for model to load
     * @throws IOException
     */

	@Test
	void run() throws IOException {
		UserInterface u = new UserInterface();
		u.run();

		int xSet = 350;
		int yset = 447;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			
			e.printStackTrace();
		}

		robot.delay(15000);
		robot.mouseMove(xSet, yset);
		robotClass.leftClick(robot);
		
		ImageHandler ih = new ImageHandler();
		BufferedImage inputImage = ih.read(inputPath, outputImagePath);
		ImageIcon imageDisplay = new ImageIcon(outputImagePath);
        JLabel imageLabel = new JLabel(imageDisplay);
        assertTrue(u.button1.isVisible());
        assertNotNull (imageLabel);
	}

}
