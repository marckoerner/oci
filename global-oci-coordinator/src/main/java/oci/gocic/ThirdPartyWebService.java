package oci.gocic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;

import oci.thirdparty.types.ThridPartyMetaData;

@Path("/files")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.MULTIPART_FORM_DATA)
public class ThirdPartyWebService {
	
	private static final String OCI_PATH = "C:\\oci-test\\";
	private static final String OCI_GC_PATH = OCI_PATH + "GC\\";

	@GET
	@Path("/{fileName}")
	public Response getFile(@PathParam("fileName") String fileName) {
		
		File file = new File(OCI_GC_PATH + fileName);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
				"attachment; filename=image_from_server.png");
		return response.build();
	}

	@POST
	public Response addFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("metadata") String metaDataJson) throws IOException {

		//		System.out.println(metaDataJson);

		// convert JSON string into Java object with GSON lib
		Gson g = new Gson();
		ThridPartyMetaData metaDataObject = g.fromJson(metaDataJson, ThridPartyMetaData.class);

		System.out.println("Name: " + metaDataObject.getName());
		System.out.println("Price: " + metaDataObject.getPrice());
		System.out.println("Location: " + metaDataObject.getLocation());

		String gcFilePath = OCI_GC_PATH + fileDetail.getFileName();

		try {
			OutputStream out = new FileOutputStream(new File(gcFilePath));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String output = "File uploaded to GC: " + gcFilePath;

		distributeToLocalCoordinators(gcFilePath, metaDataObject);

		return Response.status(200).entity(output).build();
	}

	// distribute to LCs as defined in metadata 
	private void distributeToLocalCoordinators(String gcFilePath, ThridPartyMetaData metaDataObject) {
		String s = null;
		
        Iterator<String> itr = metaDataObject.getLocation().iterator();
        while(itr.hasNext())
        {
			try {
				// run the copy command using the Runtime exec method:
				//TODO: should be for Linux with the help of scp
				// Example: scp /home/stacy/images/image*.jpg stacy@myhost.com:/home/stacy/archive
//				String command = "scp " + gcFilePath.toString() + " " + "user@" + gocic.getIpAddress(metaDataObject.getName()) + ":" + OCI_PATH;
				String lcName = itr.next();
				String command = "cmd.exe /C copy " + gcFilePath.toString() + " " + OCI_PATH + lcName;
				Process p = Runtime.getRuntime().exec(command);
				
				// store the transfered files in the GC state		
				GlobalOciCoordinator.localCoordinatorFiles.get(lcName).add(metaDataObject.getFileName());

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				// read the output from the command
				System.out.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}

				// read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}

			}
			catch (IOException e) {
				System.out.println("exception happened - here's what I know: ");
				e.printStackTrace();
			}
		}
        
        GlobalOciCoordinator.printAllLocalCoordinatorFiles();
	}

	@PUT
	@Path("/{fileName}")
	public Response updateFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		File file = new File(fileDetail.getFileName());

		if (file.exists()) {

			try {
				OutputStream out = new FileOutputStream(new File(OCI_GC_PATH + fileDetail.getFileName()));
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String output = "File updated: " + OCI_GC_PATH + fileDetail.getFileName();
			return Response.status(200).entity(output).build();	

		}
		else
		{
			String output = "ERROR: Non-existing file cannot be updated : "+ OCI_GC_PATH + fileDetail.getFileName();
			return Response.status(500).entity(output).build();	
		}	
	}	

	@DELETE
	@Path("/{fileName}")
	public Response deleteFile(@PathParam("fileName") String fileName) {
		boolean bool = false;
		File file = new File(OCI_GC_PATH + fileName);
		bool = file.delete();

		if (bool) {
			String output = "File deleted : "+ OCI_GC_PATH + fileName;
			return Response.status(200).entity(output).build();			
		}
		else {
			String output = "ERROR: File cannot be deleted : "+ OCI_GC_PATH + fileName;
			return Response.status(500).entity(output).build();
		}
	}
}
