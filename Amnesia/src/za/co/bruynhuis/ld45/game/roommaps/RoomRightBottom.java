/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game.roommaps;

import com.jme3.math.FastMath;

/**
 *
 * @author NideBruyn
 */
public class RoomRightBottom {
    
    public static String generateRandomRoom() {
        String roomStr = null;
        int index = FastMath.nextRandomInt(0, 1);
        
        if (index == 0) {
            roomStr = "####################\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#  ########        #\n"
                    + "#        ##        #\n"
                    + "#        ##        #\n"
                    + "#    X   ##        #\n"
                    + "#        ##        #\n"
                    + "#        ##        #\n"
                    + "###########         \n"
                    + "###########         \n"
                    + "###########        #\n"
                    + "#        ##        #\n"
                    + "#        ##        #\n"
                    + "#    X   ##        #\n"
                    + "#        ##        #\n"
                    + "#        ##        #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#########  #########";
            
        } else if (index == 1) {
            roomStr = "################W###\n"
                    + "#               L  #\n"
                    + "#                  #\n"
                    + "#     #########    #\n"
                    + "#     #   #   #    #\n"
                    + "#     #   #####    #\n"
                    + "#     #  M         #\n"
                    + "#     #            #\n"
                    + "#     ##############\n"
                    + "#                   \n"
                    + "#                   \n"
                    + "#   ##########D#####\n"
                    + "#            #     #\n"
                    + "#            #     #\n"
                    + "#   ######   #     #\n"
                    + "#   #    #   #     #\n"
                    + "#   #    #   #     #\n"
                    + "#   ######   ####  #\n"
                    + "#k                 #\n"
                    + "#########  #########";
            
        }
        
        return roomStr;
    }
    
}
