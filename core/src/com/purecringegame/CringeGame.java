package com.purecringegame;

import com.badlogic.gdx.Game;
import com.purecringegame.Screens.PureCringeMenu;

public class CringeGame extends Game {

    @Override
    public void create() {
        this.setScreen(new PureCringeMenu(this));
    }
}