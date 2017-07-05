package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.systems.util.DualSystem;

public class CollisionSystem extends DualSystem {

    public CollisionSystem() {
        super(Family.all(CollisionBox.class, Solid.class).get(),
                Family.all(CollisionBox.class, Position.class, Velocity.class, Colliding.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity firsEntity : entitiesB) {
            Velocity vel = firsEntity.getComponent(Velocity.class);
            CollisionBox box = firsEntity.getComponent(CollisionBox.class);
            Position pos = firsEntity.getComponent(Position.class);
            float leftEdge = vel.velocity.x * deltaTime + box.box.x;
            float rightEdge = vel.velocity.x * deltaTime + box.box.x + box.box.width;
            float bottomEdge = vel.velocity.y * deltaTime + box.box.y;
            float topEdge = vel.velocity.y * deltaTime + +box.box.y + box.box.height;

            for (Entity secondEntity : entitiesA) {
                CollisionBox otherBox = secondEntity.getComponent(CollisionBox.class);
                float otherLeft = otherBox.box.x;
                float otherRight = otherBox.box.x + otherBox.box.width;
                float otherBottom = otherBox.box.y;
                float otherTop = otherBox.box.y + otherBox.box.height;

                if (!(box.box.x >= otherRight || box.box.x + box.box.width <= otherLeft)) {
                    if (bottomEdge < otherTop && bottomEdge > otherBottom) {
                        pos.position.y = otherTop + box.box.height / 2;
                        vel.velocity.y = 0;
                    } else if (topEdge > otherBottom && topEdge < otherTop) {
                        pos.position.y = otherBottom - box.box.height / 2;
                        vel.velocity.y = 0;
                    }
                }

                if (!(box.box.y >= otherTop || box.box.y + box.box.height <= otherBottom)) {
                    if (leftEdge < otherRight && leftEdge > otherLeft) {
                        pos.position.x = otherRight + box.box.width / 2;
                        vel.velocity.x = 0;
                    } else if (rightEdge > otherLeft && rightEdge < otherRight) {
                        pos.position.x = otherLeft - box.box.width / 2;
                        vel.velocity.x = 0;
                    }
                }
            }
        }
    }
}
