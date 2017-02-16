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
    Listener listener = new Listener();

    private ArrayList<String> newPlayerNames = new ArrayList<String>();
    public ArrayList<String> getNewPlayerNames(){ return newPlayerNames; }

    private Table playerTable = new Table();

    public GameCreateActors(final RoboticonQuest game){
        this.game = game;

        final Label lblNewGame = new Label("Game Creation Menu", this.game.skin);
        final Label playerName = new Label("Player Name", this.game.skin);

        final TextButton addPlayerBtn = new TextButton("Add Player", this.game.skin);
        addPlayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.print(listener.getString());
                Gdx.input.getTextInput(listener, "Player Name", "Player 1", "The name for the player");
                System.out.print(listener.getString()+"\n");
                newPlayerNames.add(listener.getString());
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
                game.setScreen(game.gameScreen);
                game.gameScreen.newGame();
            }
        });

        add(addPlayerBtn);
        add().width(40);
        add(removePlayerBtn);
        row();
        add(playerTable);
    }

    private void UpdatePlayerTable(){
        playerTable.clearChildren();
        playerTable.add(new Label("List of Players for the Game", this.game.skin));

        for(int i = 0; i < newPlayerNames.size(); i++){
            System.out.print(newPlayerNames.get(i)+"\n");
            playerTable.add(new Label(newPlayerNames.get(i), this.game.skin));
            playerTable.row();
        }
    }
}

class Listener implements Input.TextInputListener{
    private String readString;
    public String getString(){return readString;}

    @Override
    public void input (String text){
        System.out.print("input called"+"\n");
        readString = text;
    }

    @Override
    public void canceled(){
        System.out.print("canceled called"+"\n");
        readString = "Player 1";
    }


}