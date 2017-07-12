package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.danilafe.duality.ecs.components.Position;
import com.danilafe.duality.ecs.components.Velocity;

public class PositionSystem extends IteratingSystem {

    public PositionSystem() {
        super(Family.all(Position.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entity.getComponent(Position.class).position
                .add(new Vector2(entity.getComponent(Velocity.class).velocity).scl(deltaTime));
    }

}
