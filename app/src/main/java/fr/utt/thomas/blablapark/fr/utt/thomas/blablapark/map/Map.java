package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.Localisation;
import fr.utt.thomas.blablapark.R;

public class Map extends FragmentActivity{

    private double longitude ;
    private double latitude;
    private GoogleMap gMap;
    private Localisation localisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        gMap = mapFrag.getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);

        localisation = new Localisation();

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
        h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.
    }

    public void zoom() {

        latitude = Double.parseDouble(localisation.getLatitude());
        longitude = Double.parseDouble(localisation.getLongitude());
        Log.i("coucou", "location: " + latitude + " " + longitude);

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