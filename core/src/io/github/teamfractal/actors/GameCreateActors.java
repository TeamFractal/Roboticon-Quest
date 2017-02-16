package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.RoboticonMarketScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class GameCreateActors extends Table {
    private RoboticonQuest game;

    private ArrayList<String> newPlayerNames = new ArrayList<String>();
    public ArrayList<String> getNewPlayerNames(){ return newPlayerNames; }

    private Table playerTable = new Table();

    public GameCreateActors(final RoboticonQuest game){
        this.game = game;

        final Label lblNewGame = new Label("Game Creation Menu", this.game.skin);
        final Label playerName = new Label("Player Name", this.game.skin);

        final TextField textField = new TextField("Name", this.game.skin);

        final TextButton addPlayerBtn = new TextButton("Add Player", this.game.skin);
        addPlayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newPlayerNames.add(textField.getText());
                textField.setText("");
                UpdatePlayerTable();
            }
        });

        final TextButton removePlayerBtn = new TextButton("Remove Last Player", this.game.skin);
        removePlayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newPlayerNames.remove(newPlayerNames.size()-1);
                UpdatePlayerTable();
            }
        });

        final TextButton confirmBtn = new TextButton("Confirm ->", this.game.skin);
        confirmBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.SetupPlayers(newPlayerNames);
                game.setScreen(game.gameScreen);
                game.gameScreen.newGame();
            }
        });

        Table nameEntryTable = new Table();
        nameEntryTable.add(new Label("Enter Name Here:", this.game.skin)).padLeft(30);
        nameEntryTable.add(textField);
        nameEntryTable.row();
        nameEntryTable.add(addPlayerBtn);
        nameEntryTable.add(removePlayerBtn);
        add(nameEntryTable);
        row();
        add(playerTable);
        add(confirmBtn);
    }

    private void UpdatePlayerTable(){
        playerTable.clearChildren();
        playerTable.add(new Label("List of Players for the Game", this.game.skin));
        playerTable.row();

        for(int i = 0; i < newPlayerNames.size(); i++){
            System.out.print(newPlayerNames.get(i)+"\n");
            playerTable.add(new Label(newPlayerNames.get(i), this.game.skin));
            playerTable.row();
        }
    }
}
