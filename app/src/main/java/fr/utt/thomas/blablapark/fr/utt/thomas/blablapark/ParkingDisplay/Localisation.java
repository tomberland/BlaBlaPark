package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay;

/**
 * Created by Thomas on 12/05/2015.
 * Permet de créer une nouvelle localisation, de la sauvegarder dans un fichier, et de la récupérer
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Localisation {

    private String latitude;
    private String longitude;
    private String nomFichier = "sauvegarde_localisation";

    public Localisation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Localisation() {
        this("0.0", "0.0");
    }

    //sauvegarde la position actuelle de la personne dans la mémoire du téléphone
    public void sauvegarder(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(nomFichier, Context.MODE_PRIVATE);
        fos.write(this.toString().getBytes());
        fos.close();
    }

    //récupère les coordonnées sauvegardées
    public void recupererCoordonnees(Context context) throws IOException {
        StringBuffer coordonneesRecuperees = new StringBuffer();
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

    //constructeurs
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

}