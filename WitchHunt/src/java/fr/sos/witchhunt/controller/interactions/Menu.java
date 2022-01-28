package fr.sos.witchhunt.controller.interactions;

/**
 * <p><b>Represents a menu with a title and plural choices.</b></p>
 * <p>Used for displaying possibilities and asking the user to make a decision.</p>
 * <p>When created from the {@link fr.sos.witchhunt.model 'model' package}, the possibilities offered are considered <b>part of the game's rules.</b></p>
 * <p>When created from the {@link fr.sos.witchhunt.controller 'controller' package}, the menu is considered as an application's menu.</p>
 * <p>The way a menu is rendered <b>depending on the type of possibilities it offers</b> is completely handled by the {@link fr.sos.witchhunt.view classes of the 'view' package}.</p>
 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) <code>DisplayMediator::displayPossibilities(Menu)</code> for menus that are part of the game's rules (model level) 
 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) <code>DisplayMediator::displayMenu(Menu)</code> for application-scale menus (controller level)
 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) <code>InputMediator::makeChoice(Menu)</code> for gathering the user's unique choice within a menu.
 */
public class Menu {
	
	/**
	 * <b>This menu's entitled</b>
	 */
	private String name;
	/**
	 * <p><b>The possibilities offered by this menu.</b></p>
	 * <p>Can be objects of any kind.
	 * The view will render these possibilities differently depending on the class, enum or interface they are an instance of.</p>
	 * <p>The objects within this array have a fixed position and their order is important.</p>
	 */
	private Object [] options;
	private int optionsCount;
	
	/**
	 * <b>Builds a menu with its entitled and a various, fixed, number of options.</b>
	 * @param name This menu's entitled
	 * @param options When instantiating, any constructor argument after the menu's name will be added to the {@link #options array of possibilities}, in the same order as the arguments. 
	 */
	public Menu(String name, Object ... options) {
		this.name = name;
		this.options=options;
		this.optionsCount=options.length;
	}

	
	//GETTERS
	public String getName() {
		return name;
	}
	public Object[] getOptions() {
		return options;
	}
	public int getOptionsCount() {
		return optionsCount;
	}
	/**
	 * Returns the object of the given position within the {@link #options possibilities array}. 
	 * @param n The position of the object in the {@link #options possibilities array}. <b>Starts at 1 : equals index + 1.</b>
	 * @return {@link #options options[n-1]}, an object of any kind.
	 */
	public Object getNthOption(int n) {
		if (n>=1 && n<= optionsCount) {
			return options[n-1];
		}
		else return null;
	}

}
