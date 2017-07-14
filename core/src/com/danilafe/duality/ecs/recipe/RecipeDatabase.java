package com.danilafe.duality.ecs.recipe;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ObjectMap;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.components.Runnable;

public class RecipeDatabase {

    private ObjectMap<String, Recipe> recipies;

    public RecipeDatabase() {
        recipies = new ObjectMap<>();
        loadDefault();
    }

    private void loadDefault() {
        recipies.put("player", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Animated animated = engine.createComponent(Animated.class);
                animated.animationData = resources.getAnimation("slime");
                animated.frameDelay = 1.f / 8;
                animated.play("default", true);
                Acceleration acceleration = engine.createComponent(Acceleration.class);
                acceleration.acceleration.y = -512;
                entity.add(animated);
                entity.add(acceleration);
                entity.add(engine.createComponent(Pushing.class));
                entity.add(engine.createComponent(CameraTracker.class));
                entity.add(engine.createComponent(PlatformWalker.class));
                entity.add(engine.createComponent(Input.class));
                entity.add(engine.createComponent(Overlapping.class));
                entity.add(engine.createComponent(ActiveGroup.class));
                entity.add(engine.createComponent(SurfaceTracker.class));
                entity.add(engine.createComponent(Player.class));
                entity.add(engine.createComponent(Velocity.class));
                entity.add(engine.createComponent(FrictionEntity.class));
                entity.add(engine.createComponent(Colliding.class));
                entity.add(createPosition(engine, x, y));
                entity.add(createCollisionBox(engine, x, y, 16, 8));
                return entity;
            }
        });
        recipies.put("wall", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Animated animated = engine.createComponent(Animated.class);
                animated.animationData = resources.getAnimation("tiles");
                animated.frameDelay = 0;
                animated.play("default", false);
                FrictionCause frictionCause = engine.createComponent(FrictionCause.class);
                frictionCause.frictionAmount = .25f;
                entity.add(animated);
                entity.add(frictionCause);
                entity.add(engine.createComponent(Solid.class));
                entity.add(createPosition(engine, x, y));
                entity.add(createCollisionBox(engine, x, y, 16, 16));
                return entity;
            }
        });
        recipies.put("camera", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Camera camera = engine.createComponent(Camera.class);
                camera.camera = new OrthographicCamera();
                Following following = engine.createComponent(Following.class);
                following.maxMove = 1024;
                CameraShake cameraShake = engine.createComponent(CameraShake.class);
                cameraShake.damping = 0.8f;
                entity.add(camera);
                entity.add(following);
                entity.add(cameraShake);
                entity.add(createPosition(engine, x, y));
                return entity;
            }
        });
        recipies.put("rain", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Animated animated = engine.createComponent(Animated.class);
                animated.animationData = resources.getAnimation("rain_drop");
                animated.frameDelay = 1.f / 64;
                animated.play("default", true);
                Runnable runnable = engine.createComponent(Runnable.class);
                runnable.runnable = (givenEngine, givenEntity, dt) -> {
                    if (givenEntity.getComponent(Velocity.class).velocity.y == 0)
                        givenEntity.getComponent(Animated.class).play("drop", false);
                };
                Velocity velocity = engine.createComponent(Velocity.class);
                velocity.velocity.set(0, -256);
                Expiring expiring = engine.createComponent(Expiring.class);
                expiring.expireTime = .75f;
                entity.add(animated);
                entity.add(runnable);
                entity.add(velocity);
                entity.add(expiring);
                entity.add(engine.createComponent(Colliding.class));
                entity.add(engine.createComponent(SurfaceTracker.class));
                entity.add(createPosition(engine, x, y));
                entity.add(createCollisionBox(engine, x, y, 8, 8));
                return entity;
            }
        });
        recipies.put("rain_emitter", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Emitter emitter = engine.createComponent(Emitter.class);
                emitter.particleDensity = 1.f / 4;
                emitter.emitDelay = 1;
                emitter.particleRecipe = getRecipe("rain");
                entity.add(emitter);
                entity.add(createCollisionBox(engine, x, y, 512, 8));
                return entity;
            }
        });
        recipies.put("next_sign", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Animated animated = engine.createComponent(Animated.class);
                animated.animationData = resources.getAnimation("next_sign");
                animated.play("default", false);
                entity.add(animated);
                entity.add(engine.createComponent(LevelPortal.class));
                entity.add(engine.createComponent(OverlapTracker.class));
                entity.add(createCollisionBox(engine, x, y, 16, 16));
                entity.add(createPosition(engine, x, y));
                return entity;
            }
        });
        recipies.put("block", new Recipe() {
            @Override
            public Entity create(PooledEngine engine, ResourceManager resources, float x, float y) {
                Entity entity = engine.createEntity();
                Animated animated = engine.createComponent(Animated.class);
                animated.animationData = resources.getAnimation("block");
                animated.play("default", true);
                Acceleration acceleration = engine.createComponent(Acceleration.class);
                acceleration.acceleration.y = -512;
                entity.add(animated);
                entity.add(acceleration);
                entity.add(engine.createComponent(Colliding.class));
                entity.add(engine.createComponent(Pushable.class));
                entity.add(engine.createComponent(Platform.class));
                entity.add(engine.createComponent(PlatformWalker.class));
                entity.add(engine.createComponent(Velocity.class));
                entity.add(createCollisionBox(engine, x, y, 16, 16));
                entity.add(createPosition(engine, x, y));
                return entity;
            }
        });
    }

    public Recipe getRecipe(String name) {
        return recipies.get(name);
    }

}
