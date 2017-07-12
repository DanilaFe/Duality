package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.danilafe.duality.Constants;
import com.danilafe.duality.ecs.components.Following;
import com.danilafe.duality.ecs.components.Position;

public class FollowingSystem extends IteratingSystem {

    public FollowingSystem() {
        super(Family.all(Following.class, Position.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position pos = entity.getComponent(Position.class);
        Following fol = entity.getComponent(Following.class);

        if (fol.otherEntity == null) return;
        Position otherPosition = fol.otherEntity.getComponent(Position.class);
        if (fol.immediate) {
            pos.position.set(otherPosition.position);
        } else {
            Vector2 displacement = new Vector2(otherPosition.position).sub(pos.position);
            float distance = displacement.len();
            float maxSpeed = fol.maxMove * Following.CURVE.apply(distance / Constants.POSITION_DEPENDENCE) * deltaTime;
            float actualMove = (distance < maxSpeed) ? distance : maxSpeed;

            displacement.setLength(actualMove);
            pos.position.add(displacement);
        }
    }

}
