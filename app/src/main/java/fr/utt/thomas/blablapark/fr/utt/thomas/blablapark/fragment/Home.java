package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.map.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Localisation localisation;
    private String latitude;
    private String longitude;

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,
                false);

        ((ImageButton) rootView.findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //lance l'activit� NouvellePersonneActivity

 //               ((MainActivity) getActivity()).onSectionAttached(3);
                Intent intentMap = new Intent(getActivity(), Map.class);
                getActivity().startActivity(intentMap);
            }
        });

        ((ImageButton) rootView.findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                localisation = new Localisation();

                //cherche sa position gps
                localisation.findLocalization(getActivity());

                //attend un peu sinon a pas encore trouvé location
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        latitude = localisation.getLatitude();
                        longitude = localisation.getLongitude();
                        try {
                            localisation.setLatitude(latitude);
                            localisation.setLongitude(longitude);
                            localisation.sauvegarder(getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 5000); // <-- the "1000" is the delay time in miliseconds.
            }
        });

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
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
