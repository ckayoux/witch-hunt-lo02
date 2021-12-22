package fr.sos.witchhunt.view.gui;

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

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
public class ActionButton extends JButton {
	
	private Color bgColor=null;
	private Color hoverColor=null;
	private MouseListener colorChangeManager=null;
	private Border defaultBorder = null;
	private Border hoverBorder = null;
	private Border pressedBorder = null;
	private Insets insets = new Insets(10, 20, 10, 20);
	
	public ActionButton(String str) {
		super(str);
		this.setAlignmentX(CENTER_ALIGNMENT);
		//this.setMargin(insets);
		this.defaultBorder=this.getBorder();
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
	
	public static String makeButtonText(Object o) {
		String buttonText=null;
		if (o instanceof String) {
			String str = (String) o;
			if(str.matches("^/c/.*")) {
				buttonText=null;//console only
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
