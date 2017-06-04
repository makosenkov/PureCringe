package com.purecringegame;

import com.badlogic.gdx.ApplicationAdapter;
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

import static com.purecringegame.utils.Const.ppm;

public class PureCringe extends ApplicationAdapter {

    private OrthographicCamera camera;
    private Box2DDebugRenderer b2dr;
    private World world;

    private NewContactListener contactListener;
    private Body player;
    private Body startBox;
    private float time;


    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / 2, h / 2);

        world = new World(new Vector2(0, -9.8f), false);
        contactListener = new NewContactListener();
        world.setContactListener(contactListener);
        b2dr = new Box2DDebugRenderer();

        player = createBox(8, 10, 3, 10, false, "PLAYER");
        startBox = createBox(8, 0, 64, 16, true, "BOX");

        time = -3;
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0.5f, 0, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.scl(ppm));
        if (contactListener.youLose) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 2, height / 2);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        time += delta;
        System.out.println(time);
        spawnBox(delta);
        inputUpdate(delta);
        cameraUpdate(delta);


    }

    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getPosition().x * ppm > -150) {
            horizontalForce = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getPosition().x * ppm < 150) {
            horizontalForce = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.applyForceToCenter(0, 300, false);
        }
        player.setLinearVelocity(horizontalForce * 10, player.getLinearVelocity().y);
        if (player.getLinearVelocity().y < -10) player.setLinearVelocity(player.getLinearVelocity().x, -10);
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = player.getPosition().x * ppm;
        position.y = player.getPosition().y * ppm;
        camera.position.set(position);

        camera.update();
    }

    public void spawnBox(float delta) {
        if (time > 0.6f) {
            createBox(MathUtils.random(-150, 150), (player.getPosition().y * ppm - 600), 5, 500, true, "OBSTACLE");
            time = 0;
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
}

