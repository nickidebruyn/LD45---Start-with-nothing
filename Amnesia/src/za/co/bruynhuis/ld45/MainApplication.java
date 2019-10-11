/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45;

import com.bruynhuis.galago.app.Base3DApplication;
import com.bruynhuis.galago.games.cubes.AbstractCubesTheme;
import com.bruynhuis.galago.resource.EffectManager;
import com.bruynhuis.galago.resource.FontManager;
import com.bruynhuis.galago.resource.ModelManager;
import com.bruynhuis.galago.resource.ScreenManager;
import com.bruynhuis.galago.resource.SoundManager;
import com.bruynhuis.galago.resource.TextureManager;
import za.co.bruynhuis.ld45.game.theme.DungeonTheme;
import za.co.bruynhuis.ld45.screens.MenuScreen;
import za.co.bruynhuis.ld45.screens.PlayScreen;

/**
 *
 * @author NideBruyn
 */
public class MainApplication extends Base3DApplication {

    private AbstractCubesTheme theme;

    public static void main(String[] args) {
        new MainApplication();
    }

    public MainApplication() {
        super("amnesia", 1280, 720, "ld45.save", null, null, true);
    }

    @Override
    protected void preInitApp() {
        theme = new DungeonTheme(this);
        theme.load();
    }

    @Override
    protected void postInitApp() {
        showScreen(PlayScreen.NAME);
        cam.setFrustumPerspective(55f, (float)cam.getWidth() / cam.getHeight(), 0.1f, 60f);

    }

    @Override
    protected boolean isPhysicsEnabled() {
        return true;
    }

    @Override
    protected void initScreens(ScreenManager screenManager) {
        screenManager.loadScreen(MenuScreen.NAME, new MenuScreen());
        screenManager.loadScreen(PlayScreen.NAME, new PlayScreen());

    }

    @Override
    public void initModelManager(ModelManager modelManager) {
        modelManager.loadModel("Models/particles-key.j3o");
        modelManager.loadModel("Models/ghost.j3o");
    }

    @Override
    protected void initSound(SoundManager soundManager) {
        soundManager.loadSoundFx("dark-in-here", "Sounds/dark-in-here.ogg");
        soundManager.loadSoundFx("where", "Sounds/where.ogg");
        soundManager.loadSoundFx("need-key", "Sounds/need-key.ogg");
        soundManager.loadSoundFx("found-key", "Sounds/found-key.ogg");
        soundManager.loadSoundFx("complete", "Sounds/complete.ogg");
        soundManager.loadSoundFx("key", "Sounds/key.ogg");
        soundManager.loadSoundFx("door-open", "Sounds/door-open.ogg");
        soundManager.loadSoundFx("door-close", "Sounds/door-close.ogg");
        soundManager.loadSoundFx("gameover", "Sounds/gameover.ogg");
        
        soundManager.loadMusic("walk", "Sounds/walk.ogg");
        soundManager.setMusicVolume("walk", 0.5f);
        soundManager.setMusicSpeed("walk", 1.7f);
        
        soundManager.loadMusic("monster", "Sounds/monster1.ogg");
        soundManager.setMusicVolume("monster", 0.6f);
        
        soundManager.loadMusic("background", "Sounds/background.ogg");
        soundManager.setMusicVolume("background", 0.1f);
                
    }

    @Override
    protected void initEffect(EffectManager effectManager) {
        effectManager.loadEffect("pickup", "Models/pickup.j3o");
    }

    @Override
    protected void initTextures(TextureManager textureManager) {
    }

    @Override
    protected void initFonts(FontManager fontManager) {
    }

    public AbstractCubesTheme getTheme() {
        return theme;
    }

}
