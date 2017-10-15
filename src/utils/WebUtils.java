package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebUtils {

	public static String pageHtml = "";


	private final static String USER_AGENT = "Mozilla/5.0";
	
	private final static String apiKey = "fcdddfcc2be3b398be4e1c3dc6ec3ba7";

	public static String getRecentTracks(String username) throws Exception {

		String url = "https://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + username + "&api_key=" + apiKey + "&format=json";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return (response.toString());
	}


	public static void pullHTML(String urlString) {
		pageHtml = "";
		try {
			System.out.println("connecting to:");
			System.out.println(urlString);
			// get URL content
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				pageHtml = pageHtml + (inputLine) + "\n";
			}

			br.close();
		} catch (MalformedURLException e) {
		} catch (IOException e){
		}
	}
	
	public static String getSongInfo(String artist, String track) throws Exception{
		String url = "https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + apiKey + "&artist=" + artist + "&track=" + track + "&format=json";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return (response.toString());
	}

}
