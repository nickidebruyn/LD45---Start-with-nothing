/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.bruynhuis.galago.util.Timer;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author NideBruyn
 */
public class LightControl extends AbstractControl {

    private Game game;
    private PointLight pointLight;
    private float activeDistance = 15;
    private boolean attached = false;
    private boolean flicker = false;
    private Timer flickerTimer = new Timer(10);

    public LightControl(Game game, PointLight pointLight, boolean flicker) {
        this.game = game;
        this.pointLight = pointLight;
        this.flicker = flicker;
    }

    @Override
    protected void controlUpdate(float tpf) {

        if (game.isStarted() && !game.isPaused() && !game.isGameover()) {

            if (pointLight.getPosition().distance(game.getPlayer().getPosition()) < activeDistance) {
                if (!attached) {
                    game.getLevelNode().addLight(pointLight);
                    attached = true;
                    if (flicker) {
                        flickerTimer.start();
                    }
                }

            } else {
                
                if (attached) {
                    game.getLevelNode().removeLight(pointLight);
                    attached = false;
                    
                }

            }

            //Check for flickering
            if (attached && flicker) {
                flickerTimer.update(tpf);
                
                if (flickerTimer.finished()) {
                    pointLight.setEnabled(!pointLight.isEnabled());
                    flickerTimer.setMaxTime(FastMath.nextRandomInt(10, 100));
                    flickerTimer.reset();
                }
                
            }
            
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

}
