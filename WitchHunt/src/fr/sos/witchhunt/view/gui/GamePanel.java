package fr.sos.witchhunt.view.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import fr.sos.witchhunt.InputMediator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;  

public class GamePanel extends JPanel {
	
	private InputMediator inputMediator;
	
	private final int gridWidth = 10;
	private final int gridHeight = 10;
	private final Insets insets = new Insets(5,5,5,5);
	
	private List<CustomGridBagCell> cellsList = new ArrayList<CustomGridBagCell>();
	
	private final Border cellBorder = BorderFactory.createLineBorder(new Color(41,128,185));
	
	private TopNotificationsPanel topNotificationsPanel;
	private ActionsPanel actionsPanel;
	private PlayersPanel playersPanel;
	private CardsPanel cardsPanel;
	private BotNotificationsPanel botNotificationsPanel;
	private ScorePanel scorePanel;
	
	
	
	public GamePanel(/*InputMediator im*/) {
		/*this.inputMediator = im;*/
		/*this.setLayout(new BorderLayout());
		this.add(new JButton("messages recents"),BorderLayout.NORTH);
		this.add(new JButton("Joueurs"),BorderLayout.WEST);
		this.add(new JButton("Cartes"),BorderLayout.CENTER);
		this.add(new JButton("Joueurs"),BorderLayout.EAST);
		AllNotificationsPanel anp = new AllNotificationsPanel();
		JPanel south = new JPanel(){
			@Override
			public Insets getInsets() {
				return insets;
			}
		};
		BoxLayout bl = new BoxLayout (south,BoxLayout.LINE_AXIS);
		south.add(anp);
		this.add(south,BorderLayout.SOUTH);*/
		
		this.topNotificationsPanel = new TopNotificationsPanel(0,0,8,1);

		this.actionsPanel = new ActionsPanel(8,0,2,7);
		this.playersPanel = new PlayersPanel(0,1,2,6);
		this.cardsPanel = new CardsPanel(2,1,6,6);
		this.botNotificationsPanel = new BotNotificationsPanel (0,7,8,3);
		this.scorePanel = new ScorePanel(8,7,2,3);
		this.setLayout(new GridBagLayout());
		
		buildCustomGridBag();
		/*gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = this.insets;
		

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 7;
		gbc.gridheight = 1;
		gbc.weightx=gbc.gridwidth/(float)9;
		gbc.weighty=gbc.gridheight/(float)9;
		this.add(topNotificationsPanel,gbc);
		gbc.weightx=2/(float)9;
		gbc.weighty=5/(float)9;
		gbc.gridwidth=2;
		gbc.gridheight=6;
		gbc.gridx = 6;
		this.add(actionsPanel,gbc);
		gbc.gridwidth=1;
		gbc.gridheight=5;
		gbc.weightx=1/(float)9;
		gbc.weighty=5/(float)9;
		gbc.gridx=0;
		gbc.gridy=1;
		this.add(playersPanel,gbc);
		gbc.gridx=1;
		gbc.gridwidth=5;
		gbc.gridheight=4;
		gbc.weightx=5/(float)9;
		gbc.weighty=5/(float)9;
		this.add(cardsPanel,gbc);
		gbc.gridx=0;
		gbc.gridy=6;
		gbc.gridwidth=6;
		gbc.gridheight=2;
		gbc.weightx=6/(float)9;
		gbc.weighty=2/(float)9;
		this.add(botNotificationsPanel,gbc);
		gbc.gridx=6;
		gbc.gridy=6;
		gbc.gridwidth=2;
		gbc.weightx=2/(float)9;
		gbc.weighty=2/(float)9;
		this.add(scorePanel,gbc);*/
	}
	
