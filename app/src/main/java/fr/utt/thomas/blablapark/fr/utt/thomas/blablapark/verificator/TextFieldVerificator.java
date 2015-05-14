package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.verificator;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Marc on 15/05/2015.
 */
public class TextFieldVerificator {


    private Context mContext;
    public boolean result;
    private TextView passVerif;
    private TextView emailVerif;

    public TextFieldVerificator(Context context, TextView email, TextView pass){
        mContext = context;
        passVerif = pass;
        emailVerif=email;
        result = false;

    }


    public void afficheResult(){
        ;
        if (emailVerif.getText().toString().matches("") && emailVerif.getText().toString().matches("")){
            Toast toast = Toast.makeText(mContext,
                    "Email or password field empty",
                    Toast.LENGTH_SHORT);
            toast.show();
            result = false;
        }
        else{
            result = true;
        }
    }

    public void setEmailVerif(TextView email){
        emailVerif=email;
    }

    public void setPassVerif(TextView pass){
        passVerif=pass;
    }
}
