package fr.sos.witchhunt.view.gui.scenes.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Button representing a deck (a {@link fr.sos.witchhunt.model.players.Player#getHand() player's hand} or the {@link fr.sos.witchhunt.model.flow.Tabletop#getPile() pile}.</b></p>
 * <p>Used in the {@link GamePanel GamePanel's deck selector panel}.</p>
 * <p>This class is only in charge of setting the buttons' style. User-input is collected using a dedicated controller ({@link fr.sos.witchhunt.controller.interactions.DeckSelectorButtonController DeckSelectorButtonController}).</p>
 * <p>The background color, as well as the outline color, of a DeckSelectorButtons, can be changed on the basis of a {@link Theme}.</p>
 * <p>The border of a DeckSelectorButton is made thicker when it is selected. See {@link GamePanel#setSelectedDeckButton(DeckSelectorButton)} and {@link GamePanel#boldenPlayer(Player)}.</p>
 * <p>The color changes when a game event concerning the associated player happens : it's the player's turn, they are accused ... See {@link GamePanel#showAccusedPlayer(Player)} for example.</p>
 * @see GamePanel {@literal <#>--->} DeckSelectorPanel {@literal <#>--->} DeckSelectorButton(this)
 * @see GamePanel#showAccusedPlayer(Player)
 * @see GamePanel#showCurrentPlayer(Player)
 * @see GamePanel#showWitchingPlayer(Player)
 * @see GamePanel#showHuntedPlayer(Player)
 * @see GamePanel#resetEffects()
 * 
 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
 * @see fr.sos.witchhunt.model.players.Player Player
 * @see fr.sos.witchhunt.model.players.Player#getHand() Player::getHand()
 * @see fr.sos.witchhunt.model.flow.Tabletop#getPile() Tabletop::getPile()
 * 
 * @see Theme
 * 
 * @see fr.sos.witchhunt.controller.interactions.DeckSelectorButtonController DeckSelectorButtonController
 */
public class DeckSelectorButton extends JButton {
	/**
	 * The instance of {@link fr.sos.witchhunt.model.players.Player Player} associated with this button.
	 * @see fr.sos.witchhunt.model.players.Player Player
	 */
	private Player associatedPlayer;
	/**
	 * The instance of {@link fr.sos.witchhunt.model.cards.RumourCardsPile Player} associated with this button.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 */
	private RumourCardsPile associatedDeck;

	private final static String pileText ="Discarded cards";
	
	/**
	 * Internal margins for the DeckSelectorButtons.
	 */
	private Insets insets = new Insets(10, 20, 10, 20);
	
	/**
	 * Required for styling up the button on mouse pressed / exited from within an anonymous class.
	 */
	private boolean colorChanged=false;
	
	/**
	 * The button's default background color.
	 * If null, it will have a JButton's default look.
	 * @see #makeBackground(Theme)
	 */
	private Color bgColor=null;
	/**
	 * The button's background color when it is hovered.
	 * Derived from the {@link #bgColor default background color}. (lightened).
	 * @see #makeBackground(Theme)
	 */
	private Color hoverColor=null;
	/**
	 * The MouseListener in charge of controlling the button's style on different MouseEvents.
	 * Not in charge of collecting any input from the button, for that, see {@link fr.sos.witchhunt.controller.interactions.DeckSelectorButtonController DeckSelectorButtonController}.
	 * @see #makeBackground(Theme)
	 */
	private MouseListener colorChangeManager=null;
	
	/**
	 * The current border color.
	 */
	private Color currentBorderColor=null;
	/**
	 * The default border for this button if no specific theme is applied.
	 */
	private final Border defaultBorder;
	/**
	 * The current border for this button.
	 */
	private Border currentBorder;
	/**
	 * A DeckSelectorButton can be boldened, which makes its border thicker.
	 * @see GamePanel#setSelectedDeckButton(DeckSelectorButton)
	 * @see GamePanel#boldenPlayer(Player)}.
	 */
	private boolean boldened=false;
	/**
	 * The default thickness for a custom border.
	 */
	private int specialBordersSize = 1;
	/**
	 * The current border's thickness is increased by this number of pixels when the button is {@link #bolden() boldened}.
	 */
	private final int boldenBy = 2;
	
	/**
	 * Makes a DeckSelectorButton associated with the given Player and their hand.
	 * @param p The Player whose hand can be selected using this button.
	 * @see fr.sos.witchhunt.model.players.Player Player
	 * @see fr.sos.witchhunt.model.players.Player#getHand() Player::getHand()
	 */
	public DeckSelectorButton(Player p) {
		super(p.getName());
		this.defaultBorder=this.getBorder();
		this.currentBorder=defaultBorder;
		this.associatedPlayer=p;
		this.associatedDeck=p.getHand();
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	/**
	 * Makes a DeckSelectorButton associated with the given deck.
	 * @param pile The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} which can be selected using this button.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 */
	public DeckSelectorButton(RumourCardsPile pile) {
		super(pileText);
		this.defaultBorder=this.getBorder();
		this.currentBorder=defaultBorder;
		this.associatedPlayer=null;
		this.associatedDeck = pile;
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	/**
	 * Displays the button with custom background color when painting the component.
	 */
	 @Override
     protected void paintComponent(Graphics g) {
		 if(!isContentAreaFilled()) {
			 g.setColor(getBackground());
	         g.fillRect(0, 0, getWidth(), getHeight());
	         super.paintComponent(g);
		 }
		 else super.paintComponent(g);
     }
	
	 /**
	  * <p><b>Sets this button's background color based on the given {@link Theme}. Defines how the style should change on MouseEvents.</b></p>
	  * <p>The color associated to the {@link Theme} is obtained using {@link Notification#getFg()}.</p>
	  * <p>The {@link #bgColor default background color} is set on the basis of a lightened version of that color.</p>
	  * <p>On hover, the button gets darker.</p>
	  * <p>When pressed, the button gets even darker.</p>
	  * <p>The border is made thicker when the button is clicked, and darkened when the background color is.</p>
	  * @param theme The Theme, on the basis of which, the button's style shall be determinated.
	  * @see #bgColor
	  * @see #colorChangeManager
	  * @see #remakeBorder()
	  * @see #currentBorderColor
	  * @see #paintComponent(Graphics)
	  */
	public void makeBackground(Theme theme) {
		Color themeColor = new Notification(theme).getFg();
		float[] hsbVals = new float [3] ;
		Color.RGBtoHSB(themeColor.getRed(),themeColor.getGreen(),themeColor.getBlue(),hsbVals);
		float sat = hsbVals[1];
		float bri = hsbVals[2];
		float lightenedSat = (float) Math.max(0, sat-0.40);	
		float darkenedSat = (float) Math.min(1, sat+0.20);
		float lightenedBri = (float) Math.min(1, bri+0.30);		
		float darkenedBri = (float) Math.max(0, bri-0.30);	
		Color lightenedColor = Color.getHSBColor(hsbVals[0],lightenedSat,lightenedBri);
		Color darkenedColor = Color.getHSBColor(hsbVals[0],darkenedSat,darkenedBri);
		super.setBackground(lightenedColor);
		
		this.bgColor=lightenedColor;
		this.setContentAreaFilled(false);
		
		this.colorChangeManager= new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				setBackground(darkenedColor);
				currentBorderColor=darkenedColor;
				remakeBorder();
				colorChanged=true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setBackground(themeColor);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(themeColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(lightenedColor);
				if(colorChanged) {
					currentBorderColor=themeColor;
					remakeBorder();
					colorChanged=false;
				}
				
			}
			
		};
		this.addMouseListener(colorChangeManager);
	}
	
	
	/**
	 * <b>Updates the button's text on the basis of the represented entities.</b>
	 * <p>If the represented deck is a Player's hand, the button will display that Player's score once this method is called. See {@link GamePanel#updateScoreDisplay(Player, fr.sos.witchhunt.model.flow.ScoreCounter)}.</p>
	 * <p>If the represented deck is the pile, the button will display the number of Rumour cards it contains. See {@link GamePanel#updateDeckContent(RumourCardsPile, boolean)} </p>
	 * @see GamePanel#updateScoreDisplay(Player, fr.sos.witchhunt.model.flow.ScoreCounter)
	 * @see GamePanel#updateDeckContent(RumourCardsPile, boolean)
	 */
	public void updateText() {
		if(this.associatedPlayer!=null) {
			this.setText(associatedPlayer.getName()+"  |  "+associatedPlayer.getScore());
		}
		else {
			this.setText(pileText+" ("+this.associatedDeck.getCardsCount()+")");
		}
	}
	
	/**
	 * <b>Resets the button's style to defaults.</b>
	 * <p>Called at the end of a {@link fr.sos.witchhunt.model.flow.Turn Turn} by {@link GamePanel#resetEffects()}.</p>
	 * <p>Both the background and border color will be reset to defaults.</p>
	 * <p>The {@link #colorChangeManager MouseListener in charge of styling up the button dynamically} is destroyed.</p>
	 * <p>However, the border will remain thicker if the button was {@link #boldened}.</p>
	 * @see GamePanel#resetEffects()
	 * @see #currentBorder
	 * @see #defaultBorder
	 * @see #boldened
	 * @see #colorChangeManager
	 */
	public void resetTheme() {
		this.currentBorder=defaultBorder;
		this.setBorder(defaultBorder);
		this.setContentAreaFilled(true);
		if(this.colorChangeManager!=null)  this.removeMouseListener(colorChangeManager);
		this.colorChanged=true;
		this.setBackground(null);
		if(this.boldened) this.bolden(); //stay boldened even if reset
	}
	
	/**
	 * <p><b>Dresses-up the button based on the given {@link Theme}.</b></p>
	 * <p>Both the button's background and border colors are affected.</p>
	 * @param nt The Theme on the basis of which the button should be styled.
	 * @see Theme
	 * @see Notification#getFg()
	 * @see #makeBackground(Theme)
	 * @see #currentBorder
	 */
	public void makeTheme(Theme nt) {
		this.makeBackground(nt);
		this.currentBorderColor=(new Notification(nt)).getFg();
		LineBorder lb = new LineBorder(this.currentBorderColor,specialBordersSize);
		this.currentBorder=BorderFactory.createCompoundBorder(lb, getSpecialEmptyBorder());

		this.setBorder(currentBorder);

	}
	
	/**
	 * <p><b>Updates only the border.</b></p>
	 * <p>Called when the border's color or thickness has changed, on a MouseEvent (see {@link #makeBackground(Theme)}) or when the button is {@link #bolden() boldened}.</p>
	 * @see #currentBorder
	 * @see #defaultBorder
	 */
	private void remakeBorder() {
		if(this.currentBorder==defaultBorder) this.setBorder(defaultBorder);
		else {
			LineBorder lb = new LineBorder(this.currentBorderColor,specialBordersSize);
			this.currentBorder=BorderFactory.createCompoundBorder(lb, getSpecialEmptyBorder());
			this.setBorder(currentBorder);
		}
		
	}
	
	/**
	 * <p><b>Makes this button's border thicker by {@link #boldenBy}.</b></p>
	 * <p>Used to show a deck as currently selected. Called by {@link GamePanel#boldenPlayer(Player)} and {@link GamePanel#setSelectedDeckButton(DeckSelectorButton)}.</p>
	 * <p>If the button has the default style, a custom border is created on the basis of the {@link Theme#LIGHT_SEPARATOR} theme. Otherwise, there is already a custom border, which is simply thickened.</p>
	 * @see #remakeBorder()
	 */
	public void bolden() {
		if(!this.boldened) this.specialBordersSize+=boldenBy;
		if(currentBorder==defaultBorder) {
			LineBorder lb = new LineBorder(new Notification(Theme.LIGHT_SEPARATOR).getFg(),specialBordersSize);
			this.setBorder(BorderFactory.createCompoundBorder(lb,getSpecialEmptyBorder()));
		}
		else {
			this.remakeBorder();
		}
		this.boldened=true;
	}
	
	/**
	 * <p><b>Makes this button's border thickness back to normal.</b></p>
	 * <p>Called when a DeckSelectorButton is unselected or if another one is selected. See {@link GamePanel#setSelectedDeckButton(DeckSelectorButton)}.</p>
	 * @see GamePanel#setSelectedDeckButton(DeckSelectorButton)
	 * @see GamePanel#unBoldenPlayer(Player)
	 */
	public void unbolden() {
		if(this.boldened) {
			specialBordersSize-=boldenBy;
			remakeBorder();
		}
		this.boldened=false;
	}
	
	/**
	 * An {@link javax.swing.border.EmptyBorder EmptyBorder} has to be used in order to keep the button's insets active when {@link #paintComponent(Graphics) painting the component}.
	 * @return An {@link javax.swing.border.EmptyBorder EmptyBorder} calculated to simulate the button's insets and to leave enough space for the custom {@link #currentBorder current border}. 
	 */
	public EmptyBorder getSpecialEmptyBorder() {
		return new EmptyBorder(insets.top-specialBordersSize, insets.left-specialBordersSize,insets.bottom-specialBordersSize,insets.right-specialBordersSize);
	}
	
	public Player getAssociatedPlayer () {
		return this.associatedPlayer;
	}
	
	public RumourCardsPile getDeck () {
		return this.associatedDeck;
	}
	
	@Override
	public Insets getInsets() {
		return insets;
	}
	
	@Override
	public Insets getMargin() {
		return insets;
	}
	
	public void setInsets(Insets i) {
		this.insets=i;
		this.setMargin(insets);
	}


}
