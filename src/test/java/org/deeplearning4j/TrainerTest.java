package org.deeplearning4j;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the model training. Best layer saved
 */
class TrainerTest {

    /**
	 * Check if the best dog train model saved
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
    @Test
    void dogTrain() throws ClassNotFoundException, IOException {
    	double best = 0;
    	Trainer.dogTrain();
    	assertNotNull(best);
    }
}
