package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Parent implements Component, Pool.Poolable {

    public Array<Entity> children = new Array<>();

    @Override
    public void reset() {
        children.clear();
    }

}
