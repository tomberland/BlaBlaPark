package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

/**
 * Created by Thomas on 12/05/2015.
 * Permet de retrouver sa voiture
 */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.Localisation;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;

public class FindCar extends Fragment {

    private double longitude ;
    private double latitude;
    MapView mMapView;
    private GoogleMap googleMap;
    private Localisation localisation;
    private ListView mDrawerListView;

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

        //affiche carte, centré sur sa voiture
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

        //récupère les coordonnées de la voiture enregistrée dans le fichier
        try {
            localisation.recupererCoordonnees(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

        LatLng oldPosition = new LatLng(latitude, longitude);

        //affiche un marker à la position de la voiture
        Marker voiture = googleMap.addMarker(new MarkerOptions()
                .position(oldPosition)
                .title("Ma voiture")
                .snippet("est ici")
                  .icon(BitmapDescriptorFactory
                          .fromResource(R.drawable.voiture)));

        // Move the camera instantly to the currentPosition with a zoom of 10.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oldPosition, 10));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);

        return rootView;
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

        //permet de revenir sur l'Accueil si l'utilisateur clique sur le bouton retour
        ((MainActivity) getActivity()).onSectionAttached(1);
        ((MainActivity) getActivity()).restoreActionBar();

        //permet au navigationDrawer de ne pas rester sur Trouver ma voiture
        mDrawerListView = (ListView) getActivity().findViewById(R.id.navList);
        mDrawerListView.setItemChecked(0, true);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}