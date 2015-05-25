package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marc on 25/05/2015.
 */
public class PlacesList implements Serializable {

    @Key
    public String status;

    @Key
    public List<Place> results;

}
