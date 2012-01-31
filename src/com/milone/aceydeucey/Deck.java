/*
  Copyright 2008 Google Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/ 
package com.milone.aceydeucey;

import java.util.Random;


public class Deck {

  private Card[] mCard;
  private int mCardCount;
  //private int mTotalCards;
  
  public Deck(int decks) {
	    Init(decks);
	  }
  
  private void Init(int decks) {
	  
	  //Creates deck array
    mCardCount = decks * 52;  //how many decks called for, make 52 per
    //mTotalCards = mCardCount;
    mCard = new Card[mCardCount];
    for (int deck = 0; deck < decks; deck++) { 
      for (int suit = 0; suit < 4; suit++) {  
        for (int value = 0; value < 13; value++) { 
          mCard[deck*52 + suit*13 + value] = new Card(value+1, suit);
          //default is 8 decks for this game, mcard array will reach 
          //and the loop will run 416 times. 0 - 415 cards 
        }
      }
    }
//8 (or whatever number) decks are now created but in EXACT order in the array..let's randomize. 
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
      swapIdx = rand.nextInt(lastIdx);  //generate random number
      swapCard = mCard[swapIdx];     //SwapCard = holding spot for random card
      mCard[swapIdx] = mCard[lastIdx];   //replace the random card with that card that was last in the deck
     mCard[lastIdx] = swapCard;  //then move the random card into the last spot on the deck
      lastIdx--; //loop counter, so theres a new last card next time, to ensure every spot gets swapped
    }
  }
}
