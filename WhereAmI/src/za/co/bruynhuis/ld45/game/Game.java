/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.bruynhuis.galago.app.Base3DApplication;
import com.bruynhuis.galago.control.RotationControl;
import com.bruynhuis.galago.games.cubes.AbstractCubesTheme;
import com.bruynhuis.galago.games.cubes.CubesGame;
import com.bruynhuis.galago.sprite.Sprite;
import com.bruynhuis.galago.util.SpatialUtils;
import com.bruynhuis.galago.util.Timer;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import za.co.bruynhuis.ld45.game.roommaps.RoomAll;
import za.co.bruynhuis.ld45.game.roommaps.RoomExit;
import za.co.bruynhuis.ld45.game.roommaps.RoomLeftBottom;
import za.co.bruynhuis.ld45.game.roommaps.RoomLeftRight;
import za.co.bruynhuis.ld45.game.roommaps.RoomLeftRightBottom;
import za.co.bruynhuis.ld45.game.roommaps.RoomLeftRightTop;
import za.co.bruynhuis.ld45.game.roommaps.RoomLeftTop;
import za.co.bruynhuis.ld45.game.roommaps.RoomRightBottom;
import za.co.bruynhuis.ld45.game.roommaps.RoomRightTop;
import za.co.bruynhuis.ld45.game.roommaps.RoomStart;
import za.co.bruynhuis.ld45.game.roommaps.RoomTopBottom;
import za.co.bruynhuis.ld45.game.theme.DungeonTheme;
import static za.co.bruynhuis.ld45.game.theme.DungeonTheme.borderSize;
import static za.co.bruynhuis.ld45.game.theme.DungeonTheme.roomColumns;
import static za.co.bruynhuis.ld45.game.theme.DungeonTheme.roomHeight;
import static za.co.bruynhuis.ld45.game.theme.DungeonTheme.roomRows;
import static za.co.bruynhuis.ld45.game.theme.DungeonTheme.roomWidth;

/**
 *
 * @author NideBruyn
 */
public class Game extends CubesGame {

    private boolean levelLoading = false;
    private int nextRoomX = 0;
    private int nextRoomY = 0;
    private int nextRoomType = 0;

    private int previousRoomX = 0;
    private int previousRoomY = 0;
    private int previousXShift = 0;
    private int previousYShift = 0;

    private Timer roomLoadingTimer = new Timer(50);

    //Rooms types
    private static final int ROOM_TYPE_LEFT_RIGHT = 0;
    private static final int ROOM_TYPE_LEFT_RIGHT_BOTTOM = 1;
    private static final int ROOM_TYPE_LEFT_RIGHT_TOP = 2;
    private static final int ROOM_TYPE_RIGHT_BOTTOM = 3;
    private static final int ROOM_TYPE_LEFT_BOTTOM = 4;
    private static final int ROOM_TYPE_RIGHT_TOP = 5;
    private static final int ROOM_TYPE_LEFT_TOP = 6;
    private static final int ROOM_TYPE_TOP_BOTTOM = 7;
    private static final int ROOM_TYPE_ALL = 8;
    private static final int ROOM_TYPE_EXIT = 9;
    private static final int ROOM_TYPE_START = 10;

    private int[] ROOMS_OPEN_LEFT = {0, 1, 2, 4, 6, 8};
    private int[] ROOMS_OPEN_RIGHT = {0, 1, 2, 3, 5, 8};
    private int[] ROOMS_OPEN_BOTTOM = {1, 3, 4, 7, 8};
    private int[] ROOMS_OPEN_LEFT_RIGHT = {0, 1, 2, 8};
    private int[] ROOMS_OPEN_LEFT_RIGHT_TOP = {2, 8};
    private int[] ROOMS_OPEN_LEFT_RIGHT_BOTTOM = {1, 8};
    private int[] ROOMS_OPEN_RIGHT_BOTTOM = {1, 3, 8};
    private int[] ROOMS_OPEN_LEFT_BOTTOM = {1, 4, 8};
    private int[] ROOMS_OPEN_RIGHT_TOP = {2, 5, 8};
    private int[] ROOMS_OPEN_LEFT_TOP = {2, 6, 8};
    private int[] ROOMS_OPEN_TOP_BOTTOM = {7, 8};

