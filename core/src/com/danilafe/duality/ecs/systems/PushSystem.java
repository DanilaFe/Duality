package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;
import com.danilafe.duality.Constants;
import com.danilafe.duality.ecs.components.physics.*;
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
            Pushable pushableComponent = pushable.getComponent(Pushable.class);

            pushableVelocity.velocity.x = 0;
            float futureLeft = pushableBox.box.x;
            float futureRight = futureLeft + pushableBox.box.width;
            float futureBottom = pushableBox.box.y;
            float futureTop = futureBottom + pushableBox.box.height;

            Array<Entity> entitiesPushingLeft = new Array<>();
            Array<Entity> entitiesPushingRight = new Array<>();
            for(Entity pushing : entitiesB){
                CollisionBox pushingBox = pushing.getComponent(CollisionBox.class);
                Velocity pushingVelocity = pushing.getComponent(Velocity.class);

                float pushingLeft = pushingBox.box.x + pushingVelocity.velocity.x * deltaTime;
                float pushingRight = pushingLeft + pushingBox.box.width;
                float pushingBottom = pushingBox.box.y + pushingVelocity.velocity.y * deltaTime;
                float pushingTop = pushingBottom + pushingBox.box.height;

                if(!(pushableBox.box.x + pushableBox.box.width < pushingBox.box.x ||
                        pushableBox.box.x > pushingBox.box.x + pushingBox.box.width) &&
                        !(pushableComponent.entities.containsKey(pushing) &&
                                pushableComponent.entities.get(pushing) / pushingVelocity.velocity.x > 0)) continue;
                if(pushingBottom >= futureTop || pushingTop <= futureBottom || pushingLeft > futureRight ||
                        pushingRight < futureLeft) continue;

                if(pushingVelocity.velocity.x < 0 && pushingLeft <= futureRight) {
                    entitiesPushingLeft.add(pushing);
                    pushableVelocity.velocity.x += pushingVelocity.velocity.x;
                } else if(pushingVelocity.velocity.x > 0 && pushingRight >= futureLeft){
                    entitiesPushingRight.add(pushing);
                    pushableVelocity.velocity.x += pushingVelocity.velocity.x;
                }
            }

            pushableComponent.entities.clear();
            pushableVelocity.velocity.x *= Constants.PUSH_DECREASE;
            for(Entity entity : entitiesPushingLeft){
                entity.getComponent(Velocity.class).velocity.x = pushableVelocity.velocity.x;
                entity.getComponent(Position.class).position.x =
                        pushableBox.box.x + pushableBox.box.width + entity.getComponent(CollisionBox.class).box.width / 2;
                pushableComponent.entities.put(entity, -1.f);
            }
            for(Entity entity : entitiesPushingRight){
                entity.getComponent(Velocity.class).velocity.x = pushableVelocity.velocity.x;
                entity.getComponent(Position.class).position.x =
                        pushableBox.box.x - entity.getComponent(CollisionBox.class).box.width/ 2;
                pushableComponent.entities.put(entity, 1.f);
            }
        }
    }
}
