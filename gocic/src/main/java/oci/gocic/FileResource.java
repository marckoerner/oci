package oci.gocic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import oci.gocic.types.ThridPartyMetaData;

@Path("/files")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.MULTIPART_FORM_DATA)
public class FileResource {

	//	private static final String ThridParty_PATH = "C://oci-test//ThridParty";
	private static final String GC_PATH = "C:\\oci-test\\GC\\";
	private static final String LC1_PATH = "C:\\oci-test\\LC1\\";
//	private static final String LC2_PATH = "C:\\oci-test\\LC2\\";

	@GET
	@Path("/{fileName}")
	public Response getFile(@PathParam("fileName") String fileName) {

		File file = new File(GC_PATH + fileName);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
				"attachment; filename=image_from_server.png");
		return response.build();
	}

	@POST
	public Response addFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("metadata") String metaDataBody) throws IOException {
		
//TODO:		@FormDataParam("metadata") FormDataBodyPart metaDataBody) throws IOException {		
//		metaDataBody.setMediaType(MediaType.APPLICATION_JSON_TYPE);
//		ThridPartyMetaData metadata = metaDataBody.getValueAs(ThridPartyMetaData.class);
		
		System.out.println(metaDataBody);

		String gcFilePath = GC_PATH + fileDetail.getFileName();
			
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

		String output = "File uploaded to : " + GC_PATH + fileDetail.getFileName();

		distributeToLocalCoordinators(gcFilePath, LC1_PATH);

		return Response.status(200).entity(output).build();
	}

	// distribute to LCs as defined in metadata 
	//TODO: (so mapping of LC to IPs is needed)
	private void distributeToLocalCoordinators(String gcFilePath, String lc1FilePath) {
		String s = null;

		try {
			// run the copy command using the Runtime exec method:
			//TODO: should be for Linux with the help of scp
			String command = "cmd.exe /C copy " + gcFilePath.toString() + " " + lc1FilePath.toString();
			Process p = Runtime.getRuntime().exec(command);

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

	@PUT
	@Path("/{fileName}")
	public Response updateFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		File file = new File(fileDetail.getFileName());

		if (file.exists()) {

			try {
				OutputStream out = new FileOutputStream(new File(GC_PATH + fileDetail.getFileName()));
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

			String output = "File updated: " + GC_PATH + fileDetail.getFileName();
			return Response.status(200).entity(output).build();	

		}
		else
		{
			String output = "ERROR: Non-existing file cannot be updated : "+ GC_PATH + fileDetail.getFileName();
			return Response.status(500).entity(output).build();	
		}	
	}	

	@DELETE
	@Path("/{fileName}")
	public Response deleteFile(@PathParam("fileName") String fileName) {

		boolean bool = false;

		File file = new File(GC_PATH + fileName);
		bool = file.delete();

		if (bool)  {
			String output = "File deleted : "+ GC_PATH + fileName;
			return Response.status(200).entity(output).build();			
		}
		else
		{
			String output = "ERROR: File cannot be deleted : "+ GC_PATH + fileName;
			return Response.status(500).entity(output).build();	
		}
	}
}
