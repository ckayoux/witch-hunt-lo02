package fr.sos.witchhunt.view.gui.scenes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.ChoicesPanelController;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
/**
 * <p><b>This class defines a visual representation for {@link fr.sos.witchhunt.controller.interactions.Menu Menus} on the GUI View.</b></p>
 * <p>Is an extension of {@link javax.swing.JPanel JPanel} adapted to fit into a scene that is an extension of {@link GridBagPanel}. (see {@link GridBagCell})</p>
 * <p>Used by the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel ingame scene}, in order to represent a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} letting the
 * user choose an action to perform in the match among plural possibilities.</p>
 * <p>Used by the {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel main menu scene} in order to navigate into the application as well.</p> * 
 * <p>Displays the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} as a JLabel placed at the top of the cell.</p>
 * <p>Also displays each {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu entry} , which is represented by a corresponding {@link ChoiceButton}.</p>
 * <p>Collects user-input from the graphical interface using a single instance of {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController}.</p>
 * @see GridBagCell
 * @see GridBagPanel
 * 
 * @see ChoiceButton this {@code <#>--} ChoiceButtons
 * @see fr.sos.witchhunt.controller.interactions.ChoicesPanelController
 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
 *  
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#displayMenu(Menu) GamePanel::displayMenu(Menu)
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#displayCards(Menu) GamePanel::displayCards(Menu)
 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel#displayMenu(Menu) MainMenuPanel::displayMenu(Menu)
 */
public class ChoicesPanel extends GridBagCell {
	/**
	 * <b>The text displayed at the top of the cell, into a JLabel.</b>
	 * <b>Set to the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} when one is active or to an empty String the rest of the time.</b>
	 * @see javax.swing.JLabel
	 * @see fr.sos.witchhunt.controller.interactions.Menu#getName() Menu::getName()
	 */
	protected JLabel prompt = new JLabel("",SwingConstants.CENTER);
	/**
	 * <p><b>The list of all {@link ChoiceButton} to be rendered.</b></p>
	 * <p>Each {@link ChoiceButton} represents an {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() entry of the active Menu}.</p>
	 * @see ChoiceButton
	 * @see #renderActionButtons()
	 */
	protected List<ChoiceButton> choiceButtonsList = new ArrayList<ChoiceButton>();
	/**
	 * <p><b>A list containing vertical margins to dispose between each {@link ChoiceButton} when rendering.</b></p>
	 * @see #renderActionButtons()
	 */
	private List<Component> interButtonsMargins = new ArrayList<Component> ();
	
	private boolean isRendered=false;
	
	/**
	 * <p><b>The instance of {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController} in charge of collecting user-input</b>.
	 * <p>Listens for {@link java.awt.event.ActionEvent ActionEvents} on each {@link #choiceButtonsList active ChoiceButton}.</p>
	 * <p>Set when calling {@link #makeChoice(Menu, InputMediator)} and destroyed when calling {@link #choiceHasBeenMade()}.</p>
	 * @see fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController
	 * @see #makeChoice(Menu, InputMediator)
	 * @see #choiceHasBeenMade()
	 */
	private ChoicesPanelController controller = null;
	private boolean choosingACard=false;
	
	/**
	 * <p><b>The cell's content is disposed vertically,</b> following a {@link javax.swing.BoxLayout#PAGE_AXIS BoxLayout's PAGE_AXIS disposition}.</p> 
	 * @see GridBagCell
	 */
	public ChoicesPanel(int x, int y,int w, int h,Border cellBorder,List<GridBagCell> cellsList) {
		super(x,y,w,h,cellBorder,cellsList);
		this.getPan().setLayout(new BoxLayout(this.getPan(),BoxLayout.PAGE_AXIS));	
	}
	
