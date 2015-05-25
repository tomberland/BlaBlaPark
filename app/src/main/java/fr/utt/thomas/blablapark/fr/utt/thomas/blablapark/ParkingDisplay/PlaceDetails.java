package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay;

/**
 * Created by Marc on 25/05/2015.
 */
import java.io.Serializable;

import com.google.api.client.util.Key;



public class PlaceDetails implements Serializable {

    @Key
    public String status;

    @Key
    public Place result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