    private int[][] roomMap = new int[DungeonTheme.roomRows][DungeonTheme.roomColumns];

    private static final int TILE_BORDER = 0;
    private static final int TILE_WALLS = 1;
    private static final int TILE_FLOOR = 2;
    private static final int TILE_ROOF = 3;
    private static final int TILE_START = 4;
    private static final int TILE_ROOF_LIGHT_GREEN = 5;
    private static final int TILE_WALLS_TRAP = 6;
    private static final int TILE_LIGHT = 7;
    private static final int TILE_ROOF_LIGHT = 8;
    private static final int TILE_BLANK = 100;
    private static final int TILE_EXIT_LEFT = 9;
    private static final int TILE_EXIT_RIGHT = 10;
    private static final int TILE_WINDOW = 11;
    private static final int TILE_DOOR_LEFT = 12;
    private static final int TILE_DOOR_RIGHT = 13;
    private static final int TILE_KEY = 14;
    private static final int TILE_WALLS_OTHER = 15;
    private static final int TILE_MONSTER = 16;

    private boolean startLoaded = false;

    public Game(Base3DApplication baseApplication, Node rootNode, AbstractCubesTheme abstractCubesTheme) {
        super(baseApplication, rootNode, abstractCubesTheme);
    }

    private void fillOutstandingRooms() {

        for (int r = 0; r < roomMap.length; r++) {
            int[] colMap = roomMap[r];
            for (int c = 0; c < colMap.length; c++) {
//                System.out.print("" + colMap[c]);
                if (colMap[c] == 0) {
                    int randomRoom = FastMath.nextRandomInt(0, 8);
                    loadRoom(randomRoom, c, r);
                }
            }
//            System.out.println("");
        }
    }

    @Override
    public void init() {

        blockTerrainControl.addChunkListener(new BlockChunkListener() {

            @Override
            public void onSpatialUpdated(BlockChunkControl blockChunk) {
                Geometry optimizedGeometry = blockChunk.getOptimizedGeometry_Opaque();
                RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
                if (rigidBodyControl == null) {
                    rigidBodyControl = new RigidBodyControl(0);
                    optimizedGeometry.addControl(rigidBodyControl);
                    baseApplication.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
                }
                rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
            }
        });

        if (!DungeonTheme.observer) {
            roomLoadingTimer.setMaxTime(1);
        }

        startLoaded = false;
        loadBase();

        levelLoading = true;
        previousRoomX = 0;
        previousRoomY = 0;
        nextRoomX = FastMath.nextRandomInt(0, 3);
        nextRoomY = 0;
        nextRoomType = ROOM_TYPE_ALL;

        //Fast load
        if (DungeonTheme.observer) {
            levelNode.addControl(new AbstractControl() {
                @Override
                protected void controlUpdate(float tpf) {

                    if (levelLoading) {
                        roomLoadingTimer.update(tpf);

                        if (roomLoadingTimer.finished()) {
                            loadNextRoom();

                            if (levelLoading) {
                                roomLoadingTimer.reset();

                            } else {
                                fillOutstandingRooms();

                                log("START: " + startPosition);
                                log("END: " + endPosition);
                                loadTile(TILE_START, (int) startPosition.x, (int) startPosition.z);
//                                loadTile(TILE_END, (int) endPosition.x, (int) endPosition.z);
                                roomLoadingTimer.stop();
                                baseApplication.getMessageManager().sendMessage("DONE", null);

                            }

                        }

                    }

                }

                @Override
                protected void controlRender(RenderManager rm, ViewPort vp) {

                }
            });

            roomLoadingTimer.start();

        } else {
            //Game load
            while (levelLoading) {
                loadNextRoom();
            }
            fillOutstandingRooms();
            loadTile(TILE_START, (int) startPosition.x, (int) startPosition.z);
//            loadTile(TILE_END, (int) endPosition.x, (int) endPosition.z);
        }

    }

