package org.deeplearning4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Fetch picture and introduction of the predicted dog
 */

public class WikiScraper {
	StringBuilder contentText_formatted;
	Image resizedImage;
	String shorterText = "";
	String url1;

	/**
	 * method to scrape Wikipedia first 2 sentences of first paraghraph and first
	 * image based on keyword
	 * 
	 * @param url
	 */
	public void scrapeTopicWiki(String url) {
		// take care some special case for the gap between breed naming in the model and
		// wiki
		if (url.equals("/wiki/Brussels_griffon")) {
			url1 = "/wiki/Griffon_Bruxellois";
		} else if (url.equals("/wiki/Chinese_Shar-pei")) {
			url1 = "/wiki/Shar_Pei";
		} else if (url.equals("/wiki/Plott")) {
			url1 = "/wiki/Plott_Hound";
		} else if (url.equals("/wiki/Havanese")) {
			url1 = "/wiki/Havanese_dog";
		} else if (url.equals("/wiki/Boxer")) {
			url1 = "/wiki/Boxer_(dog)";
		} else if (url.equals("/wiki/Pomeranian")) {
			url1 = "/wiki/Pomeranian_(dog)";
		} else if (url.equals("/wiki/Brittany")) {
			url1 = "/wiki/Brittany_(dog)";
		} else if (url.equals("/wiki/Newfoundland")) {
			url1 = "/wiki/Newfoundland_(dog)";
		} else {
			url1 = url;
		}

		Document doc;
		BufferedImage intImage;
		boolean success = false;
		int trytimes = 0;
		while (!success && trytimes < 3) {
			try {
				doc = Jsoup.connect("http://www.wikipedia.org" + url1).get();
				String contentText = doc.select("p").first().text().trim();
				// take care of a broad name of a dog and transfer it to the format wiki can
				// recognize
				if (contentText.indexOf("may refer to:") >= 0) {
					url1 = url1 + "_(dog)";
					doc = Jsoup.connect("http://www.wikipedia.org" + url1).get();
					contentText = doc.select("p").first().text().trim();
				}
				// some of the wiki introduction start from second paragraph instead of first
				if (contentText.length() < 11) {
					if (doc.select("p").get(1).text().length() > 0) {
						contentText = doc.select("p").get(1).text();
					} else if (doc.select("p").get(2).text().length() > 0) {
						contentText = doc.select("p").get(2).text();
					} else {
						contentText = "Sorry! The details of " + url1.split("/")[2] + " on Wiki is not found";
					}
				}
				// get the first image
				Element firstImage = doc.select("img[src*=" + url.split("/")[2] + "]").first();
				// supplementary image in the case that wiki does not have an image
				if (firstImage != null) {
					String imgSrc = firstImage.absUrl("src");
					URL imageUrl = new URL(imgSrc);
					intImage = ImageIO.read(imageUrl);

				} else {
					File substitue = new File("img/notavailable.png");
					intImage = ImageIO.read(substitue);
				}
				// resize the image from wiki
				double heightO = intImage.getHeight();
				double widthO = intImage.getWidth();
				double hwRatio = heightO / widthO;
				resizedImage = intImage.getScaledInstance(300, (int) (300 * hwRatio), Image.SCALE_SMOOTH);
				// format the wiki dog intro in 2 ways, 1. shorten it to 2 sentences. 2. limit
				// 50 tokens in a row
				String[] sentence = contentText.split("\\.");
				if (sentence.length > 2) {
					for (int i = 0; i < 2; i++) {
						shorterText = shorterText + sentence[i] + "." + " ";
					}
				} else {
					for (int i = 0; i < sentence.length; i++) {
						shorterText = shorterText + sentence[i] + "." + " ";
					}
				}
				if (url1.equals("/wiki/Affenpinscher")) {
					shorterText = contentText;
				}
				shorterText = shorterText.trim();
				StringTokenizer tok = new StringTokenizer(shorterText, " ");
				contentText_formatted = new StringBuilder(shorterText.length());
				int contentLength = 0;
				while (tok.hasMoreTokens()) {
					String word = tok.nextToken();
					if (contentLength + word.length() > 50) {
						contentText_formatted.append("\n");
						contentLength = 0;
					}
					contentText_formatted.append(word).append(" ");
					contentLength += word.length();
				}
				success = true;
			} catch (IOException e) {
				// catch exceptions if the scraper cannot located the key word
				// transform the key words and try 2 times
				// Case 1: keyword contains "of" or "and"
				if (trytimes == 0) {
					String[] urlcomponent = url1.split("/")[2].split("_");
					String output = "";
					for (int i = 0; i < urlcomponent.length; i++) {
						if (urlcomponent[i].equals("Of") || urlcomponent[i].equals("And")) {
							output = output + "_" + Character.toLowerCase(urlcomponent[i].charAt(0))
									+ urlcomponent[i].substring(1);
						} else {
							output = output + "_" + urlcomponent[i];
						}
					}
					url1 = "/wiki/" + output.substring(1);
					trytimes = trytimes + 1;
					// Case2: keyword in wiki has "_dog" at the end
				} else if (trytimes == 1) {
					String[] urlcomponent = url1.split("/")[2].split("_");
					String output = "";
					if (urlcomponent.length >= 2) {
						url1 = "/wiki/" + urlcomponent[0] + "_dog";
					} else {
						url1 = "/wiki/" + url1.split("/")[2] + "_dog";
					}
					trytimes = trytimes + 1;
					// Case3: keyword in wiki has "_dog" at the end
				} else if (trytimes == 2) {
					String[] urlcomponent = url1.split("/")[2].split("_");
					String output = "";
					if (urlcomponent.length >= 2) {
						url1 = "/wiki/" + urlcomponent[1] + "_dog";
					}
					trytimes = trytimes + 1;
				}
			}
		}
		// if the keyword still fail to fetch wiki after two times of trials, return
		// below reminder
		if (!success) {
			System.out.println("Cannot Find Information About this Dog. Please try again with other dog image");
		}

	}
}
