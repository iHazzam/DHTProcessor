import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import com.sun.jersey.api.representation.Form;

import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import java.io.File;

import java.io.PrintWriter;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import org.w3c.dom.*;
import java.lang.Object;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class MyClient {
	
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

	public static void main(String[] args) throws IOException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		// get file
		ClientResponse getResponse = service.path("rest").path("files/firstfile.txt").accept(MediaType.APPLICATION_OCTET_STREAM).get(ClientResponse.class);
		InputStream is = getResponse.getEntity(InputStream.class);
		byte contentBytes[] = convertInputStreamToByteArrary(is);

		FileOutputStream stream = new FileOutputStream(new File("firstfile.txt"));
		stream.write(contentBytes);
		stream.close();

		// put file
		FormDataMultiPart form = new FormDataMultiPart();
		form.field("name", "MyClient.class");
		form.bodyPart(new FileDataBodyPart("content", new File("MyClient.class"), MediaType.APPLICATION_OCTET_STREAM_TYPE));
		ClientResponse post = service.path("rest").path("files/newFile").type(MediaType.MULTIPART_FORM_DATA).accept(MediaType.TEXT_HTML).post(ClientResponse.class, form);

		//print xml
		ClientResponse getFLResponse = service.path("rest").path("files/"). accept(MediaType.TEXT_XML).get(ClientResponse.class);
		String xml = getFLResponse.getEntity(String.class);

		try(  PrintWriter out = new PrintWriter( "xmlparse.xml" )  ){
			out.println( xml );
		}
		try {
			File file = new File("xmlparse.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			NodeList files = doc.getElementsByTagName("fileinfo");
			for (int i=0;i<files.getLength(); i++) {
				System.out.println(files.item(i).getTextContent());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println("Form response " + post.getEntity(String.class));
		}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/myapp/").build();
		}

}