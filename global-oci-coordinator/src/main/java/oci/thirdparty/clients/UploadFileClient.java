/**
 * 
 */
package oci.thirdparty.clients;

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

import com.google.gson.Gson;

import oci.thirdparty.types.ThridPartyMetaData;

/**
 * @author Torsten Runge
 *
 */

// TODO integrate Update und Delete Client
public class UploadFileClient {

	public static void main(String[] args) throws IOException {

		//final String OCI_PATH = "C:\\oci-test\\";
		//final String OCI_THIRDPARTY_PATH = OCI_PATH + "ThirdParty\\";

		final String OCI_PATH = "/home/runge/oci-test/";
		final String OCI_THIRDPARTY_PATH = OCI_PATH + "ThirdParty/";

		String metaDataJsonFile = "";
		//metaDataJsonFile = "C:\\Users\\runge\\git\\oci\\global-oci-coordinator\\src\\main\\java\\oci\\thirdparty\\metadata\\CoffeeEdgeServiceMetaData.json";
		metaDataJsonFile = "./src/main/java/oci/thirdparty/metadata/CoffeeEdgeServiceMetaData.json";

		String metaDataJsonString;
		ThridPartyMetaData metaDataObject;

		if (!metaDataJsonFile.isEmpty()) {
			// read meta data from a JSON file into a JSON string    
			metaDataJsonString = new String(Files.readAllBytes(Paths.get(metaDataJsonFile)));

			// convert JSON string into Java object with GSON lib	
			Gson g = new Gson();
			metaDataObject = g.fromJson(metaDataJsonString, ThridPartyMetaData.class);			
		}
		else {
			// generate ThridPartyMetaData Java object			
			metaDataObject = new ThridPartyMetaData("Hamburger Service 3rd Party Edge Service", "hamburger.png", 8.50);
			metaDataObject.addLocation("LC1");
			metaDataObject.addLocation("LC3");			

			// convert meta data from a Java Object into a JSON string 
			Gson g = new Gson();
			metaDataJsonString = g.toJson(metaDataObject);			
		}

		System.out.println("Name: " + metaDataObject.getServiceName());
		System.out.println("FileName: " + metaDataObject.getFileName());
		System.out.println("Price: " + metaDataObject.getPrice());
		System.out.println("Location: " + metaDataObject.getLocation());

		final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File(OCI_THIRDPARTY_PATH + metaDataObject.getFileName()));		    

		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();	    
		final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("metadata", metaDataJsonString, MediaType.APPLICATION_JSON_TYPE).bodyPart(filePart);

		//final WebTarget target = client.target("http://localhost:8080/gocic/webapi/files");
		final WebTarget target = client.target("http://localhost:9999/gc/files");
		final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

		System.out.println(response.toString());
		//Use response object to verify upload success

		formDataMultiPart.close();
		multipart.close();
	}
}