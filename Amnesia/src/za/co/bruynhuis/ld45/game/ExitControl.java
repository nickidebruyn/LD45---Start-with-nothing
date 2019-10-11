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

/**
 *
 * @author NideBruyn
 */
public class ExitControl extends AbstractControl {

    private Game game;
    private Vector3f location;
    private boolean leftDoor;
    private float activeDistance = 3;
    private float gameCompleteDistance = 0.6f;
    private boolean triggerDoor;
    private boolean doorOpen;

    public ExitControl(Game game, Vector3f location, boolean leftDoor) {
        this.game = game;
        this.location = location;
        this.leftDoor = leftDoor;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

            //Check if the player has reached the exit
            if (doorOpen && location.distance(game.getPlayer().getPosition()) < gameCompleteDistance) {
                game.doGameComplete();
            }

            //Check if the door must open or close.
            if (triggerDoor) {

                //If Door is open close it
                if (doorOpen) {
                    game.getBaseApplication().getSoundManager().playSound("door-close");
                    doorOpen = false;
                    if (leftDoor) {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftBottom").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftTop").getBlock());
                    } else {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightBottom").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightTop").getBlock());

                    }

                } else {
                    //If the door is closed open it.
                    doorOpen = true;
                    game.getBaseApplication().getSoundManager().playSound("door-open");
                    if (leftDoor) {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftBottomOpen").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitLeftTopOpen").getBlock());
                    } else {
                        game.getBlockTerrainControl().setBlock((int) location.x, 1, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightBottomOpen").getBlock());
                        game.getBlockTerrainControl().setBlock((int) location.x, 2, (int) location.z, game.getCubesTheme().getBlockDefinition("exitRightTopOpen").getBlock());

                    }
                    

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
            triggerDoor = true;
        }
    }

}
