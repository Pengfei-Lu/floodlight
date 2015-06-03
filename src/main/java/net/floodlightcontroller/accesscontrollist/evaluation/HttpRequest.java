package net.floodlightcontroller.accesscontrollist.evaluation;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	public static String getRequest(String urlString) throws IOException {
		URL getUrl = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) getUrl
				.openConnection();
		connection.setConnectTimeout(10000);

		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		connection.getInputStream(), "utf-8"));
		String result = "";
		String line = "";
		while ((line = reader.readLine()) != null){
            result = result + line;
        }		
		reader.close();
		connection.disconnect();
		return result;
	}
	
	public static String postRequest(String urlString, String content)
			throws IOException {
		URL postUrl = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) postUrl
				.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setConnectTimeout(10000);

		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		out.writeBytes(content);
		out.flush();
		out.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "utf-8"));
		String result = "";
		String line = "";
		while ((line = reader.readLine()) != null){
            result = result + line;
        }	
		reader.close();
		connection.disconnect();
		return result;
	}


	public static String deleteRequest(String url, final String context) throws IOException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDeleteWithBody delete = new HttpDeleteWithBody(url);
		ContentProducer cp = new ContentProducer() {
			@Override
			public void writeTo(OutputStream outstream) throws IOException {
			Writer writer = new OutputStreamWriter(outstream, "UTF-8");
			writer.write(context);
			writer.flush();
			}
			};
		HttpEntity entity = new EntityTemplate(cp);
		delete.setEntity(entity);
		 
        HttpResponse response = null;
        response = httpClient.execute(delete);
        
        return EntityUtils.toString(response.getEntity());
	}
}

class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase{
	@Override
	public String getMethod() {
		return "DELETE";
	}
	
	public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}