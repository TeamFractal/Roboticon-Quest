package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.AuctionActors;
import io.github.teamfractal.actors.ResourceMarketActors;


public class ResourceMarketScreen implements Screen {
	final RoboticonQuest game;
	final Stage stage;
	final Table table;
	private final ResourceMarketActors marketActors;
	
	public ResourceMarketScreen(final RoboticonQuest game, MarketScreen marketScreen) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		this.table = new Table();
		table.setFillParent(true);

		marketActors = new ResourceMarketActors(game, this, marketScreen); // generates actors for the screen

		table.add(marketActors);

		stage.addActor(table);
	}

	public void prepare(){
		marketActors.widgetUpdate();
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
		marketActors.screenResize(width, height);
		//auctionActors.screenResize(width, height);
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
