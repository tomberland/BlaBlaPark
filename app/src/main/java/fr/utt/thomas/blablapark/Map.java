package fr.utt.thomas.blablapark;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Map extends Activity implements LocationListener {

    MapView map;
    TextView longitude ;
    TextView latitude;
    MyLocationNewOverlay myLocation;
    LocationManager locMgr;
    MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        longitude = (TextView) findViewById(R.id.textView3);
        latitude = (TextView) findViewById(R.id.textView2);

        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        initializeMap();
        initializeLocalization();

        String geo = "geo:0,0?q="+latitude+","+longitude+"("+nomLieu+")";
        //lance Google Maps
        showMap(Uri.parse(geo));
    }

    private void initializeLocalization() {

        Criteria criteria = new Criteria();
        String provider = locMgr.getBestProvider(criteria, true);
        Location location = locMgr.getLastKnownLocation(provider);
        Boolean pb = locMgr.isProviderEnabled(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            locMgr.removeUpdates(this);
        } else {
            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
//test
//            latitude.setText("Location not available");
//            longitude.setText(pb.toString());
        }
    }

    public void initializeMap(){
        map = (MapView) findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        GeoPoint startPoint = new GeoPoint(541541, 9735936);
        mapController = (MapController) map.getController();
        mapController.setCenter(startPoint);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(10);
        map.setUseDataConnection(true);

    }

    protected void onResume() {
        super.onResume();
    }


    public void onLocationChanged(Location location) {
        //       int lat = (int) (location.getLatitude());
        //       int lng = (int) (location.getLongitude());
        //       latitude.setText(String.valueOf(lat));
        //       longitude.setText(String.valueOf(lng));

//        this.location = location;
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        System.out.println("provider");
        System.out.println("lat");
        System.out.println("lng");
        latitude.setText(lat);
        longitude.setText(lng+" ");
        locMgr.removeUpdates(this);

        GeoPoint startPoint2 = new GeoPoint((int)(location.getLatitude() * 1E6),(int)(location.getLongitude() * 1E6) );//Integer.parseInt(lng.toString()) * 1E6
        mapController.setCenter(startPoint2);
        mapController.setZoom(20);
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

    //lance une application de localisation, sur notre téléphone il n'y a que Google Maps
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

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
