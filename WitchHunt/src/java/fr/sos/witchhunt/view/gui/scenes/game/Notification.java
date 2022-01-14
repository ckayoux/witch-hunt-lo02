package fr.sos.witchhunt.view.gui.scenes.game;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * <p><b>Defines a notification to be displayed as a text message.</b></p>
 * <p>Associated with a {@link Theme}, which determines the Notification's {@link #fg color} (and can also determine its {@link #text}).</p>
 * @see Theme
 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox Notifications are used in NotificationBoxes to display messages.
 * @see fr.sos.witchhunt.view.gui.scenes.game.DeckSelectorButton They are also used by other components like the DeckSelectorButtons to customize their color based on a Theme.
 */
public class Notification {
	/**
	 * <p><b>The message delivered by this Notification.</b></p>
	 * <p>Will be displayed when using <code>{@link fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox#printNotification(Notification) NotificationsBox::printNotification(Notification)}</code>.</p>
	 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox NotificationsBox
	 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox#printNotification(Notification) NotificationsBox::printNotification(Notification)
	 */
	private String text;
	/**
	 * <p><b>The Color associated with this Notification.</b></p>
	 * <p>A message displayed in a {@link fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox NotificationsBox} will have this color.</p>
	 * <p>Determined by the {@link Theme} used at instantiation.</p>
	 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox NotificationsBox
	 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox#printNotification(Notification) NotificationsBox::printNotification(Notification)
	 * @see fr.sos.witchhunt.view.gui.scenes.game.DeckSelectorButton#makeBackground(Theme) Can be used to style up buttons' background or borders based on a Theme
	 */
	private Color fg;

	/**
	 * <b>Creates a Notification with the given message and the given theme.</b> 
	 * @param text The message to be associated with this Notification.
	 * @param type The theme to be associated with this Notification. Defines a type of Notification
	 */
	public Notification(String text, Theme type) {
		this(type);
		this.text=text;
	}
	
	/**
	 * <p><b>Creates a Notification based on the given theme.</b></p>
	 * <p>The {@link Theme} determines the Notification's color.</p>
	 * <p>Some {@link Theme Themes} like {@link Theme#LIGHT_SEPARATOR} will also determine the message associated with this Notification.</p> 
	 * @param type The theme categorizing this Notification and defining its {@link #fg color}.
	 */
	public Notification (Theme type) {
			switch (type) {
				case NORMAL:
					fg=Color.BLACK;
					break;
				case OFFENSIVE:
					fg=new Color(240,0,32); //Red
					break;
				case HUNT :
					fg=new Color(255, 79, 0); //Orange
					break;
				case WITCH:
					fg=new Color(147,112,219); //Purple
					break;
				case TURN:
					fg=new Color(52,201,36); //Green
					break;
				case SCORE:
					fg=new Color(41,128,185); //Blue
					break;
				case HARD_SEPARATOR:
					fg=new Color(156,147,135); //Grey
					text="----------------------------------------------------------------------\n";
					break;
				case LIGHT_SEPARATOR:
					fg=new Color(156,147,135); //Grey
					text="     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
					break;
				case CRLF:
					fg=Color.BLACK;
					text="\n";		
		}
	}
	
	public Color getFg() {
		return this.fg;
	}
	
	public String getText() {
		return this.text;
	}
	
	/**
	 * <b>Makes a {@link javax.swing.text.SimpleAttributeSet} out of this Notification's {@link #fg color}.</b>
	 * <p>Used when displaying this notifications into a {@link javax.swing.text.StyledDocument} (see {@link fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox NotificationsBox}).</p>
	 * @return A {@link javax.swing.text.SimpleAttributeSet} only containing information relative to the Font's foreground color, which is set to this Notification's color.
	 * @see fr.sos.witchhunt.view.gui.scenes.game.NotificationsBox NotificationsBox is used to display Notifications into a {@link javax.swing.text.StyledDocument}
	 */
	public SimpleAttributeSet makeAttributeSet() {
		SimpleAttributeSet output = new SimpleAttributeSet();
		StyleConstants.setForeground(output, fg);
		return output;
	}
	
}
