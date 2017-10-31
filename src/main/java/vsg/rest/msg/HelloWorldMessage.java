package vsg.rest.msg;

/**
 * Created by Denis Orlov.
 */
public class HelloWorldMessage {


	private String message;

	public HelloWorldMessage(String message) {
		this.message = message;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String pMessage) {
		message = pMessage;
	}


}
