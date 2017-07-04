package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.danilafe.duality.ecs.components.Camera;
import com.danilafe.duality.ecs.components.CameraShake;
import com.danilafe.duality.ecs.components.Position;

public class CameraSystem extends IteratingSystem {

    public CameraSystem(){
        super(Family.all(Camera.class, Position.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Camera cam = entity.getComponent(Camera.class);
        Position pos = entity.getComponent(Position.class);
        CameraShake csh = entity.getComponent(CameraShake.class);

        Vector2 newPosition = new Vector2(pos.position).add(cam.offset);
        if(csh != null){
            csh.currentDelay -= deltaTime;
            while(csh.currentDelay <= 0 && csh.moveDelay != 0 && csh.shakeLength > csh.threshold){
                csh.currentDelay += csh.moveDelay;
                csh.shakeLength *= csh.damping;
                csh.currentOffset.set(csh.movingTowards);
                csh.movingFrom.set(csh.currentOffset);
                csh.movingTowards.setLength(csh.shakeLength);
                csh.movingTowards.rotate((float) (135 + Math.random() * 90));
            }
            if(csh.moveDelay != 0 && csh.shakeLength > csh.threshold){
                Vector2 progress = new Vector2(csh.movingTowards).sub(csh.movingFrom).scl(1.f - csh.currentDelay / csh.moveDelay);
                csh.currentOffset.set(csh.movingFrom).add(progress);
            } else {
                csh.currentOffset.set(0, 0);
            }
            newPosition.add(csh.currentOffset);
        }

        cam.camera.position.set(newPosition.x, newPosition.y, 0);
        cam.camera.update();
    }

}
