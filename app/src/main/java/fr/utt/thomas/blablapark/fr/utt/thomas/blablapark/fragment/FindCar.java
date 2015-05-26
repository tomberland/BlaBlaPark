package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import java.io.IOException;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindCar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindCar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindCar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocationManager locMgr;
    private double longitude ;
    private double latitude;

    MapView mMapView;
    private GoogleMap googleMap;
    private Localisation localisation;

    private OnFragmentInteractionListener mListener;

    public static FindCar newInstance() {
        FindCar fragment = new FindCar();
        return fragment;
    }

    public FindCar() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_car, container, false);

//            Intent intentMap = new Intent(getActivity(), MapFindCar.class);
//            getActivity().startActivity(intentMap);

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

        try {
            localisation.recupererCoordonnees(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

        LatLng oldPosition = new LatLng(latitude, longitude);

    //        Marker hamburg = gMap.addMarker(new MarkerOptions().position(currentPosition).title("Hamburg"));

        Marker voiture = googleMap.addMarker(new MarkerOptions()
                .position(oldPosition)
                .title("Ma voiture")
                .snippet("est ici")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //                .fromResource(android.R.drawable.ic_menu_mylocation)));
        //                .icon(BitmapDescriptorFactory
        //                        .fromResource(R.drawable.ic_launcher)));

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
        h.postDelayed(r, 4000); // <-- the "4000" is the delay time in miliseconds.

        // Perform any camera updates here
        return rootView;
    }

    public void zoom() {

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

        LatLng currentPosition = new LatLng(latitude, longitude);

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(4);
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
