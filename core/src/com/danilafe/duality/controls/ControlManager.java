package com.danilafe.duality.controls;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class ControlManager {

    private Array<ControlData> controls;
    private Array<Integer> groupKeys;

    public ControlManager() {
        loadControls();
    }

    private void loadControls() {
        controls = new Array<>();
        controls.add(new ControlData(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.SLASH, Input.Keys.SHIFT_RIGHT, null, 0, 0, 1, 2, 3));
        controls.add(new ControlData(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S, Input.Keys.E, Input.Keys.Q, null, 0, 0, 1, 2, 3));
    }

    public ControlData getNthControl(int id) {
        if (id < controls.size) return controls.get(id);
        else return controls.get(0);
    }

}
