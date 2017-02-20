package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.teamfractal.RoboticonQuest;
import java.util.ArrayList;

public class GameCreateActors extends Table {
    private RoboticonQuest game;

    private ArrayList<String> newPlayerNames = new ArrayList<String>();

    private Table playerTable = new Table();

    public GameCreateActors(final RoboticonQuest game){
        this.game = game;

        final Label lblNewGame = new Label("Game Creation Menu", this.game.skin);

        final TextField textField = new TextField("Name", this.game.skin);

        final TextButton addPlayerBtn = new TextButton("Add Player", this.game.skin);
        addPlayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(newPlayerNames.size() < 4 && textField.getText().length() > 0) {
                    newPlayerNames.add(textField.getText());
                    textField.setText("");
                    UpdatePlayerTable();
                }
            }
        });

        final TextButton removePlayerBtn = new TextButton("Remove Last Player", this.game.skin);
        removePlayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!newPlayerNames.isEmpty()) {
                    newPlayerNames.remove(newPlayerNames.size() - 1);
                    UpdatePlayerTable();
                }
            }
        });

        final TextButton confirmBtn = new TextButton("Confirm ->", this.game.skin);
        confirmBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!newPlayerNames.isEmpty()) {
                    game.SetupPlayers(newPlayerNames);
                    game.setScreen(game.gameScreen);
                    game.gameScreen.newGame();
                }
            }
        });

        add(lblNewGame);
        row();
        add().height(30);
        row();

        Table nameEntryTable = new Table();
        nameEntryTable.add(new Label("Enter Name Here:", this.game.skin)).padRight(30);
        nameEntryTable.add(textField);
        nameEntryTable.row();
        nameEntryTable.add(addPlayerBtn);
        nameEntryTable.add(removePlayerBtn);
        add(nameEntryTable);
        row();
        add().height(30);
        row();

        add(playerTable);
        add(confirmBtn);
    }

    private void UpdatePlayerTable(){
        playerTable.clearChildren();
        playerTable.add(new Label("List of Players for the Game", this.game.skin));
        playerTable.row();

        for(int i = 0; i < newPlayerNames.size(); i++){
            playerTable.add(new Label(newPlayerNames.get(i), this.game.skin));
            playerTable.row();
        }
    }
}
