package com.milone.aceydeucey;

//import com.admob.android.ads.AdManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AceyDeucey extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	// Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// AdManager.setTestDevices( new String[]
		// {AdManager.TEST_EMULATOR,"HT99EHF02135",} );

		View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener(this);
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		View scoreButton = findViewById(R.id.scoreboard_button);
		scoreButton.setOnClickListener(this);
		SharedPreferences app_preferences = getSharedPreferences("AcePrefs", 0);
		boolean active_save = app_preferences.getBoolean("active_save", false);

		if (active_save == false)
			continueButton.setEnabled(false);
		else
			continueButton.setEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences app_preferences = getSharedPreferences("AcePrefs", 0);
		boolean active_save = app_preferences.getBoolean("active_save", false);
		View continueButton = findViewById(R.id.continue_button);

		if (active_save == false)
			continueButton.setEnabled(false);
		else
			continueButton.setEnabled(true);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.continue_button:

			SharedPreferences app_preferences = getSharedPreferences(
					"AcePrefs", 0);
			SharedPreferences.Editor editor = app_preferences.edit();
			editor.putBoolean("start_saved_game", true); // value to store
			editor.commit();

			startActivity(new Intent(AceyDeucey.this, Gameplay.class));
			break;
		case R.id.about_button:
			startActivity(new Intent(AceyDeucey.this, Msg2.class));
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		//case R.id.scoreboard_button:
		//	startActivity(new Intent(AceyDeucey.this, EntryScreenActivity.class));
		//	break;
		case R.id.exit_button:
			finish();
			break;
		}

	}

	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.new_game_title)
				.setItems(R.array.difficulty,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startGame(i);
							}
						}).show();
	}

	// 3rd option for cards first
	private void startGame(int i) {
		if (i == 1) {
			startActivity(new Intent(AceyDeucey.this, Msg3.class));
		} else {
			SharedPreferences app_preferences = getSharedPreferences(
					"AcePrefs", 0);
			SharedPreferences.Editor editor = app_preferences.edit();
			editor.putBoolean("start_saved_game", false); // value to store
			editor.commit();
			startActivity(new Intent(AceyDeucey.this, Gameplay.class));
		}

	}

}
