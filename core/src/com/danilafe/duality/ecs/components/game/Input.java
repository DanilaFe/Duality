package com.danilafe.duality.ecs.components.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.danilafe.duality.controls.ControlData;

public class Input implements Component, Pool.Poolable {

    public ControlData controlData;

    @Override
    public void reset() {
        controlData = null;
    }

}
