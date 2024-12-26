package com.guardianangel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.guardianangel.entities.EnemyEntity;

import java.util.Random;

public class EnemySpawnSystem extends EntitySystem {
    private float spawnTimer = 0f;
    private float spawnInterval = 1f;
    private Engine engine;
    private Random random;
    private OrthographicCamera camera;

    private int maxEnemiesOnWave = 10;
    private int enemiesSpawned = 0;
    private int waves = 3;
    public EnemySpawnSystem(Engine engine, OrthographicCamera camera) {
        this.engine = engine;
        this.camera = camera;
        this.random = new Random();
    }

    @Override
    public void update(float deltaTime) {
        spawnTimer += deltaTime;

        if (enemiesSpawned < maxEnemiesOnWave && spawnTimer >= spawnInterval) {
            spawnTimer = 0f;

            float spawnX = camera.position.x + camera.viewportWidth / 2 + random.nextInt(10, 150);

            int enemyType = random.nextBoolean() ? 1 : 2;

            EnemyEntity enemy = new EnemyEntity(spawnX, enemyType);
            engine.addEntity(enemy);

            enemiesSpawned++;
        }
    }

    public int getMaxEnemiesOnWave() {
        return maxEnemiesOnWave;
    }

    public void resetWave() {
        enemiesSpawned = 0;
    }
    public int getWaves() {
        return waves;
    }
    public void decreaseWaves() {
        waves--;
    }
}
