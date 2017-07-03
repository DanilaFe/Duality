package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.danilafe.duality.ecs.components.Animated;
import com.danilafe.duality.ecs.components.Position;

public class RenderSystem extends IteratingSystem {

    public OrthographicCamera activeCamera;
    public Texture backgroundTexture;

    private SpriteBatch mainBatch;
    private SpriteBatch textureBatch;
    private SpriteBatch activeBatch;

    private ShaderProgram shaderProgram;

    public RenderSystem(){
        super(Family.all(Animated.class, Position.class).get());
        ShaderProgram.pedantic = false;
        shaderProgram = new ShaderProgram(Gdx.files.internal("vertex.glsl").readString(), Gdx.files.internal("fragment.glsl").readString());
        if(!shaderProgram.isCompiled()){
            System.err.println(shaderProgram.getLog());
        }

        mainBatch = new SpriteBatch();
        mainBatch.setShader(shaderProgram);

        textureBatch = new SpriteBatch();

        activeBatch = null;
    }

    private void activate(SpriteBatch batch){
        activeBatch = batch;
        activeBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        while(anm.currentDelay <= 0 && anm.frameDelay != 0){
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
        FrameBuffer gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        gameBuffer.begin();
        activate(textureBatch);
        {
            Gdx.gl.glClearColor(0,0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            if(activeCamera != null)
                activeBatch.setProjectionMatrix(activeCamera.combined);
            super.update(deltaTime);
        }
        deactivate();
        gameBuffer.end();

        FrameBuffer backgroundBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        backgroundBuffer.begin();
        activate(textureBatch);
        {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            if(backgroundTexture != null)
                textureBatch.draw(backgroundTexture, 0, 0);
        }
        deactivate();
        backgroundBuffer.end();

        activate(mainBatch);
        {
            backgroundBuffer.getColorBufferTexture().bind(1);
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
            activeBatch.getShader().setUniformi("u_background", 1);
            activeBatch.draw(gameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
        }
        deactivate();
    }
}
