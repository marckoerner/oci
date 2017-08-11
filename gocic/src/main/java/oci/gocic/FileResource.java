package oci.gocic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

//import oci.gocic.messenger.model.File;
//import oci.gocic.messenger.service.FileService;

@Path("/files")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.MULTIPART_FORM_DATA)
public class FileResource {

	//	private FileService fileService = new FileService();	

	private static final String FILE_PATH = "C:\\test\\";
	private static final String UPLOAD_PATH = "C://upload//";

	//	@GET
	//	public List<File> getFiles() {
	//		return fileService.getAllFiles();
	//	}

	@GET
	@Path("/{fileName}")
	public Response getFile(@PathParam("fileName") String fileName) {

		File file = new File(FILE_PATH + fileName);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
				"attachment; filename=image_from_server.png");
		return response.build();
	}

	@POST
	public Response addFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {		

		try {
			OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileDetail.getFileName()));
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

		String output = "File uploaded to : " + UPLOAD_PATH + fileDetail.getFileName();
		return Response.status(200).entity(output).build();
	}

	@PUT
	@Path("/{fileName}")
	public Response updateFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		File file = new File(fileDetail.getFileName());
		
		if (file.exists()) {
			
			try {
				OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileDetail.getFileName()));
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
			
			String output = "File updated: " + UPLOAD_PATH + fileDetail.getFileName();
			return Response.status(200).entity(output).build();	
			
		}
		else
		{
			String output = "ERROR: Non-existing file cannot be updated : "+ UPLOAD_PATH + fileDetail.getFileName();
			return Response.status(500).entity(output).build();	
		}	
	}	

	@DELETE
	@Path("/{fileName}")
	public Response deleteFile(@PathParam("fileName") String fileName) {
		//		fileService.removeFile(fileName);

		boolean bool = false;

		File file = new File(UPLOAD_PATH + fileName);
		bool = file.delete();

		if (bool)  {
			String output = "File deleted : "+ UPLOAD_PATH + fileName;
			return Response.status(200).entity(output).build();			
		}
		else
		{
			String output = "ERROR: File cannot be deleted : "+ UPLOAD_PATH + fileName;
			return Response.status(500).entity(output).build();	
		}
	}
}
