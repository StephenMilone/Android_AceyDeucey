package com.milone.aceydeucey;

/*  Acey Deucey for Android by Stephen Milone - Open Source Edition
 * 
 * Acey Deucey was my first android project that I coded while progressing through the chapters in an Android dev book.
 * I released it in October 2010, but as of Feburary 2012 it is available as open source on github.
 * The market release version of Acey Duecey includes AdMob and Scoreloop libraries for ad banners and global high scores, those have been removed from this copy.
 * More info available at www.stephenmilone.com and android@stephenmilone.com
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import com.milone.aceydeucey.Card;
import com.milone.aceydeucey.Prefs;

public class Gameplay extends Activity {
	int[] cardDrawID = {
			R.drawable.card_back, // 0 and 1
			R.drawable.card_back, // should never be drawn
			R.drawable.card_2s, R.drawable.card_3s, R.drawable.card_4s,
			R.drawable.card_5s, R.drawable.card_6s, R.drawable.card_7s,
			R.drawable.card_8s, R.drawable.card_9s, R.drawable.card_10s,
			R.drawable.card_11s, R.drawable.card_12s, R.drawable.card_13s,
			R.drawable.card_14s, R.drawable.card_2h, R.drawable.card_3h,
			R.drawable.card_4h, R.drawable.card_5h, R.drawable.card_6h,
			R.drawable.card_7h, R.drawable.card_8h, R.drawable.card_9h,
			R.drawable.card_10h, R.drawable.card_11h, R.drawable.card_12h,
			R.drawable.card_13h, R.drawable.card_14h, R.drawable.card_2c,
			R.drawable.card_3c, R.drawable.card_4c, R.drawable.card_5c,
			R.drawable.card_6c, R.drawable.card_7c, R.drawable.card_8c,
			R.drawable.card_9c, R.drawable.card_10c, R.drawable.card_11c,
			R.drawable.card_12c, R.drawable.card_13c, R.drawable.card_14c,
			R.drawable.card_2d, R.drawable.card_3d, R.drawable.card_4d,
			R.drawable.card_5d, R.drawable.card_6d, R.drawable.card_7d,
			R.drawable.card_8d, R.drawable.card_9d, R.drawable.card_10d,
			R.drawable.card_11d, R.drawable.card_12d, R.drawable.card_13d,
			R.drawable.card_14d, };

	SeekBar betSeekBar;
	TextView topText;
	TextView middleText;
	TextView botRightText;
	TextView botLeftText;
	Button firstDeal;
	Button raiseDeal;
	Button standDeal;
	Button nextDeal;
	ImageView secondcardView;
	ImageView firstcardView;
	ImageView thirdcardView;
	private int betamt = 4;
	private int bank = 100;
	private int bankpeak = 100;
	private int betamtstatic = 5;
	private int xInt = 0;
	private Card[] mCard;
	private int mCardCount;
	private int currentcard = -1;
	private int handsplayed = 0;
	private int saved_hands = 0;
	private int saved_score = 0;
	// private boolean saved_submit_sucess = false;
	private String statstext;
	private String lasthandtext;
	private String slname;
	private boolean hintson;
	private boolean scoresubmit;
	// private boolean socialsubmit;
	private boolean start_saved_game;

	private SharedPreferences savedgamefile;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		// Initialize all the viewable items from the XML layout
		firstcardView = (ImageView) findViewById(R.id.FirstCard);
		secondcardView = (ImageView) findViewById(R.id.SecondCard);
		thirdcardView = (ImageView) findViewById(R.id.ThirdCard);

		betSeekBar = (SeekBar) findViewById(R.id.BetBar);
		betSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

		topText = (TextView) findViewById(R.id.TopText);
		middleText = (TextView) findViewById(R.id.MiddleText);
		botRightText = (TextView) findViewById(R.id.BotRightText);
		botLeftText = (TextView) findViewById(R.id.BotLeftText);

		firstDeal = (Button) findViewById(R.id.FirstDealButton);
		raiseDeal = (Button) findViewById(R.id.RaiseButton);
		standDeal = (Button) findViewById(R.id.StandButton);
		nextDeal = (Button) findViewById(R.id.OkayButton);

		standDeal.setVisibility(4);
		raiseDeal.setVisibility(4);
		nextDeal.setVisibility(4);
		firstDeal.setVisibility(4);

		// Set the deck array with the contents of (#) decks
		Init(8);

		// Get the app's shared preferences
		savedgamefile = getSharedPreferences("AcePrefs", 0);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// Get the username from shared prefs or scoreloop, ifnot null store it
		hintson = prefs.getBoolean("hints", true);
		scoresubmit = prefs.getBoolean("scoresubmit", true);
		// socialsubmit = prefs.getBoolean("socialsubmit", true);
		slname = savedgamefile.getString("slname", "player_###");
		saved_score = savedgamefile.getInt("saved_score", 0);
		saved_hands = savedgamefile.getInt("saved_hands", 0);
		start_saved_game = savedgamefile
				.getBoolean("start_saved_game", false);


		SharedPreferences.Editor editor = savedgamefile.edit();

		if (start_saved_game == true) {
			// read TEMP saved scores/hands, then set saved game trigger
			betamt = savedgamefile.getInt("temp_betamt", 5);
			bank = savedgamefile.getInt("temp_bank", 0);
			bankpeak = savedgamefile.getInt("temp_bankpeak", 100);
			handsplayed = savedgamefile.getInt("temp_handsplayed", 0);
			betSeekBar.setMax((bank) - 1);
			xInt = betamt - 1;
			betSeekBar.setProgress(10);
			betSeekBar.setProgress(betamt);
			showToast("Save game loaded. Save games are only allowed to be loaded once.");
			editor.putBoolean("active_save", false); // value to store
		}

		firstDeal.setVisibility(0);

		if (hintson == false)
			topText.setVisibility(4);

		statstext = "Current Money: $" + bank + "\nHands Played: "
				+ handsplayed + "\nCards left : "
				+ (mCardCount - currentcard - 1) + "\nPeak: $" + bankpeak;

		// if you change the above line, change it below in redeal fucntion
		botRightText.setText(statstext);

		// click First Deal
		firstDeal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				firstDeal.setVisibility(4); // 4 = invisible, 0 = visible
				betamt = betSeekBar.getProgress() + 1;
				betamtstatic = betamt;
				middleText.setText("$"
						+ String.valueOf(betSeekBar.getProgress() + 1));
				topText.setText("You can now stand or raise. Raise will double your bet. [Note Aces are high]");
				betSeekBar.setVisibility(4);
				standDeal.setVisibility(0);
				raiseDeal.setVisibility(0);
				handsplayed++;
				currentcard++;
				xInt = mCard[currentcard].GetValue()
						+ mCard[currentcard].GetSuit();
				firstcardView.setImageResource(cardDrawID[xInt]);
				currentcard++;
				xInt = mCard[currentcard].GetValue()
						+ mCard[currentcard].GetSuit();
				secondcardView.setImageResource(cardDrawID[xInt]);

				lasthandtext = mCard[currentcard - 1].GetValue() + " & "
						+ mCard[currentcard].GetValue() + " : ";

				if (mCard[currentcard].GetValue() + 1 == mCard[currentcard - 1]
						.GetValue()
						|| mCard[currentcard].GetValue() - 1 == mCard[currentcard - 1]
								.GetValue()) {
					topText.setText("This hand is unwinnable, which results in a push and no 3rd card will be dealt.");
					standDeal.setVisibility(4);
					raiseDeal.setVisibility(4);

					nextDeal.setVisibility(0);
					middleText.setText("Push. $" + betamt + " returned.");
					betamt = 0;
					lasthandtext = lasthandtext + "N/A. Push";

				}

				if (mCard[currentcard].GetValue() == mCard[currentcard - 1]
						.GetValue()) {
					topText.setText("You've been dealt a pair.  A 3 of kind results in an 11x payout else you get a push. Sorry no raising.");
					betamt = 0;
					raiseDeal.setVisibility(4);
					standDeal.setText("Go for 3");
				}
				if (betamt * 2 > bank) {
					raiseDeal.setVisibility(4);
					topText.setText("Your only move is to stand with your current bet, you cannot raise as you arent able to double your bet.");
				}

			}
		});

		// click Raise or Stand, Raise doubles bet before calling deal
		standDeal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dealpostbet();
			}
		});

		raiseDeal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				betamt = betamt * 2;
				xInt = betamt - 1;
				betSeekBar.setProgress(10); // workaround - for some reason
											// going to set the progress
											// directly to a variable only
											// worked some times, setting it to
											// an absolute value first fixed
											// this
				betSeekBar.setProgress(xInt);
				dealpostbet();
			}
		});

		// click Okay after cards dealt and payout recieved/taken
		nextDeal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				redeal();

			}
		});

	}

	private void dealpostbet() {

		standDeal.setText(" STAND ");
		standDeal.setVisibility(4);
		raiseDeal.setVisibility(4);

		nextDeal.setVisibility(0);
		currentcard++;
		xInt = mCard[currentcard].GetValue() + mCard[currentcard].GetSuit();
		thirdcardView.setImageResource(cardDrawID[xInt]);
		lasthandtext = lasthandtext + mCard[currentcard].GetValue();

		if ((mCard[currentcard].GetValue() > mCard[currentcard - 1].GetValue() && mCard[currentcard]
				.GetValue() < mCard[currentcard - 2].GetValue())
				|| (mCard[currentcard].GetValue() < mCard[currentcard - 1]
						.GetValue() && mCard[currentcard].GetValue() > mCard[currentcard - 2]
						.GetValue())
				|| (mCard[currentcard].GetValue() == mCard[currentcard - 1]
						.GetValue() && mCard[currentcard].GetValue() == mCard[currentcard - 2]
						.GetValue())) {

			topText.setText("You've won with 1:1 odds");

			if (mCard[currentcard].GetValue() == mCard[currentcard - 1]
					.GetValue()
					&& mCard[currentcard].GetValue() == mCard[currentcard - 2]
							.GetValue()) {
				betamt = betamtstatic * 11;
				topText.setText("You've won with 11:1 odds! Impressive.");
			}

			switch (mCard[currentcard - 1].GetValue()
					- mCard[currentcard - 2].GetValue()) {
			case (2):
			case (-2):
				betamt = betamt * 5;
				topText.setText("You've won with 5:1 odds");
				break;
			case (3):
			case (-3):
				betamt = betamt * 4;
				topText.setText("You've won with 4:1 odds");
				break;
			case (4):
			case (-4):
				betamt = betamt * 2;
				topText.setText("You've won with 2:1 odds");
				break;
			}

			bank = bank + betamt;
			betSeekBar.setMax((bank) - 1);
			middleText.setText("You Won $" + betamt + "!!");
			lasthandtext = lasthandtext + ". Won $" + betamt;

			if (bank > bankpeak)
				bankpeak = bank;

		} else {
			if (betamt == 0) {
				topText.setText("Hand resulted in a push, your bet will be returned.");
				lasthandtext = lasthandtext + ". Push";
				betSeekBar.setMax((bank) - 1);
				middleText.setText("Push. $" + betamtstatic + " returned.");
			} else {
				topText.setText("Sorry you lost this hand.");
				bank = bank - betamt;
				lasthandtext = lasthandtext + ". Lost $" + betamt;
				betSeekBar.setMax((bank) - 1);
				middleText.setText("You Lost $" + betamt);
			}
			if (bank <= 0) {

				nextDeal.setVisibility(4);

				if (scoresubmit == false) {
					AlertDialog.Builder alertboxn = new AlertDialog.Builder(
							this);
					alertboxn
							.setMessage("   Game Over\nYou played "
									+ handsplayed
									+ " hands and your highest point was $"
									+ bankpeak
									+ ". High score submission disabled via user settings");
					alertboxn.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									finish();
								}
							});

					alertboxn.show();
				} else

				{

					AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
					alertbox.setMessage("   Game Over\nYou played "
							+ handsplayed
							+ " hands and your highest point was $"
							+ bankpeak
							+ ". Now connecting to Scoreloop to submit your score under the name: "
							+ slname);
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {

									//final Score myscore = new Score(
									//		(double) bankpeak, null);
									//myscore.setLevel(handsplayed);

									//final ScoreController myScoreController = new ScoreController(
									//		new ScoreSubmitObserver());

									// Use the controller to submit to the
									// server
									//myScoreController.submitScore(myscore);

									// ScoreloopManager.submitScore(bankpeak,
									// new ScoreSubmitObserver());
									showToast("Awaiting high score upload to finish.");
									// showProgressIndicator();
								}
							});

					alertbox.show();
				}

			}

		}

	}

	private void redeal() {
		firstDeal.setVisibility(0); // 4 = invisible, 0 = visible
		betSeekBar.setVisibility(0);
		standDeal.setVisibility(4);
		raiseDeal.setVisibility(4);
		nextDeal.setVisibility(4);
		firstcardView.setImageResource(R.drawable.card_back);
		secondcardView.setImageResource(R.drawable.card_back);
		thirdcardView.setImageResource(R.drawable.card_back);
		xInt = betamtstatic - 1;
		betSeekBar.setProgress(10); // workaround - for some reason going to set
									// the progress directly to a variable only
									// worked some times, setting it to an
									// absolute value first fixed this
		betSeekBar.setProgress(xInt);
		topText.setText("Place your bet for the next hand.  You can turn off this top section of hints by pressing the phyiscal menu button and turning hints off.");
		if (mCardCount - currentcard < 4) {
			Init(8);
			currentcard = -1;
			showToast("You've reached the end of the deck, reshuffling all 8 decks back in.");
		}
		statstext = "Current Money: $" + bank + "\nHands Played: "
				+ handsplayed + "\nCards left : "
				+ (mCardCount - currentcard - 1) + "\nPeak: $" + bankpeak;
		botRightText.setText(statstext);
		botLeftText.setText("Last Hand Result:\n" + lasthandtext);
		lasthandtext = "";

	}

	public void Init(int decks) {

		// Creates deck array
		mCardCount = decks * 52; // how many decks called for, make 52 per
		// mTotalCards = mCardCount;
		mCard = new Card[mCardCount];
		for (int deck = 0; deck < decks; deck++) {
			for (int suit = 0; suit < 4; suit++) {
				for (int value = 0; value < 13; value++) {
					mCard[deck * 52 + suit * 13 + value] = new Card(value + 2,
							suit * 13);
					// default is 8 decks for this game, mcard array will reach
					// and the loop will run 416 times. 0 - 415 cards
				}
			}
		}
		// 8 (or whatever number) decks are now created but in EXACT order in
		// the array..let's randomize.
		Shuffle();
		Shuffle();
		Shuffle();
	}

	public void Shuffle() {
		int lastIdx = mCardCount - 1;
		int swapIdx;
		Card swapCard;
		Random rand = new Random();

		while (lastIdx > 1) {
			swapIdx = rand.nextInt(lastIdx); // generate random number
			swapCard = mCard[swapIdx]; // SwapCard = holding spot for random
										// card
			mCard[swapIdx] = mCard[lastIdx]; // replace the random card with
												// that card that was last in
												// the deck
			mCard[lastIdx] = swapCard; // then move the random card into the
										// last spot on the deck
			lastIdx--; // loop counter, so theres a new last card next time, to
						// ensure every spot gets swapped
		}
	}

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			middleText.setText("$"
					+ String.valueOf(betSeekBar.getProgress() + 1));
		}

		public void onStartTrackingTouch(SeekBar seekBar) {}
		public void onStopTrackingTouch(SeekBar seekBar) {}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		case R.id.savegame:
			SharedPreferences savedgamefile = getSharedPreferences(
					"AcePrefs", 0);
			SharedPreferences.Editor editor = savedgamefile.edit();
			editor.putInt("temp_betamt", betamt); // value to store
			editor.putInt("temp_bank", bank); // value to store
			editor.putInt("temp_bankpeak", bankpeak); // value to store
			editor.putInt("temp_handsplayed", handsplayed); // value to store
			editor.putBoolean("active_save", true); // value to store
			editor.commit();
			showToast("Game successfully saved.");
			finish();
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		hintson = prefs.getBoolean("hints", true);

		if (hintson == false) {
			topText.setVisibility(4);
		} else {
			topText.setVisibility(0);
		}

	}


	void showToast(final String message) {
//		final View view = getLayoutInflater().inflate(
				//R.layout.sl_dialog_custom, null);
		//((TextView) view.findViewById(R.id.message)).setText(message);
		//final Toast toast = new Toast(getApplicationContext());
		//toast.setDuration(Toast.LENGTH_LONG);
		//toast.setView(view);
		//toast.show();
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}



}
