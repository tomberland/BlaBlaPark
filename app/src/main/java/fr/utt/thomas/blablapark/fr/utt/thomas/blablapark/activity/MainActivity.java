package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.FindCar;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.Home;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.Localisation;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.NavigationDrawerFragment;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.Profil;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    GPSTracker gps;
    private Localisation localisation;
    private ListView mDrawerListView;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //définit le fragment
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        //définit le titre du fragment
        mTitle = getTitle();

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    //gère le fragment sélectionné
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //fragment Accueil
        if (position == 0) {

            fragmentManager.beginTransaction()
                    .replace(R.id.container, Home.newInstance(),"home")
                    .commit();

        //fragment Profil
        } else if (position == 1) {

            fragmentManager.beginTransaction()
                    .add(R.id.container, Profil.newInstance())
                    .addToBackStack("profil")
                    .commit();

        //fragment Enregistrer position voiture
        } else if (position == 2) {

            //creating GPS Class object
            gps = new GPSTracker(this);

            //crée nouvelle localisation
            localisation = new Localisation();
            //y stocke notre position actuelle
            localisation.setLatitude(Double.toString(gps.getLatitude()));
            localisation.setLongitude(Double.toString(gps.getLongitude()));

            //la sauvegarde dans un fichier
            try {
                localisation.sauvegarder(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //affiche un message pour préciser à l'utilisateur que l'action a bien été prise en compte
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.message_position_voiture_enregistre), Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toast.show();

            //permet au navigationDrawer de ne pas rester sur Enregistrer position voiture
            mDrawerListView = (ListView) findViewById(R.id.navList);
            mDrawerListView.setItemChecked(0, true); //0 pour le Home

        //fragment pour Trouver ma voiture
        } else if (position == 3) {

            fragmentManager.beginTransaction()
                    .add(R.id.container, FindCar.newInstance())
                    .addToBackStack("FindCar")
                    .commit();

        //fragment pour Indiquer une place libre
        }else if (position == 4) {

            //affiche un message pour préciser à l'utilisateur que l'action a bien été prise en compte
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.message_locate_empty_place), Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toast.show();

            //permet au navigationDrawer de ne pas rester sur Indiquer une place libre
            mDrawerListView = (ListView) findViewById(R.id.navList);
            mDrawerListView.setItemChecked(0, true);
        }
    }

    //sélectionne le bon titre
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.home);
                break;
            case 2:
                mTitle = getString(R.string.profil);
                break;
            case 3:
                mTitle = getString(R.string.save_car);
                break;
            case 4:
                mTitle = getString(R.string.findCar);
                break;
            case 5:
                mTitle = getString(R.string.locate_empty_place);
                break;
        }
    }

    //met à jour l'ActionBar
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            //définit le bouton menu d'Aide en haut à droite
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //menu d'aide
            case R.id.action_help :

                //affiche un pop-up
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aide");
//                builder.setTitle(getString(R.string.recapitulatif));
                builder.setMessage(Html.fromHtml("<font color='#33b5e5'>- Appuyez sur \"Search" +
                        " Parking\" pour afficher les places communautaires (points verts)." +
                        "<br><br>- Ceux en rouge indiquent les parkings connus.<br><br>" +
                        "- Appuyez sur un de ces points puis sur le bouton en bas à droite pour " +
                        "commencer la navigation.</font>"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                ;
                // Creer l'alerte
                AlertDialog alertDialog = builder.create();

                // Afficher l'alerte
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}