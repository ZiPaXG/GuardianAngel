package com.guardianangel.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraController {
    private final OrthographicCamera camera;
    private float targetX;
    private final float moveSpeed;

    public CameraController(OrthographicCamera camera, float initialSpeed) {
        this.camera = camera;
        this.moveSpeed = initialSpeed;
        this.targetX = camera.position.x;
    }

    public void update(float delta) {
        if (Math.abs(camera.position.x - targetX) > 0.1f) {
            camera.position.x += (targetX - camera.position.x) * Math.min(delta * moveSpeed, 1f);
            camera.update();
        }
    }

    public void shiftRight() {
        targetX += camera.viewportWidth;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
