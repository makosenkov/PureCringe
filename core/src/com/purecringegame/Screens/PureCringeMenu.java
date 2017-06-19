package com.purecringegame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Created by Михаил on 19.06.2017.
 */
public class PureCringeMenu implements Screen {

    private Game game;

    public PureCringeMenu(Game rocketGame) {

        this.game = rocketGame;

    }

    @Override
    public void show() {

        game.setScreen(new PureCringe(game));

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
