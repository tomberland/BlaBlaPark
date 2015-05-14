package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fr.utt.thomas.blablapark.R;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.map.MapFindCar;
import fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator.TextFieldVerificator;

public class ConnexionActivity extends Activity {


    TextView email;
    TextView password;
    TextFieldVerificator textFieldVerificator;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        textFieldVerificator = new TextFieldVerificator(this,email,password);

    }


    public void connexionClick(View view){


        email = (TextView) findViewById(R.id.textEmail);
        password = (TextView) findViewById(R.id.texPassword);

        textFieldVerificator.setEmailVerif(email);
        textFieldVerificator.setPassVerif(password);
        textFieldVerificator.afficheResult();
        if (textFieldVerificator.result) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connexion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
