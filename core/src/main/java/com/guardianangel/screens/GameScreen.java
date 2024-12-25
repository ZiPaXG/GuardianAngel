package com.guardianangel.screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.guardianangel.components.*;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.entities.WalkerEntity;
import com.guardianangel.entities.weapons.Pistol;
import com.guardianangel.entities.weapons.Rifle;
import com.guardianangel.entities.weapons.Weapon;
import com.guardianangel.systems.*;
import com.guardianangel.utils.CameraController;
import com.guardianangel.utils.FontManager;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Engine engine;
    private HUDSystem hudSystem;
    private CameraController cameraController;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private PlayerEntity player;
    private CrosshairSystem crosshairSystem;
    private AttackSystem attackSystem;
    private RayHandler rayHandler;
    private Animation<Texture> rainAnimation;
    private float rainAnimationTime;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Music ambientMusic;
    private float lightningMinInterval = 6f;
    private float lightningMaxInterval = 7f;
    private Music thunderSound;
    private boolean lightningEnabled = true;

    private ArrayList<Float> getSaveZoneX(TiledMap map) {
        MapObjects objects = map.getLayers().get("Objects").getObjects();
        ArrayList<Float> rect_x = new ArrayList<>();
        for (MapObject object : objects) {
            if ("SaveZone".equals(object.getName())) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    rect_x.add(rect.x);
                }
            }
        }
        return rect_x;
    }
    private void loadRainAnimation() {
        Array<Texture> rainFrames = new Array<>();
        int frameCount = 12; // Количество кадров в анимации дождя

        for (int i = 1; i <= frameCount; i++) {
            rainFrames.add(new Texture(Gdx.files.internal("Rain/"+i+".gif")));
        }

        rainAnimation = new Animation<>(0.03f, rainFrames, Animation.PlayMode.LOOP); // 0.1f — длительность кадра
        rainAnimationTime = 0f;
    }
    private void renderRainAnimation(SpriteBatch batch, OrthographicCamera camera) {
        rainAnimationTime += Gdx.graphics.getDeltaTime(); // Обновляем время анимации

        // Получаем текущий кадр анимации
        Texture currentFrame = rainAnimation.getKeyFrame(rainAnimationTime);

        // Отрисовываем анимацию поверх всего экрана
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(currentFrame,
            camera.position.x - camera.viewportWidth / 2,
            camera.position.y - camera.viewportHeight / 2,
            camera.viewportWidth,
            camera.viewportHeight);
        batch.end();
    }
    private void createLightningEffect() {
        if (!lightningEnabled) return;
        // Уровни освещения для эффекта молнии
        float[] lightningSequence = {1f, 0.8f, 0.9f, 0.6f};
        // Задержки между изменениями уровня освещения
        float[] delays = {0.1f, 0.1f, 0.1f}; // Все этапы длятся по 0.1 секунды

        // Последовательно применяем уровни освещения с таймерами
        float cumulativeDelay = 0f; // Суммарная задержка для каждого этапа
        for (int i = 0; i < lightningSequence.length; i++) {
            final float lightLevel = lightningSequence[i];
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    rayHandler.setAmbientLight(lightLevel);
                }
            }, cumulativeDelay);

            // Добавляем текущую задержку к общей
            if (i < delays.length) {
                cumulativeDelay += delays[i];
            }
        }

        // Воспроизводим звук грома при молнии
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                thunderSound.play();
            }
        }, cumulativeDelay); // Звук срабатывает в тот же момент, когда молния появляется

        // Планируем следующую молнию
        scheduleNextLightning();
    }
    private void scheduleNextLightning() {
        float nextInterval = lightningMinInterval + (float) Math.random() * (lightningMaxInterval - lightningMinInterval);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                createLightningEffect();
            }
        }, nextInterval);
    }
    public void createEntities() {
        ArrayList<Float> saveZoneX = getSaveZoneX(map);

        MapObject walkerObj = map.getLayers().get("Entities").getObjects().get("Walker");
        if (walkerObj instanceof RectangleMapObject) {
            Rectangle rect = ((RectangleMapObject) walkerObj).getRectangle();
            float scale = 2.5f;

            float walkerX = rect.x * scale;
            float walkerY = rect.y * scale;

            WalkerEntity walker = new WalkerEntity(walkerX, walkerY, 50);

            PathComponent path = walker.getComponent(PathComponent.class);
            for (float saveZone : saveZoneX) {
                path.path.add(new Vector2(saveZone * scale - walkerX, walkerY));
            }

            engine.addEntity(walker);
        } else {
            System.err.println("Walker не RectangleMapObject!!!");
        }

    }

    @Override
    public void show() {
        FontManager.loadFonts();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        engine = new Engine();

        // Загружаем музыку
        ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Rain.wav"));
        ambientMusic.setLooping(true); // Включаем зацикливание
        ambientMusic.setVolume(0.5f); // Устанавливаем громкость (по желанию)
        ambientMusic.play(); // Запускаем музыку
        thunderSound = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Grom.wav"));
        thunderSound.setVolume(0.5f); // Можно регулировать громкость
        thunderSound.setLooping(false);
        player = new PlayerEntity(new Weapon[]{new Pistol(), new Rifle()}, new int[] {24, 60});
        hudSystem = new HUDSystem(player);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        cameraController = new CameraController(camera, 5f);

        background = new Texture("ModernWorld/Sidescroller Shooter - Central City/Background/Base Color.png");

        map = new TmxMapLoader().load("ModernWorld/Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 2.5f);
        mapRenderer.setView(camera);

        rayHandler = new RayHandler(null); // null, если вы не используете Box2D World
        rayHandler.setAmbientLight(0.6f); // Установка общей освещенности сцены
        rayHandler.setBlurNum(3);         // Мягкость тени
        rayHandler.setCombinedMatrix(camera);

        crosshairSystem = new CrosshairSystem(camera, player.getCurrentWeapon());
        engine.addSystem(new EnemySystem(cameraController.getCamera()));
        EnemySpawnSystem spawnSystem = new EnemySpawnSystem(engine, cameraController.getCamera());
        engine.addSystem(spawnSystem);
        engine.addSystem(crosshairSystem);
        engine.addSystem(new ReloadSystem(player));
        attackSystem = new AttackSystem(player.getCurrentWeapon(), player, camera, rayHandler);
        engine.addSystem(attackSystem);
        engine.addSystem(new PathFollowerSystem(spawnSystem, attackSystem));
        engine.addSystem(new HUDSystem(player));
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(hudSystem);
        createEntities();
        scheduleNextLightning();
        loadRainAnimation();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cameraController.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            cameraController.shiftRight();
        }

        batch.setProjectionMatrix(cameraController.getCamera().combined);
        batch.begin();
        batch.draw(background,
            cameraController.getCamera().position.x - cameraController.getCamera().viewportWidth / 2,
            cameraController.getCamera().position.y - cameraController.getCamera().viewportHeight / 2,
            cameraController.getCamera().viewportWidth,
            cameraController.getCamera().viewportHeight);
        batch.end();

        mapRenderer.setView(cameraController.getCamera());
        mapRenderer.render();
        renderRainAnimation(batch, cameraController.getCamera());
        shapeRenderer.setProjectionMatrix(cameraController.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Entity entity : engine.getEntitiesFor(Family.all(CollisionComponent.class, PositionComponent.class).get())) {

            PositionComponent position = entity.getComponent(PositionComponent.class);
            CollisionComponent collision = entity.getComponent(CollisionComponent.class);

            if (entity.getComponent(WalkerTagComponent.class) != null) {
                collision.bounds.setPosition(position.x + 5, position.y);
            }
            else if (entity.getComponent(EnemyTagComponent.class) != null) {
                collision.bounds.setPosition(position.x - collision.bounds.width - 10, position.y);
            }

            shapeRenderer.setColor(1, 0, 0, 1);
            //shapeRenderer.rect(collision.bounds.x, collision.bounds.y, collision.bounds.width, collision.bounds.height);
        }

        shapeRenderer.end();
        engine.update(delta);
        rayHandler.updateAndRender();
        var players = engine.getEntitiesFor(Family.all(WalkerTagComponent.class).get());
        if (players.size() > 0)
        {
            Entity player = players.get(0);
            PositionComponent position = player.getComponent(PositionComponent.class);
            if (position.x >= cameraController.getCamera().position.x + cameraController.getCamera().viewportWidth / 2) {
                cameraController.shiftRight();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (!player.getCurrentWeapon().isReloading()) {
                player.switchWeapon(0);
                crosshairSystem.changeCrosshair(player.getCurrentWeapon());
                attackSystem.changeWeapon(player.getCurrentWeapon());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (!player.getCurrentWeapon().isReloading()) {
                player.switchWeapon(1);
                crosshairSystem.changeCrosshair(player.getCurrentWeapon());
                attackSystem.changeWeapon(player.getCurrentWeapon());
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        cameraController.getCamera().viewportWidth = width;
        cameraController.getCamera().viewportHeight = height;
        cameraController.getCamera().update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        lightningEnabled = false;
        ambientMusic.stop();
        thunderSound.stop();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        hudSystem.dispose();
        map.dispose();
        mapRenderer.dispose();
        ambientMusic.dispose();
        thunderSound.dispose(); // Останавливаем гром и освобождаем ресурсы
        if (rayHandler != null) {
            rayHandler.dispose();
        }
    }

}
