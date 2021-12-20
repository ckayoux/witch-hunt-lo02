package fr.sos.witchhunt.view.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.ActionsPanelController;
import fr.sos.witchhunt.model.Menu;

public class ChoicesPanel extends GridBagCell {
	private JLabel prompt = new JLabel("",SwingConstants.CENTER);
	private List<ActionButton> actionButtonsList = new ArrayList<ActionButton>();
	private List<Component> interButtonsMargins = new ArrayList<Component> ();
	private boolean isRendered=false;
	private ActionsPanelController controller = null;
	
	public ChoicesPanel(int x, int y,int w, int h,Border cellBorder,List<GridBagCell> cellsList) {
		super(x,y,w,h,cellBorder,cellsList);
		//this.getPan().setBackground(Color.RED);
		this.getPan().setLayout(new BoxLayout(this.getPan(),BoxLayout.PAGE_AXIS));	
	}
	
	
	@Override
	public void init() {
		this.getPan().setPreferredSize(this.getPan().getPreferredSize());
		this.prompt.setPreferredSize(this.getPan().getPreferredSize());
		this.getPan().add(Box.createRigidArea(new Dimension(0, 30))); //empty space above prompt
		this.prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.getPan().add(prompt);
		this.getPan().add(Box.createRigidArea(new Dimension(0, 15)));//empty space under prompt
		this.getPan().setAlignmentX(Component.CENTER_ALIGNMENT);
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
				b.setTheme(ActionButton.getButtonColorByActionType(o));
				this.actionButtonsList.add(b);
			}
		}
		this.renderPane();
	}
	
	public void setThemeAsForActionType(Object o) {
		this.actionButtonsList.forEach(b->b.setTheme(ActionButton.getButtonColorByActionType(o)));
	}
	
	public void makeChoice(Menu m,InputMediator im) {
		this.controller= new ActionsPanelController(actionButtonsList,im);
		this.renderPane();
	}
	
	public void renderActionButtons() {
		
		Iterator<ActionButton> it = actionButtonsList.iterator();
		double maxButtonWidth = Collections.max(actionButtonsList.stream().mapToDouble(b->b.getPreferredSize().getWidth())
				.boxed().toList());
		while(it.hasNext()) {
			
			ActionButton b = it.next();
			Component aboveMargin = Box.createRigidArea(new Dimension(0, 40));
			this.interButtonsMargins.add(aboveMargin);
			this.getPan().add(aboveMargin);
			
			int missingXMargin = (int) (maxButtonWidth - b.getPreferredSize().getWidth());
			int missingLeftMargin = missingXMargin/2 + missingXMargin%2;
			int missingRightMargin = missingXMargin - missingLeftMargin;
			b.setInsets(new Insets(b.getMargin().top,b.getMargin().left + missingLeftMargin, b.getMargin().bottom,b.getMargin().right + missingRightMargin));
			
			this.getPan().add(b);
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
	}
	
	public boolean isRendered() {
		return this.isRendered;
	}
	
	
}