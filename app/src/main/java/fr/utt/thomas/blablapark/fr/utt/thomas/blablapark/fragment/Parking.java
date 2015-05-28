package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GooglePlaces;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.Place;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.PlaceMapActivity;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.PlacesList;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase.MessageDB;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Parking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Parking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Parking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Localisation localisation;
    double longitude;
    double latitude;
    MapView mMapView;
    GoogleMap googleMap;
    ProgressDialog pDialog;
    GooglePlaces googlePlaces;
    PlacesList nearPlaces;
    LatLng latLng;
    SharedPreferences sharedPreferences;
    double radius;


    private OnFragmentInteractionListener mListener;

    public static Parking newInstance() {
        Parking fragment = new Parking();
        return fragment;
    }

    public Parking() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parking, container,
                false);

        Intent intent = new Intent(getActivity(), PlaceMapActivity.class);
        startActivity(intent);

//        mMapView = (MapView) rootView.findViewById(R.id.map);
//        mMapView.onCreate(savedInstanceState);
//        mMapView.onResume();// needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        googleMap = mMapView.getMap();
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setCompassEnabled(true);
//
//        Marker parking1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2973451, 4.0744009000000005))
//                .title("Parking1")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//        Marker parking2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.295699762561306, 4.06818151473999))
//                .title("Parking2")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//        localisation = new Localisation();
//
//        //cherche sa position gps
//        localisation.findLocalization(getActivity());
//
//        new LoadPlaces().execute();
//
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                zoom();
//            }
//        };
//
//        Handler h = new Handler();
//        h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.

        return rootView;
    }

    public void zoom() {

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());
        Log.i("coucou", "location: " + latitude + " " + longitude);

        LatLng currentPosition = new LatLng(latitude, longitude);

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
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


    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
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
         */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {

                String types = "parking"; // Listing places only cafes, restaurants

                // Radius in meters - increase this value if you don't find any places
 //               double radius = 5000; // 1000 meters

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                radius = Double.valueOf(sharedPreferences.getString("Perimetre", "5"));
                Log.i("coucou", "radius : "+radius);

                // get nearest places
                nearPlaces = googlePlaces.search(latitude,
                        longitude, radius, types);


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
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            // Get json response status
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
                MessageDB.message(getActivity(), "Sorry no places found. Try to change the types of places");
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

        }

    }





