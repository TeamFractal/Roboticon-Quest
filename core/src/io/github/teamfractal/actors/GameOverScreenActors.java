package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.util.ScoreComparisons;

import java.util.ArrayList;

/**
 * Created by Josh Neil creates all the actors to be displayed on the game over screen
 * @author jcn509
 *
 */
public class GameOverScreenActors extends HomeMainMenu {
	
	public GameOverScreenActors(final RoboticonQuest game) {
		super(game);	
		Label winnerLabel = new Label(ScoreComparisons.getWinnerText(game.playerList), game.skin);
		
		// All other content created in the super class' constructor
		row();
		add(winnerLabel).pad(40);
		row();
		createAndAddScoreLabels(game);
	}
	
	/**
	 * Creates a label for each player that displays their score and adds this label to the screen
	 * @param playerList The players that just finished playing the game
	 */
	private void createAndAddScoreLabels(RoboticonQuest game){
		ArrayList<Player> playerList = game.playerList;
		Label scoreLabel;
		for(int playerNumber =0; playerNumber<playerList.size();playerNumber++){
			String labelText = "Player "+Integer.toString(playerNumber+1)+" scored "+Integer.toString(playerList.get(playerNumber).getScore());
			scoreLabel = new Label(labelText,game.skin);
			add(scoreLabel);
			row();
		}
	}


}
