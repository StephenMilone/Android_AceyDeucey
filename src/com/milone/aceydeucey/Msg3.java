package com.milone.aceydeucey;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Msg3 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
  	  SharedPreferences.Editor editor = preferences.edit();

  	  editor.putBoolean("hints", true); // value to store
  	  // Commit to storage
  	  editor.commit();
        
        
    }
    
}