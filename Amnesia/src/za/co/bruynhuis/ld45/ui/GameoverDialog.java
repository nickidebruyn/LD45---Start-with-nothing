/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.ui;

import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.listener.TouchButtonListener;
import com.bruynhuis.galago.ui.panel.PopupDialog;
import com.bruynhuis.galago.ui.window.Window;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author NideBruyn
 */
public class GameoverDialog extends PopupDialog {
    
    private Label message;
    private Button exitButton;
    private Button retryButton; 
    
    public GameoverDialog(Window window) {
        super(window, "Interface/black.png", window.getWidth(), window.getHeight());
        
        setTitle("GAME OVER");
        setTitleColor(ColorRGBA.Red);
        setTitleSize(40);
        title.centerTop(0, 50);
        
        message = new Label(this, "You just lost your memory again.\n\n"
                + "Would you like to wake up and try the next game?\n\n"
                + "Please note that every time you play you get a new level.", 20, 800, 600);
        message.setAlignment(TextAlign.CENTER);
        message.centerAt(0, -50);
        
        exitButton = new Button(this, "exit", "Let me out!");
        exitButton.leftBottom(100, 50);
        
        retryButton = new Button(this, "retry", "Try Again");
        retryButton.rightBottom(100, 50);
        
    }

    @Override
    public void show() {
        super.show();
//        moveFromToCenter(0, window.getHeight() * 0.6f, 0, 0, 1f, 0);
        fadeFromTo(0, 1, 2, 1);
        window.getApplication().getInputManager().setCursorVisible(true);
    }
    
    public void addExitButtonListener(TouchButtonListener listener) {
        exitButton.addTouchButtonListener(listener);
    }
    
    public void addRetryButtonListener(TouchButtonListener listener) {
        retryButton.addTouchButtonListener(listener);
    }
}
