package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

/**
 * Created by Marc on 12/05/2015.
 * Gère le fragment d'accueil
 */

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

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GooglePlaces;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.Place;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.PlacesList;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase.MessageDB;

public class Home extends Fragment {

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sharedPreferences;
    private SeekBar seekBar;
    private TextView textView;
    private GoogleMap googleMap;
    MapView mMapView;
    int radius;
    GPSTracker gps;
    PlacesList nearPlaces;
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

        //affiche carte, centré sur soi, sans parking
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //creating GPS Class object
        gps = new GPSTracker(getActivity());

        //récupère et crée notre position actuelle
        LatLng currentPosition = new LatLng(gps.getLatitude(), gps.getLongitude());

        googleMap = mMapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.voiture)));
        googleMap.getUiSettings().setCompassEnabled(true);

        //appelle une fonction définit par la suite. Elle cherche les places aux alentours
        new LoadPlaces().execute();

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);

        //récupère le "radius" : périmètre de recherche définit par l'utilisateur
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "50")); //50 : valeur par défaut

        //définit la barre permettant de choisir le périmètre
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setProgress(radius / 100); //valeur enregistré en km
        textView = (TextView) rootView.findViewById(R.id.textView1);
        //affiche l'ancienne valeur, enregistrée lors de la précédent
        textView.setText(radius + " m");

        //listener sur la seekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //progress : valeur sélectionnée
            int progress = radius;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
                textView.setText(progress * 100 + " m"); //*100 pour avoir la valeur en mètre
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //enregistre la valeur final du périmètre dans les préférences de l'application
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //valeur à enregistrer
                String tmp = String.valueOf(progress * 100);
                editor.putString("Perimetre", tmp); // value to store
                editor.commit();

                //efface la carte pour ne plus afficher les marqueurs plus dans le périmètre
                googleMap.clear();

                //appelle une fonction définit par la suite. Elle cherche les places aux alentours
                new LoadPlaces().execute();
            }
        });

        //gère le bouton central. Il permet d'affichcer les places communautaires
        ((ImageButton) rootView.findViewById(R.id.searchButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //met à jour la valeur
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "50"));

                float [] dist = new float[1];

              /**
                * positions communautaires créées en dur ici
                */

                //calcul la distance entre notre position actuelle et une place
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.2973451, 4.0744009000000005, dist);

                //crée un marker (point qui sera afficher sur la carte)
                Marker parking1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2973451, 4.0744009000000005))
                        .title("Nicolas S")
                        .snippet("Il y a 1 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                //si la place se situent dans le périmètre, on l'affiche
                if (dist[0]>radius){
                    parking1.setVisible(false);
                }else{
                    parking1.setVisible(true);
                }

                //place 2
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.295699762561306, 4.06818151473999, dist);
                Marker parking2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.295699762561306, 4.06818151473999))
                        .title("Ismail Y")
                        .snippet("Il y a 15 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (dist[0]>radius){
                    parking2.setVisible(false);
                }else{
                    parking2.setVisible(true);
                }

                //place 3
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.270447303657924, 4.065794348716736, dist);
                Marker parking3 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.270447303657924, 4.065794348716736))
                        .title("Marc S")
                        .snippet("Il y a 2 heures")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (dist[0]>radius){
                    parking3.setVisible(false);
                }else{
                    parking3.setVisible(true);
                }

                //place 4
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.26921184758687, 4.064174294471741, dist);
                Marker parking4 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.26921184758687, 4.064174294471741))
                        .title("Thomas B")
                        .snippet("Il y a 5 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (dist[0]>radius){
                    parking4.setVisible(false);
                }else{
                    parking4.setVisible(true);
                }

                //place 5
                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), 48.282736, 4.071035, dist);
                Marker parking5 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.282736, 4.071035))
                        .title("Nadia G")
                        .snippet("Il y a 35 minutes")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (dist[0]>radius){
                    parking5.setVisible(false);
                }else{
                    parking5.setVisible(true);
                }
            }
        });
        return rootView;
    }

    //cherche les places de parking aux alentours, grâce à l'api GooglePlaces
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

                //récupère le périmètre définit par l'utilisateur grâce à la seekBar
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "5"));

                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),gps.getLongitude(),radius,types);


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