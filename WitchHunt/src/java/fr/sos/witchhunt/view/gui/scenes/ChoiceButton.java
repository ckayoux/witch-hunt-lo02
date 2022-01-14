package fr.sos.witchhunt.view.gui.scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

/**
 * <p><b>Button of a {@link ChoicesPanel}, used to collect user-input in order to choose an entry within a {@link fr.sos.witchhunt.controller.interactions.Menu Menu}.</b></p>
 * <p>This class only defines the buttons' style, user input is collected using a {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController} when a {@link ChoicesPanel} calls for it.</p>
 * <p>These buttons have a finite set of possible background colors, each one corresponding to a kind of action ({@link fr.sos.witchhunt.model.players.HumanPlayer#chooseTurnAction() choosing to accuse an opponent, choosing to play a Hunt! effect}...)</p> 
 * <p>Their style changes on hover and click.</p>
 * @see ChoicesPanel
 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
 */
public class ChoiceButton extends JButton {
	/**
	 * The current default background color for this button.
	 * For a JButton's default background color, set to null.
	 */
	private Color bgColor=null;
	/**
	 * The current hover color for this button.
	 * For a a JButton's default hover color, set to null.  
	 */
	private Color hoverColor=null;
	/**
	 * The current default border for this button.
	 * For a a JButton's default Border, set to null.  
	 */
	private Border defaultBorder = null;
	/**
	 * The current hover border for this button.
	 * For a a JButton's default hover Border, set to null.  
	 */
	private Border hoverBorder = null;
	/**
	 * The border to display around the button when it is pressed.
	 * For a a JButton's default hover border, set to null.  
	 */
	private Border pressedBorder = null;
	
	/**
	 * The MouseListener in charge of controlling the Button's style on hover, pressed and exited.
	 * Not in charge of collecting any input from the button, for that, see {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController ChoicesPanelController}.
	 * {@link #setTheme(Color)}
	 */
	private MouseListener colorChangeManager=null;
	/**
	 * The buttons internal margins. Modified when rendered by a {@link ChoicesPanel}, for a normalized width equal to that of the largest button.
	 * @see ChoicesPanel#renderActionButtons()
	 */
	private Insets insets = new Insets(10, 20, 10, 20);
	
	/**
	 * @param str The button's text. 
	 * @see ChoicesPanel#displayMenu(fr.sos.witchhunt.controller.interactions.Menu)
	 */
	public ChoiceButton(String str) {
		super(str);
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.defaultBorder=this.getBorder();
	}
	
