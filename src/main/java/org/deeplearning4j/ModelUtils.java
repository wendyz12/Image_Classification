package org.deeplearning4j;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;

import java.io.*;
import java.util.Map;

/**
 * Group model related methods shared across different classes (Classifier, Trainer, etc.)
 */

public class ModelUtils {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ModelUtils.class);

    protected static final int numClasses = 133;
    protected static final long seed = 12345;

    public static final String bestLayer = "savedLayer";

    private static final String featureExtractionLayer = "fc2";

    /**
     * Prepare model by replacing the OutputLayer with a blank layer
     * @param tune
     * @return
     * @throws IOException
     */

    public static ComputationGraph prepareModel(boolean tune) throws IOException {
        ComputationGraph model;
        //Import vgg
        //Note that the model imported does not have an output layer (check printed summary)
        //  nor any training related configs (model from keras was imported with only weights and json)
        log.info("\n\nLoading org.deeplearning4j.transferlearning.vgg16...\n\n");
        // could try ResNet50
        ZooModel zooModel = VGG16.builder().build();
        ComputationGraph vgg16 = (ComputationGraph) zooModel.initPretrained();

        // Replacement for the last layer
        OutputLayer output = new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nIn(4096).nOut(numClasses)
                .weightInit(new NormalDistribution(0, 0.2 * (2.0 / (4096 + numClasses)))) //This weight init dist gave better results than Xavier
                .activation(Activation.SOFTMAX).build();

        if(tune) {
            //Decide on a fine tune configuration to use.
            //In cases where there already exists a setting the fine tune setting will
            //  override the setting for all layers that are not "frozen".
            FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                    .updater(new Nesterovs(5e-5))
                    .seed(seed)
                    .build();

            //Construct a new model with the intended architecture and print summary
            model = new TransferLearning.GraphBuilder(vgg16)
                    .fineTuneConfiguration(fineTuneConf)
                    .setFeatureExtractor(featureExtractionLayer) //the specified layer and below are "frozen"
                    .removeVertexKeepConnections("predictions") //replace the functionality of the final vertex
                    .addLayer("predictions", output, "fc2" )
                    .build();

            log.info(vgg16.summary());
        } else {
            //Construct a new model with the intended architecture and print summary
            model = new TransferLearning.GraphBuilder(vgg16)
                    .setFeatureExtractor(featureExtractionLayer) //the specified layer and below are "frozen"
                    .removeVertexKeepConnections("predictions") //replace the functionality of the final vertex
                    .addLayer("predictions", output, "fc2" )
                    .build();
        }
        return model;
    }

    /**
     * Create a file with the name as savedName, then save the last layer of the given model to the file
     * @param model
     * @param savedName
     * @throws IOException
     */
    public static void saveModelLastLayer(ComputationGraph model, String savedName) throws IOException {
        Layer fc2 = model.getOutputLayer(0);
        Map<String, INDArray> fc2Param = fc2.paramTable();

        FileOutputStream fileOut = new FileOutputStream(savedName);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(fc2Param);

        out.close();
        fileOut.close();
    }

    /**
     * Read the last layer from the file, update the given model by load the last layer to it
     * @param model
     * @param savedName
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static void loadModelLastLayer(ComputationGraph model, String savedName) throws IOException, ClassNotFoundException {
        // fetch saved layer param
        FileInputStream fileIn = new FileInputStream(savedName);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        Map <String,INDArray> fc2Param = (Map <String,INDArray>) in.readObject();
        in.close();
        fileIn.close();

        // update model
        Layer fc2 = model.getOutputLayer(0);
        fc2.setParamTable( fc2Param );
    }
}
