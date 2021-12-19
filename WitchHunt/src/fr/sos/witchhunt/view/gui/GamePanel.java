package fr.sos.witchhunt.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.ActionsPanelController;
import fr.sos.witchhunt.model.Menu;  

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
	
	
	
	public GamePanel() {

		this.topNotificationsPanel = new TopNotificationsPanel(0,0,8,1);

		this.actionsPanel = new ActionsPanel(8,0,2,7);
		this.playersPanel = new PlayersPanel(0,1,2,6);
		this.cardsPanel = new CardsPanel(2,1,6,6);
		this.botNotificationsPanel = new BotNotificationsPanel (0,7,8,3);
		this.scorePanel = new ScorePanel(8,7,2,3);
		this.setLayout(new GridBagLayout());
		
		buildCustomGridBag();
	}

	
	public void displayMainNotification(Notification n) {
		botNotificationsPanel.appendNotification(n);
		topNotificationsPanel.setNotification(n);
	}
	public void displaySecondaryNotification(Notification n) {
		botNotificationsPanel.appendNotification(n);
	}
	
	public void resetNotificationsBoxes() {
		botNotificationsPanel.eraseContent();
		topNotificationsPanel.eraseContent();
	}
	
	public void wannaContinue(InputMediator im) {
		actionsPanel.wannaContinue(im);
	}
	
	public void displayMenu(Menu m) {
		actionsPanel.displayMenu(m);
	}

	
	public void makeChoice(Menu m) {
		actionsPanel.makeChoice(m, inputMediator);
	}

	
	public void resetActionPanel() {
		if(actionsPanel.isRendered) actionsPanel.resetPane();
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
		
		public void init() {
			
		}

		
	}
	
	private class TopNotificationsPanel extends CustomGridBagCell {
		private NotificationsBox notificationsBox = new NotificationsBox();
		
		public TopNotificationsPanel(int x, int y,int w, int h)  {
			super(	x,y,w,h);
			this.getPan().setLayout(new BorderLayout());
			this.getPan().setBackground(Color.BLACK);
			//this.getPan().add(textBox);
		}
		
		public void setNotification(Notification n) {
			this.notificationsBox.setNotification(n);
		}

		public NotificationsBox getTextBox () {
			return notificationsBox;
		}
		
		public void eraseContent() {
			this.notificationsBox.eraseContent();
		}
		
		@Override
		public void init() {
			notificationsBox = new NotificationsBox();
			notificationsBox.setPreferredSize(getPan().getPreferredSize());
			this.getPan().add(notificationsBox);
		}
	}
	
	private class ActionsPanel extends CustomGridBagCell {
		private JLabel prompt = new JLabel("",SwingConstants.CENTER);
		private List<ActionButton> actionButtonsList = new ArrayList<ActionButton>();
		private List<Component> interButtonsMargins = new ArrayList<Component> ();
		private boolean isRendered=false;
		private ActionsPanelController controller = null;
		
		public ActionsPanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			//this.getPan().setBackground(Color.RED);
			this.getPan().setLayout(new BoxLayout(this.getPan(),BoxLayout.PAGE_AXIS));	
		}
		
		
		@Override
		public void init() {
			this.getPan().setPreferredSize(this.getPan().getPreferredSize());
			this.prompt.setPreferredSize(this.getPan().getPreferredSize());
			this.getPan().add(Box.createRigidArea(new Dimension(0, 15))); //empty space above prompt
			this.prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.getPan().add(prompt);
			this.getPan().add(Box.createRigidArea(new Dimension(0, 30)));//empty space under prompt
			this.getPan().setAlignmentX(CENTER_ALIGNMENT);
		}
		public void wannaContinue(InputMediator im) {
			this.resetPane();
			this.prompt.setText("Wanna continue ?");
			ActionButton continueButton =  new ActionButton("CONTINUE");
			this.actionButtonsList.add(continueButton);
			this.controller= new ActionsPanelController(actionButtonsList,im);
			this.renderPane();
		}
		
		public void displayMenu(Menu m) {
			this.resetPane();
			this.prompt.setText(m.getName());
			for(Object o : m.getOptions()) {
				String buttonText = ActionButton.makeButtonText(o);
				if(buttonText!=null) {
					ActionButton b =  new ActionButton(buttonText);
					this.actionButtonsList.add(b);
				}
			}
			this.renderPane();
		}
		
		public void makeChoice(Menu m,InputMediator im) {
			this.controller= new ActionsPanelController(actionButtonsList,im);
			this.renderPane();
		}
		
		public void renderActionButtons() {
			
			Iterator<ActionButton> it = actionButtonsList.iterator();
			while(it.hasNext()) {
				ActionButton b = it.next();
				this.getPan().add(b);
				if (it.hasNext()) {
					Component aboveMargin = Box.createRigidArea(new Dimension(0, 15));
					this.interButtonsMargins.add(aboveMargin);
					this.getPan().add(aboveMargin);
				}
			}
			
		}
		
		public void resetPane() {
			this.prompt.setText("");
			
			this.actionButtonsList.forEach(b->this.getPan().remove(b));
			this.interButtonsMargins.forEach(b->this.getPan().remove(b));
			this.actionButtonsList.removeIf(b->true);
			this.interButtonsMargins.removeIf(m->true);
			this.isRendered=false;
			this.controller=null;
			this.getPan().updateUI();
		}
		public void renderPane() {
			renderActionButtons();
			this.isRendered=true;
			/*double maxButtonWidth = Collections.max(actionButtonsList.stream().mapToDouble(b->b.getPreferredSize().getWidth())
					.boxed().toList());
			double maxButtonHeight = Collections.max(actionButtonsList.stream().mapToDouble(b->b.getPreferredSize().getHeight())
				.boxed().toList());
			Dimension normalizedSize = new Dimension ((int)maxButtonWidth,(int)maxButtonHeight);
			actionButtonsList.forEach(b->{
				b.setSize(normalizedSize);
			});*/ //NORMALIZE BUTTON SIZES ... BUT THEN HOW TO CENTER THEM ?
		}
		
		public boolean isRendered() {
			return this.isRendered;
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
		private NotificationsBox notificationsBox;// = new TextBox();

		
		public BotNotificationsPanel(int x, int y,int w, int h)  {
			super(	x,y,w,h);
			this.getPan().setLayout(new BorderLayout());
			this.getPan().setBackground(Color.ORANGE);
			//this.getPan().add(textBox);
		}
		
		@Override
		public void init() {
			notificationsBox = new NotificationsBox();
			notificationsBox.setPreferredSize(getPan().getPreferredSize());
			this.getPan().add(notificationsBox);
		}
		
		public NotificationsBox getTextBox () {
			return notificationsBox;
		}
		public void eraseContent() {
			this.notificationsBox.eraseContent();
		}
		
		public void setTextBox (NotificationsBox tb) {
			this.notificationsBox=tb;
		}
		
		public void appendNotification(Notification n) {
			this.notificationsBox.printNotification(n);
		}
	}
	private class ScorePanel extends CustomGridBagCell{
		public ScorePanel(int x, int y,int w, int h) {
			super(x,y,w,h);
			this.getPan().setBackground(Color.PINK);
		}
	}

	public void initCells ( ) {
		/*TextBox textBox = new TextBox();
		textBox.setPreferredSize(this.botNotificationsPanel.getPan().getPreferredSize());
		this.botNotificationsPanel.getPan().add(textBox);
		this.botNotificationsPanel.setTextBox(textBox);*/
		cellsList.forEach(c->c.init());
	}


	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
	}







	
}
