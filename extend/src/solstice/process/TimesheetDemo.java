package solstice.process;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class TimesheetDemo {
	public static void main(String[] args) {
		URL url;
		HttpURLConnection request = null;
		String method = "GET";
		String parameters = "";
		try {
			/*
			 * Set the URL and Parameters to create connection Set Request Method (GET, POST
			 * or DELETE)
			 */
			if ("GET".equals(method)) {
				parameters = "&users_list=all&view_type=week&date=05-11-2014&bill_status=All&component_type=general";
				url = new URL("https://projectsapi.zoho.com/restapi/portal/851698770/projects/1/logs/?"
						+ parameters);
				request = (HttpURLConnection) url.openConnection();
				request.setRequestMethod("GET");
			} else if ("POST".equals(method)) {
				parameters = "&name=Registration_Document_33A&date=05-14-2014&bill_status=Billable&hours=02:20";
				url = new URL("https://projectsapi.zoho.com/restapi/portal/851698770/projects/1/logs/?"
						+ parameters);
				request = (HttpURLConnection) url.openConnection();
				request.setRequestMethod("POST");
			} else if ("DELETE".equals(method)) {
				url = new URL(
						"https://projectsapi.zoho.com/restapi/portal/851698770/projects/1/logs/[LOGID]/?");
				request = (HttpURLConnection) url.openConnection();
				request.setRequestMethod("DELETE");
			}
			// add request header
			request.setRequestProperty("Accept", "application/json");
			request.setRequestProperty("Authorization", "Zoho-oauthtoken 1000.118e66179a123c88d3a87ff72c7c3784.1e35d4857de2727f6d49ffd26d7b60bf");
			request.setDoOutput(true);
			request.setDoInput(true);
			request.connect();
			// Get Response
			BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = bf.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			bf.close();
			// Response HTTP Status Code
			System.out.println("Response HTTP Status Code : " + request.getResponseCode());
			// Response Body
			System.out.println("Response Body : " + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				request.disconnect();
			}
		}
	}
}
