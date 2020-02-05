package org.deeplearning4j;
import java.awt.AWTException; 
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent; 
import java.io.*;

/**
 * 
 * For testing the UserInterface. It will move mouse around
 * and click buttons. Please do not move mouse manually while testing
 */
public class RobotClass {
	/**
	 * same as leftClicking the mouse button
	 * @param robot
	 */
	public static void leftClick(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	/**
	 * same as doubleClicking the mouse button
	 * @param robot
	 */
	public static void doubleClick(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

}
