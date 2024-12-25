package com.guardianangel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public class WinScreen implements Screen {
    private final Stage stage;
    private final Viewport viewport;
    private Music winMusic;

    public WinScreen() {
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = SkinFactory.createSkin();

        Label.LabelStyle titleStyle = skin.get(Label.LabelStyle.class);
        Label gameOverLabel = new Label("Win", titleStyle);
        gameOverLabel.setFontScale(2f);

        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().changeScreen(new GameScreen());
            }
        });

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        com.badlogic.gdx.scenes.scene2d.ui.Table table = new com.badlogic.gdx.scenes.scene2d.ui.Table();
        table.center();
        table.setFillParent(true);

        table.add(gameOverLabel).padBottom(50).row();
        table.add(retryButton).width(300).height(80).padBottom(20).row();
        table.add(exitButton).width(300).height(80).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        winMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Win.wav"));
        winMusic.setLooping(false); // Включаем зацикливание
        winMusic.setVolume(0.5f); // Устанавливаем громкость (по желанию)
        winMusic.play();
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
        winMusic.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
