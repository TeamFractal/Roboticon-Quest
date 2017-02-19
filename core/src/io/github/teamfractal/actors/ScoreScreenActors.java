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

    public ScoreScreenActors(final RoboticonQuest game, ScoreScreen screen) {
        this.game = game;
        this.screen = screen;

        final Label lblGameEnded = new Label("THE GAME HAS ENDED", this.game.skin);
        final Label lblWinnerName = new Label("THE WINNER IS" + this.game.getWinningPlayer().getName() + "!", this.game.skin);

        final TextButton exitButton = new TextButton("Exit ->", game.skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.dispose();
                Gdx.app.exit();
            }
        });

        align(Align.center);

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
        for(int i = 0; i < this.game.playerList.size(); i++) {
            add(new Label(this.game.playerList.get(i).getName() + " had score " + Integer.toString(this.game.playerList.get(i).getScore()), this.game.skin));
            row();
        }

        //Row 11
        add(exitButton).colspan(span);
    }
}
