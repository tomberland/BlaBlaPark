package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

/**
 * Created by Thomas on 12/05/2015.
 */

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Localisation implements LocationListener {

    private LocationManager locMgr;
    Location location;
//    private double longitudeDouble ;
//    private double latitudeDouble;
    private Localisation localisation;
    private String latitude;
    private String longitude;
    private String nomFichier = "sauvegarde_localisation";
    private StringBuffer coordonneesRecuperees;

    public Localisation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Localisation() {
        this("0.0", "0.0");
    }

    //sauvegarde l'O dans la mémoire du téléphone
    public void sauvegarder(Context context) throws IOException {
        Log.i("coucou", "location sauvegarder : " + latitude + " " + longitude);
        FileOutputStream fos = context.openFileOutput(nomFichier, Context.MODE_PRIVATE);
        fos.write(this.toString().getBytes());
        fos.close();
    }

    //récupère les coordonnées sauvegardées
    public void recupererCoordonnees(Context context) throws IOException {
        coordonneesRecuperees = new StringBuffer();
        FileInputStream fis = context.openFileInput(nomFichier);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line;

        while ((line = reader.readLine()) != null) {
            coordonneesRecuperees.append(line);
        }

        reader.close();

        // Remplacer les valeurs de latitude et de longitude
        if (coordonneesRecuperees != null) {
            String[] tab = new String[2];
            tab = coordonneesRecuperees.toString().split(" ");

            latitude = tab[0];
            longitude = tab[1];
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return latitude + " " + longitude;
    }




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_map);
//
//        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        initializeLocalization();
//    }

    public void findLocalization(Context context) {

        locMgr = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locMgr.getBestProvider(criteria, true);
        location = locMgr.getLastKnownLocation(provider);

        if (location != null) {
//            Log.i("coucou", "Provider " + provider + " has been selected.");
            onLocationChanged(location);
            locMgr.removeUpdates(this);
        } else {
//            Log.i("coucou", "location null"+", provider :"+provider);
            //           locMgr.requestLocationUpdates(provider, 60000, 0, this);
            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    public void onLocationChanged(Location location2) {

        latitude = String.valueOf(location2.getLatitude());
        longitude = String.valueOf(location2.getLongitude());

//        localisation.setLatitude(String.valueOf(latitude));
//        localisation.setLongitude(String.valueOf(longitude));

        locMgr.removeUpdates(this);
        Log.i("coucou", "location dans onLocChanged : " + latitude + " " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {
        locMgr.removeUpdates(this);
    }

 //   protected void onResume() {
//        super.onResume();
//    }
}