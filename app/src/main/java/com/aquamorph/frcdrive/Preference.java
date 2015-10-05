package com.aquamorph.frcdrive;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preference extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
