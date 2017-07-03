package com.danilafe.duality.animation;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class AnimationData {

    private TextureRegion[][] textureRegions;
    private ObjectMap<String, ImmutableArray<Integer>> animations;

    public AnimationData(Texture sourceTexture, int segmentWidth, int segmentHeight){
        textureRegions = TextureRegion.split(sourceTexture, segmentWidth, segmentHeight);
        animations = new ObjectMap<String, ImmutableArray<Integer>>();
    }

    public void registerAnimation(String name, int...frames){
        Array<Integer> newFrames = new Array<Integer>();
        for(int newFrame : frames){
            newFrames.add(newFrame);
        }
        animations.put(name, new ImmutableArray<Integer>(newFrames));
    }

    public ImmutableArray<Integer> getAnimation(String string){
        return animations.get(string);
    }

    public TextureRegion getFrame(int index){
        return textureRegions[index / textureRegions[0].length][index % textureRegions[0].length];
    }

}
