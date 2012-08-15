package com.milone.aceydeucey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AceyDeucey extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

		//Declare Button variables and make them listen for clicks (touches)
		Button continueButton = (Button) findViewById(R.id.continue_button);
		Button newButton = (Button) findViewById(R.id.new_button);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		Button exitButton = (Button) findViewById(R.id.exit_button);
		continueButton.setOnClickListener(this);
		newButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	
		//Load the shared Prefs and check if a save game is active.
		SharedPreferences app_preferences = getSharedPreferences("AcePrefs", 0);
		boolean active_save = app_preferences.getBoolean("active_save", false);

		//If so, let the continue button be enabled, otherwise only new game can be choosen
		if (active_save == false)
			continueButton.setEnabled(false);
		else
			continueButton.setEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//When returning to this activity, check again if a save game exists 
		//enable/disable the continue button as before
		SharedPreferences app_preferences = getSharedPreferences("AcePrefs", 0);
		boolean active_save = app_preferences.getBoolean("active_save", false);
		Button continueButton = (Button) findViewById(R.id.continue_button);

		if (active_save == false)
			continueButton.setEnabled(false);
		else
			continueButton.setEnabled(true);

	}

	
	public void onClick(View v) {
	
	//switch determines which button was clicked.
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
