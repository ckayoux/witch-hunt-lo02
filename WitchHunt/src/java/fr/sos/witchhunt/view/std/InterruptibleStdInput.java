package fr.sos.witchhunt.view.std;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;

/**
 * <p><b>Non-blocking console input reader.</b></p>
 * <p>Runnable as it is instantiated in its own thread by an {@link fr.sos.witchhunt.controller.InputMediator InputMediator} willing to collect standard input.
 * Can be interrupted at any moment.</p>
 * <p>Holds the role of Producer in the {@link fr.sos.witchhunt.controller.InputMediator InputMediator} - {@link fr.sos.witchhunt.view.InputSource InputSource} connexion, where 
 * {@link fr.sos.witchhunt.controller.InputMediator InputMediator} is an intermediary Consumer. Sends collected input through the {@link fr.sos.witchhunt.view.InputSource InputSource} 
 * interface's methods.</p>
 * 
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.view.InputSource InputSource
 * @see #call()
 * @see #run()
 * @see #post()
 * @see #post(String)
 */
public class InterruptibleStdInput implements Callable<String>, Runnable, InputSource {
	private InputMediator inputMediator;
	private StdView console;
	
	/**
	 * @param The calling {@link fr.sos.witchhunt.controller.InputMediator InputMediator}, to which collected user-input is {@link #post() sent}. 
	 * @param console {@link StdView The console view}, used for displaying the prompt symbol and newline characters.
	 */
	public InterruptibleStdInput(InputMediator im,StdView console) {
		this.console=console;
		this.inputMediator=im;
	}
	
	/**
	 * Reads a String terminated by a newline character from the standard input, System.in .
	 * Displays input-related symbols in the {@link #console}.
	 * @return The read String, or null if the reader was interrupted.
	 * @throws IOException
	 * @see {@link #run()} Called by the {@link #run()} method
	 * @see java.util.concurrent.Callable
	 */
	@Override
	public String call() throws IOException {
	    BufferedReader br = new BufferedReader(
	        new InputStreamReader(System.in));
	    console.invite(); //displays the prompt symbol
	    String input;
	    try {
	        // wait until we have data to complete a readLine()
	        while (!br.ready() ) {
	          Thread.sleep(200);
	        }
	        input = br.readLine();
	    } catch (InterruptedException e) {
	    	console.crlf();
	        return null;
	    }
	    return input;
	  }
	
	/**
	 * Input-reading is performed when the Thread starts.
	 * {@link #post(String) sends to the master input mediator} the {@link #call()} read String if it is not <code>null</code>
	 * @see #call()
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}
	
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post(Player p) {
		
	}
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post() {
		inputMediator.receive();
	}
}
