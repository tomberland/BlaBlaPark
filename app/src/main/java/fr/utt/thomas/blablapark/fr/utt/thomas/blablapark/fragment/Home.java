package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GooglePlaces;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.Place;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.PlacesList;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase.MessageDB;

public class Home extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Localisation localisation;
//    private String latitude;
//    private String longitude;
    private double longitude ;
    private double latitude;
    MapView mMapView;
    private GoogleMap googleMap;
    private SharedPreferences sharedPreferences;
    private SeekBar seekBar;
    private TextView textView;
    int radius;
    GPSTracker gps;
    float [] dist;
    Marker parking1, parking2, parking3, parking4;

    // Nearest places
    PlacesList nearPlaces;
    ArrayList<String> listLatLng = new ArrayList<String>();
    ProgressDialog pDialog;
    GooglePlaces googlePlaces;

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //affiche carte en fond (centré sur soi, sans parking)
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        //   creating GPS Class object
        gps = new GPSTracker(getActivity());

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.i("coucou", "Dans onCreateView de Home : latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            Log.i("coucou", "couldnt get location");
        }

        new LoadPlaces().execute();

        zoom();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "50"));

        Log.i("coucou", "radius : " + radius);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setProgress(radius / 100);
        textView = (TextView) rootView.findViewById(R.id.textView1);
        textView.setText(radius + " m");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = radius;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
                Log.i("coucou", "progress : " + progress);
                textView.setText(progress * 100 + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String tmp = String.valueOf(progress * 100);
                Log.i("coucou", "progress : " + progress * 100 + " tmp : " + tmp);
                editor.putString("Perimetre", tmp); // value to store
                editor.commit();

                googleMap.clear();
                new LoadPlaces().execute();
            }
        });

        //Bouton central
        ((ImageButton) rootView.findViewById(R.id.searchButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //met à jour la valeur
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "50"));

                dist = new float[1];
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.2973451, 4.0744009000000005, dist);
                parking1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2973451, 4.0744009000000005))
                        .title("Nicolas S")
                        .snippet("Il y a 1 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                Log.i("coucou", "dist[0] : " + dist[0]);
                if (dist[0]>radius){
                    parking1.setVisible(false);
                }else{
                    parking1.setVisible(true);
                }

                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.295699762561306, 4.06818151473999, dist);
                parking2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.295699762561306, 4.06818151473999))
                        .title("Ismail Y")
                        .snippet("Il y a 15 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                Log.i("coucou", "dist[0] : " + dist[0]);
                if (dist[0]>radius){
                    parking2.setVisible(false);
                }else{
                    parking2.setVisible(true);
                }

                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.270447303657924, 4.065794348716736, dist);
                parking3 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.270447303657924, 4.065794348716736))
                        .title("Marc S")
                        .snippet("Il y a 2 heures")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                Log.i("coucou", "dist[0] : " + dist[0]);
                if (dist[0]>radius){
                    parking3.setVisible(false);
                }else{
                    parking3.setVisible(true);
                }

                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.26921184758687, 4.064174294471741, dist);
                parking4 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.26921184758687, 4.064174294471741))
                        .title("Thomas B")
                        .snippet("Il y a 5 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                Log.i("coucou", "dist[0] : " + dist[0]);
                if (dist[0]>radius){
                    parking4.setVisible(false);
                }else{
                    parking4.setVisible(true);
                }
            }
        });
        return rootView;
    }

    public void zoom() {

        Log.i("coucou", "Dans zoom de Home : latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        LatLng currentPosition = new LatLng(gps.getLatitude(), gps.getLongitude());

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "5"));
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
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed Places into LISTVIEW
//                     * */
//                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if (status.equals("OK")) {
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                                Double lat = p.geometry.location.lat;
                                Double lng = p.geometry.location.lng;
                                LatLng latLng = new LatLng(lat, lng);

                                Marker pos = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(p.name)
                                        .snippet(p.vicinity)
                                        .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            }
                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
                        MessageDB.message(getActivity(), "Sorry no places found. Try to change the search area");
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        MessageDB.message(getActivity(), "Sorry unknown error occured.");
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        MessageDB.message(getActivity(), "Sorry query limit to google places is reached");
                    } else if (status.equals("REQUEST_DENIED")) {
                        MessageDB.message(getActivity(), "Sorry error occured. Request is denied");
                    } else if (status.equals("INVALID_REQUEST")) {
                        MessageDB.message(getActivity(), "Sorry error occured. Invalid Request");
                    } else {
                        MessageDB.message(getActivity(), "Sorry error occured.");
                    }
                }
 //           });
//        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
