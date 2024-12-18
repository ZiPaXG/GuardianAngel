package com.guardianangel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent implements Component {
    public Animation<TextureRegion> currentAnimation;
    public Animation<TextureRegion> idleAnimation;
    public Animation<TextureRegion> runAnimation;
    public Animation<TextureRegion> hurtAnimation;
    public Animation<TextureRegion> deathAnimation;

    public boolean isHurtOrDead = false;

    public float stateTime = 0f;
    private float scale = 3f;

    public SpriteComponent(Texture idleSpriteSheet, int idleFrameCount, float idleFrameDuration,
                           Texture runSpriteSheet, int runFrameCount, float runFrameDuration,
                           Texture hurtSpriteSheet, int hurtFrameCount, float hurtFrameDuration,
                           Texture deathSpriteSheet, int deathFrameCount, float deathFrameDuration) {
        this.idleAnimation = createAnimation(idleSpriteSheet, idleFrameCount, idleFrameDuration, true);
        this.runAnimation = createAnimation(runSpriteSheet, runFrameCount, runFrameDuration, true);
        this.hurtAnimation = createAnimation(hurtSpriteSheet, hurtFrameCount, hurtFrameDuration, false);
        this.deathAnimation = createAnimation(deathSpriteSheet, deathFrameCount, deathFrameDuration, false);
        this.currentAnimation = idleAnimation;
    }


    private Animation<TextureRegion> createAnimation(Texture spriteSheet, int frameCount, float frameDuration, boolean loop) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / frameCount, spriteSheet.getHeight());
        for (int i = 0; i < frameCount; i++) {
            frames[i] = tmp[0][i];
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        return animation;
    }


    public void setAnimation(Animation<TextureRegion> animation) {
        if (this.currentAnimation != animation) {
            this.currentAnimation = animation;

            this.stateTime = 0f;
        }
    }

    public void render(SpriteBatch batch, float x, float y) {
        float width = currentAnimation.getKeyFrame(stateTime, true).getRegionWidth() * scale;
        float height = currentAnimation.getKeyFrame(stateTime, true).getRegionHeight() * scale;

        batch.draw(currentAnimation.getKeyFrame(stateTime, true), x, y, width, height);
    }
}
