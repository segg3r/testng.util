package by.segg3r.testng.util.spring;

import java.util.List;

import org.springframework.context.support.GenericApplicationContext;

public class ApplicationContextInitializationResult {

	private GenericApplicationContext applicationContext;
	private List<Object> autowiringCandidates;

	public ApplicationContextInitializationResult(
			GenericApplicationContext applicationContext,
			List<Object> autowiringCandidates) {
		super();
		this.applicationContext = applicationContext;
		this.autowiringCandidates = autowiringCandidates;
	}

	public GenericApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(
			GenericApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public List<Object> getAutowiringCandidates() {
		return autowiringCandidates;
	}

	public void setAutowiringCandidates(List<Object> autowiringCandidates) {
		this.autowiringCandidates = autowiringCandidates;
	}

}
