package com.example.charles.coresparent.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.charles.coresparent.R;

/**
 * Created by Charles on 18/05/2015.
 */
public class Parametres extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.activity_parametres);
    }
}
