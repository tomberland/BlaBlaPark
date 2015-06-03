package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.FindCar;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.Home;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.NavigationDrawerFragment;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.fragment.Profil;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator.GpsVerificator;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0) {

//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, Home.newInstance(),"home")
//                    .commit();
            fragmentManager.beginTransaction()
                    .add(R.id.container, Home.newInstance())
                    .addToBackStack("fragBack")
                    .commit();

        } else if (position == 1) {

            fragmentManager.beginTransaction()
                    .add(R.id.container, Profil.newInstance())
                    .addToBackStack("fragBack")
                    .commit();

        } else if (position == 2) {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.message_position_voiture_enregistre), Toast.LENGTH_LONG);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(20);
                toast.show();

        } else if (position == 3) {

            fragmentManager.beginTransaction()
                    .add(R.id.container, FindCar.newInstance())
                    .addToBackStack("fragBack")
                    .commit();

        }
        else if (position == 4) {

            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.message_locate_empty_place), Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toast.show();
        }
    }

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
            case R.id.action_help :

                //lance l'activité Préférences
//                Intent intent1 = new Intent(getActivity(), Preference.class);
//                startActivity(intent1);

//                //pas Toast basique, on agrandit le texte ici
//                Toast toast = Toast.makeText(getApplicationContext(), "Coucou3", Toast.LENGTH_LONG);
//                LinearLayout toastLayout = (LinearLayout) toast.getView();
//                TextView toastTV = (TextView) toastLayout.getChildAt(0);
//                toastTV.setTextSize(20);
//                toast.show();
//
//                return true;
//
//            case R.id.save_car_position:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aide");
//                builder.setTitle(getString(R.string.recapitulatif));
                builder.setMessage(Html.fromHtml("<font color='#33b5e5'>- Appuyez sur \"Search Parking\" pour afficher les places communautaires (points verts).<br><br>- Ceux en rouge indiquent les parkings connus.<br><br>- Appuyez sur un de ces points puis sur le bouton en bas à droite pour commencer la navigation.</font>"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
//                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                            }
//                        })
//                        .setNeutralButton(R.string.show_map, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                            }
//                        })
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