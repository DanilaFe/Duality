package com.danilafe.duality.ecs.components.util;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.danilafe.duality.ecs.GameRunnable;

public class Runnable implements Component, Pool.Poolable {

    public GameRunnable runnable;

    @Override
    public void reset() {
        runnable = null;
    }

}
