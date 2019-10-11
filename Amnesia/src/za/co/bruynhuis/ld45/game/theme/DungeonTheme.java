/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game.theme;

import com.bruynhuis.galago.games.cubes.AbstractCubesTheme;
import com.cubes.Block;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.CubesSettings;
import com.cubes.shapes.BlockShape_Cuboid;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

/**
 *
 * @author NideBruyn
 */
public class DungeonTheme extends AbstractCubesTheme {

    //Level variables
    public static final int roomColumns = 4;
    public static final int roomRows = 4;

    //Room variables
    public static final int roomWidth = 20;
    public static final int roomHeight = 20;

    public static final int borderSize = 1;

    public static final boolean observer = false;

    public static final float tileSize = 1f;

    public static int chunkSize = 5;

    public DungeonTheme(Application application) {
        super(application, "Textures/dungeon.png", 4, 4);
    }

    @Override
    protected void registerSettings(CubesSettings cubesSettings) {
        cubesSettings.setChunkSizeX(((roomColumns * roomWidth) + (borderSize * 2)) / chunkSize);
        cubesSettings.setChunkSizeY(5);
        cubesSettings.setChunkSizeZ(((roomRows * roomHeight) + (borderSize * 2)) / chunkSize);
        setVertical(false);

        setChunkCountX(chunkSize + 1);
        setChunkCountY(1);
        setChunkCountZ(chunkSize + 1);

        if (!DungeonTheme.observer) {
            Material m = application.getAssetManager().loadMaterial("Materials/dungeon.j3m");
            Texture t = (Texture) m.getParam("DiffuseMap").getValue();
            t.setMagFilter(Texture.MagFilter.Nearest);
            t.setMinFilter(Texture.MinFilter.NearestLinearMipMap);
            cubesSettings.setBlockMaterial(m);
        }

    }

    @Override
    protected void registerBlocks() {
        //FLOOR
        Block floor = new Block(new BlockSkin(new BlockSkin_TextureLocation(0, 0), false));
        registerBlock("floor", floor, 0, 3, false);

        //WALL
        Block wall = new Block(new BlockSkin(new BlockSkin_TextureLocation(1, 1), false));
        registerBlock("wall", wall, 1, 2, false);

        //WALLTOP
        Block wallTop = new Block(new BlockSkin(new BlockSkin_TextureLocation(1, 0), false));
        registerBlock("wallTop", wallTop, 1, 2, false);

        //WALLBOTTOM
        Block wallBottom = new Block(new BlockSkin(new BlockSkin_TextureLocation(1, 2), false));
        registerBlock("wallBottom", wallBottom, 1, 2, false);

        //WALLTRAP
        Block wallTrap = new Block(new BlockSkin(new BlockSkin_TextureLocation(0, 1), false));
        registerBlock("wallTrap", wallTrap, 1, 2, false);

        //WALLOTHER
        Block wallOther = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 1), false));
        registerBlock("wallOther", wallOther, 1, 2, false);

        //WALLLIGHT
        Block wallLight = new Block(new BlockSkin(new BlockSkin_TextureLocation(1, 3), false));
        registerBlock("wallLight", wallLight, 1, 2, false);

        //ROOF
        Block roof = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 0), false));
        registerBlock("roof", roof, 2, 3, false);

        //ROOF LIGHT
        Block roofLight = new Block(new BlockSkin(new BlockSkin_TextureLocation(3, 2), false));
        registerBlock("roofLight", roofLight, 2, 3, false);

        //START
        Block start = new Block(new BlockSkin(new BlockSkin_TextureLocation(3, 1), false));
        registerBlock("start", start, 3, 3, false);

        //KEY        
        Block key = new Block(new BlockSkin(new BlockSkin_TextureLocation(1, 3), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f}));
            }
        };
        registerBlock("key", key, 2, 2, false);

        //END
        Block end = new Block(new BlockSkin(new BlockSkin_TextureLocation(3, 0), false));
        registerBlock("end", end, 3, 2, false);

        //exitLeftBottom
        Block exitLeftBottom = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 3), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.4f}));
            }
        };
        registerBlock("exitLeftBottom", exitLeftBottom, 2, 3, false);

        //exitLeftTop
        Block exitLeftTop = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 2), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.4f}));
            }
        };
        registerBlock("exitLeftTop", exitLeftTop, 2, 2, false);

        //exitLeftBottomOpen
        Block exitLeftBottomOpen = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 3), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, -0.4f, 0.5f, 0.5f}));
            }
        };
        registerBlock("exitLeftBottomOpen", exitLeftBottomOpen, 2, 3, false);

        //exitLeftTopOpen
        Block exitLeftTopOpen = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 2), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, -0.4f, 0.5f, 0.5f}));
            }
        };
        registerBlock("exitLeftTopOpen", exitLeftTopOpen, 2, 2, false);

        //exitRightBottom
        Block exitRightBottom = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 3), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, -0.4f, 0.5f, 0.5f}));
            }
        };
        registerBlock("exitRightBottom", exitRightBottom, 2, 3, false);

        //exitRightTop
        Block exitRightTop = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 2), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, -0.4f, 0.5f, 0.5f}));
            }
        };
        registerBlock("exitRightTop", exitRightTop, 2, 2, false);

        //exitRightBottomOpen
        Block exitRightBottomOpen = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 3), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.4f}));
            }
        };
        registerBlock("exitRightBottomOpen", exitRightBottomOpen, 2, 3, false);

        //exitRightTopOpen
        Block exitRightTopOpen = new Block(new BlockSkin(new BlockSkin_TextureLocation(2, 2), false)) {
            {
                setShapes(new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.4f}));
            }
        };
        registerBlock("exitRightTopOpen", exitRightTopOpen, 2, 2, false);

        //WALLTOPEXIT
        Block wallTopExit = new Block(new BlockSkin(new BlockSkin_TextureLocation(0, 2), false));
        registerBlock("wallTopExit", wallTopExit, 0, 1, false);

    }

    public int getMapTotalHeight() {
        return (roomRows * roomHeight) + (borderSize * 2);
    }

    public int getMapTotalWidth() {
        return (roomColumns * roomWidth) + (borderSize * 2);
    }
}
