package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.danilafe.duality.ecs.components.*;

public class PlayerSystem extends IteratingSystem {

    static final float PLAYER_ACCELERATION = 256;
    static final float PLAYER_VELOCITY_LIMT = 64;

    public PlayerSystem(){
        super(Family.all(Player.class, Acceleration.class, Velocity.class, FrictionEntity.class, Animated.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Player player = entity.getComponent(Player.class);
        Acceleration acceleration = entity.getComponent(Acceleration.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        FrictionEntity frictionEntity = entity.getComponent(FrictionEntity.class);
        Animated animated = entity.getComponent(Animated.class);

        if(velocity.velocity.x > PLAYER_VELOCITY_LIMT) velocity.velocity.x = PLAYER_VELOCITY_LIMT;
        else if(velocity.velocity.x < -PLAYER_VELOCITY_LIMT) velocity.velocity.x = -PLAYER_VELOCITY_LIMT;
        animated.flipHorizontal = velocity.velocity.x > 0;

        if(!player.active) {
            acceleration.acceleration.x = 0;
            frictionEntity.reduceAmount = 1;
            return;
        }

        float xAccel = 0;
        if(Gdx.input.isKeyPressed(player.leftKey)) xAccel -= PLAYER_ACCELERATION;
        if(Gdx.input.isKeyPressed(player.rightKey)) xAccel += PLAYER_ACCELERATION;
        acceleration.acceleration.x = xAccel;
        frictionEntity.reduceAmount = (xAccel == 0 || Math.signum(xAccel) != Math.signum(velocity.velocity.x)) ? 1 : .25f;

        if(Gdx.input.isKeyJustPressed(player.jumpKey)) velocity.velocity.y = 128;
    }

}
