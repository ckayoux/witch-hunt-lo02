package fr.sos.witchhunt.view.gui.scenes.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.sos.witchhunt.model.cards.Card;
import fr.sos.witchhunt.model.cards.RumourCard;

/**
 * <b>Represents a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} on the {@link GamePanel ingame scene} of the graphical interface.</b>
 * <p>A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} is represented graphically by {@link fr.sos.witchhunt.model.cards.RumourCard#getImage() a picture}.
 * Each concrete extension of  {@link fr.sos.witchhunt.model.cards.RumourCard RumourCard} comes with its own picture, which is the one that is shown when the card is {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() revealed}.
 * Unrevealed cards are all shown with a same picture : {@link fr.sos.witchhunt.model.cards.Card#getUnrevealedCardImage() Card::getUnrevealedCardImage()}.</p>
 * <p>{@link GamePanel} {@literal <#>--->} cardsPanel {@literal <#>--->} DeckPanel {@literal <#>--->} RenderedCard (this)</p>
 * <p>This class is only responsible for rendering the cards on the graphical interface and styling them up.
 * When a user is invited to select a card, they can select them by directly clicking on its picture - this is achieved by a dedicated controller, {@link fr.sos.witchhunt.controller.interactions.CardSelectorController CardSelectorController}, which is entirely in charge
 * of this feature.</p>
 * <p>When a card is {@link GamePanel#makeCardsChoosable(fr.sos.witchhunt.model.players.Player, fr.sos.witchhunt.model.cards.RumourCardsPile, fr.sos.witchhunt.model.cards.RumourCardsPile, Theme) made choosable}, its appearence changes :
 * a border, the color of which can be changed depending on MouseEvents, outlines it.</p>
 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
 * @see GamePanel GamePanel has a CardsPanel, which can contain plural DeckPanels, the latter ones can contain plural RenderedCards.
 * @see GamePanel#makeCardsChoosable(fr.sos.witchhunt.model.players.Player, fr.sos.witchhunt.model.cards.RumourCardsPile, fr.sos.witchhunt.model.cards.RumourCardsPile, Theme)
 * @see fr.sos.witchhunt.controller.interactions.CardSelectorController A dedicated controller, CardSelectorController, allows the users to choose cards directly by clicking on their picture
 */
public class RenderedCard extends JLabel {
	/**
	 * The Rumour card represented by this object.
	 */
	private RumourCard represents=null;
	/**
	 * @see fr.sos.witchhunt.model.cards.RumourCard#isRevealed()
	 */
	private boolean revealed = false;
	
	/**
	 * <p><b>The same picture is used for representing all unrevealed cards.</b></p>
	 * <p>Loaded only once for resource saving purposes.</p>
	 * @see fr.sos.witchhunt.model.cards.Card#getUnrevealedCardImage()
	 */
	private final static ImageIcon unrevealedRenderedCard = new ImageIcon(Card.getUnrevealedCardImage());
	
	/**
	 * <p>A Map associating each instance of {@link fr.sos.witchhunt.model.cards.RumourCard RumourCard} that has already been rendered as a RenderedCard with an ImageIcon representing it.</p>
	 * <p>Used for resources saving purposes : each picture is loaded only the first time it is needed.</p>
	 */
	private static Map<RumourCard,ImageIcon> renderedCardIconsMap = new HashMap<RumourCard,ImageIcon>();
	
	private final static Theme defaultTheme = Theme.LIGHT_SEPARATOR;
	
	/**
	 * <p>MouseListener that is responsible for changing the RenderedCard's appearence based on MouseEvents.</p>
	 * <p>Not responsible for collecting user-input : this is achieved by a dedicated controller, {@link fr.sos.witchhunt.controller.interactions.CardSelectorController CardSelectorController}.</p>
	 * @see #setTheme(Theme)
	 */
	private MouseListener colorChangeManager=null;
	
