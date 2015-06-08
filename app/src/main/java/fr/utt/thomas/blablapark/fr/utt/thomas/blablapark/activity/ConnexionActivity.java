package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity;

/**
 * Created by Ismail on 01/05/2015.
 * Gère la 1ère page de l'application
 * L'utilisateur doit rentrer ses identifiants pour se connecter
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.ParkingDisplay.GPSTracker;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase.MyDatabaseAdapter;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator.GpsVerificator;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator.TextFieldVerificator;

public class ConnexionActivity extends Activity {

    TextView email;
    TextView password;
    TextFieldVerificator textFieldVerificator;
    MyDatabaseAdapter dbAdaptater;
    GPSTracker gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        textFieldVerificator = new TextFieldVerificator(this,email,password);
 //       dbAdaptater = new MyDatabaseAdapter(this);
    }

    public void connexionClick(View view){

        email = (TextView) findViewById(R.id.textEmail);
        password = (TextView) findViewById(R.id.texPassword);

        textFieldVerificator.setEmailVerif(email);
        textFieldVerificator.setPassVerif(password);
        textFieldVerificator.afficheResult();

        //vérifie que les champs ne sont pas vides
        if (textFieldVerificator.result) {

            /*Local database
           dbAdaptater = new MyDatabaseAdapter(this);
           long id = dbAdaptater.insertData(email.getText().toString(), password.getText().toString());
          //  String data = dbAdaptater.viewData();
          //  MessageDB.message(this,data);
           if (id<0)
            {
                MessageDB.message(this,"unsucesful insert");
            }else
            {
                MessageDB.message(this,"successful insert");
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            }*/

            //creating GPS Class object
            gps = new GPSTracker(this);
            //Affiche un pop-up pour nous avertir que le GPS n'est pas activé sur notre téléphone et nous permet de l'activer
            GpsVerificator gpsVerificator = new GpsVerificator(this);
            gpsVerificator.getGpsLocalizationResult();

            // check if GPS location can get. If yes, launch next activity
            if (gps.canGetLocation()) {
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            }
        }
    }

    //si l'utilisateur clique sur le bouton Inscription, lance l'activité
    public void inscriptionClick(View view)
    {
        Intent intent = new Intent(this, InscriptionActivity.class);
        this.startActivity(intent);
    }
}
