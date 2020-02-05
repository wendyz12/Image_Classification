package org.deeplearning4j;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Classify the input image and provide top 1 prediction result and top 5 results
 */

public class Classifier {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Classifier.class);
    ComputationGraph model;
    boolean resultReady = false;
    double[] arr;

    public Classifier() throws IOException, ClassNotFoundException {
        model = ModelUtils.prepareModel(false);
        ModelUtils.loadModelLastLayer(model, ModelUtils.bestLayer);
    }

    /**
     * Function for obtaining the label id with the largest probability
     * @param ImagePath
     * @return
     * @throws IOException
     */

    public int classify(String ImagePath) throws IOException {
        // load image
        File f = new File(ImagePath);
        NativeImageLoader img = new NativeImageLoader(224, 224, 3);
        INDArray input = img.asMatrix( f );

        INDArray[] ret = model.output(input);
        DataBuffer buffer = ret[0].data();
        arr = buffer.asDouble();
        resultReady = true;

        int maxAt = 0;
        for(int i=0; i<arr.length; i++) {
            maxAt = arr[i] > arr[maxAt] ? i:maxAt;
        }

        return maxAt;
    }

    /**
     * Select top5 results and save them to a string table
     * @return
     */

    public String[][] probabilityCalc () {
        assert resultReady;
        int size = 5;
        String [][] table = new String[size][2];
        PriorityQueue<Double[]> que = new PriorityQueue<>(new Comparator<Double[]>() {
            @Override
            public int compare(Double[] o1, Double[] o2) {
                int n = Double.compare(o1[1], o2[1]);
                return n;
            }
        });
        for(int i = 0; i < 133; i++) {
            Double[] result = new Double[] {(double)i, arr[i]};
            que.offer(result);
            if(que.size() > size) {
                que.poll();
            }
        }

        for(int i = size - 1; i >-1; i--) {
            Double[] ele = que.poll();
            table[i][0] = ele[0].toString();
            table[i][1] = ele[1].toString();
        }

        return table;
    }

}
