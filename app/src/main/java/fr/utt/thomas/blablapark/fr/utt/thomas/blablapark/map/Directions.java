package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.map;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.maps.android.PolyUtil;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 19/05/2015.
 */
public class Directions extends AsyncTask<URL, Integer, String> {


    static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private List<LatLng> latLngs = new ArrayList<LatLng>();

    protected String doInBackground(URL... urls) {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

            GenericUrl url = new GenericUrl("http://maps.googleapis.com/maps/api/directions/json");
            url.put("origin", "Chicago,IL");
            url.put("destination", "Los Angeles,CA");
            url.put("sensor",false);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            DirectionsResult directionsResult = httpResponse.parseAs(DirectionsResult.class);
            String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
            latLngs = PolyUtil.decode(encodedPoints);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
 //       clearMarkers();
//        addMarkersToMap(latLngs);
    }

    public static class DirectionsResult {
        @Key("routes")
        public List<Route> routes;
    }

    public static class Route {
        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyLine;
    }

    public static class OverviewPolyLine {
        @Key("points")
        public String points;
    }
}

