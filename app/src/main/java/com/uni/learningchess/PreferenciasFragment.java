package com.uni.learningchess;

import android.os.Bundle;
import android.preference.PreferenceFragment;

    public class PreferenciasFragment extends PreferenceFragment {


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
