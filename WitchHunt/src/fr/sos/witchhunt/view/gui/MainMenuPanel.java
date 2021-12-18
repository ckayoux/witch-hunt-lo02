package fr.sos.witchhunt.view.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.ActionsPanelController;
import fr.sos.witchhunt.model.Menu;

public class MainMenuPanel extends JPanel{
	private ActionsPanel actionsPanel;
	private InputMediator inputMediator;
	
	public MainMenuPanel() {
		super();
		this.setLayout(new BorderLayout());
		actionsPanel = new ActionsPanel();
		this.add(actionsPanel,BorderLayout.CENTER);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.HEIGHT,Window.WIDTH);
	}
	public void init() {
		this.actionsPanel.init();
	}
	

	public void makeChoice(Menu m) {
		this.actionsPanel.makeChoice(m, inputMediator);
	}
	
	private class ActionsPanel extends JPanel {
		private JLabel prompt = new JLabel("",SwingConstants.CENTER);
		private List<ActionButton> actionButtonsList = new ArrayList<ActionButton>();
		private List<Component> interButtonsMargins = new ArrayList<Component> ();
		private boolean isRendered=false;
		private ActionsPanelController controller = null;
		
		public ActionsPanel() {
			super();
			//this.getPan().setBackground(Color.RED);
			this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));	
		}
		
		
		public void init() {
			this.setPreferredSize(this.getPreferredSize());
			this.prompt.setPreferredSize(this.getPreferredSize());
			this.add(Box.createRigidArea(new Dimension(0, 15))); //empty space above prompt
			this.prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(prompt);
			this.add(Box.createRigidArea(new Dimension(0, 30)));//empty space under prompt
			this.setAlignmentX(CENTER_ALIGNMENT);
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

		}
		
		public void renderActionButtons() {
			
			Iterator<ActionButton> it = actionButtonsList.iterator();
			while(it.hasNext()) {
				ActionButton b = it.next();
				this.add(b);
				if (it.hasNext()) {
					Component aboveMargin = Box.createRigidArea(new Dimension(0, 15));
					this.interButtonsMargins.add(aboveMargin);
					this.add(aboveMargin);
				}
			}
			
		}
		
		public void resetPane() {
			this.prompt.setText("");
			
			this.actionButtonsList.forEach(b->this.remove(b));
			this.interButtonsMargins.forEach(b->this.remove(b));
			this.actionButtonsList.removeIf(b->true);
			this.interButtonsMargins.removeIf(m->true);
			this.isRendered=false;
			this.controller=null;
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

	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
		
	}

	public void displayMenu(Menu m) {
		actionsPanel.displayMenu(m);
	}

	public void resetActionPanel() {
		actionsPanel.resetPane();
	}

}
