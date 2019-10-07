/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.screens;

import com.bruynhuis.galago.screen.AbstractScreen;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.listener.TouchButtonAdapter;
import za.co.bruynhuis.ld45.ui.Button;

/**
 *
 * @author NideBruyn
 */
public class MenuScreen extends AbstractScreen {

    public static final String NAME = "MenuScreen";
    private Label title;
    private Button playButton;

    @Override
    protected void init() {
        title = new Label(hudPanel, "amnesia", 42);
        title.centerTop(0, 0);

        playButton = new Button(hudPanel, "play", "Start Game");
        playButton.center();
        playButton.addTouchButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(PlayScreen.NAME);

                }
            }

        });
    }

    @Override
    protected void load() {
    }

    @Override
    protected void show() {
    }

    @Override
    protected void exit() {
    }

    @Override
    protected void pause() {
    }

}
