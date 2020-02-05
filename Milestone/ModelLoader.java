public class ModelLoader {

	/**
	 * load selected Model
	 * @param modelPath
	 * @return
	 */
	public MultiLayerNetwork loadModel(String modelPath) {
		
		String simpleMlp = new ClassPathResource("simple_mlp.h5").getFile().getPath();
		MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);	

		return model;	
	}

}


