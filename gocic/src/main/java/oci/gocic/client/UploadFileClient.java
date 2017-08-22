/**
 * 
 */
package oci.gocic.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * @author runge
 *
 */
public class UploadFileClient {
	
	public static void main(String[] args) throws IOException 	{
		
//		ThridPartyMetaData metaDataBody = new ThridPartyMetaData("Awesome 3rdPartyApp", 12.50);
//		metaDataBody.addLocation("LC2");
//		metaDataBody.addLocation("LC3");
//		metaDataBody.printLocation();
		
		//TODO: read metadata from a JSON file into a Java object		
		
		// read metadata from a JSON file into a string    
	    String metaDataBody = new String(Files.readAllBytes(Paths.get("C:\\Users\\runge\\git\\oci\\gocic\\src\\main\\java\\oci\\gocic\\types\\metadata.txt")));	
		
		final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
	 
	    final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("C:\\oci-test\\ThirdParty\\hamburger.png"));
	    
	    
	    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();	    
	    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("metadata", metaDataBody, MediaType.APPLICATION_JSON_TYPE).bodyPart(filePart);
    
	    final WebTarget target = client.target("http://localhost:8080/gocic/webapi/files");
	    final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
	    
	    System.out.println(response.toString());
	    //Use response object to verify upload success
	     
	    formDataMultiPart.close();
	    multipart.close();
	}
}