package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment;

/**
 * Created by Marc on 10/05/2015.
 * Gère le fragment Profil
 */

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity.MainActivity;

public class Profil extends Fragment {

    ArrayAdapter<String> adapter;
    private List<String> listItem;
    private List<String> listItem2;
    private RatingBar ratingBar;
    private OnFragmentInteractionListener mListener;
    private ListView mDrawerListView;

    public static Profil newInstance() {
        Profil fragment = new Profil();
        return fragment;
    }

    public Profil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        //force l'orientation du fragment en portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //définit les variables
        listItem = new ArrayList<String>();
        listItem.add("Pseudo");
        listItem.add("Email");
        listItem.add("Password");
        listItem2 = new ArrayList<String>();
        listItem2.add("MarcSi");
        listItem2.add("marc.sirisak@utt.fr");
        listItem2.add("********");
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        //définit la couleur du texte
        final int blueColor = Color.parseColor("#4682B4");

        ListView listview = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text1, listItem){

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                //affiche le texte
                text1.setText(listItem.get(position).toString());
                text1.setTextColor(blueColor);
                text1.setTextSize(15);
                text2.setText(listItem2.get(position).toString());
                text2.setTextSize(15);

                return view;
            }
        };
        listview.setAdapter(adapter);
        ratingBar.setRating(2);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setEnabled(false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        //permet de revenir sur l'Accueil si l'utilisateur clique sur le bouton retour
        ((MainActivity) getActivity()).onSectionAttached(1);
        ((MainActivity) getActivity()).restoreActionBar();

        //permet au navigationDrawer de ne pas rester sur Profil
        mDrawerListView = (ListView) getActivity().findViewById(R.id.navList);
        mDrawerListView.setItemChecked(0, true);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
