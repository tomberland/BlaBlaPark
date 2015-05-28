package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase.MessageDB;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator.InternetVerificator;

public class PlaceMapActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    // Nearest places
    PlacesList nearPlaces;
    ArrayList<String> listLatLng = new ArrayList<String>();
    ProgressDialog pDialog;
    GooglePlaces googlePlaces;
    GPSTracker gps;
    Boolean isInternetPresent = false;
    InternetVerificator cd;
    LatLng currentPosition;
    SharedPreferences sharedPreferences;
    double radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);

        cd = new InternetVerificator(getApplicationContext());

        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            MessageDB.message(getApplicationContext(), "Internet Connection Error");
            // stop executing code by return
            return;
        }

        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            MessageDB.message(getApplicationContext(), "couldnt get location");
            // stop executing code by return
            return;
        }





        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        googleMap = mapFrag.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        new LoadPlaces().execute();

        currentPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
        Log.i("coucou", "Location : "+gps.getLatitude()+" "+ gps.getLongitude());
//        Runnable r = new Runnable() {

        //     public void run(){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
        //           }
        //      };

        Marker pos = googleMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .title("Im here")
                .snippet("est ici")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));



        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }



    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlaceMapActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                //
                String types = "parking"; // Listing places only cafes, restaurants



                // Radius in meters - increase this value if you don't find any places
//                double radius = 10000; // 1000 meters

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                radius = Double.valueOf(sharedPreferences.getString("Perimetre", "5"));
                Log.i("coucou", "radius : "+radius);



                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                                Double lat = p.geometry.location.lat;
                                Double lng = p.geometry.location.lng;
                                LatLng latLng = new LatLng(lat,lng);

                                Marker pos = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(p.name)
                                        .snippet(p.vicinity)
                                        .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            }
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        MessageDB.message(getApplicationContext(), "Sorry no places found. Try to change the types of places");
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        MessageDB.message(getApplicationContext(), "Sorry unknown error occured.");
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        MessageDB.message(getApplicationContext(),  "Sorry query limit to google places is reached");
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        MessageDB.message(getApplicationContext(), "Sorry error occured. Request is denied");
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        MessageDB.message(getApplicationContext(), "Sorry error occured. Invalid Request");
                    }
                    else
                    {
                        MessageDB.message(getApplicationContext(), "Sorry error occured.");
                    }
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }



}
