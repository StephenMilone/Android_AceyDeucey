
package com.milone.aceydeucey;

class Card {

  private int mValue;
  private int mSuit;

  public Card(int value, int suit) {
    mValue = value;
    mSuit = suit;
  }

  public int GetValue() { return mValue; }
  public int GetSuit() { return mSuit; }
  
}


