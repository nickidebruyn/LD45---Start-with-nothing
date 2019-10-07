/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import za.co.bruynhuis.ld45.screens.PlayScreen;

/**
 *
 * @author NideBruyn
 */
public class DoorControl extends AbstractControl {

    private Game game;
    private Player player;
    private Vector3f location;
    private boolean leftDoor;
    private float activeDistance = 3;
    private boolean triggerDoor;
    private boolean doorOpen;

    public DoorControl(Game game, Vector3f location, boolean leftDoor) {
        this.game = game;
        this.location = location;
        this.leftDoor = leftDoor;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

            if (player == null) {
                player = (Player) game.getPlayer();
            }

            //Check if the door must open or close.
            if (triggerDoor) {

                //If Door is open close it
                if (!doorOpen) {
                    //If the door is closed open it.
                    doorOpen = true;
                    System.out.println("OPENING THE DOOR");
                    game.getBaseApplication().getSoundManager().playSound("door-open");
                    if (leftDoor) {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftBottomOpen").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftTopOpen").getBlock());
                    } else {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightBottomOpen").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightTopOpen").getBlock());

                    }
                    player.useKey();

                }

                triggerDoor = false;
            }

        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    public void triggerDoor() {
        if (!triggerDoor && location.distance(game.getPlayer().getPosition()) <= activeDistance) {

            if (!doorOpen) {
                if (player.hasKey()) {
                    triggerDoor = true;
                } else {
                    game.getBaseApplication().getSoundManager().playSound("need-key");
                    game.getBaseApplication().getMessageManager().sendMessage(PlayScreen.DISPLAY_MESSAGE, "I need a key to open this door!");
                }

            }

        }
    }

}
