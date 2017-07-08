package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import controls.ControlData;

public class Player implements Component, Pool.Poolable {

    public ControlData controlData;

    @Override
    public void reset() {
        controlData = null;
    }
}
