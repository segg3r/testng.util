package by.segg3r.testng.util.mongo;

import by.segg3r.testng.util.spring.SpringContextListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Listeners({SpringContextListener.class, MongoStartupListener.class})
public class MongoTest {

	@Autowired
	private MongoTemplate template;
	@Autowired
	private MongoStartupResult startupResult;

	@Test(description = "should be able to use mongo template")
	public void testTemplate() {
		template.save(new Entity());

		assertEquals(template.findAll(Entity.class).size(), 1);
	}

	@Test(description = "should clean database after each test",
		dependsOnMethods = "testTemplate")
	public void testDatabaseClean() {
		assertTrue(template.findAll(Entity.class).isEmpty());
	}

	@Test(description = "should provide mongo startup result through application context")
	public void testStartupResult() throws Exception {
		assertNotNull(startupResult);

		assertEquals(startupResult.getHost(), "127.0.0.1");
		assertTrue(startupResult.getPort() > 0);
		assertEquals(startupResult.getTemplate(), template);
		assertEquals(startupResult.getDatabaseName(), "integration_test_db");
		assertNotNull(startupResult.getExecutable());
		assertNotNull(startupResult.getMongo());
		assertNotNull(startupResult.getMongoProcess());
	}

	@Document(collection = "entities")
	public static final class Entity {}

}
