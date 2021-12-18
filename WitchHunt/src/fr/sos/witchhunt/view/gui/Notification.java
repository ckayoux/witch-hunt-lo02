package fr.sos.witchhunt.view.gui;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Notification {
	private String text;
	private Color fg;

	public Notification(String text, NotificationType type) {
		this(type);
		this.text=text;
	}
	
	public Notification (NotificationType type) {
			switch (type) {
				case NORMAL:
					fg=Color.BLACK;
					break;
				case OFFENSIVE:
					fg=Color.RED;
					break;
				case HUNT :
					fg=Color.ORANGE;
					break;
				case WITCH:
					fg=new Color(147,112,219);
					break;
				case TURN:
					fg=new Color(41,128,185);
					break;
				case SCORE:
					fg=Color.green;
					break;
				case SEPARATOR:
					fg=Color.BLACK;
					text="-----------------------------------\n";
					break;
				case CRLF:
					fg=null;
					text="\n";		
		}
	}
	
	public String getText() {
		return this.text;
	}
	
	public SimpleAttributeSet makeAttributeSet() {
		SimpleAttributeSet output = new SimpleAttributeSet();
		StyleConstants.setForeground(output, fg);
		return output;
	}
	
}
