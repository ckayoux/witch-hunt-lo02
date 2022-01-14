package fr.sos.witchhunt.view.gui.scenes.game;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * <p><b>A component designed for displaying {@link Notification Notifications} on the Graphical Interface.</b></p>
 * <p>Used to describe the events happening in the match.</p>
 * <p>Made of a JTextPane, which can be scrollable or not and which displays the content of a {@link javax.swing.text.StyledDocument StyledDocument}.</p>
 * <p>Incoming {@link Notification Notifications} are printed into the JTextPane's {@link javax.swing.text.StyledDocument StyledDocument} with a foreground color determined
 * by their {@link Theme}.</p>
 * <p>Two NotificationsBoxes are used by the {@link GamePanel ingame scene} : one at the top, which displays only one important notification at the time,
 * and a larger one at the bottom of the scene, which displays all notifications and can be scrolled up to show older events.</p>
 * @see Notification
 * @see Theme
 * @see javax.swing.JTextPane
 * @see javax.swing.text.StyledDocument
 * @see GamePanel {@literal <#>--->} NotificationsBox(this)
 */
public class NotificationsBox extends JScrollPane{
		/**
		 * The texte pane rendering the content of {@link #doc a StyledDocument}.
		 */
		private JTextPane textPane ;
		/**
		 * A set of style attributes for the {@link StyledDocument}.
		 */
		private StyleContext context = new StyleContext();
		/**
		 * The StyledDocument to which the incoming {@link Notification Notifications'} text is appended with a determined foreground color.
		 */
	    private StyledDocument doc = new DefaultStyledDocument(context);    

	    /**
	     * <b>Creates a minimal and functional version of the component, which can be specialized to add or modify some of its features.</b>
	     * <p>It will, by default, become vertically scrollable only when its content height excedes its own height. Autoscroll is on by default.</p>
	     * <p>The JTextPane's content cannot be edited by the users.</p>
	     * <p>A common font for all messages is chosen.</p>
	     */
		public NotificationsBox() {
			super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			textPane = new JTextPane(doc);
			textPane.setEditable(false);
			this.getViewport().add(textPane);
			
			DefaultCaret caret = (DefaultCaret)this.textPane.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
			this.textPane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		}

		public JTextPane getTextArea() {
			return this.textPane;
		}
		
		/**
		 * <b>Replaces</b> the NotificationBoxe's content with the given {@link Notification}.
		 * @param n The notification to display alone.
		 * @see #eraseContent()
		 * @see #printNotification(Notification)
		 * @see GamePanel#displayMainNotification(Notification)
		 */
		public void setNotification(Notification n) {
			eraseContent();
			printNotification(n);
		}
		
		/**
		 * <b>Erases the content of this NotificationBox.</b>
		 * @see GamePanel#resetNotificationsBoxes()
		 * @see #setNotification(Notification)
		 */
		public void eraseContent() {
			Element root = doc.getDefaultRootElement();
			Element previous = root.getElement(0);
			try {
				doc.remove(previous.getStartOffset(),doc.getLength());
			} catch (BadLocationException e) {
				
			}
		}
		
		/**
		 * <b>Inserts the given {@link Notification} into this NotificationBoxe's {@link #doc document}.</b>
		 * @param n The {@link Notification} to show inside this NotificationBox
		 * @see Notification#getText()
		 * @see Notification#makeAttributeSet() The Notification is displayed with a foreground color depending of its associated {@link Theme}.
		 */
		public void printNotification(Notification n)  {
			try {
				this.doc.insertString(doc.getLength(), n.getText(), n.makeAttributeSet());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		
	}