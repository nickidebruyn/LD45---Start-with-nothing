/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author NideBruyn
 */
public class KeyControl extends AbstractControl {

    private Game game;
    private Player player;
    private Vector3f location;
    private PointLight pointLight;
    private float activeDistance = 3;
    private boolean trigger;

    public KeyControl(Game game, Vector3f location, PointLight light) {
        this.game = game;
        this.location = location;
        this.pointLight = light;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

            if (player == null) {
                player = (Player) game.getPlayer();
            }

            //Check if the key can be picked up
            if (trigger) {
                player.addKey();
                game.getBaseApplication().getSoundManager().playSound("found-key");
                game.getBaseApplication().getSoundManager().playSound("key");
                game.getBaseApplication().getEffectManager().doEffect("pickup", spatial.getWorldTranslation().clone());
                spatial.removeFromParent();
                game.getLevelNode().removeLight(pointLight);
                trigger = false;
            }

        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    public void trigger() {
        if (!trigger && location.distance(game.getPlayer().getPosition()) <= activeDistance) {
            trigger = true;

        }
    }

}
