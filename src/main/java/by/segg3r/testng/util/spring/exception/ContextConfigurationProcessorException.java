package by.segg3r.testng.util.spring.exception;

public class ContextConfigurationProcessorException extends RuntimeException {

	private static final long serialVersionUID = -4279751497106346778L;

	public ContextConfigurationProcessorException(String message) {
		super(message);
	}

	public ContextConfigurationProcessorException(String message, Throwable t) {
		super(message, t);
	}
	
}
