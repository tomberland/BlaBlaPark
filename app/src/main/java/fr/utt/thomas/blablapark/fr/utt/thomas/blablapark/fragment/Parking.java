package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;


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
//    private String latitude;
//    private String longitude;
    private double longitude ;
    private double latitude;
    MapView mMapView;
    private GoogleMap googleMap;

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

        Marker parking1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.2973451, 4.0744009000000005))
                .title("Parking1")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Marker parking2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.295699762561306, 4.06818151473999))
                .title("Parking2")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

//                LatLng oldPosition = new LatLng(48.2973451, 4.0744009000000005);
//                Marker voiture = googleMap.addMarker(new MarkerOptions()
//                        .position(oldPosition)
//                        .title("Ma voiture")
//                        .snippet("est ici")
//                        .icon(BitmapDescriptorFactory
//                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                //                .fromResource(android.R.drawable.ic_menu_mylocation)));
//                //                .icon(BitmapDescriptorFactory
//                //                        .fromResource(R.drawable.ic_launcher)));

                localisation = new Localisation();

                //cherche sa position gps
                localisation.findLocalization(getActivity());

                //attend un peu sinon a pas encore trouv� location
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        zoom();
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.

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

}
