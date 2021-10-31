package fr.sos.witchhunt.view;

import fr.sos.witchhunt.controller.Application;

public interface InputSource {
	public default void post(String str) {
		Application.inputController.wake();
		Application.inputController.receive(str);
	}
	public default void post() {
		Application.inputController.wake();
		Application.inputController.receive();
	}
}
