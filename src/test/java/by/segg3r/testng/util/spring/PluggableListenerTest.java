package by.segg3r.testng.util.spring;

import by.segg3r.testng.util.SuiteTestListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Listeners({SpringContextListener.class, PluggableListenerTest.DatabaseContextListener.class})
public class PluggableListenerTest {

	public static class Database {
		private static Database instance;
		public Database mock;
		public static Database get() {
			if (instance == null) instance = new Database();
			return instance;
		}
		public Database() {
			this.mock = mock(Database.class);
		}
		public void dropTables() {
			mock.dropTables();
		}
	}

	@Configuration
	public static class DatabaseConfiguration {
		@Bean
		public Database database() {
			return Database.get();
		}
	}

	@ContextConfiguration(configClasses = DatabaseConfiguration.class)
	public static class DatabaseContextListener implements SuiteTestListener {
		@Override
		public void onAfterSuiteTest(Object suite, ITestResult testResult) {
			Database.get().dropTables();
		}
	}

	@Autowired
	private Database database;

	@Test(description = "setup")
	public void setup() {
	}

	@Test(description = "should have been drop tables", dependsOnMethods = {"setup"})
	public void testDropTables() {
		verify(database.mock).dropTables();
	}

}
