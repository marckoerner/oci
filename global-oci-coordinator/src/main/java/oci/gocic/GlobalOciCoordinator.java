/**
 * 
 */
package oci.gocic;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Torsten Runge
 *
 */
public class GlobalOciCoordinator {

	/**
	 * @param args
	 */
	
	static ConcurrentHashMap<String, LocalCoordinator> localCoordinators = new ConcurrentHashMap<>();
	
    private static final Logger LOGGER = Logger.getLogger(GlobalOciCoordinator.class.getName());
		
	public static void main(String[] args) throws Exception {
		LOGGER.info("Logger Name: " + LOGGER.getName());
		LOGGER.info("Global OCI Coordinator started");
				
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
        
        // Example
        // add some local coordinators      
        addLocalCoordinator("LC1", InetAddress.getByName("127.0.0.1"));
        addLocalCoordinator("LC2", InetAddress.getByName("127.0.0.2"));
        addLocalCoordinator("LC3", InetAddress.getByName("127.0.0.3"));
        
        printAllLocalCoordinators();
        
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
	
	public static List<LocalCoordinator> getAllLocalCoordinators() {
		return new ArrayList<LocalCoordinator>(localCoordinators.values());		
	}
	
	public static void printAllLocalCoordinators() {
	    for (ConcurrentHashMap.Entry<String, LocalCoordinator> entry : localCoordinators.entrySet()) {
	        String key = entry.getKey().toString();
	        String value = entry.getValue().getIp().toString();
	        System.out.println("key: " + key + " value: " + value);
	      }

	}
	
	public LocalCoordinator getLocalCoordinator(String name) {
		return localCoordinators.get(name);
	}
	
	public static LocalCoordinator addLocalCoordinator(String name, InetAddress ip) {
		LocalCoordinator lc = new LocalCoordinator(name, ip);
		localCoordinators.put(name, lc);
		return lc;
	}
	
	public LocalCoordinator removeLocalCoordinator(String name) {
		return localCoordinators.remove(name);
	}

}