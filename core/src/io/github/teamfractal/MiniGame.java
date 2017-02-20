package io.github.teamfractal;

import java.util.Random;
import java.util.Scanner;

public class MiniGame {
	private static final Random rand = new Random();
	private final int WIN_AMOUNT = 600;
	private final int MAX_GUESS = 3;
	private final int MIN_GUESS = 1;

	public boolean WinGame(int guessedNumber){
		int generatedNumber = rand.nextInt((MAX_GUESS - MIN_GUESS) + 1) + MIN_GUESS;
		return guessedNumber == generatedNumber;
	}

	public int getPrice(boolean bIsWin){
		if (bIsWin){
			return WIN_AMOUNT;
		}
		else {
			return 0;
		}

	}
	
}
