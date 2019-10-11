/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Circ;
import com.bruynhuis.galago.control.camera.CameraMovementControl;
import com.bruynhuis.galago.filters.FXAAFilter;
import com.bruynhuis.galago.games.cubes.CubesGameListener;
import com.bruynhuis.galago.listener.CameraPickListener;
import com.bruynhuis.galago.listener.PickEvent;
import com.bruynhuis.galago.listener.PickListener;
import com.bruynhuis.galago.messages.MessageListener;
import com.bruynhuis.galago.screen.AbstractScreen;
import com.bruynhuis.galago.ui.Image;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.SpriteWidget;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.button.TouchStick;
import com.bruynhuis.galago.ui.listener.TouchButtonAdapter;
import com.bruynhuis.galago.ui.listener.TouchStickAdapter;
import com.jme3.app.FlyCamAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import za.co.bruynhuis.ld45.MainApplication;
import za.co.bruynhuis.ld45.game.DoorControl;
import za.co.bruynhuis.ld45.game.ExitControl;
import za.co.bruynhuis.ld45.game.Game;
import za.co.bruynhuis.ld45.game.KeyControl;
import za.co.bruynhuis.ld45.game.Player;
import za.co.bruynhuis.ld45.game.theme.DungeonTheme;
import za.co.bruynhuis.ld45.ui.GameCompletedDialog;
import za.co.bruynhuis.ld45.ui.GameoverDialog;
import za.co.bruynhuis.ld45.ui.MessageCallback;
import za.co.bruynhuis.ld45.ui.StartGameDialog;

/**
 *
 * @author NideBruyn
 */
public class PlayScreen extends AbstractScreen implements CubesGameListener, ActionListener, PickListener, MessageListener {

    public static final String NAME = "PlayScreen";
    public static final String DISPLAY_MESSAGE = "DISPLAY_MESSAGE";
    private Label title;
    private Image crosshair;
    private Image eyeTop;
    private Image eyeBottom;
    private Label messageLabel;
    private SpriteWidget keyImage;
    private Label keyLabel;
    private GameoverDialog gameoverDialog;
    private GameCompletedDialog gameCompletedDialog;
    private StartGameDialog startGameDialog;
    private boolean firstLoad = true;

    private MainApplication mainApplication;
    private FlyCamAppState flyCamAppState;
    private TouchStick moveTouchStick;
    private TouchStick lookTouchStick;
    private CameraMovementControl cameraMovementControl;
    private float movePercentage = 0.5f;
    private float lookPercentage = 0.025f;

    private Game game;
    private Player player;
    private boolean[] arrowKeys = new boolean[4];
    private CameraPickListener cameraPickListener;

    private Vector3f lightDirection = new Vector3f(-0.8f, -0.4f, -0.8f).normalizeLocal();
    private DirectionalLight directionalLight;
    private AmbientLight ambientLight;
    private DirectionalLightShadowRenderer directionalLightShadowRenderer;
    private FilterPostProcessor fpp;

