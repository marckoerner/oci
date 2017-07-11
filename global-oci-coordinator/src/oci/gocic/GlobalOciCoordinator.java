/**
 * 
 */
package oci.gocic;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author marc koerner
 *
 */
public class GlobalOciCoordinator {
	
    private static final Logger LOGGER = Logger.getLogger(GlobalOciCoordinator.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOGGER.info("Logger Name: " + LOGGER.getName());
		LOGGER.info("Global OCI Coordinator started");
		
		System.out.println("Type exit+Enter to stop the program");
		
		String inLine = "";
		InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
	    BufferedReader		bufferedReader 		= new BufferedReader(inputStreamReader);
		
		boolean exit = false;
		while(exit == false){
			
			try {
				inLine = bufferedReader.readLine();
			} catch (IOException error) {
				LOGGER.warning(error.getMessage());
				error.printStackTrace();
			}
			
			if(inLine.equals("exit")) {
				exit = true;
			}

		} // while
		
		LOGGER.info("Global OCI Coordinator stopped");

	}

}
