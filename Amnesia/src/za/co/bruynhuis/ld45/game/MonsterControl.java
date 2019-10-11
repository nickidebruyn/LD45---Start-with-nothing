/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author NideBruyn
 */
public class MonsterControl extends AbstractControl {

    private Game game;
    private Vector3f location;
    private float activeDistance = 5;
    private float killDistance = 0.5f;
    private boolean active;
    private float distanceBetweenCharacterAndTarget = 0;
    private float moveSpeed = 2.0f;
    private Quaternion targetRotation = Quaternion.IDENTITY;
    private float minWalkDistance = 0.1f;
    private Vector3f clearVec = new Vector3f(1, 0, 1);

    public MonsterControl(Game game, Vector3f location) {
        this.game = game;
        this.location = location;

    }

    @Override
    protected void controlUpdate(float tpf) {
        if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

            //Check if the player has reached the enemy
            if (spatial.getWorldTranslation().mult(clearVec).distance(game.getPlayer().getPosition().mult(clearVec)) < killDistance) {
                game.getBaseApplication().getSoundManager().playSound("gameover");
                game.doGameOver();
            }

            //Check if the player is in the monsters range
            if (spatial.getWorldTranslation().mult(clearVec).distance(game.getPlayer().getPosition().mult(clearVec)) <= activeDistance) {
                if (!active) {
                    active = true;
                    game.getBaseApplication().getSoundManager().playMusic("monster");

                }

                if (active) {
                    //Move towards the player
                    moveTowardsPlayer(tpf);

                }

            } else {
                active = false;
                game.getBaseApplication().getSoundManager().stopMusic("monster");
            }

        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    private void moveTowardsPlayer(float tpf) {
        //Calculate the character movement
        distanceBetweenCharacterAndTarget = spatial.getWorldTranslation().mult(clearVec).distance(game.getPlayer().getPosition().mult(clearVec));
        if (distanceBetweenCharacterAndTarget > minWalkDistance) {

            //Move the character towards the target
            //1. start rotating character towards target each frame
//            characterNode.lookAt(targetMarker.getLocalTranslation(), Vector3f.UNIT_Y);
            targetRotation.lookAt(game.getPlayer().getPosition().mult(clearVec).subtract(spatial.getWorldTranslation().mult(clearVec)).normalize(), Vector3f.UNIT_Y);
            spatial.getLocalRotation().slerp(targetRotation, 0.1f);

            //2. move in the direction the character is facing
            spatial.move(spatial.getLocalRotation().getRotationColumn(2).normalize().mult(moveSpeed * tpf));

        }
    }

}