    @Override
    protected void init() {

//        title = new Label(hudPanel, "AMNESIA", 54);
//        title.centerTop(0, 20);
        eyeTop = new Image(hudPanel, "Interface/eye.png", window.getWidth(), window.getHeight());
        eyeTop.centerAt(0, window.getHeight() * 0.5f);

        eyeBottom = new Image(hudPanel, "Interface/eye.png", window.getWidth(), window.getHeight());
        eyeBottom.rotate(FastMath.DEG_TO_RAD * 180);
        eyeBottom.centerAt(0, -window.getHeight() * 0.5f);

        crosshair = new Image(hudPanel, "Interface/crosshair.png", 32, 32, true);
        crosshair.center();

        keyImage = new SpriteWidget(hudPanel, "Textures/dungeon.png", 50, 50, 4, 4, 0, 0, true);
        keyImage.leftTop(10, 10);

        keyLabel = new Label(hudPanel, "x 0", 24, 200, 50);
        keyLabel.leftTop(70, 10);
        keyLabel.setAlignment(TextAlign.LEFT);

        messageLabel = new Label(hudPanel, "Some important message to show!", 20, 800, 50);
        messageLabel.setTextColor(ColorRGBA.White);

        moveTouchStick = new TouchStick(hudPanel, "movement-stick", 300, 300);
        moveTouchStick.leftBottom(0, 0);
        moveTouchStick.addTouchStickListener(new TouchStickAdapter() {
            @Override
            public void doLeft(float x, float y, float distance) {
//                cameraMovementControl.setStrafeSpeed(distance * movePercentage);
//                cameraMovementControl.setStrafeLeft(true);
//                cameraMovementControl.setStrafeRight(false);
                arrowKeys[1] = false;
                arrowKeys[3] = true;

            }

            @Override
            public void doRight(float x, float y, float distance) {
//                cameraMovementControl.setStrafeSpeed(distance * movePercentage);
//                cameraMovementControl.setStrafeLeft(false);
//                cameraMovementControl.setStrafeRight(true);
                arrowKeys[1] = true;
                arrowKeys[3] = false;

            }

            @Override
            public void doUp(float x, float y, float distance) {
//                cameraMovementControl.setMoveSpeed(distance * movePercentage);
//                cameraMovementControl.setForward(true);
//                cameraMovementControl.setBackward(false);
                arrowKeys[0] = true;
                arrowKeys[2] = false;

            }

            @Override
            public void doDown(float x, float y, float distance) {
//                cameraMovementControl.setMoveSpeed(distance * movePercentage);
//                cameraMovementControl.setForward(false);
//                cameraMovementControl.setBackward(true);
                arrowKeys[0] = false;
                arrowKeys[2] = true;

            }

            @Override
            public void doRelease(float x, float y) {
                cameraMovementControl.setMoveSpeed(0);
                cameraMovementControl.setForward(false);
                cameraMovementControl.setBackward(false);
                cameraMovementControl.setStrafeLeft(false);
                cameraMovementControl.setStrafeRight(false);

                arrowKeys[0] = false;
                arrowKeys[1] = false;
                arrowKeys[2] = false;
                arrowKeys[3] = false;

            }
        });

        lookTouchStick = new TouchStick(hudPanel, "look-stick", 300, 300);
        lookTouchStick.rightBottom(0, 0);
        lookTouchStick.addTouchStickListener(new TouchStickAdapter() {
            @Override
            public void doLeft(float x, float y, float distance) {
                cameraMovementControl.setLookSpeed(distance * lookPercentage);
                cameraMovementControl.setLeft(true);
                cameraMovementControl.setRight(false);

            }

            @Override
            public void doRight(float x, float y, float distance) {
                cameraMovementControl.setLookSpeed(distance * lookPercentage);
                cameraMovementControl.setLeft(false);
                cameraMovementControl.setRight(true);

            }

            @Override
            public void doUp(float x, float y, float distance) {
                cameraMovementControl.setLookSpeed(distance * lookPercentage);
                cameraMovementControl.setUp(true);
                cameraMovementControl.setDown(false);

            }

            @Override
            public void doDown(float x, float y, float distance) {
                cameraMovementControl.setLookSpeed(distance * lookPercentage);
                cameraMovementControl.setUp(false);
                cameraMovementControl.setDown(true);

            }

            @Override
            public void doRelease(float x, float y) {
                cameraMovementControl.setLookSpeed(0);
                cameraMovementControl.setLeft(false);
                cameraMovementControl.setRight(false);
                cameraMovementControl.setUp(false);
                cameraMovementControl.setDown(false);

            }
        });

        startGameDialog = new StartGameDialog(window);
        startGameDialog.addStartButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    beginGame();
                    startGameDialog.hide();

                }
            }

        });

        gameoverDialog = new GameoverDialog(window);
        gameoverDialog.addExitButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    exitScreen();
                }
            }

        });
        gameoverDialog.addRetryButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(PlayScreen.NAME);
                }
            }

        });

        gameCompletedDialog = new GameCompletedDialog(window);
        gameCompletedDialog.addExitButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    exitScreen();
                }
            }

        });
        gameCompletedDialog.addRetryButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(PlayScreen.NAME);
                }
            }

        });

        cameraPickListener = new CameraPickListener(camera, rootNode);
        cameraPickListener.setPickListener(this);

        baseApplication.getMessageManager().addMessageListener(this);
    }

    private void blinkEyes(int blinkCount, TweenCallback callback) {
        eyeTop.moveFromToCenter(0, window.getHeight() * 0.3f, 0, window.getHeight(), 1f, 0.5f, Circ.INOUT, blinkCount, true);
        eyeBottom.moveFromToCenter(0, -window.getHeight() * 0.3f, 0, -window.getHeight(), 1f, 0.5f, Circ.INOUT, blinkCount, true, callback);

    }

    private void initControls() {
        inputManager.addMapping("move_left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("move_right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("move_up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("move_down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "move_left");
        inputManager.addListener(this, "move_right");
        inputManager.addListener(this, "move_up");
        inputManager.addListener(this, "move_down");
        inputManager.addListener(this, "jump");
    }

    private void registerEnvironment() {
        directionalLight = new DirectionalLight();
        directionalLight.setDirection(lightDirection);
        directionalLight.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
//        rootNode.addLight(directionalLight);

        ambientLight = new AmbientLight(ColorRGBA.Gray);
        rootNode.addLight(ambientLight);

        directionalLightShadowRenderer = new DirectionalLightShadowRenderer(baseApplication.getAssetManager(), 1024, 1);
        directionalLightShadowRenderer.setLight(directionalLight);
        directionalLightShadowRenderer.setShadowIntensity(0.2f);
//        baseApplication.getViewPort().addProcessor(directionalLightShadowRenderer);

        fpp = new FilterPostProcessor(baseApplication.getAssetManager());
        baseApplication.getViewPort().addProcessor(fpp);

        FogFilter fogFilter = new FogFilter();
        fogFilter.setFogColor(ColorRGBA.Black);
        fogFilter.setFogDensity(5f);
        fogFilter.setFogDistance(800);
        fpp.addFilter(fogFilter);

        FXAAFilter fxaaf = new FXAAFilter();
        fpp.addFilter(fxaaf);

        BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        bf.setBloomIntensity(1.0f);
        fpp.addFilter(bf);

        SSAOFilter ssaof = new SSAOFilter();
        ssaof.setSampleRadius(1.2f);
        ssaof.setIntensity(2.5f);
        ssaof.setScale(0.28f);
        ssaof.setBias(0.05f);
//        fpp.addFilter(ssaof);

    }

    private void unregisterEnvironment() {
        if (directionalLight != null) {
            rootNode.removeLight(directionalLight);
            rootNode.removeLight(ambientLight);
        }

        if (directionalLightShadowRenderer != null) {
            baseApplication.getViewPort().removeProcessor(directionalLightShadowRenderer);
        }

        if (fpp != null) {
            baseApplication.getViewPort().removeProcessor(fpp);
        }

    }

    @Override
    protected void load() {

        mainApplication = (MainApplication) baseApplication;

        arrowKeys = new boolean[4];

        game = new Game(mainApplication, rootNode, mainApplication.getTheme());
        game.load();

        player = new Player(game);
        player.load();

        game.addGameListener(this);

        if (DungeonTheme.observer) {
            camera.setLocation(new Vector3f(((DungeonTheme) mainApplication.getTheme()).getMapTotalWidth() * 0.5f,
                    60, ((DungeonTheme) mainApplication.getTheme()).getMapTotalHeight() * 0.65f));
            camera.lookAt(new Vector3f(((DungeonTheme) mainApplication.getTheme()).getMapTotalWidth() * 0.5f,
                    0, ((DungeonTheme) mainApplication.getTheme()).getMapTotalHeight() * 0.65f - DungeonTheme.tileSize), Vector3f.UNIT_Y);

        } else {

            registerEnvironment();

            camera.setLocation(game.getStartPosition().clone());
            camera.lookAt(new Vector3f(mainApplication.getTheme().getCubesSettings().getChunkSizeX() * 0.5f,
                    DungeonTheme.tileSize * 5 * 0.5f,
                    mainApplication.getTheme().getCubesSettings().getChunkSizeZ() * 0.5f), Vector3f.UNIT_Y);

            if (isScreenControlsActive()) {
                cameraMovementControl = new CameraMovementControl(camera);
                cameraMovementControl.setStrafeSpeed(0);
                cameraMovementControl.setMoveSpeed(0);
                cameraMovementControl.setLookSpeed(0);

            } else {
                flyCamAppState = new FlyCamAppState();
                baseApplication.getStateManager().attach(flyCamAppState);

            }

        }

    }

    private boolean isScreenControlsActive() {
        return baseApplication.isMobileApp();
    }

    private void beginGame() {
        inputManager.setCursorVisible(false);
        baseApplication.getSoundManager().playMusic("background");
        baseApplication.getSoundManager().playSound("dark-in-here");

        showMessageToPlayer("Why is it so dark in here?", 3, new MessageCallback() {

            @Override
            public void callback() {

                game.start(player);

                //Blink eyes
                blinkEyes(4, new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> bt) {

                        baseApplication.getSoundManager().playSound("where");
                        showMessageToPlayer("Where am I, how did I get here?", 3, null);
                        crosshair.show();

                        if (isScreenControlsActive()) {
                            moveTouchStick.show();
                            lookTouchStick.show();
                            rootNode.addControl(cameraMovementControl);
                            cameraPickListener.registerWithInput(inputManager);

                        } else {
                            moveTouchStick.hide();
                            lookTouchStick.hide();
                            initControls();

                            cameraPickListener.registerWithInput(inputManager);

                            if (flyCamAppState != null) {
                                flyCamAppState.getCamera().setMoveSpeed(5);
                                flyCamAppState.getCamera().setRotationSpeed(3);
                            }

                        }

                    }
                });
            }
        });
    }

    @Override
    protected void show() {

        setPreviousScreen(null);
        messageLabel.hide();
        moveTouchStick.hide();
        lookTouchStick.hide();
        mainApplication.showStats();

        crosshair.hide();
        if (!DungeonTheme.observer) {

            if (firstLoad) {
                startGameDialog.show();
                firstLoad = false;

            } else {
                beginGame();
            }

        } else {
            game.start(player);
        }

    }

    @Override
    protected void exit() {
        if (flyCamAppState != null) {
            cameraPickListener.unregisterInput();
            baseApplication.getStateManager().detach(flyCamAppState);
        }

        if (cameraMovementControl != null) {
            rootNode.removeControl(cameraMovementControl);
        }

        unregisterEnvironment();
        baseApplication.getSoundManager().stopMusic("background");
        game.close();
    }

    @Override
    public void update(float tpf) {
        if (isActive()) {

            if (game.isStarted() && !game.isGameover() && !game.isPaused()) {

                if (!DungeonTheme.observer) {
                    float playerMoveSpeed = ((DungeonTheme.tileSize * 5f) * tpf);
                    Vector3f camDir = camera.getDirection().mult(playerMoveSpeed);
                    Vector3f camLeft = camera.getLeft().mult(playerMoveSpeed);
                    player.getWalkDirection().set(0, 0, 0);
                    if (arrowKeys[0]) {
                        player.getWalkDirection().addLocal(camDir);
                    }
                    if (arrowKeys[1]) {
                        player.getWalkDirection().addLocal(camLeft.negate());
                    }
                    if (arrowKeys[2]) {
                        player.getWalkDirection().addLocal(camDir.negate());
                    }
                    if (arrowKeys[3]) {
                        player.getWalkDirection().addLocal(camLeft);
                    }
                    player.getWalkDirection().setY(0);
                    player.walk();
                    camera.setLocation(player.getPosition().add(0, 0.25f, 0));
                }

//                log("Camera at :" + camera.getLocation());
            }

        }
    }

    @Override
    public void onAction(String actionName, boolean value, float lastTimePerFrame) {
        if (actionName.equals("move_up")) {
            arrowKeys[0] = value;
        } else if (actionName.equals("move_right")) {
            arrowKeys[1] = value;
        } else if (actionName.equals("move_left")) {
            arrowKeys[3] = value;
        } else if (actionName.equals("move_down")) {
            arrowKeys[2] = value;
        }
    }

    @Override
    protected void pause() {
    }

    @Override
    public void doGameOver() {
        baseApplication.getSoundManager().stopMusic("walk");
        gameoverDialog.show();

    }

    @Override
    public void doGameCompleted() {
        log("Well done, you completed the level");
        baseApplication.getSoundManager().stopMusic("walk");
        baseApplication.getSoundManager().playSound("complete");
        if (flyCamAppState != null) {
            cameraPickListener.unregisterInput();
            baseApplication.getStateManager().detach(flyCamAppState);
        }

        gameCompletedDialog.show();

    }

    @Override
    public void doScoreChanged(int score) {
        keyLabel.setText("x " + player.getKeyCount());

    }

    @Override
    public void doCollisionPlayerWithStatic(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionPlayerWithPickup(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionPlayerWithEnemy(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionPlayerWithBullet(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionObstacleWithBullet(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionEnemyWithBullet(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionEnemyWithEnemy(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionPlayerWithObstacle(Spatial collided, Spatial collider) {

    }

    @Override
    public void doCollisionEnemyWithObstacle(Spatial collided, Spatial collider) {

    }

    @Override
    public void picked(PickEvent pickEvent, float tpf) {
        if (isActive()) {

            if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

                if (pickEvent.isKeyDown()) {
                    log("You picked at: " + pickEvent.getContactPoint());
                    log("Your selection: " + pickEvent.getContactObject().getName());

                    if (pickEvent.getContactObject() != null) {
                        if (pickEvent.getContactObject().getControl(ExitControl.class) != null) {
                            log("Trigger the exit door");
                            pickEvent.getContactObject().getControl(ExitControl.class).triggerDoor();
                        }
                        if (pickEvent.getContactObject().getControl(DoorControl.class) != null) {
                            log("Trigger the room door");
                            pickEvent.getContactObject().getControl(DoorControl.class).triggerDoor();
                        }
                        if (pickEvent.getContactObject().getName().startsWith(Game.TYPE_PICKUP)) {

                            if (pickEvent.getContactObject().getParent().getControl(KeyControl.class) != null) {
                                log("Trigger the KeyControl");
                                pickEvent.getContactObject().getParent().getControl(KeyControl.class).trigger();
                            }
                        }

                    }

//                    Vector3Int blockLocation = BlockNavigator.getPointedBlockLocation(game.getBlockTerrainControl(), pickEvent.getContactPoint(), false);
//                    if (blockLocation != null) {
//                        Block b = game.getBlockTerrainControl().getBlock(blockLocation);
//                        if (b != null) {
//                            log("You selected block: " + b);
//                            
//                        }
//                        
//                    }
                }

            }

        }
    }

    @Override
    public void drag(PickEvent pickEvent, float tpf) {

    }

    @Override
    public void messageReceived(String message, Object object) {

        if (message != null) {

            if (message.equals(DISPLAY_MESSAGE)) {
                showMessageToPlayer((String) object, 2, null);

            }

        }

    }

    protected void showMessageToPlayer(String message, float time, final MessageCallback callback) {
        messageLabel.setText(message);
        messageLabel.show();
        messageLabel.moveFromToCenter(0, -window.getHeight() * 0.6f, 0, -100, 0.5f, 0);
        messageLabel.fadeFromTo(1, 0, 1, time, new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> bt) {
                messageLabel.hide();
                if (callback != null) {
                    callback.callback();
                }

            }
        });
    }
}
