package by.segg3r.testng.util.mongo;

import by.segg3r.testng.util.SuiteTestListener;
import by.segg3r.testng.util.spring.ContextConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testng.IExecutionListener;
import org.testng.ITestResult;

@ContextConfiguration(configClasses = {MongoInstanceConfiguration.class})
public class MongoStartupListener implements IExecutionListener, SuiteTestListener {

	private static final String SYSTEM_COLLECTION_PREFIX = "system";

	@Override
	public void onExecutionStart() {
		MongoInstance.start();
	}
	
	@Override
	public void onExecutionFinish() {
		MongoInstance.stop();
	}

	@Override
	public void onAfterSuiteTest(Object suite, ITestResult testResult) {
		MongoTemplate template = MongoInstance.get();
		for (String collectionName : template.getDb().getCollectionNames()) {
			if (!collectionName.startsWith(SYSTEM_COLLECTION_PREFIX)) {
				template.dropCollection(collectionName);
			}
		}
	}

}
