package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;

public class SaveCar extends Fragment {

    private Localisation localisation;
    GPSTracker gps;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static SaveCar newInstance() {
        SaveCar fragment = new SaveCar();
        return fragment;
    }

    public SaveCar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_car, container, false);

        //   creating GPS Class object
        gps = new GPSTracker(getActivity());

        localisation = new Localisation();
        localisation.setLatitude(Double.toString(gps.getLatitude()));
        localisation.setLongitude(Double.toString(gps.getLongitude()));

        try {
            localisation.sauvegarder(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        ((MainActivity) activity).onSectionAttached(3);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
