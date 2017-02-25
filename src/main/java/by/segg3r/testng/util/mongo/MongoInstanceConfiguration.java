package by.segg3r.testng.util.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoInstanceConfiguration {

	@Bean
	public MongoTemplate mongoTemplate() {
		return MongoInstance.get();
	}

	@Bean
	public MongoDbFactory mongoDbFactory() {
		return MongoInstance.getFactory();
	}

}
