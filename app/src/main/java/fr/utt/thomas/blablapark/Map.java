package fr.utt.thomas.blablapark;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends FragmentActivity implements LocationListener {

    String longitude ;
    String latitude;
    LocationManager locMgr;
    private GoogleMap gMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        gMap = mapFrag.getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        Log.e("Maps", "------EOC-------");

//        LatLng current = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//        // Move the camera instantly to hamburg with a zoom of 15.
//        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
//
//        // Zoom in, animating the camera.
//        gMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        initializeLocalization();
    }

    private void initializeLocalization() {

        Criteria criteria = new Criteria();
        String provider = locMgr.getBestProvider(criteria, true);
        Location location = locMgr.getLastKnownLocation(provider);
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        LatLng currentPosition = new LatLng(lat, lng);

        if (location != null) {
            Log.i("coucou", "Provider " + provider + " has been selected.");
            onLocationChanged(location);
            locMgr.removeUpdates(this);
        } else {
            Log.i("coucou", "location null");
            Log.i("coucou", "provider :"+provider);
 //           locMgr.requestLocationUpdates(provider, 60000, 0, this);
            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

//        Marker hamburg = gMap.addMarker(new MarkerOptions().position(currentPosition).title("Hamburg"));

        Marker kiel = gMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .title("Hamburg")
                .snippet("Hamburg is cool")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_launcher)));

        // Move the camera instantly to hamburg with a zoom of 15.
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));

        // Zoom in, animating the camera.
        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    protected void onResume() {
        super.onResume();
    }


    public void onLocationChanged(Location location) {

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        locMgr.removeUpdates(this);
        Log.i("coucou", "location : " + latitude + " " + longitude);

//        String geo = "geo:0,0?q="+latitude+","+longitude+"coucou";
//        //lance Google Maps
//        showMap(Uri.parse(geo));

        //� faire !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        locMgr.removeUpdates(this);
    }

//    //lance une application de localisation, sur notre t�l�phone il n'y a que Google Maps
//    public void showMap(Uri geoLocation) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(geoLocation);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
