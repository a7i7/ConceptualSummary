import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ConceptNetDataAccessInterface {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static String getUrl = "http://conceptnet5.media.mit.edu/data/5.4/assoc/c/en/";
	private static HttpURLConnection con = null;
	
	
	public static String getListOfAssociations(String object) throws IOException {
		
		URL obj = new URL(getUrl+object);
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} else {
			System.out.println("GET request not worked");
		}
		return null;
	}
}
