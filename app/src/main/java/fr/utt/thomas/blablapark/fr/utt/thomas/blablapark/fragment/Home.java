package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Localisation localisation;
//    private String latitude;
//    private String longitude;
    private double longitude ;
    private double latitude;
    MapView mMapView;
    private GoogleMap googleMap;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SeekBar seekBar;
    private TextView textView;
    int radius;
//    GPSTracker gps;

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

        localisation = new Localisation();

        //cherche sa position gps
        localisation.findLocalization(getActivity());

//        latitude = Double.parseDouble(localisation.getLatitude());
//        longitude = Double.parseDouble(localisation.getLongitude());

//        // creating GPS Class object
//        gps = new GPSTracker(getActivity());
//
//        // check if GPS location can get
//        if (gps.canGetLocation()) {
//            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
//        } else {
//            Log.i("coucou", "couldnt get location");
//        }

        //attend un peu sinon a pas encore trouvé location
        Runnable r = new Runnable() {
            @Override
            public void run(){
                zoom();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.

        //Bouton central
        ((ImageButton) rootView.findViewById(R.id.searchButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mMapView = (MapView) rootView.findViewById(R.id.map);
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

                Marker parking1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2973451, 4.0744009000000005)).title("Parking1"));
                Marker parking2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.295699762561306, 4.06818151473999)).title("Parking2"));
                Marker parking3 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.5, 4.0744009000000005)).title("Parking1"));
                Marker parking4 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2, 4.1)).title("Parking2"));

//                Intent intent = new Intent(getActivity(), PlaceMapActivity.class);
//                startActivity(intent);
            }
        });

        ((ImageButton) rootView.findViewById(R.id.indicate)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertCommunityPopUp();
            }
        });

        //Bouton pour enregistrer la position de sa voiture
        ((ImageButton) rootView.findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //pas Toast basique, on agrandit le texte ici
                Toast toast = Toast.makeText(getActivity(), "Votre position actuelle a été enregistrée. Il vous suffira d'aller dans le menu \"Trouver ma voiture\" pour la retrouver.", Toast.LENGTH_LONG);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(20);
                toast.show();

                localisation = new Localisation();

                //cherche sa position gps
                localisation.findLocalization(getActivity());

                //attend un peu sinon a pas encore trouvé location
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
//                        latitude = localisation.getLatitude();
//                        longitude = localisation.getLongitude();
                        latitude = Double.parseDouble(localisation.getLatitude());
                        longitude = Double.parseDouble(localisation.getLongitude());
                        try {
//                            localisation.setLatitude(latitude);
//                            localisation.setLongitude(longitude);
                            localisation.setLatitude(Double.toString(latitude));
                            localisation.setLongitude(Double.toString(longitude));
                            localisation.sauvegarder(getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.
            }
        });



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        radius = Integer.valueOf(sharedPreferences.getString("Perimetre", "50"));

        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setProgress(radius);
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


            }
        });
        return rootView;
    }

    public void zoom() {

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

//        latitude = gps.getLatitude();
//        longitude = gps.getLongitude();

        Log.i("coucou", "location dans zoom : " + latitude + " " + longitude);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private void alertCommunityPopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("Alert empty place to community");
        helpBuilder.setMessage("Thank you for alerting the empty place, you earned 5exp ");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }


}
