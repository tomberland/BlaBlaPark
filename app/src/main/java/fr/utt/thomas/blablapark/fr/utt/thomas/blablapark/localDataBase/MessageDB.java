package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Marc on 17/05/2015.
 */
public class MessageDB {

    public static void message(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
