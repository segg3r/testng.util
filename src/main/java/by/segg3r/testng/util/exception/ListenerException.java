package by.segg3r.testng.util.exception;

public class ListenerException extends RuntimeException {

	private static final long serialVersionUID = 7034046379209144593L;

	public ListenerException(String message) {
		super(message);
	}
	
	public ListenerException(String message, Throwable t) {
		super(message, t);
	}
	
}
