package org.deeplearning4j;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test wikiScraper class
 *
 */
class WikiScraperTest {
	WikiScraper wiki = new WikiScraper();

	/**
	 * transform image from Image to BufferedImage type
	 * 
	 * @param img is the image that need to be transformed
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	/**
	 * Test to check if the introduction of dog from wiki exists
	 */
	@Test
	void testExistenceOfIntro() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		StringBuilder intro = wiki.contentText_formatted;
		assertNotNull(intro);

	}

	/**
	 * Test to check if the Image of dog from wiki exists
	 */
	@Test
	void testExistenceOfImage() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		Image buffered = wiki.resizedImage;
		assertNotNull(buffered);

	}

	/**
	 * Test to check if the width of the to-display image is 300
	 */
	@Test
	void testWidthOfImage() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		BufferedImage buffered = toBufferedImage(wiki.resizedImage);
		double imageWidth = buffered.getWidth();
		assertEquals(300.0, imageWidth, 0.0);

	}

	/**
	 * Test to check if the height of the to-display image is 300* height width
	 * ratio of original image
	 */
	@Test
	void testHeightfImage() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		BufferedImage buffered = toBufferedImage(wiki.resizedImage);
		double imageHeight = buffered.getHeight();
		double hwRatio = 265.0 / 336.0;
		double expectedHeight = hwRatio * 300;
		assertEquals(300 * hwRatio, imageHeight, 0.5);

	}

	/**
	 * Test to check if the length of to-display dog intro is within 2 sentences
	 */
	@Test
	void testIntroLength() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		String intro = wiki.shorterText;
		String[] numOfSentence = intro.toString().split("[!?.]+");
		assertTrue(numOfSentence.length < 3, "introduction is in 2 sentences");
	}

	/**
	 * Test to check if the to-display dog intro has less than 50 words/tokens per
	 * row
	 */
	@Test
	void testIntroRowLength() {
		wiki.scrapeTopicWiki("/wiki/American_Foxhound");
		StringBuilder intro = wiki.contentText_formatted;
		String[] introRows = intro.toString().split("[\\r\\n]+");
		StringTokenizer tok1 = new StringTokenizer(introRows[0], " ");
		int row1TokenLength = 0;
		while (tok1.hasMoreTokens()) {
			row1TokenLength = row1TokenLength + tok1.nextToken().length();
		}
		assertTrue(row1TokenLength <= 50, "each row has less than 50 tokens");
	}

}
