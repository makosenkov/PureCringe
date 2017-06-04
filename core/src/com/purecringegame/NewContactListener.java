package com.purecringegame;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class NewContactListener implements ContactListener {

    public boolean youLose = false;

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getUserData().equals("PLAYER") && contact.getFixtureB().getUserData().equals("OBSTACLE")){
            youLose = true;
        }

        if (contact.getFixtureA().getUserData().equals("OBSTACLE") && contact.getFixtureB().getUserData().equals("PLAYER")){
            youLose = true;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
