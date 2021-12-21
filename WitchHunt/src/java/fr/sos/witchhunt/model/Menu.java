package fr.sos.witchhunt.model;

import java.util.Collection;

import java.util.Iterator;

public class Menu {
	
	//ATTRIBUTES
	private String name;
	private Object [] options;
	private int optionsCount;
	
	public Menu(String name, Object ... options) {
		this.name = name;
		this.options=options;
		this.optionsCount=options.length;
	}

	
	//GETTERS
	public String getName() {
		return name;
	}
	public Object[] getOptions() {
		return options;
	}
	public int getOptionsCount() {
		return optionsCount;
	}
	public Object getNthOption(int n) {
		if (n>=1 && n<= optionsCount) {
			return options[n-1];
		}
		else return null;
	}

}
