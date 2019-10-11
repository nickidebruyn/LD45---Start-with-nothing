/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game;

import com.bruynhuis.galago.games.cubes.CubesGame;
import com.bruynhuis.galago.games.cubes.CubesPlayer;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import za.co.bruynhuis.ld45.game.theme.DungeonTheme;

/**
 *
 * @author NideBruyn
 */
public class Player extends CubesPlayer {

    private CharacterControl playerControl;
    private Vector3f walkDirection = new Vector3f();
    private int keyCount = 0;

    public Player(CubesGame physicsGame) {
        super(physicsGame);
    }
    
    public boolean hasKey() {
        return keyCount > 0;
    }
    
    public void useKey() {
        keyCount --;        
        game.fireScoreChangedListener(getScore());
    }
    
    public void addKey() {
        keyCount ++;
        game.fireScoreChangedListener(getScore());
    }
    
    public int getKeyCount() {
        return keyCount;
    }

    @Override
    protected void init() {
        if (!DungeonTheme.observer) {
            playerControl = new CharacterControl(new CapsuleCollisionShape(DungeonTheme.tileSize*0.3f, DungeonTheme.tileSize * 1.5f), 0.05f);
//        playerControl.setJumpSpeed(2);
//        playerControl.setFallSpeed(2);
//        playerControl.setGravity(80);
            playerControl.setPhysicsLocation(game.getStartPosition().clone());
            game.getBaseApplication().getBulletAppState().getPhysicsSpace().add(playerControl);
        }

    }

    public void walk() {
        if (!DungeonTheme.observer) {
            if (walkDirection.equals(Vector3f.ZERO)) {
                game.getBaseApplication().getSoundManager().pauseMusic("walk");
            } else {
                game.getBaseApplication().getSoundManager().playMusic("walk");
            }
            
            playerControl.setWalkDirection(walkDirection);
        }
    }

    @Override
    public Vector3f getPosition() {
        if (!DungeonTheme.observer) {
            return playerControl.getPhysicsLocation();
        } else {
            return playerNode.getWorldTranslation();
        }

    }

    @Override
    public void doDie() {

    }

    public CharacterControl getPlayerControl() {
        return playerControl;
    }

    public void setPlayerControl(CharacterControl playerControl) {
        this.playerControl = playerControl;
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

}
