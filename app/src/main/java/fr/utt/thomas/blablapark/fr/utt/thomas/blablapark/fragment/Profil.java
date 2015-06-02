package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;
import fr.utt.thomas.blablapark.R;

public class Profil extends Fragment {

    ArrayAdapter<String> adapter;
    private List<String> listItem;
    private List<String> listItem2;
    private RatingBar ratingBar;
    private OnFragmentInteractionListener mListener;

    public static Profil newInstance() {
        Profil fragment = new Profil();
        return fragment;
    }

    public Profil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container,
                false);

        listItem = new ArrayList<String>();
        listItem.add("Pseudo");
        listItem.add("Email");
        listItem.add("Password");
        listItem2 = new ArrayList<String>();
        listItem2.add("Marcoucou");
        listItem2.add("marc.sirisak@utt.fr");
        listItem2.add("********");
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        ListView listview = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text1, listItem){
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(listItem.get(position).toString());
                text1.setTextColor(-1);
                text1.setTextSize(15);
                text2.setText(listItem2.get(position).toString());
                text2.setTextSize(15);

                return view;
            }
        };
        listview.setAdapter(adapter);
        ratingBar.setRating(2);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setEnabled(false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("fragment", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    if (getFragmentManager().findFragmentByTag("home") == null) {
                        Log.i("fragment", "onKey Back listener is working!!!");
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Fragment home = new Home();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        if(!isAdded())
                        {
                            fragmentTransaction.add(home, "home");
                        }
                        fragmentTransaction.replace(R.id.container, home);
                        fragmentTransaction.commit();
                    }else
                    {
                        Log.i("fragment", "no adding again te fragment!!!");
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, getFragmentManager().findFragmentByTag("home"));
                        fragmentTransaction.commit();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootView;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
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
