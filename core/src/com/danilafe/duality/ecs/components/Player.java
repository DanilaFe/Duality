package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Pool;

public class Player implements Component, Pool.Poolable {

    public int leftKey = Input.Keys.LEFT;
    public int rightKey = Input.Keys.RIGHT;
    public int jumpKey = Input.Keys.SPACE;
    public Controller controller = null;

    @Override
    public void reset() {
        leftKey = Input.Keys.LEFT;
        rightKey = Input.Keys.RIGHT;
        jumpKey = Input.Keys.SPACE;
        controller = null;
    }
}
