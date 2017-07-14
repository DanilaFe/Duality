package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.EntityUtils;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.systems.util.DualSystem;
import javafx.geometry.Pos;

public class PlatformSystem extends DualSystem{

    public PlatformSystem() {
        super(Family.all(Platform.class, CollisionBox.class).get(),
                Family.all(Velocity.class, Position.class, CollisionBox.class, PlatformWalker.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity walkerEntity: entitiesB){
            PlatformWalker walker = walkerEntity.getComponent(PlatformWalker.class);
            Velocity velocity = walkerEntity.getComponent(Velocity.class);
            CollisionBox collisionBox  = walkerEntity.getComponent(CollisionBox.class);

            float leftEdge = collisionBox.box.x + velocity.velocity.x * deltaTime;
            float rightEdge = leftEdge + collisionBox.box.width;
            float bottomEdge = collisionBox.box.y + velocity.velocity.y * deltaTime;

            for(Entity platformEntity : entitiesA){
                Platform platform = platformEntity.getComponent(Platform.class);
                CollisionBox platformBox = platformEntity.getComponent(CollisionBox.class);

                float platformLeft = platformBox.box.x;
                float platformRight = platformBox.box.x + platformBox.box.width;
                float platformBottom = platformBox.box.y;
                float platformTop = platformBox.box.y + platformBox.box.height;

                if(!(rightEdge <= platformLeft|| leftEdge >= platformRight) &&
                        (platform.oneWay || walker.solid) && velocity.velocity.y <= 0 &&
                        collisionBox.box.y >= platformTop &&
                        (bottomEdge < platformTop && bottomEdge > platformBottom)){
                    velocity.velocity.y = 0;
                    walkerEntity.getComponent(Position.class).position.y = platformTop + collisionBox.box.height / 2;
                }
            }
        }
    }
}
