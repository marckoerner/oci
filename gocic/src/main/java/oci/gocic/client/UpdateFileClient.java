/**
 * 
 */
package oci.gocic.client;

import java.io.File;
//import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

//import org.glassfish.jersey.media.multipart.FormDataMultiPart;
//import org.glassfish.jersey.media.multipart.MultiPartFeature;
//import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * @author runge
 *
 */
public class UpdateFileClient {
	

	public static void main(String[] args) throws IOException 
	{
		
		
		
		
		
		
		
		
		
		
	    final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		 
	    final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("C:/test/hamburger.png"));
	    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
	    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(filePart);
	      
	    final WebTarget target = client.target("http://localhost:8080/messenger/webapi/files");
	    final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
	    
	    System.out.println("Status code:" + response.getStatus());
	    //Use response object to verify upload success
	     
	    formDataMultiPart.close();
	    multipart.close();
		
		
		
		
//		----------
		
//		final String URI = "http://localhost:8080/messenger/webapi/files/";
//		final String fileName = "hamburger.png";
//		
//		File file = new File("C:/test/hamburger.png");
//		
//		
//	    Client client = ClientBuilder.newClient();
//	    WebTarget target = client.target(URI).path(fileName);
//	    Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE).put(Entity.entity(file, MediaType.MULTIPART_FORM_DATA_TYPE));
//
//	    System.out.println("Status code:" + response.getStatus());
//	    
//	    ------------
	    
//	    Book createdBook = response.readEntity(Book.class);
//	    return createdBook;
//	    
//	    
//	    
//	    
//	 
//	    final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("C:/test/hamburger.png"));
//	    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
//	    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(filePart);
//	      
//	    final WebTarget target = client.target(URI);
//	    final Response response = target.request().put(Entity.entity(multipart, multipart.getMediaType()));
//	    
//	    System.out.println(response.toString());
//	    //Use response object to verify upload success
//	     
//	    formDataMultiPart.close();
//	    multipart.close();
	}
	   
}