package io.github.teamfractal.actors;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.github.teamfractal.Auction;
import io.github.teamfractal.MiniGame;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.NotEnoughMoneyException;
import io.github.teamfractal.exception.NotEnoughResourceException;
import io.github.teamfractal.screens.CasinoScreen;
import io.github.teamfractal.screens.MarketScreen;
import io.github.teamfractal.screens.ResourceMarketScreen;
import io.github.teamfractal.util.AuctionBid;
import io.github.teamfractal.util.AuctionableItem;

import javax.xml.soap.Text;

public class CasinoActors extends Table {
    private ResourceMarketActors resourceMarketActors;

    private Auction auction;
    private RoboticonQuest game;
    private MiniGame miniGame;

    private Label casinoTitleLbl;
    private TextField betAmountText;
    private TextButton placeBetBtn;
    private TextField resultAmountText;
    private TextField guessText;
    private Label instructionLblGuess;
    private TextButton returnButton;
    private Label playerStats;


    public CasinoActors(final RoboticonQuest game, CasinoScreen screen, final MarketScreen marketScreen) {
        center();
        Skin skin = game.skin;
        this.game = game;
        auction = game.auction;
        this.miniGame = new MiniGame();

        // Create UI Components
        playerStats = new Label("Your Resources\n\n\n\n\n", game.skin); //Pad out the initial string with new lines as the label width property does not correctly update
        casinoTitleLbl = new Label("Casino", skin);
        instructionLblGuess = new Label("Guess a number between 1 and 3. A bet costs 300", skin);
        placeBetBtn = new TextButton("Place Bet", skin);
        resultAmountText = new TextField("Your winnings will appear here", skin);
        guessText = new TextField("Type your guess here", skin);

        returnButton = new TextButton("Back to the Market Menu", skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(marketScreen);
            }
        });


        placeBetBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int guess = Integer.parseInt(guessText.getText());
                if(guess >= 1 && guess <= 3){
                        game.getPlayer().addMoney(-300);
                        int winningAmount = miniGame.getPrice(miniGame.WinGame(guess));
                        resultAmountText.setText("You won: " + Integer.toString(winningAmount));
                        game.getPlayer().addMoney(winningAmount);
                        widgetUpdate();
                }

            }
        });

        TextFieldFilter digitFilter = new TextFieldFilter() {
            public  boolean acceptChar(TextField textField, char c) {
                if (Character.isDigit(c))
                    return true;
                return false;
            }
        };

        resultAmountText.setTextFieldFilter(digitFilter);

        // Adjust properties.
        casinoTitleLbl.setAlignment(Align.left);

        add(playerStats);
        row();
        add(casinoTitleLbl);
        row();
        add(instructionLblGuess);
        row();
        add(guessText);
        row();
        add(placeBetBtn);
        row();
        add(resultAmountText);
        row();
        add(returnButton);
        row();
        pad(20);

        widgetUpdate();
    }

    public void widgetUpdate() {

        String statText = "Your resources:\n\n" +
                " Ore: "    + game.getPlayer().getOre()    + "\n" +
                " Energy: " + game.getPlayer().getEnergy() + "\n" +
                " Food: "   + game.getPlayer().getFood()   + "\n" +
                " Money: "  + game.getPlayer().getMoney()  + "\n" ;

        playerStats.setText(statText);

    }

}