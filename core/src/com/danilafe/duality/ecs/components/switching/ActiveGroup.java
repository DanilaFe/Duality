package com.danilafe.duality.ecs.components.switching;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ActiveGroup implements Component, Pool.Poolable {

    public boolean active;
    public int switchId = 0;

    @Override
    public void reset() {
        active = false;
        switchId = 0;
    }

}
