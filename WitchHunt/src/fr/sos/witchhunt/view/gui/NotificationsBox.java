package fr.sos.witchhunt.view.gui;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class NotificationsBox extends JScrollPane{
		private JTextPane textPane ;
		private StyleContext context = new StyleContext();
	    private StyledDocument doc = new DefaultStyledDocument(context);    

		public NotificationsBox() {
			super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			textPane = new JTextPane(doc);
			textPane.setEditable(false);
			this.getViewport().add(textPane);
			
			DefaultCaret caret = (DefaultCaret)this.textPane.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
			this.textPane.setFont(new Font("Consolas", Font.BOLD, 16));
		}

		public JTextPane getTextArea() {
			return this.textPane;
		}
		
		
		public void setNotification(Notification n) {
			eraseContent();
			printNotification(n);
		}
		public void eraseContent() {

			Element root = doc.getDefaultRootElement();
			Element previous = root.getElement(0);
			try {
				doc.remove(previous.getStartOffset(),doc.getLength());
			} catch (BadLocationException e) {
				
			}
			
			//while()
		}
		
		
		public void printNotification(Notification n)  {
			//textPane.setCharacterAttributes(n.makeAttributeSet(), true);
			try {
				this.doc.insertString(doc.getLength(), n.getText(), n.makeAttributeSet());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}