package pl.kobietydokodu.wyzwanie;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchWikipedia {

    Logger log = LoggerFactory.getLogger(FetchWeather.class);

    public String fetchData() throws URISyntaxException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        URIBuilder requestUri = new URIBuilder("https://en.wikipedia.org/w/api.php");
        requestUri.addParameter("action", "query");
        requestUri.addParameter("format", "json");
        requestUri.addParameter("prop", "revisions");
        requestUri.addParameter("titles", "Main Page");
        HttpGet httpGet = new HttpGet(requestUri.build());
        CloseableHttpResponse response1 = null;
        try {
            response1 = httpclient.execute(httpGet);
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            entity1.getContent();

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new InputStreamReader(entity1.getContent()));

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject queryObject = (JSONObject) jsonObject.get("query");
            JSONObject pagesObject = (JSONObject) queryObject.get("pages");
            Object key = pagesObject.keySet().iterator().next();
            JSONObject pageObject = (JSONObject) pagesObject.get(key);
            JSONArray revisionsArray = (JSONArray) pageObject.get("revisions");
            JSONObject revisionObject = (JSONObject) revisionsArray.get(0);

            String description = (String) revisionObject.get("comment");
            String date = (String) revisionObject.get("timestamp");

            EntityUtils.consume(entity1);
            return date + ": " + description;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                response1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
