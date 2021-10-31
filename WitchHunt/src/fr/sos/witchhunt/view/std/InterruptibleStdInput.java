package fr.sos.witchhunt.view.std;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Callable;

import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.view.InputSource;

public class InterruptibleStdInput implements Callable<String>, Runnable, InputSource {
	public InterruptibleStdInput() {
		
	}
	
	public String call() throws IOException {
	    BufferedReader br = new BufferedReader(
	        new InputStreamReader(System.in));
	    Application.console.invite();
	    String input;
	    try {
	        // wait until we have data to complete a readLine()
	        while (!br.ready() ) {
	          Thread.sleep(200);
	        }
	        input = br.readLine();
	    } catch (InterruptedException e) {
	        return null;
	    }
	    return input;
	  }
	

	@Override
	public void run() {
		String input=null;
		try {
			input=this.call();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if(input != null) post(input);
		
	}
}
