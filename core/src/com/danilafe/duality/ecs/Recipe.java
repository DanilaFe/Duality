package com.danilafe.duality.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.components.CollisionBox;
import com.danilafe.duality.ecs.components.Position;

public abstract class Recipe {


    public abstract Entity create(PooledEngine engine, ResourceManager resources, float x, float y);

    protected Position createPosition(PooledEngine engine, float x, float y) {
        Position position = engine.createComponent(Position.class);
        position.position.set(x, y);
        return position;
    }
    protected CollisionBox createCollisionBox(PooledEngine engine, float x, float y, float width, float height){
        CollisionBox collisionBox = engine.createComponent(CollisionBox.class);
        collisionBox.box.setSize(width, height);
        collisionBox.box.setCenter(x, y);
        return collisionBox;
    }
}
