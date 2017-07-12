package com.danilafe.duality.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.danilafe.duality.Constants;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.controls.ControlManager;
import com.danilafe.duality.ecs.EntityUtils;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.recipe.RecipeDatabase;
import com.danilafe.duality.ecs.systems.ActiveGroupSystem;
import com.danilafe.duality.serialized.LevelData;

public class Level {

    LevelData levelData;

    public Level(String levelFile) {
        levelData = new Json().fromJson(LevelData.class, levelFile);
    }

    public static Level loadInternal(String name) {
        return new Level(Gdx.files.internal("levels/" + name + ".json").readString());
    }

    private float chunkCoord(int position, float offset) {
        return position * Constants.TILE_SIZE + offset;
    }

    private float chunkX(int position, LevelData.Chunk chunk) {
        return chunkCoord(position, chunk.offset.x);
    }

    private float chunkY(int position, LevelData.Chunk chunk) {
        return chunkCoord(position, chunk.offset.y);
    }

    private void loadChunkTiles(LevelData.Chunk chunk, PooledEngine engine, ResourceManager resources, RecipeDatabase recipes) {
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
    }

    private void loadChunkPlayers(LevelData.Chunk chunk, PooledEngine engine, ResourceManager resources, RecipeDatabase recipes, ControlManager controls) {
        IntMap<Array<Entity>> groupedPlayers = new IntMap<>();
        for (LevelData.PlayerSpawn spawn : chunk.players) {
            Entity playerEntity = recipes.getRecipe(spawn.entityName)
                    .create(engine, resources, chunkX(spawn.coords[0], chunk), chunkY(spawn.coords[1], chunk));
            ActiveGroup group = playerEntity.getComponent(ActiveGroup.class);
            group.switchId = spawn.switchId;
            engine.addEntity(playerEntity);

            Array<Entity> insertInto;
            if (groupedPlayers.containsKey(spawn.switchId)) {
                insertInto = groupedPlayers.get(spawn.switchId);
            } else {
                insertInto = new Array<>();
                groupedPlayers.put(spawn.switchId, insertInto);
            }
            insertInto.add(playerEntity);
        }

        IntMap.Keys keys = groupedPlayers.keys();
        while (keys.hasNext) {
            int playerNumber = 0;
            int group = keys.next();
            for (Entity player : groupedPlayers.get(group)) {
                player.getComponent(Input.class).controlData = controls.getNthControl(playerNumber++);
            }
        }
    }

    private void loadChunkDecorations(LevelData.Chunk chunk, PooledEngine engine, ResourceManager resources) {
        for (LevelData.DecorativeEntity entity : chunk.decorations) {
            Entity createdEntity =
                    EntityUtils.createDecorativeEntity(engine, resources, entity.resourceName, entity.animationName,
                            entity.loop,
                            chunkX(entity.coords[0], chunk), chunkY(entity.coords[1], chunk) - Constants.TILE_SIZE / 2);
            engine.addEntity(createdEntity);
        }
    }

    private void loadChunkEntities(LevelData.Chunk chunk, PooledEngine engine, ResourceManager resources, RecipeDatabase recipes) {
        for (LevelData.GeneralEntity entity : chunk.entities) {
            Entity createdEntity = recipes.getRecipe(entity.entityName).create(engine, resources,
                    chunkX(entity.coords[0], chunk), chunkY(entity.coords[1], chunk));
            engine.addEntity(createdEntity);
        }
    }

    private void loadChunkLevelPortal(LevelData.Chunk chunk, PooledEngine engine, ResourceManager resources, RecipeDatabase recipes) {
        if (chunk.levelPortal != null) {
            Entity levelPortal = recipes.getRecipe("next_sign").create(engine, resources,
                    chunkX(chunk.levelPortal.coords[0], chunk), chunkY(chunk.levelPortal.coords[1], chunk));
            if (chunk.levelPortal.loadType.equals("internal"))
                levelPortal.getComponent(LevelPortal.class).levelSupplier = () -> Level.loadInternal(chunk.levelPortal.levelName);
            engine.addEntity(levelPortal);
        }
    }

    private void loadGroups(PooledEngine engine) {
        ActiveGroupSystem activeGroupSystem = engine.getSystem(ActiveGroupSystem.class);
        activeGroupSystem.groupTransitions.clear();
        IntArray groups = new IntArray();
        for (String key : levelData.groups.keys()) {
            groups.add(Integer.parseInt(key));
        }
        boolean transition = false;
        for (int i = 0; i < groups.size; i++) {
            LevelData.SwitchGroup group = levelData.groups.get(Integer.toString(i));
            activeGroupSystem.nextGroupMap.put(groups.get(i), groups.get((i + 1) % groups.size));
            activeGroupSystem.groupTransitions.put(groups.get(i), transition);

            if (group.active) activeGroupSystem.switchGroup(groups.get(i));
            transition = !transition;
        }
    }

    public void create(PooledEngine engine, ResourceManager resources, RecipeDatabase recipes, ControlManager controls) {
        engine.addEntity(recipes.getRecipe("camera").create(engine, resources, 0, 0));
        engine.getEntitiesFor(Family.all(Camera.class).get()).first().getComponent(Camera.class).camera
                .setToOrtho(false, Constants.VIEW_WIDTH, Constants.VIEW_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());

        for (LevelData.Chunk chunk : levelData.chunks) {
            loadChunkTiles(chunk, engine, resources, recipes);
            loadChunkPlayers(chunk, engine, resources, recipes, controls);
            loadChunkDecorations(chunk, engine, resources);
            loadChunkEntities(chunk, engine, resources, recipes);
            loadChunkLevelPortal(chunk, engine, resources, recipes);
        }
        loadGroups(engine);
    }

}
