package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Menu;

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
	 * <p><b>Requests the View to get an input from the user corresponding to a choice of the given Menu</b></p>
	 * <p>Only calls for input and display error messages concerning that input</p>
	 * <p>The display of the menu itselfs is achieved by a class implementing{@link DisplayMediator}</p>
	 * @param possibilities An instance of Menu, with a given title and given options
	 * @return A positive <i>int</i> corresponding to the position of the selected option in the given Menu's array of choices.
	 */
	public int makeChoice(Menu possibilities);
}
