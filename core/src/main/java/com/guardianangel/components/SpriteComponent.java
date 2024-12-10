package com.guardianangel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent implements Component {
    public Animation<TextureRegion> animation;
    public float stateTime = 0f;
    private float scale = 3f;

    public SpriteComponent(Texture spriteSheet, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / frameCount, spriteSheet.getHeight());
        for (int i = 0; i < frameCount; i++) {
            frames[i] = tmp[0][i];
        }
        this.animation = new Animation<>(frameDuration, frames);
    }

    public void render(SpriteBatch batch, float x, float y) {
        float width = animation.getKeyFrame(stateTime, true).getRegionWidth() * scale;
        float height = animation.getKeyFrame(stateTime, true).getRegionHeight() * scale;

        batch.draw(animation.getKeyFrame(stateTime, true), x, y, width, height);
    }
}
