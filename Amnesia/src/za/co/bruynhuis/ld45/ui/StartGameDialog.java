/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
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
public class StartGameDialog extends PopupDialog {
    
    private Label message;
    private Button startButton; 
    
    public StartGameDialog(Window window) {
        super(window, "Interface/black.png", window.getWidth(), window.getHeight());
        
        setTitle("AMNESIA");
        setTitleColor(ColorRGBA.White);
        setTitleSize(40);
        title.centerTop(0, 50);
        
        message = new Label(this, "Welcome to my little game made for Ludum Dare 45.\n\n"
                + "Amnesia refers to the loss of memories, such as facts, "
                + "information and experiences. Though forgetting your identity "
                + "is a common plot device in movies and television, that's not "
                + "generally the case in real-life amnesia.\n "
                + "Instead, people with amnesia — also called amnestic syndrome — usually "
                + "know who they are. But, they may have trouble learning new information and"
                + " forming new memories.\n\n"
                + "...................", 20, 800, 600);
        message.setAlignment(TextAlign.CENTER);
        message.centerAt(0, -100);
   
        
        startButton = new Button(this, "start", "Play");
        startButton.centerBottom(0, 50);
        
    }

    @Override
    public void show() {
        super.show();
        message.fadeFromTo(0, 1, 1, 1, new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> bt) {
                window.getApplication().getInputManager().setCursorVisible(true);
            }
        });
        
    }
    
    public void addStartButtonListener(TouchButtonListener listener) {
        startButton.addTouchButtonListener(listener);
    }
}
