package fr.sos.witchhunt.controller.interactions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;
import fr.sos.witchhunt.view.gui.scenes.game.RenderedCard;
/**
 * <p><b>Gathers user-input from an instance of DeckPanel {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel (internal class of GamePanel)} active on the Graphical User Interface.</b></p>
 * <p>When a user clicks on a card's picture, depending on which constructor this controller was instantiated by :</p> 
 * <p>- Displays the {@link fr.sos.witchhunt.view.gui.scenes.game.RenderedCard RenderedCard} as {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#setSelectedCard(RenderedCard) selected} (boldened border)</p>
 * <p>- If this controller's constructor was given an instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator}, also {@link #post(String) sends} to it the integer corresponding to the</p>
 * card's position in the {@link #jList list of choosable rendered cards}. Used for choosing cards directly using their picture instead of a {@link ChoicesPanelController menu made of buttons}.
 * This integer corresponds to a choice in the {@link Menu} containing all choosable cards ordered the same way.</p>
 * <p>Can be in concurrence with {@link fr.sos.witchhunt.view.std.InterruptibleStdInput Std input} : <code>{@link fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) see ConcreteInputMediator::makeChoice(Menu)}</code>.
 * The first instance of <code>{@link fr.sos.witchhunt.gui.view.InputSource InputSource}</code> to {@link fr.sos.witchhunt.gui.view.InputSource#post(String) post} rules over all others.</p>
 * 
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel.CardsPanel.DeckPanel
 * @see fr.sos.witchhunt.view.gui.scenes.game.RenderedCard DeckPanel {@code < >--} RenderedCard
 * 
 * @see Menu
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
 * @see fr.sos.witchhunt.gui.view.std.InterruptibleStdInput InterruptibleStdInput
 * @see fr.sos.witchhunt.gui.view.std.InputSource InputSource
 */
public class CardSelectorController implements InputSource {
	/**
	 * <p>The instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator} that is
	 * {@link fr.sos.witchhunt.controller.InputMediator#wait() awaiting} for {@link fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) an integer choice}
	 * and {@link fr.sos.witchhunt.controller.InputMediator#receive() managing the concurrence between all views}.</p>
	 * <p>If <code>null</code>, the controller is not used for making a choice and transfer it to the model : it is only used to display a {@link fr.sos.witchhunt.view.gui.scenes.game.RenderedCard card} as 
	 * {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#setSelectedCard(RenderedCard) selected}, interacting only with the {@link fr.sos.witchhunt.view.gui GUI view}.</p>
	 * 
	 * @see #CardSelectorController(List, GamePanel, InputMediator)
	 * @see #CardSelectorController(List, GamePanel)
	 * 
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.InputMediator#wait() InputMediator::wait()
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.controller.InputMediator#receive() InputMediator::receive()
	 */
	private InputMediator inputMediator=null;
	/**
	 * <p>The list of {@link fr.sos.witchhunt.model.view.scenes.game.RenderedCard RenderedCard} covered by this controller.</p>
	 * <p>- If the controller is {@link #CardSelectorController(List, GamePanel, InputMediator) used for making a choice to transfer to the model}, only choosable cards are covered.</p>
	 * <p>- Otherwise, any card can be selected, since it is disconnected from the {@link fr.sos.witchhunt.model model} and has holds sway only on the {@link fr.sos.witchhunt.view.gui GUI view}.</p>
	 */
	private List<RenderedCard> jList;
	/**
	 * This map associates, to {@link #jList each RenderedCard covered by this controller}, one MouseListener.
	 * It is used for {@link #destroyThisControllersMouseListeners() killing only the MouseListeners created by this instance of controller,} once a choice has been made.
	 * @see java.awt.event.MouseListener
	 * @see #destroyThisControllersMouseListeners()
	 */
	private Map<RenderedCard,MouseListener> thisControllersMLMap = new HashMap<RenderedCard,MouseListener>();
	
	/**
	 * <p>With this constructor, this controller is <b>not used for making a choice and transfer it to the model</b> : it is only used to display a {@link fr.sos.witchhunt.view.gui.scenes.game.RenderedCard card} as 
	 * {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#setSelectedCard(RenderedCard) selected}, <b>interacting only with the {@link fr.sos.witchhunt.view.gui GUI view}</b>.</p>
	 * <p>Creates a MouseListener for {@link #jList each RenderedCard} : on click, displays a {@link fr.sos.witchhunt.view.gui.scenes.game.RenderedCard card} as 
	 * {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#setSelectedCard(RenderedCard) selected}.</p>
	 * <p>These MouseListeners will remain active as long as the concerned {@link fr.sos.witchhunt.view.gui.scenes.game.RenderedCard are displayed}.
	 * The {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel} creating this controller can decide to {@link #destroyAllMouseListeners() kill all MouseListeners} of each RenderedCard covered by this controller.</p>
	 * @param jList Value for field {@link #jList}
	 * @param gp The instance of {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel} (GUI view) instantiating this controller.
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#setSelectedCard(RenderedCard) GamePanel::setSelectedCard(RenderedCard)
	 * @see #destroyAllMouseListeners()
	 * @see java.awt.event.MouseListener
	 */
	public CardSelectorController(List<RenderedCard> jList,GamePanel gp) {
		this.jList=jList;
		for(int i=0; i<jList.size(); i++) {
			RenderedCard j = jList.get(i);
			final int choice = i;
			j.addMouseListener(new MouseListener() {
	
				@Override
				public void mouseClicked(MouseEvent e) {
					gp.setSelectedCard(j);
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			});
		}
	}
	
	public CardSelectorController(List<RenderedCard> jList,GamePanel gp,InputMediator im) {
		this.jList=jList;
		this.inputMediator=im;
		for(int i=0; i<jList.size(); i++) {
			RenderedCard j = jList.get(i);
			final int choice = i;
			
			MouseListener ml = new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//if(gp.getSelectedCard()!=j) {
						post(Integer.toString(choice+1));
						gp.cardHasBeenChosen(j);
						destroyThisControllersMouseListeners();
				//	}
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			};
			
			this.thisControllersMLMap.put(j, ml);
			j.addMouseListener(ml);
			
		}
	}
	
	public void destroyAllMouseListeners() {
		this.jList.forEach(j->{
			for(MouseListener ml : j.getMouseListeners()) {
				j.removeMouseListener(ml);
			}
		});
	}

	private void destroyThisControllersMouseListeners() {
		this.thisControllersMLMap.keySet().forEach(j->{
			j.removeMouseListener(thisControllersMLMap.get(j));
		});
	}
	
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}

	@Override
	public void post() {
		inputMediator.receive();
	}

	@Override
	public void post(Player p) {
		// TODO Auto-generated method stub
		
	}
	
}
