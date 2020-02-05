# Machine Learning on Dog Breed Classification

### Environment Setup
* Apache Maven

  Install Maven:  `brew install maven` if you have [Homebrew](https://brew.sh/) installed or please check out the [Maven Installation Guide](https://maven.apache.org/install.html).
  
* **Please note** the latest macOS, Catalina(10.15.1 and above), may not support the project. 

### Download and Run
* Download the entire git repo at https://github.com/UPenn-CIT599/final-project-team33-nidhi_elva_qi

* Compile and run the program

  ##### Option 1 - via Terminal

  * Open up terminal and go to the directory of the local git repo
  
  * Run: `./run.sh`
  
  ##### Option 2 - via IDEs (Intellij/Eclipse)

  * Open the project
    
    * In Eclipse
    
      <img src="https://github.com/UPenn-CIT599/final-project-team33-nidhi_elva_qi/raw/master/img/EclipseOpenProject.png" width="250">
      
     * In Intellij
    
       <img src="https://github.com/UPenn-CIT599/final-project-team33-nidhi_elva_qi/raw/master/img/IntelliJOpenProject.png" width="300">
  
  * Go to src/main/java/org.deeplearning4j/UserInterface.java
  
  * Run main function
  
### Interact with User Interface
* Click on "Choose a picture" button to select any cute dog image you have locally

* Click on "Predict" while the button is enabled to classify the selected dog

* Enjoy

  <img src="https://github.com/UPenn-CIT599/final-project-team33-nidhi_elva_qi/raw/master/img/FinalDisplay.png" width="350">

* Repeat the steps above until you close off the User Interface and have enough fun with our program

### Project Demo Recording

  * https://drive.google.com/file/d/1Va96_M2t0uIT97tk1qVPhnlEfGPxUMQ1/view?usp=sharing

### Test Cases

Under src/test/java/org.deeplearning4j

  * 19 JUnit tests across all the classes: UserInterface(1), ImageHandler(1), WikiScraper(6), Classifier(3), ModelUtils(4), Trainer(1), ImgDataSetIterator(3)
  
  * 1 Sanity test, ScraperCheck, for WikiScraper class to ensure, for 133 labels, we have the right information fetched online

### References
* Deeplearning4j Quickstart Tutorial

  https://deeplearning4j.org/docs/latest/deeplearning4j-quickstart

* Deeplearning4j documentation:
  
  https://deeplearning4j.org/api/latest/org/deeplearning4j/nn/api/Model.html

* Resizing images 
  
  https://github.com/eclipse/deeplearning4jhttps://www.codejava.net/java-se/graphics/how-to-resize-images-in-java

* For creating JTable and adding probability values
  
  https://docs.oracle.com/javase/tutorial/uiswing/components/table.html 

* Demo Swing UI (from TA Patrick):
  
  https://drive.google.com/file/d/1wrebKh_HjKpu0F1eDC7Tiw3G2TPNN0m-/view?usp=sharing
 
* Documentation Swing UI:
  
  https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html
 
* Junit 5 testing
  
  https://junit.org/junit5/docs/current/user-guide/
 
* Tutorials (for Swing UI):
  
  https://docs.oracle.com/javase/tutorial/uiswing/index.html
  
  https://www.javatpoint.com/java-swing
 
* Scraping website(wiki)
  
  https://jsoup.org/cookbook/input/load-document-from-url
  
  https://jsoup.org/cookbook/extracting-data/attributes-text-html
 
* JFileChooser
  
  https://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
 
* Towards Data Science(for Images in presentation)
  
  https://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
  
* For Java Robot class (testing UserInterface)

  https://docs.oracle.com/javase/7/docs/api/java/awt/Robot.html
  
* Dog training images from udacity 

  s3-us-west-1.amazonaws.com/udacity-aind/dog-project/dogImages.zip

### More Notes
If you would like to run test cases or train the model yourself, please run `./setup.sh` in terminal to download the training dataset of dogImages. This folder should be saved directly under the project level directory.

