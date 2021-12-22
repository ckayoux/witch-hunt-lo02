package fr.sos.witchhunt.view.gui;

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

public class RenderedCard extends JLabel {
	private RumourCard represents=null;
	private boolean revealed = false;
	private final static ImageIcon unrevealedRenderedCard = new ImageIcon(Card.getUnrevealedCardImage());
	private static Map<RumourCard,ImageIcon> renderedCardIconsMap = new HashMap<RumourCard,ImageIcon>();
	
	private final static NotificationType defaultTheme = NotificationType.LIGHT_SEPARATOR;
	
	private MouseListener colorChangeManager=null;
	
	private Color themeColor = (new Notification(defaultTheme)).getFg();
	private Color currentBorderColor=null;
	private Color currentBorderHoverColor=null;
	private Color currentBorderClickedColor=null;
	private int currentBorderThickness=0;
	private boolean bordered=false;
	private final static int defaultBorderThickness=2;
	private final static int boldenBy=2;
	
	private boolean boldened = false;
	
	public RenderedCard(RumourCard rc) {
		super();
		//this.setPreferredSize(Card.IMAGES_SIZE);
		//this.setMinimumSize(Card.IMAGES_SIZE);
		this.represents=rc;
		this.update(false);
		this.resetTheme();
	}
	
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
					//this.setText(this.represents.getName()); //TMP
				}
			}
			else if (!this.represents.isRevealed()) {
				this.setIcon(unrevealedRenderedCard);
				//this.setText("*unrevealed*"); //TMP
			}
		}
	}
	
	public RumourCard getAssociatedRumourCard() {
		return this.represents;
	}
	
	public void border() {
		this.currentBorderThickness=defaultBorderThickness;
		this.repaint();
	}

	public void bolden() {
		if(!this.boldened) {
			if(!this.bordered) this.border();
			this.currentBorderThickness+=boldenBy;
			this.boldened=true;
			currentBorderColor=currentBorderClickedColor;
			this.repaint();
		}
	}
	
	public void unbolden() {
		if(this.boldened) {
			if(!this.bordered) this.currentBorderThickness=0;
			this.currentBorderThickness-=boldenBy;
			this.boldened=false;
			currentBorderColor=themeColor;
			this.repaint();
		}
	}
	
	public void resetTheme() {
		this.setTheme(defaultTheme);
		if(bordered&&!this.boldened) {
			this.currentBorderThickness=0;
		}
	}
	
	public void setTheme(NotificationType theme) {
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