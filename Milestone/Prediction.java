public class Prediction {
	
	ModelLoader model = new Model();
	ImageHandler handler = new ImageHandler();
	SwingUI frontEnd = new SwingUI();
	
	MultiLayerNetwork theModel = model.loadModel();
	BufferedImage inputImage = handler.read(inputImagePath)

	/**
	 * predict the imput image
	 * @param model
	 * @param image
	 * @return
	 */
	public Evaluation predict (MultiLayerNetwork model, BufferedImage image) {

		DataSetIterator myData = image
		Evaluation eval = model.evaluate(myData);

		return eval;
	}

	/**
	 * report eveluation result to frontend
	 */
	pubilc void report(Evaluation eval) {

	}


}

