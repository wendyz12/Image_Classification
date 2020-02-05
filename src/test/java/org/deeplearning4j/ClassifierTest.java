package org.deeplearning4j;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classifier Tests
 */
class ClassifierTest {
    private final static Random rnd = new Random(System.currentTimeMillis());

    /**
     * Test to check if the output breed corresponds to maximum probability value
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    void classifyAfftest() throws IOException, ClassNotFoundException {
        HashMap<String, Double> probabilityHash = new HashMap<String, Double>();
        Classifier c = new Classifier();
        int breedIdx = c.classify("img/Afftest.jpg");
        int expectedBreed = -1;
        String[][] table = c.probabilityCalc();
        probabilityHash.put(table[0][0], Double.parseDouble(table[0][1]));
        probabilityHash.put(table[1][0], Double.parseDouble(table[1][1]));
        probabilityHash.put(table[2][0], Double.parseDouble(table[2][1]));
        probabilityHash.put(table[3][0], Double.parseDouble(table[3][1]));
        probabilityHash.put(table[4][0], Double.parseDouble(table[4][1]));
        ArrayList<Double> list = new ArrayList<Double> (probabilityHash.values());
        double maxProbability = Collections.max(list);
        for (String key: probabilityHash.keySet()) {
            if (probabilityHash.get(key) == maxProbability) {
                expectedBreed = (int)Double.parseDouble(key);
            }
        }
        assertEquals(breedIdx, expectedBreed);
    }
    
    /**
     * Test to check if the results returned by classify are within the label range
     * @throws IOException
     * @throws ClassNotFoundException
     */

    @Test
    void classifyWithinRange() throws IOException, ClassNotFoundException {
        // create a random image of nothing in it
        String fileName = "img/random.jpg";
        int width = 240;
        int height = 240;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] array = new int[image.getWidth() * image.getHeight() * image.getRaster().getNumBands()];
        for(int x=0; x<image.getWidth(); ++x) {
            for(int y=0; y<image.getHeight(); ++y) {
                image.setRGB(x, y, rnd.nextInt(0xFFFFFF));
            }
        }
        ImageIO.write(image, "JPG", new File(fileName));

        Classifier c = new Classifier();
        int breed = c.classify(fileName);
        assert(breed >= 0);
        assert(breed <= ImgDataSetIterator.numClasses);
    }

    /**
     * Test to check if maximum probability of predicted breed is at last than greater than 0.8
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    void probabilityCalcThreshold() throws IOException, ClassNotFoundException {
        HashMap<String, Double> probabilityHash = new HashMap<String, Double>();
        Classifier c = new Classifier();
        double sum = 0;

        c.classify("img/Afftest.jpg");
        String[][] table = c.probabilityCalc();

        probabilityHash.put(table[0][0], Double.parseDouble(table[0][1]));
        probabilityHash.put(table[1][0], Double.parseDouble(table[1][1]));
        probabilityHash.put(table[2][0], Double.parseDouble(table[2][1]));
        probabilityHash.put(table[3][0], Double.parseDouble(table[3][1]));
        probabilityHash.put(table[4][0], Double.parseDouble(table[4][1]));
        ArrayList<Double> list = new ArrayList<Double> (probabilityHash.values());
        double maxProbability = Collections.max(list);
        for(Double d : list) {
            sum += d;
        }

        assertTrue(maxProbability > 0.8);
        assertTrue(sum > 0.99);
    }
}
