package edu.wit.comp2000.group13.lists;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/** <b>GUI Class</b></br>
 * 	GUI game
 * 
 *  @author Damien Sardinha and Andrew Mellen
 *  @date November 14, 2016
 *  @class COMP2000 */
public class UnoGUI extends Application{
	private int totalPlayers;
	private Player currentPlayer;
	private Card cardInPlay;
	private Label playerNameLbl;
	private ImageView cardInPlayImage;
	private Rectangle colorRect = new Rectangle();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//setup the pane
		BorderPane root = new BorderPane();
	    Scene scene = new Scene(root, 850, 850);
	    root.autosize();
	    
	  	startPage(root, primaryStage);
	  	
	  	
	  	primaryStage.setScene(scene);
	  	primaryStage.show();
	}
	
	/**Creates the startPage*/
	private void startPage(BorderPane root, Stage primaryStage){
		VBox startUpMenu = new VBox(20);
		Button startGame = new Button("Start New Game!");
		startGame.setOnAction((event) -> {
		  	   root.getChildren().clear();
		  	   settingsPage(root, primaryStage);
		 });
		Button quitGame = new Button("Exit!");
		quitGame.setOnAction((event) -> {    
	  		System.exit(0);
	  	});
		ImageView gameLogo = new ImageView(new Image("uno_logo.jpg"));
		startUpMenu.getChildren().addAll(gameLogo, startGame, quitGame);
		startUpMenu.setAlignment(Pos.CENTER);
		primaryStage.setTitle("Welcome to Uno!");
		root.setCenter(startUpMenu);
	}
	
	/**Creates the settingsPage*/
	private void settingsPage(BorderPane root, Stage primaryStage){
		VBox settingsMenu = new VBox(20);
				
		Label numberOfPlayers = new Label("How many players will be in the game?");
		numberOfPlayers.setAlignment(Pos.CENTER);
				
		TextField playerNamesField = new TextField();
		Label listPlayers = new Label("List Players Names(follow by spaces)(type \"AI\" for computer players): ");
		
		// Player Slider
		Slider playerSelector = new Slider();
		playerSelector.setMin(2);
		playerSelector.setMax(10);
		playerSelector.setValue(3);
		playerSelector.setMajorTickUnit(1);
		playerSelector.setSnapToTicks(true);
		playerSelector.setShowTickLabels(true);
		playerSelector.setShowTickMarks(true);
		playerSelector.setMajorTickUnit(1);
		playerSelector.setMinorTickCount(0);
		playerSelector.setBlockIncrement(1);
		
		//Play Game button on the configuration screen
		Button playGame = new Button("Play Game!");
		playGame.setAlignment(Pos.CENTER);
		playGame.setOnAction((event) -> {
			Double d = playerSelector.getValue();
			totalPlayers = d.intValue();

			// Clears the window
			root.getChildren().clear();

			// calls the game class
			gamePage(root, primaryStage, playerNamesField.getText());
			//updateDeckCard();
			//updateMainPlayerName();
		});
		
		settingsMenu.getChildren().addAll(numberOfPlayers, playerSelector, listPlayers, playerNamesField, playGame);
		settingsMenu.setAlignment(Pos.CENTER);
		primaryStage.setTitle("Settings");
		root.setCenter(settingsMenu);
	}
	
	/**Creates the gamePage*/
	private void gamePage(BorderPane root, Stage primaryStage, String names){
		startGame(totalPlayers, names);
		currentPlayer = Game.getCurrentPlayerForGUI(); 
		cardInPlay = Game.getCardInPlay();
		Game.doSpecialActionAsFirstCard("");
		currentPlayer = Game.getCurrentPlayerForGUI();
		cardInPlay = Game.getCardInPlay();
		
		playerNameLbl = new Label(currentPlayer.getName() + "'s Turn.");
		playerNameLbl.setStyle("-fx-font-size: 20");
		
		HBox cardHBox = new HBox(15);
		updateCardHBox(cardHBox, currentPlayer.getLowIndexGUI(), currentPlayer.getHighIndexGUI());
		
		ImageView deckImage = new ImageView("uno_backcard.jpg");
		//Draw a card by pressing deck image, move hand hbox to pos to see new card
		deckImage.setOnMouseClicked(e -> {
			Card drawnCard = Game.drawCard(currentPlayer);
			int cardIndex = currentPlayer.getHand().indexOf(drawnCard);
			if(cardIndex < 3)
				cardIndex = 3;
			if(cardIndex > currentPlayer.getHand().getSize() - 4)
				cardIndex = currentPlayer.getHand().getSize() - 4;
			currentPlayer.setLowIndexGUI(cardIndex - 3);
			currentPlayer.setHighIndexGUI(cardIndex + 3);
			if(currentPlayer.getLowIndexGUI() < 0)
				currentPlayer.setLowIndexGUI(0);
			if(currentPlayer.getHighIndexGUI() > currentPlayer.getHand().getSize())
				currentPlayer.setHighIndexGUI(currentPlayer.getHand().getSize() - 1);
			updateCardHBox(cardHBox, currentPlayer.getLowIndexGUI(), currentPlayer.getHighIndexGUI());
		});
		
		//Rectangle
		colorRect.setHeight(100);
		colorRect.setWidth(100);
		setRectangleColor();
		
		cardInPlayImage = new ImageView("imgs/" + cardInPlay.getColor() + cardInPlay.getCardsNumber() + ".png");
		
		//Move hand Buttons
		Button moveHandRight = new Button("Move Hand ->");
		moveHandRight.setOnAction(e -> {
			currentPlayer.setLowIndexGUI(currentPlayer.getLowIndexGUI() + 7);
			currentPlayer.setHighIndexGUI(currentPlayer.getHighIndexGUI() + 7);
			if(currentPlayer.getHighIndexGUI() >= currentPlayer.getHand().getSize()){
				currentPlayer.setHighIndexGUI(currentPlayer.getHand().getSize() - 1);
				currentPlayer.setLowIndexGUI(currentPlayer.getHighIndexGUI() - 6);
				if(currentPlayer.getLowIndexGUI() < 0)
					currentPlayer.setLowIndexGUI(0);
			}
			updateCardHBox(cardHBox, currentPlayer.getLowIndexGUI(), currentPlayer.getHighIndexGUI());
		});
		Button moveHandLeft= new Button("<- Move Hand");
		moveHandLeft.setOnAction(e -> {
			currentPlayer.setLowIndexGUI(currentPlayer.getLowIndexGUI() - 7);
			currentPlayer.setHighIndexGUI(currentPlayer.getHighIndexGUI() - 7);
			if(currentPlayer.getLowIndexGUI() < 0){
				currentPlayer.setLowIndexGUI(0);
				if(currentPlayer.getHighIndexGUI() >= currentPlayer.getHand().getSize())
					currentPlayer.setHighIndexGUI(currentPlayer.getHand().getSize() - 1);
				else
					currentPlayer.setHighIndexGUI(6);
			}
			updateCardHBox(cardHBox, currentPlayer.getLowIndexGUI(), currentPlayer.getHighIndexGUI());
		});
		
		
		HBox topStuff = new HBox();
		topStuff.getChildren().add(playerNameLbl);
		topStuff.setAlignment(Pos.CENTER);
		root.setTop(topStuff);
		HBox deckAndCardInPlay = new HBox(20);
		deckAndCardInPlay.getChildren().addAll(deckImage, cardInPlayImage, colorRect);
		deckAndCardInPlay.setAlignment(Pos.CENTER);
		root.setCenter(deckAndCardInPlay);
		HBox cardsAndButtons = new HBox(10);
		cardsAndButtons.getChildren().addAll(moveHandLeft, cardHBox, moveHandRight);
		cardsAndButtons.setAlignment(Pos.CENTER);
		root.setBottom(cardsAndButtons);
		primaryStage.setFullScreen(true);
	}
	
	/**Updates the Card HBox to the indices*/
	private void updateCardHBox(HBox cardHBox, int begIndex, int endIndex){
		cardHBox.getChildren().clear();
		for(int x = begIndex; x <= endIndex; x++){
			Card card = currentPlayer.getHand().get(x);
			ImageView newCard = new ImageView("imgs/" + card.getColor() + card.getCardsNumber() + ".png");
			cardHBox.getChildren().add(newCard);
			
			newCard.setOnMouseClicked(e -> {
				Card cardSelected = currentPlayer.getHand().get(cardHBox.getChildren().indexOf(newCard) + begIndex);
				String color = "";
				
				if(cardSelected.getCardsNumber() >= 13){ //wild card
					double xPos = e.getX();
					double yPos = e.getY();
					boolean right = false;
					boolean top = false;
					
					if(xPos > 133 / 2.0){
						right = true;
					}
					if(yPos < 100){
						top = true;
					}
					if(top && right){
						color = "blue";
					}
					if(top && !right){
						color = "red";
					}
					if(!top && !right){
						color = "yellow";
					}
					if(!top && right){
						color = "green";
					}
				}
				
				//Check if the play is valid
				Card cPlaced = Game.playCardForGUI(cardHBox.getChildren().indexOf(newCard) + begIndex, color);
				if(cPlaced != null){ //play was valid
					cardInPlay = cPlaced;
					setRectangleColor();
					if(cPlaced.getCardsNumber() >= 13)
						cardInPlayImage.setImage(new Image("imgs/none" + cardInPlay.getCardsNumber() + ".png"));
					else
						cardInPlayImage.setImage(new Image("imgs/" + cardInPlay.getColor() + cardInPlay.getCardsNumber() + ".png"));
					if(currentPlayer.getHighIndexGUI() >= currentPlayer.getHand().getSize()){
						currentPlayer.setHighIndexGUI(currentPlayer.getHand().getSize() - 1);
						currentPlayer.setLowIndexGUI(currentPlayer.getHighIndexGUI() - 6);
						if(currentPlayer.getLowIndexGUI() < 0)
							currentPlayer.setLowIndexGUI(0);
					}
				}
				currentPlayer = Game.getCurrentPlayerForGUI();
				playerNameLbl.setText(currentPlayer.getName() + "'s Turn.");
				updateCardHBox(cardHBox, currentPlayer.getLowIndexGUI(), currentPlayer.getHighIndexGUI());
			});
			
		}
	}
	
	/**set the color of the rectangle*/
	private void setRectangleColor(){
		switch(cardInPlay.getColor()){
			case "red":
				colorRect.setFill(Color.RED);
				break;
			case "blue":
				colorRect.setFill(Color.BLUE);
				break;
			case "green":
				colorRect.setFill(Color.GREEN);
				break;
			case "yellow":
				colorRect.setFill(Color.YELLOW);
				break;
		}
	}
	
	/**Starts the game from the Game class*/
	private void startGame(int playerCount, String names) {
		//Takes a long string of names and converts it into an array
	 	String[] n;
	 	if(names.isEmpty()){
	 		n = new String[playerCount];
	 	}
	 	else{
	 		n = names.split(" ");
	 	}
	 
		Game.newPlayersSetup(playerCount, n);
		Game.newRound();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
