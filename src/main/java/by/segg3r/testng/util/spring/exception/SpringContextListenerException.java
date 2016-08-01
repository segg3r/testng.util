package by.segg3r.testng.util.spring.exception;

public class SpringContextListenerException extends RuntimeException {

	private static final long serialVersionUID = -4279751497106346778L;

	public SpringContextListenerException(String message) {
		super(message);
	}

	public SpringContextListenerException(String message, Throwable t) {
		super(message, t);
	}
	
}
