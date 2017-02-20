package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.AuctionActors;


public class AuctionScreen implements Screen {
    final RoboticonQuest game;
    final Stage stage;
    final Table table;
    private final AuctionActors auctionActors;


    public AuctionScreen(final RoboticonQuest game, MarketScreen marketScreen) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.table = new Table();
        table.setFillParent(true);
        final MarketScreen parentMarketScreen = marketScreen;

        auctionActors = new AuctionActors(game, this, marketScreen); // generates actors for the screen
        table.add(auctionActors);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {


    }

    @Override
    public void hide() {


    }

    @Override
    public void dispose() {
        stage.dispose();

    }
    public Stage getStage(){
        return this.stage;
    }
}
