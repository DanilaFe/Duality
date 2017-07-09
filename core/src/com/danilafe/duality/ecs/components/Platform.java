package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class Platform implements Component, Pool.Poolable {

    public boolean oneWay;

    @Override
    public void reset() {
        oneWay = false;
    }

}
