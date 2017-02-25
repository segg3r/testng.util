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

	public MongoStartupResult(MongodExecutable executable,
			MongodProcess mongoProcess, MongoClient mongo,
			MongoTemplate template) {
		super();
		this.executable = executable;
		this.mongoProcess = mongoProcess;
		this.mongo = mongo;
		this.template = template;
	}

	public MongodExecutable getExecutable() {
		return executable;
	}

	public void setExecutable(MongodExecutable executable) {
		this.executable = executable;
	}

	public MongodProcess getMongoProcess() {
		return mongoProcess;
	}

	public void setMongoProcess(MongodProcess mongoProcess) {
		this.mongoProcess = mongoProcess;
	}

	public MongoClient getMongo() {
		return mongo;
	}

	public void setMongo(MongoClient mongo) {
		this.mongo = mongo;
	}

	public MongoTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

}
