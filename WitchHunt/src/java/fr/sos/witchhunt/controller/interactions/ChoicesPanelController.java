package fr.sos.witchhunt.controller.interactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.scenes.ActionButton;
import fr.sos.witchhunt.view.gui.scenes.ChoicesPanel;

/**
 * <p><b>Gathers user-input from an instance of {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel} active on the Graphical User Interface.</b></p>
 * <p>When a user clicks on a button, {@link #post(String) sends} to the {@link fr.sos.witchhunt.controller.InputMediator InputMediator} the integer corresponding to the
 * button's position in the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}.
 * This integer corresponds to a choice in the {@link Menu} from which the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#renderActionButtons() ChoicesPanel's buttons are created}.</p>
 * <p>Can be in concurrence with {@link fr.sos.witchhunt.view.std.InterruptibleStdInput Std input} : <code>{@link fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) see ConcreteInputMediator::makeChoice(Menu)}</code>.
 * The first instance of <code>{@link fr.sos.witchhunt.gui.view.InputSource InputSource}</code> to {@link fr.sos.witchhunt.gui.view.InputSource#post(String) post} rules over all others.</p>
 * 
 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
 * @see fr.sos.witchhunt.view.gui.scenes.ActionButton ChoicesPanel {@code <#>--} ActionButtons
 * @see Menu
 *
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
 * @see fr.sos.witchhunt.gui.view.std.InterruptibleStdInput InterruptibleStdInput
 * @see fr.sos.witchhunt.gui.view.std.InputSource InputSource
 */
public class ChoicesPanelController implements InputSource {
	/**
	 * <p>The instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator} that is
	 * {@link fr.sos.witchhunt.controller.InputMediator#wait() awaiting} for {@link fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) an integer choice}
	 * and {@link fr.sos.witchhunt.controller.InputMediator#receive() managing the concurrence between all views}.</p>
	 * 
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.InputMediator#wait() InputMediator::wait()
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.controller.InputMediator#receive() InputMediator::receive()
	 */
	private InputMediator inputMediator;
	/**
	 * The list of {@link fr.sos.witchhunt.view.gui.scenes.ActionButton ActionButtons} of a {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}.
	 * @see fr.sos.witchhunt.view.gui.scenes.ActionButton ActionButton
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
	 */
	private List<ActionButton> bList;

	
	/**
	 * <p><b>Creates the controller and starts listening on each button.</b></p>
	 * <p>On click, {@link #post(String) posts} the integer corresponding to the buttons' position in the {@link #bList buttons list}.</p>
	 * @param bList Value for field {@link #bList}
	 * @param im Value for field {@link #inputMediator}
	 * 
	 * @see #post(String)
	 * @see java.awt.event.ActionListener ActionListener
	 * @see fr.sos.witchhunt.view.gui.scenes.ActionButton#addActionListener(ActionListener) ActionButton::addActionListener(ActionListener)
	 */
	public ChoicesPanelController(List<ActionButton> bList,InputMediator im) {
		this.inputMediator=im;
		this.bList=bList;
		for(int i=0;i<bList.size(); i++) {
			ActionButton b = bList.get(i);
			final int choice = i;
			b.addActionListener( new ActionListener (){
				@Override
				public void actionPerformed(ActionEvent e) {
					post(Integer.toString(choice+1));
				}
			});
		}
	}
	
	/**
	 * Removes each button's {@link java.awt.event.ActionListener ActionListener} to make sure input is sent only once.
	 * Called from the exterior.
	 * @see java.awt.event.ActionListener ActionListener
	 * @see fr.sos.witchhunt.view.gui.scenes.ActionButton#removeActionListener(ActionListener) ActionButton::removeActionListener(ActionListener)
	 */
	public void destroyMouseListeners() {
		bList.forEach(b->
		{
			for(ActionListener al : b.getActionListeners()) {
				b.removeActionListener(al);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post() {
		inputMediator.receive();
	}
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post(Player p) {

	}
}
