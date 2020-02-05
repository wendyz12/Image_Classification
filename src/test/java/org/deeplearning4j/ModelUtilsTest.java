package org.deeplearning4j;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is the test class for all the model related functions
 */

class ModelUtilsTest {

    /**
     * This JUnit test tests the function of prepareModel
     * and covers model build part without FineTuneConfiguration
     */

    @Test
    void prepareModelNoTuning() {
        ComputationGraph graph;
        try {
            graph = ModelUtils.prepareModel(false);
            assertNotNull(graph);
        } catch (IOException ex) {
            // we are not expecting any I/O issue.
            assert(false);
        }
    }

    /**
     * This JUnit test tests the function of prepareModel
     * and covers model build part with FineTuneConfiguration
     */

    @Test
    void prepareModelTunning() {
        ComputationGraph graph;
        try {
            graph = ModelUtils.prepareModel(true);
            assertNotNull(graph);
        } catch (IOException ex) {
            // we are not expecting any I/O issue.
            assert(false);
        }
    }

    /**
     * This JUnit test tests the function of saveModelLastLayer
     * and sees if the last layer file is being successfully saved
     * @throws IOException
     */

    @Test
    void saveModelLastLayer() throws IOException {
        String savedName = "testSaveModelLastLayer";
        File check = new File(savedName);
        if(check.exists()) {
            // make sure we don't have this file before saving
            check.delete();
        }
        ComputationGraph graph = ModelUtils.prepareModel(false);
        ModelUtils.saveModelLastLayer( graph, savedName);
        assert(check.exists());
    }

    /**
     * This JUnit test tests the function of loadModelLastLayer
     * and sees if the saved last layer has been loaded successfully
     * by comparing before and after state of the same model
     * @throws IOException
     * @throws ClassNotFoundException
     */

    @Test
    void loadModelLastLayer() throws IOException, ClassNotFoundException {
        ComputationGraph graph = ModelUtils.prepareModel(false);

        // Fetch (uninitialized) layer param before loading.
        Layer fc2 = graph.getOutputLayer(0);
        Map<String, INDArray> before= fc2.paramTable();

        // Load last layer, which overwrites all parameters in the paramTable.
        ModelUtils.loadModelLastLayer(graph, ModelUtils.bestLayer);

        // Fetch updated parameter table.
        fc2 = graph.getOutputLayer(0);
        Map<String, INDArray> after = fc2.paramTable();

        // Parameters must have changed after loading new parameters for
        // the last layer.
        for(String key: after.keySet()) {
            assertNotEquals(before.get(key), after.get(key));
        }
    }
}