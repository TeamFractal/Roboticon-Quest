package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.utils.DistributionAdapters;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Roboticon;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.ScoreScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreScreenActors extends Table{
    private RoboticonQuest game;
    private ScoreScreen screen;

        private Label topText;
        private Label playerStats;
        private Label marketStats;

    public ScoreScreenActors(final RoboticonQuest game, ScoreScreen screen) {
        this.game = game;
        this.screen = screen;

        // Buy Roboticon Text: Top Left
        final Label lblGameEnded = new Label("THE GAME HAS ENDED", game.skin);
        final Label lblWinnerName = new Label("THE WINNER IS" + <something.getWinner()> + "!", game.skin);

        // Current Roboticon Text: Top Right
        String playerRoboticonText = "CUSTOMISE PLAYER " + (game.getPlayerInt() + 1) + "'S ROBOTICONS HERE";
        final Label lblCurrentRoboticon = new Label(playerRoboticonText, game.skin);


        final TextButton exitButton = new TextButton("Exit ->", game.skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.dispose();
                // TODO add a method call to terminate the program.
            }
        });

        align(Align.center);
        //padTop(30);

        int span = 4; //This is the largest number of columns on any given row

        //Row 1
        add(lblGameEnded).colspan(span);

        row();

        //Row 2
        add(lblWinnerName);

        row().height(10);

        //Row 3
        add();

        row();

        //Iterate through players, and display their score
        for(int i = 0; i < game.playerList.size(); i++) {
            add(new Label("Player " + Integer.toString(game.playerList.get(i).getScore()) + " had score " + Integer.toString(game.playerList.get(i).getScore())));
            row();
        }

        //Row 11
        add(exitButton).colspan(span);
    }
}
