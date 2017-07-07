package com.danilafe.duality.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.components.Animated;
import com.danilafe.duality.ecs.components.Average;
import com.danilafe.duality.ecs.components.Position;
import javafx.geometry.Pos;

public class EntityUtils {

    public static Entity createDecorativeEntity(PooledEngine engine, ResourceManager resourceManager,
                                         String resourceName, String animationName,
                                         boolean loop, float x, float y){
        Entity entity = engine.createEntity();
        Animated animated = engine.createComponent(Animated.class);
        animated.animationData = resourceManager.getAnimation(resourceName);
        animated.play(animationName, loop);
        TextureRegion frame = animated.animationData.getFrame(0);
        Position position = engine.createComponent(Position.class);
        position.position.set(x, y + frame.getRegionHeight() / 2);
        entity.add(animated);
        entity.add(position);
        return entity;
    }

}
