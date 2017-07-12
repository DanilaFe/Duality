package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.danilafe.duality.ecs.components.Acceleration;
import com.danilafe.duality.ecs.components.Velocity;

public class AccelerationSystem extends IteratingSystem {

    public AccelerationSystem(){
        super(Family.all(Velocity.class, Acceleration.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entity.getComponent(Velocity.class).velocity
                .add(new Vector2(entity.getComponent(Acceleration.class).acceleration).scl(deltaTime));
    }
}
