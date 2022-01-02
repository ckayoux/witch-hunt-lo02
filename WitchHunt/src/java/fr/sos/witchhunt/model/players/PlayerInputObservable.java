package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.Menu;

/**
 * <p><b>Interface implemented by Players and their daughter classes</b></p>
 * <p>Specifies a set of methods allowing the players to communicate with an input controller implementing {@link InputMediator}.</p>
 * <p>All methods specified this interface are meant to trigger their counterpart from the InputMediator interface.</p>
 * @see InputMediator
 * @see PlayerDisplayObservable
 * @see fr.sos.witchhunt.model.players.Player Player
 * @see fr.sos.witchhunt.model.players.CPUPlayer CPUPlayer
 * @see fr.sos.witchhunt.model.players.HumanPlayer HumanPlayer
 */
public interface PlayerInputObservable {
	
	/**
	 * <b>Associate to a PlayerInputObservable entity the given controller implementing InputMediator.</b>
	 * @param im An input controller implementing InputMediator
	 */
	public void setInputMediator(InputMediator im);
	
	/**
	 * <b>Method meant to request the associated input mediator to make a choice based on user input within a restricted set of options</b> 
	 * @param m A Menu with a given title and given options
	 * @return An integer corresponding to the position of the chosen option in the menu's options array
	 * @see InputMediator#makeChoice(Menu m)
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 */
	public int makeChoice(Menu m);
}
