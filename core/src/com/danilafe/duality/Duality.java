package com.danilafe.duality;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Queue;
import com.danilafe.duality.controls.ControlManager;
import com.danilafe.duality.ecs.components.graphics.Camera;
import com.danilafe.duality.ecs.recipe.RecipeDatabase;
import com.danilafe.duality.ecs.systems.*;
import com.danilafe.duality.level.Level;

public class Duality extends ApplicationAdapter {
    private static Duality instance;

    Queue<DualityRunnable> runQueue;
    PooledEngine engine;

    ResourceManager resourceManager;
    RecipeDatabase recipeDatabase;
	ControlManager controlManager;

	@Override
	public void create () {
		runQueue = new Queue<>();
		engine = new PooledEngine();
		resourceManager = new ResourceManager();
		recipeDatabase = new RecipeDatabase();
		controlManager = new ControlManager();

		engine.addSystem(new ActiveGroupSystem());
		EmitterSystem emitterSystem = new EmitterSystem();
		emitterSystem.resources = resourceManager;
		engine.addSystem(emitterSystem);
		engine.addSystem(new ExpiringSystem());
		engine.addSystem(new AccelerationSystem());
		engine.addSystem(new FrictionSystem());
		engine.addSystem(new PlatformSystem());
		engine.addSystem(new PushSystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new PositionSystem());
		engine.addSystem(new AverageSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CollisionBoxSystem());
		engine.addSystem(new FollowingSystem());
		engine.addSystem(new OverlapSystem());
		engine.addSystem(new SurfaceSystem());
		engine.addSystem(new RunnableSystem());
		engine.addSystem(new PlayerSystem());
		engine.addSystem(new LevelSystem());
		RenderSystem renderSystem = new RenderSystem();
		renderSystem.shaderProgram = resourceManager.getShader("default");
		renderSystem.backgroundTexture = resourceManager.getTexture("background");
		engine.addSystem(renderSystem);

		Level.loadInternal("test_level").create(engine, resourceManager, recipeDatabase, controlManager);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		engine.update(Gdx.graphics.getDeltaTime());
		while(runQueue.size != 0) {
			runQueue.removeFirst().run(engine, resourceManager, recipeDatabase, controlManager);
		}
	}
	
	@Override
	public void dispose () {

	}

	@Override
	public void resize(int width, int height) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(Camera.class).get());
		entities.forEach(e -> e.getComponent(Camera.class).camera.setToOrtho(false, Constants.VIEW_WIDTH, Constants.VIEW_WIDTH* height / width));
	}

	public static void runAfterStep(DualityRunnable runnable){
        instance.runQueue.addLast(runnable);
    }

}
