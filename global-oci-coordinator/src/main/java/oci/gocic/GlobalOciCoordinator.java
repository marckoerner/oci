/**
 * 
 */
package oci.gocic;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import com.google.gson.Gson;

import oci.gocic.types.GlobalCoordinatorConf;

/**
 * @author Torsten Runge
 *
 */
public class GlobalOciCoordinator {

	/**
	 * @param args
	 */

//TODO	static Vector<Vector<Float>> networkTopology = new Vector<Vector<Float>>();
	static GlobalCoordinatorConf config;
	static List<LocalCoordinator> localCoordinators = new ArrayList<>();
	static ConcurrentHashMap<String, List<LocalCoordinator>> fileToLocalCoordinatorMap = new ConcurrentHashMap<>();

	public static final Logger LOGGER = Logger.getLogger(GlobalOciCoordinator.class.getName());

	public static void main(String[] args) throws Exception {
		LOGGER.info("Logger Name: " + LOGGER.getName());
		LOGGER.info("Global OCI Coordinator started");
		
		// 1. Read Config File 
		
		if (args.length < 1)
		{
			LOGGER.info("ERROR: No Global Coordinator Config file specified. Example: gc.jar gc-conf.json");
		}
		
		String globalCoordinatorConfigJsonFile = "";
		globalCoordinatorConfigJsonFile = args[0];

		String globalCoordinatorConfigJsonString;

		if (!globalCoordinatorConfigJsonFile.isEmpty()) {
			
			// read config from a JSON file into a JSON string    
			globalCoordinatorConfigJsonString = new String(Files.readAllBytes(Paths.get(globalCoordinatorConfigJsonFile)));

			// convert JSON string into Java object with GSON lib	
			Gson g = new Gson();
			config = g.fromJson(globalCoordinatorConfigJsonString, GlobalCoordinatorConf.class);
			
			LOGGER.info("Global Coordinator Config file " + args[0] + " was successfully loaded.");
		}
		else {
			// generate GlobalCoordinatorConf Java object			
//			config = new GlobalCoordinatorConf();
//			config.addLocalCoordinator(1, InetAddress.getByName("127.0.0.1"), "DE");
//			config.addLocalCoordinator(2, InetAddress.getByName("127.0.0.2"), "FR");
//			config.addLocalCoordinator(3, InetAddress.getByName("127.0.0.3"), "DE");	
			
			
		}
		
		// 2. Setup GC
		
		localCoordinators = config.getLocalCoordinators();
		
		
		// 3. Setup Jetty

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/gc");

		Server jettyServer = new Server(9999);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "oci.gocic");       
		jerseyServlet.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, MultiPartFeature.class.getCanonicalName());

		// TODO: add some network links between local coordinators
//		addLink(lc1, lc2, metric);
//		networkTopology[1][2]=5;

		printLocalCoordinators();

		// Example end

		try {
			jettyServer.start();
			jettyServer.join();
		}
		finally {
			jettyServer.destroy();
		}

		LOGGER.info("Global OCI Coordinator stopped");

	}

	public static void printLocalCoordinators() {
		
		Iterator<LocalCoordinator> lcListItr = localCoordinators.iterator();
		while (lcListItr.hasNext())
		{
			LocalCoordinator currentLc = lcListItr.next();
			System.out.println("LC ID: " + currentLc.getId() + " IP: " + currentLc.getIp() + " Location: " + currentLc.getLocation());
		}
	}

	public static List<LocalCoordinator> getLocalCoordinatorsByLocation(String location) {
		
		List<LocalCoordinator> lcList = new ArrayList<LocalCoordinator>();		
		
		Iterator<LocalCoordinator> lcListItr = localCoordinators.iterator();
		while (lcListItr.hasNext())
		{
			LocalCoordinator currentLc = lcListItr.next();
			
			if (currentLc.getLocation().contains(location)) {
				lcList.add(currentLc);
			}
		}

		return lcList;		
	}

	public static LocalCoordinator getLocalCoordinator(int id) {
		
		Iterator<LocalCoordinator> lcListItr = localCoordinators.iterator();
		while (lcListItr.hasNext())
		{
			LocalCoordinator currentLc = lcListItr.next();
			
			if (currentLc.getId() == id) {
				return currentLc;
			}
		}		
		
		return null;
	}
	
	public static void printLocalCoordinatorFiles() {
		for (ConcurrentHashMap.Entry<String, List<LocalCoordinator>> entry : fileToLocalCoordinatorMap.entrySet()) {
			String key = entry.getKey().toString();
			List<LocalCoordinator> value = entry.getValue();

			Iterator<LocalCoordinator> itr = value.iterator();
			while(itr.hasNext())
			{      
				System.out.println("key: " + key + " value: " + "LC " + itr.next().getId());
			}     
		}

	}



}