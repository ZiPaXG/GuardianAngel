package com.guardianangel;

import com.badlogic.gdx.Game;
import com.guardianangel.screens.MainMenuScreen;

public class Main extends Game {
    static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void create() {
        instance = this;
        this.setScreen(new MainMenuScreen());
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
