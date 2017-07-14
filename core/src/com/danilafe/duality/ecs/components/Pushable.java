package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

public class Pushable implements Component, Pool.Poolable {

    public ObjectMap<Entity, Float> entities = new ObjectMap<>();

    @Override
    public void reset() {
        entities.clear();
    }

}
