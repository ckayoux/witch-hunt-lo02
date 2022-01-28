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

public class DeckSelectorButton extends JButton {
	
	private Player associatedPlayer;
	private RumourCardsPile associatedDeck;
	private final static String pileText ="Discarded cards";
	
	
	private Insets insets = new Insets(10, 20, 10, 20);

	private boolean colorChanged=false;
	
	private Color bgColor=null;
	private Color hoverColor=null;
	private MouseListener colorChangeManager=null;
	
	private Color currentBorderColor=null;
	private final Border defaultBorder;
	private Border currentBorder;
	private boolean boldened=false;
	private int specialBordersSize = 1;
	private final int boldenBy = 2;
	
	public DeckSelectorButton(Player p) {
		super(p.getName());
		this.defaultBorder=this.getBorder();
		this.currentBorder=defaultBorder;
		this.associatedPlayer=p;
		this.associatedDeck=p.getHand();
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public DeckSelectorButton(RumourCardsPile pile) {
		super(pileText);
		this.defaultBorder=this.getBorder();
		this.currentBorder=defaultBorder;
		this.associatedPlayer=null;
		this.associatedDeck = pile;
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	 @Override
     protected void paintComponent(Graphics g) {
		 if(!isContentAreaFilled()) {
			 g.setColor(getBackground());
	         g.fillRect(0, 0, getWidth(), getHeight());
	         super.paintComponent(g);
		 }
		 else super.paintComponent(g);
     }
	
	public void makeBackground(Theme theme) {
		Color themeColor = new Notification(theme).getFg();
		float[] hsbVals = new float [3] ;
		Color.RGBtoHSB(themeColor.getRed(),themeColor.getGreen(),themeColor.getBlue(),hsbVals);
		/*float sat = hsbVals[1];
		sat = (float)Math.min(1.0, sat+0.35);*/
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
				//setBackground(darkenedColor);
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
	
	
	
	public void updateText() {
		if(this.associatedPlayer!=null) {
			this.setText(associatedPlayer.getName()+"  |  "+associatedPlayer.getScore());
		}
		else {
			this.setText(pileText+" ("+this.associatedDeck.getCardsCount()+")");
		}
	}

	public void resetTheme() {
		this.currentBorder=defaultBorder;
		this.setBorder(defaultBorder);
		this.setContentAreaFilled(true);
		if(this.colorChangeManager!=null)  this.removeMouseListener(colorChangeManager);
		this.setBackground(null);
		if(this.boldened) this.bolden(); //stay boldened even if reset
	}
	
	public void makeTheme(Theme nt) {
		this.makeBackground(nt);
		this.currentBorderColor=(new Notification(nt)).getFg();
		LineBorder lb = new LineBorder(this.currentBorderColor,specialBordersSize);
		this.currentBorder=BorderFactory.createCompoundBorder(lb, getSpecialEmptyBorder());

		this.setBorder(currentBorder);

	}
	
	private void remakeBorder() {
		//adapt to changed border size if border is not the default one
		if(this.currentBorder==defaultBorder) this.setBorder(defaultBorder);
		else {
			LineBorder lb = new LineBorder(this.currentBorderColor,specialBordersSize);
			this.currentBorder=BorderFactory.createCompoundBorder(lb, getSpecialEmptyBorder());
			this.setBorder(currentBorder);
		}
		
	}
	
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
	
	public void unbolden() {
		if(this.boldened) {
			specialBordersSize-=boldenBy;
			remakeBorder();
		}
		this.boldened=false;
	}
	
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
