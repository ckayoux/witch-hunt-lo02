package fr.sos.witchhunt.model;

public class Menu {
	
	//ATTRIBUTES
	private String name;
	private String [] options;
	private int optionsNumber;
	
	//SETTERS
	public Menu(String name, String ... options) {
		this.name = name;
		this.options=options;
		this.optionsNumber=options.length;
	}
	
	//GETTERS
	public String getName() {
		return name;
	}
	public String[] getOptions() {
		return options;
	}
	public int getOptionsNumber() {
		return optionsNumber;
	}

}
