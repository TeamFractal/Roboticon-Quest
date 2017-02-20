package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.CasinoActors;


public class CasinoScreen implements Screen {
    final RoboticonQuest game;
    final Stage stage;
    final Table table;
    private final CasinoActors casinoActors;

    public CasinoScreen(final RoboticonQuest game, MarketScreen marketScreen) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.table = new Table();
        table.setFillParent(true);

        casinoActors = new CasinoActors(game, this, marketScreen); // generates actors for the screen
        table.add(casinoActors);
        stage.addActor(table);
    }

    public void prepare(){
        casinoActors.widgetUpdate();
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
