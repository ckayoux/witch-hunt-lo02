package fr.sos.witchhunt.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel;

public class PlayerCreatorController implements InputSource {
	private InputMediator inputMediator;
	private PlayerCreatorPanel pcp;
	private int nthPlayer;
	private int nthCPUPlayer;
	private List<String> takenNames;
	
	private boolean couldNotCreate=false;
	
	public PlayerCreatorController(int nthPlayer,int nthCPUPlayer,PlayerCreatorPanel p,List<String> takenNames,InputMediator im) {
		this.inputMediator=im;
		this.nthCPUPlayer=nthCPUPlayer;
		this.nthPlayer=nthPlayer;
		this.takenNames=takenNames;
		this.pcp=p;
		
		p.getIsHumanCB().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!p.getIsHumanCB().isSelected()) {
					p.getNameField().setText("CPU "+nthCPUPlayer);
					p.getNameField().setEnabled(false);
				}
				else {
					p.getNameField().setEnabled(true);
				}
			}
		});
		
		p.getNameField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			 public void changedUpdate(DocumentEvent e) {
				  if(couldNotCreate) this.resetCreateButton();
			  }
			  @Override
			public void removeUpdate(DocumentEvent e) {
				  if(couldNotCreate)  this.resetCreateButton();
			  }
			  @Override
			public void insertUpdate(DocumentEvent e) {
				  if(couldNotCreate) this.resetCreateButton();
			  }
			  
			  private void resetCreateButton() {
				  p.resetCreateButton();
				  couldNotCreate=false;
			  }
		});
		
		p.getNameField().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryCreatingPlayer();
			}
		});
		
		p.getCreateButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryCreatingPlayer();
			}
		});
	}
		
	private void tryCreatingPlayer() {
		String name = this.pcp.getNameField().getText();
		boolean isHuman = this.pcp.getIsHumanCB().isSelected();
		if(isHuman) {
			if(name!=null && name.length()>1 && !takenNames.contains(name)&& !name.contains("CPU")) {
				post(inputMediator.createPlayer(nthPlayer,name,isHuman));
			}
			else {
				if(takenNames.contains(name)) {
					this.pcp.reject("Name already taken");
					couldNotCreate=true;
				}
				else if(name.contains("CPU")) {
					this.pcp.reject("Reserved name");
					couldNotCreate=true;
				}
				else {
					this.pcp.reject("No name provided");
					couldNotCreate=true;
				}
			}
		}
		else {
			post(inputMediator.createPlayer(nthPlayer,"",isHuman));
		}
	}

	@Override
	public void post(String str) {
		
	}

	@Override
	public void post() {
	
	}
	
	@Override
	public void post(Player p) {
		this.inputMediator.receive(p);
	}

	public void destroy() {
		for(ActionListener al : this.pcp.getCreateButton().getActionListeners()) this.pcp.getCreateButton().removeActionListener(al);
		for(ActionListener al : this.pcp.getIsHumanCB().getActionListeners()) this.pcp.getIsHumanCB().removeActionListener(al);
		for(ActionListener al : this.pcp.getNameField().getActionListeners()) this.pcp.getNameField().removeActionListener(al);
	}


}
