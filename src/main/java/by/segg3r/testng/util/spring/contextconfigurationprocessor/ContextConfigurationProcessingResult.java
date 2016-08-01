package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.util.Collections;
import java.util.List;

public class ContextConfigurationProcessingResult {

	public static ContextConfigurationProcessingResult empty() {
		return new ContextConfigurationProcessingResult();
	}
	
	private List<Object> autowiringCandidates;

	public ContextConfigurationProcessingResult() {
		this(Collections.emptyList());
	}

	public ContextConfigurationProcessingResult(
			List<Object> autowiringCandidates) {
		super();
		this.autowiringCandidates = autowiringCandidates;
	}

	public List<Object> getAutowiringCandidates() {
		return autowiringCandidates;
	}

	public void setAutowiringCandidates(List<Object> autowiringCandidates) {
		this.autowiringCandidates = autowiringCandidates;
	}

}
