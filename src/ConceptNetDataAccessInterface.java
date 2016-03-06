import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;

public class ConceptNetDataAccessInterface {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static String getUrl = "http://conceptnet5.media.mit.edu/data/5.4/assoc/c/en/";
	private static HttpURLConnection con = null;
	
	public static List<String> getListOfAssociations(String object) throws IOException
	{
		return getListOfAssociations(object,-1);
	}
	public static List<String> getListOfAssociations(String object,int limit) throws IOException {
		
		final String ENGLISH_LANGUAGE = "en";
		
		String conceptNetResponse = null;
		URL obj = new URL(getUrl+object+((limit==-1)?"":"?limit="+limit));
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();

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
			conceptNetResponse = response.toString();
		}
		else
		{
			System.out.println("GET request not worked");
		}
		
		JSONObject conceptNetResponseJSONObject = new JSONObject(conceptNetResponse);
		JSONArray arrayOfSimilarConcepts = (JSONArray) conceptNetResponseJSONObject.get("similar");
		List<String> similarConcepts = new ArrayList<String>();
		
		for(int i=0;i<arrayOfSimilarConcepts.length();i++)
		{
			JSONArray data = (JSONArray) arrayOfSimilarConcepts.get(i);
			String [] word = data.getString(0).split("/");
			double associationScore = data.getDouble(1);	//not using this for now
			String language = word[2];
			String similarWord = word[3];
			if(language.equals(ENGLISH_LANGUAGE) && !similarWord.contains("_"))
				similarConcepts.add(similarWord);
		}
		
		return similarConcepts;
	}
}
