package fr.sos.witchhunt.controller.interactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.sos.witchhunt.view.gui.scenes.game.DeckSelectorButton;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;

/**
 * <p><b>Gather user-input from a DeckSelectorButton of a DeckSelectorPanel {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel (internal classes of GamePanel)} active on the Graphical User Interface.</b></p>
 * <p>When clicked, these buttons will allow selecting a {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} (a {@link fr.sos.witchhunt.model.players.Player#getHand() player's hand} or 
 * {@link fr.sos.witchhunt.model.flow.Tabletop#getPile() the pile}).
 * The calling {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel} will show the DeckPanel corresponding to this deck inside of its CardsPanel.</p>
 * <p>Doesn't interact with the model.</p>
 * 
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel {@code <#>--} DeckSelectorPanel {@code <#>--} DeckSelectorButton
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel {@code <#>--} CardsPanel {@code <#>--} DeckPanel 
 */
public class DeckSelectorButtonController {

	/**
	 * <p>Adds an {@link java.awt.event.ActionListener} to the given DeckSelectorButton.</p>
	 * <p>When the button is clicked, the calling GamePanel will set this button as selected, or unselect it, and will update its CardPanel.</p>
	 * <p>This controller will be active while the calling GamePanel is.</p> 
	 * @param b The DeckSelectorButton of the calling GamePanel's DeckSelectorPanel
	 * @param gp The calling GamePanel
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel {@code <#>--} DeckSelectorPanel {@code <#>--} DeckSelectorButton
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel {@code <#>--} CardsPanel {@code <#>--} DeckPanel 
	 */
	public DeckSelectorButtonController(DeckSelectorButton b,GamePanel gp) {
		b.addActionListener( new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gp.getSelectedDeckButton()!=b) gp.setSelectedDeckButton(b);
				else gp.setSelectedDeckButton(null);
			}
		});
	}


}
