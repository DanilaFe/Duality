package com.danilafe.duality.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.components.*;
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

    public static void safeDelete(Engine engine, Entity toDelete){
        engine.getEntitiesFor(Family.all(Average.class).get()).forEach(e -> {
            Average average = e.getComponent(Average.class);
            average.entities.removeValue(toDelete, false);
        });
        engine.getEntitiesFor(Family.all(Child.class).get()).forEach(e -> {
            Child child = e.getComponent(Child.class);
            if(child.parent == toDelete) child.parent = null;
        });
        engine.getEntitiesFor(Family.all(Following.class).get()).forEach(e -> {
           Following following = e.getComponent(Following.class);
           if(following.otherEntity == toDelete) following.otherEntity = null;
        });
        engine.getEntitiesFor(Family.all(Parent.class).get()).forEach(e -> {
            Parent parent = e.getComponent(Parent.class);
            parent.children.removeValue(toDelete, false);
        });
        engine.getEntitiesFor(Family.all(SurfaceTracker.class).get()).forEach(e -> {
            SurfaceTracker tracker = e.getComponent(SurfaceTracker.class);
            if(tracker.lastSurface == toDelete) tracker.lastSurface = null;
        });
        engine.removeEntity(toDelete);
    }

}
