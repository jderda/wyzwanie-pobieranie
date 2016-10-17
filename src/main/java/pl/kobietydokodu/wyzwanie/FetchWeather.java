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

public class FetchWeather {
    
    Logger log = LoggerFactory.getLogger(FetchWeather.class);

    public String getData() throws URISyntaxException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        URIBuilder requestUri = new URIBuilder("https://query.yahooapis.com/v1/public/yql");
        requestUri.addParameter("format", "json");
        requestUri.addParameter("q", "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"wrocław\")");
        HttpGet httpGet = new HttpGet(requestUri.build());
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            entity.getContent();
            
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new InputStreamReader(entity.getContent()));

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject queryObject = (JSONObject) jsonObject.get("query");
            JSONObject resultsObject = (JSONObject) queryObject.get("results");
            JSONObject channelObject = (JSONObject) resultsObject.get("channel");
            JSONObject itemObject = (JSONObject) channelObject.get("item");
            JSONArray forecastArray = (JSONArray) itemObject.get("forecast");
            JSONObject forecastObject = (JSONObject) forecastArray.get(0);
            
            String description = (String) forecastObject.get("text");
            String date = (String) forecastObject.get("date");
            
            EntityUtils.consume(entity);

            return date + ": " + description;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
