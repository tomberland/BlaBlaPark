package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.IOException;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;


public class FindCar extends Fragment {

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

        Marker voiture = googleMap.addMarker(new MarkerOptions()
                .position(oldPosition)
                .title("Ma voiture")
                .snippet("est ici")
 //               .icon(BitmapDescriptorFactory
 //                       .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
 //                       .fromResource(android.R.drawable.ic_menu_mylocation)));               //ce serait bien de mettre une petite icone
                  .icon(BitmapDescriptorFactory
                          .fromResource(R.drawable.voiture)));

        zoom();

        return rootView;
    }

    public void zoom() {

//        latitude = Double.parseDouble(localisation.getLatitude());
//        longitude = Double.parseDouble(localisation.getLongitude());
//        Log.i("coucou", "location: " + latitude + " " + longitude);

        LatLng currentPosition = new LatLng(latitude, longitude);

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
  //      ((MainActivity) activity).restoreActionBar();
        ((MainActivity) activity).onSectionAttached(4);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ((MainActivity) getActivity()).onSectionAttached(1);
        ((MainActivity) getActivity()).restoreActionBar();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}