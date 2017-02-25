package by.segg3r.testng.util.mongo;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class MongoInstance {

	private static final Logger LOG = LogManager.getLogger(MongoInstance.class);

	private static final String MONGO_HOST = "127.0.0.1";
	private static final String MONGO_DB_NAME = "integration_test_db";
	private static final int MONGO_PORT_RANGE_START = 27117;
	private static final int MONGO_PORT_RANGE_END = 32000;

	private static MongodExecutable executable;
	private static MongodProcess mongoProcess;
	private static MongoClient mongo;
	private static MongoTemplate template;

	public static MongoTemplate get() {
		return template;
	}

	public static MongoDbFactory getFactory() {
		return new SimpleMongoDbFactory(mongo, MONGO_DB_NAME);
	}

	public static synchronized void start() {
		if (template == null) {
			int port = findAvailablePort();
			
			MongoStartupResult mongoStartupResult = startMongo(port);
			
			executable = mongoStartupResult.getExecutable();
			mongoProcess = mongoStartupResult.getMongoProcess();
			mongo = mongoStartupResult.getMongo();
			template = mongoStartupResult.getTemplate();
		}
	}

	public static synchronized void stop() {
		try {
			if (template != null) {
				mongo.close();
				mongoProcess.stop();
				executable.stop();
			}
		} catch (Exception e) {
			LOG.warn("Could not stop mongo instance", e);
		}
	}
	
	private static int findAvailablePort() {
		for (int port = MONGO_PORT_RANGE_START; port <= MONGO_PORT_RANGE_END; port++) {
			if (isPortAvailable(port)) {
				return port;
			}
		}
		
		String errorMessage = "Could not find free port in port range ("
				+ MONGO_PORT_RANGE_START + "-" + MONGO_PORT_RANGE_END + ")";
		LOG.error(errorMessage);
		throw new RuntimeException(errorMessage);
	}
	
	private static boolean isPortAvailable(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			// Do nothing
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
				}
			}
		}

		return false;
	}
	
	private static MongoStartupResult startMongo(int port) {
		MongodExecutable executable = null;
		
		try {
			Command command = Command.MongoD;
	
			IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
					.defaults(command)
					.artifactStore(
							new ArtifactStoreBuilder()
									.defaults(command)
									.download(
											new DownloadConfigBuilder()
													.defaultsForCommand(command))
									.executableNaming(new UUIDTempNaming()))
					.build();
	
			MongodStarter runtime = MongodStarter
					.getInstance(runtimeConfig);
	
			IMongodConfig mongoConfig = new MongodConfigBuilder()
					.version(Version.V3_0_2)
					.net(new Net(port, Network.localhostIsIPv6()))
					.build();
	
			executable = runtime.prepare(mongoConfig);
			MongodProcess mongoProcess = executable.start();
	
			MongoClient mongo = new MongoClient(MONGO_HOST, port);
			mongo.getDatabase(MONGO_DB_NAME);
			MongoTemplate template = new MongoTemplate(mongo, MONGO_DB_NAME);
			
			return new MongoStartupResult(executable, mongoProcess, mongo, template);
		} catch (IOException e) {
			if (executable != null) {
				executable.stop();
			}
			
			String errorMessage = "Error starting embedded Mongo instance";
			LOG.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}

}
