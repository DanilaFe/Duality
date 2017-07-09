package com.danilafe.duality.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;

public class ControlData {

    private int leftKey;
    private int rightKey;
    private int jumpKey;
    private int interactKey;
    private Controller controller;

    public ControlData(int left, int right, int jump, int interact, Controller cnt) {
        leftKey = left;
        rightKey = right;
        jumpKey = jump;
        interactKey = interact;
        controller = cnt;
    }

    public ControlData() {
        this(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE, Input.Keys.UP, null);
    }

    public float horizontalAccel() {
        return (Gdx.input.isKeyPressed(leftKey) ? -1 : 0) + (Gdx.input.isKeyPressed(rightKey) ? 1 : 0);
    }

    public boolean jumpPressed() {
        return Gdx.input.isKeyPressed(jumpKey);
    }

    public boolean interactPressed() {
        return Gdx.input.isKeyPressed(interactKey);
    }

}
