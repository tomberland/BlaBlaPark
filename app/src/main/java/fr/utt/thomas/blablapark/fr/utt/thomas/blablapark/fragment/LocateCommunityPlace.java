package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocateCommunityPlace.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocateCommunityPlace#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocateCommunityPlace extends Fragment {

    private OnFragmentInteractionListener mListener;

//    // TODO: Rename and change types and number of parameters
//    public static LocateCommunityPlace newInstance(String param1, String param2) {
//        LocateCommunityPlace fragment = new LocateCommunityPlace();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static LocateCommunityPlace newInstance() {
        LocateCommunityPlace fragment = new LocateCommunityPlace();
        return fragment;
    }

    public LocateCommunityPlace() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        alertCommunityPopUp();

        return inflater.inflate(R.layout.fragment_locate_community_place, container, false);
    }

//    private void alertCommunityPopUp() {
//
//        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
//        helpBuilder.setTitle("Alert empty place to community");
//        helpBuilder.setMessage("Thank you for alerting the empty place, you earned 5exp");
//        helpBuilder.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                    }
//                });
//
//        // Remember, create doesn't show the dialog
//        AlertDialog helpDialog = helpBuilder.create();
//        helpDialog.show();
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(5);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
