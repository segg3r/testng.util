package by.segg3r.testng.util.mongo;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoStartupResult {

	private MongodExecutable executable;
	private MongodProcess mongoProcess;
	private MongoClient mongo;
	private MongoTemplate template;
	private String host;
	private int port;

	public MongoStartupResult() {
	}

	public MongodExecutable getExecutable() {
		return executable;
	}

	public MongoStartupResult setExecutable(MongodExecutable executable) {
		this.executable = executable;
		return this;
	}

	public MongodProcess getMongoProcess() {
		return mongoProcess;
	}

	public MongoStartupResult setMongoProcess(MongodProcess mongoProcess) {
		this.mongoProcess = mongoProcess;
		return this;
	}

	public MongoClient getMongo() {
		return mongo;
	}

	public MongoStartupResult setMongo(MongoClient mongo) {
		this.mongo = mongo;
		return this;
	}

	public MongoTemplate getTemplate() {
		return template;
	}

	public MongoStartupResult setTemplate(MongoTemplate template) {
		this.template = template;
		return this;
	}

	public String getHost() {
		return host;
	}

	public MongoStartupResult setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public MongoStartupResult setPort(int port) {
		this.port = port;
		return this;
	}

}
