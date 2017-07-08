package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.ecs.components.Average;
import com.danilafe.duality.ecs.components.Position;

public class AverageSystem extends IteratingSystem {

    public AverageSystem() {
        super(Family.all(Average.class, Position.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = entity.getComponent(Position.class);
        Average average = entity.getComponent(Average.class);

        if (average.entities.size != 0) {
            position.position.set(0, 0);
            for (Entity following : average.entities) {
                Position followingPos = following.getComponent(Position.class);
                position.position.add(followingPos.position);
            }
            position.position.scl(1.f / average.entities.size);
        }
    }

}
