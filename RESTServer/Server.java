/*
One instance of this class is created for each incoming HTTP request (which maps to exactly one REST-annotated function of the class)
   - that instance is discarded at the end of the HTTP request
   - note that instance variables are therefore of no use because instances only exist for the duration of a single function call
   - any state must therefore be in "static" class variables; this matches the REST philosophy of "no per-client state at the server"
*/

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.MediaType;

import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.core.header.FormDataContentDisposition;

import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//everything on /base/files/ will hit this class
@Path("/files")
public class Server {
	
	private static final String PREFIX = "/var/lib/tomcat8/webapps/myapp/files/";
	
	//anything on /base/files/x (where x is anything) will hit this method, with "x" being provided as the "filename" parameter
	// (the actual name of the below method is irrelevant)
	@GET
	@Path("/{param}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getFile(@PathParam("param") String filename) {
		
		//by default files are looked for in Tomcat's root directory, so we prepend our subdirectory on there first...
		filename = PREFIX + filename;
		
		//read filename into a byte array & send to client
		File f = new File(filename);
		
		if (f.exists()) {
			return Response.status(Response.Status.OK).entity(f).build();
			}
			else
			return Response.status(Response.Status.NOT_FOUND).entity("").build();
		}
	
	public static byte[] convertInputStreamToByteArrary(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final int BUF_SIZE = 1024;
		byte[] buffer = new byte[BUF_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) > -1) {
			out.write(buffer, 0, bytesRead);
			}
		in.close();
		byte[] byteArray = out.toByteArray();
		return byteArray;
		}
	
	//this method gets called when a POST is sent to /base/files/newFile
	// - it expects multipart form data as the body of the POST request
	// - the fields of the form are mapped to the parameters of the method as specified below (i.e. FormParam() identifies a particular form field, the value of which goes into the method parameter)
	// - NOTE: the parameter "@FormDataParam("content") FormDataContentDisposition fileDetail" is optional, and can be used to later do e.g. fileDetail.getFileName()...
	@POST
	@Path("/newFile")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response newFile(@FormDataParam("name") String filename, @FormDataParam("content") InputStream contentStream, @FormDataParam("content") FormDataContentDisposition fileDetail) throws IOException {
		
		//by default files are looked for in Tomcat's root directory, so we prepend our subdirectory on there first...
		filename = PREFIX + filename;
		
		byte contentBytes[] = convertInputStreamToByteArrary(contentStream);
		FileOutputStream stream = new FileOutputStream(new File(filename));
		stream.write(contentBytes);
		stream.close();
		
		return Response.status(Response.Status.OK).build();
		}
	// GET all the files as HTML on browser
	@GET
	@Path("")
	@Produces({ MediaType.TEXT_HTML })
	public Response listFiles() {
		String html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Resources On Server</title></head><body><ul>";

		File folder = new File(PREFIX);
		File[] listOfFiles = folder.listFiles();
		System.out.println(listOfFiles.length);
		for (int i = 0; i < listOfFiles.length; i++) {
			String addToHtml = "";
			if (listOfFiles[i].isFile()) {
				addToHtml = "<li>(File) - <a href='http://localhost:8080/myapp/rest/files/" + listOfFiles[i].getName() + "'>" + listOfFiles[i].getName() + "</a></li>";
			} else if (listOfFiles[i].isDirectory()) {
				addToHtml =  "<li>(Directory) " + listOfFiles[i].getName() + "  </li>";
			}
			html = html + addToHtml;
		}
		html = html + "</ul></body></html>";



		return Response.status(Response.Status.OK).entity(html).build();
	}
	//GET the files as XML
	@GET
	@Path("")
	@Produces({ MediaType.TEXT_XML })
	public Response listFilesXML(@PathParam("param") String filename) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><files>";

		File folder = new File(PREFIX);
		File[] listOfFiles = folder.listFiles();
		System.out.println(listOfFiles.length);
		for (int i = 0; i < listOfFiles.length; i++) {
			String addToXml = "";
			if (listOfFiles[i].isFile()) {
				addToXml = "<fileinfo>(File) - " + listOfFiles[i].getName() +  "</fileinfo>";
			} else if (listOfFiles[i].isDirectory()) {
				addToXml = "<fileinfo>(Directory) - " + listOfFiles[i].getName() + "</fileinfo>";
			}
			xml = xml + addToXml;
		}
		xml = xml + "</files>";


		return Response.status(Response.Status.OK).entity(xml).build();
	}

	@GET
	@Path("/{param}/zip")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getZipFile(@PathParam("param") String filename) {

		return null;
	}

}