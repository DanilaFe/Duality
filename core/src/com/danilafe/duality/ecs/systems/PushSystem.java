package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.systems.util.DualSystem;

public class PushSystem extends DualSystem {

    public PushSystem() {
        super(Family.all(Pushable.class, CollisionBox.class, Velocity.class).get(),
                Family.all(Pushing.class, CollisionBox.class, Velocity.class, Position.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity pushable : entitiesA){
            CollisionBox pushableBox = pushable.getComponent(CollisionBox.class);
            Velocity pushableVelocity = pushable.getComponent(Velocity.class);

            for(Entity pushing : entitiesB){
                CollisionBox pushingBox = pushing.getComponent(CollisionBox.class);
                Velocity pushingVelocity = pushing.getComponent(Velocity.class);
                Position pushingPosition = pushing.getComponent(Position.class);
            }
        }
    }
}
