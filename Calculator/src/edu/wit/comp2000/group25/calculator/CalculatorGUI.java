package edu.wit.comp2000.group25.calculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculatorGUI extends Application {
	//FORMATTING VARIABLES
	/** padding above middle button rows */
	private int padAboveBtns = 5;
	/** padding above the top row of buttons and below the bottom row of buttons */
	private int padBlwAbvBtmTopRow = 10;
	/** padding on the left and right sides */
	private int padLeftRight = 10;
	/** padding for inside the text field */
	private int padTxtField = 10;
	/** size of the buttons (they're always square) */
	private int buttonSize = 60;
	/** spacing between the left and right sides of buttons */
	private int spaceBetweenButtons = 5;
	/** font size of buttons and text field */
	private int fontSize = 16;
	
	/** Buttons*/
	private Button[] firstRowButtons = {new Button("C"), new Button("<"), new Button("("), new Button(")")};
	private Button[] secondRowButtons = {new Button("7"), new Button("8"), new Button("9"), new Button("/")};
	private Button[] thirdRowButtons = {new Button("4"), new Button("5"), new Button("6"), new Button("*")};
	private Button[] fourthRowButtons = {new Button("1"), new Button("2"), new Button("3"), new Button("-")};
	private Button[] fifthRowButtons = {new Button("0"), new Button("."), new Button("="), new Button("+")};
	/** Array of all the button arrays */
	private Button[][] buttons = {firstRowButtons, secondRowButtons, thirdRowButtons, fourthRowButtons, fifthRowButtons};
	/** HBoxs */
	private HBox[] rows = {new HBox(), new HBox(), new HBox(), new HBox(), new HBox()};
	/** TextField */
	private TextField txtField = new TextField();
	/** Hold stage for methods */
	private Stage primaryStage;
	/** String to hold input for TextField */
	private String currentInput = "";
	/** True if an expression was just evaluated */
	private boolean equalLastPressed = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try{
			this.primaryStage = primaryStage;
			BorderPane pane = new BorderPane();
			/**EventHandler for all of the buttons */
			EventHandler<ActionEvent> buttonEvent = new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					Button btnPressed = (Button)arg0.getSource();
					updateCurrentInput(btnPressed.getText());
					txtField.requestFocus(); //Keep text field as focus for event
					txtField.deselect(); //Deselect text
				}
			};
			/** EventHandler for key presses */
			EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent event) {
					String key = "";
					switch(event.getCharacter().charAt(0)){
						case 13: //Enter key
							key = "=";
							break;
						case 8: //Backspace key
							key = "<";
							break;
						default:
							key = event.getCharacter().toUpperCase();
					}
					updateCurrentInput(key);
				}

			};
			
			//Add each row of buttons to the hbox rows
			for(int x = 0; x < rows.length; x++){
				rows[x].getChildren().addAll(buttons[x]);
			}
			//set up vertical line of buttons
			VBox vbox = new VBox();
			vbox.getChildren().addAll(rows);
			
			//add elements to the pane
			pane.setTop(txtField);
			pane.setCenter(vbox);
			
			//FORMATTING:
			//Formatting for HBoxs
			for(HBox box : rows){
				if(box == rows[4]){ //Last Row of Buttons
					box.setPadding(new Insets(padAboveBtns, 0, padBlwAbvBtmTopRow, 0));
				}else if(box == rows[0]){ //First Row of Buttons
					box.setPadding(new Insets(padBlwAbvBtmTopRow, 0, 0, 0));
				}else{ //Middle rows of Buttons
					box.setPadding(new Insets(padAboveBtns, 0, 0, 0));
				}
				box.setSpacing(spaceBetweenButtons);
				//Formatting for individual buttons
				for(Button button : box.getChildren().toArray(new Button[0])){
					button.setMinSize(buttonSize, buttonSize);
					String buttonText = button.getText();
					//Set special buttons to bold and bigger font
					if(buttonText.equals("=") || buttonText.equals("C") || buttonText.equals("<")){
						button.setStyle("-fx-font-weight: bold; -fx-font-size: " + (fontSize + 8));
					}
					else{
						button.setFont(new Font(fontSize));
					}
					button.setOnAction(buttonEvent); //Action for buttons
				}
			}
			//Padding for side of buttons
			pane.setPadding(new Insets(0, padLeftRight, 0, padLeftRight));
			//Formatting for TextField
			BorderPane.setMargin(txtField, new Insets(padBlwAbvBtmTopRow, 0, 0, 0));
			txtField.setEditable(false);
			txtField.setPadding(new Insets(padTxtField));
			txtField.setFont(new Font(fontSize));
			txtField.setOnKeyTyped(keyEvent); //Tie keyevent method to textfield
			
			//Setup scene and stage
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("calculator.png"));
			primaryStage.setResizable(false); //Non-Resizable
			primaryStage.sizeToScene(); //Resize to scene because of non resizable bug
			primaryStage.setTitle("Calculator");
			primaryStage.show();
			
			//change txtField maxWidth as width - padding of buttons
			txtField.setMaxWidth(rows[0].getWidth() - (rows[0].getPadding().getLeft()*2));
		}catch(Exception ex){
			System.err.println(ex);
		}
	}
	
	/** Given an inputed string, update the text box
	 *  @param input  Any key press or button press */
	private void updateCurrentInput(String input){
		String validInputs = "1234567890+-/*=C<.()";
		if(!validInputs.contains(input)){
			return; //If input isn't valid just return
		}
		//Clear the text after you type after evaluating something
		if(equalLastPressed && !input.equals("=")){
			String operators = "+-*/";
			if(!operators.contains(input)){ //if it is an operator, use evaluated number
				currentInput = "";
			}
			equalLastPressed = false;
		}
		switch(input){
			case "C": //Clear
				currentInput = "";
				break;
			case "<": //Backspace
				if(currentInput.length() != 0)
					currentInput = currentInput.substring(0, currentInput.length() - 1);
				break;
			case "Q": //Quit
				primaryStage.close();
				break;
			case "=": //Evaluate
				try{
					double answer = Calculator.evaluate(currentInput);
					if(answer%1 != 0){
						currentInput = String.format("%f", answer);
					}else{
						currentInput = ""+(int)answer;
					}
					equalLastPressed = true; //equal pressed, clear text next time
					break;
				}catch(Exception e){ //Catch bad expressions
					currentInput = "ERROR";
					equalLastPressed = true;
					break;
				}
			default: //Numbers or operators
				currentInput = currentInput + input;
		}
		txtField.setText(currentInput);
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
