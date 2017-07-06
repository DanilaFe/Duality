package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.danilafe.duality.ecs.components.*;

public class PlayerSystem extends IteratingSystem {

    static final float PLAYER_ACCELERATION = 256;
    static final float PLAYER_VELOCITY_LIMIT = 96;
    static final float PLAYER_VELOCITY_JUMP = 172;

    static final float PLAYER_FRICTION_STANDING = 1;
    static final float PLAYER_FRICTION_MOVING = .25f;

    public IntIntMap keysToId;
    public IntMap<Boolean> idTransitions;

    public PlayerSystem() {
        super(Family.all(Player.class, Acceleration.class, Velocity.class, FrictionEntity.class, Animated.class, SurfaceTracker.class).get());
        keysToId = new IntIntMap();
        idTransitions = new IntMap<>();
    }

    public void registerPlayer(int id, int key, boolean transiton) {
        keysToId.put(key, id);
        idTransitions.put(id, transiton);
    }

    public void switchGroup(int id) {
        PooledEngine engine = (PooledEngine) getEngine();
        Array<Entity> cameraPlayers = new Array<>();

        for (Entity playerEntity : getEntities()) {
            Player player = playerEntity.getComponent(Player.class);
            if (player.switchId == id) cameraPlayers.add(playerEntity);
        }
        if(cameraPlayers.size == 0) return;
        for(Entity playerEntity : getEntities()){
            Player player = playerEntity.getComponent(Player.class);
            player.active = player.switchId == id;
        }

        Entity toFollow = null;
        if(cameraPlayers.size == 1){
            toFollow = cameraPlayers.first();
        } else if(cameraPlayers.size > 1) {
            Average averageComponent;
            ImmutableArray<Entity> averageEntities = engine.getEntitiesFor(Family.all(Average.class).get());
            if(averageEntities.size() > 0){
               toFollow = averageEntities.first();
               averageComponent = toFollow.getComponent(Average.class);
            } else {
                toFollow = engine.createEntity();
                averageComponent =  engine.createComponent(Average.class);
                toFollow.add(averageComponent);
                toFollow.add(engine.createComponent(Position.class));
                engine.addEntity(toFollow);
            }
            averageComponent.entities.clear();
            averageComponent.entities.addAll(cameraPlayers);
        }
        engine.getEntitiesFor(Family.all(Following.class, Camera.class).get())
                    .first().getComponent(Following.class).otherEntity = toFollow;

        engine.getSystem(RenderSystem.class).increasing = idTransitions.get(id);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Player player = entity.getComponent(Player.class);
        Acceleration acceleration = entity.getComponent(Acceleration.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        FrictionEntity frictionEntity = entity.getComponent(FrictionEntity.class);
        Animated animated = entity.getComponent(Animated.class);

        if (velocity.velocity.x > PLAYER_VELOCITY_LIMIT) velocity.velocity.x = PLAYER_VELOCITY_LIMIT;
        else if (velocity.velocity.x < -PLAYER_VELOCITY_LIMIT) velocity.velocity.x = -PLAYER_VELOCITY_LIMIT;
        animated.flipHorizontal = velocity.velocity.x > 0;

        if (!player.active) {
            acceleration.acceleration.x = 0;
            frictionEntity.reduceAmount = PLAYER_FRICTION_STANDING;
            return;
        }

        float xAccel = 0;
        if (Gdx.input.isKeyPressed(player.leftKey)) xAccel -= PLAYER_ACCELERATION;
        if (Gdx.input.isKeyPressed(player.rightKey)) xAccel += PLAYER_ACCELERATION;
        acceleration.acceleration.x = xAccel;
        frictionEntity.reduceAmount = (xAccel == 0 || Math.signum(xAccel) != Math.signum(velocity.velocity.x)) ? PLAYER_FRICTION_STANDING : PLAYER_FRICTION_MOVING;

        if (Gdx.input.isKeyJustPressed(player.jumpKey) && entity.getComponent(SurfaceTracker.class).onSurface)
            velocity.velocity.y = PLAYER_VELOCITY_JUMP;
    }

    @Override
    public void update(float deltaTime) {
        for (IntIntMap.Entry entry : keysToId.entries()) {
            if (Gdx.input.isKeyPressed(entry.key)) switchGroup(entry.value);
        }

        super.update(deltaTime);
    }
}