	/**
	 * {@inheritDoc}
	 * Set alignment of contained Components to {@link java.awt.Component#CENTER_ALIGNMENT CENTER_ALIGNMENT}.
	 * Adds a margin before and after the {@link #prompt}.
	 */
	@Override
	public void init() {
		this.getPan().setPreferredSize(this.getPan().getPreferredSize());
		this.prompt.setPreferredSize(this.getPan().getPreferredSize());
		this.getPan().add(Box.createRigidArea(new Dimension(0, 30))); //empty space above prompt
		this.prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.getPan().add(prompt);
		this.getPan().add(Box.createRigidArea(new Dimension(0, 15)));//empty space under prompt
		this.getPan().setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	/**
	 * <b>Displays a single button inviting the users to click it in order to continue playing.</b>
	 * Not based on a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} like most of this classe's features.
	 * @see fr.sos.witchhunt.controller.InputMediator#wannaContinue() InputMediator::wannaContinue()
	 * @param im The {@link fr.sos.witchhunt.controller.InputMediator InputMediator} in charge of collecting user-input from this button
	 * @see ChoiceButton
	 * @see #renderPane()
	 * @see #controller
	 */
	public void wannaContinue(InputMediator im) {
		this.resetPane();
		this.prompt.setText("Wanna continue ?");
		ChoiceButton continueButton =  new ChoiceButton("CONTINUE");
		this.choiceButtonsList.add(continueButton);
		this.controller= new ChoicesPanelController(choiceButtonsList,im);
		this.renderPane();
	}
	
	/**
	 * <p><b>Represents any {@link fr.sos.witchhunt.controller.interactions.Menu Menu} given in parameter into the cell. </b></p>
	 * <p>- Displays the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} using a {@link #prompt JLabel} placed at the top of the cell.</p>
	 * <p>- Makes a {@link ChoiceButton} out of {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() each entry of this Menu} that is not console-only (special char sequence "/c/") and adds it to the {@link #choiceButtonsList list of ChoiceButtons to render}.
	 * The buttons' text and style are set using static methods of the {@link ChoiceButton} class, determining how to represent a choice depending on its type.</p>
	 * <p>- Renders the result</p> 
	 * @param m The {@link fr.sos.witchhunt.controller.interactions.Menu Menu} to be represented into the cell
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see #prompt The Menu's entitled is displayed using a JLabel placed at the top of the cell
	 * @see ChoiceButton
	 * @see ChoiceButton#makeButtonText(Object)
	 * @see ChoiceButton#getButtonColorByActionType(Object)
	 * @see ChoiceButton#setTheme(java.awt.Color)
	 * @see #renderPane()
	 */
	public void displayMenu(Menu m) {
		this.resetPane();
		this.prompt.setText(m.getName());
		for(Object o : m.getOptions()) {
			String buttonText = ChoiceButton.makeButtonText(o);
			if(buttonText!=null) {
				ChoiceButton b =  new ChoiceButton(buttonText);
				b.setTheme(ChoiceButton.getButtonColorByActionType(o));
				this.choiceButtonsList.add(b);
			}
		}
		this.renderPane();
	}
	
	/**
	 * <p><b>Represents a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} containing only entries of type {@link fr.sos.witchhunt.model.cards.RumourCard RumourCard} into the cell. </b></p>
	 * <p>- Displays the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} using a {@link #prompt JLabel} placed at the top of the cell.</p>
	 * <p>- Makes a {@link ChoiceButton} out of {@link ChoiceButton#makeCardButtonText(RumourCard, boolean) each Rumour card contained in this Menu's array of options}.</p>
	 * <p>- Renders the result</p> 
	 * <p>{@link #isChoosingACard()} will be set to true.</p>
	 * @param m The {@link fr.sos.witchhunt.controller.interactions.Menu Menu} to be represented into the cell
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see #prompt The Menu's entitled is displayed using a JLabel placed at the top of the cell
	 * @see ChoiceButton
	 * @see ChoiceButton#makeCardButtonText(RumourCard, boolean)
	 * @see #displayMenu(Menu)
	 */
	public void displayCards(Menu m,boolean forceReveal) {
		this.choosingACard=true;
		this.resetPane();
		this.prompt.setText(m.getName());
		for(Object o : m.getOptions()) {
			String buttonText = ChoiceButton.makeCardButtonText((RumourCard)o,forceReveal);
			if(buttonText!=null) {
				ChoiceButton b =  new ChoiceButton(buttonText);
				b.setTheme(ChoiceButton.getButtonColorByActionType(o));
				this.choiceButtonsList.add(b);
			}
		}
		this.renderPane();
	}
	
	/**
	 * @see ChoiceButton#getButtonColorByActionType(Object)
	 */
	public void setThemeAsForActionType(Object o) {
		this.choiceButtonsList.forEach(b->b.setTheme(ChoiceButton.getButtonColorByActionType(o)));
	}
	
	/**
	 * <b>Instantiates a {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController} to gather user-input within the {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu entries} represented by this cell.</b>
	 * @param The {@link fr.sos.witchhunt.controller.InputMediator InputMediator} sending the input request and waiting for the user's choice.
	 * @see fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 */
	public void makeChoice(InputMediator im) {
		this.controller= new ChoicesPanelController(choiceButtonsList,im);
	}
	
	/**
	 * <p><b>Adds each {@link ChoiceButton} of the {@link #choiceButtonsList list of ChoiceButtons to render} to the cell's content pane</b>, under the {@link #prompt}.</p>
	 * <p>The width of the {@link ChoiceButton ChoiceButtons} is normalized artificially to match that of the longest one by {@link ChoiceButton#setInsets(Insets) modifying each button's internal margins}.</p>
	 * <p>Adds a vertical margin ({@link javax.swing.Box#createRigidArea(Dimension) Box::createRigidArea(Dimension)}) between the buttons.</p>
	 * @see ChoiceButton
	 * @see ChoiceButton#setInsets(Insets)
	 * @see javax.swing.Box#createRigidArea(Dimension) RigidAreas are used as vertical margins disposed between the ChoiceButtons.
	 */
	public void renderActionButtons() {
		
		Iterator<ChoiceButton> it = choiceButtonsList.iterator();
		double maxButtonWidth = Collections.max(choiceButtonsList.stream().mapToDouble(b->b.getPreferredSize().getWidth())
				.boxed().toList());
		while(it.hasNext()) {
			
			ChoiceButton b = it.next();
			Component aboveMargin = Box.createRigidArea(new Dimension(0, 40));
			this.interButtonsMargins.add(aboveMargin);
			this.getPan().add(aboveMargin);
			
			int missingXMargin = (int) (maxButtonWidth - b.getPreferredSize().getWidth());
			int missingLeftMargin = missingXMargin/2 + missingXMargin%2;
			int missingRightMargin = missingXMargin - missingLeftMargin;
			b.setInsets(new Insets(b.getMargin().top,b.getMargin().left + missingLeftMargin, b.getMargin().bottom,b.getMargin().right + missingRightMargin));
			
			this.getPan().add(b);
		}
		
	}
	
	/**
	 * <p><b>Cleans the cell's content.</b></p>
	 * <p>- The {@link #prompt} is set to an empty String.</p>
	 * <p>- Each of the {{@link #choiceButtonsList} rendered ChoiceButtons} is removed.</p>
	 * <p>- Each of the {@link inter-button margins #interButtonsMargins} is removed.</p>
	 * <p>- If there is a {@link #controller} waiting to receive input from within this cell's {@link ChoiceButton ChoiceButtons}, abandons them.</p>
	 */
	public void resetPane() {
		this.prompt.setText("");
		
		this.choiceButtonsList.forEach(b->this.getPan().remove(b));
		this.interButtonsMargins.forEach(b->this.getPan().remove(b));
		this.choiceButtonsList.removeIf(b->true);
		this.interButtonsMargins.removeIf(m->true);
		this.isRendered=false;
		this.controller=null;
		this.getPan().updateUI();
	}
	
	/**
	 * @see #renderActionButtons()
	 */
	public void renderPane() {
		renderActionButtons();
		this.isRendered=true;
	}
	
	public boolean isRendered() {
		return this.isRendered;
	}
	
	/**
	 * @return <i>true</i> if the cell is currently used for choosing a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}, <i>false</i> otherwise.
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#choiceHasBeenMade(Object) Used by the ingame scene to display a chosen card differently on the other cells
	 */
	public boolean isChoosingACard() {
		return this.choosingACard;
	}
	
	/**
	 * <p><b>Called by {@link fr.sos.witchhunt.controller.ConcreteInputMediator the InputMediator collecting input from the current scene} to notify the ChoicesPanel that 
	 * a user has made their choice, from this view or from another.</b>
	 * and that the {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController#destroyMouseListeners() controller should be destroyed} immediately.</p> 
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator ConcreteInputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) ConcreteInputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#choiceHasBeenMade(Object) GamePanel::choiceHasBeenMade(Object)
	 * @see fr.sos.witchhunt.controller.interactions.ChoicesPanelController#destroyMouseListeners() ChoicesPanelController::destroyMouseListeners()
	 */
	public void choiceHasBeenMade() {
		if(this.controller!=null) this.controller.destroyMouseListeners();
		this.controller=null;
		this.choosingACard=false;
	}
	
	public JLabel getPrompt() {
		return this.prompt;
	}
}