	public void onceCreatedDo() {
		cellsList.forEach(c->c.onceCreatedDo());
		for(int i=0;i<100;i++) botNotificationsPanel.getTextBox().log("tast");
		botNotificationsPanel.getTextBox().log("test");
		topNotificationsPanel.setNotification("toto",Color.GREEN);

	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.WIDTH,Window.HEIGHT);
	}
	@Override
	public Insets getInsets() {
		return new Insets(15,7,15,7); //vertical margin is double the horizontal margin
	}
	
	public void buildCustomGridBag () {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = this.insets;
		
		Iterator<CustomGridBagCell> it = cellsList.iterator();
		while (it.hasNext()) {
			CustomGridBagCell cell = it.next();
			int w = cell.getWidth();
			int h = cell.getHeight();
			int x = cell.getX();
			int y = cell.getY();
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = w;
			gbc.weightx = w/(float)this.gridWidth;
			gbc.gridheight = h;
			gbc.weighty = h/(float)this.gridHeight;
			this.add(cell.getPan(),gbc);
		}
	}
	
	private class CustomGridBagCell{
		private int gwidth;
		private int gheight;
		private int gx;
		private int gy;
		private JPanel pan;
		
		public CustomGridBagCell (int x, int y,int w, int h) {
			pan=new JPanel();
			pan.setBorder(cellBorder);
			this.gx=x;
			this.gy=y;
			this.gwidth=w;
			this.gheight=h;
			cellsList.add(this);
		}
		
		public void onceCreatedDo() {
			
		}
		
		public int getX() {
			return gx;
		}
		public int getY() {
			return gy;
		}
		
		public int getWidth() {
			return this.gwidth;
		}
		public int getHeight() {
			return this.gheight;
		}
		public JPanel getPan() {
			return pan;
		}
		
	}
	
	private class TextBox extends JScrollPane{
		private JTextArea textArea ;
		//private JScrollPane scrollPane;
		private StyleContext context = new StyleContext();
	    private StyledDocument doc = new DefaultStyledDocument(context);
		public TextBox() {
			super(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			textArea = new JTextArea(doc);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			this.getViewport().add(textArea);
			
			DefaultCaret caret = (DefaultCaret)this.textArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
			this.textArea.setFont(new Font("Tahoma", Font.BOLD, 16));
		}

		public JTextArea getTextArea() {
			return this.textArea;
		}
		
		public void log(String str) {
			StringBuffer sb = new StringBuffer(str);
			sb.append('\n');
			this.textArea.append(sb.toString());
		}
		
		public void setText(String str) {
			this.textArea.setText(str);
		}
		
		public void print(String str)  {
			this.textArea.append(str);
		}
		
	}
	
	private class TopNotificationsPanel extends CustomGridBagCell {
		private TextBox textBox = new TextBox();
		
		public TopNotificationsPanel(int x, int y,int w, int h)  {
			super(	x,y,w,h);
			this.getPan().setLayout(new BorderLayout());
			this.getPan().setBackground(Color.BLACK);
			this.getPan().add(textBox);
		}
		
		public void setNotification(String str,Color c) {
			this.textBox.getTextArea().setForeground(c);
			this.textBox.setText(str);
		}

		public TextBox getTextBox () {
			return textBox;
		}
	}
	
	private class ActionsPanel extends CustomGridBagCell {
		public ActionsPanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			this.getPan().setBackground(Color.RED);
		}
	}
	
	private class PlayersPanel extends CustomGridBagCell {
		public PlayersPanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			this.getPan().setBorder(null);
			this.getPan().setBackground(Color.GREEN);
		}
	}
	private class CardsPanel extends CustomGridBagCell {
		public CardsPanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			this.getPan().setBackground(Color.YELLOW);
		}
	}
	private class BotNotificationsPanel extends CustomGridBagCell {
		private TextBox textBox = new TextBox();
		
		public BotNotificationsPanel(int x, int y,int w, int h)  {
			super(	x,y,w,h);
			this.getPan().setLayout(new BorderLayout());
			this.getPan().setBackground(Color.ORANGE);
			this.getPan().add(textBox);
		}
		
		
		public TextBox getTextBox () {
			return textBox;
		}
	}
	private class ScorePanel extends CustomGridBagCell{
		public ScorePanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			this.getPan().setBackground(Color.PINK);
		}
	}
	
	
}
