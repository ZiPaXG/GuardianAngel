package com.guardianangel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.guardianangel.Main;

class AuthorsScreen implements Screen {
    private final Stage stage;
    private final Viewport viewport;

    public AuthorsScreen() {
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = SkinFactory.createSkin();

        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        String authorsText = Gdx.files.internal("Authors.txt").readString();
        Label authorsLabel = new Label(authorsText, labelStyle);
        authorsLabel.setWrap(true);

        com.badlogic.gdx.scenes.scene2d.ui.Table table = new com.badlogic.gdx.scenes.scene2d.ui.Table();
        table.center();
        table.setFillParent(true);

        table.add(authorsLabel).width(Gdx.graphics.getWidth() * 0.8f).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().changeScreen(new MainMenuScreen());
            }
        });

        table.add(backButton).width(300).height(80).padTop(20);
        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
}
