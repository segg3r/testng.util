package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContextConfigurationProcessingResult {

	public static ContextConfigurationProcessingResult empty() {
		return new ContextConfigurationProcessingResult();
	}

	public static ContextConfigurationProcessingResult withAutowiringCandidates(List<Object> autowiringCandidates) {
		return new ContextConfigurationProcessingResult()
				.setAutowiringCandidates(autowiringCandidates);
	}

	public static ContextConfigurationProcessingResult merge(
			ContextConfigurationProcessingResult... results) {
		return merge(Lists.newArrayList(results));
	}

	public static ContextConfigurationProcessingResult merge(
			List<ContextConfigurationProcessingResult> contextConfigurationProcessingResults) {
		return ContextConfigurationProcessingResult.withAutowiringCandidates(
				contextConfigurationProcessingResults.stream()
						.flatMap(result -> result.getAutowiringCandidates().stream())
						.collect(Collectors.toList()));
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

	public ContextConfigurationProcessingResult setAutowiringCandidates(List<Object> autowiringCandidates) {
		this.autowiringCandidates = autowiringCandidates;
		return this;
	}

}
