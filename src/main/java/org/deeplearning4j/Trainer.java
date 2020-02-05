package org.deeplearning4j;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.TransferLearningHelper;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Optional class for training purpose
 * Saves the best layer. Takes 10 mins to run
 */

public class Trainer{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Trainer.class);

    private static final int trainPerc = 80;
    private static final int batchSize = 15;
    private static final int epochLimit = 5;
    private static final String featureExtractionLayer = "fc2";

    /*
    for training/testing from scratch

    public static void main(String [] args) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException, ClassNotFoundException {
        loadData();
        dogTrain();
        classify("test.jpg");
    }

    public static void loadData() throws IOException {
        DogDataSetIterator.setup(batchSize,trainPerc);
        DataSetIterator trainIter = ImgDataSetIterator.trainIterator();
    }
    */

    /**
     * Function for model training and building purpose
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static void dogTrain() throws IOException, ClassNotFoundException {
        ComputationGraph vgg16Transfer = ModelUtils.prepareModel(true);
        TransferLearningHelper helper = new TransferLearningHelper(vgg16Transfer,featureExtractionLayer);
        File savedLayer = new File(ModelUtils.bestLayer);

        if (savedLayer.exists()) {
            log.info("Found trained output layer");
            // ModelUtils.loadModelLastLayer(helper.unfrozenGraph(), bestLayer);
            // FIXME: loading layer to helper would stall learning
            ModelUtils.loadModelLastLayer(vgg16Transfer, ModelUtils.bestLayer);
        }

        // Dataset iterators
        ImgDataSetIterator.featurizedPreSave(helper,featureExtractionLayer,batchSize,trainPerc);
        DataSetIterator trainIter = ImgDataSetIterator.featurizedTrainIterator();
        DataSetIterator testIter = ImgDataSetIterator.featurizedTestIterator();

        Evaluation eval;
        int iter = 0;
        int trainDataSaved = 0;
        double best = 0;
        for( int epoch = 0; epoch < epochLimit; epoch++ ) {
            while (trainIter.hasNext()) {
                // process mini-batch
                // vgg16Transfer.fit(trainIter.next());
                helper.fitFeaturized(trainIter.next());
                if (iter / 20 > 0 && iter % 20 == 0) {
                    // eval = vgg16Transfer.evaluate(testIter);
                    eval = helper.unfrozenGraph().evaluate(testIter);
                    log.info("Iteration " + iter + " F1 score: " + Double.toString(eval.f1()));
                    testIter.reset();
                    if (eval.f1() > best) {
                        log.info("Save best result");
                        best = eval.f1();
                        // save model and also save momentum by set save updater to true
                        // vgg16Transfer.save(savedModel, true);
                        ModelUtils.saveModelLastLayer(vgg16Transfer, ModelUtils.bestLayer);
                    }
                }
                iter++;
            }
            log.info("Epoch " + epoch + " finished" );
            trainIter.reset();
        }
        log.info("Model build complete");
    }
}
