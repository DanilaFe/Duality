package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.danilafe.duality.ecs.components.Acceleration;
import com.danilafe.duality.ecs.components.Position;
import com.danilafe.duality.ecs.components.Velocity;

public class PhysicsSystem extends IteratingSystem {

    public PhysicsSystem(){
        super(Family.all(Velocity.class).one(Acceleration.class, Position.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position pos = entity.getComponent(Position.class);
        Acceleration acc = entity.getComponent(Acceleration.class);
        Velocity vel = entity.getComponent(Velocity.class);
        if(acc != null) vel.velocity.add(new Vector2(acc.acceleration).scl(deltaTime));
        if(pos != null) pos.position.add(new Vector2(vel.velocity).scl(deltaTime));
    }
}
