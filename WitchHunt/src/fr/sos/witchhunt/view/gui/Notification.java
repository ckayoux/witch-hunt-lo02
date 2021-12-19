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
	
	public String getText() {
		return this.text;
	}
	
	public SimpleAttributeSet makeAttributeSet() {
		SimpleAttributeSet output = new SimpleAttributeSet();
		StyleConstants.setForeground(output, fg);
		return output;
	}
	
}
