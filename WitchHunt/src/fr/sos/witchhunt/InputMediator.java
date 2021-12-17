package fr.sos.witchhunt;

import java.util.List;

import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Interface implemented by the central input controller</b></p>
 * <p>Makes the link between the Model's components and the View's components..</p>
 * <p>Specifies the methods required by the Model's components which require user inputs.</p>
 * 
 * @see InputMediator
 * @see fr.sos.witchhunt.controller.InputController
 */
public interface InputMediator {
	
	/**
	 * <b>Answers a yes-no question based on user-input.</b>
	 * @return <i>true</i> if the user has chosen "yes", <i>false</i> if they have chosen "no".
	 */
	public boolean answerYesNoQuestion();
	
	/**
	 * <b>Asks for user-input from the view before continuing further.</b>
	 */
	public void wannaContinue();
	
	/**
	 * <p><b>Requests the View to gather information from user-input in order to create a player to add to the game.</b></p>
	 * @param id The id of the player that is to be created.
	 * @param chosenNames The list of all already taken player names.
	 * @return The player, created on the basis of the information gathered from user-input
	 * @see fr.sos.witchhunt.model.players.Player
	 */
	public Player createPlayer(int id,List<String> chosenNames);
	
	/**
	 * <p><b>Requests the View to get an input from the user corresponding to a choice of the given Menu</b></p>
	 * <p>Only calls for input and display error messages concerning that input</p>
	 * <p>The display of the menu itselfs is achieved by a class implementing{@link DisplayMediator}</p>
	 * @param possibilities An instance of Menu, with a given title and given options
	 * @return A positive <i>int</i> corresponding to the position of the selected option in the given Menu's array of choices.
	 */
	public int makeChoice(Menu possibilities);
	
}
