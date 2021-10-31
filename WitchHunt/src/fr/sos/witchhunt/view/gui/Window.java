package fr.sos.witchhunt.view.gui;

import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JFrame;

import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.view.InputSource;

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements InputSource {
	
	//CONSTRUCTOR
	public Window () {
		this.setTitle("Witch Hunt");
		this.setSize(new Dimension(1280,720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JButton b = new JButton("Test");
		b.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				post("You clicked the button");
			}
		});
		this.getContentPane().add(b);
		
		this.setVisible(true);
	}
	
	@Override
	public void post(String str) {
		Application.inputController.wake();
		Application.inputController.receive(str);
		Application.console.log(str);
	}
	public void post() {
		Application.inputController.wake();
		Application.inputController.receive();
		Application.console.crlf();
	}
}

