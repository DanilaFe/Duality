package com.danilafe.duality.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.RecipeDatabase;
import com.danilafe.duality.ecs.components.Animated;
import com.danilafe.duality.ecs.components.Player;
import com.danilafe.duality.ecs.systems.PlayerSystem;
import com.danilafe.duality.serialized.LevelData;

public class Level {

    public static final float TILE_SIZE = 16;
    LevelData levelData;

    public Level(String levelFile){
        levelData = new Json().fromJson(LevelData.class, levelFile);
    }

    public static Level loadInternal(String name){
        return new Level(Gdx.files.internal("levels/" + name + ".json").readString());
    }

    public void create(PooledEngine engine, ResourceManager resources, RecipeDatabase recipes){
        engine.addEntity(recipes.getRecipe("camera").create(engine, resources, 0, 0));

        for(LevelData.Chunk chunk : levelData.chunks){
            int maxX = 0;
            int maxY = 0;
            for(LevelData.Coordinate coord : chunk.tiles){
                if(coord.x > maxX) maxX = coord.x;
                if(coord.y > maxY) maxY = coord.y;
            }

            LevelData.Tile[][] generated = new LevelData.Tile[maxX + 1][maxY + 1];
            for(LevelData.Coordinate coord : chunk.tiles){
                generated[coord.x][coord.y] = levelData.tileDefinitions.get(coord.tile);
            }

            for(int x = 0; x <= maxX; x++){
                for(int y = 0; y <= maxY; y++){
                    if(generated[x][y] == null) continue;

                    Entity newEntity = recipes.getRecipe(generated[x][y].entityName).create(engine, resources,
                            x * TILE_SIZE + chunk.offset.x, y * TILE_SIZE + chunk.offset.y);
                    boolean hasLeft = (x > 0 && generated[x - 1][y] != null);
                    boolean hasRight = (x < generated.length - 1 && generated[x + 1][y] != null);
                    boolean hasBottom = (y > 0 && generated[x][y - 1] != null);
                    boolean hasTop = (y < generated[x].length - 1 && generated[x][y + 1] != null);

                    Animated animated = newEntity.getComponent(Animated.class);
                    if(animated == null) continue;

                    if(hasLeft ^ hasRight){
                        if(!hasTop || !hasBottom){
                            animated.play((!hasTop) ? generated[x][y].topCornerFrame : generated[x][y].bottomCornerFrame, true);
                        } else {
                            animated.play(generated[x][y].wallFrame, true);
                        }
                        animated.flipHorizontal = hasLeft;
                    } else if(!hasTop){
                        animated.play(generated[x][y].topFrame, true);
                    } else if(!hasBottom){
                        animated.play(generated[x][y].bottomFrame, true);
                    } else {
                        animated.play(generated[x][y].defaultFrame, true);
                    }

                    engine.addEntity(newEntity);
                }
            }

            for(LevelData.PlayerSpawn spawn : chunk.players){
                Entity playerEntity = recipes.getRecipe(spawn.entityName)
                        .create(engine, resources, spawn.x * TILE_SIZE + chunk.offset.x, spawn.y * TILE_SIZE + chunk.offset.y);
                Player player = playerEntity.getComponent(Player.class);
                player.switchId = spawn.switchId;
                engine.addEntity(playerEntity);
            }
            PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);
            playerSystem.idTransitions.clear();
            for(String key : levelData.groups.keys()){
                int groupId = Integer.parseInt(key);
                LevelData.SwitchGroup group = levelData.groups.get(key);
                playerSystem.idTransitions.put(groupId, group.transition);
                if(group.active) playerSystem.switchGroup(groupId);
            }
        }
    }

}
