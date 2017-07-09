package com.danilafe.duality.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.EntityUtils;
import com.danilafe.duality.ecs.recipe.RecipeDatabase;
import com.danilafe.duality.ecs.components.ActiveGroup;
import com.danilafe.duality.ecs.components.Animated;
import com.danilafe.duality.ecs.components.Camera;
import com.danilafe.duality.ecs.components.LevelPortal;
import com.danilafe.duality.ecs.systems.ActiveGroupSystem;
import com.danilafe.duality.ecs.systems.RenderSystem;
import com.danilafe.duality.serialized.LevelData;

public class Level {

    public static final float TILE_SIZE = 16;
    LevelData levelData;

    public Level(String levelFile) {
        levelData = new Json().fromJson(LevelData.class, levelFile);
    }

    public static Level loadInternal(String name) {
        return new Level(Gdx.files.internal("levels/" + name + ".json").readString());
    }

    private float chunkCoord(int position, float offset) {
        return position * TILE_SIZE + offset;
    }

    private float chunkX(int position, LevelData.Chunk chunk) {
        return chunkCoord(position, chunk.offset.x);
    }

    private float chunkY(int position, LevelData.Chunk chunk) {
        return chunkCoord(position, chunk.offset.y);
    }

    public void create(PooledEngine engine, ResourceManager resources, RecipeDatabase recipes) {
        engine.addEntity(recipes.getRecipe("camera").create(engine, resources, 0, 0));
        engine.getEntitiesFor(Family.all(Camera.class).get()).first().getComponent(Camera.class).camera
                .setToOrtho(false, RenderSystem.VIEW_WIDTH, RenderSystem.VIEW_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());

        for (LevelData.Chunk chunk : levelData.chunks) {
            int maxX = 0;
            int maxY = 0;
            for (LevelData.Coordinate coord : chunk.tiles) {
                if (coord.coords[0] > maxX) maxX = coord.coords[0];
                if (coord.coords[1] > maxY) maxY = coord.coords[1];
            }

            LevelData.Tile[][] generated = new LevelData.Tile[maxX + 1][maxY + 1];
            for (LevelData.Coordinate coord : chunk.tiles) {
                generated[coord.coords[0]][coord.coords[1]] = levelData.tileDefinitions.get(coord.tile);
            }

            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (generated[x][y] == null) continue;

                    Entity newEntity = recipes.getRecipe(generated[x][y].entityName).create(engine, resources,
                            chunkX(x, chunk), chunkY(y, chunk));
                    boolean hasLeft = (x > 0 && generated[x - 1][y] != null);
                    boolean hasRight = (x < generated.length - 1 && generated[x + 1][y] != null);
                    boolean hasBottom = (y > 0 && generated[x][y - 1] != null);
                    boolean hasTop = (y < generated[x].length - 1 && generated[x][y + 1] != null);

                    Animated animated = newEntity.getComponent(Animated.class);
                    if (animated == null) continue;

                    if (hasLeft ^ hasRight) {
                        if (!hasTop || !hasBottom) {
                            animated.play((!hasTop) ? generated[x][y].topCornerFrame : generated[x][y].bottomCornerFrame, true);
                        } else {
                            animated.play(generated[x][y].wallFrame, true);
                        }
                        animated.flipHorizontal = hasLeft;
                    } else if (!hasTop) {
                        animated.play(generated[x][y].topFrame, true);
                    } else if (!hasBottom) {
                        animated.play(generated[x][y].bottomFrame, true);
                    } else {
                        animated.play(generated[x][y].defaultFrame, true);
                    }

                    engine.addEntity(newEntity);
                }
            }

            for (LevelData.PlayerSpawn spawn : chunk.players) {
                Entity playerEntity = recipes.getRecipe(spawn.entityName)
                        .create(engine, resources, chunkX(spawn.coords[0], chunk), chunkY(spawn.coords[1], chunk));
                ActiveGroup group = playerEntity.getComponent(ActiveGroup.class);
                group.switchId = spawn.switchId;
                engine.addEntity(playerEntity);
            }
            ActiveGroupSystem activeGroupSystem = engine.getSystem(ActiveGroupSystem.class);
            activeGroupSystem.idTransitions.clear();
            IntArray groups = new IntArray();
            for (String key : levelData.groups.keys()) {
                groups.add(Integer.parseInt(key));
            }
            boolean transition = false;
            for(int i = 0; i < groups.size; i++){
                LevelData.SwitchGroup group = levelData.groups.get(Integer.toString(i));
                activeGroupSystem.groupTransitions.put(groups.get(i), groups.get((i + 1) % groups.size));
                activeGroupSystem.idTransitions.put(groups.get(i), transition);

                if(group.active) activeGroupSystem.switchGroup(groups.get(i));
                transition = !transition;
            }

            for (LevelData.DecorativeEntity entity : chunk.decorations) {
                Entity createdEntity =
                        EntityUtils.createDecorativeEntity(engine, resources, entity.resourceName, entity.animationName,
                                entity.loop,
                                chunkX(entity.coords[0], chunk), chunkY(entity.coords[1], chunk) - TILE_SIZE / 2);
                engine.addEntity(createdEntity);
            }

            for (LevelData.GeneralEntity entity : chunk.entities) {
                Entity createdEntity = recipes.getRecipe(entity.entityName).create(engine, resources,
                        chunkX(entity.coords[0], chunk), chunkY(entity.coords[1], chunk));
                engine.addEntity(createdEntity);
            }

            if (chunk.levelPortal != null) {
                Entity levelPortal = recipes.getRecipe("next_sign").create(engine, resources,
                        chunkX(chunk.levelPortal.coords[0], chunk), chunkY(chunk.levelPortal.coords[1], chunk));
                if (chunk.levelPortal.loadType.equals("internal"))
                    levelPortal.getComponent(LevelPortal.class).levelSupplier = () -> Level.loadInternal(chunk.levelPortal.levelName);
                engine.addEntity(levelPortal);
            }
        }
    }

}
