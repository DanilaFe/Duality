package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
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

    public PlayerSystem(){
        super(Family.all(Player.class, Acceleration.class, Velocity.class, FrictionEntity.class, Animated.class, SurfaceTracker.class).get());
        keysToId = new IntIntMap();
        idTransitions = new IntMap<>();
    }

    public void registerPlayer(int id, int key, boolean transiton){
        keysToId.put(key, id);
        idTransitions.put(id, transiton);
    }
    public void switchPlayer(int id){
        Entity newPlayer = null;
        for(Entity playerEntity : getEntities()){
            Player player = playerEntity.getComponent(Player.class);
            player.active = player.switchId == id;
            if(player.active) newPlayer = playerEntity;
        }
        Engine engine = getEngine();
        engine.getEntitiesFor(Family.all(Following.class, Camera.class).get())
                .first().getComponent(Following.class).otherEntity = newPlayer;
        engine.getSystem(RenderSystem.class).increasing = idTransitions.get(id);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Player player = entity.getComponent(Player.class);
        Acceleration acceleration = entity.getComponent(Acceleration.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        FrictionEntity frictionEntity = entity.getComponent(FrictionEntity.class);
        Animated animated = entity.getComponent(Animated.class);

        if(velocity.velocity.x > PLAYER_VELOCITY_LIMIT) velocity.velocity.x = PLAYER_VELOCITY_LIMIT;
        else if(velocity.velocity.x < -PLAYER_VELOCITY_LIMIT) velocity.velocity.x = -PLAYER_VELOCITY_LIMIT;
        animated.flipHorizontal = velocity.velocity.x > 0;

        if(!player.active) {
            acceleration.acceleration.x = 0;
            frictionEntity.reduceAmount = PLAYER_FRICTION_STANDING;
            return;
        }

        float xAccel = 0;
        if(Gdx.input.isKeyPressed(player.leftKey)) xAccel -= PLAYER_ACCELERATION;
        if(Gdx.input.isKeyPressed(player.rightKey)) xAccel += PLAYER_ACCELERATION;
        acceleration.acceleration.x = xAccel;
        frictionEntity.reduceAmount = (xAccel == 0 || Math.signum(xAccel) != Math.signum(velocity.velocity.x)) ? PLAYER_FRICTION_STANDING : PLAYER_FRICTION_MOVING;

        if(Gdx.input.isKeyJustPressed(player.jumpKey) && entity.getComponent(SurfaceTracker.class).onSurface) velocity.velocity.y = PLAYER_VELOCITY_JUMP;
    }

    @Override
    public void update(float deltaTime) {
        for(IntIntMap.Entry entry : keysToId.entries()){
            if(Gdx.input.isKeyPressed(entry.key)) switchPlayer(entry.value);
        }

        super.update(deltaTime);
    }
}
