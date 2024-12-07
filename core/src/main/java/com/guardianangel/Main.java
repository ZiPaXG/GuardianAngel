package com.guardianangel;

import com.badlogic.gdx.Game;
import com.guardianangel.screens.GameScreen;

public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