	/**
	 * Main theme color, from which are derived the border colors for all supported MouseEvents.
	 */
	private Color themeColor = (new Notification(defaultTheme)).getFg();
	/**
	 * Current border color.
	 */
	private Color currentBorderColor=null;
	/**
	 * Color for the border of this card when it is mouse-hovered.
	 */
	private Color currentBorderHoverColor=null;
	/**
	 * Color for the border of this card when its picture is clicked on.
	 */
	private Color currentBorderClickedColor=null;
	/**
	 * Current thickness of the card's border. Default : 0, no border.
	 */
	private int currentBorderThickness=0;
	/**
	 * Indicates whether the RenderedCard has a border or not.
	 */
	private boolean bordered=false;
	private final static int defaultBorderThickness=2;
	/**
	 * The border thickness can be increased when it gets {@link #bolden() boldened}.
	 */
	private final static int boldenBy=2;
	
	private boolean boldened = false;
	
	/**
	 * <b>Creates a RenderedCard representing the given {@link fr.sos.witchhunt.model.cards.RumourCard RumourCard} and the default theme.</b>
	 * @param rc The RumourCard to render
	 */
	public RenderedCard(RumourCard rc) {
		super();
		this.represents=rc;
		this.update(false);
		this.resetTheme();
	}
	
	/**
	 * <b>Updates the RenderedCard based on the {@link #getAssociatedRumourCard() represented}  {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() RumourCard's reveal status}.</b>
	 * <p>All unrevealed Rumour card will be represented with the same ImageIcon : see {@link #unrevealedRenderedCard} and {@link fr.sos.witchhunt.model.cards.Card#getUnrevealedCardImage() Card::getUnrevealedCardImage()}.</p>
	 * <p>A revealed Rumour card will be represented by its {@link fr.sos.witchhunt.model.cards.RumourCard#getImage() own picture}.</p>
	 * <p>Called when using {@link GamePanel#updateCardRevealStatus(RumourCard)} or {@link GamePanel#updateDeckContent(fr.sos.witchhunt.model.cards.RumourCardsPile, boolean)}.</p>
	 * @param forceReveal If this boolean is <i>true</i>, the RenderedCard will be shown as if the {@link #getAssociatedRumourCard() represented Rumour card} was {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() revealed}, even if it isn't.
	 * @see fr.sos.witchhunt.model.cards.RumourCard#getImage() RumourCard::getImage()
	 * @see fr.sos.witchhunt.model.cards.Card#getUnrevealedCardImage() Card::getUnrevealedCardImage()
	 * @see fr.sos.witchhunt.model.cards.RumourCard#isRevealed() RumourCard::isRevealed()
	 * @see GamePanel#updateCardRevealStatus(RumourCard)
	 * @see GamePanel#updateDeckContent(fr.sos.witchhunt.model.cards.RumourCardsPile, boolean)
	 */
	public void update(boolean forceReveal) {
		if(this.represents!=null) {
			if((this.represents.isRevealed()&&!revealed)||forceReveal) {
				if(!forceReveal&&this.represents.isRevealed()) this.revealed=true;
				ImageIcon alreadyRendered = renderedCardIconsMap.get(this.represents);
				if(alreadyRendered!=null) {
					this.setIcon(alreadyRendered);
				}
				else {
					ImageIcon i = new ImageIcon(this.represents.getImage());
					renderedCardIconsMap.put(this.represents, i);
					this.setIcon(i);
				}
			}
			else if (!this.represents.isRevealed()) {
				this.setIcon(unrevealedRenderedCard);
			}
		}
	}
	
	public RumourCard getAssociatedRumourCard() {
		return this.represents;
	}
	
	public void border() {
		this.currentBorderThickness=defaultBorderThickness;
	}

	/**
	 * <p><b>Increases the RenderedCard's border thickness by {@link #boldenBy}.</b></p>
	 * <p>If the card wasn't already bordered, borders it with the default border style.</p>
	 * <p>Called when {@link GamePanel#setSelectedCard(RenderedCard) selecting this RenderedCard.</p>
	 * @see GamePanel#setSelectedCard(RenderedCard)
	 * @see #bordered
	 */
	public void bolden() {
		if(!this.boldened) {
			if(!this.bordered) this.border();
			this.currentBorderThickness+=boldenBy;
			this.boldened=true;
			currentBorderColor=currentBorderClickedColor;
			this.repaint();
		}
	}
	
