package com.purecringegame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.purecringegame.utils.NewContactListener;

import java.util.ArrayList;

import static com.purecringegame.utils.Const.ppm;

public class PureCringe implements com.badlogic.gdx.Screen {

    private Game game;

    private OrthographicCamera camera;
    private Box2DDebugRenderer b2dr;
    private World world;

    private NewContactListener contactListener;
    private Body player;
    private Body startBox;
    private Body disposer;
    private float obstacleTime, speedTime, obstTimeLimit;
    private float speed;

    private SpriteBatch batch;
    private Texture texture, obstacleTex, platformTex;

    private ArrayList<Body> obstacleList;

    public PureCringe(Game game) {
        this.game = game;
    }


    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

        world = new World(new Vector2(0, -9.8f), false);
        contactListener = new NewContactListener();
        world.setContactListener(contactListener);
        b2dr = new Box2DDebugRenderer();

        speed = -5;

        player = createBox(8, 10, 32, 32, false, "PLAYER");
        startBox = createBox(8, 0, 64, 16, true, "BOX");
        disposer = createDisposer();

        batch = new SpriteBatch();
        texture = new Texture("player1.png");
        obstacleTex = new Texture("obstacle.png");
        platformTex = new Texture("platform.png");

        obstacleList = new ArrayList<>();
        speedTime = -2;
        obstacleTime = -2;
        obstTimeLimit = 0.1f;
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0.5f, 0, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //b2dr.render(world, camera.combined.scl(ppm));

        batch.begin();
        batch.draw(platformTex, startBox.getPosition().x * ppm + 234 - texture.getWidth()/2,
                startBox.getPosition().y * ppm + 626 + (- player.getPosition().y) * ppm - texture.getHeight()/2);
        batch.draw(texture, player.getPosition().x * ppm + 250 - texture.getWidth()/2, 620 - texture.getHeight()/2);
        for (Body body: obstacleList) {
            batch.draw(obstacleTex, body.getPosition().x * ppm + 242, (body.getPosition().y - player.getPosition().y) * ppm + 612);
        }
        batch.end();

        if (contactListener.youLose) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        updateDisposer(delta);
        inputUpdate(delta);
        cameraUpdate(delta);
        gameplayUpdate(delta);
        if (!obstacleList.isEmpty()) System.out.println(obstacleList.get(0).getPosition().x + " " + obstacleList.get(0).getPosition().y);
    }

    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getPosition().x * ppm > -234) {
            horizontalForce = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getPosition().x * ppm < 234) {
            horizontalForce = 1;
        }
        player.setLinearVelocity(horizontalForce * 10, player.getLinearVelocity().y);
        if (player.getLinearVelocity().y < speed) player.setLinearVelocity(player.getLinearVelocity().x, speed);
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = 0;
        position.y = player.getPosition().y * ppm - 220;
        camera.position.set(position);

        camera.update();
    }

    public void gameplayUpdate(float delta) {
        obstTimeLimit = (6/-speed);

        if (contactListener.gameStarted) {
            obstacleTime += delta;
            speedTime += delta;

            if (speedTime > 0.4f && speed > -25) {
                speed -= 0.15f;
                System.out.println(speed);
                speedTime = 0;
            }

            if (obstacleTime > obstTimeLimit) {
                createObstacle();
                obstacleTime = 0;
            }
        }

        if (contactListener.disposeObstacle) {
            System.out.println("trying to dispose");
            disposeObstacle();
            contactListener.disposeObstacle = false;
        }
    }

    public Body createBox(float x, float y, int width, int height, boolean isStatic, String key) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic) def.type = BodyDef.BodyType.StaticBody;
        else def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / ppm, y / ppm);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / ppm, height / 2 / ppm);
        pBody.createFixture(shape, 1.0f).setUserData(key);
        shape.dispose();
        return pBody;
    }

    public void createObstacle() {
        Body oBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(MathUtils.random(-242, 242) / ppm, (player.getPosition().y * ppm - 700) / ppm);
        def.fixedRotation = true;
        oBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / ppm, 8 / ppm);
        oBody.createFixture(shape, 1.0f).setUserData("OBSTACLE");
        shape.dispose();

        obstacleList.add(oBody);
    }

    public Body createDisposer() {
        Body dBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(250 / ppm, 100 / ppm);
        def.fixedRotation = true;
        dBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(500 / ppm, 10/ ppm);
        dBody.createFixture(shape, 1.0f).setUserData("DISPOSER");
        shape.dispose();
        return dBody;
    }

    public void updateDisposer(float delta){
        disposer.setTransform(disposer.getPosition().x, player.getPosition().y + 8, disposer.getAngle());
    }

    public void disposeObstacle(){
        world.destroyBody(obstacleList.get(0));
        obstacleList.remove(0);
        System.out.println("disposed");
    }
}

