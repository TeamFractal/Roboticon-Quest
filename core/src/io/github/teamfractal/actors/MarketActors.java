package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.screens.CasinoScreen;
import io.github.teamfractal.screens.MarketScreen;
import io.github.teamfractal.screens.ResourceMarketScreen;
import io.github.teamfractal.screens.AuctionScreen;

public class MarketActors extends Table {
    private RoboticonQuest game;

    private CasinoScreen casinoScreen;
    private ResourceMarketScreen marketScreen;
    private AuctionScreen auctionScreen;

    public MarketActors(final RoboticonQuest game, MarketScreen screen) {
        center();
        Skin skin = game.skin;
        this.game = game;

        this.casinoScreen = new CasinoScreen(this.game, screen);
        this.marketScreen = new ResourceMarketScreen(this.game, screen);
        this.auctionScreen = new AuctionScreen(this.game, screen);

        // Create UI Components
        final TextButton casinoBtn = new TextButton("To the casino", skin);
        TextButton marketBtn = new TextButton("To the shopfront", skin);
        TextButton auctionBtn = new TextButton("To the auction house", skin);
        TextButton nextBtn = new TextButton("Next ->", skin);

        casinoBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Go to the casino screen.
                casinoScreen.prepare();
                game.setScreen(casinoScreen);
            }
        });

        marketBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Go to the market screen.
                marketScreen.prepare();
                game.setScreen(marketScreen);

            }
        });

        auctionBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Go to the auction screen.
                game.setScreen(auctionScreen);
            }
        });

        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.nextPhase();
            }
        });

        center();

        add(casinoBtn);
        row();
        add(auctionBtn);
        row();
        add(marketBtn);
        row();
        add(nextBtn);
        row();
    }

}