package edu.wit.comp2000.group18.spellchecker;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * MainController Class<br/>
 * --------------------<br/>
 * Controller class for the GUI
 * 
 * @author Group 18 - Bradley Rawson, Andrew Mellen, Olivia Deng, Kai Zhao
 * @courseNumber COMP2000-05
 * @assignment Application 1
 * @date September 22, 2016
 */
public class MainController {
	@FXML private ComboBox<String> comboBox;
	@FXML private TextArea textBox;
	
	/**
	 * Runs this method on startup to set up GUI stuff
	 */
	public void initialize() {
		ArrayList<String> fileNames = new ArrayList<>();
		fileNames.add("sources.txt");
		fileNames.add("the-lancashire-cotton-famine.txt");
		fileNames.add("wit-attendance-policy.txt");
		fileNames.add("ForTesting.txt");
		comboBox.setItems(FXCollections.observableArrayList(fileNames));
		comboBox.getSelectionModel().select(0);
		setTextBox();
	}
	
	/**
	 * Runs when the user selects something new in the ComboBox
	 * 
	 * @param event
	 */
	public void onComboChange(ActionEvent event){
		setTextBox();
	}
	
	/**
	 * Sets the output from SpellChecker into the text area
	 */
	private void setTextBox(){
		textBox.setText(SpellChecker.checkFile(comboBox.getValue()));
	}

}
