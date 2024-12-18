package com.guardianangel.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.guardianangel.components.*;
import com.guardianangel.entities.EnemyEntity;
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

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;


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

        crosshairSystem = new CrosshairSystem(camera, player.getCurrentWeapon());
        engine.addSystem(new EnemySystem());
        EnemySpawnSystem spawnSystem = new EnemySpawnSystem(engine, cameraController.getCamera());
        engine.addSystem(spawnSystem);
        engine.addSystem(new PathFollowerSystem(spawnSystem));
        engine.addSystem(crosshairSystem);
        engine.addSystem(new ReloadSystem(player));
        attackSystem = new AttackSystem(player.getCurrentWeapon(), player);
        engine.addSystem(attackSystem);
        engine.addSystem(new HUDSystem(player));
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(hudSystem);
        createEntities();
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
            shapeRenderer.rect(collision.bounds.x, collision.bounds.y, collision.bounds.width, collision.bounds.height);
        }

        shapeRenderer.end();
        engine.update(delta);

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
            player.switchWeapon(0);
            crosshairSystem.changeCrosshair(player.getCurrentWeapon());
            attackSystem.changeWeapon(player.getCurrentWeapon());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            player.switchWeapon(1);
            crosshairSystem.changeCrosshair(player.getCurrentWeapon());
            attackSystem.changeWeapon(player.getCurrentWeapon());
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
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        hudSystem.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
