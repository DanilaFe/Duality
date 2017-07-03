package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.danilafe.duality.ecs.components.Animated;
import com.danilafe.duality.ecs.components.Position;

public class RenderSystem extends IteratingSystem {

    public OrthographicCamera activeCamera;

    private SpriteBatch mainBatch;
    private SpriteBatch textureBatch;
    private SpriteBatch activeBatch;

    private ShaderProgram shaderProgram;

    public RenderSystem(){
        super(Family.all(Animated.class, Position.class).get());

        mainBatch = new SpriteBatch();

        textureBatch = new SpriteBatch();

        activeBatch = null;
    }

    private void activate(SpriteBatch batch){
        activeBatch = batch;
        activeBatch.begin();
    }

    private void deactivate(){
        activeBatch.end();
        activeBatch = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Animated anm = entity.getComponent(Animated.class);
        Position pos = entity.getComponent(Position.class);

        anm.currentDelay -= deltaTime;
        if(anm.currentDelay <= 0){
            anm.currentDelay += anm.frameDelay;
            anm.currentFrame++;
            if(anm.currentFrame >= anm.currentFrames.size()){
                anm.currentFrame = anm.loop ? 0 : anm.currentFrame - 1;
            }
        }

        if(activeBatch == null) return;
        TextureRegion toRender = anm.animationData.getFrame(anm.currentFrames.get(anm.currentFrame));
        activeBatch.draw(toRender, pos.position.x - toRender.getRegionWidth() / 2, pos.position.y - toRender.getRegionHeight() / 2);
    }

    @Override
    public void update(float deltaTime) {
        if(activeCamera != null){
            mainBatch.setProjectionMatrix(activeCamera.combined);
        }
        activate(mainBatch);
        {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            super.update(deltaTime);
        }
        deactivate();
    }
}
