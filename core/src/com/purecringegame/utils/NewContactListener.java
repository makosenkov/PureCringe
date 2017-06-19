package com.purecringegame.utils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class NewContactListener implements ContactListener {

    public boolean youLose = false;
    public boolean gameStarted = false;
    public boolean disposeObstacle = false;

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getUserData().equals("PLAYER") && contact.getFixtureB().getUserData().equals("OBSTACLE")){
            youLose = true;
        }
        if (contact.getFixtureA().getUserData().equals("OBSTACLE") && contact.getFixtureB().getUserData().equals("PLAYER")){
            youLose = true;
        }

        if (contact.getFixtureA().getUserData().equals("OBSTACLE") && contact.getFixtureB().getUserData().equals("DISPOSER")){
            disposeObstacle = true;
            System.out.println("collision detected");
        }
        if (contact.getFixtureA().getUserData().equals("DISPOSER") && contact.getFixtureB().getUserData().equals("OBSTACLE")){
            disposeObstacle = true;
            System.out.println("collision detected");
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getUserData().equals("PLAYER") && contact.getFixtureB().getUserData().equals("BOX")){
            gameStarted = true;
        }

        if (contact.getFixtureA().getUserData().equals("BOX") && contact.getFixtureB().getUserData().equals("PLAYER")){
            gameStarted = true;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