    private void loadBase() {
        int borderWidth = ((DungeonTheme) cubesTheme).getMapTotalWidth();
        int borderHeight = ((DungeonTheme) cubesTheme).getMapTotalHeight();
        System.out.println("Border size (w, h)=(" + borderWidth + ", " + borderHeight + ")");

        for (int r = 0; r < borderHeight; r++) {
            for (int c = 0; c < borderWidth; c++) {

                loadTile(TILE_FLOOR, c, r);

                if (!DungeonTheme.observer) {
//                    log("Loading roof");
                    loadTile(TILE_ROOF, c, r);
                }

                if (r >= borderSize && r < (borderHeight - borderSize) && c >= borderSize && c < (borderWidth - borderSize)) {

                } else {
                    loadTile(TILE_BORDER, c, r);

                }
            }

        }

        loadTile(TILE_ROOF, borderWidth, borderHeight);
    }

    private void loadNextRoom() {

        //First we calculate which direction the next room will move to
        int roomXShift = 0;
        int roomYShift = 0;

        if (nextRoomX == 0) {
            roomXShift = FastMath.nextRandomInt(0, 1);
            if (roomXShift == 0) {
                roomYShift = 1;

            } else if (roomXShift == 1 && previousXShift == -1) {
                roomYShift = 1;
                roomXShift = 0;

            }

        } else if (nextRoomX == DungeonTheme.roomColumns - 1) {
            roomXShift = FastMath.nextRandomInt(-1, 0);
            if (roomXShift == 0) {
                roomYShift = 1;

            } else if (roomXShift == -1 && previousXShift == 1) {
                roomYShift = 1;
                roomXShift = 0;

            }

        } else {
            roomXShift = FastMath.nextRandomInt(-4, 4);
            if (roomXShift == 0) {
                roomYShift = 1;

            } else if (roomXShift < 0 && previousXShift == 1) {
                roomYShift = 1;
                roomXShift = 0;

            } else if (roomXShift > 0 && previousXShift == -1) {
                roomYShift = 1;
                roomXShift = 0;

            } else if (roomXShift < 0) {
                roomXShift = -1;

            } else if (roomXShift > 0) {
                roomXShift = 1;

            }

        }

        //Calculate the correct type of room to add now
        if (previousXShift == 0 && previousYShift == 0) {

            //Left shift
//            if (roomXShift < 0 && roomYShift == 0) {
//                nextRoomType = ROOMS_OPEN_LEFT[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT.length - 1)];
//
//            } else if (roomXShift > 0 && roomYShift == 0) {
//                nextRoomType = ROOMS_OPEN_RIGHT[FastMath.nextRandomInt(0, ROOMS_OPEN_RIGHT.length - 1)];
//
//            } else {
//                nextRoomType = ROOMS_OPEN_BOTTOM[FastMath.nextRandomInt(0, ROOMS_OPEN_BOTTOM.length - 1)];
//
//            }
            nextRoomType = ROOM_TYPE_START;

        } else if (previousXShift < 0 && previousYShift == 0) {
            //Left shift

            if (roomXShift < 0 && roomYShift == 0) {
                nextRoomType = ROOMS_OPEN_LEFT_RIGHT[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_RIGHT.length - 1)];

            } else if (roomXShift > 0 && roomYShift == 0) {
                nextRoomType = ROOMS_OPEN_LEFT_RIGHT[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_RIGHT.length - 1)];

            } else {
                nextRoomType = ROOMS_OPEN_RIGHT_BOTTOM[FastMath.nextRandomInt(0, ROOMS_OPEN_RIGHT_BOTTOM.length - 1)];

            }

        } else if (previousXShift > 0 && previousYShift == 0) {
            //Right shift

            if (roomXShift < 0 && roomYShift == 0) {
                nextRoomType = ROOM_TYPE_ALL;

            } else if (roomXShift > 0 && roomYShift == 0) {
                nextRoomType = ROOMS_OPEN_LEFT_RIGHT[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_RIGHT.length - 1)];

            } else {
                nextRoomType = ROOMS_OPEN_LEFT_BOTTOM[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_BOTTOM.length - 1)];

            }

        } else if (previousXShift == 0 && previousYShift > 0) {
            //Down shift

            if (roomXShift < 0 && roomYShift == 0) {
                nextRoomType = ROOMS_OPEN_LEFT_RIGHT_TOP[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_RIGHT_TOP.length - 1)];

            } else if (roomXShift > 0 && roomYShift == 0) {
                nextRoomType = ROOMS_OPEN_LEFT_RIGHT_TOP[FastMath.nextRandomInt(0, ROOMS_OPEN_LEFT_RIGHT_TOP.length - 1)];

            } else {
                nextRoomType = ROOMS_OPEN_TOP_BOTTOM[FastMath.nextRandomInt(0, ROOMS_OPEN_TOP_BOTTOM.length - 1)];

            }

        } else {
            nextRoomType = ROOM_TYPE_ALL;

        }

        //Lets check if this will be the last room
        if ((nextRoomY + roomYShift) >= DungeonTheme.roomRows) {
            nextRoomType = ROOM_TYPE_EXIT;
        }

        //Add the current room
        loadRoom(nextRoomType, nextRoomX, nextRoomY);
        previousRoomX = nextRoomX;
        previousRoomY = nextRoomY;

        if (!startLoaded) {
            startPosition = new Vector3f((nextRoomX * DungeonTheme.roomWidth) + ((nextRoomX + DungeonTheme.roomWidth) * 0.5f),
                    DungeonTheme.tileSize * 5 * 0.5f,
                    (nextRoomY * DungeonTheme.roomHeight) + ((nextRoomY + DungeonTheme.roomHeight) * 0.5f));
            startLoaded = true;

        }

        endPosition = new Vector3f((nextRoomX * DungeonTheme.roomWidth) + ((nextRoomX + DungeonTheme.roomWidth) * 0.5f),
                DungeonTheme.tileSize * 5 * 0.5f,
                (nextRoomY * DungeonTheme.roomHeight) + ((nextRoomY + DungeonTheme.roomHeight) * 0.5f));

        nextRoomX += roomXShift;
        nextRoomY += roomYShift;

        previousXShift = roomXShift;
        previousYShift = roomYShift;

        if (nextRoomY >= DungeonTheme.roomRows) {
            levelLoading = false;

        }
    }

    private void loadRoom(int type, int columnIndex, int rowIndex) {

        roomMap[rowIndex][columnIndex] = 1;

        if (type == ROOM_TYPE_LEFT_RIGHT) {
            loadRoomFromMap(RoomLeftRight.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_LEFT_RIGHT_BOTTOM) {
            loadRoomFromMap(RoomLeftRightBottom.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_LEFT_RIGHT_TOP) {
            loadRoomFromMap(RoomLeftRightTop.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_RIGHT_BOTTOM) {
            loadRoomFromMap(RoomRightBottom.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_LEFT_BOTTOM) {
            loadRoomFromMap(RoomLeftBottom.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_RIGHT_TOP) {
            loadRoomFromMap(RoomRightTop.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_LEFT_TOP) {
            loadRoomFromMap(RoomLeftTop.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_TOP_BOTTOM) {
            loadRoomFromMap(RoomTopBottom.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_ALL) {
            loadRoomFromMap(RoomAll.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_EXIT) {
            loadRoomFromMap(RoomExit.generateRandomRoom(), columnIndex, rowIndex);

        } else if (type == ROOM_TYPE_START) {
            loadRoomFromMap(RoomStart.generateRandomRoom(), columnIndex, rowIndex);

        }

//        System.out.println("");
    }

    private void loadRoomFromMap(String map, int columnIndex, int rowIndex) {

        int borderWidth = roomColumns * roomWidth;
        int borderHeight = roomRows * roomHeight;

        int startX = (columnIndex * roomWidth);
        int startY = (rowIndex * roomHeight);

        int r = 0;
        int c = 0;
        for (int i = 0; i < map.length(); i++) {
            char item = map.charAt(i);

            if (item == '\n') {
//                System.out.println("");
                r++;
                c = 0;
            } else {
//                System.out.print("" + item);
                if (item == '#') {
                    loadTile(TILE_WALLS, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'T') {
                    loadTile(TILE_WALLS_TRAP, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'O') {
                    loadTile(TILE_WALLS_OTHER, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'W') {
                    loadTile(TILE_WINDOW, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'L') {
                    loadTile(TILE_LIGHT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'X') {
                    loadTile(TILE_ROOF_LIGHT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'G') {
                    loadTile(TILE_ROOF_LIGHT_GREEN, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'E') {
                    loadTile(TILE_EXIT_LEFT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'e') {
                    loadTile(TILE_EXIT_RIGHT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'D') {
                    loadTile(TILE_DOOR_LEFT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'd') {
                    loadTile(TILE_DOOR_RIGHT, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'k') {
                    loadTile(TILE_KEY, borderSize + startX + c, borderSize + startY + r);

                } else if (item == 'M') {
                    loadTile(TILE_MONSTER, borderSize + startX + c, borderSize + startY + r);

                } else if (item == ' ') {
//                    loadTile(TILE_BLANK, borderSize + startX + c, startY - r);
                }
                c++;
            }

        }

    }

    private void loadTile(int type, int x, int z) {

//        Spatial tile = null;
        log("Loading tile " + type + " at: " + x + ", " + z);
        if (type == TILE_BORDER) {
            blockTerrainControl.setBlock(x, 0, z, cubesTheme.getBlockDefinition("wall").getBlock());
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("wall").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("wall").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wall").getBlock());

        } else if (type == TILE_WALLS) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("wallBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("wall").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

        } else if (type == TILE_WALLS_TRAP) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("wallBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("wallTrap").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

        } else if (type == TILE_WALLS_OTHER) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("wallBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("wallOther").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

        } else if (type == TILE_KEY) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("key").getBlock());

            Sprite key = new Sprite(TYPE_PICKUP, 0.6f, 0.6f, 4, 4, 0, 0);
            key.setImage(cubesTheme.getTilesetFile());
            key.getMaterial().setFloat("AlphaDiscardThreshold", 0.55f);
            key.setLocalTranslation(x + 0.5f, 2f, z + 0.5f);
            key.addControl(new RotationControl(new Vector3f(0, -60, 0)));
            key.flipCoords(true);
            key.flipHorizontal(true);
            levelNode.attachChild(key);
            key.attachChild(baseApplication.getModelManager().getModel("Models/particles-key.j3o"));

            PointLight pl = new PointLight(new Vector3f(x + 0.5f, 2f, z + 0.5f));
            pl.setColor(ColorRGBA.Yellow.mult(2));
            pl.setRadius(3);
            levelNode.addControl(new LightControl(this, pl, false));

            KeyControl keyControl = new KeyControl(this, new Vector3f(x, 2, z), pl);
            key.addControl(keyControl);

        } else if (type == TILE_EXIT_LEFT) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("exitLeftBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("exitLeftTop").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTopExit").getBlock());

            Spatial trigger = SpatialUtils.addBox(levelNode, 0.55f, 1f, 0.55f);
            SpatialUtils.addColor(trigger, ColorRGBA.Pink, true);
            SpatialUtils.updateSpatialTransparency(trigger, true, 0.0f);
            trigger.setLocalTranslation(x + 0.5f, 2, z + 0.5f);

            ExitControl exitControl = new ExitControl(this, new Vector3f(x, 2, z), true);
            trigger.addControl(exitControl);

        } else if (type == TILE_EXIT_RIGHT) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("exitRightBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("exitRightTop").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTopExit").getBlock());

            Spatial trigger = SpatialUtils.addBox(levelNode, 0.55f, 1f, 0.55f);
            SpatialUtils.addColor(trigger, ColorRGBA.Pink, true);
            SpatialUtils.updateSpatialTransparency(trigger, true, 0.0f);
            trigger.setLocalTranslation(x + 0.5f, 2, z + 0.5f);

            ExitControl exitControl = new ExitControl(this, new Vector3f(x, 2, z), false);
            trigger.addControl(exitControl);

        } else if (type == TILE_WINDOW) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("wallBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("wallLight").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

        } else if (type == TILE_LIGHT) {
            PointLight pl = new PointLight(new Vector3f(x + 0.5f, 2.5f, z + 0.5f));
            pl.setColor(ColorRGBA.Yellow.mult(2));
            pl.setRadius(4);
            levelNode.addControl(new LightControl(this, pl, false));

        } else if (type == TILE_FLOOR) {
            blockTerrainControl.setBlock(x, 0, z, cubesTheme.getBlockDefinition("floor").getBlock());

        } else if (type == TILE_START) {
            blockTerrainControl.setBlock(x, 0, z, cubesTheme.getBlockDefinition("start").getBlock());

            PointLight pl = new PointLight(new Vector3f(x + 0.5f, 2, z + 0.5f));
            pl.setColor(ColorRGBA.White.mult(3));
            pl.setRadius(5);
            levelNode.addControl(new LightControl(this, pl, false));

        } else if (type == TILE_ROOF_LIGHT_GREEN) {
            blockTerrainControl.setBlock(x, 4, z, cubesTheme.getBlockDefinition("end").getBlock());
            PointLight pl = new PointLight(new Vector3f(x + 0.5f, 3, z + 0.5f));
            pl.setColor(ColorRGBA.White.mult(2));
            pl.setRadius(6);
            levelNode.addControl(new LightControl(this, pl, true));

        } else if (type == TILE_ROOF) {
            blockTerrainControl.setBlock(x, 4, z, cubesTheme.getBlockDefinition("roof").getBlock());

        } else if (type == TILE_ROOF_LIGHT) {
            blockTerrainControl.setBlock(x, 4, z, cubesTheme.getBlockDefinition("roofLight").getBlock());

            PointLight pl = new PointLight(new Vector3f(x + 0.5f, 3, z + 0.5f));
            pl.setColor(ColorRGBA.White.mult(2));
            pl.setRadius(4);
            levelNode.addControl(new LightControl(this, pl, FastMath.nextRandomInt(0, 1) == 0));

        } else if (type == TILE_DOOR_LEFT) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("exitLeftBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("exitLeftTop").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

            Spatial trigger = SpatialUtils.addBox(levelNode, 0.55f, 1f, 0.55f);
            SpatialUtils.addColor(trigger, ColorRGBA.Blue, true);
            SpatialUtils.updateSpatialTransparency(trigger, true, 0.0f);
            trigger.setLocalTranslation(x + 0.5f, 2, z + 0.5f);

            DoorControl doorControl = new DoorControl(this, new Vector3f(x, 2, z), true);
            trigger.addControl(doorControl);

        } else if (type == TILE_DOOR_RIGHT) {
            blockTerrainControl.setBlock(x, 1, z, cubesTheme.getBlockDefinition("exitRightBottom").getBlock());
            blockTerrainControl.setBlock(x, 2, z, cubesTheme.getBlockDefinition("exitRightTop").getBlock());
            blockTerrainControl.setBlock(x, 3, z, cubesTheme.getBlockDefinition("wallTop").getBlock());

            Spatial trigger = SpatialUtils.addBox(levelNode, 0.55f, 1f, 0.55f);
            SpatialUtils.addColor(trigger, ColorRGBA.Blue, true);
            SpatialUtils.updateSpatialTransparency(trigger, true, 0.0f);
            trigger.setLocalTranslation(x + 0.5f, 2, z + 0.5f);

            DoorControl doorControl = new DoorControl(this, new Vector3f(x, 2, z), false);
            trigger.addControl(doorControl);
            
        } else if (type == TILE_MONSTER) {
            blockTerrainControl.setBlock(x, 4, z, cubesTheme.getBlockDefinition("roof").getBlock());
            
            Spatial m = baseApplication.getModelManager().getModel("Models/ghost.j3o");
            m.setLocalScale(0.3f);
            SpatialUtils.updateSpatialTransparency(m, true, 0.65f);
            levelNode.attachChild(m);
            m.setLocalTranslation(x, 1.1f, z);
            
            m.addControl(new MonsterControl(this, new Vector3f(x, 1f, z)));

        } else if (type == TILE_BLANK) {
            blockTerrainControl.removeBlock(x, 1, z);
            blockTerrainControl.removeBlock(x, 2, z);
            blockTerrainControl.removeBlock(x, 3, z);

        }

    }

    public Timer getRoomLoadingTimer() {
        return roomLoadingTimer;
    }

}