	/**
	 * <p><b>Makes the card's border go back to its normal appearance if it was {@link #boldened}. Does not modify the current border's style in terms of color.</b></p>
	 * <p>Called when a RenderedCard is unselected or when another one is {@link GamePanel#setSelectedCard(RenderedCard)}.</p>
	 * @see #bolden()
	 * @see #boldened
	 * @see GamePanel#setSelectedCard(RenderedCard)
	 */
	public void unbolden() {
		if(this.boldened) {
			if(!this.bordered) this.currentBorderThickness=0;
			this.currentBorderThickness-=boldenBy;
			this.boldened=false;
			currentBorderColor=themeColor;
			this.repaint();
		}
	}
	
	/**
	 * <p><b>Resets the RenderedCard's appearence to defaults.</b></p>
	 * <p>If there is a custom border, its color is reset. The border will also become invisible, excepted if the card is {@link #boldened}.</p>
	 * @see #defaultTheme
	 * @see #setTheme(Theme)
	 * @see GamePanel#resetCardsEffects()
	 */
	public void resetTheme() {
		if(bordered&&!this.boldened) {
			this.currentBorderThickness=0;
			this.bordered=false;
		}
		this.setTheme(defaultTheme);
	}
	
	/**
	 * <p><b>Sets the RenderedCard's appearance on the basis of the provided {@link Theme}.</b></p>
	 * <p>If a custom border is to be set, its color will change based on the MouseEvents catched by {@link #colorChangeManager}.</p>
	 * <p>The theme's base color is given by {@link Notification#getFg()}.</p>
	 * <p>The border's colors for each supported MouseEvent are derived (by {@link #lighten(Color) lightening} or {@link #darken(Color) darkening}) the base color.</p>
	 * @param theme The {@link Theme}, on the basis of which the RenderedCard should be dressed up.
	 * @see Theme
	 * @see Notification#getFg()
	 */
	public void setTheme(Theme theme) {
		this.themeColor = new Notification(theme).getFg();
		
		this.currentBorderColor=themeColor;
		this.currentBorderHoverColor=lighten(themeColor);
		this.currentBorderClickedColor=darken(themeColor);
		
		if(theme!=defaultTheme) {
			this.border(); //no border for default theme excepted if boldened
			this.bordered=true;
		}
		else {
			this.bordered=false;
		}
		
		this.repaint();
		
		if(colorChangeManager!=null) {
			this.removeMouseListener(colorChangeManager);
			colorChangeManager=null;
		}
		
		this.colorChangeManager= new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if(!boldened) {
					currentBorderColor=currentBorderHoverColor;
				}
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(!boldened) {
					currentBorderColor=themeColor;
				}
				repaint();
			}
			
		};
		this.addMouseListener(colorChangeManager);
	}
	
	/**
	 * @param original The base Color we want to obtain a lightened version of
	 * @return A lightened version of the given Color
	 */
	private Color lighten(Color original) {
		float[] hsbVals = new float [3] ;
		Color.RGBtoHSB(original.getRed(),original.getGreen(),original.getBlue(),hsbVals);
		float sat = hsbVals[1];
		float bri = hsbVals[2];
		float lightenedSat = (float) Math.max(0, sat-0.15);	
		float lightenedBri = (float) Math.min(1, bri+0.10);		
		Color lightenedColor = Color.getHSBColor(hsbVals[0],lightenedSat,lightenedBri);
		return lightenedColor;

	}
	/**
	 * @param original The base Color we want to obtain a darker version of
	 * @return The darkened version of the given Color
	 */
	private Color darken (Color original) {
		float[] hsbVals = new float [3] ;
		Color.RGBtoHSB(original.getRed(),original.getGreen(),original.getBlue(),hsbVals);
		float sat = hsbVals[1];
		float bri = hsbVals[2];
		float darkenedSat = (float) Math.min(1, sat+0.30);
		float darkenedBri = (float) Math.max(0, bri-0.25);	
		Color darkenedColor = Color.getHSBColor(hsbVals[0],darkenedSat,darkenedBri);
		return darkenedColor;
	}
	
	/**
	 * The component is painted with its custom border if it has one.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bordered||boldened) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(currentBorderColor);
			g2d.setStroke(new BasicStroke(currentBorderThickness));
			g2d.drawRect(currentBorderThickness/2,currentBorderThickness/2,this.getWidth()-currentBorderThickness,this.getHeight()-currentBorderThickness);
		}	
	}
}