	/**
	 * If the button's look is not the one by default for JButtons, fill their background with the current background color when repainting.
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
	  * <p><b>Sets the background default and hover colors as well as the button's borders</b> on the basis of the given color.</p>
	  * <p>The button's {@link #bgColor} default background color will become the one given in the method's parameters.</p>
	  * <p>The background and border colors when hovering and pressing is created automatically by lightening/darkening this color.</p>
	  * <p>On hover, creates a compound border out of an {@link javax.swing.EmptyBorder to simulate margins} and of a {@link javax.swing.BevelBorder BevelBorder (when hovering/pressing)} or a {@link javax.swing.LineBorder (default)}.</p>
	  * <p>The new theme will be displayed when {@link #paintComponent(Graphics) repainting} the component.</p>
	  * <p>The events are all listened by a specialized {@link #colorChangeManager MouseListener} associated with this class and responsible for styling up the button on MouseEvents.</p>
	  * @param bg The main color for this button's theme.
	  * @see java.awt.Color#getHSBColor(float, float, float)
	  * @see javax.swing.EmptyBorder
	  * @see javax.swing.LineBorder
	  * @see javax.swing.BevelBorder
	  * 
	  * @see #bgColor
	  * @see #hoverColor
	  * @see #defaultBorder
	  * @see #hoverBorder
	  * @see #pressedBorder
	  * 
	  * @see #colorChangeManager
	  */
	public void setTheme(Color bg) {
		super.setBackground(bg);
		this.bgColor=bg;
		if(bg!=null) {
			float[] hsbVals = new float [3] ;
			Color.RGBtoHSB(bg.getRed(),bg.getGreen(),bg.getBlue(),hsbVals);
			float sat = hsbVals[1];
			sat = (float)Math.min(1.0, sat+0.35);
			float bri = hsbVals[2];
			bri = (float) Math.max(0, bri-0.27);
			this.hoverColor=Color.getHSBColor(hsbVals[0],sat,hsbVals[2]);
			Color borderColor = Color.getHSBColor(hsbVals[0],sat,bri);
			int bthickness = 2;
			BevelBorder obb = (BevelBorder) BorderFactory.createBevelBorder(0,bgColor,borderColor);
			BevelBorder ibb = (BevelBorder) BorderFactory.createBevelBorder(1,bgColor,borderColor);
			EmptyBorder lbeb = new EmptyBorder(new Insets(insets.top-bthickness,insets.left-bthickness,insets.bottom-bthickness,insets.right -bthickness));
			EmptyBorder bbeb = new EmptyBorder(new Insets(insets.top-2,insets.left-2,insets.bottom-2,insets.right -2));
			LineBorder lb = (LineBorder) BorderFactory.createLineBorder(borderColor,bthickness);
			this.hoverBorder = BorderFactory.createCompoundBorder(obb, bbeb);
			this.pressedBorder = BorderFactory.createCompoundBorder(ibb, bbeb);
			this.defaultBorder=BorderFactory.createCompoundBorder(lb, lbeb);
			this.setBorder(defaultBorder);
			this.setContentAreaFilled(false);
			
			if(colorChangeManager!=null) {
				this.removeMouseListener(colorChangeManager);
			}
			
			this.colorChangeManager= new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					setBorder(pressedBorder);

				}

				@Override
				public void mousePressed(MouseEvent e) {
					setBackground(hoverColor);
					setBorder(pressedBorder);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					setBorder(pressedBorder);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					if(hoverColor!=null) {
						setBackground(hoverColor);
						
						setBorder(hoverBorder); //bevel border
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if(bgColor!=null) {
						setBackground(bgColor);
						setBorder(defaultBorder);
					}
					
				}
				
			};
			this.addMouseListener(colorChangeManager);
		}
		
	}
	
	/**
	 * <p><b>Makes a color out of a {@link fr.sos.witchhunt.model.players.TurnAction TurnAction} or a {@link fr.sos.witchhunt.model.players.DefenseAction DefenseAction}.</b></p>
	 * @see #setTheme(Color) The color will be a basis for the whole button's theme - see method #setTheme(Color)
	 * @param o A {@link fr.sos.witchhunt.model.players.TurnAction TurnAction} or a {@link fr.sos.witchhunt.model.players.DefenseAction DefenseActions}. Other objects would make this method return null, and the style would be reset to defaults.
	 * @return A Color corresponding to the action type given in parameters.
	 * @see ChoicesPanel#setThemeAsForActionType(Object)
	 */
	public static Color getButtonColorByActionType(Object o) {
		if (o instanceof Identity) {
			Identity i = (Identity) o;
			switch (i) {
			case VILLAGER:
				return new Color(255, 142, 85);
			case WITCH:
				return new Color(161,132,220);
			}
		}
		else if (o instanceof TurnAction) {
			TurnAction ta = (TurnAction) o;
			switch (ta) {
				case ACCUSE:
					return new Color(255,97,97);
				case HUNT:
					return new Color(255, 142, 85);
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return new Color(255,97,97);
				case WITCH:
					return new Color(161,132,220);
				case DISCARD:
					return new Color(156,147,135);
			}
		}
		return null;
	}
	
	/**
	 * <p><b>Sets the buttons' text based on the corresponding {@link fr.sos.witchhunt.controller.interactions.Menu Menu} entry.</b></p>
	 * <p>The result is generally less verbose than for the {@link fr.sos.witchhunt.view.std.StdView#stringifyMenuOption(Object) console view}.</p>
	 * <p>A {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu's entry} is of the <code>Object</code> type. The conversion result will depend on <code>instanceof</code> assertions, testing for 
	 * a membership to a more specific type.</p>
	 * <p><b>Called by the {@link ChoicesPanel#displayMenu(Menu) ChoicesPanel::displayMenu(Menu) method}.
	 * <p>A String is displayed as it is, excepted for <b>"/c/" ("Console-only" special sequence)</b>, which will make this method return <code>null</code> to indicate that no button should be made out of this option.</p>
	 * <p>An {@link fr.sos.witchhunt.model.Identity Identity} will display a text corresponding to it.</p>
	 * <p>A {@link fr.sos.witchhunt.model.players.TurnAction TurnAction} or a {@link fr.sos.witchhunt.model.players.DefenseAction DefenseAction} will be translated into a verbose description of the action's effect.</p>
	 * <p>A {@link fr.sos.witchhunt.model.players.Player Player} or a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} will be replaced by the value of their <code>getName()</code> method.</p>
	 * @param o A {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu's entry} (of type <code>Object</code>)
	 * @return A short String describing the Menu's entry, depending on the classes or enums it is an instance of.
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu::getOptions()
	 * 
	 * @see #makeMenu(Menu)
	 * @see #logPossibilities(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.std.StdView#stringifyMenuOption(Object) GUI homologue of the console view's <code>StdView::stringifyMenuOption(Object)</code> method.
	 */
	public static String makeButtonText(Object o) {
		String buttonText=null;
		if (o instanceof String) {
			String str = (String) o;
			if(str.matches("^/c/.*")) {
				buttonText=null;
			}
			else {
				buttonText=str;
			}
		}
		else if (o instanceof Identity) {
			Identity i = (Identity) o;
			switch (i) {
			case VILLAGER:
				return "Villager";
			case WITCH:
				return "Witch";
			}
		}
		else if (o instanceof TurnAction) {
			TurnAction ta = (TurnAction) o;
			switch (ta) {
				case ACCUSE:
					return "Accuse";
				case HUNT:
					return "Hunt";
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return "Reveal";
				case WITCH:
					return "Witch";
				case DISCARD:
					return "Discard";
			}
		}
		else if (o instanceof Player) {
			Player p = (Player) o;
			return p.getName();
		}
		else if (o instanceof RumourCard) {
			RumourCard rc = (RumourCard) o;
			return rc.getName();
		}
		else {
			buttonText = o.toString();
		}
		return buttonText;
	}
	
	/**
	 * <b>Sets the button text to make it represent the given {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}.</b>
	 * <p>The button will show the {@link fr.sos.witchhunt.model.cards.RumourCard#getName() Rumour card's name} if is {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() revealed}
	 * or if <code>forceReveal</code> (2nd arg) is <i>true</i>.</p>
	 * <p>Otherwise, the card's name will be hidden.</p>
	 * @param rc The choosable Rumour Card pointed towards by this button
	 * @param forceReveal If <i>true</i>, even an {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() unrevealed} Rumour card's name will be shown.
	 * @return A String representing the given Rumour card.
	 * @see ChoicesPanel#displayCards(Menu, boolean) called when using the <code>ChoicesPanel::displayCards(Menu, boolean)</code> method
	 */
	public static String makeCardButtonText(RumourCard rc,boolean forceReveal) {
		if(rc.isRevealed()||forceReveal) return rc.getName();
		else return "*Unrevealed*";
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
