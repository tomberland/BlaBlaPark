package fr.utt.thomas.blablapark;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapFindCar extends FragmentActivity {

    private LocationManager locMgr;
    Location location;
    private double longitude ;
    private double latitude;
    private GoogleMap gMap;
    private Localisation localisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_find_car);

        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        gMap = mapFrag.getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);

        //récupère les anciennes coordonnées et les affiche
        localisation = new Localisation();

        try {
            localisation.recupererCoordonnees(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

        LatLng oldPosition = new LatLng(latitude, longitude);

//        Marker hamburg = gMap.addMarker(new MarkerOptions().position(currentPosition).title("Hamburg"));

        Marker voiture = gMap.addMarker(new MarkerOptions()
                .position(oldPosition)
                .title("Ma voiture")
                .snippet("est ici")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
        //                .icon(BitmapDescriptorFactory
        //                        .fromResource(R.drawable.ic_launcher)));

        //cherche sa position gps
        localisation.findLocalization(this);

        //attend un peu sinon a pas encore trouvé location
        Runnable r = new Runnable() {
            @Override
            public void run(){
                zoom();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 4000); // <-- the "1000" is the delay time in miliseconds.
    }

    public void zoom() {

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());

        LatLng currentPosition = new LatLng(latitude, longitude);

        // Move the camera instantly to the currentPosition with a zoom of 10.
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

        // Zoom in, animating the camera.
        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    protected void onResume() {
    super.onResume();
}

}
