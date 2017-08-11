/**
 * 
 */
package oci.gocic.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;



/**
 * @author runge
 *
 */
public class DeleteFileClient {
	

	public static void main(String[] args) throws IOException 
	{	    
	    String Uri = "http://localhost:8080/messenger/webapi/files";
	    String fileName = "hamburger.png";
	    
	    Client client = ClientBuilder.newClient();
	    WebTarget target = client.target(Uri).path(fileName);
	    Response response = target.request().delete();
	    System.out.println("Status code:" + response.getStatus());
	}
}