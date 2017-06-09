package edu.wit.comp2000.group18.spellchecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Main Class<br/>
 * --------------------<br/>
 * Launcher Class for GUI
 * 
 * @author Group 18 - Bradley Rawson, Andrew Mellen, Olivia Deng, Kai Zhao
 * @courseNumber COMP2000-05
 * @assignment Application 1
 * @date September 22, 2016
 */
public class Main extends Application {
	@Override
	/**
	 * Start the GUI
	 */
	public void start(Stage primaryStage) {
		try{
			Parent root = FXMLLoader.load(getClass().getResource("display_window.fxml"));
			Scene scene = new Scene(root);
			
			//Title and Icon
			primaryStage.setTitle("Spell Checker");
			primaryStage.getIcons().add(new Image("file:icon.png")); //Open Source Icon:
																//https://design.google.com/icons/
			//Make sure window is non-resizable
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception ex) {
			System.err.println(ex);
		}
	}

	/**
	 * main method. Starts the program, runs start method
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
