package org.deeplearning4j;

import com.google.gson.Gson;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.transferlearning.TransferLearningHelper;
import org.nd4j.linalg.dataset.AsyncDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.ExistingMiniBatchDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Class for setting up iterator to read through training/test data for training purpose
 * We are already providing with featurized dataset
 * So it's optional to run it
 */

public class ImgDataSetIterator {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ImgDataSetIterator.class);

    private static final String DOG_DIR = "dogImages/train/";

    private static final String [] allowedExtensions = BaseImageLoader.ALLOWED_FORMATS;
    private static final Random rng  = new Random(13);

    private static final int height = 224;
    private static final int width = 224;
    private static final int channels = 3;
    public static final int numClasses = 133;

    private static ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
    private static InputSplit trainData,testData;
    private static int batchSize;

    private static final String featurizePrefix = "dogs"; 
    private static final String featureExtractorLayer = "fc2";
    private static org.nd4j.linalg.dataset.api.iterator.DataSetIterator trainIterator() throws IOException {
        return makeIterator(trainData);
    }

    private static org.nd4j.linalg.dataset.api.iterator.DataSetIterator testIterator() throws IOException {
        return makeIterator(testData);
    }

    /**
     * Utility functions for loading files and split them into training/testing data
     * @param batchSizeArg
     * @param trainPerc
     */

    private static void setup(int batchSizeArg, int trainPerc) {
        batchSize = batchSizeArg;
        File parentDir = new File(DOG_DIR);
        FileSplit filesInDir = new FileSplit(parentDir, allowedExtensions, rng);
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, allowedExtensions, labelMaker);
        if (trainPerc >= 100) {
            throw new IllegalArgumentException("Percentage of data set aside for training has to be less than 100%. Test percentage = 100 - training percentage, has to be greater than 0");
        }
        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, trainPerc, 100-trainPerc);
        trainData = filesInDirSplit[0];
        testData = filesInDirSplit[1];
    }

    private static org.nd4j.linalg.dataset.api.iterator.DataSetIterator makeIterator(InputSplit split) throws IOException {
        ImageRecordReader recordReader = new ImageRecordReader(height,width,channels,labelMaker);
        recordReader.initialize(split);
        File labelFile = new File("labels.json");
        if( !labelFile.exists() ) {
            // dump labels to json file
            String labels = new Gson().toJson(recordReader.getLabels());
            FileWriter writer = new FileWriter(labelFile);
            log.info( labels );
            writer.write( labels );
            writer.close();
        }
        org.nd4j.linalg.dataset.api.iterator.DataSetIterator iter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numClasses);
        iter.setPreProcessor( new VGG16ImagePreProcessor());
        return iter;
    }

    /**
     * Utility function for featurizing training/test data
     * @param helper
     * @param featurizeExtractionLayer
     * @param batchSize
     * @param trainPerc
     * @throws IOException
     */

    public static void featurizedPreSave(TransferLearningHelper helper, String featurizeExtractionLayer, int batchSize, int trainPerc) throws IOException {
        File featurizedFolder = new File("trainFolder");
        if(featurizedFolder.exists()) {
            return;
        }

        setup(batchSize,trainPerc);
        DataSetIterator trainIter = ImgDataSetIterator.trainIterator();
        DataSetIterator testIter = ImgDataSetIterator.testIterator();

        int trainDataSaved = 0;
        while(trainIter.hasNext()) {
            DataSet currentFeaturized = helper.featurize(trainIter.next());
            saveToDisk(currentFeaturized,trainDataSaved,true);
            trainDataSaved++;
        }

        int testDataSaved = 0;
        while(testIter.hasNext()) {
            DataSet currentFeaturized = helper.featurize(testIter.next());
            saveToDisk(currentFeaturized,testDataSaved,false);
            testDataSaved++;
        }

        log.info("Finished featurizing test and train data");
    }

    /**
     * Utility function for saving featurized data
     * @param currentFeaturized
     * @param iterNum
     * @param isTrain
     */
    private static void saveToDisk(DataSet currentFeaturized, int iterNum, boolean isTrain) {
        File fileFolder = isTrain ? new File("trainFolder"): new File("testFolder");
        if (iterNum == 0) {
            fileFolder.mkdirs();
        }
        String fileName = featurizePrefix + featureExtractorLayer + "-";
        fileName += isTrain ? "train-" : "test-";
        fileName += iterNum + ".bin";
        currentFeaturized.save(new File(fileFolder,fileName));
        log.info("Saved " + (isTrain?"train ":"test ") + "dataset #"+ iterNum);
    }

    /**
     * Utility functions for loading featurized data
     * @return
     * @throws IOException
     */

    public static DataSetIterator featurizedTrainIterator() throws IOException {
        DataSetIterator existingTrainingData = new ExistingMiniBatchDataSetIterator(new File("trainFolder"),featurizePrefix+featureExtractorLayer+"-train-%d.bin");
        DataSetIterator asyncTrainIter = new AsyncDataSetIterator(existingTrainingData);
        return asyncTrainIter;
    }

    public static DataSetIterator featurizedTestIterator() throws IOException {
        DataSetIterator existingTrainingData = new ExistingMiniBatchDataSetIterator(new File("testFolder"),featurizePrefix+featureExtractorLayer+"-test-%d.bin");
        DataSetIterator asyncTrainIter = new AsyncDataSetIterator(existingTrainingData);
        return asyncTrainIter;
    }
}
