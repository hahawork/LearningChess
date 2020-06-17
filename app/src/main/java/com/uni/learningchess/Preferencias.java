package com.uni.learningchess;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       /* Código para mostrar preferencias sin Fragment*
        addPreferencesFromResource(R.xml.preferencias);*/
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenciasFragment()).commit();
    }
}
