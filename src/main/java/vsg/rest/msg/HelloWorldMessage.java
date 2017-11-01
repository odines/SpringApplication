package vsg.rest.msg;

import org.springframework.stereotype.Component;

/**
 * Created by Denis Orlov.
 */
@Component
public class HelloWorldMessage {


	private String message;

	public HelloWorldMessage() {
	}

	public HelloWorldMessage(String message) {
		this.message = message;
	}

	public void addMessage() {
		System.out.println(this.message + " Adding message");
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String pMessage) {
		message = pMessage;
	}


}
