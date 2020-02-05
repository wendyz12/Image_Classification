package org.deeplearning4j;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.TransferLearningHelper;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.io.IOException;

/**
 * Test if the data is prepared for training and testing
 */
class ImgDataSetIteratorTest {
    ZooModel zooModel = VGG16.builder().build();
    
    /**
     * Test if featurized folder is created
     * @throws IOException
     */
    @Test
    //It will work only when there is dogImages folder in the project level directory
    //Please download as per instruction in ReadMe file
    void featurizedPreSave() throws IOException {
        int trainPerc = 80;
        int batchSize = 15;
        File featurizedFolder = new File("trainFolder");
        if(featurizedFolder.exists()) {
            // make sure we don't have this file before saving
            featurizedFolder.delete();
        }
        String featurizeExtractionLayer = "fc2";
        ComputationGraph vgg16 = (ComputationGraph) zooModel.initPretrained();
        TransferLearningHelper helper = new TransferLearningHelper(vgg16, featurizeExtractionLayer);
        ImgDataSetIterator.featurizedPreSave(helper, featurizeExtractionLayer, batchSize, trainPerc);
        assert(featurizedFolder.exists());
    }
    
    /**
     * Test if featurizedTrainIterator is built
     * @throws IOException
     */
    @Test
    void featurizedTrainIterator() throws IOException {
        DataSetIterator asyncTrainIter;
        asyncTrainIter = ImgDataSetIterator.featurizedTrainIterator();
        assertNotNull(asyncTrainIter);
    }
    
    /**
     * Test if featurizedTestIterator is built
     * @throws IOException
     */
    @Test
    void featurizedTestIterator() throws IOException {
        DataSetIterator asyncTestIter;
        asyncTestIter = ImgDataSetIterator.featurizedTrainIterator();
        assertNotNull(asyncTestIter);
    }
}
