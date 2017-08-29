/**
 * 
 */
package oci.gocic;

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
	
    private static final Logger LOGGER = Logger.getLogger(GlobalOciCoordinator.class.getName());
		
	public static void main(String[] args) throws Exception {
		LOGGER.info("Logger Name: " + LOGGER.getName());
		LOGGER.info("Global OCI Coordinator started");
				
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(9999);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "oci.gocic");        
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, MultiPartFeature.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        }
        finally {
            jettyServer.destroy();
        }
				
		LOGGER.info("Global OCI Coordinator stopped");

	} // main

} // class GlobalOciCoordinator
