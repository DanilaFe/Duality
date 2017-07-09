package com.danilafe.duality.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;

public class ControlData {

    private int leftKey;
    private int rightKey;
    private int jumpKey;
    private int interactKey;
    private int switchKey;

    private Controller controller;
    private int horizontalAxis;
    private int jumpButton;
    private int interactButton;
    private int switchButton;

    public ControlData(int left, int right, int jump, int interact, int swtch, Controller cnt, int hAxis, int jumpBtn, int interactBtn, int switchBtn) {
        leftKey = left;
        rightKey = right;
        jumpKey = jump;
        interactKey = interact;
        switchKey = swtch;
        controller = cnt;

        horizontalAxis = hAxis;
        jumpButton = jumpBtn;
        interactButton = interactBtn;
        switchButton = switchBtn;
    }

    public ControlData() {
        this(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE, Input.Keys.UP, Input.Keys.SHIFT_RIGHT, null, 0, 0, 1, 2);
    }

    public float horizontalAccel() {
        float keyboardAccel = (Gdx.input.isKeyPressed(leftKey) ? -1 : 0) + (Gdx.input.isKeyPressed(rightKey) ? 1 : 0);
        float controllerAccel = (controller == null) ? 0 : controller.getAxis(horizontalAxis);
        return Math.round((keyboardAccel + controllerAccel) * 10) / 10;
    }

    public boolean jumpPressed() {
        return Gdx.input.isKeyPressed(jumpKey) || (controller != null && controller.getButton(jumpButton));
    }

    public boolean interactPressed() {
        return Gdx.input.isKeyPressed(interactKey) || (controller != null && controller.getButton(interactButton));
    }

    public boolean switchPressed() {
        return Gdx.input.isKeyPressed(switchKey) || (controller != null && controller.getButton(switchButton));
    }

}
