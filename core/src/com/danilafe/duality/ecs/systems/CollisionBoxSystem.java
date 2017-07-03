package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.ecs.components.CollisionBox;
import com.danilafe.duality.ecs.components.Position;

public class CollisionBoxSystem extends IteratingSystem {

    public CollisionBoxSystem(){
        super(Family.all(CollisionBox.class, Position.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position pos = entity.getComponent(Position.class);
        CollisionBox box = entity.getComponent(CollisionBox.class);
        box.box.setCenter(pos.position);
    }

}
