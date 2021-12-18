package fr.sos.witchhunt.view.gui;

import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JFrame;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.view.InputSource;

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame /*implements InputSource*/ {
	
	public static final int WIDTH = 1600;
	public static final int HEIGHT= 900;
	
	//CONSTRUCTOR
	public Window () {
		this.setTitle("Witch Hunt");
		this.setSize(new Dimension(WIDTH,HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);	
		GamePanel gamePanel = new GamePanel();
		this.setContentPane(gamePanel);
		this.setVisible(true);
		gamePanel.onceCreatedDo();
	}
	/*
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}
	public void post() {
		inputMediator.receive();
	}*/
}

