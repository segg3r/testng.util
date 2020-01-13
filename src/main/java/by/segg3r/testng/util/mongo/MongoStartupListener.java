package by.segg3r.testng.util.mongo;

import by.segg3r.testng.util.SuiteTestListener;
import by.segg3r.testng.util.spring.ContextConfiguration;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testng.IExecutionListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ContextConfiguration(configClasses = {MongoInstanceConfiguration.class})
public class MongoStartupListener implements IExecutionListener, SuiteTestListener {

	private static final String SYSTEM_COLLECTION_PREFIX = "system";
	private static final long COLLECTION_REMOVAL_TIMEOUT = 60 * 1000; //60s
	private static final long COLLECTION_REMOVAL_SLEEP = 10; //10ms

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
		getTestCollectionNames().forEach(template::dropCollection);

		waitTestCollectionsRemoved();
	}

	private void waitTestCollectionsRemoved() {
		long start = System.currentTimeMillis();
		while (!getTestCollectionNames().isEmpty()) {
			try {
				Thread.sleep(COLLECTION_REMOVAL_SLEEP);
			} catch (InterruptedException e) {
				throw new RuntimeException(
						"Sleep was interrupted while waiting for test collections to be removed.");
			}

			long now = System.currentTimeMillis();
			if (now - start >= COLLECTION_REMOVAL_TIMEOUT) {
				throw new RuntimeException("Test collections were not removed in timeout: "
						+ COLLECTION_REMOVAL_TIMEOUT);
			}
		}
	}

	private List<String> getTestCollectionNames() {
		MongoTemplate template = MongoInstance.get();
		MongoIterable<String> collectionIterator = template.getDb().listCollectionNames();
		List<String> testCollectionNames = new ArrayList<>();
		MongoCursor<String> cursor = collectionIterator.iterator();
		while (cursor.hasNext()) {
			String collectionName = cursor.next();
			if (!collectionName.startsWith(SYSTEM_COLLECTION_PREFIX)) {
				testCollectionNames.add(collectionName);
			}
		}
		return testCollectionNames;
	}

}
